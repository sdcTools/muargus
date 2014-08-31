// TODO: change the open view methods to one method that takes an argument Classname
package muargus.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import muargus.HTMLReportWriter;
import muargus.model.GlobalRecodeModel;
import muargus.model.MakeProtectedFileModel;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.MainFrameView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author ambargus
 */
public class MainFrameController {

    MainFrameView view;

    MetadataMu metadata;
    SpecifyMetadataController specifyMetadataController;
    SelectCombinationsModel selectCombinationsModel;
    GlobalRecodeModel globalRecodeModel;
    MakeProtectedFileModel makeProtectedFileModel;

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
        this.globalRecodeModel = null;
        this.makeProtectedFileModel = null;
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
        view.enableAction(Action.ViewReport, this.metadata != null);
        view.enableAction(Action.SpecifyCombinations, this.metadata != null
                && this.metadata.getVariables().size() > 0);

        boolean tablesCalculated = false;
        if (this.selectCombinationsModel != null) {
            tablesCalculated = this.metadata != null
                    && this.selectCombinationsModel.getTables().size() > 0;
        }
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
        this.specifyMetadataController = new SpecifyMetadataController(this.view, this.metadata, this);
        this.specifyMetadataController.showView();
        this.metadata = this.specifyMetadataController.getMetadata();
        organise();
    }

    /**
     *
     */
    public void specifyCombinations() {
        if(this.selectCombinationsModel == null){
            this.selectCombinationsModel = new SelectCombinationsModel();
        }
        SelectCombinationsController controller = new SelectCombinationsController(
                this.view, this.metadata, this.selectCombinationsModel, this);
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
        if (this.globalRecodeModel == null) {
            this.globalRecodeModel = new GlobalRecodeModel();
            this.globalRecodeModel.setVariables(this.selectCombinationsModel.getVariablesInTables());
        }

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
        if (this.makeProtectedFileModel == null) {
            this.makeProtectedFileModel = new MakeProtectedFileModel();
            this.makeProtectedFileModel.setVariables(this.selectCombinationsModel.getVariablesInTables());
        }

        MakeProtectedFileController controller = new MakeProtectedFileController(
                this.view, this.metadata, this.makeProtectedFileModel, this.selectCombinationsModel);
        controller.showView();
        
        viewReport();
    }     
   

    /**
     *
     */
    public void viewReport() {
        ViewReportController viewReportController = new ViewReportController(this.view, createReport());
        viewReportController.showView();
    }                                                  
    
    private HTMLDocument createReport() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            
            builder = factory.newDocumentBuilder();
            
            Document doc = builder.newDocument();
            
            //OutputFormat format = new OutputFormat(doc);
            HTMLReportWriter.createReportTree(doc, metadata, selectCombinationsModel, globalRecodeModel);
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            
            tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            //tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            StringWriter output = new StringWriter();
            
            tr.transform(new DOMSource(doc), new StreamResult(output));
            
            
//            DOMImplementationLS imp = (DOMImplementationLS)doc.getImplementation();
//            
//            LSSerializer serializer = imp.createLSSerializer();
//            String s = serializer.writeToString(doc.getDocumentElement());
            String s = output.toString();
            Reader stringReader = new StringReader(s);
            HTMLEditorKit htmlKit = new HTMLEditorKit();
            
            HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
            htmlDoc.putProperty("IgnoreCharsetDirective", new Boolean(true));
            htmlKit.read(stringReader, htmlDoc, 0);
            
            return htmlDoc;
        } catch (ParserConfigurationException|IOException|BadLocationException|TransformerException ex) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return null;
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
        try {
            Desktop.getDesktop().browse(new URL("http://neon.vb.cbs.nl/casc/Software/MuManual4.2.pdf").toURI());
        } catch (URISyntaxException | IOException e) {
        }
    }

    public void clearDataBeforeSelectCombinations() {
        this.selectCombinationsModel = null;
         for(int i = this.view.getUnsafeCombinationsTable().getColumnCount() -1; i >= 0; i--){
            this.view.getUnsafeCombinationsTable().getColumnModel().removeColumn(this.view.getUnsafeCombinationsTable().getColumnModel().getColumn(i));
        }
        for(int i = this.view.getVariablesTable().getColumnCount() -1; i >= 0; i--){
            this.view.getVariablesTable().getColumnModel().removeColumn(this.view.getVariablesTable().getColumnModel().getColumn(i));
        }
        this.view.setVariableNameLabel("");
        this.clearDataAfterSelectCombinations();
    }

    public void clearDataAfterSelectCombinations() {
        this.globalRecodeModel = null;
        this.makeProtectedFileModel = null;
    }

}
