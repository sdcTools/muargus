package muargus.model;

import argus.model.ArgusException;
import argus.utils.StrUtils;

/**
 * Model class containing relevant information for the modification of a
 * numerical variable. An instance for each numerical variable of this class
 * will exist.
 *
 * @author Statistics Netherlands
 */
public class ModifyNumericalVariablesSpec {

    private final VariableMu variable;
    private boolean modified;
    private String modifiedText;
    private double[] min_max;
    private Double bottomValue;
    private Double topValue;
    private String bottomReplacement;
    private String topReplacement;
    private Double roundingBase;
    private Double weightNoisePercentage;

    /**
     * Constructor of the model class ModifyNumericalVariablesSpec. Sets the
     * variable as not modified and the modification text as an empty String.
     * Initializes the rounding base, weight noise percentage, top and bottom
     * values.
     *
     * @param variable The numerical variable for which modifications can be
     * made.
     */
    public ModifyNumericalVariablesSpec(VariableMu variable) {
        this.variable = variable;
        this.modified = false;
        this.modifiedText = "";
        this.bottomValue = Double.NaN;
        this.topValue = Double.NaN;
        this.roundingBase = Double.NaN;
        this.weightNoisePercentage = Double.NaN;
    }

    /**
     * Gets the VariableMu instance of the variable.
     *
     * @return VariableMu instance of the variable.
     */
    public VariableMu getVariable() {
        return this.variable;
    }

    /**
     * Returns whether this variable has been modified.
     *
     * @return Returns a boolean indicating whether this variable has been
     * modified.
     */
    public boolean isModified() {
        return this.modified;
    }

    /**
     * Sets whether this variable has been modified. This method also sets the
     * text that will be shown as an indication that the variable has been
     * modified.
     *
     * @param modified Boolean indicating whether this variable has been
     * modified.
     */
    public void setModified(boolean modified) {
        this.modified = modified;
        setModifiedText();
    }

    /**
     * Gets the text that will be shown as an indication that the variable has
     * been modified.
     *
     * @return String containing the text that will be shown as an indication
     * that the variable has been modified.
     */
    public String getModifiedText() {
        return this.modifiedText;
    }

    /**
     * Sets the text that will be shown as an indication that the variable has
     * been modified.
     */
    public void setModifiedText() {
        this.modifiedText = (this.modified ? "X" : "");
    }

    /**
     * Gets the minimum value used in the data file for the specified variable.
     *
     * @return Double containing the minimum value used in the data file for the
     * specified variable.
     */
    public double getMin() {
        return this.min_max[0];
    }

    /**
     * Gets the maximum value used in the data file for the specified variable.
     *
     * @return Double containing the maximum value used in the data file for the
     * specified variable.
     */
    public double getMax() {
        return this.min_max[1];
    }

    /**
     * Sets the minimum and maximum value used in the data file for the
     * specified variable.
     *
     * @param min_max Array of doubles containing the minimum[0] and maximum[1]
     * value used in the data file for the specified variable.
     */
    public void setMin_max(double[] min_max) {
        this.min_max = min_max;
    }

    /**
     * Gets the bottom value used for bottom coding.
     *
     * @return Double containing the bottom value.
     */
    public Double getBottomValue() {
        return this.bottomValue;
    }

    /**
     * Sets the bottom value using a String. If the value is not a valid Double,
     * the bottom value is set at 'Not a Number' (NaN).
     *
     * @param bottomValue String containing the bottom value.
     */
    public void setBottomValue(String bottomValue) {
        try {
            this.bottomValue = StrUtils.toDouble(bottomValue);
        } catch (ArgusException e) {
            this.bottomValue = Double.NaN;
        }
    }

    /**
     * Sets the bottom value using a Double.
     *
     * @param bottomValue Double containing the bottom value.
     */
    public void setBottomValue(Double bottomValue) {
        this.bottomValue = bottomValue;
    }

    /**
     * Gets the top value used for top coding.
     *
     * @return Double containing the top value.
     */
    public Double getTopValue() {
        return this.topValue;
    }

    /**
     * Sets the top value using a String.
     *
     * @param topValue String containing the top value.
     */
    public void setTopValue(String topValue) {
        try {
            this.topValue = StrUtils.toDouble(topValue);
        } catch (ArgusException e) {
            this.topValue = Double.NaN;
        }
    }

    /**
     * Sets the top value using a Double.
     *
     * @param topValue Double containing the top value.
     */
    public void setTopValue(Double topValue) {
        this.topValue = topValue;
    }

    /**
     * Gets the value replacing all values equal to or below the bottom value
     * after bottom coding.
     *
     * @return String containing the value replacing all values equal to or
     * below the bottom value after bottom coding.
     */
    public String getBottomReplacement() {
        return this.bottomReplacement;
    }

    /**
     * Sets the value replacing all values equal to or below the bottom value
     * after bottom coding.
     *
     * @param bottomReplacement String containing the value replacing all values
     * equal to or below the bottom value after bottom coding.
     */
    public void setBottomReplacement(String bottomReplacement) {
        this.bottomReplacement = bottomReplacement;
    }

    /**
     * Gets the value replacing all values equal to or above the top value after
     * top coding.
     *
     * @return String containing the value replacing all values equal to or
     * above the top value after top coding.
     */
    public String getTopReplacement() {
        return this.topReplacement;
    }

    /**
     * Sets the value replacing all values equal to or above the top value after
     * top coding.
     *
     * @param topReplacement String containing the value replacing all values
     * equal to or above the top value after top coding.
     */
    public void setTopReplacement(String topReplacement) {
        this.topReplacement = topReplacement;
    }

    /**
     * Gets the rounding base.
     *
     * @return Double containing the rounding base.
     */
    public Double getRoundingBase() {
        return this.roundingBase;
    }

    /**
     * Sets the rounding base using a String.
     *
     * @param roundingBase String containing the rounding base.
     */
    public void setRoundingBase(String roundingBase) {
        try {
            this.roundingBase = StrUtils.toDouble(roundingBase);
        } catch (ArgusException e) {
            this.roundingBase = Double.NaN;
        }
    }

    /**
     * Sets the rounding base using a Double.
     *
     * @param roundingBase Double containing the rounding base.
     */
    public void setRoundingBase(Double roundingBase) {
        this.roundingBase = roundingBase;
    }

    /**
     * Gets the amount of noise to be added to the weight variable as a
     * percentage.
     *
     * @return Double containing the amount of noise to be added to the weight
     * variable as a percentage.
     */
    public Double getWeightNoisePercentage() {
        return this.weightNoisePercentage;
    }

    /**
     * Sets the weight noise percentage using a String.
     *
     * @param weightNoisePercentage String containing the amount of noise to be
     * added to the weight variable as a percentage.
     */
    public void setWeightNoisePercentage(String weightNoisePercentage) {
        try {
            this.weightNoisePercentage = StrUtils.toDouble(weightNoisePercentage);
        } catch (ArgusException e) {
            this.weightNoisePercentage = Double.NaN;
        }
    }

    /**
     * Sets the weight noise percentage using a Double.
     *
     * @param weightNoisePercentage Double containing the amount of noise to be
     * added to the weight variable as a percentage.
     */
    public void setWeightNoisePercentage(Double weightNoisePercentage) {
        this.weightNoisePercentage = weightNoisePercentage;
    }

}
