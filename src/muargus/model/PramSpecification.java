/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public class PramSpecification {
    
    private boolean useBandwidth;
    private int defaultProbability;
    private ArrayList<PramVariableSpec> pramVarSpec;
    private String[] variablesColumnNames = {"P", "BW", "Variable"};
    private String[] codesColumnNames = {"Code", "Label", "Prob."};
    private String[][] variablesData;
    
    public PramSpecification() {
        
    }
    
    

    public ArrayList<PramVariableSpec> getPramVarSpec() {
        return this.pramVarSpec;
    }

    public void setPramVarSpec(ArrayList<PramVariableSpec> pramVarSpec) {
        this.pramVarSpec = pramVarSpec;
    }

    public String[] getVariablesColumnNames() {
        return variablesColumnNames;
    }

    public String[] getCodesColumnNames() {
        return this.codesColumnNames;
    }

    public String[][] getVariablesData() {
        return variablesData;
    }

    public void setVariablesData(String[][] variablesData) {
        this.variablesData = variablesData;
    }
    
}
