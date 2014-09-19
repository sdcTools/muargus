package muargus.model;

/**
 * Model class of the RiskSpecification screen (both individual and household).
 * Only a single instance of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class RiskModelClass {

    private final double leftValue;
    private final double rightValue;
    private final int frequency;
    private final int hhFrequency;

    /**
     * Constructor of the model class RiskModelClass.
     *
     * @param leftValue Double containing the bottom left hand value.
     * @param rightValue Double containing the upper right hand value.
     * @param frequency Integer containing the individual risk frequency.
     * @param hhFrequency Integer containing the household frequency.
     */
    public RiskModelClass(double leftValue, double rightValue, int frequency, int hhFrequency) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.frequency = frequency;
        this.hhFrequency = hhFrequency;
    }

    /**
     * Gets the bottom left hand value.
     *
     * @return Double containing the bottom left hand value.
     */
    public double getLeftValue() {
        return this.leftValue;
    }

    /**
     * Gets the upper right hand value.
     *
     * @return Double containing the upper right hand value.
     */
    public double getRightValue() {
        return this.rightValue;
    }

    /**
     * Gets the individual risk frequency.
     *
     * @return Integer containing the individual risk frequency.
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Gets the household frequency.
     *
     * @return Integer containing the household frequency.
     */
    public int getHhFrequency() {
        return this.hhFrequency;
    }

}
