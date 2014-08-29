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
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import muargus.MuARGUS;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.extern.dataengine.IProgressListener;
import muargus.model.ProtectedFile;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.TableMu;
import muargus.model.UnsafeCodeInfo;
import muargus.model.UnsafeInfo;
import muargus.model.VariableMu;

/**
 *
 * @author pibd05
 */
public class TableService {

    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());

    private final CMuArgCtrl c = MuARGUS.getMuArgCtrl();

    private PropertyChangeListener listener;

    public void makeProtectedFile(final MetadataMu metadata) {
        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            // called in a separate thread...
            @Override
            protected Void doInBackground() throws Exception {
                makeFileInBackground(metadata);
                return null;
            }

            // called on the GUI thread
            @Override
            public void done() {
                super.done();
                try {
                    get();
                    workerDone();
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    JOptionPane.showMessageDialog(null, ex.getCause().getMessage());
                }
            }
        };
        worker.addPropertyChangeListener(null);
        worker.execute();
    }

    private void makeFileInBackground(final MetadataMu metadata) {
        ProtectedFile model = metadata.getCombinations().getProtectedFile();
        IProgressListener progressListener = new IProgressListener() {
            @Override
            public void UpdateProgress(final int percentage) {
                firePropertyChange("progress", null, percentage);
                //propertyChanged(listener, "progress", null, percentage);
            }
        };
        c.SetProgressListener(progressListener);
        int index = 0;
        for (VariableMu variable : getVariables(metadata, metadata.getCombinations())) {
            index++;
            if (model.getVariables().contains(variable)) {
                c.SetSuppressPrior(index, variable.getSuppressweight());
            }
        }
        
        boolean result = c.MakeFileSafe(model.getNameOfSafeFile(), model.isWithPrior(), model.isWithEntropy(),
                model.getHouseholdType(), model.isRandomizeOutput(), model.isPrintBHR());

         //TODO: verander in argusException
        if (!result) {
            System.out.println("gefaald");
        } else {
            System.out.println("gelukt");
        }
    }

    public void calculateTables(final MetadataMu metadata) {

        final SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            // called in a separate thread...
            @Override
            protected Void doInBackground() throws Exception {
                calculateInBackground(metadata);
                getUnsafeCombinations(metadata);
                return null;
            }

            // called on the GUI thread
            @Override
            public void done() {
                super.done();
                try {
                    get();
                    workerDone();
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    JOptionPane.showMessageDialog(null, ex.getCause().getMessage());
                }
            }
        };
        worker.addPropertyChangeListener(null);
        worker.execute();
    }

    private void workerDone() {
        this.firePropertyChange("status", null, "done");
    }

    public void setPropertyChangeListener(PropertyChangeListener listener) {
        this.listener = listener;
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

    private ArrayList<VariableMu> getVariables(MetadataMu metadata, Combinations model) {
        if (metadata.getDataFileType() != MetadataMu.DATA_FILE_TYPE_FIXED) {
            return metadata.getVariables();
        }
        ArrayList<VariableMu> variables = new ArrayList<>(model.getVariablesInTables());
        if (model.isRiskModel()) {
            for (VariableMu weightVar : metadata.getVariables()) {
                if (weightVar.isWeight()) {
                    if (!variables.contains(weightVar)) {
                        variables.add(weightVar);
                    }
                    break;
                }
            }
        }
        return variables;
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

    private void calculateInBackground(MetadataMu metadata) throws ArgusException {
        Combinations model = metadata.getCombinations();
        ArrayList<VariableMu> variables = getVariables(metadata, model);
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
            throw new ArgusException("Error in ExploreFile");
        }

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

    public void getUnsafeCombinations(MetadataMu metadata) {
        Combinations model = metadata.getCombinations();
        model.clearUnsafe();
        for (int varIndex = 0; varIndex < model.getVariablesInTables().size(); varIndex++) {
            VariableMu variable = model.getVariablesInTables().get(varIndex);

            HashMap<String, String> codelist = new HashMap<>();
            if (variable.isCodelist()) {
                readCodelist(codelist, variable.getCodeListFile(), metadata);
            }

            int[] nDims = new int[]{0};
            int[] unsafeCount = new int[model.getVariablesInTables().size()];
            boolean result = c.UnsafeVariable(varIndex + 1, nDims, unsafeCount);
            UnsafeInfo unsafe = new UnsafeInfo();
            unsafe.setUnsafeCombinations(nDims[0], unsafeCount);
            model.setUnsafe(variable, unsafe);

            int[] nCodes = new int[]{0};
            result = c.UnsafeVariablePrepare(varIndex + 1, nCodes);
            int[] isMissing = new int[]{0};
            int[] freq = new int[]{0};
            String[] code = new String[1];
            for (int codeIndex = 0; codeIndex < nCodes[0]; codeIndex++) {
                result = c.UnsafeVariableCodes(varIndex + 1,
                        codeIndex + 1,
                        isMissing,
                        freq,
                        code,
                        nDims,
                        unsafeCount);
                UnsafeCodeInfo codeInfo = new UnsafeCodeInfo(code[0], isMissing[0] != 0);
                if (codelist.containsKey(code[0].trim())) {
                    codeInfo.setLabel(codelist.get(code[0].trim()));
                }
                codeInfo.setFrequency(freq[0]);
                codeInfo.setUnsafeCombinations(nDims[0], unsafeCount);
                unsafe.addUnsafeCodeInfo(codeInfo);
            }
            result = c.UnsafeVariableClose(varIndex + 1);
        }
    }

    private void readCodelist(HashMap<String, String> codelist, String path, MetadataMu metadata) {

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
            return;
        }
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                codelist.put(parts[0].trim(), parts[1].trim());
            }
            reader.close();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            try {
                reader.close();
            } catch (Exception e) {
                ;
            }
        }
    }

}
