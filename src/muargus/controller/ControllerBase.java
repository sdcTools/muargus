/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import muargus.MuARGUS;
import muargus.view.DialogBase;

/**
 *
 * @author pibd05
 */
public class ControllerBase implements PropertyChangeListener {

    private DialogBase viewBase;
    private String stepName;
    
    public ControllerBase() {
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
                if (success) {
                    doNextStep();
                }
                break;
            case "error":
                //the new value always contains an ArgusException with the error details
                viewBase.showErrorMessage((ArgusException)pce.getNewValue());
                break;
        }
    }
    
    protected void setView(DialogBase view) {
        this.viewBase = view;
    }
    
    protected DialogBase getView() {
        return this.viewBase;
    }
    
    protected void doNextStep() {
        //Base class implementation is empty
    }
    
    private void setCurrentStepName(String stepName) {
        this.stepName = stepName;
        viewBase.showStepName(this.stepName);
    }
        
        
    
}
