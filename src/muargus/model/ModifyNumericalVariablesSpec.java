/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import argus.model.ArgusException;
import argus.utils.StrUtils;

/**
 *
 * @author ambargus
 */
public class ModifyNumericalVariablesSpec {

    private final VariableMu variable;
    private boolean modified;
    private String modifiedText;
    private Double[] min_max;
    private Double bottomValue;
    private Double topValue;
    private String bottomReplacement;
    private String topReplacement;
    private Double roundingBase;
    private Double weightNoisePercentage;

    public ModifyNumericalVariablesSpec(VariableMu variable) {
        this.variable = variable;
        this.modified = false;
        this.modifiedText = "";
        this.bottomValue = Double.NaN;
        this.topValue = Double.NaN;
        this.roundingBase = Double.NaN;
        this.weightNoisePercentage = Double.NaN;
    }

    public VariableMu getVariable() {
        return this.variable;
    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        setModifiedText();
    }

    public String getModifiedText() {
        return this.modifiedText;
    }

    public void setModifiedText() {
        this.modifiedText = (this.modified ? "X" : "");;
    }

    public Double getMin() {
        return this.min_max[0];
    }

    public Double getMax() {
        return this.min_max[1];
    }

    public void setMin_max(Double[] min_max) {
        this.min_max = min_max;
    }

    public Double getBottomValue() {
        return this.bottomValue;
    }
    
    public void setBottomValue(String bottomValue) {
        try {
            this.bottomValue = StrUtils.toDouble(bottomValue);
        } catch (ArgusException e) {
            this.bottomValue = Double.NaN;
        }
    }
    
    public void setBottomValue(Double bottomValue){
        this.bottomValue = bottomValue;
    }

    public Double getTopValue() {
        return this.topValue;
    }

    public void setTopValue(String topValue) {
        try {
            this.topValue = StrUtils.toDouble(topValue);
        } catch (ArgusException e) {
            this.topValue = Double.NaN;
        }
    }
    
    public void setTopValue(Double topValue) {
        this.topValue = topValue;
    }

    public String getBottomReplacement() {
        return this.bottomReplacement;
    }

    public void setBottomReplacement(String bottomReplacement) {
        this.bottomReplacement = bottomReplacement;
    }

    public String getTopReplacement() {
        return this.topReplacement;
    }

    public void setTopReplacement(String topReplacement) {
        this.topReplacement = topReplacement;
    }

    public Double getRoundingBase() {
        return this.roundingBase;
    }

    public void setRoundingBase(String roundingBase) {
        try {
            this.roundingBase = StrUtils.toDouble(roundingBase);
        } catch (ArgusException e) {
            this.roundingBase = Double.NaN;
        }
    }
    
    public void setRoundingBase(Double roundingBase) {
        this.roundingBase = roundingBase;
    }

    public Double getWeightNoisePercentage() {
        return this.weightNoisePercentage;
    }

    public void setWeightNoisePercentage(String weightNoisePercentage) {
        try {
            this.weightNoisePercentage = StrUtils.toDouble(weightNoisePercentage);
        } catch (ArgusException e) {
            this.weightNoisePercentage = Double.NaN;
        }
    }
    
    public void setWeightNoisePercentage(Double weightNoisePercentage){
        this.weightNoisePercentage = weightNoisePercentage;
    }

}
