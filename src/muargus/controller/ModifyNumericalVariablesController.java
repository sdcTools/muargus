package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import argus.utils.SystemUtils;
import muargus.model.MetadataMu;
import muargus.model.ModifyNumericalVariables;
import muargus.model.ModifyNumericalVariablesSpec;
import muargus.model.VariableMu;
import muargus.view.ModifyNumericalVariablesView;

/**
 * The Controller class of the ModifyNumericalVariables screen.
 *
 * @author Statistics Netherlands
 */
public class ModifyNumericalVariablesController extends ControllerBase<ModifyNumericalVariables> {

    private final MetadataMu metadata;

    /**
     * Constructor for the ModifyNumericalVariablesController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
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
     * Sets the ModifyNumericalVariablesSpecs for each numeric variable.
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
     * Sets the variables data. Each row in the variables data consists of the
     * modification text and the variable name.
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
     * Gets the minimum and maximum values for the selected variable.
     *
     * @param variable VariableMu instance of the selected variable.
     * @return Array of doubles containing both the minimum and the maximum
     * value.
     */
    public double[] getMinMax(VariableMu variable) {
        double[] min_max = getCalculationService().getMinMax(variable);
        return min_max;
    }

    /**
     * Gets the minimum value for the selected variable.
     *
     * @param selected ModifyNumericalVariablesSpec instance of the selected
     * variable.
     * @return String containing the minimum value for the selected variable.
     */
    public String getMin(ModifyNumericalVariablesSpec selected) {
        return getIntIfPossible(selected.getMin());
    }

    /**
     * Gets the maximum value for the selected variable.
     *
     * @param selected ModifyNumericalVariablesSpec instance of the selected
     * variable.
     * @return String containing the maximum value for the selected variable.
     */
    public String getMax(ModifyNumericalVariablesSpec selected) {
        return getIntIfPossible(selected.getMax());
    }

    /**
     * Checks for all values if they are valid and if not returns the relevant
     * warning message(s).
     *
     * @param selected ModifyNumericalVariablesSpec instance of the selected
     * variable.
     * @param bottomValue String containing the user input of the bottom value.
     * @param topValue String containing the user input of the top value.
     * @param bottomReplacement String containing the user input of the bottom
     * replacement value.
     * @param topReplacement String containing the user input of the top
     * replacement value.
     * @param roundingBase String containing the user input for the rounding
     * base.
     * @param weightNoisePercentage String containing the user input for the
     * weight noise percentage.
     * @return String containing all relevant warning message(s).
     */
    public String getWarningMessage(ModifyNumericalVariablesSpec selected, String bottomValue, String topValue,
            String bottomReplacement, String topReplacement, String roundingBase, String weightNoisePercentage) {

        String warningMessage = "";

        //Check whether the bottom value and the bottom replacement value are valid.
        boolean bottom = false;
        try {
            Double temp = Double.parseDouble(bottomValue);
            bottom = true;
            if (temp < selected.getMin() || temp > selected.getMax()) {
                warningMessage += "Bottom Value needs to be in the range between the minimum and maximum value\n";
                bottom = false;
            } else if (bottomReplacement.equals("")) {
                selected.setBottomValue(bottomValue);
                warningMessage += "Bottom replacement value cannot be empty\n";
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

        //Check whether the top value and the top replacement value are valid.
        boolean top = false;
        try {
            Double temp = Double.parseDouble(topValue);
            top = true;
            if (temp < selected.getMin() || temp > selected.getMax()) {
                warningMessage += "Top Value needs to be in the range between the minimum and maximum value\n";
                top = false;
            } else if (topReplacement.equals("")) {
                warningMessage += "Top replacement value cannot be empty\n";
                selected.setBottomValue(topValue);
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

        //If both top and bottom values are valid, check if the bottom value is less than the top value.
        if (top && bottom) {
            if (Double.parseDouble(topValue) <= Double.parseDouble(bottomValue)) {
                warningMessage += "Top value needs to be larger than the bottom value\n";
            }
        }

        //Check whether the rounding base is valid.
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

        //Check whether the weight noise percentage is valid.
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

        return warningMessage;
    }

    /**
     * Gets the String format for the double value and sets this to an integer
     * if possible.
     *
     * @param value Double value that will be converted to a String.
     * @return String containing the integer value if possible and otherwise the
     * double value.
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
     * Apply's the numerical modifications. If a variable is ordinal, rounding
     * will not be done and the weightnoise will only be applied when a variable
     * is a weight variable. The final parameter of each modifiation (rounding,
     * weightnoise and top & bottom coding) indicates that this modification
     * should be undone if the value is not a double (NaN).
     *
     * @param selected ModifyNumericalVariablesSpec instance of the variable for
     * which the modifications are made.
     */
    public void apply(ModifyNumericalVariablesSpec selected) {
        try {
            if (!selected.getVariable().isCategorical()) {
                getCalculationService().setRounding(
                        selected.getVariable(),
                        selected.getRoundingBase(),
                        selected.getVariable().getDecimals(),
                        Double.isNaN(selected.getRoundingBase()));
            }
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
            SystemUtils.writeLogbook("Modify numerical variables has been done.");
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }
}
