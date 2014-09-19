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
import muargus.model.MicroaggregationSpec;
import muargus.model.Microaggregation;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.VariableMu;
import muargus.view.MicroaggregationView;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ambargus
 */
public class MicroaggregationController extends ControllerBase<Microaggregation> {
    private final MetadataMu metadata;
    private final boolean numerical;

    public MicroaggregationController(java.awt.Frame parentView, MetadataMu metadata, boolean numerical) {
        this.numerical = numerical;
        super.setView(new MicroaggregationView(parentView, true, this));
        this.metadata = metadata;
        fillModel(numerical);
        getView().setMetadata(this.metadata);
    }
    
    private void fillModel(boolean numerical) {
        Microaggregation model = metadata.getCombinations().getMicroaggregation(numerical);
        if (model.getVariables().isEmpty()) {
            for (VariableMu variable : this.metadata.getVariables()) {
                if (isNumerical() ? variable.isNumeric() : variable.isCategorical()) {
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
            MicroaggregationSpec microaggregation = getModel().getMicroaggregations().get(getModel().getMicroaggregations().size()-1);
            try {
                FileUtils.copyFile(new File(microaggregation.getReplacementFile().getInputFilePath()), 
                        new File(microaggregation.getReplacementFile().getOutputFilePath()));
                getView().showMessage("Micro aggregation successfully completed");
                getView().setProgress(0);
                getView().showStepName("");
                getMicroaggregationView().updateVariableRows(microaggregation);
            }
            catch (IOException ex) {
                getView().showErrorMessage(new ArgusException(ex.getMessage()));
            }
    }
    
    public boolean isNumerical() {
        return numerical;
    }
    
    private MicroaggregationView getMicroaggregationView() {
        return (MicroaggregationView)getView();
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
        ArrayList<VariableMu> selected = getMicroaggregationView().getSelectedVariables();
        if (selected.isEmpty()) {
            return;
        }
        if (!getView().showConfirmDialog(String.format("The micro aggregation involving %s will be removed. Continue?",
                printVariableNames(selected)))) {
            return;
        }
        for (MicroaggregationSpec microaggregation : getModel().getMicroaggregations()) {
            if (microaggregation.getVariables().size() == selected.size()) {
                boolean difference = false;
                for (VariableMu variable : microaggregation.getVariables()) {
                    if (!selected.contains(variable)) {
                        difference = true;
                        break;
                    }
                }
                if (!difference) {
                    getModel().getMicroaggregations().remove(microaggregation);
                    this.metadata.getReplacementSpecs().remove(microaggregation);
                    getMicroaggregationView().updateVariableRows(microaggregation);
                    //TODO: remove temporary files?
                    return;
                }
            }
        }
        getView().showMessage(String.format("Micro aggregation involving %s not found", printVariableNames(selected)));
    }
    
    public void calculate() {
        ArrayList<VariableMu> selectedVariables = getMicroaggregationView().getSelectedVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        try {
            MicroaggregationSpec microaggregation = new MicroaggregationSpec(
                    getMicroaggregationView().getMinimalNumberOfRecords(),
                    getMicroaggregationView().getOptimal(),
                    numerical);
            microaggregation.getVariables().addAll(selectedVariables);
            microaggregation.setReplacementFile(new ReplacementFile("Microaggregation"));
            getModel().getMicroaggregations().add(microaggregation);
            this.metadata.getReplacementSpecs().add(microaggregation);
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
