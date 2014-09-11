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

    private ArrayList<ModifyNumericalVariablesSpec> modifyNumericalVariablesSpec =  null;
    private String[][] variablesData;
    private final String[] variablesColumnNames = {"Modified", "Variable"};

    public ModifyNumericalVariables() {
        System.out.println("Model");
    }

    public ArrayList<ModifyNumericalVariablesSpec> getModifyNumericalVariablesSpec() {
        return modifyNumericalVariablesSpec;
    }

    public void setModifyNumericalVariablesSpec(ArrayList<ModifyNumericalVariablesSpec> modifyNumericalVariablesSpec) {
        this.modifyNumericalVariablesSpec = modifyNumericalVariablesSpec;
    }

    public void setVariablesData(String[][] variablesData) {
        this.variablesData = variablesData;
    }

    public String[][] getVariablesData() {
        return variablesData;
    }
    
    public boolean isModified(int index){
        return this.modifyNumericalVariablesSpec.get(index).isModified();
    }
    
    public void setModified(int index, boolean modified) {
        this.modifyNumericalVariablesSpec.get(index).setModified(modified);
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
