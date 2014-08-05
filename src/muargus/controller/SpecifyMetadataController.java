// TODO: Combine buttonlist methods
package muargus.controller;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import muargus.view.SpecifyMetadataView;

/**
 *
 * @author ambargus
 */
public class SpecifyMetadataController {
    
    SpecifyMetadataView view;
    ArrayList<String> list;

    /**
     * 
     * @param view 
     */
    public SpecifyMetadataController(SpecifyMetadataView view) {
        this.view = view;
        this.list = null;
        //setList(list);
        
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
        view.setVisible(false);
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
