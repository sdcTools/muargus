package muargus;

import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import muargus.controller.SelectCombinationsController;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.extern.dataengine.IProgressListener;
import muargus.model.ProtectedFile;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.RecodeMu;
import muargus.model.TableMu;
import muargus.model.CodeInfo;
import muargus.model.PramVariableSpec;
import muargus.model.ReplacementSpec;
import muargus.model.RiskModelClass;
//import muargus.model.UnsafeInfo;
import muargus.model.VariableMu;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Statistics Netherlands
 */
public class CalculationService {

    /**
     *
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
     *
     */
    private enum BackgroundTask {

        ExploreFile,
        CalculateTables,
        MakeProtectedFile,
        MakeReplacementFile
    }

    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());
    private final CMuArgCtrl c;
    private MetadataMu metadata;
    private final ProgressListener progressListener; //must remain in scope, or will be garbage collected 

    /**
     *
     * @param muArgCtrl
     */
    public CalculationService(final CMuArgCtrl muArgCtrl) {
        this.c = muArgCtrl;
        this.progressListener = new ProgressListener(this);
        this.c.SetProgressListener(this.progressListener);
        this.metadata = null;
    }

    private PropertyChangeListener listener;

    /**
     *
     * @param listener
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
     *
     * @param metadata
     */
    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        this.c.CleanAll();
    }

    /**
     *
     * @param listener
     */
    public void makeReplacementFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.MakeReplacementFile, listener);
    }

    /**
     *
     * @throws ArgusException
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
     *
     * @param errorType
     * @return
     */
    private String getErrorString(int errorType) {
        String[] error = new String[1];
        this.c.GetErrorString(errorType, error);
        return error[0];
    }

    /**
     *
     * @param recode
     * @return
     * @throws ArgusException
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
     *
     * @param dimensions
     * @return
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
     *
     * @throws ArgusException
     */
    public void applyRecode() throws ArgusException {
        //c.SetProgressListener(null);
        boolean result = this.c.ApplyRecode();
        if (!result) {
            throw new ArgusException("Error during Apply recode");
        }
        //return getUnsafeCombinations(metadata);
    }

    /**
     *
     * @param recode
     * @throws ArgusException
     */
    public void undoRecode(RecodeMu recode) throws ArgusException {
        //c.SetProgressListener(null);
        int index = getIndexOf(recode.getVariable());
        boolean result = this.c.UndoRecode(index);
        if (!result) {
            throw new ArgusException("Error while undoing recode");
        }
        applyRecode();
    }

    /**
     *
     * @param recode
     * @param positions
     * @throws ArgusException
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
     *
     * @throws ArgusException
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
     *
     * @throws ArgusException
     */
    private void makeFileInBackground() throws ArgusException {
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

        boolean result = this.c.MakeFileSafe(protectedFile.getSafeMeta().getFileNames().getDataFileName(),
                protectedFile.isWithPrior(),
                protectedFile.isWithEntropy(),
                protectedFile.getHouseholdType(),
                protectedFile.isRandomizeOutput(),
                protectedFile.isPrintBHR());

        if (!result) {
            throw new ArgusException("Error during Make protected file");
        }
    }

    /**
     *
     * @param listener
     */
    public void calculateTables(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.CalculateTables, listener);
    }

    /**
     *
     * @param ex
     */
    private void workerDone(Exception ex) {
        if (ex == null) {
            this.firePropertyChange("result", null, "success");
        } else {
            this.firePropertyChange("error", null, ex.getCause());
            this.firePropertyChange("result", null, "error");
        }
        //this.listener = null;
    }

    /**
     *
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (this.listener != null) {
            this.listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    //TODO: wordt deze methode gebruikt?
    /**
     *
     * @param worker
     * @return
     */
    private PropertyChangeListener getWorkerPropertyChangeListener(final SwingWorker worker) {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                worker.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        };
    }

    /**
     *
     * @param variable
     * @param varNr
     * @return
     */
    private boolean addVariable(VariableMu variable, int varNr) {
        //For numeric non-weight variables, the dll needs a missing value, but it's not required in the model
        //So a default (dummy) value is passed in this case
        String missing0 = variable.getMissing(0);
        if (variable.isNumeric() && !variable.isWeight() && "".equals(missing0)) {
            missing0 = StringUtils.repeat("X", variable.getVariableLength());
        } else {
            missing0 += StringUtils.repeat(" ", variable.getVariableLength() - missing0.length());      //Make the right length
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

    //TODO: wordt deze methode gebruikt?
//    /**
//     * 
//     * @param model
//     * @param metadata
//     * @return
//     * @throws ArgusException 
//     */
//    private int getNVar(Combinations model, MetadataMu metadata) throws ArgusException {
//        //For free format, all variables
//        if (metadata.getDataFileType() != MetadataMu.DATA_FILE_TYPE_FIXED) {
//            return metadata.getVariables().size();
//        }
//
//        int nVar = model.getVariablesInTables().size();
//        if (model.isRiskModel()) {
//            //Add weight when risk model is used.
//            for (VariableMu weightVar : metadata.getVariables()) {
//                if (weightVar.isWeight()) {
//                    if (!model.getVariablesInTables().contains(weightVar)) {
//                        nVar++;
//                    }
//                    return nVar;
//                }
//            }
//            throw new ArgusException("Risk model but no weight variable");
//        }
//        return nVar;
//    }
    /**
     *
     * @return
     */
    public ArrayList<VariableMu> getVariables() {
        return this.metadata.getVariables();
    }

    /**
     *
     * @return
     */
    public ArrayList<TableMu> getTables() {
        return this.metadata.getCombinations().getTables();
    }

    /**
     *
     * @param variables
     * @return
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
     *
     * @param taskType
     * @throws ArgusException
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
                makeFileInBackground();
                break;
            case MakeReplacementFile:
                makeReplacementFileInBackground();
                break;
        }
    }

    /**
     *
     * @throws ArgusException
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
     *
     * @param listener
     */
    public void exploreFile(PropertyChangeListener listener) {
        executeSwingWorker(BackgroundTask.ExploreFile, listener);
    }

    /**
     *
     * @param taskType
     * @param listener
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
                    logger.log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    workerDone(ex);
                }
            }
        };
        worker.addPropertyChangeListener(this.listener);
        worker.execute();

    }

    /**
     *
     * @throws ArgusException
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
                    getVarIndicesInTable(table),//, model),
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
     *
     * @param variables
     * @return
     */
    private int[] getVarIndicesInFile(ArrayList<VariableMu> variables) {
        int[] indices = new int[variables.size()];
        for (int index = 0; index < indices.length; index++) {
            indices[index] = getIndexOf(variables.get(index));//1 + this.metadata.getVariables().indexOf(variables.get(index));
        }
        return indices;
    }

    /**
     *
     * @param table
     * @param model
     * @return
     */
    private int[] getVarIndicesInTable(TableMu table) {//, Combinations model) {
        int[] indices = new int[table.getVariables().size()];
        for (int index = 0; index < indices.length; index++) {
            indices[index] = getIndexOf(table.getVariables().get(index));
            //indices[index] = 1 + getVariables().indexOf(table.getVariables().get(index));
        }
        return indices;
    }

    /**
     *
     * @param variable
     * @return
     */
    private int getRelatedVariableIndex(VariableMu variable) {
        if (variable.getRelatedVariable() == null) {
            return 0;
        }
        return getIndexOf(variable.getRelatedVariable());//this.metadata.getVariables().indexOf(variable.getRelatedVariable()) + 1;

    }

    /**
     *
     * @param index
     * @param variable
     * @param metadata
     * @param delta
     * @return
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
                if (recode != null && (recode.isRecoded() || recode.isTruncated())) {
                    variable.setCodeListFile(recode.getCodeListFile());
                    variable.setCodelist(variable.getCodeListFile() != null);
                }
            }
        }
        return delta;
    }

    /**
     *
     * @param variable
     * @param top
     * @param value
     * @param replacement
     * @param undo
     * @throws ArgusException
     */
    public void setTopBottomCoding(VariableMu variable, boolean top, double value, String replacement, boolean undo) throws ArgusException {
        boolean result;
        if (top) {
            result = this.c.SetCodingTop(
                    getIndexOf(variable),
                    //getVariables().indexOf(variable) + 1,
                    value,
                    replacement,
                    undo);
        } else {
            result = this.c.SetCodingBottom(
                    getIndexOf(variable),
                    //getVariables().indexOf(variable) + 1,
                    value,
                    replacement,
                    undo);
        }
        if (!result) {
            throw new ArgusException("Error in Set Top/Bottom coding");
        }
    }

    /**
     *
     * @param variable
     * @param base
     * @param nDecimals
     * @param undo
     * @throws ArgusException
     */
    public void setRounding(VariableMu variable, Double base, int nDecimals, boolean undo) throws ArgusException {
        boolean result = this.c.SetRound(getIndexOf(variable),//getVariables().indexOf(variable) + 1,
                base,
                nDecimals,
                undo);
        if (!result) {
            throw new ArgusException("Error in Set Rounding");
        }
    }

    /**
     *
     * @param variable
     * @param noise
     * @param undo
     * @throws ArgusException
     */
    public void setWeightNoise(VariableMu variable, double noise, boolean undo) throws ArgusException {
        boolean result = this.c.SetWeightNoise(getIndexOf(variable),//getVariables().indexOf(variable) + 1,
                noise,
                undo);
        if (!result) {
            throw new ArgusException("Error in Set Weight noise");
        }
    }

    /**
     *
     */
    public void fillSafeFileMetadata() {
        MetadataMu safeMeta = this.metadata.getCombinations().getProtectedFile().getSafeMeta();
        //ArrayList<VariableMu> variables = getVariables();
        int delta = 0;
        for (VariableMu var : safeMeta.getVariables()) {
            int varIndex = getIndexOf(var);//variables.indexOf(var) + 1;
            delta = setSafeFileProperties(varIndex, var, this.metadata, delta);
        }
        safeMeta.setRecordCount(this.c.NumberofRecords());
    }

    /**
     *
     * @throws ArgusException
     */
    public void getVariableInfo() throws ArgusException {
        Combinations model = this.metadata.getCombinations();
        //boolean hasRecode = (model.getGlobalRecode() != null);
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
     *
     * @param pramVariable
     * @throws ArgusException
     */
    public void setPramVariable(PramVariableSpec pramVariable) throws ArgusException {
        //int varIndex = getVariables().indexOf(pramVariable.getVariable()) + 1;
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
     *
     * @param pramVariable
     * @throws ArgusException
     */
    public void undoSetPramVariable(PramVariableSpec pramVariable) throws ArgusException {
        //int varIndex = getVariables().indexOf(pramVariable.getVariable()) + 1;
        int varIndex = getIndexOf(pramVariable.getVariable());
        boolean result = this.c.SetPramVar(varIndex, -1, true);
        if (!result) {
            throw new ArgusException("Error during SetPramVar");
        }
    }

    /**
     *
     * @param variable
     * @return
     */
    public double[] getMinMax(VariableMu variable) {
        int varIndex = getIndexOf(variable);//getVariables().indexOf(variable) + 1;
        double[] min = new double[1];
        double[] max = new double[1];
        this.c.GetMinMaxValue(varIndex, min, max);
        double[] min_max = {min[0], max[0]};
        return min_max;
    }

    /**
     *
     * @param table
     * @param riskThreshold
     * @param household
     * @return
     * @throws ArgusException
     */
    public int calculateUnsafe(TableMu table, double riskThreshold, boolean household) throws ArgusException {
        int tableIndex = getIndexOf(table);//this.metadata.getCombinations().getTables().indexOf(table) + 1;
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
     *
     * @param table
     * @param riskThreshold
     * @return
     * @throws ArgusException
     */
    public double calculateReidentRate(TableMu table, double riskThreshold) throws ArgusException {
        int tableIndex = getIndexOf(table);//int tableIndex = this.metadata.getCombinations().getTables().indexOf(table) + 1;
        double[] reidentRate = new double[1];
        if (!this.c.ComputeBIRRateThreshold(tableIndex, riskThreshold, reidentRate)) {
            throw new ArgusException("Error calculating reident rate");
        }
        return reidentRate[0];
    }

    /**
     *
     * @param table
     * @param nUnsafe
     * @param household
     * @return
     * @throws ArgusException
     */
    public double calculateRiskThreshold(TableMu table, int nUnsafe, boolean household) throws ArgusException {
        int tableIndex = getIndexOf(table);//int tableIndex = this.metadata.getCombinations().getTables().indexOf(table) + 1;
        double[] riskThreshold = new double[1];
        int[] errorCode = new int[1];
        //c.SetProgressListener(null);
        boolean result = household
                ? this.c.CalculateBHRFreq(tableIndex, true, this.c.NumberofRecords() - nUnsafe, this.c.NumberofRecords() - nUnsafe, riskThreshold, errorCode)
                : this.c.CalculateBIRFreq(tableIndex, this.c.NumberofRecords() - nUnsafe, riskThreshold, errorCode);
        if (!result) {
            throw new ArgusException("Error calculating frequency");
        }
        return riskThreshold[0];
    }

    /**
     *
     * @param table
     * @param classes
     * @param cumulative
     * @return
     * @throws ArgusException
     */
    public double fillHistogramData(TableMu table, ArrayList<RiskModelClass> classes, boolean cumulative) throws ArgusException {
        classes.clear();
        double[] ksi = new double[1];

        int tableIndex = getIndexOf(table);//this.metadata.getCombinations().getTables().indexOf(table) + 1;
        int nClasses = MuARGUS.getNHistogramClasses(cumulative);
        double[] classLeftValue = new double[nClasses + 1];
        int[] frequency = new int[nClasses + 1];
        int[] hhFrequency = new int[nClasses + 1];
        if (this.metadata.isHouseholdData()) {
            int[] errorCode = new int[1];
            //c.SetProgressListener(null);
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

}
