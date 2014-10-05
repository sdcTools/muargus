/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import java.util.ArrayList;
import muargus.model.MetadataMu;
import muargus.model.RiskModelClass;
import muargus.model.RiskSpecification;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.RiskSpecificationView;
import muargus.view.TablePickView;

/**
 *
 * @author ambargus
 */
public class RiskSpecificationController extends ControllerBase<RiskSpecification> {
    
    private final int MAX_ITERATIONS = 10;
    
    private final MetadataMu metadata;
    private final java.awt.Frame parentView; 
    
    public RiskSpecificationController(java.awt.Frame parentView, MetadataMu metadata) {
        this.parentView = parentView;
        super.setView(new RiskSpecificationView(parentView, true, this, metadata.isHouseholdData()));
        this.metadata = metadata;
        setModel(pickRiskSpecification());
        init();
        ((RiskSpecificationView)this.getView()).setRiskTable(getModel().getRiskTable());
        this.getView().setMetadata(this.metadata);
    }
    
    private ArrayList<String> getRiskTableTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (TableMu table : getRiskTables()) {
            titles.add(table.getTableTitle());
        }
        return titles;
    }
        
    private ArrayList<TableMu> getRiskTables() {
        ArrayList<TableMu> tables = new ArrayList<>();
        for (TableMu tableMu : this.metadata.getCombinations().getTables()) {
            if (tableMu.isRiskModel()) {
                tables.add(tableMu);
            }
        }
        return tables;
    }
    
    private RiskSpecification pickRiskSpecification() {
        if (getRiskTables().size() > 1) {
            TablePickView tableView = new TablePickView(this.parentView, true);
            tableView.setTables(getRiskTableTitles());
            tableView.setVisible(true);
            if (tableView.getSelectedIndex() == -1) {
                return null;
            }
            return getModel(tableView.getSelectedIndex());
        }
        else {
            return getModel(0);
            
        }
    }

    private void init() {
        boolean initialize = getModel().getClasses().isEmpty();
        fillModelHistogramData(false);
        if (initialize) {
            initializeRiskThreshold();
        }
        
        calculateByRiskThreshold();
    }
    
    public void fillModelHistogramData(boolean cumulative) {
        try {
            double maxReident = getCalculationService().fillHistogramData(getModel().getRiskTable(), 
                    getModel().getClasses(), cumulative);
            getModel().setMaxReidentRate(maxReident);
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
        
    }

    private RiskSpecification getModel(int index) {
        RiskSpecification riskSpec = this.metadata.getCombinations().getRiskSpecifications().get(getRiskTables().get(index));
        riskSpec.SetRiskTable(getRiskTables().get(index));
        return riskSpec;
    }
    
    private void initializeRiskThreshold() {
        ArrayList<RiskModelClass> classes = getModel().getClasses();
        double min = Math.log(classes.get(0).getLeftValue());
        double max = Math.log(classes.get(classes.size()-1).getRightValue());
        getModel().setRiskThreshold(Math.exp((min + max)/2));
    }
    
    public void calculateByRiskThreshold() {
        try {
            getModel().setUnsafeRecords(getCalculationService().calculateUnsafe(
                    getModel().getRiskTable(), getModel().getRiskThreshold(), this.metadata.isHouseholdData()));
            getModel().setReidentRateThreshold(getCalculationService().calculateReidentRate(
                    getModel().getRiskTable(), getModel().getRiskThreshold()));
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }
    
    public void calculateByUnsafeRecords() {
        try {
            getModel().setRiskThreshold(getCalculationService().calculateRiskThreshold(
                    getModel().getRiskTable(), getModel().getUnsafeRecords(), this.metadata.isHouseholdData()));
            getModel().setReidentRateThreshold(getCalculationService().calculateReidentRate(
                    getModel().getRiskTable(), getModel().getRiskThreshold()));
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
        
    }
    
    public void calculateByReidentThreshold(double soughtValue, int nDecimals) throws ArgusException {
        if (soughtValue > getModel().getMaxReidentRate() ||
                soughtValue < 0) {
            throw new ArgusException("Re ident rate threshold should be between 0 and the maximum rate");
        }
        double r0 = 0;
        double r1 = getModel().getMaxRisk();
        double t0 = 0;
        double t1 = getModel().getMaxReidentRate();
        
        double value = getModel().getReidentRateThreshold();
        double risk = getModel().getRiskThreshold();
        int iteration = 0;
        double epsilon = Math.exp(-Math.log(10)*nDecimals);
        while (iteration < MAX_ITERATIONS) {
            if (Math.abs(value - soughtValue) < epsilon)
                break;
            value = getCalculationService().calculateReidentRate(getModel().getRiskTable(), risk);
            if (value > soughtValue) {
                r1 = risk;
                risk = r0 + (risk-r0) * (soughtValue - t0)/(value - t0);
                t1 = value;
            }
            else {
                r0 = risk;
                risk = r1 - (r1 - risk) * (t1 - soughtValue)/(t1 - value);
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
