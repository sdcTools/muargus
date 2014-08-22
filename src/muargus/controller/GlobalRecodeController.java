/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import muargus.model.GlobalRecodeModel;
import muargus.model.MetadataMu;
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
     */
    public GlobalRecodeController(java.awt.Frame parentView, MetadataMu metadata, GlobalRecodeModel model) {
        this.model = model;
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
    public void read() {                                           
        // TODO add your handling code here:
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
