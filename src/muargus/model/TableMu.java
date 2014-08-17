/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ambargus
 */
public class TableMu {
    
    private boolean riskModel = false;
    private int threshold = 1;
    private ArrayList<VariableMu> variables = new ArrayList<>();
    
    
    public TableMu(){
        
    }

    public boolean isRiskModel() {
        return riskModel;
    }

    public void setRiskModel(boolean riskModel) {
        this.riskModel = riskModel;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = Integer.parseInt(threshold);
    }

    public ArrayList<VariableMu> getVariables() {
        return variables;
    }
    
    public void addVariable(VariableMu variable){
        this.variables.add(variable);
    }

    public void setVariables(VariableMu[] variables) {
        this.variables.addAll(Arrays.asList(variables));
    }
    
    public void setVariables(ArrayList<VariableMu> variables){
        this.variables.addAll(variables);
    }
    
    public String[] getTable(){
        String[] table = new String[variables.size()+2];
        if(isRiskModel()){
            table[0] = "R";
        } else {
            table[0] = "";
        }
        table[1] = Integer.toString(threshold);
        for(int i = 0; i < variables.size(); i++){
            table[i+2] = variables.get(i).getName();
        }
        return table;
    }
}
