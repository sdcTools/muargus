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
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import muargus.model.VariableMu;

/**
 * List renderer. Shows the variable name instead of the variable object name.
 *
 * @author Statistics Netherlands
 */
public class VariableNameCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String valueString = value instanceof VariableMu ? ((VariableMu) value).getName() : value.toString();
        return super.getListCellRendererComponent(list, valueString, index, isSelected, cellHasFocus);
    }
}
