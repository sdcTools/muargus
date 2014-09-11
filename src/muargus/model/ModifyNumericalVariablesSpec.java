/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

/**
 *
 * @author ambargus
 */
public class ModifyNumericalVariablesSpec {
    
    private final VariableMu variable;
    private boolean modified;
    private double[] min_max;
    private String bottomValue;
    private String topValue;
    private String bottomReplacement;
    private String topReplacement;
    private String roundingBase;
    private String weightNoisePercentage;

    public ModifyNumericalVariablesSpec(VariableMu variable) {
        this.variable = variable;
        this.modified = false;
    }

    public VariableMu getVariable() {
        return variable;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public double getMin() {
        return min_max[0];
    }
    
    public double getMax() {
        return min_max[1];
    }

    public void setMin_max(double[] min_max) {
        this.min_max = min_max;
    }

    public String getBottomValue() {
        return bottomValue;
    }

    public void setBottomValue(String bottomValue) {
        this.bottomValue = bottomValue;
    }

    public String getTopValue() {
        return topValue;
    }

    public void setTopValue(String topValue) {
        this.topValue = topValue;
    }

    public String getBottomReplacement() {
        return bottomReplacement;
    }

    public void setBottomReplacement(String bottomReplacement) {
        this.bottomReplacement = bottomReplacement;
    }

    public String getTopReplacement() {
        return topReplacement;
    }

    public void setTopReplacement(String topReplacement) {
        this.topReplacement = topReplacement;
    }

    public String getRoundingBase() {
        return roundingBase;
    }

    public void setRoundingBase(String roundingBase) {
        this.roundingBase = roundingBase;
    }

    public String getWeightNoisePercentage() {
        return weightNoisePercentage;
    }

    public void setWeightNoisePercentage(String weightNoisePercentage) {
        this.weightNoisePercentage = weightNoisePercentage;
    }
    
}
