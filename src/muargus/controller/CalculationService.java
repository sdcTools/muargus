/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import muargus.MuARGUS;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.extern.dataengine.IProgressListener;
import muargus.model.ProtectedFile;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.RecodeMu;
import muargus.model.TableMu;
import muargus.model.CodeInfo;
import muargus.model.PramVariableSpec;
import muargus.model.RiskModelClass;
//import muargus.model.UnsafeInfo;
import muargus.model.VariableMu;

/**
 *
 * @author pibd05
 */
public class CalculationService {

    private enum BackgroundTask {
        ExploreFile,
        CalculateTables,
        MakeProtectedFile,
    }
    
    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());
    private final CMuArgCtrl c;
    private MetadataMu metadata;

    public CalculationService(final CMuArgCtrl muArgCtrl) {
        c = muArgCtrl;
        this.metadata = null;
    }

    private PropertyChangeListener listener;

    public void makeProtectedFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.MakeProtectedFile, listener);
    }

    private int getIndexOf(VariableMu variable) {
        //Get the index (1-based) of the variable in the dll
        return getVariables().indexOf(variable) + 1;
    }

    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        c.CleanAll();
    }
    
    public String doRecode(RecodeMu recode) throws ArgusException {
        int index = getIndexOf(recode.getVariable());
        int[] errorType = new int[]{0};
        int[] errorLine = new int[]{0};
        int[] errorPos = new int[]{0};
        String[] warning = new String[1];
        boolean result = c.DoRecode(index,
                recode.getGrcText(),
                recode.getMissing_1_new(),
                recode.getMissing_2_new(),
                errorType,
                errorLine,
                errorPos,
                warning);
        if (!result) {
            throw new ArgusException(String.format("Error in recoding; line %d, position %d \nNo recoding done",
                    errorLine[0], errorPos[0]));
        }
        return warning[0];
    }
    
    public ArrayList<TableMu> getTableUnsafeCombinations(int dimensions) {
        int index=1;
        boolean[] baseTable = new boolean[1];
        int[] nUC = new int[1];
        int[] varList = new int[MuARGUS.MAXDIMS];
        ArrayList<TableMu> tables = new ArrayList<>();
        while (true) {
            boolean result = c.GetTableUC(dimensions, index, baseTable, nUC, varList);
            if (!result) {
                return tables;
            }
            TableMu table = new TableMu();
            table.setNrOfUnsafeCombinations(nUC[0]);
            ArrayList<VariableMu> variables = getVariables();
            for (int varIndex=0; varIndex < dimensions; varIndex++) {
                table.addVariable(variables.get(varList[varIndex]-1));
            }
            tables.add(table);
            index++;
        }
    }
    
    public void applyRecode() throws ArgusException {
        c.SetProgressListener(null);
        boolean result = c.ApplyRecode();
        if (!result) {
            throw new ArgusException("Error during Apply recode");
        }
        //return getUnsafeCombinations(metadata);
    }

    public void undoRecode(RecodeMu recode) throws ArgusException {
        c.SetProgressListener(null);
        int index = getIndexOf(recode.getVariable());
        boolean result = c.UndoRecode(index);
        if (!result) {
            throw new ArgusException("Error while undoing recode");
        }
        applyRecode();
    }
    
    public void truncate(RecodeMu recode, int positions) throws ArgusException {
        int index = getIndexOf(recode.getVariable());
        boolean result = c.DoTruncate(index, positions);
        if (!result) {
            throw new ArgusException("Error during Truncate");
        }
        recode.setTruncated(true);
        applyRecode();
    }

    private void makeFileInBackground() throws ArgusException {
        ProtectedFile protectedFile = metadata.getCombinations().getProtectedFile();
        IProgressListener progressListener = new IProgressListener() {
            @Override
            public void UpdateProgress(final int percentage) {
                firePropertyChange("progress", null, percentage);
                //propertyChanged(listener, "progress", null, percentage);
            }
        };
        c.SetProgressListener(progressListener);
        
        c.SetOutFileInfo(metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FIXED,
                metadata.getSeparator(),
                "",
                true
                );
        int index = 0;
        for (VariableMu variable : metadata.getVariables()) {
            index++;
            if (protectedFile.getVariables().contains(variable)) {
                c.SetSuppressPrior(index, variable.getSuppressweight());
            }
        }
        
        boolean result = c.MakeFileSafe(protectedFile.getSafeMeta().getFileNames().getDataFileName(), 
                protectedFile.isWithPrior(), 
                protectedFile.isWithEntropy(),
                protectedFile.getHouseholdType(), 
                protectedFile.isRandomizeOutput(), 
                protectedFile.isPrintBHR());

        if (!result) {
            throw new ArgusException("Error during Make protected file");
        }
    }

    public void calculateTables(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.CalculateTables, listener);
    }

    private void workerDone(Exception ex) {
        if (ex == null) {
            this.firePropertyChange("result", null, "success");
        }
        else {
            this.firePropertyChange("error", null, ex.getCause());
            this.firePropertyChange("result", null, "error");
        }
        //this.listener = null;
    }

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (this.listener != null) {
            this.listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    private PropertyChangeListener getWorkerPropertyChangeListener(final SwingWorker worker) {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                worker.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        };
    }

    private boolean addVariable(VariableMu variable, int varNr) {
        return c.SetVariable(varNr,
                variable.getStartingPosition(),
                variable.getVariableLength(),
                variable.getDecimals(),
                variable.getMissing(0),
                variable.getMissing(1),
                variable.isHouse_id(),
                variable.isHousehold(),
                variable.isCategorical(),
                variable.isNumeric(),
                variable.isWeight(),
                getRelatedVariableIndex(variable));
        //1 + metadata.getVariables().indexOf(variable.getRelatedVariable())); //TODO: handle error
    }

    private int getNVar(Combinations model, MetadataMu metadata) throws ArgusException {
        //For free format, all variables
        if (metadata.getDataFileType() != MetadataMu.DATA_FILE_TYPE_FIXED) {
            return metadata.getVariables().size();
        }

        int nVar = model.getVariablesInTables().size();
        if (model.isRiskModel()) {
            //Add weight when risk model is used.
            for (VariableMu weightVar : metadata.getVariables()) {
                if (weightVar.isWeight()) {
                    if (!model.getVariablesInTables().contains(weightVar)) {
                        nVar++;
                    }
                    return nVar;
                }
            }
            throw new ArgusException("Risk model but no weight variable");
        }
        return nVar;
    }

    public ArrayList<VariableMu> getVariables() {
        return metadata.getVariables();
//        if (metadata.getDataFileType() != MetadataMu.DATA_FILE_TYPE_FIXED) {
//            return metadata.getVariables();
//        }
//        ArrayList<VariableMu> variables = new ArrayList<>(metadata.getCombinations().getVariablesInTables());
//        if (metadata.getCombinations().isRiskModel()) {
//            for (VariableMu weightVar : metadata.getVariables()) {
//                if (weightVar.isWeight()) {
//                    if (!variables.contains(weightVar)) {
//                        variables.add(weightVar);
//                    }
//                    break;
//                }
//            }
//        }
//        return variables;
    }

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

    private void executeInBackground(BackgroundTask taskType) throws ArgusException {
        switch (taskType) {
            case CalculateTables:
                calculateInBackground();
                break;
            case ExploreFile:
                exploreInBackground();
                break;
            case MakeProtectedFile:
                makeFileInBackground();
                break;
        }
            
    }
    
    private void exploreInBackground() throws ArgusException {
        //TODO implement
    }
    
    public void exploreFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.ExploreFile, listener);
    }
    
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
                    logger.log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    workerDone(ex);
                }
            }
        };
       worker.addPropertyChangeListener(this.listener);
       worker.execute();
       
    }
    
    private void calculateInBackground() throws ArgusException {
        Combinations model = metadata.getCombinations();
        ArrayList<VariableMu> variables = getVariables(); 
        boolean result = c.SetNumberVar(variables.size());
        if (!result) {
            throw new ArgusException("Error in SetNumberVar: Insufficient memory");
        }

        int index = 0;
        for (VariableMu variable : variables) {
            index++;
            result = addVariable(variable, index);
            if (!result) {
                throw new ArgusException(String.format("Error in SetVariable: variable %d", index));
            }
        }

        int[] errorCodes = new int[1];
        int[] lineNumbers = new int[1];
        int[] varIndexOut = new int[1];

        IProgressListener progressListener = new IProgressListener() {
            @Override
            public void UpdateProgress(final int percentage) {
                firePropertyChange("progress", null, percentage);
                //propertyChanged(listener, "progress", null, percentage);
            }
        };
        c.SetProgressListener(progressListener);
        firePropertyChange("stepName", null, "ExploreFile");
        //propertyChanged(listener, "stepName", null, "ExploreFile");

        result = c.SetInFileInfo(metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FIXED,
                metadata.getSeparator(),
                metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FREE_WITH_META);
        if (!result) {
            throw new ArgusException("Error in SetInFileInfo");
        }

        result = c.ExploreFile(metadata.getFileNames().getDataFileName(),
                errorCodes,
                lineNumbers,
                varIndexOut);
        if (!result) {
            throw new ArgusException(String.format("Error in ExploreFile:\ncode %d, line %d, variable %s",
                errorCodes[0], lineNumbers[0]+1, getVariables().get(varIndexOut[0]).getName()));
        }
        metadata.setRecordCount(c.NumberofRecords());
        
        int x = model.getTables().size();
        result = c.SetNumberTab(x); //this.model.getTables().size());
        if (!result) {
            throw new ArgusException("Error in SetNumberTab");
        }

        int riskVarIndex = getRiskVarIndex(variables);
        index = 0;
        for (TableMu table : model.getTables()) {
            index++;
            result = c.SetTable(
                    index,
                    table.getThreshold(),
                    table.getVariables().size(),
                    getVarIndices(table, model),
                    table.isRiskModel(),
                    riskVarIndex);
            if (!result) {
                throw new ArgusException("Error in SetTable");
            }
        }
        firePropertyChange("stepName", null, "ComputeTables");
        firePropertyChange("progress", null, 0);

        result = c.ComputeTables(errorCodes, varIndexOut);
        if (!result) {
            throw new ArgusException("Error in ComputeTables");
        }
    }

    private int[] getVarIndices(TableMu table, Combinations model) {
        int[] indices = new int[table.getVariables().size()];
        for (int index = 0; index < indices.length; index++) {
            indices[index] = 1 + model.getVariablesInTables().indexOf(table.getVariables().get(index));
        }
        return indices;
    }

    private int getRelatedVariableIndex(VariableMu variable) {
        if (variable.getRelatedVariable() == null) {
            return 0;
        }
        //TODO
        return 1;
    }

    private int setSafeFileProperties(final int index, final VariableMu variable, MetadataMu metadata, int delta) {
        if (index == 0) {
            //Variable was not in dll
            variable.setStartingPosition(Integer.toString(variable.getStartingPosition() + delta));
        }
        else {
            int[] startPos = new int[1];
            int[] nPos = new int[1];
            int[] nSuppressions = new int[1];
            double[] entropy = new double[1];
            int[] bandwidth = new int[1];
            String[] missing1 = new String[1];
            String[] missing2 = new String[1];
            int[] nOfCodes = new int[1];
            int[] nOfMissing = new int[1];
            c.GetVarProperties(index, startPos, nPos, nSuppressions, entropy, bandwidth, missing1, missing2, nOfCodes, nOfMissing);
            variable.setStartingPosition(Integer.toString(startPos[0]));
            delta += nPos[0] - variable.getVariableLength();
            variable.setVariableLength(Integer.toString(nPos[0]));
            variable.setnOfSuppressions(nSuppressions[0]);
            variable.setEntropy(entropy[0]);
            variable.setBandwidth(bandwidth[0]);
            variable.setnOfCodes(nOfCodes[0]);
            if (nOfMissing[0] > 0)
                variable.setMissing(0, missing1[0]);
            if (nOfMissing[0] > 1)
                variable.setMissing(1, missing2[0]);

            //Global recode codelist
            if (metadata.getCombinations().getGlobalRecode() != null){
                RecodeMu recode = metadata.getCombinations().getGlobalRecode().getRecodeByVariableName(variable.getName());
                if (recode != null &&  (recode.isRecoded() || recode.isTruncated())) {
                    variable.setCodeListFile(recode.getCodeListFile());
                    variable.setCodelist(variable.getCodeListFile() != null);
                }
            }
        }
        return delta;
    }
    public void fillSafeFileMetadata() {
        MetadataMu safeMeta = metadata.getCombinations().getProtectedFile().getSafeMeta();
        ArrayList<VariableMu> variables = getVariables();
        int delta = 0;
        for (VariableMu var : safeMeta.getVariables()) {
            int varIndex = variables.indexOf(var) + 1;
            delta = setSafeFileProperties(varIndex, var, metadata, delta);
        }
        safeMeta.setRecordCount(c.NumberofRecords());
    }
    
    private String getRecodeCodelistFile(Combinations combinations, VariableMu variable) {
        RecodeMu recode = combinations.getGlobalRecode().getRecodeByVariableName(variable.getName());
        return (recode != null && (recode.isRecoded() || recode.isTruncated())) ? recode.getCodeListFile() : "";
    }
    
    public ArrayList<String> getUnsafeCombinations() {
        ArrayList<String> missingCodelists = new ArrayList<>();
        Combinations model = metadata.getCombinations();
        boolean hasRecode = (model.getGlobalRecode() != null);
        model.clearUnsafeCombinations();
        for (int varIndex = 0; varIndex < model.getVariablesInTables().size(); varIndex++) {
            VariableMu variable = model.getVariablesInTables().get(varIndex);

            HashMap<String, String> codelist = new HashMap<>();
            String codelistFile = "";
            if (hasRecode) {
                codelistFile = getRecodeCodelistFile(model, variable);
            }
            if ("".equals(codelistFile) && variable.isCodelist()) {
                codelistFile = variable.getCodeListFile();
            }
            if (!"".equals(codelistFile)) {
                try {
                    readCodelist(codelist, codelistFile, metadata);
                }
                catch (ArgusException ex) {
                    missingCodelists.add(ex.getMessage());
                }
            }

            int[] nDims = new int[]{0};
            int[] unsafeCount = new int[model.getMaxDimsInTables()];
            boolean result = c.UnsafeVariable(varIndex + 1, nDims, unsafeCount);
            //UnsafeInfo unsafe = new UnsafeInfo();
            model.setUnsafeCombinations(variable, unsafeCount, nDims[0]);
            //model.setUnsafe(variable, unsafeCount);

            int[] nCodes = new int[]{0};
            result = c.UnsafeVariablePrepare(varIndex + 1, nCodes);
            int[] isMissing = new int[]{0};
            int[] freq = new int[]{0};
            String[] code = new String[1];
            variable.clearCodeInfos();
            for (int codeIndex = 0; codeIndex < nCodes[0]; codeIndex++) {
                result = c.UnsafeVariableCodes(varIndex + 1,
                        codeIndex + 1,
                        isMissing,
                        freq,
                        code,
                        nDims,
                        unsafeCount);
                CodeInfo codeInfo = new CodeInfo(code[0], isMissing[0] != 0);
                if (codelist.containsKey(code[0].trim())) {
                    codeInfo.setLabel(codelist.get(code[0].trim()));
                }
                codeInfo.setFrequency(freq[0]);
                codeInfo.setUnsafeCombinations(nDims[0], unsafeCount);
                variable.addCodeInfo(codeInfo);
            }
            result = c.UnsafeVariableClose(varIndex + 1);
        }
        return missingCodelists;
    }

    public void setPramVariable(PramVariableSpec pramVariable) throws ArgusException {
        int varIndex = getVariables().indexOf(pramVariable.getVariable()) + 1;
        // TODO: kijk nog goed hoe en waar bandwidth nodig is
        int bandWidth = this.metadata.getCombinations().getPramSpecification().useBandwidth() ? pramVariable.getBandwidth() : -1;
        boolean result = c.SetPramVar(varIndex, bandWidth, false);
        if (!result) {
            throw new ArgusException("Error during SetPramVar");
        }
        for (int codeIndex=0; codeIndex < pramVariable.getVariable().getCodeInfos().size(); codeIndex++) {
            result = c.SetPramValue(codeIndex+1, pramVariable.getVariable().getCodeInfos().get(codeIndex).getPramProbability());
            if (!result)
                throw new ArgusException("Error during SetPramValue");
        }
        result = c.ClosePramVar(varIndex);
            if (!result)
                throw new ArgusException("Error during ClosePramVar");
    }
    
    public void UndoSetPramVariable(PramVariableSpec pramVariable) throws ArgusException {
        int varIndex = getVariables().indexOf(pramVariable.getVariable()) + 1;
        boolean result = c.SetPramVar(varIndex, -1, true);
        if (!result) {
            throw new ArgusException("Error during SetPramVar");
        }
    }
    
    public int calculateUnsafe(TableMu table, double riskThreshold) {
        int tableIndex = this.metadata.getCombinations().getTables().indexOf(table) + 1;
        int[] nUnsafe = new int[1];
        c.SetBirThreshold(tableIndex, Math.log(riskThreshold), nUnsafe);
        return nUnsafe[0];
    }

    public double calculateReidentRate(TableMu table, double riskThreshold) {
        int tableIndex = this.metadata.getCombinations().getTables().indexOf(table) + 1;
        double[] reidentRate = new double[1];
        c.ComputeBIRRateThreshold(tableIndex, riskThreshold, reidentRate);
        return reidentRate[0];
    }

    public double calculateRiskThreshold(TableMu table, int nUnsafe) {
        int tableIndex = this.metadata.getCombinations().getTables().indexOf(table) + 1;
        double[] riskThreshold = new double[1];
        int[] errorCode = new int[1];
        c.SetProgressListener(null);
        c.CalculateBIRFreq(tableIndex, c.NumberofRecords() - nUnsafe, riskThreshold, errorCode);
        return riskThreshold[0];
    }

    public double fillHistogramData(TableMu table, ArrayList<RiskModelClass> classes, boolean cumulative) {
        //TODO: cumulative
        classes.clear();
        double[] ksi = new double[1];
        
        int tableIndex = this.metadata.getCombinations().getTables().indexOf(table) + 1;
        int nClasses = MuARGUS.getNHistogramClasses(cumulative);
        double[] classLeftValue = new double[nClasses + 1];
        int[] frequency = new int[nClasses];
            int[] hhFrequency = new int[nClasses];
        if (metadata.isHouseholdData()) {
            c.GetBHRHistogramData(tableIndex, nClasses, classLeftValue, hhFrequency, frequency);
        }
        else {
            c.GetBIRHistogramData(tableIndex, nClasses, classLeftValue, ksi, frequency);
        }
        int sumFreq = 0;
        int sumHHfreq = 0;
        for (int classIndex=0; classIndex < nClasses; classIndex++) {
            RiskModelClass rmc = new RiskModelClass(Math.exp(classLeftValue[classIndex]),
                    Math.exp(classLeftValue[classIndex+1]),
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
            
            //TODO: to other class
    private void readCodelist(HashMap<String, String> codelist, String path, MetadataMu metadata) throws ArgusException {
        BufferedReader reader = null;
        try {
            File file = new File(path);
            if (!file.isAbsolute()) {
                File dir = new File(metadata.getFileNames().getMetaFileName()).getParentFile();
                file = new File(dir, path);
            }
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            logger.log(Level.SEVERE, null, ex);
            throw new ArgusException(String.format("Codelist %s not found", path));
        }
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    codelist.put(parts[0].trim(), parts[1].trim());
                }
            }
            reader.close();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            try {
                reader.close();
            } catch (Exception e) {
                ;
            }
            throw new ArgusException(String.format("Error in codelist file (%s)", path));
        }
    }

}
