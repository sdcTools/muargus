package muargus.model;

/**
 * Model class containing relevant information for microaggregation. 
 *
 * @author Statistics Netherlands
 */
public class MicroaggregationSpec extends ReplacementSpec {

    private final int minimalNumberOfRecords;
    private final boolean optimal;

    /**
     * Constructor of the model class Microaggregation. 
     *
     * @param minimalNumberOfRecords Integer containing the minimum number of
     * records per group.
     * @param optimal Boolean indicating whether the optimal method is used.
     */
    public MicroaggregationSpec(int minimalNumberOfRecords, boolean optimal) {
        this.minimalNumberOfRecords = minimalNumberOfRecords;
        this.optimal = optimal;
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

}
