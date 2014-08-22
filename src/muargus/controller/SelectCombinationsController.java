/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.model.Metadata;
//import static com.sun.glass.ui.Cursor.setVisible;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.extern.dataengine.IProgressListener;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.model.TableMu;
import muargus.model.UnsafeCodeInfo;
import muargus.model.UnsafeInfo;
import muargus.model.VariableMu;
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
     */
    public void calculateTables() throws ArgusException {
        this.model = this.modelClone;
        TableService service = new TableService();
        service.setPropertyChangeListener(this);
        service.calculateTables(this.model, this.metadata);
        //service.getUnsafeCombinations(this.model, this.metadata);
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
