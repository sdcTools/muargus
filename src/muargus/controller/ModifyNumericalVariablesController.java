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
 * @author Statistics Netherlands
 */
public class ModifyNumericalVariablesController extends ControllerBase<ModifyNumericalVariables> {

    private final MetadataMu metadata;

    /**
     *
     * @param parentView
     * @param metadata
     */
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

    /**
     *
     */
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

    /**
     *
     * @param variable
     * @return
     */
    public double[] getMinMax(VariableMu variable) {
        double[] min_max = getCalculationService().getMinMax(variable);
        return min_max;
    }

    /**
     *
     * @param selected
     * @return
     */
    public String getMin(ModifyNumericalVariablesSpec selected) {
        return getIntIfPossible(selected.getMin());
    }

    /**
     *
     * @param selected
     * @return
     */
    public String getMax(ModifyNumericalVariablesSpec selected) {
        return getIntIfPossible(selected.getMax());
    }

    /**
     *
     * @param selected
     * @param bottomValue
     * @param topValue
     * @param bottomReplacement
     * @param topReplacement
     * @param roundingBase
     * @param weightNoisePercentage
     * @return
     */
    public String getWarningMessage(ModifyNumericalVariablesSpec selected, String bottomValue, String topValue,
            String bottomReplacement, String topReplacement, String roundingBase, String weightNoisePercentage) {

        String warningMessage = "";

        boolean bottom = false;
        try {
            Double temp = Double.parseDouble(bottomValue);
            bottom = true;
            if (temp < selected.getMin() || temp > selected.getMax()) {
                warningMessage += "Bottom Value needs to be in the range between the minimum and maximum value\n";
                bottom = false;
            }
        } catch (NumberFormatException e) {
            if (bottomReplacement.equals("") && !bottomValue.equals("")) {
                warningMessage += "Bottom replacement value cannot be empty\n";
            } else if (!bottomValue.equals("")) {
                warningMessage += "Illegal value for the bottom value\n";
                bottom = false;
            } else if (!bottomReplacement.equals("")) {
                warningMessage += "Bottom value cannot be empty\n";
                bottom = false;
            }
        }

        //TODO: change into try-catch
//        if (!Double.isNaN(Double.parseDouble(bottomValue))) {
//            if (bottomReplacement.equals("")) {
//                warningMessage += "Bottom replacement value cannot be empty\n";
//            } else {
//                bottom = true;
//                if (Double.parseDouble(bottomValue) < selected.getMin()
//                        || Double.parseDouble(bottomValue) > selected.getMax()) {
//                    warningMessage += "Bottom Value needs to be in the range between the minimum and maximum value\n";
//                    bottom = false;
//                }
//            }
//        } else if (!bottomValue.equals("")) {
//            warningMessage += "Illegal value for the bottom value\n";
//            bottom = false;
//        } else if (!bottomReplacement.equals("")) {
//            warningMessage += "Bottom value cannot be empty\n";
//            bottom = false;
//        }
        boolean top = false;
        try {
            Double temp = Double.parseDouble(topValue);
            top = true;
            if (temp < selected.getMin() || temp > selected.getMax()) {
                warningMessage += "Top Value needs to be in the range between the minimum and maximum value\n";
                top = false;
            }
        } catch (NumberFormatException e) {
            if (topReplacement.equals("") && !topValue.equals("")) {
                warningMessage += "Top replacement value cannot be empty\n";
            } else if (!topValue.equals("")) {
                warningMessage += "Illegal value for the top value\n";
                top = false;
            } else if (!topReplacement.equals("")) {
                warningMessage += "Top value cannot be empty\n";
                top = false;
            }
        }

        // top = false;
//        if (!Double.isNaN(Double.parseDouble(topValue))) {
//            if (topReplacement.equals("")) {
//                warningMessage += "Top replacement value cannot be empty\n";
//            } else {
//                top = true;
//                if (Double.parseDouble(topValue) < selected.getMin()
//                        || Double.parseDouble(topValue) > selected.getMax()) {
//                    warningMessage += "Top Value needs to be in the range between the minimum and maximum value\n";
//                    top = false;
//                }
//            }
//        } else if (!topValue.equals("")) {
//            warningMessage += "Illegal value for the top value\n";
//            top = false;
//        } else if (!topReplacement.equals("")) {
//            warningMessage += "Top value cannot be empty\n";
//            top = false;
//        }
        if (top && bottom) {
            if (Double.parseDouble(topValue) <= Double.parseDouble(bottomValue)) {
                warningMessage += "Top value needs to be larger than the bottom value\n";
            }
        }

        try {
            Double temp = Double.parseDouble(roundingBase);
            if (temp <= 0) {
                warningMessage += "Illegal Value for rounding\n";
            }
        } catch (NumberFormatException e) {
            if (!roundingBase.equals("")) {
                warningMessage += "Illegal value for the rounding base\n";
            }
        }

//        if (!Double.isNaN(Double.parseDouble(roundingBase))) {
//            if (Double.parseDouble(roundingBase) <= 0) {
//                warningMessage += "Illegal Value for rounding\n";
//            }
//        } else if (!roundingBase.equals("")) {
//            warningMessage += "Illegal value for the rounding base\n";
//        }
        try {
            Double temp = Double.parseDouble(weightNoisePercentage);
            if (temp <= 0 || temp > 100) {
                warningMessage += "Illegal Value for the weight noise percentage\n"
                        + "Percentage needs to be a number greater than 0 and less than or equal to 100";
            }
        } catch (NumberFormatException e) {
            if (!weightNoisePercentage.equals("")) {
                warningMessage += "Illegal value for the weight noise percentage\n";
            }
        }

//        if (!Double.isNaN(Double.parseDouble(weightNoisePercentage))) {
//            if (Double.parseDouble(weightNoisePercentage) <= 0
//                    || Double.parseDouble(weightNoisePercentage) > 100) {
//                warningMessage += "Illegal Value for the weight noise percentage\n"
//                        + "Percentage needs to be a number greater than 0 and less than or equal to 100";
//            }
//        } else if (!weightNoisePercentage.equals("")) {
//            warningMessage += "Illegal value for the weight noise percentage\n";
//        }
        return warningMessage;
    }

    /**
     *
     * @param value
     * @return
     */
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
            getView().showErrorMessage(ex);
        }
        return value_String;
    }

    /**
     *
     * @param selected
     */
    public void apply(ModifyNumericalVariablesSpec selected) {
        try {
            getCalculationService().setRounding(
                    selected.getVariable(),
                    selected.getRoundingBase(),
                    selected.getVariable().getDecimals(),
                    Double.isNaN(selected.getRoundingBase())); // als het geen normaal getal is, dan is undo true
            getCalculationService().setTopBottomCoding(
                    selected.getVariable(),
                    true,
                    selected.getTopValue(),
                    selected.getTopReplacement(),
                    Double.isNaN(selected.getTopValue()));
            getCalculationService().setTopBottomCoding(
                    selected.getVariable(),
                    false,
                    selected.getBottomValue(),
                    selected.getBottomReplacement(),
                    Double.isNaN(selected.getBottomValue()));
            if (selected.getVariable().isWeight()) {
                getCalculationService().setWeightNoise(
                        selected.getVariable(),
                        selected.getWeightNoisePercentage(),
                        Double.isNaN(selected.getWeightNoisePercentage()));
            }
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }
}
