package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import com.ibm.statistics.plugin.Case;
import com.ibm.statistics.plugin.Cursor;
import com.ibm.statistics.plugin.DataUtil;
import com.ibm.statistics.plugin.StatsException;
import com.ibm.statistics.plugin.StatsUtil;
import java.io.File;
import java.io.FileNotFoundException;
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

    public final static String tempDataFileExtension = "asc";
    public final static int NUMERIC = 0;

    public SpssUtils() {
    }

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
                    v.setStartingPosition(metadata.getSpssStartingPosition());
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
        try {
            // start spss and make an instance of dataUtil
            StatsUtil.start();

            // Get the spss data file
            String fileName = metadata.getFileNames().getDataFileName();
            StatsUtil.submit("get file = \"" + fileName + "\".");

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
                String fileNameNew = metadata.getFileNames().getDataFileName();
                metadata.setSpssTempDataFileName(fileNameNew.substring(0, fileNameNew.length() - 3) + SpssUtils.tempDataFileExtension);

                // Writes the spss data to a temporary file
                // TODO: chang to fixed format
                try(PrintWriter writer = new PrintWriter(new File(metadata.getSpssTempDataFileName()))){
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

}
