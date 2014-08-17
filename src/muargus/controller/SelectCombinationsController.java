/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.SelectCombinationsView;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsController {
    
    SelectCombinationsView view;
    SelectCombinationsModel model;
    MetadataMu metadata;
    MetadataMu metadataClone;
    ArrayList<String> list;
    
    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());

    public SelectCombinationsController(java.awt.Frame parentView, MetadataMu metadata, SelectCombinationsModel model) {
        this.model = model;
        this.view = new SelectCombinationsView(parentView, true, this, this.model);
        this.metadata = metadata;
        this.metadataClone = new  MetadataMu(metadata);
        this.view.setMetadataMu(this.metadataClone);
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
     */
    public void calculateTables() {                                                      
        view.setVisible(false);
    }                                                     

    /**
     * 
     */
    public void moveToSelected() {                                                     
        // TODO add your handling code here:
    }                                                    

    /**
     * 
     */
    public void removeFromSelected() {                                                         
        // TODO add your handling code here:
    }                                                        

    /**
     * 
     */
    public void removeAllFromSelected() {                                                            
        // TODO add your handling code here:
    }                                                           

    /**
     * 
     */
    public void addRow() {                                             
        // TODO add your handling code here:
    }                                            

    /**
     * 
     */
    public void removeRow() {                                                
        // TODO add your handling code here:
    }                                               

    /**
     * 
     */
    public void automaticSpecification() {                                                             
        // TODO add your handling code here:
    }                                                            

    /**
     * 
     */
    public void clear() {                                            
        // TODO add your handling code here:
    }                                           

    /**
     * 
     */
    public void setTableRiskModel() {                                                        
        // TODO add your handling code here:
    }                                                       

    /**
     * 
     */
    public void cancel() {                                             
        view.setVisible(false);
    } 
    
}
