/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import muargus.CalculationService;
import argus.model.ArgusException;
import argus.utils.SystemUtils;
import com.ibm.statistics.plugin.Case;
import com.ibm.statistics.plugin.DataUtil;
import com.ibm.statistics.plugin.StatsException;
import com.ibm.statistics.plugin.StatsUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.SpssVariable;
import muargus.view.SelectCombinationsView;

/**
 *
 * @author Statistics Netherlands
 */
public class SelectCombinationsController extends ControllerBase<Combinations> {

    private final MetadataMu metadata;

    private static final Logger logger = Logger.getLogger(SelectCombinationsController.class.getName());

    public SelectCombinationsController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SelectCombinationsView(parentView, true, this));
        this.metadata = metadata;

        getSettings();
        setModel(new Combinations(this.metadata.getCombinations()));

        getView().setMetadata(this.metadata);
        getSelectCombinationsView().setModel(getModel()); // the view gets a copy of the current Combinations
    }

    private SelectCombinationsView getSelectCombinationsView() {
        return (SelectCombinationsView) getView();
    }

//    public void setList(ArrayList<String> list){
//        DefaultListModel model1 = new DefaultListModel();
//        for(String s: list){
//            model1.addElement(s);
//        }
//    }
    /**
     *
     * @throws argus.model.ArgusException
     */
    public void calculateTables() throws ArgusException {
        getSelectCombinationsView().enableCalculateTables(false);
        saveSettings();
        this.metadata.setCombinations(getModel());
        if (this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS) {
            SpssUtils.generateSpssData(this.metadata);
        }
        CalculationService service = MuARGUS.getCalculationService();
        service.setMetadata(this.metadata);
        service.exploreFile(this);

    }

//    private void generateSpssData() {
//        try {
//            // start spss and make an instance of dataUtil
//            StatsUtil.start();
//            DataUtil dataUtil = new DataUtil();
//
//            // Get the spss data file
//            String fileName = this.metadata.getFileNames().getDataFileName();
//            StatsUtil.submit("get file = \"" + fileName + "\".");
//
//            // Make an array of variable names to use as a filter
//            ArrayList<String> variables = new ArrayList<>();
//            for (SpssVariable v : this.metadata.getSpssVariables()) {
//                if (v.isSelected()) {
//                    variables.add(v.getName());
//                }
//            }
//            dataUtil.setVariableFilter(variables.toArray(new String[variables.size()]));
//
//            // fetch an array of cases. One case contains the data for the variables specified in the filter.
//            Case[] data = dataUtil.fetchCases(true, 0);
//            
//            // sets the number of cases in the metadata (this is used during the writing of the safe data file).
//            this.metadata.setSpssNumberOfCases(data.length);
//            try {
//                String fileNameNew = this.metadata.getFileNames().getDataFileName();
//                this.metadata.setSpssTempDataFileName(fileNameNew.substring(0, fileNameNew.length() - 3) + "asc");
//                try (PrintWriter writer = new PrintWriter(new File(this.metadata.getSpssTempDataFileName()))) {
//                    for (Case c : data) {
//                        //TODO: verander naar fixed format??
//                        writer.println(c.toString().substring(1, c.toString().length() - 1).replace("null", "").replace(',', ';'));
//                    }
//                }
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            StatsUtil.stop();
//        } catch (StatsException ex) {
//            Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private void getSettings() {
        int[] thresholds = new int[MuARGUS.MAXDIMS];
        for (int t = 1; t <= MuARGUS.MAXDIMS; t++) {
            thresholds[t - 1] = SystemUtils.getRegInteger("general", "threshold" + Integer.toString(t), 1);
        }
        this.metadata.getCombinations().setThresholds(thresholds);
    }

    private void saveSettings() {
        int[] thresholds = this.metadata.getCombinations().getThresholds();
        if (thresholds == null || thresholds.length < MuARGUS.MAXDIMS) {
            return;
        }
        for (int t = 1; t <= MuARGUS.MAXDIMS; t++) {
            SystemUtils.putRegInteger("general", "threshold" + Integer.toString(t), thresholds[t - 1]);
        }
    }

    /**
     *
     */
    public void automaticSpecification() {
        //het zou mooier zijn als de berekening niet in de view zou gebeuren
    }

    /**
     *
     */
    public void cancel() {
        getView().setVisible(false);
    }

    @Override
    protected void doNextStep(boolean success) {
        if (success && getStepName().equals("ExploreFile")) {
            MuARGUS.getCalculationService().calculateTables(this);
        } else {
            getSelectCombinationsView().enableCalculateTables(true);
            if (success) {
                getView().setVisible(false);
            }
        }
    }

//    public void clearData(){
//        this.controller.clearDataAfterSelectCombinations();
//    }
}
