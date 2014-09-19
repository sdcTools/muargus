/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import muargus.CalculationService;
import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import muargus.view.DialogBase;

/**
 *
 * @author pibd05
 */
public class ControllerBase<T> implements PropertyChangeListener {

    private DialogBase viewBase;
    private String stepName;
    private T model;
    
    public ControllerBase() {
    }
    
    protected T getModel() {
        return model;
    }
    
    protected void setModel(T model) {
        this.model = model;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case "stepName":
                setCurrentStepName(pce.getNewValue().toString()); 
                break;                
            case "progress":
                this.viewBase.setProgress((int)pce.getNewValue());
                break;
            case "result":
                boolean success = "success".equals(pce.getNewValue());
                doNextStep(success);
                break;
            case "error":
                //the new value always contains an ArgusException with the error details
                viewBase.showErrorMessage((ArgusException)pce.getNewValue());
                break;
        }
    }
    
    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        getView().setVisible(true);
    }
    
    protected CalculationService getCalculationService() {
        return muargus.MuARGUS.getCalculationService();
    }
    
    protected void setView(DialogBase view) {
        this.viewBase = view;
    }
    
    protected DialogBase getView() {
        return this.viewBase;
    }
    
    protected void doNextStep(boolean success) {
        //Base class implementation is empty
    }
    
    private void setCurrentStepName(String stepName) {
        this.stepName = stepName;
        viewBase.showStepName(this.stepName);
    }

    protected String getStepName() {
        return stepName;
    }

}