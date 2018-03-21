/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus.controller;

import java.util.ArrayList;
import muargus.model.MetadataMu;
import muargus.model.TableCollection;
import muargus.model.TableMu;
import muargus.view.ShowTableCollectionView;

/**
 * The Controller class of the ShowTableCollection screen.
 *
 * @author Statistics Netherlands
 */
public class ShowTableCollectionController extends ControllerBase<TableCollection> {

    /**
     * Constructor for the ShowTableCollectionController. This constructor makes
     * a new view and sets the metadata for the view.
     *
     * @param parentView the Frame of the mainFrame that is given to the
     * ShowTableCollectionView.
     * @param metadata the original metadata.
     */
    public ShowTableCollectionController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new ShowTableCollectionView(parentView, true, this));
        setModel(metadata.getCombinations().getTableCollection());
        getView().setMetadata(metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     * Makes an ArrayList of all tables and sets this in the model class. This
     * method first sets the dimensions and the columnNames. Then it will make a
     * new ArrayList. The ArrayList is filled with for each dimension an
     * ArrayList with its tables. So first all the tables with dimension one are
     * added, then those with dimensions two etc. Finally the ArrayList will
     * contain all tables and will be added to the model.
     */
    public final void setAllTables() {
        setDimensions();
        setColumnNames();

        ArrayList<TableMu> allTables = new ArrayList<>();
        for (int i = 1; i <= getModel().getDimensions(); i++) {
            allTables.addAll(getCalculationService().getTableUnsafeCombinations(i));
        }
        getModel().setAllTables(allTables);
    }

    /**
     * Sets the maximum number of dimensions in the model. This method will loop
     * through all of the original tables and change the dimensions to the size
     * of the largest table. After looping, the dimensions are set in the model.
     */
    public void setDimensions() {
        int dimensions = 0;
        for (TableMu t : getModel().getOriginalTables()) {
            if (t.getVariables().size() > dimensions) {
                dimensions = t.getVariables().size();
            }
        }
        getModel().setDimensions(dimensions);
    }

    /**
     * Sets the column names in the model. This method will generate an Array of
     * column names for each dimension and sets this in the model.
     */
    public void setColumnNames() {
        String[] columnNames = new String[getModel().getDimensions() + 1];
        columnNames[0] = "# unsafe cells";
        for (int i = 1; i < columnNames.length; i++) {
            columnNames[i] = "Var " + i;
        }
        getModel().setColumnNames(columnNames);
    }

    /**
     * Generates an array with the data that will be displayed on the table.
     * This function makes a new two-dimensional array with the length equal to
     * the number of tables and the with equal to the number of columns. The
     * first column will be filled with the number of unsafe combinations and
     * the following columns will be filled with the names of the variables of
     * each table
     *
     * @param tables The Array of tables that should be displayed
     * @return A double array containing the data that will be displayed on the
     * table
     */
    public Object[][] getData(ArrayList<TableMu> tables) {
        Object[][] data = new Object[tables.size()][getModel().getColumnNames().length];
        for (int i = 0; i < tables.size(); i++) {
            TableMu t = tables.get(i);
            data[i][0] = t.getNrOfUnsafeCombinations();
            for (int j = 0; j < t.getVariables().size(); j++) {
                data[i][j + 1] = t.getVariables().get(j).getName();
            }
        }
        return data;
    }

    /**
     * Generates an ArrayList of tables that need to be displayed and sets the
     * subsetData. If all tabels should be shown, the subsetData is filled with
     * the complete data from the model. Otherwise only the tables that meet the
     * requirements will be added to a new ArrayList. This ArrayList is given to
     * the method getData(), which generates a double array with the data. The
     * subsetData is then filled with this new data.
     *
     * @param showAllTables boolean value that determines if all tables should
     * be shown or only the tables that have unsafe combinations (!= 0)
     */
    public void setSubData(boolean showAllTables) {
        if (getModel().getSelectedVariable().getName().equals("all") && showAllTables) {
            getModel().setSubdata(getModel().getData());
        } else {
            ArrayList<TableMu> tables = new ArrayList<>();
            for (TableMu t : getModel().getAllTables()) {
                if (getModel().getSelectedVariable().getName().equals("all")) {
                    if (t.getNrOfUnsafeCombinations() > 0) {
                        tables.add(t);
                    }
                } else if (t.getVariables().contains(getModel().getSelectedVariable())) {
                    if (showAllTables) {
                        tables.add(t);
                    } else {
                        if (t.getNrOfUnsafeCombinations() > 0) {
                            tables.add(t);
                        }
                    }
                }
            }
            getModel().setSubdata(getData(tables));
        }
    }
}
