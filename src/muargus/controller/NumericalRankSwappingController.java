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
import muargus.model.MetadataMu;
import muargus.model.NumericalRankSwapping;
import muargus.model.RankSwappingSpec;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.VariableMu;
import muargus.view.NumericalRankSwappingView;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ambargus
 */
public class NumericalRankSwappingController extends ControllerBase<NumericalRankSwapping> {
    private final MetadataMu metadata;

    public NumericalRankSwappingController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new NumericalRankSwappingView(parentView, true, this));
        this.metadata = metadata;
        fillModel();
        getView().setMetadata(metadata);
    }
    
    private void fillModel() {
        NumericalRankSwapping model = metadata.getCombinations().getNumericalRankSwapping();
        if (model.getVariables().isEmpty()) {
            for (VariableMu variable : this.metadata.getVariables()) {
                if (variable.isNumeric()) {
                    model.getVariables().add(variable);
                }
            }
        }
        setModel(model);
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
            RankSwappingSpec swapping = getModel().getRankSwappings().get(getModel().getRankSwappings().size()-1);
            try {
                FileUtils.copyFile(new File(swapping.getReplacementFile().getInputFilePath()), 
                        new File(swapping.getReplacementFile().getOutputFilePath()));
                getView().showMessage("RankSwapping successfully completed");
                getView().setProgress(0);
                getView().showStepName("");
                getNumericalRankSwappingView().updateVariableRows(getModel().getRankSwappings().get(getModel().getRankSwappings().size()-1));
            }
            catch (IOException ex) {
                getView().showErrorMessage(new ArgusException(ex.getMessage()));
            }
    }
    
    private NumericalRankSwappingView getNumericalRankSwappingView() {
        return (NumericalRankSwappingView)getView();
    }
    
    private String printVariableNames(ArrayList<VariableMu> list) {
        StringBuilder b = new StringBuilder(list.get(0).getName());
        for (int i=1; i < list.size(); i++) {
            b.append(", ");
            b.append(list.get(i).getName());
        }
        return b.toString();
    }
    
    public void undo() {
        ArrayList<VariableMu> selected = getNumericalRankSwappingView().getSelectedVariables();
        if (selected.isEmpty()) {
            return;
        }
        if (!getView().showConfirmDialog(String.format("The rank swapping involving %s will be removed. Continue?",
                printVariableNames(selected)))) {
            return;
        }
        ArrayList<RankSwappingSpec> swappingsToUndo = new ArrayList<>();
        for (VariableMu variable : selected) {
            for (RankSwappingSpec swapping : getModel().getRankSwappings()) {
                if (swapping.getVariables().contains(variable) && !swappingsToUndo.contains(swapping)) {
                    swappingsToUndo.add(swapping);
                }
            }
        }
        for (RankSwappingSpec swapping : swappingsToUndo) {
            getModel().getRankSwappings().remove(swapping);
            this.metadata.getReplacementSpecs().remove(swapping);
            getNumericalRankSwappingView().updateVariableRows(swapping);
            //TODO: remove temporary files?
        }
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
            rankSwapping.setReplacementFile(new ReplacementFile("RankSwapping"));
            getModel().getRankSwappings().add(rankSwapping);
            this.metadata.getReplacementSpecs().add(rankSwapping);
            getCalculationService().makeReplacementFile(this);
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }
    
    private boolean variablesAreUsed(ArrayList<VariableMu> variables) {
        for (VariableMu variable : variables) {
            for (ReplacementSpec replacement : this.metadata.getReplacementSpecs()) {
                if (replacement.getVariables().contains(variable)) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
