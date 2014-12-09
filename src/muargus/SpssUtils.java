//TODO: test for String format and add the possibility to add/read/write date/time format
package muargus;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import argus.utils.Tokenizer;
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
import javax.swing.JOptionPane;
import muargus.model.MetadataMu;
import argus.model.SpssVariable;
import com.ibm.statistics.plugin.Case;
import com.ibm.statistics.plugin.Cursor;
import com.ibm.statistics.plugin.DataUtil;
import com.ibm.statistics.plugin.NumericMissingValueType;
import com.ibm.statistics.plugin.StatsException;
import com.ibm.statistics.plugin.StatsUtil;
import com.ibm.statistics.plugin.Variable;
import com.ibm.statistics.plugin.VariableFormat;
import java.awt.Frame;
import javax.swing.JFileChooser;
import muargus.controller.SelectCombinationsController;
import muargus.model.VariableMu;

/**
 * SpssUtils class.
 *
 * @author Statistics Netherlands
 */
public class SpssUtils {

    private final String tempDataFileExtension = ".dat";
    private final String tempName = "temp";
    private final int NUMERIC = 0;
    private final boolean fixed = true;
    private final ArrayList<SpssVariable> spssVariables = new ArrayList<>();
    public String spssDataFileName; // original spss file (.sav) containing the original data
    private File spssTempDataFiles; // temporary fixed format (.dat) file containing the original data
    public File safFile; // safe fixed format file (.saf) containing the safe data
    public File safeSpssFile; // safe spss file (.sav) containing the safe data

    /**
     * Gets the variables from spss. For every variable an instance of the
     * SpssVariable class is made containing all the information of this
     * variable.
     *
     * @param metadata Metadata file.
     * @param parent the Frame of the mainFrame.
     * @return List List containing the SpssVariable instances.
     */
    public List<SpssVariable> getVariablesFromSpss(MetadataMu metadata, Frame parent) {
        //getSpssInstallationDirectory(parent);
        //if (this.spssVariables.size() < 1) {
        this.spssVariables.clear();
            this.spssDataFileName = metadata.getFileNames().getDataFileName();
            try {
                StatsUtil.start();
                StatsUtil.submit("get file = \"" + this.spssDataFileName + "\".");
                Cursor c = new Cursor();
                for (int i = 0; i < StatsUtil.getVariableCount(); i++) {
                    SpssVariable variable = new SpssVariable(StatsUtil.getVariableName(i), StatsUtil.getVariableFormatDecimal(i),
                            StatsUtil.getVariableFormatWidth(i), StatsUtil.getVariableMeasurementLevel(i),
                            StatsUtil.getVariableType(i), StatsUtil.getVariableLabel(i), StatsUtil.getVariableAttributeNames(i),
                            StatsUtil.getVariableFormat(i));
                    // set numeric or string missings & value labels
                    if (variable.getVariableType() == this.NUMERIC) {
                        variable.setNumericValueLabels(c.getNumericValueLabels(i));
                        variable.setNumericMissings(StatsUtil.getNumericMissingValues(i));
                    } else {
                        variable.setStringValueLabels(c.getStringValueLabels(i));
                        variable.setStringMissings(StatsUtil.getStringMissingValues(i));
                    }
                    this.spssVariables.add(variable);
                }
                metadata.setRecordCount(StatsUtil.getCaseCount());
                StatsUtil.stop();
            } catch (StatsException e) { }
        //}
        return this.spssVariables;
    }

    public ArrayList<SpssVariable> getSpssVariables() {
        return spssVariables;
    }
    
    

