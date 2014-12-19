/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.util.ArrayList;
import muargus.extern.dataengine.Numerical;
import muargus.model.MetadataMu;
import muargus.model.NumericalRankSwapping;
import muargus.model.RankSwappingSpec;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.VariableMu;
import muargus.view.NumericalRankSwappingView;

/**
 * The Controller class of the NumericalRankSwapping screen.
 *
 * @author Statistics Netherlands
 */
public class NumericalRankSwappingController extends ControllerBase<NumericalRankSwapping> {

    private final MetadataMu metadata;

    /**
     * Constructor for the NumericalRankSwappingController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public NumericalRankSwappingController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new NumericalRankSwappingView(parentView, true, this));
        this.metadata = metadata;
        fillModel();
        getView().setMetadata(metadata);
    }

    /**
     * Gets the model and fills the model with the numeric variables if the
     * model is empty.
     */
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

    /**
     * Does the next step if the previous step was succesful.
     *
     * @param success Boolean indicating whether the previous step was
     * succesful.
     */
    @Override
    protected void doNextStep(boolean success) {
        RankSwappingSpec swapping = getModel().getRankSwappings().get(getModel().getRankSwappings().size() - 1);
        Numerical num = new Numerical();
        int[] errorCode = new int[1];
        num.DoRankSwap(swapping.getReplacementFile().getInputFilePath(),
                swapping.getReplacementFile().getOutputFilePath(),
                muargus.MuARGUS.getDefaultSeparator(),
                swapping.getOutputVariables().size(),
                (int) swapping.getPercentage(),
                errorCode);
        if (errorCode[0] != 0) {
            getView().showErrorMessage(new ArgusException("Error during rank swapping"));
            this.metadata.getReplacementSpecs().remove(swapping);
            getModel().getRankSwappings().remove(swapping);
        } else {
            getView().showMessage("RankSwapping successfully completed");
            SystemUtils.writeLogbook("Numerical rank swapping has been done.");
        }
        getView().setProgress(0);
        getView().showStepName("");
        getNumericalRankSwappingView().updateVariableRows(swapping);
    }

    /**
     * Gets the NumericalRankSwapping view.
     *
     * @return NumericalRankSwappingView
     */
    private NumericalRankSwappingView getNumericalRankSwappingView() {
        return (NumericalRankSwappingView) getView();
    }

    /**
     * Checks whether the value for the percentage is valid.
     *
     * @return Boolean indicating whether the value for the percentage is valid.
     */
    private boolean checkFields() {
        double percentage = getNumericalRankSwappingView().getPercentage();
        if (Double.isNaN(percentage) || percentage <= 0 || percentage > 100) {
            getView().showErrorMessage(new ArgusException("Illegal value for the Rank swapping percentage"));
            return false;
        }
        return true;
    }

    /**
     * Undo's the numerical rank swapping
     */
    public void undo() {
        ArrayList<VariableMu> selected = getNumericalRankSwappingView().getSelectedVariables();
        if (selected.isEmpty()) {
            return;
        }
        if (!getView().showConfirmDialog(String.format("The rank swapping involving %s will be removed. Continue?",
                VariableMu.printVariableNames(selected)))) {
            return;
        }
        String rankSwappings = (getModel().getRankSwappings().size()>1)? "s are:" : " is:";
        for (RankSwappingSpec swapping : getModel().getRankSwappings()) {
            if (swapping.getOutputVariables().size() == selected.size()) {
                boolean difference = false;
                for (VariableMu variable : swapping.getOutputVariables()) {
                    if (!selected.contains(variable)) {
                        difference = true;
                        break;
                    }
                }
                if (!difference) {
                    getModel().getRankSwappings().remove(swapping);
                    this.metadata.getReplacementSpecs().remove(swapping);
                    SystemUtils.writeLogbook("Numerical rank swapping has been undone.");
                    getNumericalRankSwappingView().updateVariableRows(swapping);
                    return;
                }
            }
            rankSwappings += "\n- " + VariableMu.printVariableNames(swapping.getOutputVariables());
        }
        
        getView().showMessage(String.format("Rank swapping involving %s not found.\n"
                + "The available rank swapping" + rankSwappings, VariableMu.printVariableNames(selected)));
    }

    /**
     * Calculates the numerical rank swapping.
     */
    public void calculate() {
        if (!checkFields()) {
            return;
        }
        ArrayList<VariableMu> selectedVariables = getNumericalRankSwappingView().getSelectedVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        RankSwappingSpec rankSwapping = new RankSwappingSpec(getNumericalRankSwappingView().getPercentage());
        try {
            rankSwapping.getOutputVariables().addAll(selectedVariables);
            rankSwapping.setReplacementFile(new ReplacementFile("RankSwapping"));
            this.metadata.getReplacementSpecs().add(rankSwapping);
            getCalculationService().makeReplacementFile(this);
            getModel().getRankSwappings().add(rankSwapping);
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
