/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

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
public class RiskSpecificationController {
    
    private RiskSpecificationView view;
    private RiskSpecification model;
    private MetadataMu metadataMu;
    private CalculationService calculationService;
    private java.awt.Frame parentView; 
    
    public RiskSpecificationController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.parentView = parentView;
        this.view = new RiskSpecificationView(parentView, true, this);
        this.metadataMu = metadataMu;
        this.model = this.metadataMu.getCombinations().getRiskSpecification();
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
        for (TableMu tableMu : this.metadataMu.getCombinations().getTables()) {
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
        this.view.setMetadataMu(this.metadataMu);
        
        this.view.showChart();
        this.view.setVisible(true);
    }
    
    public void fillModelHistogramData(boolean cumulative) {
        double maxReident = calculationService.fillHistogramData(this.model.getRiskTable(), 
                this.model.getClasses(), cumulative);
        this.model.setMaxReidentRate(maxReident);
        
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
    
    public void calculateByReidentThreshold() {
        //TODO: more complicated because of iterations
    }
    
    
    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        this.view.setVisible(false);
    }


    
    
}
