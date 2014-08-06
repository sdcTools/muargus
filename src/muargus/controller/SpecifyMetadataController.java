// TODO: Combine buttonlist methods
package muargus.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
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
        try {
        this.metadataClone = (MetadataMu) metadata.clone();
        }
        catch (CloneNotSupportedException ex) {
                        logger.log(Level.SEVERE, null, ex);
        }
        this.view.setMetadataMu(this.metadataClone  );
            
        //this.view = view;
        //this.list = null;
        //setList(list);
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
        //Generate generate; = new Generate(view, true);
        //generate.setVisible(true);
    }                                              

    /**
     * 
     */
    public void ok() {
        if (!this.metadata.equals(this.metadataClone))
        {
        }
        this.view.setVisible(false);
    }                                        
    
    /**
     * 
     */
    public void moveUp() {                                             
        // TODO add your handling code here:
    }                                            

    /**
     * 
     */
    public void variables() {                                                  
        // TODO add your handling code here:
    }                                                 

    /**
     * 
     */
    public void newButton() {                                          
        // TODO add your handling code here:
    }                                         

    /**
     * 
     */
    public void delete() {                                             
        // TODO add your handling code here:
    }                                            

    /**
     * 
     */
    public void cancel() {                                             
        // TODO add your handling code here:
    }                                            

    /**
     * 
     */
    public void codelistfileButton() {                                                   
        // TODO add your handling code here:
    }                                                  

    /**
     * 
     */
    public void moveDown() {                                               
        // TODO add your handling code here:
    }                                              

    /**
     * 
     */
    public void hhIdentifier() {                                                     
        // TODO add your handling code here:
    }                                                    

    /**
     * 
     */
    public void hhvariable() {                                                   
        // TODO add your handling code here:
    }                                                  

    /**
     * 
     */
    public void weight() {                                               
        // TODO add your handling code here:
    }                                              

    /**
     * 
     */
    public void other() {                                              
        // TODO add your handling code here:
    }                                             

    /**
     * 
     */
    public void categorical() {                                                 
        // TODO add your handling code here:
    }                                                

    /**
     * 
     */
    public void numerical() {                                               
        // TODO add your handling code here:
    }                                              

    /**
     * 
     */
    public void truncationAllowed() {                                                       
        // TODO add your handling code here:
    }                                                      

    /**
     * 
     */
    public void codelistfile() {                                                  
        // TODO add your handling code here:
    }
    
}
