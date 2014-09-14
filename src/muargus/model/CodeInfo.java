package muargus.model;

/**
 * Model class containing basic info for a single code. Each categorical
 * variable contains an array of instances from this class.
 *
 * @author Statistics Netherlands
 */
public class CodeInfo {

    private final String code;
    private String label;
    private final boolean isMissing;
    private int frequency;
    private int[] unsafeCombinations;
    private int pramProbability;

    /**
     * Constructor for model class CodeInfo.
     *
     * @param code String containing the code used.
     * @param isMissing Boolean indicating whether this code is a missing or
     * not.
     */
    public CodeInfo(String code, boolean isMissing) {
        this.code = code;
        this.isMissing = isMissing;
    }

    /**
     * Gets the code.
     *
     * @return Returns a string containing the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the label name of the code.
     *
     * @param label String of the label name.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the label name of the code.
     *
     * @return Returns a string containing the label name of the code.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Returns whether the code is a missing or not.
     *
     * @return Returns a boolean indicating whether this code is a missing or
     * not
     */
    public boolean isMissing() {
        return isMissing;
    }

    /**
     * Gets the frequency of this code in the data-file
     *
     * @return Returns an integer containing the frequency of this code in the
     * data-file
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Sets the frequency of this code in the data-file
     *
     * @param frequency Integer containing the frequency of this code in the
     * data-file
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Gets the number of unsafe combinations for a particular dimension.
     *
     * @param dimIndex Integer of the number of dimensions for which one wants
     * the number of unsafe combinations.
     * @return Returns an integer containing the number of unsafe combinations
     * for the given number of dimensions
     */
    public int getUnsafe(int dimIndex) {
        return this.unsafeCombinations[dimIndex];
    }

    /**
     * Sets the number of unsafe combinations for all dimensions.
     *
     * @param count Integer containing the number of dimensions.
     * @param unsafe Array of integers with the number of unsafe combinations
     * for each dimension
     */
    public void setUnsafeCombinations(int count, int[] unsafe) {
        this.unsafeCombinations = new int[count];
        System.arraycopy(unsafe, 0, this.unsafeCombinations, 0, count);
    }

    /**
     * Gets an Object array with all the information of this code that will be
     * displayed in the MainFrame. The Object array is filled with the code,
     * it's label, the frequency, and for requested number of dimensions the
     * number of unsafe combinations.
     *
     * @param maxDims Integer of the requested maximum number of dimensions for
     * which the number of unsafe combinations are desirable
     * @return Returns an array containing the code, label, frequency and number
     * of unsafe combinations for the requested number of dimensions
     */
    public Object[] toObjectArray(int maxDims) {
        Object[] objArr = new Object[maxDims + 3];
        objArr[0] = this;
        objArr[1] = this.label;
        objArr[2] = this.frequency;
        for (int dimNr = 1; dimNr <= maxDims; dimNr++) {
            objArr[dimNr + 2] = this.unsafeCombinations.length < dimNr
                    ? "-" : Integer.toString(this.unsafeCombinations[dimNr - 1]);
        }
        return objArr;
    }

    /**
     * Gets the probability used for the PRAM-method
     *
     * @return Returns an integer containing the PRAM-probability
     */
    public int getPramProbability() {
        if (pramProbability < 0) {
            pramProbability = 0;
        }
        return pramProbability;
    }

    /**
     * Sets the probability used for the PRAM-method
     *
     * @param pramProbability Integer containing the PRAM-probability
     */
    public void setPramProbability(int pramProbability) {
        this.pramProbability = pramProbability;
    }

}
