/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.io.File;
import java.util.ArrayList;
import muargus.io.MetaWriter;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.MicroaggregationSpec;
import muargus.model.ProtectedFile;
import muargus.model.RankSwappingSpec;
import muargus.model.ReplacementSpec;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.MakeProtectedFileView;

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileController extends ControllerBase<ProtectedFile> {

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
        if (!isRiskThresholdSpecified()) {
            return;
        }
        this.metadata.getCombinations().getProtectedFile().initSafeMeta(file, this.metadata);
        removeRedundentReplacementSpecs();
        getCalculationService().makeProtectedFile(this);
    }
    
    private boolean isRiskThresholdSpecified() {
        Combinations comb = this.metadata.getCombinations();
        for (TableMu table : comb.getTables()) {
            if (table.isRiskModel()) {
                if (!comb.getRiskSpecifications().containsKey(table) 
                        || comb.getRiskSpecifications().get(table).getRiskThreshold() == 0) {
                    if (!notSpecifiedIsOk(table)) {
                        return false;
                    }
                } 
            }
        }
        return true;
    }
    
    private boolean notSpecifiedIsOk(TableMu table) {
        String message = String.format("Table %s was specified in the Risk Model, but no Risk threshold was specified.\nContinue anyway?",
                table.getTableTitle());
        return (getView().showConfirmDialog(message));
    }
    
    private void removeRedundentReplacementSpecs() {
        ArrayList<ReplacementSpec> toRemove = new ArrayList<>();
        int index = 0;
        for (ReplacementSpec replacement : this.metadata.getReplacementSpecs()) {
            ArrayList<VariableMu> variablesFound = new ArrayList<>();
            for (int index2 = index+1; index2 < this.metadata.getReplacementSpecs().size(); index2++) {
                ReplacementSpec replacement2 = this.metadata.getReplacementSpecs().get(index2);
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
        for (ReplacementSpec replacement : toRemove) {
            if (replacement instanceof RankSwappingSpec) {
                this.metadata.getCombinations().getNumericalRankSwapping().getRankSwappings().remove((RankSwappingSpec)replacement);
            }
            else {
                MicroaggregationSpec spec = (MicroaggregationSpec)replacement;
                this.metadata.getCombinations().getMicroaggregation(spec.isNumerical()).getMicroaggregations().remove(spec);
            }
        }
    }
    
    private void saveSafeMeta() {
        
        getCalculationService().fillSafeFileMetadata();
        MetadataMu safeMetadata = this.metadata.getCombinations().getProtectedFile().getSafeMeta();
        try {
            MetaWriter.writeRda(safeMetadata.getFileNames().getMetaFileName(), 
                    safeMetadata, false);
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
