package muargus;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import muargus.model.CodeInfo;

/**
 * Table cell renderer for codes. Sets empty values to "-" and makes missing codes red.
 *
 * @author Statistics Netherlands
 */
public class CodeTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 0) {
            value = ((CodeInfo) value).getCode();
        }
        if (value != null && value.equals(-1)) {
            value = "-";
        }

        Component cr = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (((CodeInfo) table.getValueAt(row, 0)).isMissing()) {
            setForeground(Color.red);
        } else {
            setForeground(isSelected ? Color.white : Color.black);
        }
        setHorizontalAlignment(column > 1 ? JLabel.RIGHT : JLabel.LEFT);
        return cr;
    }

}
