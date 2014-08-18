/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.SelectCombinationsView;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsController {
    
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
        view.setVisible(false);
        CMuArgCtrl c = new CMuArgCtrl();
        boolean result = c.SetNumberVar(metadata.getVariables().size());
        if (!result)
            throw new ArgusException("Insufficient memory");
        for (int index=0; index < metadata.getVariables().size(); index++) {
            VariableMu variable = metadata.getVariables().get(index);
            c.SetVariable(index,
                    variable.getStartingPosition(),
                    variable.getVariableLength(),
                    variable.getDecimals(),
                    variable.getMissing(0),
                    variable.getMissing(1),
                    variable.isHouse_id(),
                    variable.isHousehold(),
                    variable.isCategorical(),
                    variable.isNumeric(),
                    variable.isWeight(),
                    metadata.getVariables().indexOf(variable.getRelatedVariable())); //TODO: handle error
        }
        int[] errorCodes = new int[1];
        int[] lineNumbers = new int[1];
        int[] varIndex = new int[1];
        result = c.ExploreFile(metadata.getFileNames().getDataFileName(),
                errorCodes,
                lineNumbers,
                varIndex);
        //TODO handle error
        
        result = c.SetNumberTab(this.model.getTables().size());
        //TODO handle error
        
        for (int index=0; index < model.getTables().size(); index++) {
            TableMu table = model.getTables().get(index);
            c.SetTable(index,
                    table.getThreshold(),
                    table.getVariables().size(),
                    getVarIndices(table),
                    false, //TODO
                    1); //TODO
        }
    }
    
    private int[] getVarIndices(TableMu table) {
        return new int[] {0}; //TODO
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
