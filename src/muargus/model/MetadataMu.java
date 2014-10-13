package muargus.model;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Model class of the SpecifyMetadata screen. This model class contains all
 * information on the metadata.
 *
 * @author Statistics Netherlands
 */
public class MetadataMu {

    private static final Logger logger = Logger.getLogger(MetadataMu.class.getName());

    // file types 
    public static final int DATA_FILE_TYPE_FIXED = 1;
    public static final int DATA_FILE_TYPE_FREE = 2;
    public static final int DATA_FILE_TYPE_FREE_WITH_META = 3;
    public static final int DATA_FILE_TYPE_SPSS = 4;

    //default
    private int dataFileType = DATA_FILE_TYPE_FIXED;
    private boolean householdData = false;
    private String separator = ";";

    private BufferedReader reader;
    private Tokenizer tokenizer;
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
        variables = new ArrayList<>();
        replacementSpecs = new ArrayList<>();
        filenames = new DataFilePair(null, null);
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
        this.filenames = new DataFilePair(
                metadata.filenames.getDataFileName(), metadata.filenames.getMetaFileName());
        this.separator = metadata.separator;
        for (VariableMu var : metadata.variables) {
            this.variables.add(new VariableMu(var));
        }
        try {
            linkRelatedVariables();
        } catch (ArgusException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
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
        //TODO: some initialization
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

//    /**
//     * Gets the ArrayList containing all the ReplacementFiles.
//     *
//     * @return ArrayList containing all the ReplacementFiles.
//     */
//    public ArrayList<ReplacementFile> getReplacementFiles() {
//        return replacementFiles;
//    }
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
    /**
     * Reads the entire metafile and initalizes the variables. This method reads
     * the metadatafile line for line. It makes a new variable object for each
     * variable it finds and provides the relevant information of this variable
     * (is it recodable, what is it's ID_level etc).
     *
     * @throws ArgusException Throws an ArgusException when the file cannot be
     * read.
     */
    public void readMetadata() throws ArgusException {
        if (this.filenames.getMetaFileName().length() == 0) {
            return;
        }

        dataFileType = DATA_FILE_TYPE_FIXED;
        VariableMu variable = null;

        try {
            reader = new BufferedReader(new FileReader(new File(this.filenames.getMetaFileName())));
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            logger.log(Level.SEVERE, null, ex);
            throw new ArgusException("Metadata file not found");
        }

        tokenizer = new Tokenizer(reader);
        while (tokenizer.nextLine() != null) {
            String value = tokenizer.nextToken();

            if (!value.substring(0, 1).equals("<")) {
                variable = new VariableMu();
                variable.setRecodable(false);
                variable.setName(tokenizer.getValue());
                variables.add(variable);
                if (getDataFileType() == DATA_FILE_TYPE_FIXED) {
                    variable.setStartingPosition(tokenizer.nextToken());
                } else {
                    variable.setStartingPosition("1");  //not relevant, but must be >0
                }
                variable.setVariableLength(Integer.parseInt(tokenizer.nextToken()));
                variable.setMissing(0, tokenizer.nextToken());
                variable.setMissing(1, tokenizer.nextToken());
            } else if (variable == null) {
                switch (value) {
                    case "<SEPARATOR>":
                        setDataFileType(DATA_FILE_TYPE_FREE);
                        setSeparator(tokenizer.nextToken());
                        break;
                    case "<SPSS>":
                        setDataFileType(DATA_FILE_TYPE_SPSS);
                        break;
                    case "<NAMESINFRONT>":
                        setDataFileType(DATA_FILE_TYPE_FREE_WITH_META);
                        break;
                }
            } else {
                switch (value) {
                    case "<RECODEABLE>":
                    case "<RECODABLE>":
                        variable.setRecodable(true);
                        break;
                    case "<CODELIST>":
                        variable.setCodelist(true);
                        variable.setCodeListFile(tokenizer.nextToken());
                        break;
                    case "<IDLEVEL>":
                        variable.setIdLevel(tokenizer.nextToken());
                        break;
                    case "<TRUNCABLE>":
                        variable.setTruncable(true);
                        break;
                    case "<NUMERIC>":
                        variable.setNumeric(true);
                        break;
                    case "<DECIMALS>":
                        variable.setDecimals(tokenizer.nextToken());
                        break;
                    case "<WEIGHT>":
                        variable.setWeight(true);
                        break;
                    case "<HOUSE_ID>":
                        variable.setHouse_id(true);
                        break;
                    case "<HOUSEHOLD>":
                        variable.setHousehold(true);
                        break;
                    case "<SUPPRESSWEIGHT>":
                        variable.setSuppressweight(tokenizer.nextToken());
                        break;
                    case "<RELATED>":
                        variable.setRelatedVariableName(tokenizer.nextToken());
                        break;
                    default:
                        break;
                }
            }
        }
        tokenizer.close();

        linkRelatedVariables();
    }

//    /**
//     *
//     * @param w
//     * @param variable
//     */
//    private void writeVariable(Writer w, VariableMu variable) {
//
//    }
    /**
     * Writes the metadata to a .rda file when a BufferdWritier has been
     * initialized.
     *
     * @param w BufferedWriter already initiated with the neede file.
     * @param all Boolean variable stating whether all metadata needs to be
     * written. When writing the safe metadata, the suppressionweight is for
     * example not necessary.
     * @throws IOException Throws an IOException when an error occurs during
     * writing.
     */
    private void write(BufferedWriter w, boolean all) throws IOException {
        try (
                PrintWriter writer = new PrintWriter(w)) {
            if (dataFileType == DATA_FILE_TYPE_FREE) {
                writer.println("   <SEPARATOR> " + StrUtils.quote(separator));
            }
            if (dataFileType == DATA_FILE_TYPE_SPSS) {
                writer.println("   <SPSS>");
            }
            for (VariableMu variable : this.variables) {
                variable.write(writer, this.dataFileType, all);
            }
        }
    }

    /**
     * Writes the metadata to a .rda file.
     *
     * @param file File containing the metadata.
     * @param all all metadata wegschrijven ja of nee? --> safe metadata neemt
     * niet alles mee (suppressionweight)
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    public void write(File file, boolean all) throws ArgusException {
        try {
            write(new BufferedWriter(new FileWriter(file)), all);
            this.filenames = new DataFilePair(this.filenames.getDataFileName(), file.getPath());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     * Links variables to each other if this is specified.
     *
     * @throws ArgusException Throws an ArgusException when a variable is
     * related to a non-specified variable.
     */
    private void linkRelatedVariables() throws ArgusException {
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

//    public void setMetadataFile(String metaFile){
//        this.metadataFile = metaFile;
//    }
//    
//    public String getMetadataFile(){      
//        if (filenames.getMetaFileName() != null){
//            metadataFile = getFileNames().getMetaFileName();
//        } else if(metadataFile == null || metadataFile.equals(defaultmetaFile)){ 
//            metadataFile = defaultmetaFile;
//        }
//        return metadataFile;
//    }
    /**
     * Gets the type of the data file. There are four different data types:
     * fixed, free, free with meta and SPSS.
     *
     * @return Integer containing the type of the data file. The integers
     * correspond to the DATA_FILE_TYPE constants. Possible values: 
     *   DATA_FILE_TYPE_FIXED
     *   DATA_FILE_TYPE_FREE
     *   DATA_FILE_TYPE_FREE_WITH_META
     *   DATA_FILE_TYPE_SPSS
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
     *   DATA_FILE_TYPE_FIXED
     *   DATA_FILE_TYPE_FREE
     *   DATA_FILE_TYPE_FREE_WITH_META
     *   DATA_FILE_TYPE_SPSS
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
     * @return DataFilePair instance containing the path of the
     * metadata file and of the data file.
     */
    public DataFilePair getFileNames() {
        return this.filenames;
    }

    /**
     * Sets the file names of both the metadata file and the data file
     *
     * @param filenames DataFilePair instance containing the
     * path of the metadata file and of the data file.
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

//    // TODO: remove after testing
//    /**
//     * 
//     */
//    public void testPrintAll() {
//        try {
//            for (VariableMu v : variables) {
//                System.out.println("Variable name: " + v.getName());
//                System.out.println("\tRecodable is " + v.isRecodable());
//                System.out.println("\tCodelist is " + v.isCodelist() + " " + v.getCodeListFile());
//                System.out.println("\tIDLevel is " + v.getIdLevel());
//                System.out.println("\tTruncable is " + v.isTruncable());
//                System.out.println("\tNumeric is " + v.isNumeric());
//                System.out.println("\tWeight is " + v.isWeight());
//                System.out.println("\tHouse_ID is " + v.isHouse_id());
//                System.out.println("\tHousehold is " + v.isHousehold());
//                System.out.println("\tSuppressWeight is " + v.getSuppressweight());
//                //System.out.println("\tRelatedVariable is " + v.getRelatedVariable().getName());
//            }
//        } catch (Exception e) {
//        }
//    }
    /**
     * Gets an ArrayList containing all the Variables in the Metadata.
     *
     * @return ArrayList containing all the Variables in the Metadata.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

//    /**
//     * Sets an ArrayList containing all the Variables in the Metadata.
//     *
//     * @param variables ArrayList containing all the Variables in the Metadata.
//     */
//    public void setVariables(ArrayList<VariableMu> variables) {
//        this.variables = variables;
//    }
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

//    public static void main (String[] args) throws ArgusException{
//        MetadataMu t = new MetadataMu();
//        t.readMetadata(t.getMetadataFile());
//    }
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