    /**
     * Sets the variables for use whithin argus.
     *
     * @param variables List containing the SpssVariable instances.
     * @param metadata Metadata file.
     */
    public void setVariablesSpss(List<SpssVariable> variables, MetadataMu metadata) {
        int startingPos = 1;
        for (SpssVariable spssVariable : variables) {
            // add selected variables
            if (spssVariable.isSelected()) {
                VariableMu variable = new VariableMu(spssVariable.getName());
                if (!doesVariableExist(variable, metadata)) {
                    // Set the missing values and variableLength either for numeric or for string missing values
                    int variableLength = spssVariable.getVariableLength();
                    //TODO: add time/date type
                    if (spssVariable.getVariableType() == this.NUMERIC) {
                        for (int i = 0; i < spssVariable.getNumericMissings().length; i++) {
                            if (i == VariableMu.MAX_NUMBER_OF_MISSINGS) {
                                break;
                            }
                            variable.setMissing(i, getIntIfPossible(spssVariable.getNumericMissings()[i]));
                            if (getIntIfPossible(spssVariable.getNumericMissings()[i]).length() > variableLength) {
                                variableLength = getIntIfPossible(spssVariable.getNumericMissings()[i]).length();
                            }
                        }
                    } else {
                        for (int i = 0; i < spssVariable.getStringMissings().length; i++) {
                            if (i == VariableMu.MAX_NUMBER_OF_MISSINGS) {
                                break;
                            }
                            variable.setMissing(i, spssVariable.getStringMissings()[i]);
                            if (spssVariable.getStringMissings()[i].length() > variableLength) {
                                variableLength = spssVariable.getStringMissings()[i].length();
                            }
                        }
                    }

                    // Set all other relevant information.
                    variable.setVariableLength(variableLength);
                    variable.setDecimals(spssVariable.getNumberOfDecimals());
                    variable.setNumeric(spssVariable.isNumeric());
                    variable.setCategorical(spssVariable.isCategorical());
                    variable.setStartingPosition(startingPos);
                    if (this.fixed) {
                        startingPos += spssVariable.getVariableLength();
                    }
                    variable.setSpssVariable(spssVariable);
                    metadata.getVariables().add(variable);
                } else {
                    // reset the startingposition
                    this.getVariable(variable, metadata).setStartingPosition(startingPos);
                    startingPos += spssVariable.getVariableLength();
                }
            } else {
                // remove variables that are not selected.
                VariableMu v = new VariableMu(spssVariable.getName());
                if (doesVariableExist(v, metadata)) {
                    removeVariable(spssVariable.getName(), metadata);
                }
            }
        }
    }

