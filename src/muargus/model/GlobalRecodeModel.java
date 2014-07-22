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
public class GlobalRecodeModel {
    private ArrayList<Integer> defaultValues;
    private ArrayList<ArrayList<String>> variablesTable;
    private ArrayList<String> editBoxValues;
    private String codelistPath;
    private String nameCodelistPath;

    /**
     * 
     */
    public GlobalRecodeModel() {
        this.defaultValues = new ArrayList<>();
        this.variablesTable = new ArrayList<>();
        this.editBoxValues = new ArrayList<>();
        this.codelistPath = "";
        this.nameCodelistPath = "";
    }

    /**
     * 
     * @return 
     */
    public ArrayList<Integer> getDefaultValues() {
        return defaultValues;
    }

    /**
     * 
     * @param defaultValues 
     */
    public void setDefaultValues(ArrayList<Integer> defaultValues) {
        this.defaultValues = defaultValues;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<ArrayList<String>> getVariablesTable() {
        return variablesTable;
    }

    /**
     * 
     * @param variablesTable 
     */
    public void setVariablesTable(ArrayList<ArrayList<String>> variablesTable) {
        this.variablesTable = variablesTable;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<String> getEditBoxValues() {
        return editBoxValues;
    }

    /**
     * 
     * @param editBoxValues 
     */
    public void setEditBoxValues(ArrayList<String> editBoxValues) {
        this.editBoxValues = editBoxValues;
    }

    /**
     * 
     * @return 
     */
    public String getCodelistPath() {
        return codelistPath;
    }

    /**
     * 
     * @param codelistPath 
     */
    public void setCodelistPath(String codelistPath) {
        this.codelistPath = codelistPath;
    }

    /**
     * 
     * @return 
     */
    public String getNameCodelistPath() {
        return nameCodelistPath;
    }

    /**
     * 
     * @param nameCodelistPath 
     */
    public void setNameCodelistPath(String nameCodelistPath) {
        this.nameCodelistPath = nameCodelistPath;
    }
    
}
