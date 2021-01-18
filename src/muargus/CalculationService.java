/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import muargus.controller.AnonDataController;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.extern.dataengine.IProgressListener;
import muargus.model.AnonDataSpec;
import muargus.model.CodeInfo;
import muargus.model.Combinations;
import muargus.model.MetadataMu;
import muargus.model.PramVariableSpec;
import muargus.model.ProtectedFile;
import muargus.model.RecodeMu;
import muargus.model.ReplacementSpec;
import muargus.model.RiskModelClass;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.ViewRerrorView;
import org.apache.commons.lang3.StringUtils;

/**
 * Class for calculations. This class calls an external .dll for its
 * calculations.
 *
 * @author Statistics Netherlands
 */
public class CalculationService {

    /**
     * Progress listener.
     */
    private class ProgressListener extends IProgressListener {

        private final CalculationService service;

        public ProgressListener(CalculationService service) {
            this.service = service;
        }

        @Override
        public void UpdateProgress(final int percentage) {
            this.service.firePropertyChange("progress", null, percentage);
        }

    }

    /**
     * Enumeration of background tasks.
     */
    private enum BackgroundTask {
        ExploreFile,
        CalculateTables,
        MakeProtectedFile,
        MakeReplacementFile
    }

    private final CMuArgCtrl c;
    private MetadataMu metadata;
    private final ProgressListener progressListener; //must remain in scope, or will be garbage collected 
    private PropertyChangeListener listener;

    /**
     * Constructor for the CalculationService.
     *
     * @param muArgCtrl CMuArgCtrl. CMuArgCtrl is the wrapper class around the
     * external .dll.
     */
    public CalculationService(final CMuArgCtrl muArgCtrl) {
        this.c = muArgCtrl;
        this.progressListener = new ProgressListener(this);
        this.c.SetProgressListener(this.progressListener);
        this.metadata = null;
    }

