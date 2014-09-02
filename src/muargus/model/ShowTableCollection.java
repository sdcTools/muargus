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

    
    private ArrayList<VariableMu> variables;
    private boolean showAllTables;
    private VariableMu selectedVariable;
    private ArrayList<TableMu> tables;
    private final String[] columnames;
    private String[][] data;
    
    public ShowTableCollection() {
        this.columnames = new String[]{"# unsafe cells", "Var 1", "Var 2", "Var 3", "Var 4", "Var 5"};
        this.tables = new ArrayList<>();
    }
    
    public void setVariables(ArrayList<VariableMu> variables){
        this.variables = variables;
    }
    
    public ArrayList<VariableMu> getVariables(){
        return this.variables;
    }

    public boolean isShowAllTables() {
        return showAllTables;
    }

    public void setShowAllTables(boolean showAllTables) {
        this.showAllTables = showAllTables;
    }

    public VariableMu getSelectedVariable() {
        return selectedVariable;
    }

    public void setSelectedVariable(VariableMu selectedVariable) {
        this.selectedVariable = selectedVariable;
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

    public String[] getColumnames() {
        return columnames;
    }
    
    public String[][] getData() {
        if (this.data == null) {
            this.data = new String[this.tables.size()][6];
            
            for(int i = 0; i < tables.size(); i++){
                TableMu t = tables.get(i);
                for(int j = 0; j < t.getVariables().size(); j++){
                    this.data[i][j+1] = t.getVariables().get(j).getName();
                }
                this.data[i][0] = Integer.toString(i+1);
            }
        }
        return this.data;
    }
    
}
