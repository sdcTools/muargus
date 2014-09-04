// TODO: change the open view methods to one method that takes an argument Classname
package muargus.controller;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import muargus.HTMLReportWriter;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.view.MainFrameView;
import org.w3c.dom.Document;

/**
 *
 * @author ambargus
 */
public class MainFrameController {

    private final MainFrameView view;

    private MetadataMu metadata;
    private Document report;

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
        //this.selectCombinationsModel = new Combinations();
        //this.globalRecodeModel = null;
        //this.makeProtectedFileModel = null;
        this.metadata = new MetadataMu();
        this.report = null;
    }

    public MetadataMu getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        organise();
    }

    private void organise() {
        if (this.metadata.getCombinations() == null) {
            clearDataBeforeSelectCombinations();
        }

        view.enableAction(Action.SpecifyMetadata, this.metadata != null);
        view.enableAction(Action.ViewReport, this.report != null);
        view.enableAction(Action.SpecifyCombinations, this.metadata != null
                && this.metadata.getVariables().size() > 0);

        boolean tablesCalculated = false;
        if (this.metadata.getCombinations() != null) {
            tablesCalculated = this.metadata != null
                    && this.metadata.getCombinations().getTables().size() > 0;
        }
        view.enableAction(Action.GlobalRecode, tablesCalculated);
        view.enableAction(Action.MakeProtectedFile, tablesCalculated);

        // Release 2
        view.enableAction(Action.ShowTableCollection, tablesCalculated);
        view.enableAction(Action.IndividualRiskSpecification, tablesCalculated);
        view.enableAction(Action.HouseholdRiskSpecification, tablesCalculated);
        view.enableAction(Action.ModifyNumericalVariables, tablesCalculated);
        view.enableAction(Action.NumericalMicroAggregation, tablesCalculated);
        view.enableAction(Action.NumericalRankSwapping, tablesCalculated);

    }

    /**
     *
     */
    public void openMicrodata() {
        DataFilePair filenames = view.showOpenMicrodataDialog(this.metadata.getFileNames());
        if (filenames == null) {
            return;
        }

        MetadataMu newMetadata = new MetadataMu();
        newMetadata.setFileNames(filenames);
        try {
            newMetadata.readMetadata();
        } catch (Exception ex) {
            this.view.showErrorMessage(new ArgusException("Error reading metadata file: " + ex.getMessage()));
            return;
        }
        try {
            newMetadata.verify();
        } catch (ArgusException ex) {
            this.view.showErrorMessage(new ArgusException("Metadata contains error(s): " + ex.getMessage()));
        }
        this.metadata = newMetadata;
        organise();
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
        if (this.metadata.getCombinations() == null) {
            this.metadata.createCombinations();
        }
        SelectCombinationsController controller = new SelectCombinationsController(
                this.view, this.metadata);
        controller.showView();
        showUnsafeCombinations(0);
    }

    /**
     *
     */
    public void showTableCollection() {
        if (this.metadata.getCombinations().getShowTableCollection() == null) {
            this.metadata.getCombinations().createShowTableCollection();
        }

        ShowTableCollectionController controller = new ShowTableCollectionController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     * @param selectedVariableIndex
     */
    public void globalRecode(int selectedVariableIndex) {
        if (this.metadata.getCombinations().getGlobalRecode() == null) {
            this.metadata.getCombinations().createGlobalRecode();
        }

        GlobalRecodeController controller = new GlobalRecodeController(
                this.view, this.metadata);
        controller.showView(selectedVariableIndex);
        showUnsafeCombinations(controller.getSelectedVariableIndex());
    }

    private void showUnsafeCombinations(int variableIndex) {
        ArrayList<String> missingCodelists = MuARGUS.getCalculationService().getUnsafeCombinations(this.metadata);
        if (!missingCodelists.isEmpty()) {
            view.showMessage("\n" + missingCodelists);
        }
        view.showUnsafeCombinations(this.metadata.getCombinations(), variableIndex);
        organise();
    }

    /**
     *
     */
    public void pramSpecification() {
        if (this.metadata.getCombinations().getPramSpecification() == null) {
            this.metadata.getCombinations().createPramSpecification();
        }
        //PramSpecificationController controller = new PramSpecificationController(
//                this.view, this.metadata);
//        controller.showView();
        showUnsafeCombinations(0);

    }

    /**
     *
     */
    public void individualRiskSpecification() {
        if (this.metadata.getCombinations().getRiskSpecification() == null) {
            this.metadata.getCombinations().createRiskSpecification();
        }

        RiskSpecificationController controller = new RiskSpecificationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    public void householdRiskSpecification() {
        individualRiskSpecification();
    }

    /**
     *
     */
    public void numericalVariables() {
        if (this.metadata.getCombinations().getModifyNumericalVariables() == null) {
            this.metadata.getCombinations().createModifyNumericalVariables();
        }

        ModifyNumericalVariablesController controller = new ModifyNumericalVariablesController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    public void numericalMicroaggregation() {
        if (this.metadata.getCombinations().getNumericalMicroaggregation() == null) {
            this.metadata.getCombinations().createNumericalMicroaggregation();
        }

        NumericalMicroaggregationController controller = new NumericalMicroaggregationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    public void numericalRankSwapping() {
        if (this.metadata.getCombinations().getNumericalRankSwapping() == null) {
            this.metadata.getCombinations().createNumericalRankSwapping();
        }

        NumericalRankSwappingController controller = new NumericalRankSwappingController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    public void makeProtectedFile() {
        try {
            if (this.metadata.getCombinations().getProtectedFile() == null) {
                this.metadata.getCombinations().createProtectedFile();
            }

            MakeProtectedFileController controller = new MakeProtectedFileController(
                    this.view, this.metadata);
            controller.showView();
            if (controller.isFileCreated()) {
                this.report = createReport();
                viewReport(true);
            }
            organise();
        } catch (ArgusException ex) {
            view.showErrorMessage(ex);
        }
    }

    /**
     *
     */
    public void viewReport(boolean save) {
        try {
            ViewReportController viewReportController = new ViewReportController(this.view, this.report);
            if (save) {
                viewReportController.saveReport(this.metadata);
            }
            viewReportController.showView();
        } catch (ArgusException ex) {
            view.showErrorMessage(ex);
        }
    }

    private Document createReport() throws ArgusException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            HTMLReportWriter.createReportTree(doc, this.metadata);

            return doc;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ex);
            throw new ArgusException("Error creating report");
        }
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

    private void clearDataBeforeSelectCombinations() {

        //this.selectCombinationsModel = null;
        for (int i = this.view.getUnsafeCombinationsTable().getColumnCount() - 1; i >= 0; i--) {
            this.view.getUnsafeCombinationsTable().getColumnModel().removeColumn(this.view.getUnsafeCombinationsTable().getColumnModel().getColumn(i));
        }
        for (int i = this.view.getVariablesTable().getColumnCount() - 1; i >= 0; i--) {
            this.view.getVariablesTable().getColumnModel().removeColumn(this.view.getVariablesTable().getColumnModel().getColumn(i));
        }
        this.view.setVariableNameLabel("");
        //this.clearDataAfterSelectCombinations();
    }

//    public void clearDataAfterSelectCombinations() {
//        this.globalRecodeModel = null;
//        this.makeProtectedFileModel = null;
//    }
}
