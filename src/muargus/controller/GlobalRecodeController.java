/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import muargus.view.GlobalRecodeView;

/**
 *
 * @author ambargus
 */
public class GlobalRecodeController {
    
    GlobalRecodeView view;

    /**
     * 
     * @param view 
     */
    public GlobalRecodeController(GlobalRecodeView view) {
        this.view = view;
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
