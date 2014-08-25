/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.SelectCombinationsView;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsController implements PropertyChangeListener{
    
    SelectCombinationsView view;
    SelectCombinationsModel model;
    SelectCombinationsModel modelClone;
    MetadataMu metadata;
    //ArrayList<String> list;
        
    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());

    public SelectCombinationsController(java.awt.Frame parentView, MetadataMu metadata, SelectCombinationsModel model) {
        this.model = model;
        this.getSettings();
        this.modelClone = new SelectCombinationsModel(model);

        this.view = new SelectCombinationsView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata); // clone SelectCombinationsModel
        this.view.setModel(this.modelClone);
    }
    
    public void showView() {
        this.view.setVisible(true);
    }
    
    public void setList(ArrayList<String> list){
        DefaultListModel model1 = new DefaultListModel();
        for(String s: list){
            model1.addElement(s);
        }
    }
    
    public SelectCombinationsModel getModel() {
        return this.model;
    }
    /**
     * 
     * @throws argus.model.ArgusException
     */
    public void calculateTables() throws ArgusException {
        this.model = this.modelClone;
        saveSettings();
        TableService service = new TableService();
        service.setPropertyChangeListener(this);
        service.calculateTables(this.model, this.metadata);
        //service.getUnsafeCombinations(this.model, this.metadata);
    }
    
    private void getSettings() {
        int[] thresholds = new int[MuARGUS.MAXDIMS];
        for (int t=1; t <= MuARGUS.MAXDIMS; t++) {
            thresholds[t-1] = SystemUtils.getRegInteger("general", "threshold" + Integer.toString(t), 1);
        }
        this.model.setThresholds(thresholds);
    }
    
    private void saveSettings() {
        if (this.model.getThresholds() == null || this.model.getThresholds().length < MuARGUS.MAXDIMS) {
            return;
        }
        for (int t=1; t <= MuARGUS.MAXDIMS; t++) {
            SystemUtils.putRegInteger("general", "threshold" + Integer.toString(t), this.model.getThresholds()[t]); // moet dit niet getThresholds()[t-1] zijn?
        }
    }
                                                                  

                                                          

    /**
     * 
     */
    public void automaticSpecification() {                                                             
        //het zou mooier zijn als de berekening niet in de view zou gebeuren
    }                                                            


    /**
     * 
     */
    public void cancel() {                                             
        view.setVisible(false);
    } 
 
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case "stepName":
                view.setStepName(pce.getNewValue());
                break;                
            case "progress":
                view.setProgress(pce.getNewValue());
                break;
            case "status":
                view.setVisible(pce.getNewValue() != "done");
        }
    }
}
