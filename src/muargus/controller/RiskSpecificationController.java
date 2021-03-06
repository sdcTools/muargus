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
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.util.ArrayList;
import muargus.model.MetadataMu;
import muargus.model.RiskModelClass;
import muargus.model.RiskSpecification;
import muargus.model.TableMu;
import muargus.view.RiskSpecificationView;
import muargus.view.TablePickView;

/**
 * Controller class of the RiskSpecification screen.
 *
 * @author Statistics Netherlands
 */
public class RiskSpecificationController extends ControllerBase<RiskSpecification> {

    private final int MAX_ITERATIONS = 10;

    private final MetadataMu metadata;
    private final java.awt.Frame parentView;

    /**
     * Constructor for the RiskSpecificationController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public RiskSpecificationController(java.awt.Frame parentView, MetadataMu metadata) {
        this.parentView = parentView;
        super.setView(new RiskSpecificationView(parentView, true, this, metadata.isHouseholdData()));
        this.metadata = metadata;
        setModel(pickRiskSpecification());
        init();
        ((RiskSpecificationView) this.getView()).setRiskTable(getModel().getRiskTable());
        this.getView().setMetadata(this.metadata);
    }

    /**
     * Gets the titles of the risk tables.
     *
     * @return ArrayList of Strings containing the titles of the risk tables.
     */
    private ArrayList<String> getRiskTableTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (TableMu table : getRiskTables()) {
            titles.add(table.getTableTitle());
        }
        return titles;
    }

    /**
     * Gets all risk tables.
     *
     * @return Arraylist of TableMu's containing tables for which the risk model
     * is specified.
     */
    private ArrayList<TableMu> getRiskTables() {
        ArrayList<TableMu> tables = new ArrayList<>();
        for (TableMu tableMu : this.metadata.getCombinations().getTables()) {
            if (tableMu.isRiskModel()) {
                tables.add(tableMu);
            }
        }
        return tables;
    }

    /**
     * Shows the TablePickView if for more than one table the risk model is
     * specified and sets the model using the selected table.
     */
    private RiskSpecification pickRiskSpecification() {
        if (getRiskTables().size() > 1) {
            TablePickView tableView = new TablePickView(this.parentView, true);
            tableView.setTables(getRiskTableTitles());
            tableView.setVisible(true);
            if (tableView.getSelectedIndex() == -1) {
                return null;
            }
            return getModel(tableView.getSelectedIndex());
        } else {
            return getModel(0);
        }
    }

    /**
     * Initializes the data. The method checks whether the classes, containing
     * the riskmodel information for each pillar in the histogram, are empty. If
     * so, the risk threshold is initialized. Makes a normal (non-cumulative)
     * histogram of the data and calculates the values using the risk threshold.
     */
    private void init() {
        boolean initialize = getModel().getClasses().isEmpty();
        fillModelHistogramData(false);
        if (initialize) {
            initializeRiskThreshold();
        }

        calculateByRiskThreshold();
    }

    /**
     * Fills the histogram with the data.
     *
     * @param cumulative Boolean indicating whether a cumulative or a normal
     * histogram should be shown.
     */
    public void fillModelHistogramData(boolean cumulative) {
        try {
            double maxReident = getCalculationService().fillHistogramData(getModel().getRiskTable(),
                    getModel().getClasses(), cumulative);
            getModel().setMaxReidentRate(maxReident);
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Gets the model class of the RiskSpecification screen.
     *
     * @param index Integer containing the index of the list of tables for which
     * the risk model is specified. If for only one table the risk model is
     * specified, the default value is 0.
     * @return RiskSpecification instance for the specified table.
     */
    private RiskSpecification getModel(int index) {
        RiskSpecification riskSpec = this.metadata.getCombinations().getRiskSpecifications().get(getRiskTables().get(index));
        riskSpec.SetRiskTable(getRiskTables().get(index));
        return riskSpec;
    }

    /**
     * Initializes the risk threshold. The risk threshold is the average of the
     * minimum and the maximum risk threshold.
     */
    private void initializeRiskThreshold() {
        ArrayList<RiskModelClass> classes = getModel().getClasses();
        double min = Math.log(classes.get(0).getLeftValue());
        double max = Math.log(classes.get(classes.size() - 1).getRightValue());
        getModel().setRiskThreshold(Math.exp((min + max) / 2));
    }

    /**
     * Calculates the values for the risk specification using the risk
     * threshold.
     */
    public void calculateByRiskThreshold() {
        try {
            getModel().setUnsafeRecords(getCalculationService().calculateUnsafe(
                    getModel().getRiskTable(), getModel().getRiskThreshold(), this.metadata.isHouseholdData()));
            getModel().setReidentRateThreshold(getCalculationService().calculateReidentRate(
                    getModel().getRiskTable(), getModel().getRiskThreshold()));
            SystemUtils.writeLogbook("Risk threshold has been set/changed.");
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Calculates the values for the risk specification using the number of
     * unsafe records.
     */
    public void calculateByUnsafeRecords() {
        try {
            getModel().setRiskThreshold(getCalculationService().calculateRiskThreshold(
                    getModel().getRiskTable(), getModel().getUnsafeRecords(), this.metadata.isHouseholdData()));
            getModel().setReidentRateThreshold(getCalculationService().calculateReidentRate(
                    getModel().getRiskTable(), getModel().getRiskThreshold()));
            SystemUtils.writeLogbook("Risk threshold has been set/changed.");
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Calculates the values for the risk specification using the
     * reidentification threshold.
     *
     * @param soughtValue Double containing the sought reidentification
     * threshold.
     * @param nDecimals Integer containing the number of decimals to be shown
     * @throws ArgusException Throws an ArgusException when the reidentification
     * rate of the threshold is not between 0 and the maximum rate.
     */
    public void calculateByReidentThreshold(double soughtValue, int nDecimals) throws ArgusException {
        if (soughtValue > getModel().getMaxReidentRate()
                || soughtValue < 0) {
            throw new ArgusException("Re ident rate threshold should be between 0 and the maximum rate");
        }
        double r0 = 0;
        double r1 = getModel().getMaxRisk();
        double t0 = 0;
        double t1 = getModel().getMaxReidentRate();

        double value = getModel().getReidentRateThreshold();
        double risk = getModel().getRiskThreshold();
        int iteration = 0;
        double epsilon = Math.exp(-Math.log(10) * nDecimals);
        while (iteration < this.MAX_ITERATIONS) {
            if (Math.abs(value - soughtValue) < epsilon) {
                break;
            }
            value = getCalculationService().calculateReidentRate(getModel().getRiskTable(), risk);
            if (value > soughtValue) {
                r1 = risk;
                risk = r0 + (risk - r0) * (soughtValue - t0) / (value - t0);
                t1 = value;
            } else {
                r0 = risk;
                risk = r1 - (r1 - risk) * (t1 - soughtValue) / (t1 - value);
                t0 = value;
            }
            iteration++;
        }
        getModel().setReidentRateThreshold(soughtValue);
        getModel().setRiskThreshold(risk);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

}
