/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author ambargus
 */
public class RiskSpecification {
    
    private double threshold;
    private double reidentRate;
    private int unsafeRecords;
    private double maxRisk;
    private double maxReidentRate;
    private ArrayList<RiskModelClass> classes;
    private TableMu riskTable;
    private double ksi;
    
    public RiskSpecification() {
        //TODO;
    }
    
    public void SetRiskTable(TableMu riskTable) {
        if (!riskTable.equals(this.riskTable)) {
            this.riskTable = riskTable;
            this.classes = new ArrayList<>();
        }
    }

    public ArrayList<RiskModelClass> getClasses() {
        return this.classes;
    }

    public TableMu getRiskTable() {
        return this.riskTable;
    }

    public double getKsi() {
        return this.ksi;
    }

    public void setKsi(double ksi) {
        this.ksi = ksi;
    }
    
    
       
    
    
    
}
