// TODO: change the open view methods to one method that takes an argument Classname
package muargus.controller;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import muargus.HTMLReportWriter;
import muargus.MuARGUS;
import muargus.io.MetaReader;
import muargus.model.CodeInfo;
import muargus.model.Combinations;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.model.VariableMu;
import muargus.view.MainFrameView;
import org.w3c.dom.Document;

/**
 *
 * @author Statistics Netherlands
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
        QualitativeMicroAggregation,
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

        boolean tablesCalculated = false;
        if (this.metadata.getCombinations() != null) {
            tablesCalculated = this.metadata != null && this.metadata.getCombinations().getTables().size() > 0;
        }
        boolean hasNumericalVariables = false;
        for (VariableMu variableMu : this.metadata.getVariables()) {
            if (variableMu.isNumeric()) {
                hasNumericalVariables = true;
                break;
            }
        }
        
        view.enableAction(Action.SpecifyMetadata, this.metadata != null);
        view.enableAction(Action.ViewReport, this.report != null);
        view.enableAction(Action.SpecifyCombinations, this.metadata != null
                && this.metadata.getVariables().size() > 0);
        view.enableAction(Action.GlobalRecode, tablesCalculated);
        view.enableAction(Action.MakeProtectedFile, tablesCalculated);
        view.enableAction(Action.ShowTableCollection, tablesCalculated);
        view.enableAction(Action.PramSpecification, tablesCalculated);
        view.enableAction(Action.IndividualRiskSpecification,
                tablesCalculated && metadata.getCombinations().isRiskModel() && !metadata.isHouseholdData());
        view.enableAction(Action.HouseholdRiskSpecification,
                tablesCalculated && metadata.getCombinations().isRiskModel() && metadata.isHouseholdData());
        view.enableAction(Action.ModifyNumericalVariables, tablesCalculated && hasNumericalVariables);
        view.enableAction(Action.NumericalMicroAggregation, tablesCalculated && hasNumericalVariables);
        view.enableAction(Action.NumericalRankSwapping, tablesCalculated && hasNumericalVariables);
        view.enableAction(Action.QualitativeMicroAggregation, tablesCalculated);

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
            MetaReader.readRda(newMetadata);
        } catch (ArgusException ex) {
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
        if (!this.metadata.getCombinations().getTables().isEmpty()) {
            showUnsafeCombinations(0);
        }
    }

    /**
     *
     */
    public void showTableCollection() {
        ShowTableCollectionController controller = new ShowTableCollectionController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     * @param selectedVariableIndex
     */
    public void globalRecode(int selectedVariableIndex) {
        GlobalRecodeController controller = new GlobalRecodeController(
                this.view, this.metadata);
        controller.showView(selectedVariableIndex);
        showUnsafeCombinations(controller.getSelectedVariableIndex());
    }

    private void showUnsafeCombinations(int variableIndex) {
        try {
            MuARGUS.getCalculationService().getVariableInfo();
            ArrayList<String> missingCodelists = addCodelistInfo();
            if (!missingCodelists.isEmpty()) {
                view.showMessage(StrUtils.join("\n", missingCodelists));
            }
            view.showUnsafeCombinations(this.metadata.getCombinations(), variableIndex);
            organise();
        } catch (ArgusException ex) {
            view.showErrorMessage(ex);
        }
    }

    public ArrayList<String> addCodelistInfo() {
        ArrayList<String> missingCodelists = new ArrayList<>();
        Combinations model = this.metadata.getCombinations();
        boolean hasRecode = (model.getGlobalRecode() != null);
        for (VariableMu variable : this.metadata.getVariables()) {
            if (variable.isCategorical()) {
                String codelistFile = "";
                if (hasRecode) {
                    RecodeMu recode = model.getGlobalRecode().getRecodeByVariableName(variable.getName());
                    if (recode != null && (recode.isRecoded() || recode.isTruncated())) {
                        codelistFile = recode.getCodeListFile();
                    }
                }
                if ("".equals(codelistFile) && variable.isCodelist()) {
                    codelistFile = variable.getCodeListFile();
                }
                if (!"".equals(codelistFile)) {
                    try {
                        HashMap<String, String> codelist = MetaReader.readCodelist(codelistFile, this.metadata);
                        for (CodeInfo codeInfo : variable.getCodeInfos()) {
                            if (codelist.containsKey(codeInfo.getCode().trim())) {
                                codeInfo.setLabel(codelist.get(codeInfo.getCode().trim()));
                            }
                        }
                    } catch (ArgusException ex) {
                        missingCodelists.add(ex.getMessage());
                    }
                }
            }
        }
        return missingCodelists;
    }
//                
//                    String message = getCodelist(variable, codelistFile, codelist);
//                    if (message != null) {
//                        missingCodelists.add(message);
//                    }
//                }
//                }
//            
//                if (model.getVariablesInTables().contains(variable)) {
//                    
//                    int[] nDims = new int[]{0};
//                    int[] unsafeCount = new int[model.getMaxDimsInTables()];
//
//                    boolean result = c.UnsafeVariable(varIndex + 1, nDims, unsafeCount);
//                    //UnsafeInfo unsafe = new UnsafeInfo();
//                    model.setUnsafeCombinations(variable, unsafeCount, nDims[0]);
//                    //model.setUnsafe(variable, unsafeCount);
//
//                    int[] nCodes = new int[]{0};
//                    result = c.UnsafeVariablePrepare(varIndex + 1, nCodes);
//                    int[] isMissing = new int[]{0};
//                    int[] freq = new int[]{0};
//                    String[] code = new String[1];
//                    variable.clearCodeInfos();
//                    for (int codeIndex = 0; codeIndex < nCodes[0]; codeIndex++) {
//                        result = c.UnsafeVariableCodes(varIndex + 1,
//                                codeIndex + 1,
//                                isMissing,
//                                freq,
//                                code,
//                                nDims,
//                                unsafeCount);
//                        CodeInfo codeInfo = new CodeInfo(code[0], isMissing[0] != 0);
//                        if (codelist.containsKey(code[0].trim())) {
//                            codeInfo.setLabel(codelist.get(code[0].trim()));
//                        }
//                        codeInfo.setFrequency(freq[0]);
//                        codeInfo.setUnsafeCombinations(nDims[0], unsafeCount);
//                        variable.addCodeInfo(codeInfo);
//                    }
//                    result = c.UnsafeVariableClose(varIndex + 1);
//                }
//                else {
//                    variable.clearCodeInfos();
//                    int codeIndex = 0;
//                    while (true) {
//                        codeIndex++;
//                        String[] code = new String[1];
//                        int[] pramPerc = new int[1];
//                        boolean result = c.GetVarCode(varIndex + 1, codeIndex, code, pramPerc); 
//                        if (!result)
//                            break;
//                        CodeInfo codeInfo = new CodeInfo(code[0], false);
//                         if (codelist.containsKey(code[0].trim())) {
//                            codeInfo.setLabel(codelist.get(code[0].trim()));
//                        }
//                        variable.addCodeInfo(codeInfo);
//                    }
//                    for (int i=0; i < 2; i++) {
//                        if (!"".equals(variable.getMissing(i))) {
//                            CodeInfo codeInfo = new CodeInfo(variable.getMissing(i), true);
//                            variable.addCodeInfo(codeInfo);
//                        }
//                    }
//                }
//            }
//        }
//        return missingCodelists;
//    }


    /**
     *
     */
    public void pramSpecification() {
        PramSpecificationController controller = new PramSpecificationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    private void riskSpecification(boolean household) {
        if (this.metadata.getCombinations().getRiskSpecifications().isEmpty()) {
            this.metadata.getCombinations().fillRiskSpecifications();
        }

        RiskSpecificationController controller = new RiskSpecificationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    public void householdRiskSpecification() {
        riskSpecification(true);
    }

    public void individualRiskSpecification() {
        riskSpecification(false);
    }

    /**
     *
     */
    public void numericalVariables() {
        ModifyNumericalVariablesController controller = new ModifyNumericalVariablesController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     *
     */
    public void numericalMicroaggregation() {
        MicroaggregationController controller = new MicroaggregationController(
                this.view, this.metadata, true);
        controller.showView();
    }

    /**
     *
     */
    public void numericalRankSwapping() {
        NumericalRankSwappingController controller = new NumericalRankSwappingController(
                this.view, this.metadata);
        controller.showView();
    }

    public void qualitativeMicroaggregation() {
        MicroaggregationController controller = new MicroaggregationController(view, metadata, false);
        controller.showView();
        
    }
    /**
     *
     */
    public void makeProtectedFile() {
        try {

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
            
            Desktop.getDesktop().browse(new URL("http://neon.vb.cbs.nl/casc/Software/MuManual4.2.pdf#page=13").toURI());
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
