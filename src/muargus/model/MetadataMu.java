package muargus.model;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import muargus.MuARGUS;

/**
 * Model class of the SpecifyMetadata screen. This model class contains all
 * information on the metadata.
 *
 * @author Statistics Netherlands
 */
public final class MetadataMu {

    //private static final Logger logger = Logger.getLogger(MetadataMu.class.getName());

    // file types 
    public static final int DATA_FILE_TYPE_FIXED = 1;
    public static final int DATA_FILE_TYPE_FREE = 2;
    public static final int DATA_FILE_TYPE_FREE_WITH_META = 3;
    public static final int DATA_FILE_TYPE_SPSS = 4;

    //default
    private int dataFileType = DATA_FILE_TYPE_FIXED;
    private String separator = ";";

    private final ArrayList<VariableMu> variables;

    private DataFilePair filenames;
    private Combinations combinations;
    private int recordCount;
    private final ArrayList<ReplacementSpec> replacementSpecs;

    /**
     * Constructor of the model class MetadataMu. Initializes the DataFilePair
     * and makes empty arraylists for the variables and the RecodeMu's.
     */
    public MetadataMu() {
        this.variables = new ArrayList<>();
        this.replacementSpecs = new ArrayList<>();
        this.filenames = new DataFilePair(null, null);
    }

    /**
     * Constructor of the model class MetadataMu using existing metadata as
     * input. This constructor is used when metadata already exists to either
     * make a clone of the metadata or to make the safe metadata.
     *
     * @param metadata MetadataMu instance containing the original metadata.
     */
    public MetadataMu(MetadataMu metadata) {
        this();

        this.dataFileType = metadata.dataFileType;
        this.filenames = new DataFilePair(metadata.filenames.getDataFileName(), metadata.filenames.getMetaFileName());
        this.separator = metadata.separator;
        for (VariableMu var : metadata.variables) {
            this.variables.add(new VariableMu(var));
        }
        try {
            linkRelatedVariables();
        } catch (ArgusException ex) {
            //Cannot occur, since metadata was already checked to be valid
        }
    }

    /**
     * Checks whether the data file type equals spss.
     *
     * @return Boolean indicating whether the data file type equals spss.
     */
    public boolean isSpss() {
        return this.dataFileType == MetadataMu.DATA_FILE_TYPE_SPSS;
    }

    /**
     * Gets the model class of the selectCombinations screen.
     *
     * @return Returns the Combinations model class.
     */
    public Combinations getCombinations() {
        return this.combinations;
    }

    /**
     * Creates a new instance of the Combinations model class.
     */
    public void createCombinations() {
        this.combinations = new Combinations();
    }

    /**
     * Sets the model class of the selectCombinations screen.
     *
     * @param combinations The Combinations model class.
     */
    public void setCombinations(Combinations combinations) {
        this.combinations = combinations;
    }

    /**
     * Checks whether the changes in the metadata are of significant importance.
     * If the changes are significant, this will mean that already specified
     * tables should be removed.
     *
     * @param metadata MetadataMu instance containing a clone of the original
     * metadata.
     * @return Boolean stating whether the change is significant or not.
     */
    public boolean significantDifference(MetadataMu metadata) {
        //TODO: implemenet
        //significant changes are for instance changes in field length, variable type, etc
        //insignificant changes are for instance changes in codelist file
        //for now, everything is significant
        return true;
    }

    /**
     * Gets the number of records in the data file.
     *
     * @return Integer containing the number of records in the data file.
     */
    public int getRecordCount() {
        return recordCount;
    }

