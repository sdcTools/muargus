//TODO: make news reader better
package muargus.controller;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import muargus.view.AboutView;
import muargus.view.MainFrameView;
import org.w3c.dom.Document;

/**
 * Controller class of the Main Frame.
 *
 * @author Statistics Netherlands
 */
public class MainFrameController {

    private final MainFrameView view;

    private MetadataMu metadata;
    private Document report;
    private String news;
    private final File newsLocation = new File("./resources/html/MuNews.html");

    /**
     * Enumeration for the main functionality of Mu-Argus.
     */
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
        SyntheticData
    }

    /**
     * Constructor for the MainFrameController.
     *
     * @param view the Frame of the mainFrame.
     */
    public MainFrameController(MainFrameView view) {
        this.view = view;
        this.metadata = new MetadataMu();
        this.report = null;
        this.news = "";
    }

    /**
     * Gets the metadata.
     *
     * @return MetadataMu instance containing the metadata.
     */
    public MetadataMu getMetadata() {
        return metadata;
    }

    /**
     * Sets the metadata.
     *
     * @param metadata MetadataMu instance containing the metadata.
     */
    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        organise();
    }

    /**
     * Organizes the main screens functionality. Enables/disables the buttons
     * and menu-items.
     */
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

        this.view.enableAction(Action.SpecifyMetadata, this.metadata != null);
        this.view.enableAction(Action.ViewReport, this.report != null);
        this.view.enableAction(Action.SpecifyCombinations, this.metadata != null
                && this.metadata.getVariables().size() > 0);
        this.view.enableAction(Action.GlobalRecode, tablesCalculated);
        this.view.enableAction(Action.MakeProtectedFile, tablesCalculated);
        this.view.enableAction(Action.ShowTableCollection, tablesCalculated);
        this.view.enableAction(Action.PramSpecification, tablesCalculated);
        this.view.enableAction(Action.SyntheticData, tablesCalculated);
        this.view.enableAction(Action.IndividualRiskSpecification,
                tablesCalculated && metadata.getCombinations().isRiskModel() && !metadata.isHouseholdData());
        this.view.enableAction(Action.HouseholdRiskSpecification,
                tablesCalculated && metadata.getCombinations().isRiskModel() && metadata.isHouseholdData());
        this.view.enableAction(Action.ModifyNumericalVariables, tablesCalculated && hasNumericalVariables);
        this.view.enableAction(Action.NumericalMicroAggregation, tablesCalculated && hasNumericalVariables);
        this.view.enableAction(Action.NumericalRankSwapping, tablesCalculated && hasNumericalVariables);

    }

    /**
     * Opens the open microdata screen. Gets the data file names, makes a new
     * instance of MetadataMu, reads the metadata file and verifies the
     * metadata.
     */
    public void openMicrodata() {
        DataFilePair filenames;
        boolean hasSpss = MuARGUS.getSpssUtils() != null;
        if (this.metadata.isSpss() && this.metadata.getCombinations() != null) {
            if (this.metadata.getCombinations().getTables().size() > 0) {
                filenames = this.view.showOpenMicrodataDialog(new DataFilePair(
                        MuARGUS.getSpssUtils().spssDataFileName, this.metadata.getFileNames().getMetaFileName()), hasSpss);
            } else {
                filenames = this.view.showOpenMicrodataDialog(this.metadata.getFileNames(), hasSpss);
            }
        } else {
            filenames = this.view.showOpenMicrodataDialog(this.metadata.getFileNames(), hasSpss);
        }
        if (filenames == null) {
            return;
        }

        MetadataMu newMetadata = new MetadataMu();
        newMetadata.setFileNames(filenames);
        try {
            MetaReader.readRda(newMetadata, this.view);
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
        if (this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS) {
            MuARGUS.getSpssUtils().verifyMetadata(this.metadata);
        }
        organise();
    }

    /**
     * Exits the application.
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Opens the specify metadata screen.
     */
    public void specifyMetaData() {
        SpecifyMetadataController controller = new SpecifyMetadataController(this.view, this.metadata);
        controller.showView();
        this.metadata = controller.getMetadata();
        organise();
    }

    /**
     * Opens the specify combinations screen.
     */
    public void specifyCombinations() {
        if (this.metadata.getCombinations() == null) {
            this.metadata.createCombinations();
        }
        SelectCombinationsController controller = new SelectCombinationsController(
                this.view, this.metadata);
        controller.showView();
        if (!this.metadata.getCombinations().getTables().isEmpty()) {
            showUnsafeCombinations(0, true);
        }
    }

    /**
     * Opens the show table collection screen.
     */
    public void showTableCollection() {
        ShowTableCollectionController controller = new ShowTableCollectionController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     * Opens the global recode screen.
     *
     * @param selectedVariableIndex
     */
    public void globalRecode(int selectedVariableIndex) {
        GlobalRecodeController controller = new GlobalRecodeController(
                this.view, this.metadata);
        controller.showView(selectedVariableIndex);
        showUnsafeCombinations(controller.getSelectedVariableIndex(), false);
        this.view.updateVariablesTable();
    }

    /**
     * Shows the unsafe combinations.
     *
     * @param variableIndex Integer containing the index of the variable.
     * @param redraw Boolean indicating whether the number of unsafe
     * combinations have been changed and need to be redrawn.
     */
    private void showUnsafeCombinations(int variableIndex, boolean redraw) {
        try {
            MuARGUS.getCalculationService().getVariableInfo();
            ArrayList<String> missingCodelists = addCodelistInfo();
            if (!missingCodelists.isEmpty()) {
                this.view.showMessage(StrUtils.join("\n", missingCodelists));
            }
            this.view.showUnsafeCombinations(this.metadata.getCombinations(), variableIndex, redraw);
            organise();
        } catch (ArgusException ex) {
            this.view.showErrorMessage(ex);
        }
    }

    /**
     * Adds the codelist info to the variabels.
     *
     * @return ArrayList of Strings containing the missing codelists.
     */
    public ArrayList<String> addCodelistInfo() {
        ArrayList<String> missingCodelists = new ArrayList<>();
        Combinations model = this.metadata.getCombinations();
        boolean hasRecode = (model.getGlobalRecode() != null);
        for (VariableMu variable : this.metadata.getVariables()) {
            if (variable.isCategorical()) {
                String codelistFile = "";

                if (variable.isCodelist()) {
                    codelistFile = variable.getCodeListFile();
                }
                if (hasRecode) {
                    RecodeMu recode = model.getGlobalRecode().getRecodeByVariableName(variable.getName());
                    if (recode != null && (recode.getAppliedCodeListFile().length() > 0)) {
                        codelistFile = recode.getAppliedCodeListFile();
                    }
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
                } else if (this.metadata.isSpss()) {
                    HashMap<String, String> codelist = new HashMap<>();
                    if (MuARGUS.getSpssUtils().isNumeric(variable.getSpssVariable())) {
                        Map<Double, String> numeric = variable.getSpssVariable().getNumericValueLabels();
                        Double[] key = numeric.keySet().toArray(new Double[numeric.size()]);
                        String[] value = numeric.values().toArray(new String[numeric.size()]);

                        for (int i = 0; i < numeric.size(); i++) {
                            int j = key[i].intValue();
                            codelist.put(j + "", value[i]);
                        }

                    } else {
                        codelist.putAll(variable.getSpssVariable().getStringValueLabels());
                    }
                    for (CodeInfo codeInfo : variable.getCodeInfos()) {
                        if (codelist.containsKey(codeInfo.getCode().trim())) {
                            codeInfo.setLabel(codelist.get(codeInfo.getCode().trim()));
                        }
                    }
                }
            }
        }
        return missingCodelists;
    }

//  TODO:Remove              
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
     * Opens the PRAM specification screen.
     */
    public void pramSpecification() {
        PramSpecificationController controller = new PramSpecificationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     * Opens the risk specification screen.
     */
    public void riskSpecification() {
        if (this.metadata.getCombinations().getRiskSpecifications().isEmpty()) {
            this.metadata.getCombinations().fillRiskSpecifications();
        }

        RiskSpecificationController controller = new RiskSpecificationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     * Opens the modify numerical variables screen.
     */
    public void numericalVariables() {
        ModifyNumericalVariablesController controller = new ModifyNumericalVariablesController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     * Opens the numerical microaggreagation screen.
     */
    public void numericalMicroaggregation() {
        MicroaggregationController controller = new MicroaggregationController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     * Opens the numerical rank swapping screen.
     */
    public void numericalRankSwapping() {
        NumericalRankSwappingController controller = new NumericalRankSwappingController(
                this.view, this.metadata);
        controller.showView();
    }

    /**
     * Opens the make protected file screen.
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
            this.view.showErrorMessage(ex);
        }
    }

    /**
     * Opens the view report screen.
     *
     * @param save
     */
    public void viewReport(boolean save) {
        try {
            ViewReportController viewReportController = new ViewReportController(this.view, this.report);
            if (save) {
                viewReportController.saveReport(this.metadata);
            }
            viewReportController.showView();
        } catch (ArgusException ex) {
            this.view.showErrorMessage(ex);
        }
    }

    /**
     * Creates the report as a html file.
     *
     * @return Document containing the html report.
     * @throws ArgusException Throws an ArgusException when the file cannot be
     * created.
     */
    private Document createReport() throws ArgusException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            HTMLReportWriter.createReportTree(doc, this.metadata);

            return doc;
        } catch (ParserConfigurationException ex) {
            throw new ArgusException("Error creating report: " + ex.getMessage());
        }
    }

    /**
     * Opens the manual at the beginning.
     */
    public void contents() {
        try {
            MuARGUS.showHelp(null);
        } catch (ArgusException ex) {
            view.showErrorMessage(ex);
        }
    }

    //TODO: mooier maken
    /**
     * Shows the news.
     */
    public void news() {

        if (this.news.equals("")) {
            try {
                byte[] data;
                try (FileInputStream fis = new FileInputStream(this.newsLocation)) {
                    data = new byte[(int) this.newsLocation.length()];
                    fis.read(data);
                }
                String newsString = new String(data, "UTF-8");
                String css = "<link href=\"file:///" + HTMLReportWriter.css.getAbsolutePath() + "\" rel=\"stylesheet\" type=\"text/css\">";
                this.news = newsString.replace("<!--{CSS}-->", css);
            } catch (IOException ex) {
                this.view.showErrorMessage(new ArgusException("Error while reading news file: " + ex.getMessage()));
            }
        }

        ViewReportController viewReportController = new ViewReportController(this.view, this.news, "News");
        viewReportController.showView();
    }

    /**
     * Opens the about screen.
     */
    public void about() {
        new AboutView(this.view, true).setVisible(true);
    }

    /**
     * Opens the synthetic data screen.
     */
    public void syntheticData() {
        SyntheticDataController controller = new SyntheticDataController(this.view, this.metadata);
        controller.showView();
    }

    /**
     * Clears the data if no combinations are selected.
     */
    private void clearDataBeforeSelectCombinations() {
        for (int i = this.view.getUnsafeCombinationsTable().getColumnCount() - 1; i >= 0; i--) {
            this.view.getUnsafeCombinationsTable().getColumnModel().removeColumn(this.view.getUnsafeCombinationsTable().getColumnModel().getColumn(i));
        }
        for (int i = this.view.getVariablesTable().getColumnCount() - 1; i >= 0; i--) {
            this.view.getVariablesTable().getColumnModel().removeColumn(this.view.getVariablesTable().getColumnModel().getColumn(i));
        }
        this.view.setVariableNameLabel("");
    }

}
