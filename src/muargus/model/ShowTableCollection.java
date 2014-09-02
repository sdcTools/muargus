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
public class ShowTableCollection {

    
    private ArrayList<VariableMu> variables;
    private boolean showAllTables;
    private VariableMu selectedVariable;
    
    public ShowTableCollection() {
    }
    
    public void setVariables(ArrayList<VariableMu> variables){
        this.variables = variables;
    }
    
    public ArrayList<VariableMu> getVariables(){
        return this.variables;
    }

    public boolean isShowAllTables() {
        return showAllTables;
    }

    public void setShowAllTables(boolean showAllTables) {
        this.showAllTables = showAllTables;
    }

    public VariableMu getSelectedVariable() {
        return selectedVariable;
    }

    public void setSelectedVariable(VariableMu selectedVariable) {
        this.selectedVariable = selectedVariable;
    }
    
}
