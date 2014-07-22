/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import muargus.model.OpenMicrodataModel;
import muargus.view.OpenMicrodataView;

/**
 *
 * @author ambargus
 */
public class OpenMicrodataController {
    
    OpenMicrodataView view;
    
    /**
     * 
     * @param view 
     */
    public OpenMicrodataController(OpenMicrodataView view){
        this.view = view;
    }
    
    /**
     * 
     */
    public void clear() {                                            
        view.setMicrodataText("");
        view.setMetadataText("");
        view.setInstructionVisible(false);
    }        
    
    /**
     * 
     */
    public void cancel() {                                             
        view.setInstructionVisible(false);
    }  
    
    // TODO: test if the path equals the previous path if the previous is not empty. If so give a warning. 
    /**
     * 
     */
    public void finish() {                                         
        if(!view.getMicrodataText().equals("")){
            OpenMicrodataModel.setMicrodataPath(view.getMicrodataText());
            OpenMicrodataModel.setMetadataPath(view.getMetadataText());
            OpenMicrodataModel.printVariables();
        }
        view.setVisible(false);
    }                                        

    // TODO: this needs to be written way better. It is currently just to test and try some stuff. 
    /**
     * 
     */
    public void chooseMetadataFile() {                                               
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Metadata (*.rda)", "rda"));
        String hs = "C:\\Program Files\\MU_ARGUS\\data";
        File file = new File(hs);
        fileChooser.setCurrentDirectory(file);
        fileChooser.showOpenDialog(null);
        
        String filename;
        File f = fileChooser.getSelectedFile();
        if(fileChooser.getSelectedFile() == null)
            filename = "";
        else {
            filename = f.getAbsolutePath();
            view.setInstructionVisible(true);
        }
        view.setMetadataText(filename);
        view.setMicrodataText(filename.substring(0, filename.length() - 3) + "asc");
    }                                              

    // TODO: this needs to be written way better. It is currently just to test and try some stuff. 
    /**
     * 
     */
    public void chooseMicrodataFile() {                                                
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Microdata (*.asc)", "asc"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microdata (*.dat)", "dat"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("SPSS system file (*.sav)", "sav"));
        String hs = "C:\\Program Files\\MU_ARGUS\\data";
        File file = new File(hs);
        fileChooser.setCurrentDirectory(file);
        fileChooser.showOpenDialog(null);
        
        String filename;
        File f = fileChooser.getSelectedFile();
        if(fileChooser.getSelectedFile() == null)
            filename = "";
        else {
            filename = f.getAbsolutePath();
            view.setInstructionVisible(true);
        }
        view.setMetadataText(filename.substring(0, filename.length() - 3) + "rda");
        view.setMicrodataText(filename);
    }                                                                         
}
