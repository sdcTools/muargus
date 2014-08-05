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
public class MakeProtectedFileModel {
    private ArrayList<ArrayList<String>> suppressionVariablesTable;

    /**
     * 
     */
    public MakeProtectedFileModel() {
        this.suppressionVariablesTable = new ArrayList<>();
    }

    /**
     * 
     * @return 
     */
    public ArrayList<ArrayList<String>> getSuppressionVariablesTable() {
        return suppressionVariablesTable;
    }

    /**
     * 
     * @param suppressionVariablesTable 
     */
    public void setSuppressionVariablesTable(ArrayList<ArrayList<String>> suppressionVariablesTable) {
        this.suppressionVariablesTable = suppressionVariablesTable;
    }
     
}
