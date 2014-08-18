// TODO: change the open view methods to one method that takes an argument Classname

package muargus.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.GlobalRecodeView;
import muargus.view.MainFrameView;
import muargus.view.MakeProtectedFileView;
import muargus.view.ViewReportView;

/**
 *
 * @author ambargus
 */
public class MainFrameController {
    
    
    //TODO: add all models here
    MainFrameView view;
    SelectCombinationsModel selectCombinationsModel;

    /**
     * 
     * @param view 
     */
    public MainFrameController(MainFrameView view) {
        this.view = view;
        selectCombinationsModel = new SelectCombinationsModel();
    }
       
    /**
     * 
     */
    public void openMicrodata() {    
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
    public void specifyMetaData(MetadataMu metadata) {                                                 
        SpecifyMetadataController controller = new SpecifyMetadataController(this.view, metadata);
        controller.showView();
        //view.setVisible(true);
    }   
    
    /**
     * 
     */
    public void specifyCombinations(MetadataMu metadata) {                                                     
        SelectCombinationsController controller = new SelectCombinationsController(this.view, metadata, selectCombinationsModel);
        controller.showView();
        //view.setVisible(true);
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
