/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import muargus.model.GlobalRecodeModel;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.GlobalRecodeView;

/**
 *
 * @author ambargus
 */
public class GlobalRecodeController {
    
    GlobalRecodeView view;
    GlobalRecodeModel model;
    MetadataMu metadata;

    /**
     * 
     * @param parentView
     * @param metadata 
     * @param model 
     * @param selectCombinationsModel 
     */
    public GlobalRecodeController(java.awt.Frame parentView, MetadataMu metadata, 
            GlobalRecodeModel model, SelectCombinationsModel selectCombinationsModel) {
        this.model = model;
        this.model.setVariables(selectCombinationsModel.getVariablesInTables());
        this.view = new GlobalRecodeView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);
        //this.view = view;
    }
    
    public void showView() {
        this.view.setVisible(true);
    }
    
    public GlobalRecodeModel getModel() {
        return this.model;
    }
    
    
    /**
     * 
     */
    public void close() {                                            
        view.setVisible(false);
    }                                           

    /**
     * 
     */
    public void codelistRecode() {                                                     
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Codelist (*.cdl)", "cdl"));
        String hs = "C:\\Program Files\\MU_ARGUS\\data";
        File file = new File(hs);
        fileChooser.setCurrentDirectory(file);
        fileChooser.showOpenDialog(null);
        
        String filename;
        File f = fileChooser.getSelectedFile();
        if(fileChooser.getSelectedFile() == null)
            filename = "";
        else 
            filename = f.getAbsolutePath();        
        view.setCodelistText(filename);
    }                                                    

    /**
     * 
     */
    public void truncate() {                                               
        // TODO add your handling code here:
    }                                              

    /**
     * 
     */
    public void read(String path, RecodeMu recode) throws ArgusException {                                           
        File file = new File(path);
        try {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = "";
        Tokenizer tokenizer = new Tokenizer(reader);
        while ((line = tokenizer.nextLine()) != null) {
            String token = tokenizer.nextToken();
            if (!token.startsWith("<")) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            else if (token == "<MISSING>") {
                token = tokenizer.nextToken();
                recode.setMissing_1_new(token);
                token = tokenizer.nextToken();
                if (token != null)
                    recode.setMissing_2_new(token);
            }
            else if (token == "<CODELIST>") {
                recode.setCodeListFile(tokenizer.nextToken());
            }
            else {
                throw new ArgusException("Error reading file, invalid token: " + token);
            }
        }
        reader.close();
        recode.setGrcText(sb.toString());
        }
        catch (IOException ex) {
            throw new ArgusException("Error during reading file");
        }
        
                
    }                                          

    /**
     * 
     */
    public void apply() {                                            
        // TODO add your handling code here:
    }                                           

    /**
     * 
     */
    public void undo() {                                           
        // TODO add your handling code here:
    }      

}
