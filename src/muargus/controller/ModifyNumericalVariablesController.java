/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import muargus.model.MetadataMu;
import muargus.model.ModifyNumericalVariables;
import muargus.model.ModifyNumericalVariablesSpec;
import muargus.model.VariableMu;
import muargus.view.ModifyNumericalVariablesView;

/**
 *
 * @author ambargus
 */
public class ModifyNumericalVariablesController extends ControllerBase<ModifyNumericalVariables> {

    private final MetadataMu metadata;

    public ModifyNumericalVariablesController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new ModifyNumericalVariablesView(parentView, true, this));
        this.metadata = metadata;
        setModel(metadata.getCombinations().getModifyNumericalVariables());
        getView().setMetadata(this.metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     *
     */
    public void setModifyNumericalVariablesSpecs() {
        for (VariableMu v : this.metadata.getVariables()) {
            if (v.isNumeric()) {
                ModifyNumericalVariablesSpec spec = new ModifyNumericalVariablesSpec(v);
                getModel().getModifyNumericalVariablesSpec().add(spec);
            }
        }
    }

    public void setVariablesData() {
        if (getModel().getVariablesData() == null) {
            String variablesData[][] = new String[getModel().getModifyNumericalVariablesSpec().size()][2];
            int index = 0;
            for (ModifyNumericalVariablesSpec m : getModel().getModifyNumericalVariablesSpec()) {
                variablesData[index][0] = m.getModifiedText();
                variablesData[index][1] = m.getVariable().getName();
                index++;
            }
            getModel().setVariablesData(variablesData);
        }
    }

    public double[] getMinMax(VariableMu variable) {
        double[] min_max = getCalculationService().getMinMax(variable);
        return min_max;
    }

    public String getMin(ModifyNumericalVariablesSpec selected) {
        if (selected.getMin() == 0) {
            System.out.println("test");
            return "";
        } else {
            return getIntIfPossible(selected.getMin());
        }
    }

    public String getMax(ModifyNumericalVariablesSpec selected) {
        return getIntIfPossible(selected.getMax());
    }

    public String getWarningMessage(ModifyNumericalVariablesSpec selected, String bottomValue, String topValue,
            String bottomReplacement, String topReplacement, String roundingBase, String weightNoisePercentage) {

        String warningMessage = "";

        boolean bottom = false;
        if (!Double.isNaN(selected.getBottomValue())) {
            if (bottomReplacement.equals("")) {
                warningMessage += "Bottom replacement value cannot be empty\n";
            } else {
                bottom = true;
                if (selected.getBottomValue() < selected.getMin() || selected.getBottomValue() > selected.getMax()) {
                    warningMessage += "Bottom Value needs to be in the range between the minimum and maximum value\n";
                    bottom = false;
                }
            }
        } else if (!bottomValue.equals("")) {
            warningMessage += "Illegal value for the bottom value\n";
            bottom = false;
        } else if (!bottomReplacement.equals("")) {
            warningMessage += "Bottom value cannot be empty\n";
            bottom = false;
        }

        boolean top = false;
        if (!Double.isNaN(selected.getTopValue())) {
            if (topReplacement.equals("")) {
                warningMessage += "Top replacement value cannot be empty\n";
            } else {
                top = true;
                if (selected.getTopValue() < selected.getMin() || selected.getTopValue() > selected.getMax()) {
                    warningMessage += "Top Value needs to be in the range between the minimum and maximum value\n";
                    top = false;
                }
            }
        } else if (!topValue.equals("")) {
            warningMessage += "Illegal value for the top value\n";
            top = false;
        } else if (!topReplacement.equals("")) {
            warningMessage += "Top value cannot be empty\n";
            top = false;
        }

        if (top && bottom) {
            if (selected.getTopValue() <= selected.getBottomValue()) {
                warningMessage += "Top value needs to be larger than the bottom value\n";
            }
        }

        if (!Double.isNaN(selected.getRoundingBase())) {
            if (selected.getRoundingBase() <= 0) {
                warningMessage += "Illegal Value for rounding\n";
            }
        } else if (!roundingBase.equals("")) {
            warningMessage += "Illegal value for the rounding base\n";
        }

        if (!Double.isNaN(selected.getWeightNoisePercentage())) {
            if (selected.getWeightNoisePercentage() <= 0 || selected.getWeightNoisePercentage() > 100) {
                warningMessage += "Illegal Value for the weight noise percentage\n"
                        + "Percentage needs to be a number larger than 0 and smaller than or equal to 100";
            }
        } else if (!weightNoisePercentage.equals("")) {
            warningMessage += "Illegal value for the weight noise percentage\n";
        }

        return warningMessage;
    }

    public String getIntIfPossible(double value) {
        double value_double;
        String value_String = null;
        try {
            value_double = StrUtils.toDouble(Double.toString(value));
            if ((value_double == Math.floor(value_double)) && !Double.isInfinite(value_double)) {
                int value_int = (int) value_double;
                value_String = Integer.toString(value_int);
            } else {
                value_String = Double.toString(value_double);
            }
        } catch (ArgusException ex) {
            System.out.println("warning");
            //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value_String;
    }

    public void apply(ModifyNumericalVariablesSpec selected) {
        try {
                getCalculationService().setRounding(selected.getVariable(), selected.getRoundingBase(), selected.getVariable().getDecimals());
                getCalculationService().setTopBottomCoding(selected.getVariable(), true, selected.getTopValue(), selected.getTopReplacement());
                getCalculationService().setTopBottomCoding(selected.getVariable(), false, selected.getBottomValue(), selected.getBottomReplacement());
                if (selected.getVariable().isWeight()) {
                    getCalculationService().setWeightNoise(selected.getVariable(), selected.getWeightNoisePercentage());
                }
        } catch (ArgusException ex) {
            System.out.println("error");
            getView().showErrorMessage(ex);
            //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