    /**
     * Checks whether the metadata in the .rda file si conform the spss
     * metadata.
     *
     * @param metadata MetadataMu instance containing the metadata as specified
     * in the .rda file.
     * @param parent the Frame of the mainFrame.
     */
    public void verifyMetadata(MetadataMu metadata, Frame parent) {
        this.getVariablesFromSpss(metadata, parent);
        for (VariableMu variable : metadata.getVariables()) {
            boolean found = false;
            outerloop:
            for (SpssVariable spssVariable : this.spssVariables) {
                if (variable.getName().equals(spssVariable.getName())
                        //&& variable.getVariableLength() == spssVariable.getVariableLength()
                        && variable.getDecimals() == spssVariable.getNumberOfDecimals()) {
                    for (int i = 0; i < variable.getNumberOfMissings(); i++) {
                        String missing;
                        if (spssVariable.getVariableType() == this.NUMERIC) {
                            missing = this.getIntIfPossible(spssVariable.getNumericMissings()[i]);
                        } else {
                            missing = spssVariable.getStringMissings()[i];
                        }
                        if (!variable.getMissing(i).equals(missing)) {
                            break outerloop;
                        }
                    }
                    variable.setSpssVariable(spssVariable);
                    found = true;
                    break;
                }
            }
            if (!found) {
                metadata.getVariables().removeAll(metadata.getVariables());
                JOptionPane.showMessageDialog(null, "Metadatafile does not equal the spss metadata."
                        + "\nReload the metadata via specify metadata.", "Spss metadata error", JOptionPane.ERROR_MESSAGE);
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
    private boolean doesVariableExist(VariableMu variable, MetadataMu metadata) {
        boolean doubleVariable = false;
        for (VariableMu v : metadata.getVariables()) {
            if (v.getName().equals(variable.getName())) {
                doubleVariable = true;
            }
        }
        return doubleVariable;
    }

    /**
     * Gets the double variable.
     *
     * @param variable VariableMu instance for which the double variable needs
     * to be found.
     * @param metadata MetadataMu instance containing the metadata.
     * @return VariableMu instance of the double variable.
     */
    private VariableMu getVariable(VariableMu variable, MetadataMu metadata) {
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
    private void removeVariable(String variableName, MetadataMu metadata) {
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
    public String getIntIfPossible(double value) {
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
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return value_String;
    }

    /**
     * Generates temporary data from the spss data file.
     *
     * @param metadata Metadata file.
     */
    public void generateSpssData(MetadataMu metadata) {
        if (this.fixed) {
            writeFixedFormat(metadata);
        } else {
            writeFreeFormat(metadata);
        }
    }

    /**
     * Gets the filter for which the data needs to be imported.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return String containing all variables that are selected.
     */
    private String getFilter() {
        // check if variables are selected
        boolean noVariablesSelected = true;
        for (SpssVariable variable : this.spssVariables) {
            if (variable.isSelected()) {
                noVariablesSelected = false;
                break;
            }
        }

        /* Make a String of variable names to use as a filter. If no variables
         are selected (when a .rda file exists), use all variables specified
         in the metadata, otherwise only the selected variables */
        String variableFilter = "";
        for (SpssVariable v : this.spssVariables) {
            if (noVariablesSelected || v.isSelected()) {
                variableFilter += v.getName() + " ";
            }
        }

        return variableFilter;
    }

    /**
     * Extracts the data from spss and writes this to a temporary fixed format
     * file.
     *
     * @param metadata MetadataMu instance containing the metadata.
     */
    private void writeFixedFormat(MetadataMu metadata) {
        try {
            // start spss and make an instance of dataUtil
            StatsUtil.start();
            // Sets the temporary filename
            this.spssTempDataFiles = File.createTempFile(this.tempName, this.tempDataFileExtension);
            this.spssTempDataFiles.deleteOnExit();
            /* make te commands for spss to write the data from the selected variables to a fixed format data file. */
            String[] command = {"SET DECIMAL=DOT.",
                "get file = '" + this.spssDataFileName + "'.",
                "WRITE BOM=NO OUTFILE= '" + this.spssTempDataFiles.getPath()
                + "'/" + getFilter() + ".",
                "EXECUTE."
            };
            StatsUtil.submit(command);
            StatsUtil.stop();
            setNewDataFile(metadata);
        } catch (StatsException ex) {
            Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpssUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets the temp data file as the new data file.
     *
     * @param metadata MetadataMu instance containing the metadata.
     */
    private void setNewDataFile(MetadataMu metadata) {
        DataFilePair filenames = new DataFilePair(this.spssTempDataFiles.getPath(), metadata.getFileNames().getMetaFileName());
        metadata.setFileNames(filenames);
    }

    /**
     * Makes the safe file using fixed format.
     *
     * @param safeMetadata MetadataMu instance containing the safe metadata.
     */
    public void makeSafeFileSpss(MetadataMu safeMetadata) {
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

            for (VariableMu v : variables) {
                String name = v.getSpssVariable().getName();
                endPosition = startPosition + v.getVariableLength() - 1;
                command.add(" TEMP" + name + " " + startPosition + " - " + endPosition);
                if (v.getSpssVariable().getVariableFormat().equals(VariableFormat.A)) {
                    command.set(command.size() - 1, command.get(command.size() - 1) + " (A) ");
                }
                startPosition = endPosition + 1;
            }

            command.set(command.size() - 1, command.get(command.size() - 1) + ".");
            command.add("MATCH FILES FILE = '" + this.spssDataFileName + "' /FILE = *.");
            command.add("EXECUTE.");
            for (VariableMu v : variables) {
                String name = v.getSpssVariable().getName();
                String statement;
                if(v.getSpssVariable().getVariableType() == this.NUMERIC){
                    statement= "if (SYSMIS(" + name + ") EQ 0) ";
                } else {
                    statement = "COMPUTE ";
                }
                command.add(statement + name + "= TEMP" + name + ".");
                String missing = "";
                for (int i = 0; i < v.getNumberOfMissings(); i++) {
                    if (!missing.equals("")) {
                        missing += ", ";
                    }
                    missing += "\"" + v.getMissing(i) + "\"";
                }
                command.add("MISSING VALUE " + name + "(" + missing + ").");
            }
            command.add("SAVE OUTFILE='" + this.safeSpssFile + "'/DROP=TEMP" + first + " TO TEMP" + last + ".");
            command.add("EXECUTE.");
//            for (String s : command) {
//                System.out.println(s);
//            }

            StatsUtil.submit(command.toArray(new String[command.size()]));
            StatsUtil.stop();
        } catch (StatsException ex) {
            //Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // WARNING: this has not been properly tested
    /**
     * Extracts the data from spss and writes this to a comma separated file
     * (free format).
     *
     * @param metadata MetadataMu instance containing the metadata.
     */
    private void writeFreeFormat(MetadataMu metadata) {
        try {
            // start spss and make an instance of dataUtil
            StatsUtil.start();

            // Get the spss data file
            String fileName = metadata.getFileNames().getDataFileName();
            StatsUtil.submit("get file = '" + fileName + "'.");

            // Make an array of variable names to use as a filter
            ArrayList<String> variables = new ArrayList<>();
            for (SpssVariable v : this.spssVariables) {
                if (v.isSelected()) {
                    variables.add(v.getName());
                }
            }
            DataUtil dataUtil = new DataUtil();
            dataUtil.setVariableFilter(variables.toArray(new String[variables.size()]));

            // fetch an array of cases. One case contains the data for the variables specified in the filter.
            Case[] data = dataUtil.fetchCases(true, 0);

            try {
                // Sets the temporary filename
                this.spssTempDataFiles = File.createTempFile(this.tempName, this.tempDataFileExtension);
                this.spssTempDataFiles.deleteOnExit();
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
     * Makes the safe file using free format.
     *
     * @param safeMetadata MetadataMu instance of the safe metadata.
     */
    public void makeSafeFileFreeformat(MetadataMu safeMetadata) {
        try {
            try {
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(this.safFile));
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
                StatsUtil.submit("GET FILE='" + this.spssDataFileName + "'.");
                DataUtil d = new DataUtil();
                for (int i = 0; i < safeMetadata.getVariables().size(); i++) {
                    SpssVariable variable = safeMetadata.getVariables().get(i).getSpssVariable();
                    Variable temp = new Variable("TEMP" + variable.getName(), variable.getVariableType());
                    temp.setMeasureLevel(variable.getMeasurementLevel());
                    temp.setVarLabel(variable.getVariableLabel());
                    temp.setFormatDecimal(variable.getNumberOfDecimals());
                    temp.setFormatWidth(variable.getVariableLength());
                    if (variable.getVariableType() == this.NUMERIC) {
                        double[] doubleData = new double[data[i].length];
                        for (int j = 0; j < data[i].length; j++) {
                            if (!data[i][j].equals("")) {
                                doubleData[j] = Double.parseDouble(data[i][j]);
                            }
                        }
                        temp.setNumericVarMissingValues(variable.getNumericMissings(), NumericMissingValueType.DISCRETE);
                        temp.setNumValueLabels(variable.getNumericValueLabels());
                        d.addVariableWithValue(temp, doubleData, this.NUMERIC);
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

                for (VariableMu v : variables) {
                    String name = v.getSpssVariable().getName();
                    StatsUtil.submit("if (SYSMIS(" + name + ") EQ 0) " + name + "= TEMP" + name + ".");
                }
                StatsUtil.submit("SAVE OUTFILE='" + this.safeSpssFile + "'/DROP=TEMP" + first + " TO TEMP" + last + ".");
                StatsUtil.stop();
            } catch (FileNotFoundException ex) {
                //Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (StatsException ex) {
            //Logger.getLogger(CalculationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //TODO: remove??
    /**
     * Asks for the directory where spss is installed.
     *
     * @param parent the Frame of the mainFrame.
     */
    private void getSpssInstallationDirectory(Frame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Set IBM SPSS directory");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.showOpenDialog(parent);
        System.out.println(fileChooser.getSelectedFile().getPath());
    }

}
