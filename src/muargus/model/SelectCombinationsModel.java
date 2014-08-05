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
    private ArrayList<String> variablesList;
    private ArrayList<String> itemsSelectedList;
    // TODO: change type to generic type T
    private ArrayList<ArrayList<String>> tablesTable;   

    /**
     * 
     */
    public SelectCombinationsModel() {
        this.threshold = 1;
        this.variablesList = new ArrayList<>();
        this.itemsSelectedList = new ArrayList<>();
        this.tablesTable = new ArrayList<>();
    }

    /**
     * 
     * @return 
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * 
     * @param threshold 
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<String> getVariablesList() {
        return variablesList;
    }

    /**
     * 
     * @param variablesList 
     */
    public void setVariablesList(ArrayList<String> variablesList) {
        this.variablesList = variablesList;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<String> getItemsSelectedList() {
        return itemsSelectedList;
    }

    /**
     * 
     * @param itemsSelectedList 
     */
    public void setItemsSelectedList(ArrayList<String> itemsSelectedList) {
        this.itemsSelectedList = itemsSelectedList;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<ArrayList<String>> getTablesTable() {
        return tablesTable;
    }

    /**
     * 
     * @param tablesTable 
     */
    public void setTablesTable(ArrayList<ArrayList<String>> tablesTable) {
        this.tablesTable = tablesTable;
    }
    
}
