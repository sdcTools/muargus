/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsModel {
    private int threshold;
    private boolean riskModel;
    private ArrayList<TableMu> tables;
    private VariableMu[] variables;
    private int[] thresholds;
    private HashMap<VariableMu, UnsafeInfo> unsafe;
    
    /**
     * 
     */
    public SelectCombinationsModel() {
        this.threshold = 1;
        this.tables = new ArrayList<>();
    }
    
    public SelectCombinationsModel(SelectCombinationsModel model) {
        this.threshold = model.threshold;
        this.tables = new ArrayList<>();
        for (TableMu table : model.tables) {
            this.tables.add(new TableMu(table));
        }
    }

    public void clearUnsafe() {
        this.unsafe = new HashMap<>();
    }
    
    public void setUnsafe(VariableMu variable, UnsafeInfo unsafe) {
        this.unsafe.put(variable, unsafe);
    }
    
    public UnsafeInfo getUnsafe(VariableMu variable) {
        return this.unsafe.get(variable);
    }
    
    /**
     * 
     * @return 
     */
    public String getThreshold() {
        return Integer.toString(threshold);
    }

    public int[] getThresholds() {
        return thresholds;
    }

    public void setThresholds(int[] thresholds) {
        this.thresholds = thresholds;
    }
    
    /**
     * 
     * @param threshold 
     */
    public void setThreshold(String threshold) {
        this.threshold = Integer.parseInt(threshold);
    }
    
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public ArrayList<TableMu> getTables() {
        return tables;
    }

    public void setTables(ArrayList<TableMu> tables) {
        this.tables = tables;
    }
    
    public void addTable(TableMu table){
        this.tables.add(table);
    }
    
    public void removeTable(int i){
        this.tables.remove(i);
    }
    
    public void removeTable(TableMu t){
        this.tables.remove(t);
    }

    public int getNumberOfRows() {
        return this.tables.size();
    }

    public void setNumberOfRows(int n) {
        return; //Deprecated, do not use
    }

    public boolean isRiskModel() {
        this.riskModel = false;
        for(TableMu t: this.tables){
            if(t.isRiskModel()){
                this.riskModel = true;
            }
        }
        return this.riskModel;
    }

    public void setRiskModel(boolean riskModel) {
        this.riskModel = riskModel;
    }
    
    public int getNumberOfColumns() {
        return this.calculateNumberOfColumns();
    }
    
    private int calculateNumberOfColumns() { 
        int numberOfVariables = 0;
        for(TableMu t: this.tables){
            if(t.getVariables().size() > numberOfVariables){
                numberOfVariables = t.getVariables().size();
            }
        }
        int numberOfColumns = numberOfVariables + 2;
        return numberOfColumns; 
    }
    
    public ArrayList<VariableMu> getVariablesInTables() {
        ArrayList<VariableMu> variables = new ArrayList<>();
        for (TableMu table : this.tables) {
            for (VariableMu variable : table.getVariables()) {
                if (!variables.contains(variable))
                    variables.add(variable);
            }
        }
        return variables;
    }
    
    public int getMaxDimsInTables() {
        int max = 0;
        for (TableMu table : this.tables) {
            if (table.getVariables().size() > max) {
                max = table.getVariables().size();
            }
        }
        return max;
    }
//    public VariableMu[] getVariables(){
//        return variables;
//    }
//    
//    public void setVariables(VariableMu[] variables){
//        this.variables = variables;
//    }

}
