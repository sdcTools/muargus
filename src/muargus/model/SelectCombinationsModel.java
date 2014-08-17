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
    
    //TODO: remove these and their usage and replace it with a direct call to the number of rows/columns
    private int numberOfRows; 
    private int numberOfColumns;

    /**
     * 
     */
    public SelectCombinationsModel() {
        this.threshold = 1;
        this.numberOfColumns = 0;
        this.numberOfRows = 0;
        this.tables = new ArrayList<>();
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
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

}
