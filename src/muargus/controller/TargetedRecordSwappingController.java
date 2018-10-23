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
        Numerical num = new Numerical(); // instance of the library-class
        int[] errorCode = new int[1];
       
        num.DoTargetedRecordSwap(swapping.getReplacementFile().getInputFilePath(),
                                 swapping.getReplacementFile().getOutputFilePath(),
                                 muargus.MuARGUS.getDefaultSeparator(),
                                 swapping.getOutputVariables().size(),
                                 swapping.getSwaprate(),
                                 swapping.getSimilarIndexes(),
                                 swapping.getNSim(),
                                 swapping.getHierarchyIndexes(),
                                 swapping.getNHier(),
                                 swapping.getRiskIndexes(),
                                 swapping.getNRisk(),
                                 swapping.getHHID(),
                                 swapping.getkThreshold(),
                                 swapping.getSeed(),
                                 errorCode
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
        if (Double.isNaN(percentage) || percentage < 0 || percentage > 1) {
            getView().showErrorMessage(new ArgusException("Illegal value for the swaprate (should be > 0 and < 1)"));
            return false;
        }
        return true;
    }

    /**
     * Undo's the numerical rank swapping
     */
    public void undo() {
        ArrayList<VariableMu> selected = new ArrayList<>();
        selected.addAll(getTargetedRecordSwappingView().getSelectedSimilarVariables());
        for (VariableMu variable : getTargetedRecordSwappingView().getSelectedHierarchyVariables()){
            if (!selected.contains(variable)) selected.add(variable);
        }
        for (VariableMu variable : getTargetedRecordSwappingView().getSelectedRiskVariables()){
            if (!selected.contains(variable)) selected.add(variable);
        }
        
        if (selected.isEmpty()) {
            getView().showMessage(String.format("Nothing to Undo: Targeted Record Swapping was not applied yet.\n"));
            return;
        }
        selected.add(getTargetedRecordSwappingView().getHHIDVar());
        
        if (!getView().showConfirmDialog(String.format("The Targeted Record Swapping involving %s will be removed. Continue?",
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
                    SystemUtils.writeLogbook("Targeted Record Swapping has been undone.");
                    getTargetedRecordSwappingView().updateVariableRows(swapping);
                    return;
                }
            }
            rankSwappings += "\n- " + VariableMu.printVariableNames(swapping.getOutputVariables());
        }
        
        getView().showMessage(String.format("Nothing to Undo: Targeted Record Swapping was not applied yet.\n"));
    }

    /**
     * Calculates the numerical rank swapping.
     */
    public void calculate() {
        if (!checkFields()) {
            return;
        }
        
        ArrayList<VariableMu> selectedSimilarVariables = getTargetedRecordSwappingView().getSelectedSimilarVariables();
        ArrayList<VariableMu> selectedHierarchyVariables = getTargetedRecordSwappingView().getSelectedHierarchyVariables();
        ArrayList<VariableMu> selectedRiskVariables = getTargetedRecordSwappingView().getSelectedRiskVariables();
        if (variablesAreUsed(selectedSimilarVariables)||variablesAreUsed(selectedHierarchyVariables)||variablesAreUsed(selectedRiskVariables)) {
            if (!getView().showConfirmDialog("One or more of the selected variables are already modified. Continue?")) {
                return;
            }
        }
        
        ArrayList<VariableMu> selectedVariables = new ArrayList<>();
        
        selectedVariables.addAll(selectedSimilarVariables);
        for (VariableMu variable : selectedHierarchyVariables){
            if (!selectedVariables.contains(variable)) selectedVariables.add(variable);
        }
        for (VariableMu variable : selectedRiskVariables){
            if (!selectedVariables.contains(variable)) selectedVariables.add(variable);
        }
        selectedVariables.add(getTargetedRecordSwappingView().getHHIDVar());
        
        TargetSwappingSpec targetSwapping = new TargetSwappingSpec(selectedSimilarVariables.size(),
                                                                   selectedHierarchyVariables.size(),
                                                                   selectedRiskVariables.size(),
                                                                   getTargetedRecordSwappingView().getSwaprate(),
                                                                   getTargetedRecordSwappingView().getkanonThreshold(),
                                                                   getTargetedRecordSwappingView().getSeed());
        try {
            targetSwapping.getOutputVariables().addAll(selectedVariables);
            targetSwapping.setReplacementFile(new ReplacementFile("TargetSwapping"));
            targetSwapping.calculateSimilarIndexes(selectedSimilarVariables);
            targetSwapping.calculateHierarchyIndexes(selectedHierarchyVariables);
            targetSwapping.calculateRiskIndexes(selectedRiskVariables);
            targetSwapping.calculateHHIdIndex(getTargetedRecordSwappingView().getHHIDVar());
            this.metadata.getReplacementSpecs().add(targetSwapping);
            getCalculationService().makeReplacementFile(this);
            getModel().getTargetSwappings().add(targetSwapping);
        } catch (ArgusException ex) {
            this.metadata.getReplacementSpecs().remove(targetSwapping);
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
