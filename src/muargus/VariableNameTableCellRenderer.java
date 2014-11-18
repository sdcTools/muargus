/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import muargus.model.VariableMu;

/**
 *
 * @author pibd05
 */
public class VariableNameTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String valueString = value instanceof VariableMu ? ((VariableMu) value).getName() : value.toString();
        Component cr = super.getTableCellRendererComponent(table, valueString, isSelected, hasFocus, row, column);

        if (value instanceof VariableMu) {
            setHorizontalAlignment(JLabel.LEFT);
        }

        if (value instanceof Double) {
            setHorizontalAlignment(JLabel.RIGHT);
        }
        return cr;
    }
}
