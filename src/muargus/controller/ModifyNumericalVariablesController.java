/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.ModifyNumericalVariables;
import muargus.model.ModifyNumericalVariablesSpec;
import muargus.model.VariableMu;
import muargus.view.ModifyNumericalVariablesView;

/**
 *
 * @author ambargus
 */
public class ModifyNumericalVariablesController extends ControllerBase {

    ModifyNumericalVariables model;
    MetadataMu metadata;

    public ModifyNumericalVariablesController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new ModifyNumericalVariablesView(parentView, true, this));
        this.metadata = metadata;
        this.model = metadata.getCombinations().getModifyNumericalVariables();
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
        ArrayList<ModifyNumericalVariablesSpec> specs = new ArrayList<>();
        for (VariableMu v : this.metadata.getVariables()) {
            if (v.isNumeric()) {
                ModifyNumericalVariablesSpec spec = new ModifyNumericalVariablesSpec(v);
                specs.add(spec);
            }
        }
        this.model.setModifyNumericalVariablesSpec(specs);
    }

    public void setVariablesData() {
        if (this.model.getVariablesData() == null) {
            String variablesData[][] = new String[this.model.getModifyNumericalVariablesSpec().size()][2];
            int index = 0;
            for (ModifyNumericalVariablesSpec m : this.model.getModifyNumericalVariablesSpec()) {
                variablesData[index][0] = m.getModifiedText();
                variablesData[index][1] = m.getVariable().getName();
                index++;
            }
            this.model.setVariablesData(variablesData);
        }
    }

    public Double[] getMinMax(VariableMu variable) {
        Double[] min_max = getCalculationService().getMinMax(variable);
        return min_max;
    }

    public String getMin(ModifyNumericalVariablesSpec selected) {
        if (selected.getMin() == 0) {
            System.out.println("test");
            return "";
        } else {
            return getIntIfPossibel(selected.getMin());
        }
    }

    public String getMax(ModifyNumericalVariablesSpec selected) {
        return getIntIfPossibel(selected.getMax());
    }

    public String setValues(ModifyNumericalVariablesSpec selected, Double bottomValue, Double topValue,
            String bottomReplacement, String topReplacement, Double roundingBase, Double weightNoisePercentage) {

        String warningMessage = "";

        boolean bottom = false;
        if (!bottomValue.equals(Double.NaN)) {
            if (bottomReplacement.equals("")) {
                warningMessage += "Bottom replacement value cannot be empty\n";
            } else {
                    bottom = true;
                    if (bottomValue < selected.getMin() || bottomValue > selected.getMax()) {
                        warningMessage += "Bottom Value needs to be in the range between the minimum and maximum value\n";
                        bottom = false;
                    }
            }
        }

        if (!bottomReplacement.equals("") && bottomValue.equals(Double.NaN)) {
            warningMessage += "Bottom value cannot be empty\n";
            bottom = false;
        }

        boolean top = false;
        if (!topValue.equals(Double.NaN)) {
            if (topReplacement.equals("")) {
                warningMessage += "Top replacement value cannot be empty\n";
            } else {
                    top = true;
                    if (topValue < selected.getMin() || topValue > selected.getMax()) {
                        warningMessage += "Top Value needs to be in the range between the minimum and maximum value\n";
                        top = false;
                    }
            }
        }

        if (!topReplacement.equals("") && topValue.equals(Double.NaN)) {
            warningMessage += "Top value cannot be empty\n";
            top = false;
        }

        if (top && bottom) {
            if (topValue <= bottomValue) {
                warningMessage += "Top value needs to be larger than the bottom value\n";
                top = false;
                bottom = false;
            }

            if (bottom) {
                selected.setBottomValue(getIntIfPossibel(bottomValue));
                selected.setBottomReplacement(bottomReplacement);
            }

            if (top) {
                selected.setTopValue(getIntIfPossibel(topValue));
                selected.setTopReplacement(topReplacement);
            }

        }

        if (!roundingBase.equals(Double.NaN)) {
                if (roundingBase > 0) {
                    selected.setRoundingBase(getIntIfPossibel(roundingBase));
                } else {
                    warningMessage += "Illegal Value for rounding";
                }
        }

        if (!weightNoisePercentage.equals(Double.NaN)) {
                if (weightNoisePercentage > 0) {
                    selected.setWeightNoisePercentage(getIntIfPossibel(weightNoisePercentage));
                } else {
                    warningMessage += "Illegal Value for the weight noise percentage";
                }
        }

        return warningMessage;
    }

    public String getIntIfPossibel(double value) {
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
            if (!selected.getRoundingBase().equals(Double.NaN)) {
                getCalculationService().setRounding(selected.getVariable(), selected.getRoundingBase(), selected.getVariable().getDecimals());
            }
            if (!selected.getTopValue().equals(Double.NaN)) {
                getCalculationService().setTopBottomCoding(selected.getVariable(), true, selected.getTopValue(), selected.getTopReplacement());
            }
            if (!selected.getBottomValue().equals(Double.NaN)) {
                getCalculationService().setTopBottomCoding(selected.getVariable(), false, selected.getBottomValue(), selected.getBottomReplacement());
            }
            if (!selected.getWeightNoisePercentage().equals(Double.NaN)) {
                getCalculationService().setWeightNoise(selected.getVariable(), selected.getWeightNoisePercentage());
            }
        } catch (ArgusException ex) {
            System.out.println("error");
            getView().showErrorMessage(ex);
            //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
