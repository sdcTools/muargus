package muargus.model;

/**
 * Model class containing relevant information for microaggregation. An instance
 * for each numerical variable of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class MicroaggregationSpec extends ReplacementSpec {

    private final int minimalNumberOfRecords;
    private final boolean optimal;
    private final boolean numerical;

    /**
     * Constructor of the model class Microaggregation. Makes empty arraylists
     * for the variables and the microaggregations.
     *
     * @param minimalNumberOfRecords Integer containing the minimum number of
     * records per group.
     * @param optimal Boolean indicating whether the optimal method is used.
     * @param numerical Boolean indicating whether the variable is numerical.
     */
    public MicroaggregationSpec(int minimalNumberOfRecords, boolean optimal, boolean numerical) {
        this.minimalNumberOfRecords = minimalNumberOfRecords;
        this.optimal = optimal;
        this.numerical = numerical;
    }

    /**
     * Gets the minimum number of records per group.
     *
     * @return Integer containing the minimum number of records per group.
     */
    public int getMinimalNumberOfRecords() {
        return this.minimalNumberOfRecords;
    }

    /**
     * Boolean whether the optimal method is used.
     *
     * @return Boolean indicating whether the optimal method is used.
     */
    public boolean isOptimal() {
        return this.optimal;
    }

    /**
     * Returns whether the variable is numerical.
     *
     * @return Boolean indicating whether the variable is numerical.
     */
    public boolean isNumerical() {
        return this.numerical;
    }

}
