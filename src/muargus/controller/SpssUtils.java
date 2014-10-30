//TODO: test for String format and add the possibility to add/read/write date/time format
package muargus.controller;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import argus.utils.Tokenizer;
import com.ibm.statistics.plugin.Case;
import com.ibm.statistics.plugin.Cursor;
import com.ibm.statistics.plugin.DataUtil;
import com.ibm.statistics.plugin.NumericMissingValueType;
import com.ibm.statistics.plugin.StatsException;
import com.ibm.statistics.plugin.StatsUtil;
import com.ibm.statistics.plugin.Variable;
import com.ibm.statistics.plugin.VariableFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.model.MetadataMu;
import muargus.model.SpssVariable;
import muargus.model.VariableMu;

/**
 *
 * @author pibd05
 */
public class SpssUtils {

    public final static String tempDataFileExtension = ".dat";
    public final static String tempName = "temp";
    public final static int NUMERIC = 0;
    public static boolean fixed = true;
    public static File spssTempDataFiles;
    public static File safeFile;
    public static File safeSpssFile = new File("C:\\Users\\Gebruiker\\Desktop\\safe.sav");

    /**
     * Gets the variables from spss. For every variable an instance of the
     * SpssVariable class is made containing all the information of this
     * variable.
     *
     * @param metadata Metadata file.
     * @return List List containing the SpssVariable instances.
     */
    public static List<SpssVariable> getVariablesFromSpss(MetadataMu metadata) {
        if (metadata.getSpssVariables().size() < 1) {
            metadata.setSpssDataFileName(metadata.getFileNames().getDataFileName());
            try {
                StatsUtil.start();
                StatsUtil.submit("get file = \"" + metadata.getSpssDataFileName() + "\".");
                Cursor c = new Cursor();
                for (int i = 0; i < StatsUtil.getVariableCount(); i++) {
                    SpssVariable variable = new SpssVariable(StatsUtil.getVariableName(i), StatsUtil.getVariableFormatDecimal(i),
                            StatsUtil.getVariableFormatWidth(i), StatsUtil.getVariableMeasurementLevel(i),
                            StatsUtil.getVariableType(i), StatsUtil.getVariableLabel(i), StatsUtil.getVariableAttributeNames(i),
                            StatsUtil.getVariableFormat(i));
                    // set numeric or string missings & value labels
                    if (variable.getVariableType() == SpssUtils.NUMERIC) {
                        variable.setNumericValueLabels(c.getNumericValueLabels(i));
                        variable.setNumericMissings(StatsUtil.getNumericMissingValues(i));
                    } else {
                        variable.setStringValueLabels(c.getStringValueLabels(i));
                        variable.setStringMissings(StatsUtil.getStringMissingValues(i));
                    }
                    metadata.getSpssVariables().add(variable);
                }
                metadata.setRecordCount(StatsUtil.getCaseCount());
                StatsUtil.stop();
            } catch (StatsException e) {

            }
        }
        return metadata.getSpssVariables();
    }

    /**
     * Sets the variables for use whithin argus.
     *
     * @param variables List containing the SpssVariable instances.
     * @param metadata Metadata file.
     */
    public static void setVariablesSpss(List<SpssVariable> variables, MetadataMu metadata) {
        int startingPos = 1;
        for (SpssVariable variable : variables) {
            // add selected variables
            if (variable.isSelected()) {
                VariableMu v = new VariableMu(variable.getName());
                if (!doesVariableExist(v, metadata)) {
                    // Set the missing values and variableLength either for numeric or for string missing values
                    int variableLength = variable.getVariableLength();
                    //TODO: add time/date type
                    if (variable.getVariableType() == SpssUtils.NUMERIC) {
                        for (int i = 0; i < variable.getNumericMissings().length; i++) {
                            if (i == VariableMu.MAX_NUMBER_OF_MISSINGS) {
                                break;
                            }
                            v.setMissing(i, getIntIfPossible(variable.getNumericMissings()[i]));
                            if (getIntIfPossible(variable.getNumericMissings()[i]).length() > variableLength) {
                                variableLength = getIntIfPossible(variable.getNumericMissings()[i]).length();
                            }
                        }
                    } else {
                        for (int i = 0; i < variable.getStringMissings().length; i++) {
                            if (i == VariableMu.MAX_NUMBER_OF_MISSINGS) {
                                break;
                            }
                            v.setMissing(i, variable.getStringMissings()[i]);
                            if (variable.getStringMissings()[i].length() > variableLength) {
                                variableLength = variable.getStringMissings()[i].length();
                            }
                        }
                    }

                    // Set all other relevant information.
                    v.setVariableLength(variableLength);
                    v.setDecimals(variable.getNumberOfDecimals());
                    v.setNumeric(variable.isNumeric());
                    v.setCategorical(variable.isCategorical());
                    v.setStartingPosition(startingPos);
                    if (SpssUtils.fixed) {
                        startingPos += variable.getVariableLength();
                    }
                    v.setSpssVariable(variable);
                    metadata.getVariables().add(v);
                } else {
                    // reset the startingposition
                    SpssUtils.getVariable(v, metadata).setStartingPosition(startingPos);
                    startingPos += variable.getVariableLength();
                }
            } else {
                // remove variables that are not selected.
                VariableMu v = new VariableMu(variable.getName());
                if (doesVariableExist(v, metadata)) {
                    removeVariable(variable.getName(), metadata);
                }
            }
        }
    }

