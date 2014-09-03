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
public class ShowTableCollection {

    private boolean showAllTables;
    private VariableMu selectedVariable;
    private ArrayList<VariableMu> variables;
    private ArrayList<TableMu> originalTables;
    private ArrayList<TableMu> allTables;
    private String[] columnNames;
    private String[][] data;
    private String[][] subdata;
    private int dimensions;

    public ShowTableCollection() {
        this.originalTables = new ArrayList<>();
        this.allTables = new ArrayList<>();
    }

    public void setVariables(ArrayList<VariableMu> variables) {
        this.variables = variables;
    }

    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    public boolean isShowAllTables() {
        return showAllTables;
    }

    public void setShowAllTables(boolean showAllTables) {
        this.showAllTables = showAllTables;
    }

    public ArrayList<TableMu> getAllTables() {
        return allTables;
    }
    
    public void setAllTables(ArrayList<TableMu> allTables) {
        this.allTables = allTables;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public String[][] getSubdata() {
        return subdata;
    }

    public void setSubdata(String[][] subdata) {
        this.subdata = subdata;
    }
   
    public void setOriginalTables(ArrayList<TableMu> tables) {
        this.originalTables = tables;
    }
    
    public ArrayList<TableMu> getOriginalTables(){
        return this.originalTables;
    }

    public void setColumnNames(String [] columnNames){
        this.columnNames = columnNames;
    }
    public String[] getColumnames() {
        
        return this.columnNames;
    }
    
    public void setData(String[][] data){
        this.data = data;
    }

    public String[][] getData() {
        return this.data;
    }

    public VariableMu getSelectedVariable() {
        return selectedVariable;
    }

    public void setSelectedVariable(VariableMu selectedVariable) {
        this.selectedVariable = selectedVariable;
    }
}
