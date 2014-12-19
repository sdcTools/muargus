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

/**
 * Model class of the variable specifications for PRAM. An instance for each
 * categorical variable of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class PramVariableSpec {

    private boolean useBandwidth;
    private boolean applied;
    private String appliedText;
    private final VariableMu variable;

    /**
     * Constructor of the model class PramVariableSpec. Initializes the variable
     * and sets the default values for the useBandwidth and applied.
     *
     * @param variable The variable for which the PRAM specification can be
     * applied.
     */
    public PramVariableSpec(VariableMu variable) {
        this.variable = variable;
        this.useBandwidth = false;
        this.applied = false;
    }

    /**
     * Returns whether the bandwidth is used.
     *
     * @return Boolean indicating whether the bandwidth is used.
     */
    public boolean useBandwidth() {
        return this.useBandwidth;
    }

    /**
     * Sets whether the bandwidth is used.
     *
     * @param useBandwidth Boolean indicating whether the bandwidth is used.
     */
    public void setUseBandwidth(boolean useBandwidth) {
        this.useBandwidth = useBandwidth;
    }

    /**
     * Gets the bandwidth.
     *
     * @return Integer containing the bandwidth.
     */
    public int getBandwidth() {
        return this.variable.getBandwidth();
    }

    /**
     * Sets the bandwidth
     *
     * @param bandwidth Integer containing the bandwidth.
     */
    public void setBandwidth(int bandwidth) {
        this.variable.setBandwidth(bandwidth);
    }

    /**
     * Returns whether PRAM-specification has been applied on this variable.
     *
     * @return Returns boolean indicating whether PRAM-specification has been
     * applied on this variable.
     */
    public boolean isApplied() {
        return this.applied;
    }

    /**
     * Sets whether PRAM-specification has been applied on this variable.
     *
     * @param applied Boolean indicating whether PRAM-specification has been
     * applied on this variable.
     */
    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    /**
     * Gets the variable.
     *
     * @return VariableMu instance containing the variable.
     */
    public VariableMu getVariable() {
        return this.variable;
    }

    /**
     * Gets the text that will be displayed when the variable is applied.
     *
     * @return String containing the text that will be displayed when the
     * variable is applied.
     */
    public String getAppliedText() {
        if (isApplied()) {
            this.appliedText = "X";
        } else {
            this.appliedText = "";
        }
        return appliedText;
    }

    /**
     * Gets the text that will be displayed for the bandwidth.
     *
     * @return String containing the bandwidth if PRAM-specification is applied
     * and bandwith is used. If this is not the case it contains an empty
     * String.
     */
    public String getBandwidthText() {
        if (isApplied() && useBandwidth()) {
            return Integer.toString(this.variable.getBandwidth());
        } else {
            return "";
        }
    }

}
