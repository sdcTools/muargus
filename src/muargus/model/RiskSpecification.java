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
 * Model class of the RiskSpecification screen (both individual and household).
 * Only a single instance of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class RiskSpecification {

    private double riskThreshold;
    private double reidentRateThreshold;
    private int unsafeRecords;
    private double maxReidentRate;
    private final ArrayList<RiskModelClass> classes;
    private TableMu riskTable;

    /**
     * Constructor of the model class RiskSpecification. Makes an empty
     * arraylists for the classes containing the inforation for each pillar in
     * the histogram.
     */
    public RiskSpecification() {
        this.classes = new ArrayList<>();
    }

    /**
     * Gets the RisModelClasses containing the riskmodel information for each
     * pillar in the histogram.
     *
     * @return ArrayList of RisModelClasses containing the riskmodel information
     * for each pillar in the histogram.
     */
    public ArrayList<RiskModelClass> getClasses() {
        return this.classes;
    }

    /**
     * Sets the table for which the riskModel is specified.
     *
     * @param riskTable TableMu containing the table for which the riskModel is
     * specified.
     */
    public void SetRiskTable(TableMu riskTable) {
        if (!riskTable.equals(this.riskTable)) {
            this.riskTable = riskTable;
            this.maxReidentRate = 0;
        }
    }

    /**
     * Gets the table for which the riskModel is specified.
     *
     * @return TableMu containing the table for which the riskModel is
     * specified.
     */
    public TableMu getRiskTable() {
        return this.riskTable;
    }

    /**
     * Gets the overall maximum risk. This method takes the right hand risk
     * value of the last pillar of the histogram as the maximum risk.
     *
     * @return Double containing the overall maximum risk.
     */
    public double getMaxRisk() {
        return this.classes.get(this.classes.size() - 1).getRightValue();
    }

    /**
     * Gets the overall minimum risk. This method takes the left hand risk value
     * of the first pillar of the histogram as the minimum risk.
     *
     * @return Double containing the overall minimal risk.
     */
    public double getMinRisk() {
        return this.classes.get(0).getLeftValue();
    }

    /**
     * Gets the maximum reidentification rate.
     *
     * @return Double containing the maximum reidentification rate.
     */
    public double getMaxReidentRate() {
        return maxReidentRate;
    }

    /**
     * Sets the maximum reidentification rate.
     *
     * @param maxReidentRate Double containing the maximum reidentification
     * rate.
     */
    public void setMaxReidentRate(double maxReidentRate) {
        this.maxReidentRate = maxReidentRate;
    }

    /**
     * Gets the risk threshold.
     *
     * @return Double containing the risk threshold.
     */
    public double getRiskThreshold() {
        return this.riskThreshold;
    }

    /**
     * Set the risk threshold.
     *
     * @param riskThreshold Double containing the risk threshold.
     */
    public void setRiskThreshold(double riskThreshold) {
        this.riskThreshold = riskThreshold;
    }

    /**
     * Gets the reidentification rate threshold
     *
     * @return Double containing the reidentification rate threshold.
     */
    public double getReidentRateThreshold() {
        return this.reidentRateThreshold;
    }

    /**
     * Set the reidentification rate threshold
     *
     * @param reidentRateThreshold Double containing the reidentification rate
     * threshold.
     */
    public void setReidentRateThreshold(double reidentRateThreshold) {
        this.reidentRateThreshold = reidentRateThreshold;
    }

    /**
     * Gets the number of unsafe records.
     *
     * @return Integer containing the number of unsafe records.
     */
    public int getUnsafeRecords() {
        return unsafeRecords;
    }

    /**
     * Sets the number of unsafe records.
     *
     * @param unsafeRecords Integer containing the number of unsafe records.
     */
    public void setUnsafeRecords(int unsafeRecords) {
        this.unsafeRecords = unsafeRecords;
    }

}
