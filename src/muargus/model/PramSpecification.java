package muargus.model;

import java.util.ArrayList;

/**
 * Model class of the PramSpecification screen. Only a single instance of this
 * class will exist.
 *
 * @author Statistics Netherlands
 */
public class PramSpecification {

    private final int defaultProbability;
    private final ArrayList<PramVariableSpec> pramVarSpec;
    private final String[] variablesColumnNames;
    private final String[] codesColumnNames;
    private String[][] variablesData;

    /**
     * Constructor of the model class PramSpecification. Initializes the column
     * names for the variablesTable and the codesTable. Makes an empty
     * arraylists for the pramVarSpec and sets the default value for the
     * defaultProbability.
     */
    public PramSpecification() {
        this.variablesColumnNames = new String[]{"P", "BW", "Variable"};
        this.codesColumnNames = new String[]{"Code", "Label", "Prob."};
        this.pramVarSpec = new ArrayList<>();
        this.defaultProbability = 80;
    }

    /**
     * Gets the defaultProbability.
     *
     * @return Integer containing the defaultProbability.
     */
    public int getDefaultProbability() {
        return defaultProbability;
    }

    /**
     * Gets an ArrayList containing the variable specifications for PRAM. If the
     * ArrayList is empty, use this method to add PramVariableSpec's.
     *
     * @return ArrayList containing the variable specifications for PRAM.
     */
    public ArrayList<PramVariableSpec> getPramVarSpec() {
        return this.pramVarSpec;
    }

    /**
     * Gets the column names of the variablesTable.
     *
     * @return Array of Strings containing the column names of the
     * variablesTable.
     */
    public String[] getVariablesColumnNames() {
        return variablesColumnNames;
    }

    /**
     * Gets the column names of the codesTable.
     *
     * @return Array of Strings containing the column names of the codesTable.
     */
    public String[] getCodesColumnNames() {
        return this.codesColumnNames;
    }

    /**
     * Gets the data for the table containing the modified text and the variable
     * name of each numerical variable.
     *
     * @return Double Array of Strings containing the modified text and the
     * variable name of each numerical variable.
     */
    public String[][] getVariablesData() {
        return variablesData;
    }

    /**
     * Sets the data for the table containing the modified text and the variable
     * name of each numerical variable.
     *
     * @param variablesData Double Array of Strings containing the modified text
     * and the variable name of each numerical variable.
     */
    public void setVariablesData(String[][] variablesData) {
        this.variablesData = variablesData;
    }

}
