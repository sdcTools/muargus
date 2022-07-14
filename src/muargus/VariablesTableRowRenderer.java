/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author pwof
 */
public class VariablesTableRowRenderer extends DefaultTableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component cr = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (!"".equals(table.getValueAt(row, 0))) {
            setForeground("hhID".equals(table.getValueAt(row,0)) ? Color.gray : Color.red);
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
