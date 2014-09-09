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
public class ModifyNumericalVariables {

    private ArrayList<VariableMu> variables;
    private String[][] variablesData;
    private final String[] variablesColumnNames = {"Modified", "Variable"};

    public ModifyNumericalVariables() {
    }

    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<VariableMu> variables) {
        this.variables = variables;
    }

    public void setVariablesData(String[][] variablesData) {
        this.variablesData = variablesData;
    }

    public String[][] getVariablesData() {
        return variablesData;
    }
    
    public boolean isModified(int index){
        return this.variablesData[index][0].equals("X");
    }
    
    public void setModified(int index, boolean modified) {
        this.variablesData[index][0] = (modified? "X": "");
    }
    
    public String getModifiedText(int index, boolean modified){
        this.variablesData[index][0] = (modified? "X": "");
        return this.variablesData[index][0];
    }

    public String[] getVariablesColumnNames() {
        return variablesColumnNames;
    }

}
