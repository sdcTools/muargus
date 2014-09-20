package muargus.model;

import java.util.ArrayList;

/**
 * Model class containing the table specifications. An instance for each
 * selected table will exist.
 *
 * @author Statistics Netherlands
 */
public class TableMu {

    private boolean riskModel;
    private int threshold;
    private final ArrayList<VariableMu> variables = new ArrayList<>();
    private int nrOfUnsafeCombinations;

    /**
     * Constructor for the model class TableMu. Sets the default values for the
     * riskmodel and threshold.
     */
    public TableMu() {
        this.riskModel = false;
        this.threshold = 1;
    }

    /**
     * Constructor for the model class TableMu that is used when the
     * Combinations class is cloned.
     *
     * @param table TableMu instance of the original table.
     */
    public TableMu(TableMu table) {
        this.riskModel = table.riskModel;
        this.threshold = table.threshold;
        for (VariableMu variable : table.variables) {
            this.variables.add(variable);
        }
    }

    /**
     * Returns whether the riskmodel is set for this table.
     *
     * @return Boolean indicating whether the riskmodel is set for this table.
     */
    public boolean isRiskModel() {
        return riskModel;
    }

    /**
     * Sets whether the riskmodel is set for this table.
     *
     * @param riskModel Boolean indicating whether the riskmodel is set for this
     * table.
     */
    public void setRiskModel(boolean riskModel) {
        this.riskModel = riskModel;
    }

    /**
     * Gets the threshold.
     *
     * @return Integer containing the threshold.
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Sets the threshold.
     *
     * @param threshold Integer containing the threshold.
     */
    public void setThreshold(String threshold) {
        this.threshold = Integer.parseInt(threshold);
    }

    /**
     * Sets the threshold.
     *
     * @param threshold Integer containing the threshold.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Gets the number of unsafe combinations.
     *
     * @return Integer containing the number of unsafe combinations.
     */
    public int getNrOfUnsafeCombinations() {
        return this.nrOfUnsafeCombinations;
    }

    /**
     * Sets the number of unsafe combinations.
     *
     * @param nrOfUnsafeCombinations Integer containing the number of unsafe
     * combinations.
     */
    public void setNrOfUnsafeCombinations(int nrOfUnsafeCombinations) {
        this.nrOfUnsafeCombinations = nrOfUnsafeCombinations;
    }

    /**
     * Gets all variables in the table.
     *
     * @return ArrayList containing the variables in the table.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * Gets the data for one table.
     *
     * @return Array of Strings containing with the first value indicating if
     * the riskModel is set, the second value showing the threshold and each
     * following value containing all variables in the table.
     */
    public String[] getTable() {
        String[] table = new String[variables.size() + 2];
        if (isRiskModel()) {
            table[0] = "R";
        } else {
            table[0] = "";
        }
        table[1] = Integer.toString(threshold);
        for (int i = 0; i < variables.size(); i++) {
            table[i + 2] = variables.get(i).getName();
        }

        return table;
    }

    /**
     * Checks if at least one variable of the variables from this table is set
     * for the riskModel.
     *
     * @param riskModelVariables ArrayList containing all variables for which
     * the riskModel is specified.
     * @return
     */
    public boolean contains(ArrayList<VariableMu> riskModelVariables) {
        boolean contains = false;
        for (VariableMu riskVariables : riskModelVariables) {
            for (VariableMu thisVariables : this.getVariables()) {
                if (riskVariables.equals(thisVariables)) {
                    contains = true;
                }

            }
        }
        return contains;
    }
}
