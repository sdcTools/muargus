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

import argus.utils.StrUtils;
import java.util.ArrayList;

/**
 * Model class containing the table specifications. An instance for each
 * selected table will exist.
 *
 * @author Statistics Netherlands
 */
public class TableMu {

    private boolean riskModel;
    private int threshold;
    private final ArrayList<VariableMu> variables = new ArrayList<>();
    private int nrOfUnsafeCombinations;
    private final int defaultThreshold = 1;

    /**
     * Constructor for the model class TableMu. Sets the default values for the
     * riskmodel and threshold.
     */
    public TableMu() {
        this.riskModel = false;
        this.threshold = this.defaultThreshold;
    }

    /**
     * Constructor for the model class TableMu that is used when the
     * Combinations class is cloned.
     *
     * @param table TableMu instance of the original table.
     */
    public TableMu(TableMu table) {
        this.riskModel = table.riskModel;
        this.threshold = table.threshold;
        for (VariableMu variable : table.variables) {
            this.variables.add(variable);
        }
    }

    /**
     * Returns whether the riskmodel is set for this table.
     *
     * @return Boolean indicating whether the riskmodel is set for this table.
     */
    public boolean isRiskModel() {
        return riskModel;
    }

    /**
     * Sets whether the riskmodel is set for this table.
     *
     * @param riskModel Boolean indicating whether the riskmodel is set for this
     * table.
     */
    public void setRiskModel(boolean riskModel) {
        this.riskModel = riskModel;
    }

    /**
     * Gets the threshold.
     *
     * @return Integer containing the threshold.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Sets the threshold.
     *
     * @param threshold Integer containing the threshold.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Gets the number of unsafe combinations.
     *
     * @return Integer containing the number of unsafe combinations.
     */
    public int getNrOfUnsafeCombinations() {
        return this.nrOfUnsafeCombinations;
    }

    /**
     * Sets the number of unsafe combinations.
     *
     * @param nrOfUnsafeCombinations Integer containing the number of unsafe
     * combinations.
     */
    public void setNrOfUnsafeCombinations(int nrOfUnsafeCombinations) {
        this.nrOfUnsafeCombinations = nrOfUnsafeCombinations;
    }

    /**
     * Gets all variables in the table.
     *
     * @return ArrayList containing the variables in the table.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * Gets the data for one table.
     *
     * @return Array of Objects containing with the first value indicating if
     * the riskModel is set, the second value showing the threshold and each
     * following value containing all variables in the table.
     */
    public Object[] getTableData() {
        Object[] table = new Object[variables.size() + 2];
        if (isRiskModel()) {
            table[0] = "R";
        } else {
            table[0] = "";
        }
        table[1] = this.threshold;
        for (int i = 0; i < variables.size(); i++) {
            table[i + 2] = variables.get(i).getName();
        }

        return table;
    }

    /**
     * Checks if at least one variable of the variables from this table is set
     * for the riskModel.
     *
     * @param riskModelVariables ArrayList containing all variables for which
     * the riskModel is specified.
     * @return
     */
    public boolean contains(ArrayList<VariableMu> riskModelVariables) {
        boolean contains = false;
        for (VariableMu riskVariables : riskModelVariables) {
            for (VariableMu thisVariables : this.getVariables()) {
                if (riskVariables.equals(thisVariables)) {
                    contains = true;
                }

            }
        }
        return contains;
    }

    /**
     * Constructs the title of the table, containing of the table dimension
     * names separated by x.
     *
     * @return title of the table
     */
    public String getTableTitle() {
        ArrayList<String> names = new ArrayList<>();

        for (VariableMu variable : this.getVariables()) {
            names.add(variable.getName());
        }
        return StrUtils.join(" x ", names);
    }

}
