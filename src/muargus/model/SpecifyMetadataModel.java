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
    private static ArrayList<VariableMu> variables;
    // TODO: change type to generic type T 
    private ArrayList<String> defaultValues;

    /**
     * 
     */
    public SpecifyMetadataModel() {
        this.variableCategoryPath = "";
        this.formatTypes = new ArrayList<>();
        SpecifyMetadataModel.variables = new ArrayList<>();
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
    public static ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * 
     * @param variables 
     */
    public static void setVariables(ArrayList<VariableMu> variables) {
        SpecifyMetadataModel.variables = variables;
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
