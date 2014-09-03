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
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.view.SelectCombinationsView;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsController implements PropertyChangeListener{
    
    SelectCombinationsView view;
    Combinations modelClone;
    MetadataMu metadata;
    //ArrayList<String> list;
        
    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());

    public SelectCombinationsController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new SelectCombinationsView(parentView, true, this);
        this.metadata = metadata;
        
        getSettings();
        this.modelClone = new Combinations(this.metadata.getCombinations());

        this.view.setMetadataMu(this.metadata); // clone Combinations
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
    
    /**
     * 
     * @throws argus.model.ArgusException
     */
    public void calculateTables() throws ArgusException {
        this.view.enableCalculateTables(false);
        saveSettings();
        if(this.metadata.getCombinations().getTables().size() > 0){
            ;//this.clearData();
        }
        this.metadata.setCombinations(this.modelClone);
        CalculationService service = MuARGUS.getCalculationService();
        service.setPropertyChangeListener(this);
        service.calculateTables(this.metadata);
        //service.getUnsafeCombinations(this.model, this.metadata);
    }
    
    private void getSettings() {
        int[] thresholds = new int[MuARGUS.MAXDIMS];
        for (int t=1; t <= MuARGUS.MAXDIMS; t++) {
            thresholds[t-1] = SystemUtils.getRegInteger("general", "threshold" + Integer.toString(t), 1);
        }
        this.metadata.getCombinations().setThresholds(thresholds);
    }
    
    private void saveSettings() {
        int[] thresholds = this.metadata.getCombinations().getThresholds();
        if (thresholds == null || thresholds.length < MuARGUS.MAXDIMS) {
            return;
        }
        for (int t=1; t <= MuARGUS.MAXDIMS; t++) {
            SystemUtils.putRegInteger("general", "threshold" + Integer.toString(t), thresholds[t-1]); 
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
    
//    public void clearData(){
//        this.controller.clearDataAfterSelectCombinations();
//    }
}
