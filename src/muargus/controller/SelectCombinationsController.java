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
import java.util.ArrayList;
import java.util.Arrays;
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
            String fileName = this.metadata.getFileNames().getDataFileName();
            String selectedVariables = "";
            for (SpssVariable v : this.metadata.getSpssVariables()) {
                if (v.isSelected() && v.isCategorical()) {
                    selectedVariables = selectedVariables + v.getName() + " ";
                    
                }
            }
            try {
                StatsUtil.start();
                StatsUtil.submit("get file = \"" + fileName + "\".");
                DataUtil d = new DataUtil();
                ArrayList<String> variables = new ArrayList<>();
                for(SpssVariable v: this.metadata.getSpssVariables()){
                    if(v.isSelected()){
                        variables.add(v.getName());
                    }
                }
                String[] variablesArray = new String[variables.size()];
                for(int i = 0; i< variables.size(); i++){
                    variablesArray[i] = variables.get(i);
                }
                
                d.setVariableFilter(variablesArray);
                Case[] data = d.fetchCases(false, 0);
                System.out.println(Arrays.toString(data));
//                String[] command = {"SET DECIMAL=DOT.",
//                    "get file = \"" + fileName + "\".",
//                    "WRITE OUTFILE= '" + fileName.substring(0, fileName.length() - 3) + "dat'/" + selectedVariables + ".",
//                    "EXECUTE."
//                };
//                System.out.println(Arrays.toString(command));
//                StatsUtil.submit(command);
                StatsUtil.stop();
            } catch (StatsException ex) {
                Logger.getLogger(SelectCombinationsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        CalculationService service = MuARGUS.getCalculationService();
        service.setMetadata(this.metadata);
        service.exploreFile(this);
    }

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
