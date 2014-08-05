// TODO: change the open view methods to one method that takes an argument Classname

package muargus.controller;

import argus.view.DialogOpenMicrodata;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import muargus.view.GlobalRecodeView;
import muargus.view.MainFrameView;
import muargus.view.MakeProtectedFileView;
import muargus.view.OpenMicrodataView;
import muargus.view.SelectCombinationsView;
import muargus.view.SpecifyMetadataView;
import muargus.view.ViewReportView;

/**
 *
 * @author ambargus
 */
public class MainFrameController {
    
    MainFrameView view;

    /**
     * 
     * @param view 
     */
    public MainFrameController(MainFrameView view) {
        this.view = view;
    }
       
    /**
     * 
     */
    public void openMicrodata() {    
        OpenMicrodataView view = new OpenMicrodataView(this.view, true);
        //DialogOpenMicrodata view = new DialogOpenMicrodata(this.view, true);
        view.setVisible(true);
    }                                                     

    /**
     * 
     */
    public void exit() {                                             
        System.exit(0);
    }                                            

    /**
     * 
     */
    public void specifyMetaData() {                                                 
        SpecifyMetadataView view = new SpecifyMetadataView(this.view, true);
        view.setVisible(true);
    }                                                

    /**
     * 
     */
    public void specifyCombinations() {                                                     
        SelectCombinationsView view = new SelectCombinationsView(this.view, true);
        view.setVisible(true);
    }                                                    

    /**
     * 
     */
    public void showTableCollection() {                                                            

    }                                                           

    /**
     * 
     */
    public void globalRecode() {                                                     
        GlobalRecodeView view = new GlobalRecodeView(this.view, true);
        view.setVisible(true);
    }                                                    

    /**
     * 
     */
    public void pramSpecification() {                                                          

    }                                                         

    /**
     * 
     */
    public void individualRiskSpecification() {                                                                    

    }                                                                   

    /**
     * 
     */
    public void householdRiskSpecification() {                                                                   

    }                                                                  

    /**
     * 
     */
    public void numericalVariables() {                                                           

    }                                                          

    /**
     * 
     */
    public void numericalMicroaggregation() {                                                                  

    }                                                                 

    /**
     * 
     */
    public void numericalRankSwapping() {                                                              

    }                                                             

    /**
     * 
     */
    public void makeProtectedFile() {                                                          
        MakeProtectedFileView view = new MakeProtectedFileView(this.view, true);
        view.setVisible(true);
    }                                                         

    /**
     * 
     */
    public void viewReport() {                                                   
        ViewReportView view = new ViewReportView(this.view, true);
        view.setVisible(true);
    }                                                  

    /**
     * 
     */
    public void contents() {                                                 

    }                                                

    /**
     * 
     */
    public void news() {                                             

    }                                            

    /**
     * 
     */
    public void about() {                                              

    }                                                                                   

    /**
     * 
     */
    public void manual() {                                               
        try 
        {  
            Desktop.getDesktop().browse(new URL("http://neon.vb.cbs.nl/casc/Software/MuManual4.2.pdf").toURI());
        }           
        catch (URISyntaxException | IOException e) {}
    } 
    
}
