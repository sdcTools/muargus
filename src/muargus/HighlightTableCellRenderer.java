/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
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