    /**
     * Sets the number of records that are in the data file.
     *
     * @param recordCount Integer containing the number of records in the data
     * file.
     */
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * Gets the ArrayList containing all the ReplacementFiles.
     *
     * @return ArrayList containing all the ReplacementFiles.
     */
    public ArrayList<ReplacementSpec> getReplacementSpecs() {
        return this.replacementSpecs;
    }

//    public static ArrayList<VariableMu> makeClone(ArrayList<VariableMu> list) throws CloneNotSupportedException {
//        ArrayList<VariableMu> clone = new ArrayList<>(list.size());
//        for (VariableMu item : list) {
//            clone.add((VariableMu) item.clone());
//        }
//        return clone;
//    }
//    public static ArrayList<Variables> getClone(){
//        return cloneData;
//    }
//    public void TestClone(){
//        cloneData.get(1).setName("test");
//        System.out.println(variables.get(1).getName());
//        System.out.println(cloneData.get(1).getName());
//    }
//    /**
//     *
//     * @param w
//     * @param variable
//     */
//    private void writeVariable(Writer w, VariableMu variable) {
//
//    }
    /**
     * Links variables to each other if this is specified.
     *
     * @throws ArgusException Throws an ArgusException when a variable is
     * related to a non-specified variable.
     */
    public void linkRelatedVariables() throws ArgusException {
        for (VariableMu var : variables) {
            var.linkRelatedVariable(variables);
        }
    }

    // TODO: add a message explaining whats the problem
    /**
     * Verifies for each variable if they are valid. This method checks if the
     * there are duplicate variable names, if the length of the missing values
     * are correct, if the correct amount of missing values are given and if the
     * codelist file is valid. If the datatype is fixed, the variable length,
     * startingpostion and overlap is also checeked.
     *
     * @throws ArgusException Throws an ArgusException if some values are not
     * valid.
     */
    public void verify() throws ArgusException {
        for (VariableMu var : variables) {

            //Check for duplicate variable names
            for (VariableMu var2 : variables) {
                if (!var.equals(var2) && var.getName().equalsIgnoreCase(var2.getName())) {
                    throw new ArgusException("Duplicate variable name: " + var.getName());
                }
            }

            int length = var.getVariableLength();
            //Fixed file checks
            if (dataFileType == DATA_FILE_TYPE_FIXED) {

                //Check if variableLength > 0
                if (length <= 0) {
                    throw new ArgusException("Length of variable " + var.getName() + " must be > 0");
                }
                int start = var.getStartingPosition();

                //Checkstart if startPosition > 0
                if (start <= 0) {
                    throw new ArgusException("Start position of variable " + var.getName() + " must be > 0");
                }

                //Check for overlap of variables in fixed file
                int end = start + length;
                for (VariableMu var2 : variables) {
                    int start2 = var2.getStartingPosition();
                    int end2 = start2 + var2.getVariableLength();
                    if (!var.equals(var2) && start2 < end && start < end2) {
                        throw new ArgusException("Variables " + var.getName() + " and " + var2.getName() + " overlap");
                    }
                }
            }

            //Check if length of missings is correct
            for (int index = 0; index < VariableMu.MAX_NUMBER_OF_MISSINGS; index++) {
                String missing = var.getMissing(index);
                if (missing != null && missing.length() > length) {
                    throw new ArgusException("Missing value for variable " + var.getName() + " too long");
                }

            }

            //Check if the first missing value is not empty when the variable is categorical
            if (var.isCategorical() && var.getMissing(0).equals("")) {
                throw new ArgusException("The first missing value for variable " + var.getName() + " can not be empty");
            }

            //Check if codelistFile is valid
            if (var.isCodelist()) {
                File file = new File(var.getCodeListFile());
                if (!file.isAbsolute()) {
                    File dir = new File(this.filenames.getMetaFileName()).getParentFile();
                    file = new File(dir, var.getCodeListFile());
                }
                if (!file.exists()) {
                    throw new ArgusException("Codelist for variable " + var.getName() + " cannot be found");
                }
            }
        }

//        for (int i = 0; i < variables.size(); i++) {
//            VariableMu variable = variables.get(i);
//            if (dataFileType == DATA_FILE_TYPE_FIXED) {
//                int b1 = variable.getStartingPosition();
//                int e1 = b1 + variable.getVariableLength();
//                for (int j = i + 1; j < variables.size(); j++) {
//                    VariableMu variable2 = variables.get(j);
//                    int b2 = variable2.getStartingPosition();
//                    int e2 = b2 + variable2.getVariableLength();
//                    if (b2 < e1 && e2 > b1) {
//                        throw new ArgusException("Variable " + variable.getName() + " and variable " + variable2.getName() + " overlap.");
//                    }
//                }
//                if (StringUtils.equalsIgnoreCase(variable.getName(), variable2.getName())) {
//                    throw new ArgusException("Variable" + i + " and variable" + j + " have the same name.");
//                }
//            }
//        }
    }