    /**
     * Makes a protected file.
     *
     * @param listener PropertyChangeListener.
     */
    public void makeProtectedFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.MakeProtectedFile, listener);
    }

    /**
     * Gets the index (1-based) of the variable in the dll.
     *
     * @param variable VariableMu instance.
     * @return Integer containing the index (1-based) of the variable in the
     * dll.
     */
    private int getIndexOf(VariableMu variable) {
        return getVariables().indexOf(variable) + 1;
    }

    /**
     * Gets the index (1-based) of the table in the dll.
     *
     * @param table TableMu instance.
     * @return Integer containing the index (1-based) of the variable in the
     * dll.
     */
    private int getIndexOf(TableMu table) {
        return getTables().indexOf(table) + 1;
    }

    /**
     * Sets the metadata.
     *
     * @param metadata MetadataMu instance containing the metadata.
     */
    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        this.c.CleanAll();
    }

    /**
     * Makes a replacement file.
     *
     * @param listener PropertyChangeListener.
     */
    public void makeReplacementFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.MakeReplacementFile, listener);
    }

    /**
     * Makes a replacement file in the background (in a separate thread).
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while creating temporary replacement file.
     */
    private void makeReplacementFileInBackground() throws ArgusException {
        ReplacementSpec replacement = this.metadata.getReplacementSpecs().get(this.metadata.getReplacementSpecs().size() - 1);
        int[] errorCode = new int[1];
        boolean result = this.c.WriteVariablesInFile(
                this.metadata.getFileNames().getDataFileName(),
                replacement.getReplacementFile().getInputFilePath(),
                replacement.getInputVariables().size(),
                getVarIndicesInFile(replacement.getInputVariables()),
                MuARGUS.getDefaultSeparator(),
                errorCode);
        if (!result) {
            throw new ArgusException("Error creating temporary replacement file: " + getErrorString(errorCode[0]));
        }
    }

    /**
     * Gets the error message as a String.
     *
     * @param errorType Integer containing the error type.
     * @return String containing the error message.
     */
    private String getErrorString(int errorType) {
        String[] error = new String[1];
        this.c.GetErrorString(errorType, error);
        return error[0];
    }

    /**
     * Does a global recoding. Reduces the number of codes of a variable by
     * grouping several codes together.
     *
     * @param recode RecodeMu instance containing the variable specifications
     * for global recoding.
     * @return String containing a warning of overlapping sources, not mentioned
     * codes, etc.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while recoding.
     */
    public String doRecode(RecodeMu recode) throws ArgusException {
        int index = getIndexOf(recode.getVariable());
        int[] errorType = new int[]{0};
        int[] errorLine = new int[]{0};
        int[] errorPos = new int[]{0};
        String[] warning = new String[1];
        boolean result = this.c.DoRecode(index,
                recode.getGrcText(),
                recode.getMissing_1_new(),
                recode.getMissing_2_new(),
                errorType,
                errorLine,
                errorPos,
                warning);
        if (!result) {
            throw new ArgusException(String.format("Error in recoding: %s\nline %d, position %d \nNo recoding done",
                    getErrorString(errorType[0]), errorLine[0], errorPos[0]));
        }
        return warning[0];
    }

    /**
     * Gets the unsafe combinations for each table and each dimension.
     *
     * @param dimensions Integer containing the number of dimensions.
     * @return Arraylist of TableMu's containing the number of unsafe
     * combinations for each dimension.
     */
    public ArrayList<TableMu> getTableUnsafeCombinations(int dimensions) {
        int index = 1;
        boolean[] baseTable = new boolean[1];
        int[] nUC = new int[1];
        int[] varList = new int[MuARGUS.MAXDIMS];
        ArrayList<TableMu> tables = new ArrayList<>();
        while (true) {
            boolean result = this.c.GetTableUC(dimensions, index, baseTable, nUC, varList);
            if (!result) {
                return tables;
            }
            TableMu table = new TableMu();
            table.setNrOfUnsafeCombinations(nUC[0]);
            ArrayList<VariableMu> variables = getVariables();
            for (int varIndex = 0; varIndex < dimensions; varIndex++) {
                table.getVariables().add(variables.get(varList[varIndex] - 1));
            }
            tables.add(table);
            index++;
        }
    }

    /**
     * Applies the global recoding.
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while applying global recoding.
     */
    public void applyRecode() throws ArgusException {
        boolean result = this.c.ApplyRecode();
        if (!result) {
            throw new ArgusException("Error during Apply recode");
        }
    }

    /**
     * Undo's the global recoding.
     *
     * @param recode RecodeMu instance containing the variable specifications
     * for global recoding.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while undoing global recoding.
     */
    public void undoRecode(RecodeMu recode) throws ArgusException {
        int index = getIndexOf(recode.getVariable());
        boolean result = this.c.UndoRecode(index);
        if (!result) {
            throw new ArgusException("Error while undoing recode");
        }
        applyRecode();
    }

    /**
     * Truncates the codes. This reduces the number of codes of a variable by
     * cutting of one r more positions at the right.
     *
     * @param recode RecodeMu instance containing the variable specifications
     * for global recoding.
     * @param positions Integer containing the number of positions that will be
     * cut of.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during truncate.
     */
    public void truncate(RecodeMu recode, int positions) throws ArgusException {
        int index = getIndexOf(recode.getVariable());
        boolean result = this.c.DoTruncate(index, positions);
        if (!result) {
            throw new ArgusException("Error during Truncate");
        }
        recode.setTruncated(true);
        applyRecode();
    }

    /**
     * Changes the files. Numeric variables will be replaced by the variables in
     * the replacement files.
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during SetChangeFile for replacement file.
     */
    private void doChangeFiles() throws ArgusException {
        boolean result = this.c.SetNumberOfChangeFiles(this.metadata.getReplacementSpecs().size());
        if (!result) {
            throw new ArgusException("Error during SetNumberOfChangeFiles");
        }
        int index = 0;
        for (ReplacementSpec replacement : this.metadata.getReplacementSpecs()) {
            index++;
            result = this.c.SetChangeFile(index,
                    replacement.getReplacementFile().getOutputFilePath(),
                    replacement.getOutputVariables().size(),
                    getVarIndicesInFile(replacement.getOutputVariables()),
                    MuARGUS.getDefaultSeparator());
            if (!result) {
                throw new ArgusException(String.format("Error during SetChangeFile for replacement file %d", index));
            }
        }
    }

    /**
     * Makes the protected/safe file using "traditional suppression".
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during Make protected file.
     */
    private void makeFileInBackground() throws ArgusException {
        doChangeFiles();
        ProtectedFile protectedFile = this.metadata.getCombinations().getProtectedFile();

        this.c.SetOutFileInfo(this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FIXED
                || this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS,
                this.metadata.getSeparator(),
                "", // First line
                false // Strings not in quotes
                //true // Strings in quotes
        );
        int index = 0;
        for (VariableMu variable : this.metadata.getVariables()) {
            index++;
            if (protectedFile.getVariables().contains(variable)) {
                this.c.SetSuppressPrior(index, variable.getSuppressPriority());
            }
        }
        try {
            String dataFileName;
            if (this.metadata.isSpss()) {
                File saf = File.createTempFile("MuArgus", ".saf");
                saf.deleteOnExit();
                dataFileName = saf.getPath();
                MuARGUS.getSpssUtils().safFile = saf;
            } else {
                dataFileName = protectedFile.getSafeMeta().getFileNames().getDataFileName();
            }
            
            boolean result = this.c.MakeFileSafe(dataFileName,
                    protectedFile.isWithPrior(),
                    protectedFile.isWithEntropy(),
                    protectedFile.getHouseholdType(),
                    protectedFile.isRandomizeOutput(),
                    protectedFile.isPrintBHR());
                    
            if (!result) {
                throw new ArgusException("Error during Make protected file");
            }
        } catch (IOException e) {

        }
    }

    /**
     * Makes the protected/safe file using "(k+1)-anonymisation".
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during Make protected file.
     */
    private void makeFileInBackgroundKAnon() throws ArgusException {
        // First save ascii file with microdata with recoding applied
        // Do not apply numeric changes, will be done in last step
        // Save only variables that are needed to apply (k+1)-anonymisation
        // Read that into R and apply (k+1)-anonymisation with sdcMicro and save result
        // Combine result with original microdata and apply numeric changes
        ViewRerrorView ErrorText;
        
        AnonDataController controller = new AnonDataController(this.metadata);        
        firePropertyChange("stepName", null, "Writing temp file...");
        // anonData will contain
        // Variables, Combinations, RStrings, Thresholds, dataFile, rScriptFile, runRFile
        AnonDataSpec anonData = controller.setAnonData();
        
        // Save file with recodings, no suppressions
        int[] errorCode = new int[1];
        boolean result = this.c.MakeAnonFile(anonData.getDataFile().getAbsolutePath(),
                                anonData.getKAnonVariables().size(),
                                getVarIndicesInFile(anonData.getKAnonVariables()),
                                MuARGUS.getDefaultSeparator(), errorCode);
        if (!result) {
            firePropertyChange("stepName", null, "");
            throw new ArgusException("Error creating temporary data file: " + getErrorString(errorCode[0]));
        }
        
        // Run sdcMicro R-code to make .rpl-file with (k+1)-anonymised key-variables
        firePropertyChange("stepName", null, "Running R-code...");
        firePropertyChange("progress", null, 83);
        int RrunResult = controller.runAnonData();
        if (RrunResult == controller.RINSTALL_ERROR){
            firePropertyChange("stepName", null, "");
            throw new ArgusException("R not correctly installed?");
        }
      
        ErrorText = new ViewRerrorView(null, true);
        if (RrunResult != 0){ // R returned with exit code != 0, i.e., error
            ErrorText.addTextFile(anonData.getRoutFile().getAbsolutePath());
            ErrorText.setVisible(true);
            firePropertyChange("stepName", null, "");
            firePropertyChange("progress", null, 0);
            throw new ArgusException("No safe file produced: error running Rscript for (k+1)-anonymisation.");
        } else{
            // Run "normal"  makeFileSafe, with result from R as ReplacementFile (.rpl)
            // Do it without Additional Suppressions!!!!
            firePropertyChange("stepName", null, "Writing safe file...");  
            controller.setNumberSuppAnonData();
            combineFileInBackground(anonData);
        }
    }
    
