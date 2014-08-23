// TODO: Combine buttonlist methods
package muargus.controller;

import argus.model.ArgusException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.view.SpecifyMetadataView;

/**
 *
 * @author ambargus
 */
public class SpecifyMetadataController {
    
    SpecifyMetadataView view;
    MetadataMu metadata;
    MetadataMu metadataClone;
    ArrayList<String> list;

    private static final Logger logger = Logger.getLogger(SpecifyMetadataController.class.getName());

    /**
     * 
     * @param view 
     */
    public SpecifyMetadataController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new SpecifyMetadataView(parentView, true, this);
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
    public void generate() {
        //TODO: Wat moet hier gebeuren?

        //Generate generate; = new Generate(view, true);
        //generate.setVisible(true);
    }                                              

    /**
     * 
     */
    public void ok() {
        if (!this.metadata.equals(this.metadataClone))
        {
            try {
                this.metadataClone.verify();
            }
            catch (ArgusException ex) {
                JOptionPane.showMessageDialog(view,
                        ex.getMessage());
                return;
            }
            
            this.metadata = this.metadataClone;
            if (JOptionPane.showConfirmDialog(view, "Metadata has been changed. Save changes?", "Mu Argus",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                    this.metadata.write(fileChooser.getSelectedFile());
                }
            }
        } 
        this.view.setVisible(false);
    }                                        
    
    
    public MetadataMu getMetadata() {
        return this.metadata;
    }
    
    /**
     * 
     */
    public void cancel() {
        if (!this.metadata.equals(this.metadataClone)) {
            if (JOptionPane.showConfirmDialog(view, "All changes will be discarded. Are you sure?", "Mu Argus",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                return;
            }
        }
        this.view.setVisible(false);
    }
    
}
