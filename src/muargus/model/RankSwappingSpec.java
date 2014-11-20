package muargus.model;

/**
 * Model class containing relevant information for rank swapping. 
 *
 * @author Statistics Netherlands
 */
public class RankSwappingSpec extends ReplacementSpec {

    private final double percentage;

    /**
     * Constructor of the model class Microaggregation. Makes an empty arraylist
     * for the microaggregations.
     *
     * @param percentage Integer containing the rank swapping percentage.
     */
    public RankSwappingSpec(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Gets the rank swapping percentage.
     *
     * @return Integer containing the rank swapping percentage.
     */
    public double getPercentage() {
        return percentage;
    }

}