    /**
     * Gets the type of the data file. There are four different data types:
     * fixed, free, free with meta and SPSS.
     *
     * @return Integer containing the type of the data file. The integers
     * correspond to the DATA_FILE_TYPE constants. Possible values:
     * DATA_FILE_TYPE_FIXED DATA_FILE_TYPE_FREE DATA_FILE_TYPE_FREE_WITH_META
     * DATA_FILE_TYPE_SPSS
     */
    public int getDataFileType() {
        return this.dataFileType;
    }

    /**
     * Sets the type of the data file. There are four different data types:
     * fixed, free, free with meta and SPSS.
     *
     * @param dataFileType Integer containing the type of the data file. The
     * integers correspond to the DATA_FILE_TYPE constants. Allowed values:
     * DATA_FILE_TYPE_FIXED DATA_FILE_TYPE_FREE DATA_FILE_TYPE_FREE_WITH_META
     * DATA_FILE_TYPE_SPSS
     */
    public void setDataFileType(int dataFileType) {
        this.dataFileType = dataFileType;
    }

    /**
     * Gets the separator used in free format data file types.
     *
     * @return String containing the separator.
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Sets the separator used in free format data file types.
     *
     * @param separator String containing the separator.
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Gets the file names of both the metadata file and the data file
     *
     * @return DataFilePair instance containing the path of the metadata file
     * and of the data file.
     */
    public DataFilePair getFileNames() {
        return this.filenames;
    }

    /**
     * Sets the file names of both the metadata file and the data file
     *
     * @param filenames DataFilePair instance containing the path of the
     * metadata file and of the data file.
     */
    public void setFileNames(DataFilePair filenames) {
        this.filenames = filenames;
    }

    /**
     * Returns whether the data is Household data.
     *
     * @return Boolean stating whether the data is Household data.
     */
    public boolean isHouseholdData() {
        for (VariableMu variable : variables) {
            if (variable.isHouse_id()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an ArrayList containing all the Variables in the Metadata.
     *
     * @return ArrayList containing all the Variables in the Metadata.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * Gets the hashcode. The hashcode is calculated as a addition of the
     * hashcodes from the relevant individual components: separator,
     * dataFileType, dataFileName, metaFileName and the ArrayList of variables.
     *
     * @return Integer containing the hashcode.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.separator);
        hash = 41 * hash + this.dataFileType;
        hash = 41 * hash + Objects.hashCode(this.filenames.getDataFileName());
        hash = 41 * hash + Objects.hashCode(this.filenames.getMetaFileName());
        hash = 41 * hash + Objects.hashCode(this.variables);
        return hash;
    }

    /**
     * Returns if the metadata is equal to its clone.
     *
     * @param o Object containing the Metadata.
     * @return Boolean stating whether the metadata is equal to its clone.
     */
    @Override
    public boolean equals(Object o) {
        MetadataMu cmp = (MetadataMu) o;
        if (!this.separator.equals(cmp.separator)) {
            return false;
        }
        if (this.dataFileType != cmp.dataFileType) {
            return false;
        }
        if (this.filenames.getDataFileName() == null ? cmp.filenames.getDataFileName() != null
                : !this.filenames.getDataFileName().equals(cmp.filenames.getDataFileName())) {
            return false;
        }
        if (this.filenames.getMetaFileName() == null ? cmp.filenames.getMetaFileName() != null
                : !this.filenames.getMetaFileName().equals(cmp.filenames.getMetaFileName())) {
            return false;
        }
        return this.variables.equals(cmp.variables);
    }
}
