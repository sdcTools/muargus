package muargus;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer.
 *
 * @author Statistics Netherlands
 */
public class HighlightTableCellRenderer extends DefaultTableCellRenderer {
    
    @Override
     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          Component cr = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          if (!"".equals(table.getValueAt(row, 0))) {
              setForeground(Color.red);
          }
          else {
              setForeground(isSelected ? Color.white : Color.black);
          }
          if (value instanceof Integer) {
              setHorizontalAlignment(JLabel.RIGHT);
          }   
          return cr;
     }
}
