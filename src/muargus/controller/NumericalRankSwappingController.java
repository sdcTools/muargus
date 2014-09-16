/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import argus.model.ArgusException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.NumericalRankSwapping;
import muargus.model.RankSwappingSpec;
import muargus.model.ReplacementFile;
import muargus.model.VariableMu;
import muargus.view.NumericalRankSwappingView;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ambargus
 */
public class NumericalRankSwappingController extends ControllerBase {
    NumericalRankSwapping model;
    MetadataMu metadata;
    CalculationService calculationService;

    public NumericalRankSwappingController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new NumericalRankSwappingView(parentView, true, this));
        this.metadata = metadata;
        this.calculationService = MuARGUS.getCalculationService();
        this.model = metadata.getCombinations().getNumericalRankSwapping();
        getView().setMetadata(metadata);
    }
    
    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    @Override
    protected void doNextStep(boolean success) {
            //TODO: de catalaan
            //for now: just copy the file
            ReplacementFile replacement = this.metadata.getReplacementFiles().get(this.metadata.getReplacementFiles().size()-1);
            try {
                FileUtils.copyFile(new File(replacement.getInputFilePath()), new File(replacement.getOutputFilePath()));
                getView().showMessage("RankSwapping successfully completed");
                getView().setProgress(0);
                getView().showStepName("");
            }
            catch (IOException ex) {
                getView().showErrorMessage(new ArgusException(ex.getMessage()));
            }
    }
    
    private NumericalRankSwappingView getNumericalRankSwappingView() {
        return (NumericalRankSwappingView)getView();
    }
    
    public void calculate() {
        ArrayList<VariableMu> selectedVariables = getNumericalRankSwappingView().getSelectedVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        try {
            RankSwappingSpec rankSwapping = new RankSwappingSpec(getNumericalRankSwappingView().getPercentage());
            rankSwapping.getVariables().addAll(selectedVariables);
            this.model.getRankSwappings().add(rankSwapping);
            ReplacementFile replacement = new ReplacementFile("RankSwapping");
            replacement.getVariables().addAll(selectedVariables);
            this.metadata.getReplacementFiles().add(replacement);
            this.calculationService.makeReplacementFile(this);
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }
    
    private boolean variablesAreUsed(ArrayList<VariableMu> variables) {
        for (VariableMu variable : variables) {
            for (ReplacementFile replacement : this.metadata.getReplacementFiles()) {
                if (replacement.getVariables().contains(variable)) {
                    return true;
                }
            }
        }
        return false;
    }

    
    
}
