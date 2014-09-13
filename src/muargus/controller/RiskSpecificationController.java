/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import java.util.ArrayList;
import muargus.MuARGUS;
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
public class RiskSpecificationController extends ControllerBase {
    
    private final int MAX_ITERATIONS = 10;
    
    //private RiskSpecificationView view;
    private final RiskSpecification model;
    private final MetadataMu metadata;
    private final CalculationService calculationService;
    private final java.awt.Frame parentView; 
    
    public RiskSpecificationController(java.awt.Frame parentView, MetadataMu metadata) {
        this.parentView = parentView;
        super.setView(new RiskSpecificationView(parentView, true, this, metadata.isHouseholdData()));
        this.metadata = metadata;
        this.model = this.metadata.getCombinations().getRiskSpecification();
        this.calculationService = MuARGUS.getCalculationService();
    }
    
    public ArrayList<String> getRiskTableTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for (TableMu table : getRiskTables()) {
            titles.add(getRiskTableTitle(table));
        }
        return titles;
    }
    
    public String getRiskTableTitle(TableMu table) {
        ArrayList<String> names = new ArrayList<>();
        
        for (VariableMu variable : table.getVariables()) {
            names.add(variable.getName());
        }
        return StrUtils.join(" x ", names);
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
    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        ArrayList<TableMu> riskTables = getRiskTables();
        if (riskTables.size() > 1) {
            TablePickView tableView = new TablePickView(this.parentView, true);
            tableView.setTables(getRiskTableTitles());
            tableView.setVisible(true);
            if (tableView.getSelectedIndex() == -1) {
                return;
            }
            this.model.SetRiskTable(riskTables.get(tableView.getSelectedIndex()));
        }
        else {
            this.model.SetRiskTable(riskTables.get(0));
        }
        boolean init = this.model.getClasses().isEmpty();
        fillModelHistogramData(false);
        if (init) {
            initializeRiskThreshold();
        }
        
        calculateByRiskThreshold();
        this.getView().setMetadata(this.metadata);
        
        this.getView().setVisible(true);
    }
    
    public void fillModelHistogramData(boolean cumulative) {
        try {
        double maxReident = calculationService.fillHistogramData(this.model.getRiskTable(), 
                this.model.getClasses(), cumulative);
        this.model.setMaxReidentRate(maxReident);
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
        
    }

    private void initializeRiskThreshold() {
        ArrayList<RiskModelClass> classes = this.model.getClasses();
        double min = Math.log(classes.get(0).getLeftValue());
        double max = Math.log(classes.get(classes.size()-1).getRightValue());
        this.model.setRiskThreshold(Math.exp((min + max)/2));
    }
    
    public void calculateByRiskThreshold() {
        this.model.setUnsafeRecords(this.calculationService.calculateUnsafe(
                this.model.getRiskTable(), this.model.getRiskThreshold()));
        this.model.setReidentRateThreshold(this.calculationService.calculateReidentRate(
                this.model.getRiskTable(), this.model.getRiskThreshold()));
    }
    
    public void calculateByUnsafeRecords() {
        this.model.setRiskThreshold(this.calculationService.calculateRiskThreshold(
                this.model.getRiskTable(), this.model.getUnsafeRecords()));
        this.model.setReidentRateThreshold(this.calculationService.calculateReidentRate(
                this.model.getRiskTable(), this.model.getRiskThreshold()));
        
    }
    
    public void calculateByReidentThreshold(double soughtValue, int nDecimals) throws ArgusException {
        if (soughtValue > this.model.getMaxReidentRate() ||
                soughtValue < 0) {
            throw new ArgusException("Re ident rate threshold should be between 0 and the maximum rate");
        }
        double r0 = 0;
        double r1 = this.model.getMaxRisk();
        double t0 = 0;
        double t1 = this.model.getMaxReidentRate();
        
        double value = this.model.getReidentRateThreshold();
        double risk = this.model.getRiskThreshold();
        int iteration = 0;
        double epsilon = Math.exp(-Math.log(10)*nDecimals);
        while (iteration < MAX_ITERATIONS) {
            if (Math.abs(value - soughtValue) < epsilon)
                break;
            value = this.calculationService.calculateReidentRate(this.model.getRiskTable(), risk);
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
        this.model.setReidentRateThreshold(soughtValue);
        this.model.setRiskThreshold(risk);
    }
    
    
    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }


    
    
}
