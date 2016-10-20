package view;

import webcontentmanager.WebContentManagerFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.json.JSONException;
import tasks.ButtonLockTask;
import tasks.DownloadTask;
import webcontentmanager.DirectLinksSearcher;
import webcontentmanager.SoundCloudSearcher;
import webcontentmanager.YouTubeSearcher;

/**
 *
 * @author Mateusz Gołosz
 */
public class WindowManager {

    private ArrayList<String[]> searchResults;

    private boolean locker;
    private String artist;
    private JRadioButton customLocation, defaultLocation;
    private JCheckBox saveOriginalFormat, createAlbumFolder;
    private JTextField folderLocation;
    
    private String getLocation(){
        String location;
        if (customLocation.isSelected()){
            location = folderLocation.getText();
        } else {
            location = System.getProperty("user.home") + "\\downloads\\";
        }
        return location;
    }
    
    

    public void populateSearchTable(String title, JTextField artistField, JTable searchTable, JTable downloadTable) {
        DefaultTableModel model = (DefaultTableModel) searchTable.getModel();
        model.setNumRows(0);
        if (title == null || title.isEmpty()) {
            return;
        }
        YouTubeSearcher youTubeSearcher = WebContentManagerFactory.getInstance().getYouTubeSearcher();
        SoundCloudSearcher soundCloudSearcher = WebContentManagerFactory.getInstance().getSoundCloudSearcher();
        try {
            if (artistField.getText() == null || artistField.getText().isEmpty()) {
                try {
                    artist = WebContentManagerFactory.getInstance().getLastfmAPI().getArtist(title);
                } catch (IOException | JSONException e) {
                    artist = WebContentManagerFactory.getInstance().getSpotifyAPI().getArtist(title);
                }
                artistField.setText(artist);
            } else {
                artist = artistField.getText();
            }
            this.searchResults = youTubeSearcher.searchLinks(artist.trim() + " " + title.trim());
            searchResults.addAll(soundCloudSearcher.searchLinks(artist.trim() + " " + title.trim()));

            for (String[] elem : this.searchResults) {
                model.addRow(new Object[]{elem[1], elem[2], "Go"});
            }
            new SearchButtonEditor(searchTable, 2, this, downloadTable, searchResults);

            if (searchTable.getMouseListeners().length > 2) {
                searchTable.removeMouseListener(searchTable.getMouseListeners()[2]);
            }
            searchTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (locker == true) {
                        return;
                    }
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    if (column == 2) {
                        String source = target.getValueAt(row, 1).toString();
                        populateDownloadTable(target.getValueAt(row, 0).toString(), source, "Test", downloadTable);
                        try {
                            int rowDownload = downloadTable.getModel().getRowCount(); 
                            new Thread(new DownloadTask(getLocation(), searchResults.get(row)[0], artist.trim(), title.trim(), downloadTable.getModel(), rowDownload, source, saveOriginalFormat.isSelected(), createAlbumFolder.isSelected())).start();
                        } catch (IOException ex) {
                            Logger.getLogger(SearchButtonEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JSONException ex) {
                            Logger.getLogger(SearchButtonEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if (locker == true) {
                        return;
                    }
                    locker = true;
                    Timer timer = new Timer();
                    ButtonLockTask buttonLockTask = new ButtonLockTask(false, WindowManager.this);
                    timer.schedule(buttonLockTask, 1000L);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            model.addRow(new Object[]{"Something went wrong, please try again"});
        }
    }

    public void populateDownloadTable(String file, String source, String format, JTable downloadTable) {
        DefaultTableModel model = (DefaultTableModel) downloadTable.getModel();
        if (file == null || file.isEmpty()) {
            return;
        }
        model.addRow(new Object[]{0, file, source, format});
        downloadTable.getColumnModel().getColumn(0).setCellRenderer(new ProgressCellRender());
    }

    public void setLocker(Boolean locker) {
        this.locker = locker;
    }

    public void downloadAll(String url, JTable downloadTable) {
        System.out.println("LOCATION " + getLocation() + " ORIGINALFORMAT:" + this.saveOriginalFormat.isSelected() + " createALBUM: " + this.createAlbumFolder.isSelected());
        if (url == null || url.isEmpty()) {
            return;
        }

        
        ArrayList<String[]> results = null;
        try {
            DirectLinksSearcher directLinkSearcher = WebContentManagerFactory.getInstance().getDirectLinksSearcher();
            results = directLinkSearcher.searchLinks(url);
            if (results != null) {
                for (String[] elem : results) {
                    populateDownloadTable(elem[1], elem[0], "Test", downloadTable);
                    int row = downloadTable.getModel().getRowCount(); 
                    new Thread(new DownloadTask(getLocation(), elem[0], "test1", "test2", downloadTable.getModel(), row, "direct", saveOriginalFormat.isSelected(), createAlbumFolder.isSelected())).start();
                }
            }
        } catch (IOException | JSONException ex) {
            Logger.getLogger(WindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void chooseFolder(JTextField location) {
        //int result;
        String choosertitle = "Select download folder";

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            location.setText(chooser.getSelectedFile().toString());
        }
    }

    public void setCustomLocation(JRadioButton customLocation) {
        this.customLocation = customLocation;
    }

    public void setDefaultLocation(JRadioButton defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public void setSaveOriginalFormat(JCheckBox saveOriginalFormat) {
        this.saveOriginalFormat = saveOriginalFormat;
    }

    public void setCreateAlbumFolder(JCheckBox createAlbumFolder) {
        this.createAlbumFolder = createAlbumFolder;
    }

    public void setFolderLocation(JTextField folderLocation) {
        this.folderLocation = folderLocation;
    }
    
    
    
    
}
