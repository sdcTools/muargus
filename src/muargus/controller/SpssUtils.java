package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import argus.utils.Tokenizer;
import com.ibm.statistics.plugin.Case;
import com.ibm.statistics.plugin.Cursor;
import com.ibm.statistics.plugin.DataUtil;
import com.ibm.statistics.plugin.NumericMissingValueType;
import com.ibm.statistics.plugin.StatsException;
import com.ibm.statistics.plugin.StatsUtil;
import com.ibm.statistics.plugin.Variable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.CalculationService;
import muargus.model.MetadataMu;
import muargus.model.SpssVariable;
import muargus.model.VariableMu;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author pibd05
 */
public class SpssUtils {

    public final static String tempDataFileExtension = "dat";
    public final static int NUMERIC = 0;
    public static boolean fixed = false;
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
            try {
                StatsUtil.start();
                StatsUtil.submit("get file = \"" + metadata.getFileNames().getDataFileName() + "\".");
                Cursor c = new Cursor();
                for (int i = 0; i < StatsUtil.getVariableCount(); i++) {
                    SpssVariable variable = new SpssVariable(StatsUtil.getVariableName(i), StatsUtil.getVariableFormatDecimal(i),
                            StatsUtil.getVariableFormatWidth(i), StatsUtil.getVariableMeasurementLevel(i),
                            StatsUtil.getVariableType(i), StatsUtil.getVariableLabel(i), StatsUtil.getVariableAttributeNames(i),
                            StatsUtil.getVariableFormat(i));
                    if (variable.getVariableType() == SpssUtils.NUMERIC) {
                        variable.setNumericValueLabels(c.getNumericValueLabels(i));
                        variable.setNumericMissings(StatsUtil.getNumericMissingValues(i));
                    } else {
                        variable.setStringValueLabels(c.getStringValueLabels(i));
                        variable.setStringMissings(StatsUtil.getStringMissingValues(i));
                    }
                    metadata.getSpssVariables().add(variable);
                }
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

    public static void checkMetadata(MetadataMu metadata) {
        List<SpssVariable> spssVariables = SpssUtils.getVariablesFromSpss(metadata);
        for (VariableMu variable : metadata.getVariables()) {
            boolean found = false;
            for (SpssVariable spssVariable : spssVariables) {
                if (variable.getName().equals(spssVariable.getName())) {
                    //TODO: add some more checks here
                    variable.setSpssVariable(spssVariable);
                    found = true;
                    break;
                }
            }
            if (!found) {
                metadata.getVariables().removeAll(metadata.getVariables());
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
    private static String getIntIfPossible(double value) {
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
            metadata.setSpssNumberOfCases(data.length);

            try {
                // Sets the temporary filename
                metadata.setSpssTempDataFileName(FilenameUtils.removeExtension(fileName) + "." + SpssUtils.tempDataFileExtension);
                try (PrintWriter writer = new PrintWriter(new File(metadata.getSpssTempDataFileName()))) {
                    for (Case c : data) {
                        writer.println(c.toString().substring(1, c.toString().length() - 1).replace("null", "").replace(',', ';'));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            StatsUtil.stop();
        } catch (StatsException ex) {
            Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeFixedFormat(MetadataMu metadata) {
        try {
            // start spss and make an instance of dataUtil
            StatsUtil.start();

            // Make an array of variable names to use as a filter
            ArrayList<String> variables = new ArrayList<>();
            for (SpssVariable v : metadata.getSpssVariables()) {
                if (v.isSelected()) {
                    variables.add(v.getName());
                }
            }

            String variablesCommand = "";
            for (String s : variables) {
                variablesCommand = variablesCommand + s + " ";
            }
            String fileName = metadata.getFileNames().getDataFileName();
            // Sets the temporary filename
            metadata.setSpssTempDataFileName(FilenameUtils.removeExtension(fileName) + "." + SpssUtils.tempDataFileExtension);
            

            String[] command = {"SET DECIMAL=DOT.",
                "get file = '" + fileName + "'.",
                "WRITE OUTFILE= '" + metadata.getSpssTempDataFileName() + "'/" + variablesCommand + ".",
                "EXECUTE."
            };
            
            StatsUtil.submit(command);

            StatsUtil.stop();
        } catch (StatsException ex) {
            Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void makeSafeFileSpss(MetadataMu metadata) {
        try {
            try {
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(SpssUtils.safeFile));
                Tokenizer tokenizer = new Tokenizer(reader);
                String[][] data = new String[metadata.getVariables().size()][metadata.getSpssNumberOfCases()];
                for (int i = 0; i < data[0].length; i++) {
                    String[] temp = tokenizer.nextLine().split(metadata.getSeparator());
                    for (int j = 0; j < data.length; j++) {
                        if (temp[j].isEmpty()) {
                            data[j][i] = "";
                        } else {
                            data[j][i] = temp[j];
                        }
                    }
                }

                StatsUtil.start();
                StatsUtil.submit("GET FILE='" + metadata.getFileNames().getDataFileName() + "'.");
                DataUtil d = new DataUtil();
                for (int i = 0; i < metadata.getVariables().size(); i++) {
                    SpssVariable variable = metadata.getVariables().get(i).getSpssVariable();
                    Variable temp = new Variable("TEMP" + variable.getName(), variable.getVariableType());
                    temp.setMeasureLevel(variable.getMeasurementLevel());
                    temp.setVarLabel(variable.getVariableLabel());
                    temp.setFormatDecimal(variable.getNumberOfDecimals());
                    temp.setFormatWidth(variable.getVariableLength());
                    if (variable.getVariableType() == SpssUtils.NUMERIC) { // 0 is numeric
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
                ArrayList<VariableMu> variables = metadata.getVariables();
                String first = variables.get(0).getSpssVariable().getName();
                String last = variables.get(variables.size()-1).getSpssVariable().getName();


                for (VariableMu v : metadata.getVariables()) {
                    String name = v.getSpssVariable().getName();
                    StatsUtil.submit("if (SYSMIS(" + name + ") EQ 0) " + name + "= TEMP" + name + ".");
                }
                StatsUtil.submit("SAVE OUTFILE='" + SpssUtils.safeSpssFile + "'/DROP=TEMP" + first + " TO TEMP" + last + ".");
                StatsUtil.stop();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (StatsException ex) {
            Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (VariableMu v : metadata.getVariables()) {
            System.out.println(v.getName() + ": " + v.getStartingPosition());
        }
    }

}
