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
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import muargus.MuARGUS;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.extern.dataengine.IProgressListener;
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
    SelectCombinationsModel selectCombinationsModel;

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
        this.view = new GlobalRecodeView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);
        this.selectCombinationsModel = selectCombinationsModel;
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
    public void truncate(RecodeMu recode) throws ArgusException {  
        CMuArgCtrl c = MuARGUS.getMuArgCtrl();
        int index = this.model.getVariables().indexOf(recode.getVariable());
        boolean result = c.DoTruncate(index + 1, 1);
        if (!result)
            throw new ArgusException("Error during Truncate");
        applyRecode();
        recode.setTruncated(true);
    }                                              

    /**
     * 
     */
    public void read(String path, RecodeMu recode) throws ArgusException {                                           
        recode.setMissing_1_new(null);
        recode.setMissing_2_new(null);
        recode.setCodeListFile(null);
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            Tokenizer tokenizer = new Tokenizer(reader);
            while ((line = tokenizer.nextLine()) != null) {
                String token = tokenizer.nextToken();
                if (!token.startsWith("<")) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
                else if ("<MISSING>".equals(token)) {
                    token = tokenizer.nextToken();
                    recode.setMissing_1_new(token);
                    token = tokenizer.nextToken();
                    if (token != null)
                        recode.setMissing_2_new(token);
                }
                else if ("<CODELIST>".equals(token)) {
                    recode.setCodeListFile(tokenizer.nextToken());
                }
                else {
                    throw new ArgusException("Error reading file, invalid token: " + token);
                }
            }
            reader.close();
            recode.setGrcText(sb.toString());
            recode.setGrcFile(path);
            }
        catch (IOException ex) {
            throw new ArgusException("Error during reading file");
        }
        
                
    }                                          

    /**
     * 
     */
    public void apply(RecodeMu recode) throws ArgusException {
        CMuArgCtrl c = MuARGUS.getMuArgCtrl();
        int index = this.model.getVariables().indexOf(recode.getVariable());
        int[] errorType = new int[] {0};
        int[] errorLine = new int[] {0};
        int[] errorPos = new int[] {0};
        String[] warning = new String[1];
        boolean result = c.DoRecode(index+1, 
                recode.getGrcText(),
                recode.getMissing_1_new(),
                recode.getMissing_2_new(),
                errorType,
                errorLine,
                errorPos,
                warning);
        if (!result) {
            throw new ArgusException(warning[0]);
        }
        applyRecode();
        recode.setRecoded(true);
        view.showWarning(warning[0]); 
        
        
    }
    
    private void applyRecode() throws ArgusException {
        CMuArgCtrl c = MuARGUS.getMuArgCtrl();
        c.SetProgressListener(null);

        boolean result = c.ApplyRecode();
        if (!result) {
            throw new ArgusException("Error during Apply recode");
        }
        
        new TableService().getUnsafeCombinations(this.selectCombinationsModel, this.metadata);
        
            
        // TODO add your handling code here:
    }                                           

    /**
     * 
     */
    public void undo(RecodeMu recode) throws ArgusException {    
        CMuArgCtrl c = MuARGUS.getMuArgCtrl();
        c.SetProgressListener(null);
        int index = this.model.getVariables().indexOf(recode.getVariable());
        boolean result = c.UndoRecode(index + 1);
                if (!result) {
            throw new ArgusException("Error while undoing recode");
        }
        result = c.ApplyRecode();
        if (!result) {
            throw new ArgusException("Error during Apply recode");
        }
        recode.setRecoded(false);
        recode.setTruncated(false);
        
   }      
}
