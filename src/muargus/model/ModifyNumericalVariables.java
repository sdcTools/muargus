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
package muargus.model;

import java.util.ArrayList;

/**
 * Model class of the ModifyNumbericalVariables screen. Only a single instance
 * of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class ModifyNumericalVariables {

    private final ArrayList<ModifyNumericalVariablesSpec> modifyNumericalVariablesSpec;
    private String[][] variablesData;
    private final String[] variablesColumnNames;

    /**
     * Constructor of the model class ModifyNumericalVariables. Initializes the
     * column names and makes an empty arraylist for the
     * modifyNumericalVariablesSpec.
     */
    public ModifyNumericalVariables() {
        this.modifyNumericalVariablesSpec = new ArrayList<>();
        this.variablesColumnNames = new String[]{"Modified", "Variable"};
    }

    /**
     * Gets the ArrayList containing the variable specifications for numerical
     * modification. The ModifyNumericalVariablesSpec contains all relevant
     * information for modifying the relevant variable. If the ArrayList is
     * empty, use this method to add ModifyNumericalVariablesSpec's.
     *
     * @return ArrayList containing ModifyNumericalVariablesSpec's.
     */
    public ArrayList<ModifyNumericalVariablesSpec> getModifyNumericalVariablesSpec() {
        return modifyNumericalVariablesSpec;
    }

    /**
     * Sets the data for the table containing the modified text and the variable
     * name of each numerical variable.
     *
     * @param variablesData Double Array of Strings containing the modified text
     * and the variable name of each numerical variable.
     */
    public void setVariablesData(String[][] variablesData) {
        this.variablesData = variablesData;
    }

    /**
     * gets the data for the table containing the modified text and the variable
     * name of each numerical variable.
     *
     * @return Double Array of Strings containing the modified text and the
     * variable name of each numerical variable.
     */
    public String[][] getVariablesData() {
        return variablesData;
    }

    /**
     * Gets an Array containing the column names.
     *
     * @return Array of Strings containing the column names.
     */
    public String[] getVariablesColumnNames() {
        return variablesColumnNames;
    }

}
