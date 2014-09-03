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
    private String[][] subdata;
    private int dimensions;

    public ShowTableCollection() {
        this.columnames = new String[]{"# unsafe cells", "Var 1", "Var 2", "Var 3",
            "Var 4", "Var 5", "Var 6", "Var 7", "Var 8", "Var 9", "Var 10"};
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

    public void setAllTables(ArrayList<TableMu> allTables) {
        this.allTables = allTables;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }
    

    public ArrayList<TableMu> getAllTables() {
        if (this.allTables.isEmpty()) {
            for (ArrayList<TableMu> list : tablesForEachDimension) {
                this.allTables.addAll(list);
            }
//            this.allTables.addAll(this.tablesForEachDimension.get(0));
//            this.allTables.addAll(this.tablesForEachDimension.get(1));
//            this.allTables.addAll(this.tablesForEachDimension.get(2));
        }
        return allTables;
    }

    public void setOriginalTables(ArrayList<TableMu> tables) {
        this.originalTables = tables;
        //this.addDimension_1();
    }
    
    public ArrayList<TableMu> getOriginalTables(){
        return this.originalTables;
    }

//    public void addTable(TableMu table) {
//        this.originalTables.add(table);
//    }

    public String[] getColumnames() {
        String[] columnNames = new String[dimensions + 1];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = this.columnames[i];
        }
        return columnNames;
    }

    public String[][] getData() {
        this.getAllTables();
        ArrayList<TableMu> tables = new ArrayList<>();
        if (!this.selectedVariable.getName().equals("all")) {
            for (TableMu t : allTables) {
                boolean add = false;
                for (VariableMu v : t.getVariables()) {
                    if (v.equals(this.selectedVariable)) {
                        add = true;
                    }
                }
                if (add) {
                    tables.add(t);
                }
            }
        } else {
            tables = allTables;
        }
        if (this.data == null) {
            this.data = new String[tables.size()][this.dimensions + 1];
            fillData(tables, data);
            return this.data;
        } else {
            this.subdata = new String[tables.size()][this.dimensions + 1];
            fillData(tables, subdata);
            return this.subdata;
        }
    }

    public void fillData(ArrayList<TableMu> tables, String[][] data) {
        for (int i = 0; i < tables.size(); i++) {
            TableMu t = tables.get(i);
            for (int j = 0; j < t.getVariables().size(); j++) {
                data[i][j + 1] = t.getVariables().get(j).getName();
            }
            data[i][0] = Integer.toString(i + 1);
        }
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