    /**
     * 
     * @param metadata 
     */
    public static void checkMetadata(MetadataMu metadata) {
        List<SpssVariable> spssVariables = SpssUtils.getVariablesFromSpss(metadata);
        for (VariableMu variable : metadata.getVariables()) {
            boolean found = false;
            for (SpssVariable spssVariable : spssVariables) {
                if (variable.getName().equals(spssVariable.getName())
                        && variable.getVariableLength() == spssVariable.getVariableLength()
                        && variable.getDecimals() == spssVariable.getNumberOfDecimals()) {
                    for (int i = 0; i < variable.getNumberOfMissings(); i++) {
                        if (!variable.getMissing(0).equals(spssVariable.getMissing(0))) {
                            break;
                        }
                    }
                    variable.setSpssVariable(spssVariable);
                    found = true;
                    break;
                }
            }
            if (!found) {
                metadata.getVariables().removeAll(metadata.getVariables());
                //TODO: verander naar pop-up schermpje
                System.out.println("Metadatafile does not equal the spss metadata");
                break;
            }
        }
    }

    /**
     * Checks whether a variable already exists.
     *
     * @param variable The Variable instance to be checked for already existing
     * copies.
     * @param metadata Metadata file.
     * @return Boolean indicating whether the variable already exists.
     */
    public static boolean doesVariableExist(VariableMu variable, MetadataMu metadata) {
        boolean doubleVariable = false;
        for (VariableMu v : metadata.getVariables()) {
            if (v.getName().equals(variable.getName())) {
                doubleVariable = true;
            }
        }
        return doubleVariable;
    }

    /**
     * 
     * @param variable
     * @param metadata
     * @return 
     */
    public static VariableMu getVariable(VariableMu variable, MetadataMu metadata) {
        for (VariableMu v : metadata.getVariables()) {
            if (v.getName().equals(variable.getName())) {
                return v;
            }
        }
        return null;
    }

    /**
     * Removes a variable from the metadata.
     *
     * @param variableName String containing the variableName.
     * @param metadata Metadata file.
     */
    public static void removeVariable(String variableName, MetadataMu metadata) {
        for (VariableMu v : metadata.getVariables()) {
            if (v.getName().equals(variableName)) {
                metadata.getVariables().remove(v);
                break;
            }
        }
    }

    /**
     * Returns a String containing an integer if possible.
     *
     * @param value Double containing the value to be converted to an int.
     * @return String containing the shortest notation of the value.
     */
    public static String getIntIfPossible(double value) {
        double value_double;
        String value_String = null;
        try {
            value_double = StrUtils.toDouble(Double.toString(value));
            if ((value_double == Math.floor(value_double)) && !Double.isInfinite(value_double)) {
                int value_int = (int) value_double;
                value_String = Integer.toString(value_int);
            } else {
                value_String = Double.toString(value_double);
            }
        } catch (ArgusException ex) {
            //TODO: change warning message
            System.out.println("warning");
        }
        return value_String;
    }

    /**
     * Generates temporary data from the spss data file.
     *
     * @param metadata Metadata file.
     */
    public static void generateSpssData(MetadataMu metadata) {
        if (SpssUtils.fixed) {
            SpssUtils.writeFixedFormat(metadata);
        } else {
            SpssUtils.writeFreeFormat(metadata);
        }
    }

