package muargus.model;

import java.util.ArrayList;

/**
 * Model class of the ModifyNumbericalVariables screen. Only a single instance
 * of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class ModifyNumericalVariables {

    private final ArrayList<ModifyNumericalVariablesSpec> modifyNumericalVariablesSpec;
    private String[][] variablesData;
    private final String[] variablesColumnNames;

    /**
     * Constructor of the model class ModifyNumericalVariables. Initializes the
     * column names and makes an empty arraylist for the
     * modifyNumericalVariablesSpec.
     */
    public ModifyNumericalVariables() {
        this.modifyNumericalVariablesSpec = new ArrayList<>();
        this.variablesColumnNames = new String[]{"Modified", "Variable"};
    }

    /**
     * Gets the ArrayList containing the variable specifications for numerical
     * modification. The ModifyNumericalVariablesSpec contains all relevant
     * information for modifying the relevant variable. If the ArrayList is
     * empty, use this method to add ModifyNumericalVariablesSpec's.
     *
     * @return ArrayList containing ModifyNumericalVariablesSpec's.
     */
    public ArrayList<ModifyNumericalVariablesSpec> getModifyNumericalVariablesSpec() {
        return modifyNumericalVariablesSpec;
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

    /**
     * gets the data for the table containing the modified text and the variable
     * name of each numerical variable.
     *
     * @return Double Array of Strings containing the modified text and the
     * variable name of each numerical variable.
     */
    public String[][] getVariablesData() {
        return variablesData;
    }

    /**
     * Gets an Array containing the column names.
     *
     * @return Array of Strings containing the column names.
     */
    public String[] getVariablesColumnNames() {
        return variablesColumnNames;
    }

}
