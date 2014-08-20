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
import muargus.model.UnsafeCodeInfo;
import muargus.model.UnsafeInfo;
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
    
    static {
        System.loadLibrary("libmuargusdll");
    }
    
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
        view.setVisible(false);
        CMuArgCtrl c = new CMuArgCtrl();
        boolean result = c.SetNumberVar(model.getVariablesInTables().size());
        if (!result)
            throw new ArgusException("Insufficient memory");
        
        for (int index=0; index < model.getVariablesInTables().size(); index++) {
            VariableMu variable = model.getVariablesInTables().get(index);
            result = c.SetVariable(index+1,
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
                    1 + metadata.getVariables().indexOf(variable.getRelatedVariable())); //TODO: handle error
        }
        int[] errorCodes = new int[1];
        int[] lineNumbers = new int[1];
        int[] varIndexOut = new int[1];
        result = c.ExploreFile(metadata.getFileNames().getDataFileName(),
                errorCodes,
                lineNumbers,
                varIndexOut);
        //TODO handle error
        int x = this.model.getTables().size();
        result = c.SetNumberTab(x); //this.model.getTables().size());
        //TODO handle error
        
        for (int index=0; index < model.getTables().size(); index++) {
            TableMu table = model.getTables().get(index);
            int a = index+1;
            int t = table.getThreshold();
            int d = table.getVariables().size();
            int[] i = getVarIndices(table);
            boolean b = table.isRiskModel();
            result = c.SetTable(    
                    //index+1,
                    //table.getThreshold(),
                    //table.getVariables().size(),
                    //getVarIndices(table),
                    //table.isRiskModel(),
                    a,t,d,i,b, 1); //TODO
            if (!result) {
                throw new ArgusException("Error during SetTable");
            }
                
            //TODO: handle error
        }
        
        result = c.ComputeTables(errorCodes, varIndexOut);
        //TODO: handle error
        
        model.clearUnsafe();
        for (int varIndex=0; varIndex < model.getVariablesInTables().size(); varIndex++) {
            VariableMu variable = model.getVariablesInTables().get(varIndex);
            int[] nDims = new int[] {0};
            int[] unsafeCount = new int[model.getVariablesInTables().size()];
            result = c.UnsafeVariable(varIndex+1, nDims, unsafeCount);
            UnsafeInfo unsafe = new UnsafeInfo();
            unsafe.setUnsafeCombinations(nDims[0], unsafeCount);
            model.setUnsafe(variable, unsafe);
            
            int[] nCodes = new int[]{0};
            result = c.UnsafeVariablePrepare(varIndex+1, nCodes);
            int[] isMissing = new int[]{0};
            int[] freq = new int[]{0};
            String[] code = new String[1];
            for (int codeIndex=0; codeIndex < nCodes[0]; codeIndex++) {
                result = c.UnsafeVariableCodes(varIndex+1, 
                        codeIndex+1,
                        isMissing,
                        freq,
                        code,
                        nDims,
                        unsafeCount);
                UnsafeCodeInfo codeInfo = new UnsafeCodeInfo(code[0], isMissing[0] != 0);
                codeInfo.setFrequency(freq[0]);
                codeInfo.setUnsafeCombinations(nDims[0], unsafeCount);
                unsafe.addUnsafeCodeInfo(codeInfo);
            }
            result = c.UnsafeVariableClose(varIndex+1);
        }
            
    }
    
    private int[] getVarIndices(TableMu table) {
        int[] indices = new int[table.getVariables().size()];
        for (int index=0; index < indices.length; index++) {
            indices[index] = 1+this.model.getVariablesInTables().indexOf(table.getVariables().get(index));
        }
        return indices;
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