/**
     * Makes the protected/safe file using output from (k+1)-anonymisation
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     */
    private void combineFileInBackground(AnonDataSpec anonData) throws ArgusException {
        doChangeFiles();
        ProtectedFile protectedFile = this.metadata.getCombinations().getProtectedFile();

        this.c.SetOutFileInfo(this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FIXED
                || this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS,
                this.metadata.getSeparator(),
                "",
                true
        );
        int index = 0;
        for (VariableMu variable : this.metadata.getVariables()) {
            index++;
            if (protectedFile.getVariables().contains(variable)) {
                this.c.SetSuppressPrior(index, variable.getSuppressPriority());
            }
        }
        try {
            String dataFileName;
            if (this.metadata.isSpss()) {
                File saf = File.createTempFile("MuArgus", ".saf");
                saf.deleteOnExit();
                dataFileName = saf.getPath();
                MuARGUS.getSpssUtils().safFile = saf;
            } else {
                dataFileName = protectedFile.getSafeMeta().getFileNames().getDataFileName();
            }
            
            int[] nSupps = new int[this.metadata.getVariables().size()];
            for (int i = 0; i < nSupps.length; i++) {
                nSupps[i] = 0;
            }
            for (int i = 0; i < anonData.getKAnonVariables().size(); i++){
                nSupps[getIndexOf(anonData.getKAnonVariables().get(i)) - 1] = anonData.getKAnonVariables().get(i).getnOfSuppressions();
            }
            
            boolean result = this.c.CombineToSafeFile(dataFileName,
                    nSupps,
                    protectedFile.isWithPrior(),
                    protectedFile.isWithEntropy(),
                    protectedFile.getHouseholdType(),
                    protectedFile.isRandomizeOutput(),
                    protectedFile.isPrintBHR());
                    
            if (!result) {
                throw new ArgusException("Error during Make protected file");
            }
        } catch (IOException e) {

        }
    }
    
    /**
     * Calculates the tables. For each (sub) table is calculated how many table
     * cells are greater than 0 and less than or equal to the threshold.
     *
     * @param listener PropertyChangeListener.
     */
    public void calculateTables(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.CalculateTables, listener);
    }

    /**
     * Checks if a background worker has successfully finished it's job.
     *
     * @param ex Exception that might be thrown if an error occurs.
     */
    private void workerDone(Exception ex) {
        if (ex == null) {
            this.firePropertyChange("result", null, "success");
        } else {
            this.firePropertyChange("error", null, ex.getCause());
            this.firePropertyChange("result", null, "error");
        }
    }

    /**
     * Fires a property change event.
     *
     * @param propertyName String containing the property name.
     * @param oldValue Object containing the old object before the property
     * change.
     * @param newValue Object containing the new object after the property
     * change.
     */
    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (this.listener != null) {
            this.listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    /**
     * Adds a variable.
     *
     * @param variable VariableMu instance of the to be added variable.
     * @param varNr Integer containing the index of the variable.
     * @return Boolean indicating whether the adding was successful.
     */
    private boolean addVariable(VariableMu variable, int varNr) {
        //For numeric non-weight variables, the dll needs a missing value, but it's not required in the model
        //So a default (dummy) value is passed in this case
        String missing0 = variable.getMissing(0);
        if (variable.isNumeric() && !variable.isWeight() && "".equals(missing0)) {
            missing0 = StringUtils.repeat("X", variable.getVariableLength());
        } else {
            missing0 += StringUtils.repeat(" ", variable.getVariableLength() - missing0.length());//Make the right length
        }

        return this.c.SetVariable(varNr,
                variable.getStartingPosition(),
                variable.getVariableLength(),
                variable.getDecimals(),
                missing0,
                variable.getMissing(1),
                variable.isHouse_id(),
                variable.isHousehold(),
                variable.isCategorical(),
                variable.isNumeric(),
                variable.isWeight(),
                getRelatedVariableIndex(variable));
    }

    /**
     * Gets an ArrayList containing all the Variables in the Metadata.
     *
     * @return ArrayList containing all the Variables in the Metadata.
     */
    public ArrayList<VariableMu> getVariables() {
        return this.metadata.getVariables();
    }

    /**
     * Gets all the specified tables.
     *
     * @return ArrayList containing instances of the TableMu class. Each table
     * is specified by an TableMu instance.
     */
    public ArrayList<TableMu> getTables() {
        return this.metadata.getCombinations().getTables();
    }

    /**
     * Gets the index of the weight variable.
     *
     * @param variables ArrayList of VariableMu's
     * @return Integer containing the index of the weight variable.
     */
    private int getRiskVarIndex(ArrayList<VariableMu> variables) {
        int index = 0;
        for (VariableMu variable : variables) {
            index++;
            if (variable.isWeight()) {
                return index;
            }
        }
        return 0;
    }

    /**
     * Executes calculations in the background (in a separate thread).
     *
     * @param taskType Background task.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while executing a task in the background.
     */
    private void executeInBackground(BackgroundTask taskType) throws ArgusException {
        firePropertyChange("stepName", null, taskType.toString());
        switch (taskType) {
            case CalculateTables:
                calculateInBackground();
                break;
            case ExploreFile:
                exploreInBackground();
                break;
            case MakeProtectedFile:
                // If not (k+1)-anonymisation or no suppression, use "old" proceudre
                if (!this.metadata.getCombinations().getProtectedFile().isKAnon() ||
                        (!this.metadata.getCombinations().getProtectedFile().isWithEntropy() && 
                            !this.metadata.getCombinations().getProtectedFile().isWithPrior())){
                    makeFileInBackground();
                } else { // Use k-anonymity precedure
                    makeFileInBackgroundKAnon();
                }
                break;
            case MakeReplacementFile:
                makeReplacementFileInBackground();
                break;
        }
    }

    /**
     * Explores the data file in the background.
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during a background task.
     */
    private void exploreInBackground() throws ArgusException {
        ArrayList<VariableMu> variables = getVariables();
        boolean result = this.c.SetNumberVar(variables.size());
        if (!result) {
            throw new ArgusException("Error in SetNumberVar: Insufficient memory");
        }

        int index = 0;
        for (VariableMu variable : variables) {
            index++;
            result = addVariable(variable, index);
            if (!result) {
                throw new ArgusException(String.format("Error in SetVariable: variable %s", variable.getName()));
            }
        }

        int[] errorCodes = new int[1];
        int[] lineNumbers = new int[1];
        int[] varIndexOut = new int[1];

        result = this.c.SetInFileInfo(this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FIXED
                || this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS,
                this.metadata.getSeparator(),
                this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FREE_WITH_META);
        if (!result) {
            throw new ArgusException("Error in SetInFileInfo");
        }
        result = this.c.ExploreFile(this.metadata.getFileNames().getDataFileName(),
                errorCodes,
                lineNumbers,
                varIndexOut);
        if (!result) {
            String var = (varIndexOut[0] > 0)
                    ? ", variable " + getVariables().get(varIndexOut[0] - 1).getName() : "";
            throw new ArgusException(String.format("Error in ExploreFile: %s\nLine %d%s",
                    getErrorString(errorCodes[0]), lineNumbers[0] + 1, var));
        }
        this.metadata.setRecordCount(this.c.NumberofRecords());
    }

    /**
     * Explores the data file in the background.
     *
     * @param listener PropertyChangeListener
     */
    public void exploreFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.ExploreFile, listener);
    }

    /**
     * Executes a swing worker. The swing worker executes a task in a separate
     * thread.
     *
     * @param taskType BackgroundTask containing the type of task to be executed
     * in the background.
     * @param listener PropertychangeListener.
     */
    private void executeSwingWorker(final BackgroundTask taskType, PropertyChangeListener listener) {
        this.listener = listener;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            // called in a separate thread...
            @Override
            protected Void doInBackground() throws Exception {
                executeInBackground(taskType);
                return null;
            }

            // called on the GUI thread
            @Override
            public void done() {
                super.done();
                try {
                    get();
                    workerDone(null);
                } catch (InterruptedException ex) {
                    SystemUtils.writeLogbook("Background process interrupted: " + ex.getMessage());
                } catch (ExecutionException ex) {
                    workerDone(ex);
                }
            }
        };
        worker.addPropertyChangeListener(this.listener);
        worker.execute();

    }

    /**
     * Calculates the tables including the number of unsafe combinations in the
     * background.
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while computing the tables and calculating the unsafe combinations.
     */
    private void calculateInBackground() throws ArgusException {
        Combinations model = this.metadata.getCombinations();
        ArrayList<VariableMu> variables = getVariables();

        boolean result = this.c.SetNumberTab(model.getTables().size());
        if (!result) {
            throw new ArgusException("Error in SetNumberTab");
        }

        int riskVarIndex = getRiskVarIndex(variables);
        int index = 0;
        for (TableMu table : model.getTables()) {
            index++;
            result = this.c.SetTable(
                    index,
                    table.getThreshold(),
                    table.getVariables().size(),
                    getVarIndicesInTable(table),
                    table.isRiskModel(),
                    riskVarIndex);
            if (!result) {
                throw new ArgusException("Error in SetTable");
            }
        }
        firePropertyChange("progress", null, 0);

        int[] errorCodes = new int[1];
        int[] tableIndex = new int[1];
        result = this.c.ComputeTables(errorCodes, tableIndex);
        if (!result) {
            String table = (tableIndex[0] > 0)
                    ? "\nTable " + Integer.toString(tableIndex[0]) : "";
            throw new ArgusException(String.format("Error in ComputeTables: %s%s",
                    getErrorString(errorCodes[0]), table));
        }
    }

    /**
     * Gets the variable indices (1-based) in the file.
     *
     * @param variables ArrayList of VariableMu's for which the indices are
     * requested.
     * @return Array of integers containing the 1-based indices of the
     * variables.
     */
    private int[] getVarIndicesInFile(ArrayList<VariableMu> variables) {
        int[] indices = new int[variables.size()];
        for (int index = 0; index < indices.length; index++) {
            indices[index] = getIndexOf(variables.get(index));
        }
        return indices;
    }

    /**
     * Gets the variable indices (1-based) in the tables.
     *
     * @param table ArrayList of TableMu's for which the indices of it's
     * variables are requested.
     * @return Array of integers containing the 1-based indices of the variables
     * in the tables.
     */
    private int[] getVarIndicesInTable(TableMu table) {
        int[] indices = new int[table.getVariables().size()];
        for (int index = 0; index < indices.length; index++) {
            indices[index] = getIndexOf(table.getVariables().get(index));
        }
        Arrays.sort(indices,0,indices.length); // MuArgusCtrl needs ordering from low to high
        return indices;
    }

    /**
     * Gets the variable index (1-based) of the related variable.
     *
     * @param variable VariableMu instance for which the index of it's related
     * variableMu is requested.
     * @return Integer containing the index of the related variable of the given
     * variable.
     */
    private int getRelatedVariableIndex(VariableMu variable) {
        if (variable.getRelatedVariable() == null) {
            return 0;
        }
        return getIndexOf(variable.getRelatedVariable());

    }

    /**
     * Sets the properties for the safe file for a given variable.
     *
     * @param index Integer containing the index of the variable (1-based).
     * @param variable VariableMu instance of the given variable.
     * @param metadata MetadataMu containing the metadata.
     * @param delta Integer containing the difference in starting position
     * compared to the original data file.
     * @return Integer containing the delta value.
     */
    private int setSafeFileProperties(final int index, final VariableMu variable, MetadataMu metadata, int delta) {
        if (index == 0) {
            //Variable was not in dll
            variable.setStartingPosition(variable.getStartingPosition() + delta);
        } else {
            int[] startPos = new int[1];
            int[] nPos = new int[1];
            int[] nSuppressions = new int[1];
            double[] entropy = new double[1];
            int[] bandwidth = new int[1];
            String[] missing1 = new String[1];
            String[] missing2 = new String[1];
            int[] nOfCodes = new int[1];
            int[] nOfMissing = new int[1];
            this.c.GetVarProperties(index, startPos, nPos, nSuppressions, entropy, bandwidth, missing1, missing2, nOfCodes, nOfMissing);
            variable.setStartingPosition(startPos[0]);
            delta += nPos[0] - variable.getVariableLength();
            variable.setVariableLength(nPos[0]);
            variable.setnOfSuppressions(nSuppressions[0]);
            variable.setEntropy(entropy[0]);
            variable.setBandwidth(bandwidth[0]);
            variable.setnOfCodes(nOfCodes[0]);
            if (nOfMissing[0] > 0) {
                variable.setMissing(0, missing1[0]);
            }
            if (nOfMissing[0] > 1) {
                variable.setMissing(1, missing2[0]);
            }

            //Global recode codelist
            if (metadata.getCombinations().getGlobalRecode() != null) {
                RecodeMu recode = metadata.getCombinations().getGlobalRecode().getRecodeByVariableName(variable.getName());
                if (recode != null && recode.getAppliedCodeListFile().length() > 0) {
                    variable.setCodeListFile(recode.getAppliedCodeListFile());
                    variable.setCodelist(variable.getCodeListFile() != null);
                }
            }
        }
        return delta;
    }

    /**
     * Sets for a given numerical variable the code to be used to replace all
     * values below a certain value. E.g., all values below 100,000 receive the
     * text "<=100,000"
     *
     * @param variable VariableMu instance
     * @param top Boolean indicating whether top or bottom coding should be
     * applied.
     * @param value values <= BottomLevel or >= TopLevel are to be replaced
     * depending whether top = true.
     * @param replacement String containing the code used for replacement
     * @param undo Boolean indicating whether the bottom/top coding should be
     * undone.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while doing top/bottom coding. This occurs when the variable index is
     * wrong.
     */
    public void setTopBottomCoding(VariableMu variable, boolean top, double value, String replacement, boolean undo) throws ArgusException {
        boolean result;
        if (top) {
            result = this.c.SetCodingTop(
                    getIndexOf(variable),
                    value,
                    replacement,
                    undo);
        } else {
            result = this.c.SetCodingBottom(
                    getIndexOf(variable),
                    value,
                    replacement,
                    undo);
        }
        if (!result) {
            throw new ArgusException("Error in Set Top/Bottom coding");
        }
    }

    /**
     * Sets for a numerical variable the rounding base. Rounding base can
     * contain decimals, e.g. "2.5". In that case give as rounding base 2.5 and
     * nDec=1.
     *
     * @param variable VariableMu instance
     * @param base Double containing the rounding base
     * @param nDecimals Integer containing the number of decimals in rounding
     * base
     * @param undo Boolean indicating whether the rounding should be undone.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while rounding. This occurs when the variable index is wrong.
     */
    public void setRounding(VariableMu variable, Double base, int nDecimals, boolean undo) throws ArgusException {
        boolean result = this.c.SetRound(getIndexOf(variable),
                base,
                nDecimals,
                undo);
        if (!result) {
            throw new ArgusException("Error in Set Rounding");
        }
    }

    /**
     * Sets the weight noise. The weight noise is the percentage to decrease or
     * increase the value of a numeric variable. Variable will be changed by
     * percentage, randomly selected from the interval (100 - WeightNoise, 100 +
     * WeightNoise)
     *
     * @param variable VariableMu instance
     * @param noise Double containing the percentage by which the value is
     * reduced/increased
     * @param undo Boolean indicating whether the setWeightNoise should be
     * undone.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while setting the weight noise. This occurs when the variable index is
     * wrong, e.g. WeightNoise <= 0, WeighthNoise > 100.
     */
    public void setWeightNoise(VariableMu variable, double noise, boolean undo) throws ArgusException {
        boolean result = this.c.SetWeightNoise(getIndexOf(variable),
                noise,
                undo);
        if (!result) {
            throw new ArgusException("Error in Set Weight noise");
        }
    }

    /**
     * Fills the safe metadata.
     */
    public void fillSafeFileMetadata() {
        MetadataMu safeMeta = this.metadata.getCombinations().getProtectedFile().getSafeMeta();
        int delta = 0;
        for (VariableMu var : safeMeta.getVariables()) {
            int varIndex = getIndexOf(var);
            delta = setSafeFileProperties(varIndex, var, this.metadata, delta);
        }
        safeMeta.setRecordCount(this.c.NumberofRecords());
    }

    /**
     * Gets the variable info.
     *
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while retrieving the variable info. This can occur in the .dll for the
     * functions UnsafeVariable, UnsafeVariablePrepare, UnsafeVariablesCodes and
     * UnsafeVariableClose.
     */
    public void getVariableInfo() throws ArgusException {
        Combinations model = this.metadata.getCombinations();
        model.getUnsafeCombinations().clear();
        for (int varIndex = 0; varIndex < getVariables().size(); varIndex++) {
            VariableMu variable = getVariables().get(varIndex);
            variable.getCodeInfos().clear();
            if (variable.isCategorical()) {
                if (model.getVariablesInTables().contains(variable)) {
                    //If the variable is in one of the specified tables, get the unsafe combinations 
                    int[] nDims = new int[1];
                    int[] unsafeCount = new int[model.getMaxDimsInTables()];
                    boolean result = this.c.UnsafeVariable(varIndex + 1, nDims, unsafeCount);
                    if (!result) {
                        throw new ArgusException("Error in UnsafeVariable");
                    }
                    model.setUnsafeCombinations(variable, unsafeCount, nDims[0]);

                    int[] nCodes = new int[]{0};
                    result = this.c.UnsafeVariablePrepare(varIndex + 1, nCodes);
                    if (!result) {
                        throw new ArgusException("Error in UnsafeVariablePrepare");
                    }

                    int[] isMissing = new int[1];
                    int[] freq = new int[1];
                    String[] code = new String[1];
                    for (int codeIndex = 0; codeIndex < nCodes[0]; codeIndex++) {
                        result = this.c.UnsafeVariableCodes(varIndex + 1,
                                codeIndex + 1,
                                isMissing,
                                freq,
                                code,
                                nDims,
                                unsafeCount);
                        if (!result) {
                            throw new ArgusException("Error in UnsafeVariableCodes");
                        }
                        CodeInfo codeInfo = new CodeInfo(code[0], isMissing[0] != 0);
                        codeInfo.setFrequency(freq[0]);
                        codeInfo.setUnsafeCombinations(nDims[0], unsafeCount);
                        variable.getCodeInfos().add(codeInfo);
                    }
                    result = this.c.UnsafeVariableClose(varIndex + 1);
                    if (!result) {
                        throw new ArgusException("Error in UnsafeVariableClose");
                    }
                } else {
                    //If the variable is not in the specified tables, just get the codelist
                    int codeIndex = 0;
                    while (true) {
                        codeIndex++;
                        String[] code = new String[1];
                        int[] pramPerc = new int[1];
                        boolean result = this.c.GetVarCode(varIndex + 1, codeIndex, code, pramPerc);
                        if (!result) {
                            break;
                        }
                        variable.getCodeInfos().add(new CodeInfo(code[0], false));
                    }
                    //Add the missings
                    for (int i = 0; i < 2; i++) {
                        if (!"".equals(variable.getMissing(i))) {
                            CodeInfo codeInfo = new CodeInfo(variable.getMissing(i), true);
                            variable.getCodeInfos().add(codeInfo);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets PRAM variables and values.
     *
     * @param pramVariable PramVariableSpec instance containing the the variable
     * specifications for PRAM.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while applying PRAM. This can occur in the .dll for the functions
     * SetPramVar, SetPramValue and ClosePramVar.
     */
    public void setPramVariable(PramVariableSpec pramVariable) throws ArgusException {
        int varIndex = getIndexOf(pramVariable.getVariable());
        int bandWidth = pramVariable.useBandwidth() ? pramVariable.getBandwidth() : -1;
        boolean result = this.c.SetPramVar(varIndex, bandWidth, false);
        if (!result) {
            throw new ArgusException("Error during SetPramVar");
        }
        for (int codeIndex = 0; codeIndex < pramVariable.getVariable().getCodeInfos().size(); codeIndex++) {
            CodeInfo codeInfo = pramVariable.getVariable().getCodeInfos().get(codeIndex);
            if (!codeInfo.isMissing()) {
                result = this.c.SetPramValue(codeIndex + 1, codeInfo.getPramProbability());
                if (!result) {
                    throw new ArgusException("Error during SetPramValue");
                }
            }
        }
        result = this.c.ClosePramVar(varIndex);
        if (!result) {
            throw new ArgusException("Error during ClosePramVar");
        }
    }

    /**
     * Undo's PRAM. Resets the PRAM'ed variable to the pre-PRAM'ed values.
     *
     * @param pramVariable PramVariableSpec instance containing the the variable
     * specifications for PRAM.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while undoing PRAM.
     */
    public void undoSetPramVariable(PramVariableSpec pramVariable) throws ArgusException {
        int varIndex = getIndexOf(pramVariable.getVariable());
        boolean result = this.c.SetPramVar(varIndex, -1, true);
        if (!result) {
            throw new ArgusException("Error during SetPramVar");
        }
    }

    /**
     * Gets the minimum and maximum value of a numeric variable.
     *
     * @param variable VariableMu instance of a numeric variable.
     * @return Array of doubles containing the minimum and maximum value. The
     * minimum value has the index 0 and the maximum value the index 1.
     */
    public double[] getMinMax(VariableMu variable) {
        int varIndex = getIndexOf(variable);
        double[] min = new double[1];
        double[] max = new double[1];
        this.c.GetMinMaxValue(varIndex, min, max);
        double[] min_max = {min[0], max[0]};
        return min_max;
    }

    /**
     * Calculates the number of unsafe records for the risk model.
     *
     * @param table TableMu instance for which the risk model is set.
     * @param riskThreshold Double containing the risk threshold.
     * @param household Boolean indicating whether the data are household data.
     * @return Integer containing the number of unsafe records.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while calculating the number of unsafe records.
     */
    public int calculateUnsafe(TableMu table, double riskThreshold, boolean household) throws ArgusException {
        int tableIndex = getIndexOf(table);
        int[] nUnsafe = new int[1];
        int[] dummy = new int[1];
        boolean result = household
                ? this.c.SetBHRThreshold(tableIndex, Math.log(riskThreshold), nUnsafe, dummy)
                : this.c.SetBirThreshold(tableIndex, Math.log(riskThreshold), nUnsafe);
        if (!result) {
            throw new ArgusException("Error calculating number of unsafe records");
        }
        return nUnsafe[0];
    }

    /**
     * Calculate the re-identification rate.
     *
     * @param table TableMu instance for which the risk model is set.
     * @param riskThreshold Double containing the risk threshold.
     * @return Double containing the re-identification rate.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while calculating the re-identification rate.
     */
    public double calculateReidentRate(TableMu table, double riskThreshold) throws ArgusException {
        int tableIndex = getIndexOf(table);
        double[] reidentRate = new double[1];
        if (!this.c.ComputeBIRRateThreshold(tableIndex, riskThreshold, reidentRate)) {
            throw new ArgusException("Error calculating reident rate");
        }
        return reidentRate[0];
    }

    /**
     * Calculates the risk threshold.
     *
     * @param table TableMu instance for which the risk model is set.
     * @param nUnsafe Integer containing the number of unsafe records.
     * @param household Boolean indicating whether the data are household data.
     * @return Double containing the risk threshold.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while calculating the risk threshold.
     */
    public double calculateRiskThreshold(TableMu table, int nUnsafe, boolean household) throws ArgusException {
        int tableIndex = getIndexOf(table);
        double[] riskThreshold = new double[1];
        int[] errorCode = new int[1];
        boolean result = household
                ? this.c.CalculateBHRFreq(tableIndex, true, this.c.NumberofRecords() - nUnsafe, this.c.NumberofRecords() - nUnsafe, riskThreshold, errorCode)
                : this.c.CalculateBIRFreq(tableIndex, this.c.NumberofRecords() - nUnsafe, riskThreshold, errorCode);
        if (!result) {
            throw new ArgusException("Error calculating frequency");
        }
        return riskThreshold[0];
    }

    /**
     * Fills the risk model classes with the histogram data.
     *
     * @param table TableMu instance for which the risk model is set.
     * @param classes ArrayList of RiskModelClasses. A RiskModelClass contains
     * information on a single pillar of the risk model histogram.
     * @param cumulative Boolean indicating whether the histogram is a
     * cumulative histogram.
     * @return Double containing the re-identification rate (ksi).
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while filling the risk model classes with the histogram data.
     */
    public double fillHistogramData(TableMu table, ArrayList<RiskModelClass> classes, boolean cumulative) throws ArgusException {
        classes.clear();
        double[] ksi = new double[1];

        int tableIndex = getIndexOf(table);
        int nClasses = MuARGUS.getNHistogramClasses(cumulative);
        double[] classLeftValue = new double[nClasses + 1];
        int[] frequency = new int[nClasses + 1];
        int[] hhFrequency = new int[nClasses + 1];
        if (this.metadata.isHouseholdData()) {
            int[] errorCode = new int[1];
            boolean result = this.c.CalculateBaseHouseholdRisk(errorCode);
            if (!result) {
                throw new ArgusException("Error calculating base household risk");
            }
            this.c.GetBHRHistogramData(tableIndex, nClasses, classLeftValue, hhFrequency, frequency);
        } else {
            this.c.GetBIRHistogramData(tableIndex, nClasses, classLeftValue, ksi, frequency);
        }
        int sumFreq = 0;
        int sumHHfreq = 0;
        for (int classIndex = 0; classIndex < nClasses; classIndex++) {
            RiskModelClass rmc = new RiskModelClass(Math.exp(classLeftValue[classIndex]),
                    Math.exp(classLeftValue[classIndex + 1]),
                    sumFreq + frequency[classIndex],
                    sumHHfreq + hhFrequency[classIndex]);
            classes.add(rmc);
            if (cumulative) {
                sumFreq += frequency[classIndex];
                sumHHfreq += hhFrequency[classIndex];
            }
        }
        return ksi[0];
    }

    public void removeKAnonReplacementIfAny(MetadataMu metadata){
        ArrayList<ReplacementSpec> r = metadata.getReplacementSpecs();
        for (int i = r.size() - 1; i >= 0; i--) {
            if (r.get(i) instanceof AnonDataSpec) {
                r.remove(i);
            }
        }
    }
    
}