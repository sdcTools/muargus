package muargus.model;

import argus.model.DataFilePair;
import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;

/**
 * Model class of the MakeProtectedFile screen. Only a single instance of this
 * class will exist.
 *
 * @author Statistics Netherlands
 */
public class ProtectedFile {

    private int suppressionType;
    public final int NO_SUPPRESSION = 0;
    public final int USE_WEIGHT = 1;
    public final int USE_ENTROPY = 2;

    private int householdType;
    public final int NOT_HOUSEHOLD_DATA = 0;
    public final int KEEP_IN_SAFE_FILE = 1;
    public final int CHANGE_INTO_SEQUENCE_NUMBER = 2;
    public final int REMOVE_FROM_SAFE_FILE = 3;

    private final ArrayList<VariableMu> variables;
    private String[][] data;
    private final String[] columnames;
    private boolean riskModel;

    // values for the dll
    private boolean randomizeOutput;
    private boolean printBHR;
    private MetadataMu safeMeta;

    /**
     * Constructor of the model class ProtectedFile. Initializes the column
     * names for the variablesTable. Makes an empty arraylists for the variables
     * and sets the default value for the householdType and the suppressionType.
     */
    public ProtectedFile() {
        this.variables = new ArrayList<>();
        this.columnames = new String[]{"Variables", "Priority"};
        this.householdType = this.NOT_HOUSEHOLD_DATA;
        this.suppressionType = this.USE_ENTROPY;
    }

    /**
     * Gets an ArrayList containing the categorical variables. If the ArrayList
     * is empty, use this method to add the variables.
     *
     * @return ArrayList containing the categorical variables.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * Gets the column names used in the variablesTable.
     *
     * @return Array of Strings containing the column names.
     */
    public String[] getColumnames() {
        return columnames;
    }

    /**
     * Gets the name and suppressionweight of all categorical variables.
     *
     * @return Double Array of Strings containing the name and suppressionweight
     * of all categorical variables.
     */
    public String[][] getData() {
        if (this.data == null) {
            this.data = new String[this.variables.size()][2];
            int index = 0;
            for (VariableMu v : variables) {
                this.data[index][0] = v.getName();
                this.data[index][1] = Integer.toString(v.getSuppressweight());
                index++;
            }
        }
        return this.data;
    }

    /**
     * Sets the priority/weight.
     *
     * @param selectedRow Integer containing the index of the selected variable.
     * @param priority Integer containing the weight for the selected variable.
     */
    public void setPriority(int selectedRow, int priority) {
        this.data[selectedRow][1] = Integer.toString(priority);
        this.variables.get(selectedRow).setSuppressweight(priority);
    }

    /**
     * Returns whether the option 'use weight' is selected.
     *
     * @return Boolean indicating whether the option 'use weight' is selected.
     */
    public boolean isWithPrior() {
        return this.suppressionType == this.USE_WEIGHT;
    }

    /**
     * Returns whether the option 'use entropy' is selected.
     *
     * @return Boolean indicating whether the option 'use entropy' is selected.
     */
    public boolean isWithEntropy() {
        return this.suppressionType == this.USE_ENTROPY;
    }

    /**
     * Returns whether the option 'randomize output' is selected.
     *
     * @return Boolean indicating whether the option 'randomize output' is
     * selected.
     */
    public boolean isRandomizeOutput() {
        return randomizeOutput;
    }

    /**
     * Sets whether the option 'randomize output' is selected.
     *
     * @param randomizeOutput Boolean indicating whether the option 'randomize
     * output' is selected.
     */
    public void setRandomizeOutput(boolean randomizeOutput) {
        this.randomizeOutput = randomizeOutput;
    }

    /**
     * Returns whether the risk will be added to the output file.
     *
     * @return Boolean indicationg whether the risk will be added to the output
     * file.
     */
    public boolean isPrintBHR() {
        return printBHR;
    }

    /**
     * Sets whether the risk will be added to the output file.
     *
     * @param printBHR Boolean indicationg whether the risk will be added to the
     * output file.
     */
    public void setPrintBHR(boolean printBHR) {
        this.printBHR = printBHR;
    }

    /**
     * Returns whether the risk model is used.
     *
     * @return Boolean indication whether the risk model is used.
     */
    public boolean isRiskModel() {
        return riskModel;
    }

    /**
     * Sets whether the risk model is used.
     *
     * @param riskModel Boolean indication whether the risk model is used.
     */
    public void setRiskModel(boolean riskModel) {
        this.riskModel = riskModel;
    }

    /**
     * Sets the safe metadata and the file names.
     *
     * @param file File instance choosen by the user for the safe file.
     * @param meta Metadata instance containing the safe metadata.
     */
    public void initSafeMeta(File file, MetadataMu meta) {
        this.safeMeta = new MetadataMu(meta);
        String path = getNameOfSafeFile(file);
        DataFilePair pair = new DataFilePair(path, FilenameUtils.removeExtension(path) + ".rds");
        this.safeMeta.setFileNames(pair);
    }

    /**
     * Gets the safe metadata.
     *
     * @return MetadataMu instance of the safe metadata.
     */
    public MetadataMu getSafeMeta() {
        return this.safeMeta;
    }

    /**
     * Gets the name of the safe file.
     *
     * @param file File instance choosen by the user for the safe file.
     * @return String containing the path name of the safe file.
     */
    private String getNameOfSafeFile(File file) {
        if (file.getName().contains(".")) {
            return file.getPath();
        } else {
            return file.getPath() + ".saf";
        }
    }

    /**
     * Gets the type of suppression.
     *
     * @return Integer containing the constant for the suppression type.
     * NO_SUPPRESSION = 0; USE_WEIGHT = 1; USE_ENTROPY = 2;
     */
    public int getSuppressionType() {
        return suppressionType;
    }

    /**
     * Sets the type of suppression.
     *
     * @param suppressionType Integer containing the constant for the
     * suppression type. NO_SUPPRESSION = 0; USE_WEIGHT = 1; USE_ENTROPY = 2;
     */
    public void setSuppressionType(int suppressionType) {
        this.suppressionType = suppressionType;
    }

    /**
     * Gets the household type. The household type describes whether the data is
     * household data and if so, what should be done whith this data.
     *
     * @return Integer containing the constant for the household type.
     * NOT_HOUSEHOLD_DATA = 0; KEEP_IN_SAFE_FILE = 1;
     * CHANGE_INTO_SEQUENCE_NUMBER = 2; REMOVE_FROM_SAFE_FILE = 3;
     */
    public int getHouseholdType() {
        return householdType;
    }

    /**
     * Sets the household type. The household type describes whether the data is
     * household data and if so, what should be done whith this data.
     *
     * @param householdType Integer containing the constant for the household
     * type. NOT_HOUSEHOLD_DATA = 0; KEEP_IN_SAFE_FILE = 1;
     * CHANGE_INTO_SEQUENCE_NUMBER = 2; REMOVE_FROM_SAFE_FILE = 3;
     */
    public void setHouseholdType(int householdType) {
        this.householdType = householdType;
    }

    /**
     * Returns whether the data is household data.
     *
     * @return Boolean indicating whether de data is household data.
     */
    public boolean isHouseholdData() {
        return this.householdType != this.NOT_HOUSEHOLD_DATA;
    }

    /**
     * Sets whether the data is household data.
     *
     * @param householdData Boolean indicating whether de data is household
     * data.
     */
    public void setHouseholdData(boolean householdData) {
        if (!householdData) {
            this.householdType = this.NOT_HOUSEHOLD_DATA;
        } else {
            this.householdType = this.KEEP_IN_SAFE_FILE;
        }
    }

}
