package muargus.unusedClasses;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer for combinations. Draws the R red for risk model tables.
 *
 * @author Statistics Netherlands
 */
public class CombinationsTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cr = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (table.getValueAt(row, 0) == "R") {
            setForeground(Color.red);
        } else {
            setForeground(isSelected ? Color.white : Color.black);
        }

        return cr;

    }

}
