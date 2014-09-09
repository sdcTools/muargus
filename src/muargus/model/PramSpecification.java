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
    
    private boolean useBandwidth = false;
    private final int defaultProbability = 80;
    private ArrayList<PramVariableSpec> pramVarSpec;
    private final String[] variablesColumnNames = {"P", "BW", "Variable"};
    private final String[] codesColumnNames = {"Code", "Label", "Prob."};
    private String[][] variablesData;
    
    public PramSpecification() {
        
    }

    public boolean useBandwidth() {
        return useBandwidth;
    }

    public void setUseBandwidth(boolean useBandwidth) {
        this.useBandwidth = useBandwidth;
    }

    public int getDefaultProbability() {
        return defaultProbability;
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
