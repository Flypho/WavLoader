package view;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Mateusz Gołosz
 */
public class ProgressCellRender extends JProgressBar implements TableCellRenderer {

    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setOpaque(true);
        bar.setStringPainted(true);
        bar.setValue((int)Float.parseFloat(value.toString()));
        return bar;
    }
}
