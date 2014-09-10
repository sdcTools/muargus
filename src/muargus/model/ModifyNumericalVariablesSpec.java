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
    private boolean modified = false;
    private double[] min_max;
    private double bottomValue;
    private double topValue;
    private String bottomReplacement;
    private String topReplacement;
    private double roundingBase;
    private double weightNoisePercentage;

    public ModifyNumericalVariablesSpec(VariableMu variable) {
        this.variable = variable;
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

    public double getBottomValue() {
        return bottomValue;
    }

    public void setBottomValue(double bottomValue) {
        this.bottomValue = bottomValue;
    }

    public double getTopValue() {
        return topValue;
    }

    public void setTopValue(double topValue) {
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

    public double getRoundingBase() {
        return roundingBase;
    }

    public void setRoundingBase(double roundingBase) {
        this.roundingBase = roundingBase;
    }

    public double getWeightNoisePercentage() {
        return weightNoisePercentage;
    }

    public void setWeightNoisePercentage(double weightNoisePercentage) {
        this.weightNoisePercentage = weightNoisePercentage;
    }
    
}
