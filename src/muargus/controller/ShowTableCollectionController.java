/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.ShowTableCollection;
import muargus.model.TableMu;
import muargus.view.ShowTableCollectionView;

/**
 *
 * @author ambargus
 */
public class ShowTableCollectionController {

    ShowTableCollectionView view;
    ShowTableCollection model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public ShowTableCollectionController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new ShowTableCollectionView(parentView, true, this);
        this.metadataMu = metadata;
        this.view.setMetadataMu(this.metadataMu);
    }

    public void showView() {
        this.view.setVisible(true);
    }

    public void close() {
        this.view.setVisible(false);
    }

    public void setModel(ShowTableCollection model) {
        this.model = model;
    }

    public void setAllTables() {
        ArrayList<TableMu> allTables = new ArrayList<>();
        this.setDimensions();
        this.calculationService = MuARGUS.getCalculationService();
        for (int i = 1; i <= this.model.getDimensions(); i++) {
            allTables.addAll(this.calculationService.getTableUnsafeCombinations(metadataMu, i));
        }
        this.model.setAllTables(allTables);
    }

    public void setDimensions() {
        int dimensions = 0;
        for (TableMu t : this.model.getOriginalTables()) {
            if (t.getVariables().size() > dimensions) {
                dimensions = t.getVariables().size();
            }
        }
        this.model.setDimensions(dimensions);
        this.setColumnNames();
    }

    public void setColumnNames() {
        String[] columnNames = new String[this.model.getDimensions() + 1];
        columnNames[0] = "# unsafe cells";
        for (int i = 1; i < columnNames.length; i++) {
            columnNames[i] = "Var " + i;
        }
        this.model.setColumnNames(columnNames);
    }

    public String[][] getData(ArrayList<TableMu> tables) {
        String[][] data = new String[tables.size()][this.model.getDimensions() + 1];
        for (int i = 0; i < tables.size(); i++) {
            TableMu t = tables.get(i);
            data[i][0] = Integer.toString(t.getNrOfUnsafeCombinations());
            for (int j = 0; j < t.getVariables().size(); j++) {
                data[i][j + 1] = t.getVariables().get(j).getName();
            }
        }
        return data;
    }

    public void setSubData(boolean showAllTables) {
        if (this.model.getSelectedVariable().getName().equals("all") && showAllTables) {
            this.model.setSubdata(this.model.getData());
        } else {
            ArrayList<TableMu> tables = new ArrayList<>();
            for (TableMu t : this.model.getAllTables()) {
                if (this.model.getSelectedVariable().getName().equals("all")) {
                    if (t.getNrOfUnsafeCombinations() > 0) {
                        tables.add(t);
                    }
                } else if (t.getVariables().contains(this.model.getSelectedVariable())) {
                    if (showAllTables) {
                        tables.add(t);
                    } else {
                        if (t.getNrOfUnsafeCombinations() > 0) {
                            tables.add(t);
                        }
                    }
                }
            }
            this.model.setSubdata(this.getData(tables));
        }
    }
}
