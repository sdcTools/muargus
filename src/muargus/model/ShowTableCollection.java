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
    private ArrayList<TableMu> originalTables;
    private ArrayList<TableMu> allTables;
    private ArrayList<ArrayList<TableMu>> tablesForEachDimension;
    private final String[] columnames;
    private String[][] data;
    private int dimensions;

    public ShowTableCollection() {
        this.columnames = new String[]{"# unsafe cells", "Var 1", "Var 2", "Var 3", "Var 4", "Var 5"};
        this.originalTables = new ArrayList<>();
        this.tablesForEachDimension = new ArrayList<>();
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

    public VariableMu getSelectedVariable() {
        return selectedVariable;
    }

    public void setSelectedVariable(VariableMu selectedVariable) {
        this.selectedVariable = selectedVariable;
    }

    public ArrayList<TableMu> getAllTables() {
        if(this.allTables.isEmpty()){
            this.allTables.addAll(this.tablesForEachDimension.get(0));
            this.allTables.addAll(this.tablesForEachDimension.get(1));
            this.allTables.addAll(this.tablesForEachDimension.get(2));
        }
        return allTables;
    }

    public void setTables(ArrayList<TableMu> tables) {
        this.originalTables = tables;
        this.addDimension_1();
    }

    public void addTable(TableMu table) {
        this.originalTables.add(table);
    }

    public String[] getColumnames() {
        String[] columnNames = new String[dimensions + 1];
        for(int i = 0; i< columnNames.length; i++){
            columnNames[i] = this.columnames[i];
        }
        return columnNames;
    }

    public String[][] getData() {
        this.getAllTables();
        if (this.data == null) {
            this.data = new String[this.allTables.size()][6];

            for (int i = 0; i < allTables.size(); i++) {
                TableMu t = allTables.get(i);
                for (int j = 0; j < t.getVariables().size(); j++) {
                    this.data[i][j + 1] = t.getVariables().get(j).getName();
                }
                this.data[i][0] = Integer.toString(i + 1);
            }
        }
        return this.data;
    }

    public ArrayList<ArrayList<TableMu>> getTablesForEachDimension() {
        return tablesForEachDimension;
    }

    public void addTablesForEachDimension(ArrayList<TableMu> tablesForEachDimension) {
        this.tablesForEachDimension.add(tablesForEachDimension);
    }

    public void addDimension_1() {
        ArrayList<TableMu> dimension_1 = new ArrayList<>();
        for (VariableMu v : this.variables) {
            TableMu table = new TableMu();
            table.addVariable(v);
            dimension_1.add(table);
        }
        this.tablesForEachDimension.add(dimension_1);
        this.dimensions = 1;
        this.addDimension_2();
    }

    public void addDimension_2() {
        ArrayList<VariableMu> variables = new ArrayList<>();
        for (TableMu t : originalTables) {
            if (t.getVariables().size() > 1) {
                for (VariableMu v : t.getVariables()) {
                    if (!variables.contains(v)) {
                        variables.add(v);
                    }
                }
            }
        }
        if (variables.size() > 0) {
            ArrayList<TableMu> dimension_2 = new ArrayList<>();
            for (int i = 0; i < variables.size() - 1; i++) {
                for (int j = i + 1; j < variables.size(); j++) {
                    TableMu table = new TableMu();
                    table.addVariable(variables.get(i));
                    table.addVariable(variables.get(j));
                    dimension_2.add(table);
                }
            }
            this.tablesForEachDimension.add(dimension_2);
            this.dimensions = 2;
            this.addDimension_3();
        } 
    }

    public void addDimension_3() {
        ArrayList<VariableMu> variables = new ArrayList<>();
        for (TableMu t : originalTables) {
            if (t.getVariables().size() > 2) {
                for (VariableMu v : t.getVariables()) {
                    if (!variables.contains(v)) {
                        variables.add(v);
                    }
                }
            }
        }
        if (variables.size() > 0) {
            ArrayList<TableMu> dimension_3 = new ArrayList<>();
            for (int i = 0; i < variables.size() - 2; i++) {
                for (int j = i + 1; j < variables.size() - 1; j++) {
                    for (int k = j + 1; k < variables.size(); k++) {
                        TableMu table = new TableMu();
                        table.addVariable(variables.get(i));
                        table.addVariable(variables.get(j));
                        table.addVariable(variables.get(k));
                        dimension_3.add(table);
                    }
                }
            }
            this.tablesForEachDimension.add(dimension_3);
            this.dimensions = 3;
            this.addDimension_4();
        } 
    }
    
    public void addDimension_4() {
        ArrayList<VariableMu> variables = new ArrayList<>();
        for (TableMu t : originalTables) {
            if (t.getVariables().size() > 2) {
                for (VariableMu v : t.getVariables()) {
                    if (!variables.contains(v)) {
                        variables.add(v);
                    }
                }
            }
        }
        if (variables.size() > 0) {
            ArrayList<TableMu> dimension_4 = new ArrayList<>();
            for (int i = 0; i < variables.size() - 2; i++) {
                for (int j = i + 1; j < variables.size() - 1; j++) {
                    for (int k = j + 1; k < variables.size(); k++) {
                        TableMu table = new TableMu();
                        table.addVariable(variables.get(i));
                        table.addVariable(variables.get(j));
                        table.addVariable(variables.get(k));
                        dimension_4.add(table);
                    }
                }
            }
            this.tablesForEachDimension.add(dimension_4);
            this.dimensions = 3;
            //this.addDimension_4();
        } 
    }
}
