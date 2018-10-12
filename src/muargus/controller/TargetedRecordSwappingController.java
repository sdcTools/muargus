/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.util.ArrayList;
import muargus.extern.dataengine.Numerical;
import muargus.model.MetadataMu;
import muargus.model.TargetSwappingSpec;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.TargetedRecordSwapping;
import muargus.model.VariableMu;
import muargus.view.TargetedRecordSwappingView;

/**
 *
 * @author pwof
 */
public class TargetedRecordSwappingController extends ControllerBase<TargetedRecordSwapping>{
     private final MetadataMu metadata;

    /**
     * Constructor for the TargetedRecordSwappingController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the original metadata.
     */
    public TargetedRecordSwappingController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new TargetedRecordSwappingView(parentView, true, this));
        this.metadata = metadata;
        fillModel();
        getView().setMetadata(metadata);
    }

    /**
     * Gets the model and fills the model with the numeric variables if the
     * model is empty.
     */
    private void fillModel() {
        TargetedRecordSwapping model = metadata.getCombinations().getTargetedRecordSwapping();
        if (model.getVariables().isEmpty()) {
            for (VariableMu variable : this.metadata.getVariables()) {
                if (!variable.isNumeric()) {
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

    /**
     * Does the next step if the previous step was successful.
     *
     * @param success Boolean indicating whether the previous step was
     * successful.
     */
    @Override
    protected void doNextStep(boolean success) {
        TargetSwappingSpec swapping = getModel().getTargetSwappings().get(getModel().getTargetSwappings().size() - 1);
        Numerical num = new Numerical();
        int[] errorCode = new int[1];
        int[] similar = new int[5];
        int[] hierarchy = new int[5];
        int[] risk = new int[5];
        int seed=12345;
        int hhID=1;
        
        num.DoTargetedRecordSwap(swapping.getReplacementFile().getInputFilePath(),
                                 swapping.getReplacementFile().getOutputFilePath(),
                                 muargus.MuARGUS.getDefaultSeparator(),
                                 swapping.getOutputVariables().size(),
                                 swapping.getSwaprate(),
                                 similar,
                                 hierarchy,
                                 risk,
                                 hhID,
                                 swapping.getkThreshold(),
                                 seed
                                 );

        if (errorCode[0] != 0) {
            getView().showErrorMessage(new ArgusException("Error during targeted record swapping"));
            this.metadata.getReplacementSpecs().remove(swapping);
            getModel().getTargetSwappings().remove(swapping);
        } else {
            getView().showMessage("TargetedRecordSwapping successfully completed");
            SystemUtils.writeLogbook("Tageted record swapping has been done.");
        }
        getView().setProgress(0);
        getView().showStepName("");
        getTargetedRecordSwappingView().updateVariableRows(swapping);
    }

    /**
     * Gets the NumericalRankSwapping view.
     *
     * @return TargetedRecordSwappingView
     */
    private TargetedRecordSwappingView getTargetedRecordSwappingView() {
        return (TargetedRecordSwappingView) getView();
    }

    /**
     * Checks whether the value for the percentage is valid.
     *
     * @return Boolean indicating whether the value for the percentage is valid.
     */
    private boolean checkFields() {
        double percentage = getTargetedRecordSwappingView().getSwaprate();
        if (Double.isNaN(percentage) || percentage <= 0 || percentage > 1) {
            getView().showErrorMessage(new ArgusException("Illegal value for the swaprate"));
            return false;
        }
        return true;
    }

    /**
     * Undo's the numerical rank swapping
     */
    public void undo() {
    /*    ArrayList<VariableMu> selected = getTargetedRecordSwappingView().getSelectedVariables();
        if (selected.isEmpty()) {
            return;
        }
        if (!getView().showConfirmDialog(String.format("The rank swapping involving %s will be removed. Continue?",
                VariableMu.printVariableNames(selected)))) {
            return;
        }
        String rankSwappings = (getModel().getTargetSwappings().size()>1)? "s are:" : " is:";
        for (TargetSwappingSpec swapping : getModel().getTargetSwappings()) {
            if (swapping.getOutputVariables().size() == selected.size()) {
                boolean difference = false;
                for (VariableMu variable : swapping.getOutputVariables()) {
                    if (!selected.contains(variable)) {
                        difference = true;
                        break;
                    }
                }
                if (!difference) {
                    getModel().getTargetSwappings().remove(swapping);
                    this.metadata.getReplacementSpecs().remove(swapping);
                    SystemUtils.writeLogbook("Numerical rank swapping has been undone.");
                    getTargetedRecordSwappingView().updateVariableRows(swapping);
                    return;
                }
            }
            rankSwappings += "\n- " + VariableMu.printVariableNames(swapping.getOutputVariables());
        }
        
        getView().showMessage(String.format("Rank swapping involving %s not found.\n"
                + "The available rank swapping" + rankSwappings, VariableMu.printVariableNames(selected)));
    */}

    /**
     * Calculates the numerical rank swapping.
     */
    public void calculate() {
        if (!checkFields()) {
            return;
        }
        ArrayList<VariableMu> selectedVariables = getTargetedRecordSwappingView().getSelectedSimilarVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        selectedVariables = getTargetedRecordSwappingView().getSelectedHierarchyVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        selectedVariables = getTargetedRecordSwappingView().getSelectedRiskVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        
        TargetSwappingSpec rankSwapping = new TargetSwappingSpec(getTargetedRecordSwappingView().getSwaprate(),
                                                                 getTargetedRecordSwappingView().getkanonThreshold());
        try {
            rankSwapping.getOutputVariables().addAll(selectedVariables);
            rankSwapping.setReplacementFile(new ReplacementFile("RankSwapping"));
            this.metadata.getReplacementSpecs().add(rankSwapping);
            getCalculationService().makeReplacementFile(this);
            getModel().getTargetSwappings().add(rankSwapping);
        } catch (ArgusException ex) {
            this.metadata.getReplacementSpecs().remove(rankSwapping);
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Returns whether at least one variable is used.
     *
     * @param variables Arraylist of VariableMu's.
     * @return Boolean indicating whether at least one variable is used.
     */
    private boolean variablesAreUsed(ArrayList<VariableMu> variables) {
        for (VariableMu variable : variables) {
            for (ReplacementSpec replacement : this.metadata.getReplacementSpecs()) {
                if (replacement.getOutputVariables().contains(variable)) {
                    return true;
                }
            }
        }
        return false;
    }

}
