/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsModel {
    private int threshold;
    private ArrayList<TableMu> tables;
    
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

    /**
     * 
     * @return 
     */
    public String getThreshold() {
        return Integer.toString(threshold);
    }

    /**
     * 
     * @param threshold 
     */
    public void setThreshold(String threshold) {
        this.threshold = Integer.parseInt(threshold);
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

    public int getNumberOfRows() {
        return this.tables.size();
    }

    public void setNumberOfRows(int n) {
        return; //Deprecated, do not use
    }

    public int getNumberOfColumns() {
        return calculateNumberOfColumns();
    }
    
    private int calculateNumberOfColumns() { 
        return -1; //TODO
    }

}
