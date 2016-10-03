package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

/**
 *
 * @author Mateusz Gołosz
 */
public class SearchButtonEditor extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {

    private JTable searchTable;
    private JTable downloadTable;
    private Border originalBorder;
    private ArrayList<String[]> searchResults;
    private JButton renderButton;
    private JButton editButton;
    private WindowManager windowManager;

    /**
     * Create the ButtonColumn to be used as a renderer and editor. The renderer
     * and editor will automatically be installed on the TableColumn of the
     * specified column.
     *
     * @param table the table containing the button renderer/editor
     * @param action the Action to be invoked when the button is invoked
     * @param column the column to which the button renderer/editor is added
     */
    public SearchButtonEditor(JTable searchTable, int column, WindowManager windowManager, JTable downloadTable, ArrayList<String[]> searchResults) {
        this.searchTable = searchTable;
        this.downloadTable = downloadTable;
        this.windowManager = windowManager;
        this.searchResults = searchResults;
        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        originalBorder = editButton.getBorder();


        TableColumnModel searchColumnModel = searchTable.getColumnModel();
        searchColumnModel.getColumn(column).setCellRenderer(this);
        searchColumnModel.getColumn(column).setCellEditor(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
            renderButton.setBorder(originalBorder);

        if (value == null) {
            renderButton.setText("");
            renderButton.setIcon(null);
        } else {
            renderButton.setText(value.toString());
            renderButton.setIcon(null);
        }
        return renderButton;
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    
    
}
