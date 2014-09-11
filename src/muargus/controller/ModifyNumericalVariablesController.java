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
public class ModifyNumericalVariablesController {

    ModifyNumericalVariablesView view;
    ModifyNumericalVariables model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public ModifyNumericalVariablesController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.view = new ModifyNumericalVariablesView(parentView, true, this);
        this.metadataMu = metadataMu;
        this.calculationService = MuARGUS.getCalculationService();
        this.view.setMetadataMu(this.metadataMu);
    }

    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        this.view.setVisible(true);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        this.view.setVisible(false);
    }

    /**
     * Fuction for setting the model. This function is used by the view after
     * setting the model itself
     *
     * @param model the model class of the ShowTableCollection screen
     */
    public void setModel(ModifyNumericalVariables model) {
        this.model = model;
    }

    /**
     *
     */
    public void setModifyNumericalVariablesSpecs() {
        ArrayList<ModifyNumericalVariablesSpec> specs = new ArrayList<>();
        for (VariableMu v : this.metadataMu.getVariables()) {
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
                variablesData[index][0] = (m.isModified() ? "X" : "");
                variablesData[index][1] = m.getVariable().getName();
                index++;
            }
            this.model.setVariablesData(variablesData);
        }
    }

    public double[] getMinMax(VariableMu variable) {
        double[] min_max = this.calculationService.getMinMax(variable);
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

//    public String getBottomValue(ModifyNumericalVariablesSpec selected) {
//        String temp = this.view.getBottomValueTextField();
//        if(!selected.getBottomValue().isNaN()){
//            temp = getIntIfPossibel(selected.getBottomValue());
//        }
//        return temp;
//    }
//
//    public String getTopValue(ModifyNumericalVariablesSpec selected) {
//        String temp = this.view.getTopValueTextField();
//        if(!selected.getBottomValue().isNaN()){
//            temp = getIntIfPossibel(selected.getTopValue());
//        }
//        return temp;
//    }
//
//    public String getRoundingBase(ModifyNumericalVariablesSpec selected) {
//        String temp = this.view.getRoundingBaseTextField();
//        if(selected.isModified()){
//            
//        }
//        if(!selected.getBottomValue().isNaN()){
//            temp = getIntIfPossibel(selected.getRoundingBase());
//        }
//        return temp;
//    }
//
//    public String getWeightNoisePercentage(ModifyNumericalVariablesSpec selected) {
//        String temp = this.view.getPercentageTextField();
//        if(!selected.getBottomValue().isNaN()){
//            temp = getIntIfPossibel(selected.getWeightNoisePercentage());
//        }
//        return temp;
//    }

    public String setValues(ModifyNumericalVariablesSpec selected, String bottomValue_String, String topValue_String,
            String bottomReplacement, String topReplacement, String roundingBase_String, String weightNoisePercentage_String) {

        String warningMessage = "";

        Double bottomValue_double = null;
        boolean bottomValue = false;
        if (!bottomValue_String.equals("")) {
            if (bottomReplacement.equals("")) {
                warningMessage += "Bottom replacement value cannot be empty\n";
            } else {
                try {
                    bottomValue_double = StrUtils.toDouble(bottomValue_String);
                    bottomValue = true;
                    if (bottomValue_double < selected.getMin() || bottomValue_double > selected.getMax()) {
                        warningMessage += "Bottom Value needs to be in the range between the minimum and maximum value\n";
                        bottomValue = false;
                    }
                } catch (ArgusException ex) {
                    warningMessage += "illegal bottom value\n";
                    bottomValue = false;
                    //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (!bottomReplacement.equals("") && bottomValue_String.equals("")) {
            warningMessage += "Bottom value cannot be empty\n";
            bottomValue = false;
        }

        Double topValue_double = null;
        boolean topValue = false;
        if (!topValue_String.equals("")) {
            if (topReplacement.equals("")) {
                warningMessage += "Top replacement value cannot be empty\n";
            } else {
                try {
                    topValue_double = StrUtils.toDouble(topValue_String);
                    topValue = true;
                    if (topValue_double < selected.getMin() || topValue_double > selected.getMax()) {
                        warningMessage += "Top Value needs to be in the range between the minimum and maximum value\n";
                        topValue = false;
                    }
                } catch (ArgusException ex) {
                    warningMessage += "illegal top value\n";
                    topValue = false;
                    //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        if (!topReplacement.equals("") && topValue_String.equals("")) {
            warningMessage += "Top value cannot be empty\n";
            topValue = false;
        }

        if (topValue && bottomValue) {
            if (topValue_double <= bottomValue_double) {
                warningMessage += "Top value needs to be larger than the bottom value\n";
                topValue = false;
                bottomValue = false;
            }

            if (bottomValue) {
                selected.setBottomValue(getIntIfPossibel(bottomValue_double));
                selected.setBottomReplacement(bottomReplacement);
            }

            if (topValue) {
                selected.setTopValue(getIntIfPossibel(topValue_double));
                selected.setTopReplacement(topReplacement);
            }

        }

        if (!roundingBase_String.equals("")) {
            try {
                double roundingBase_double = StrUtils.toDouble(roundingBase_String);
                if (roundingBase_double > 0) {
                    selected.setRoundingBase(getIntIfPossibel(roundingBase_double));
                } else {
                    warningMessage += "Illegal Value for rounding";
                }
            } catch (ArgusException ex) {
                warningMessage += "Illegal Value for rounding";
                //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!weightNoisePercentage_String.equals("")) {
            try {
                double weightNoisePercentage_double = StrUtils.toDouble(weightNoisePercentage_String);
                if (weightNoisePercentage_double > 0) {
                    selected.setWeightNoisePercentage(getIntIfPossibel(weightNoisePercentage_double));
                } else {
                    warningMessage += "Illegal Value for the weight noise percentage";
                }
            } catch (ArgusException ex) {
                warningMessage += "Illegal Value for the weight noise percentage";
                //Logger.getLogger(ModifyNumericalVariablesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return warningMessage;
    }

    public String getIntIfPossibel(double value) {
        double value_double = value;
        String value_String;
        if ((value_double == Math.floor(value_double)) && !Double.isInfinite(value_double)) {
            int value_int = (int) value_double;
            value_String = Integer.toString(value_int);
        } else {
            value_String = Double.toString(value_double);
        }
        return value_String;
    }
}
