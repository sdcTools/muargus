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

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import muargus.model.VariableMu;

/**
 * Table cell renderer. Shows the variable name instead of the variable object
 * name.
 *
 * @author Statistics Netherlands
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
