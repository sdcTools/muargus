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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import muargus.io.RWriter;
import muargus.model.MetadataMu;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.SyntheticDataSpec;
import muargus.model.VariableMu;
import muargus.view.SyntheticDataView;

/**
 * Controller class of the Synthetic data screen.
 *
 * @author Statistics Netherlands
 */
public class SyntheticDataController extends ControllerBase<SyntheticDataSpec> {

    private final MetadataMu metadata;
    private final SyntheticDataView view;

    /**
     * Constructor for the RController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SyntheticDataController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new SyntheticDataView(parentView, true, this);
        super.setView(this.view);
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getSyntheticData());
        fillModel();
        getView().setMetadata(this.metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     * Generates the synthetic data. Gets the SyntheticDataSpec instance, cleans
     * it, adds all relevant variables, creates a new replacement file and
     * writes the required files (Alpha, r-script, data file and .bat file).
     * After writeSyntheticData, the doNextStep is activated.
     *
     * @return Boolean indicating whether the synthetic data was succesfully
     * generated.
     */
    public boolean runSyntheticData() {
        try {
            SyntheticDataSpec syntheticData = getModel();
            clean(syntheticData);
            syntheticData.getSensitiveVariables().addAll(getSensitiveVariables());
            syntheticData.getNonSensitiveVariables().addAll(getNonSensitiveVariables());
            syntheticData.setReplacementFile(new ReplacementFile("SyntheticData"));
            this.metadata.getReplacementSpecs().add(syntheticData);
            RWriter.writeAlpha(syntheticData);
            RWriter.writeSynthetic(syntheticData);
            writeSyntheticData();
            SystemUtils.writeLogbook("R input files for synthetic data have been generated.");
            return true;
        } catch (ArgusException ex) {
            return false;
        }

    }

    /**
     * Cleans the synthetic data and removes previous replacement file
     * instances.
     *
     * @param syntheticData SyntheticDataSpec containing the replacement file.
     */
    private void clean(SyntheticDataSpec syntheticData) {
        syntheticData.getSensitiveVariables().clear();
        syntheticData.getNonSensitiveVariables().clear();
        ArrayList<ReplacementSpec> r = this.metadata.getReplacementSpecs();
        for (int i = r.size() - 1; i >= 0; i--) {
            if (r.get(i) instanceof SyntheticDataSpec) {
                r.remove(i);
            }
        }
    }

    /**
     * Gets the selected sensitive variables.
     *
     * @return List of VariableMu's containing the selected sensitive variables.
     */
    private List<VariableMu> getSensitiveVariables() {
        return ((SyntheticDataView) getView()).getSelectedSensitiveVariables();
    }

    /**
     * Gets the selected non-sensitive variables.
     *
     * @return List of VariableMu's containing the selected non-sensitive
     * variables.
     */
    private List<VariableMu> getNonSensitiveVariables() {
        return ((SyntheticDataView) getView()).getSelectedNonSensitiveVariables();
    }

    /**
     * Does the next step if the previous step was succesful.
     *
     * @param success Boolean indicating whether the previous step was
     * succesful.
     */
    @Override
    protected void doNextStep(boolean success) {
        try {
            RWriter.adjustSyntheticData(getModel());
            runR();
            if (RWriter.adjustSyntheticOutputFile(getModel())) {
                getView().showMessage("Synthetic data successfully generated");
                SystemUtils.writeLogbook("Synthetic data has been generated.");
            } else {
                getView().showErrorMessage(new ArgusException("No synthetic data generated. Check if R is properly installed.")); //TODO: beter foutmelding als R niet goed is geinstalleerd.
            }
            this.view.enableRunSyntheticDataButton(true);
        } catch (ArgusException ex) {
            this.getView().showErrorMessage(ex);
        }
    }

    /**
     * Runs the .bat file. The .bat file runs the R-script which generates the
     * synthetic data.
     */
    private void runR() throws ArgusException {
        try {
            ArrayList<String> arguments = new ArrayList<>();
            arguments.add("R");
            arguments.add("CMD");
            arguments.add("BATCH");
            arguments.add(getModel().getrScriptFile().getAbsolutePath());
            ProcessBuilder builder = new ProcessBuilder(arguments);

            Process p = builder.start();
            p.waitFor();
        } catch (InterruptedException | IOException ex) {
            throw new ArgusException("Error running R script: " + ex.getMessage());
        }
    }

    /**
     * Writes the synthetic data in a replacement file.
     */
    private void writeSyntheticData() {
        getCalculationService().makeReplacementFile(this);
    }

    /**
     * Gets the model and fills the model with the numeric variables if the
     * model is empty.
     */
    private void fillModel() {
        getModel().clear();
        for (VariableMu variable : this.metadata.getVariables()) {
            if (variable.isNumeric()) {
                getModel().getAllVariables().add(variable);
            }
        }
    }

}
