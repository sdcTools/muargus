/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

/**
 *
 * @author pibd05
 */
public class SpssVariable {
    private final String name;
    private final String dataType; //e.g. A2, F8, etc
    private final String variableType; //e.g. scale
    
    private boolean selected; //indicated whether selected in Argus meta or not

    public SpssVariable(String name, String dataType, String variableType) {
        this.name = name;
        this.dataType = dataType;
        this.variableType = variableType;
    }
    
    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getVariableType() {
        return variableType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
}
