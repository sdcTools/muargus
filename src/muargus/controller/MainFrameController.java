// TODO: change the open view methods to one method that takes an argument Classname

package muargus.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import muargus.model.GlobalRecodeModel;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.MainFrameView;
import muargus.view.MakeProtectedFileView;
import muargus.view.ViewReportView;

/**
 *
 * @author ambargus
 */
public class MainFrameController {
    
    MainFrameView view;
    
    MetadataMu metadata;
    SelectCombinationsModel selectCombinationsModel;
    GlobalRecodeModel globalRecodeModel;

    public enum Action {
        OpenMicrodata,
        SpecifyMetadata,
        SpecifyCombinations,
        ShowTableCollection,
        GlobalRecode,
        PramSpecification,
        IndividualRiskSpecification,
        HouseholdRiskSpecification,
        ModifyNumericalVariables,
        NumericalMicroAggregation,
        NumericalRankSwapping,
        MakeProtectedFile,
        ViewReport,
        Contents,
        News,
        About,
    }

    /**
     * 
     * @param view 
     */
    public MainFrameController(MainFrameView view) {
        this.view = view;
        this.selectCombinationsModel = new SelectCombinationsModel();
        this.globalRecodeModel = new GlobalRecodeModel();
        this.metadata = new MetadataMu(); 
    }

    public MetadataMu getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        organise();
    }
    
    private void organise() {
        view.enableAction(Action.SpecifyMetadata, this.metadata != null);
        view.enableAction(Action.SpecifyCombinations, this.metadata != null &&
                this.metadata.getVariables().size() > 0);
        boolean tablesCalculated = this.metadata != null &&
                this.selectCombinationsModel.getTables().size() > 0;
        view.enableAction(Action.GlobalRecode, tablesCalculated);
        view.enableAction(Action.ShowTableCollection, tablesCalculated);
        view.enableAction(Action.MakeProtectedFile, tablesCalculated);
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
    public void specifyMetaData() {                                                 
        SpecifyMetadataController controller = new SpecifyMetadataController(this.view, this.metadata);
        controller.showView();
        this.metadata = controller.getMetadata();
        organise();
    }   
    
    /**
     * 
     */
    public void specifyCombinations() {                                                     
        SelectCombinationsController controller = new SelectCombinationsController(
                this.view, this.metadata, this.selectCombinationsModel);
        controller.showView();
        this.selectCombinationsModel = controller.getModel(); // wat doet dit? De model is toch hier al aangemaakt?
        view.showUnsafeCombinations(this.selectCombinationsModel);
        organise();
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
        GlobalRecodeController controller = new GlobalRecodeController(
                this.view, this.metadata, this.globalRecodeModel, this.selectCombinationsModel);
        controller.showView();
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
