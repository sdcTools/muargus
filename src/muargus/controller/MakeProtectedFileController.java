/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.io.File;
import java.util.ArrayList;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.ReplacementFile;
import muargus.model.VariableMu;
import muargus.view.MakeProtectedFileView;

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileController extends ControllerBase {

    private final MetadataMu metadata;
    private boolean fileCreated;
    /**
     *
     * @param parentView
     * @param metadata
     */
    public MakeProtectedFileController(java.awt.Frame parentView, MetadataMu metadata) {
        //this.model = model;
        //this.selectCombinationsModel = selectCombinationsModel;
        //this.model.setRiskModel(this.selectCombinationsModel.isRiskModel());
        this.setView(new MakeProtectedFileView(parentView, true, this));
        this.metadata = metadata;
        this.fileCreated = false;

        getView().setMetadata(this.metadata);
    }

    
    /**
     *
     * @param file
     */
    public void makeFile(File file) {
        this.metadata.getCombinations().getProtectedFile().initSafeMeta(file, this.metadata);
        removeRedundentReplacementFiles();
        getCalculationService().makeProtectedFile(this);
    }
    
    private void removeRedundentReplacementFiles() {
        ArrayList<ReplacementFile> toRemove = new ArrayList<>();
        int index = 0;
        for (ReplacementFile replacement : this.metadata.getReplacementFiles()) {
            ArrayList<VariableMu> variablesFound = new ArrayList<>();
            for (int index2 = index+1; index2 < this.metadata.getReplacementFiles().size(); index2++) {
                ReplacementFile replacement2 = this.metadata.getReplacementFiles().get(index2);
                for (VariableMu variable : replacement2.getVariables()) {
                    if (replacement.getVariables().contains(variable) && !variablesFound.contains(variable)) {
                        variablesFound.add(variable);
                    }
                }
            }
            if (variablesFound.size() == replacement.getVariables().size()) {
                toRemove.add(replacement);
            }
            index++;
        }
        for (ReplacementFile replacement : toRemove) {
            this.metadata.getReplacementFiles().remove(replacement);
        }
    }
    
    private void saveSafeMeta() {
        
        getCalculationService().fillSafeFileMetadata();
        MetadataMu safeMetadata = this.metadata.getCombinations().getProtectedFile().getSafeMeta();
        File file = new File(safeMetadata.getFileNames().getMetaFileName());
        try {
            safeMetadata.write(file, false);
            this.fileCreated = true;
        }
        catch (ArgusException ex) {
            this.getView().showErrorMessage(ex);
        }
    }

    public Combinations getCombinations() {
        return this.metadata.getCombinations();
    }

    public boolean isFileCreated() {
        return fileCreated;
    }

    @Override
    protected void doNextStep(boolean success) {
        if (success) {
            saveSafeMeta();
            if (fileCreated) {
                this.getView().setVisible(false);
            }
        }
    }

  
}
