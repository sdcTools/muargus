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
public class SpecifyMetadataModel {
    private String variableCategoryPath;
    private ArrayList<String> formatTypes;
    private ArrayList<String> variables;
    // TODO: change type to generic type T 
    private ArrayList<String> defaultValues;

    /**
     * 
     */
    public SpecifyMetadataModel() {
        this.variableCategoryPath = "";
        this.formatTypes = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.defaultValues = new ArrayList<>();
    }

    /**
     * 
     * @return 
     */
    public String getVariableCategoryPath() {
        return variableCategoryPath;
    }

    /**
     * 
     * @param variableCategoryPath 
     */
    public void setVariableCategoryPath(String variableCategoryPath) {
        this.variableCategoryPath = variableCategoryPath;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<String> getFormatTypes() {
        return formatTypes;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<String> getVariables() {
        return variables;
    }

    /**
     * 
     * @param variables 
     */
    public void setVariables(ArrayList<String> variables) {
        this.variables = variables;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<String> getDefaultValues() {
        return defaultValues;
    }

    /**
     * 
     * @param defaultValues 
     */
    public void setDefaultValues(ArrayList<String> defaultValues) {
        this.defaultValues = defaultValues;
    }
    
}
