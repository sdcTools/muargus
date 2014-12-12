package muargus.model;

import argus.model.DataFilePair;
import argus.utils.StrUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 * Model class of the MakeProtectedFile screen. Only a single instance of this
 * class will exist.
 *
 * @author Statistics Netherlands
 */
public class ProtectedFile {

    private int suppressionType;
    public final static int NO_SUPPRESSION = 0;
    public final static int USE_PRIORITY = 1;
    public final static int USE_ENTROPY = 2;

    private int householdType;
    public final static int NOT_HOUSEHOLD_DATA = 0;
    public final static int KEEP_IN_SAFE_FILE = 1;
    public final static int CHANGE_INTO_SEQUENCE_NUMBER = 2;
    public final static int REMOVE_FROM_SAFE_FILE = 3;

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
        this.householdType = ProtectedFile.NOT_HOUSEHOLD_DATA;
        this.suppressionType = ProtectedFile.USE_ENTROPY;
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
     * Gets the name and suppression priority of all categorical variables.
     *
     * @return Double Array of Strings containing the name and suppression
     * priority of all categorical variables.
     */
    public String[][] getData() {
        if (this.data == null) {
            this.data = new String[this.variables.size()][2];
            int index = 0;
            for (VariableMu v : variables) {
                this.data[index][0] = v.getName();
                this.data[index][1] = Integer.toString(v.getSuppressPriority());
                index++;
            }
        }
        return this.data;
    }

    /**
     * Sets the priority.
     *
     * @param selectedRow Integer containing the index of the selected variable.
     * @param priority Integer containing the priority for the selected
     * variable.
     */
    public void setPriority(int selectedRow, int priority) {
        this.data[selectedRow][1] = Integer.toString(priority);
        this.variables.get(selectedRow).setSuppressionPriority(priority);
    }

    /**
     * Returns whether the option 'use priority' is selected.
     *
     * @return Boolean indicating whether the option 'use priority' is selected.
     */
    public boolean isWithPrior() {
        return this.suppressionType == ProtectedFile.USE_PRIORITY;
    }

    /**
     * Returns whether the option 'use entropy' is selected.
     *
     * @return Boolean indicating whether the option 'use entropy' is selected.
     */
    public boolean isWithEntropy() {
        return this.suppressionType == ProtectedFile.USE_ENTROPY;
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
        try {
            this.safeMeta = new MetadataMu(meta);
            String path;
            if(file.getPath().contains(".saf") || file.getPath().contains(".sav")){
                path = StrUtils.replaceExtension(file.getPath(), ".saf");
            } else {
                path = file.getPath() + ".saf";
            }
            
            File rds = File.createTempFile("MuArgus", ".rds");
            rds.deleteOnExit();
            DataFilePair pair = meta.isSpss()?
                    new DataFilePair(path, rds.getPath()):
                    new DataFilePair(path, FilenameUtils.removeExtension(path) + ".rds");
            this.safeMeta.setFileNames(pair);
        } catch (IOException ex) {
            Logger.getLogger(ProtectedFile.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * Gets the type of suppression.
     *
     * @return Integer containing the constant for the suppression type.
     * NO_SUPPRESSION = 0; USE_PRIORITY = 1; USE_ENTROPY = 2;
     */
    public int getSuppressionType() {
        return suppressionType;
    }

    /**
     * Sets the type of suppression.
     *
     * @param suppressionType Integer containing the constant for the
     * suppression type. NO_SUPPRESSION = 0; USE_PRIORITY = 1; USE_ENTROPY = 2;
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
        return this.householdType != ProtectedFile.NOT_HOUSEHOLD_DATA;
    }

    /**
     * Sets whether the data is household data.
     *
     * @param householdData Boolean indicating whether de data is household
     * data.
     */
    public void setHouseholdData(boolean householdData) {
        if (!householdData) {
            this.householdType = ProtectedFile.NOT_HOUSEHOLD_DATA;
        } else {
            this.householdType = ProtectedFile.KEEP_IN_SAFE_FILE;
        }
    }

}
