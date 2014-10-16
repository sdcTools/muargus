/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import com.ibm.statistics.plugin.MeasurementLevel;

/**
 *
 * @author pibd05
 */
public class SpssVariable {

    private final String name;
    private final MeasurementLevel variableType; //e.g. scale, nominal, ordinal
    private int variableLength = 0;
    private int numberOfDecimals = 0;
    private boolean numeric = false;
    private boolean categorical = false;
    private final double[] missing;

    private boolean selected; //indicated whether selected in Argus meta or not

    public SpssVariable(String name, int formatDecimal, int formatWidth, double[] missing, MeasurementLevel variableType) {
        this.name = name;
        this.numberOfDecimals = formatDecimal;
        this.variableLength = formatWidth;
        this.missing = missing;
        this.variableType = variableType;
        setMeasurementLevel();
    }

    private void setMeasurementLevel() {
        switch (variableType.toString()) {
            case ("SCALE"):
                this.numeric = true;
                break;
            case ("ORDINAL"):
                this.categorical = true;
                this.numeric = true;
                break;
            case ("NOMINAL"):
                this.categorical = true;
                break;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }

    public MeasurementLevel getVariableType() {
        return variableType;
    }

    public int getVariableLength() {
        return variableLength;
    }

    public int getNumberOfDecimals() {
        return numberOfDecimals;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public boolean isCategorical() {
        return categorical;
    }

    public double[] getMissing() {
        return missing;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