    /**
     * 
     * @param metadata
     * @return 
     */
    private static String getFilter(MetadataMu metadata) {
        // check if variables are selected
        boolean noVariablesSelected = true;
        for (SpssVariable variable : metadata.getSpssVariables()) {
            if (variable.isSelected()) {
                noVariablesSelected = false;
                break;
            }
        }

        /* Make a String of variable names to use as a filter. If no variables
         are selected (when a .rda file exists), use all variables specified
         in the metadata, otherwise only the selected variables */
        String variableFilter = "";
        for (SpssVariable v : metadata.getSpssVariables()) {
            if (noVariablesSelected || v.isSelected()) {
                variableFilter += v.getName() + " ";
            }
        }

        return variableFilter;
    }

    /**
     * 
     * @param metadata 
     */
    private static void writeFixedFormat(MetadataMu metadata) {
        try {
            // start spss and make an instance of dataUtil
            StatsUtil.start();
            // Sets the temporary filename
            SpssUtils.spssTempDataFiles = File.createTempFile(SpssUtils.tempName, SpssUtils.tempDataFileExtension);
            SpssUtils.spssTempDataFiles.deleteOnExit();
            /* make te commands for spss to write the data from the selected variables to a fixed format data file. */
            String[] command = {"SET DECIMAL=DOT.",
                "get file = '" + metadata.getSpssDataFileName() + "'.",
                "WRITE BOM=NO OUTFILE= '" + SpssUtils.spssTempDataFiles.getPath()
                + "'/" + SpssUtils.getFilter(metadata) + ".",
                "EXECUTE."
            };
            StatsUtil.submit(command);
            StatsUtil.stop();
            SpssUtils.setNewDataFile(metadata);
        } catch (StatsException ex) {
            Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpssUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param metadata 
     */
    private static void setNewDataFile(MetadataMu metadata) {
        DataFilePair filenames = new DataFilePair(SpssUtils.spssTempDataFiles.getPath(), metadata.getFileNames().getMetaFileName());
        metadata.setFileNames(filenames);
    }

    /**
     * 
     * @param safeMetadata 
     */
    public static void makeSafeFileSpss(MetadataMu safeMetadata) {
        try {
            StatsUtil.start();
            ArrayList<VariableMu> variables = safeMetadata.getVariables();
            String first = variables.get(0).getSpssVariable().getName();
            String last = variables.get(variables.size() - 1).getSpssVariable().getName();

            ArrayList<String> command = new ArrayList<>();
            command.add("TITLE   'MERGECOPYCLEAN'.");
            command.add("SET DECIMAL=DOT.");
            command.add("DATA LIST FILE = '" + safeMetadata.getFileNames().getDataFileName() + "'/");
            int startPosition = 1;
            int endPosition;
            for (VariableMu v : safeMetadata.getVariables()) {
                String name = v.getSpssVariable().getName();
                endPosition = startPosition + v.getVariableLength() - 1;
                command.set(command.size() - 1, command.get(command.size() - 1)
                        + " TEMP" + name + " " + startPosition + " - " + endPosition);
                if(v.getSpssVariable().getVariableFormat().equals(VariableFormat.A)){
                     command.set(command.size() - 1, command.get(command.size() - 1) + " (A) ");
                }
                startPosition = endPosition + 1;
            }
            command.set(command.size() - 1, command.get(command.size() - 1) + ".");
            command.add("MATCH FILES FILE = '" + safeMetadata.getSpssDataFileName() + "' /FILE = *.");
            command.add("EXECUTE.");
            for (VariableMu v : safeMetadata.getVariables()) {
                String name = v.getSpssVariable().getName();
                command.add("if (SYSMIS(" + name + ") EQ 0) " + name + "= TEMP" + name + ".");
            }
            command.add("SAVE OUTFILE='" + SpssUtils.safeSpssFile + "'/DROP=TEMP" + first + " TO TEMP" + last + ".");
            StatsUtil.submit(command.toArray(new String[command.size()]));
            StatsUtil.stop();
        } catch (StatsException ex) {
            //Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // WARNING: this has not been properly tested
    /**
     * 
     * @param metadata 
     */
    private static void writeFreeFormat(MetadataMu metadata) {
        try {
            // start spss and make an instance of dataUtil
            StatsUtil.start();

            // Get the spss data file
            String fileName = metadata.getFileNames().getDataFileName();
            StatsUtil.submit("get file = '" + fileName + "'.");

            // Make an array of variable names to use as a filter
            ArrayList<String> variables = new ArrayList<>();
            for (SpssVariable v : metadata.getSpssVariables()) {
                if (v.isSelected()) {
                    variables.add(v.getName());
                }
            }
            DataUtil dataUtil = new DataUtil();
            dataUtil.setVariableFilter(variables.toArray(new String[variables.size()]));

            // fetch an array of cases. One case contains the data for the variables specified in the filter.
            Case[] data = dataUtil.fetchCases(true, 0);

            // sets the number of cases in the metadata (this is used during the writing of the safe data file).
            metadata.setRecordCount(data.length);

            try {
                // Sets the temporary filename
                SpssUtils.spssTempDataFiles = File.createTempFile(SpssUtils.tempName, SpssUtils.tempDataFileExtension);
                SpssUtils.spssTempDataFiles.deleteOnExit();
                try (PrintWriter writer = new PrintWriter(spssTempDataFiles)) {
                    for (Case c : data) {
                        writer.println(c.toString().substring(1, c.toString().length() - 1).replace("null", "").replace(',', ';'));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SpssUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            StatsUtil.stop();
        } catch (StatsException ex) {
            Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // WARNING: this has not been properly tested
    /**
     * 
     * @param safeMetadata 
     */
    public static void makeSafeFileFreeformat(MetadataMu safeMetadata) {
        try {
            try {
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(SpssUtils.safeFile));
                Tokenizer tokenizer = new Tokenizer(reader);
                String[][] data = new String[safeMetadata.getVariables().size()][safeMetadata.getRecordCount()];
                for (int i = 0; i < data[0].length; i++) {

                    String[] temp = tokenizer.nextLine().split(safeMetadata.getSeparator());
                    for (int j = 0; j < data.length; j++) {
                        if (temp[j].isEmpty()) {
                            data[j][i] = "";
                        } else {
                            data[j][i] = temp[j];
                        }
                    }
                    String line = tokenizer.nextLine();

                    int start = 0;
                    for (int j = 0; j < data.length; j++) {
                        int end = safeMetadata.getVariables().get(j).getVariableLength() + start;
                        data[j][i] = line.substring(start, end);
                        start = end;
                    }
                }

                StatsUtil.start();
                StatsUtil.submit("GET FILE='" + safeMetadata.getSpssDataFileName() + "'.");
                DataUtil d = new DataUtil();
                for (int i = 0; i < safeMetadata.getVariables().size(); i++) {
                    SpssVariable variable = safeMetadata.getVariables().get(i).getSpssVariable();
                    Variable temp = new Variable("TEMP" + variable.getName(), variable.getVariableType());
                    temp.setMeasureLevel(variable.getMeasurementLevel());
                    temp.setVarLabel(variable.getVariableLabel());
                    temp.setFormatDecimal(variable.getNumberOfDecimals());
                    temp.setFormatWidth(variable.getVariableLength());
                    if (variable.getVariableType() == SpssUtils.NUMERIC) {
                        double[] doubleData = new double[data[i].length];
                        for (int j = 0; j < data[i].length; j++) {
                            if (!data[i][j].equals("")) {
                                doubleData[j] = Double.parseDouble(data[i][j]);
                            }
                        }
                        temp.setNumericVarMissingValues(variable.getNumericMissings(), NumericMissingValueType.DISCRETE);
                        temp.setNumValueLabels(variable.getNumericValueLabels());
                        d.addVariableWithValue(temp, doubleData, SpssUtils.NUMERIC);
                    } else {
                        temp.setStringVarMissingValues(variable.getStringMissings());
                        temp.setStrValueLabels(variable.getStringValueLabels());
                        d.addVariableWithValue(temp, data[i], 0);
                    }
                }
                d.release();
                ArrayList<VariableMu> variables = safeMetadata.getVariables();
                String first = variables.get(0).getSpssVariable().getName();
                String last = variables.get(variables.size() - 1).getSpssVariable().getName();

                for (VariableMu v : safeMetadata.getVariables()) {
                    String name = v.getSpssVariable().getName();
                    StatsUtil.submit("if (SYSMIS(" + name + ") EQ 0) " + name + "= TEMP" + name + ".");
                }
                StatsUtil.submit("SAVE OUTFILE='" + SpssUtils.safeSpssFile + "'/DROP=TEMP" + first + " TO TEMP" + last + ".");
                StatsUtil.stop();
            } catch (FileNotFoundException ex) {
                //Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (StatsException ex) {
            //Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
