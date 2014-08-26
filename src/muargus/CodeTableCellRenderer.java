/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import muargus.model.UnsafeCodeInfo;

/**
 *
 * @author pibd05
 */
public class CodeTableCellRenderer extends DefaultTableCellRenderer {

    @Override
     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
         if (column == 0) {
             value = ((UnsafeCodeInfo) value).getCode();
         }
             
             
          Component cr = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
          if (((UnsafeCodeInfo) table.getValueAt(row, 0)).isMissing()) {
              setForeground(Color.red);
          }
          else {
              setForeground(isSelected ? Color.white : Color.black);
          }
              
          return cr;

    }
    
}
