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

import muargus.CalculationService;
import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import muargus.view.DialogBase;

/**
 * Base class of the controllers.
 *
 * @author Statistics Netherlands
 * @param <T> Generic model type
 */
public class ControllerBase<T> implements PropertyChangeListener {

    private DialogBase viewBase;
    private String stepName;
    private T model;

    /**
     * Gets the model class.
     *
     * @return Generic instance of the model class.
     */
    protected T getModel() {
        return model;
    }

    /**
     * Sets the Model class.
     *
     * @param model Generic instance of the model class.
     */
    protected void setModel(T model) {
        this.model = model;
    }

    /**
     * Property change action. Executes the appropriate command after a
     * proertyChangeEvent has been fired.
     *
     * @param pce PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case "stepName":
                setCurrentStepName(pce.getNewValue().toString());
                break;
            case "progress":
                this.viewBase.setProgress((int) pce.getNewValue());
                break;
            case "result":
                boolean success = "success".equals(pce.getNewValue());
                doNextStep(success);
                break;
            case "error":
                //the new value always contains an ArgusException with the error details
                this.viewBase.showErrorMessage((ArgusException) pce.getNewValue());
                break;
        }
    }

    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        getView().setVisible(true);
    }

    /**
     * Gets the calculation service. The CalculationService contains methods
     * that call external dll's.
     *
     * @return CalculationsService instance.F
     */
    protected CalculationService getCalculationService() {
        return muargus.MuARGUS.getCalculationService();
    }

    /**
     * Sets the view belonging to the controller.
     *
     * @param view View belonging to the controller.
     */
    protected void setView(DialogBase view) {
        this.viewBase = view;
    }

    /**
     * Gets the view belonging to the controller.
     *
     * @return View belonging to the controller.
     */
    protected DialogBase getView() {
        return this.viewBase;
    }

    /**
     * Does the next step if the previous step was succesful.
     *
     * @param success Boolean indicating whether the previous step was
     * succesful.
     */
    protected void doNextStep(boolean success) {
        //Base class implementation is empty
    }

    /**
     * Sets the name of the current step.
     *
     * @param stepName String containing the name of the current step.
     */
    private void setCurrentStepName(String stepName) {
        this.stepName = stepName;
        this.viewBase.showStepName(this.stepName);
    }

    /**
     * Gets the name of the current step.
     *
     * @return String containing the name of the current step.
     */
    protected String getStepName() {
        return this.stepName;
    }

}
