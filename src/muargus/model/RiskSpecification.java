/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public RiskSpecification() {
        this.classes = new ArrayList<>();
    }
    
    public void SetRiskTable(TableMu riskTable) {
        if (!riskTable.equals(this.riskTable)) {
            this.riskTable = riskTable;
            this.maxReidentRate = 0;
        }
    }

    public ArrayList<RiskModelClass> getClasses() {
        return this.classes;
    }

    public TableMu getRiskTable() {
        return this.riskTable;
    }

    public double getMaxRisk() {
        return this.classes.get(this.classes.size()-1).getRightValue();
    }
    
    public double getMinRisk() {
        return this.classes.get(0).getLeftValue();
    }

    public double getMaxReidentRate() {
        return maxReidentRate;
    }

    public void setMaxReidentRate(double maxReidentRate) {
        this.maxReidentRate = maxReidentRate;
    }

    public double getRiskThreshold() {
        return this.riskThreshold;
    }

    public void setRiskThreshold(double riskThreshold) {
        this.riskThreshold = riskThreshold;
    }

    public double getReidentRateThreshold() {
        return this.reidentRateThreshold;
    }

    public void setReidentRateThreshold(double reidentRateThreshold) {
        this.reidentRateThreshold = reidentRateThreshold;
    }

    public int getUnsafeRecords() {
        return unsafeRecords;
    }

    public void setUnsafeRecords(int unsafeRecords) {
        this.unsafeRecords = unsafeRecords;
    }
    
    

    
    
       
    
    
    
}
