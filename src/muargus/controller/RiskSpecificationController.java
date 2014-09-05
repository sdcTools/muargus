/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.RiskSpecification;
import muargus.model.TableMu;
import muargus.view.RiskSpecificationView;

/**
 *
 * @author ambargus
 */
public class RiskSpecificationController {
    
    RiskSpecificationView view;
    RiskSpecification model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public RiskSpecificationController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.view = new RiskSpecificationView(parentView, true, this);
        this.metadataMu = metadataMu;
        this.model = this.metadataMu.getCombinations().getRiskSpecification();
        this.view.setMetadataMu(this.metadataMu);
        this.calculationService = MuARGUS.getCalculationService();
        
        ArrayList<TableMu> riskTables = getRiskTables();
        if (riskTables.size() > 1) {
            //TODO: choose risk table
        }
        else {
            this.model.SetRiskTable(riskTables.get(0));
        }
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
            
        double ksi = calculationService.fillHistogramData(this.model.getRiskTable(), 
                this.model.getClasses());
        this.model.setKsi(ksi);
        //TODO: default calculation. can be triggered by the view too
        this.view.setVisible(true);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        this.view.setVisible(false);
    }

    /**
     * Fuction for setting the model. This function is used by the view after
     * setting the model itself
     *
     * @param model the model class of the ShowTableCollection screen
     */
    public void setModel(RiskSpecification model) {
        this.model = model;
    }
    
    
}
