/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import muargus.model.MakeProtectedFileModel;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.MakeProtectedFileView;

    

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileController {
    
    MakeProtectedFileView view;
    MakeProtectedFileModel model;
    MetadataMu metadata;
    SelectCombinationsModel selectCombinationsModel;
    /**
     * 
     * @param parentView
     * @param metadata 
     * @param model 
     * @param selectCombinationsModel 
     */
    public MakeProtectedFileController(java.awt.Frame parentView, MetadataMu metadata,
            MakeProtectedFileModel model, SelectCombinationsModel selectCombinationsModel) {
        this.model = model;
        this.view = new MakeProtectedFileView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);
        this.selectCombinationsModel = selectCombinationsModel;
    }
    
    public void showView(){
        this.view.setVisible(true);
    }
    
    public MakeProtectedFileModel getModel() {
        return this.model;
    }
    
    /**
     * 
     */
    public void makeFile() {                                               
        view.setVisible(false);
    }                                              

    public SelectCombinationsModel getSelectCombinationsModel() {
        return selectCombinationsModel;
    }
    
    /**
     * 
     */
    public void noSuppression() {                                                      
        // TODO add your handling code here:
    }                                                     
    
    /**
     * 
     */
    public void useWeight() {                                                  
        // TODO add your handling code here:
    }                                                 
    
    /**
     * 
     */
    public void useEntropy() {                                                   
        // TODO add your handling code here:
    }                                                  
    
    /**
     * 
     */
    public void keepInSafeFile() {                                                       
        // TODO add your handling code here:
    }                                                      
    
    /**
     * 
     */
    public void changeIntoSequenceNumber() {                                                                 
        // TODO add your handling code here:
    }                                                                
    
    /**
     * 
     */
    public void removeFromSafeFile() {                                                           
        // TODO add your handling code here:
    }                                                          
    
    /**
     * 
     */
    public void writeRecordRandomOrder() {                                                            
        // TODO add your handling code here:
    }                                                           
    
    /**
     * 
     */
    public void cancel() {                                             
        // TODO add your handling code here:
    }                                            
    
    /**
     * 
     */
    public void suppressionWeight() {                                                             
        // TODO add your handling code here:
    }
    
}
