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
    private final MeasurementLevel variableType; //e.g. scale
    //private final double missings;

    private int variableLength = 0;
    private int numberOfDecimals = 0;
    private boolean numeric = false;
    private boolean categorical = false;
    private double[] missing;

    private boolean selected; //indicated whether selected in Argus meta or not

    public SpssVariable(String name, int formatDecimal, int formatWidth, double[] missing, MeasurementLevel variableType) {
        this.name = name;
        this.numberOfDecimals = formatDecimal;
        this.variableLength = formatWidth;
        this.missing = missing;
        this.variableType = variableType;
        setData();
    }

    private void setData() {
//        if (dataType.substring(0, 1).equals("A")) {
//            this.categorical = true;
//        } else {
//            this.numeric = true;
//        }

//        if (format.contains(".")) {
//            this.variableLength = Integer.parseInt(format.substring(1, format.indexOf(".")));
//            this.numberOfDecimals = Integer.parseInt(format.substring(format.indexOf(".") + 1, format.length()));
//        } else {
//            this.variableLength = Integer.parseInt(format.substring(1));
//        }
        System.out.print(variableLength + "\t" + numberOfDecimals + "\t" + variableType.toString());
        for (int i = 0; i < missing.length; i++) {
            System.out.print("\t" + this.missing[0]);
        }

        System.out.println("");

//        if (!this.missings.equals("")) {
//            if (this.missings.contains(",")) {
//                this.missing[0] = this.missings.substring(0, this.missings.indexOf(","));
//                this.missing[1] = this.missings.substring(this.missings.indexOf(",") + 1);
//            } else {
//                this.missing[0] = this.missings;
//            }
//        }
//        switch (variableType) {
//            case (1):
//                this.numeric = true;
//                break;
//            case (2):
//                this.categorical = true;
//                this.numeric = true;
//                break;
//            case (3):
//                this.categorical = true;
//                break;
//            default:
//                break;
//        }
    }

    public String getName() {
        return name;
    }

//    public VariableFormat getDataType() {
//        return format;
//    }
//    public int getVariableType() {
//        return variableType;
//    }

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

//    public double getMissings() {
//        return missings;
//    }
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
