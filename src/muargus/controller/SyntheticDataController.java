package muargus.controller;

import argus.model.ArgusException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import muargus.io.RWriter;
import muargus.model.MetadataMu;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.SyntheticDataSpec;
import muargus.model.VariableMu;
import muargus.view.SyntheticDataView;

/**
 *
 * @author pibd05
 */
public class SyntheticDataController extends ControllerBase<SyntheticDataSpec> {

    private final MetadataMu metadata;
    //private final static String pathRexe = "C:\\Program Files\\R\\R-3.1.2\\bin\\RScript.exe";
    private final SyntheticDataView view;

    /**
     * Constructor for the RController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SyntheticDataController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new SyntheticDataView(parentView, true, this);
        super.setView(this.view);
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getSyntheticData());
        fillModel();
        getView().setMetadata(this.metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

//    public Object[][] getSensitiveData() {
//        Object[][] data = new Object[getSensitiveVariables().size()][2];
//        for (int i = 0; i < getSensitiveVariables().size(); i++) {
//            data[i][0] = getSensitiveVariables().get(i);
//            data[i][1] = getSensitiveVariables().get(i).getAlpha();
//        }
//        return data;
//    }
    public boolean runSyntheticData() {
        try {
            SyntheticDataSpec syntheticData = getModel();
            clean(syntheticData);
            syntheticData.getSensitiveVariables().addAll(getSensitiveVariables());
            syntheticData.getNonSensitiveVariables().addAll(getNonSensitiveVariables());
            syntheticData.setReplacementFile(new ReplacementFile("SyntheticData"));
            this.metadata.getReplacementSpecs().add(syntheticData);

            /* synthetic data: sensitive variables are numbered from x1 to xn,
             non-sensitive variables are numbered from s1 to sn.
             */
            RWriter.writeAlpha(syntheticData);
            RWriter.writeSynthetic(syntheticData);
            writeSyntheticData();
            RWriter.writeBatSynthetic(syntheticData);
            return true;
        } catch (ArgusException ex) {
            return false;
//            Logger.getLogger(SyntheticDataController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clean(SyntheticDataSpec syntheticData) {
        syntheticData.getSensitiveVariables().clear();
        syntheticData.getNonSensitiveVariables().clear();
        ArrayList<ReplacementSpec> r = this.metadata.getReplacementSpecs();
        for (int i = r.size() - 1; i >= 0; i--) {
            if (r.get(i) instanceof SyntheticDataSpec) {
                r.remove(i);
            }
        }
    }

    private List<VariableMu> getSensitiveVariables() {
        return ((SyntheticDataView) getView()).getSelectedSensitiveVariables();
    }

    private List<VariableMu> getNonSensitiveVariables() {
        return ((SyntheticDataView) getView()).getSelectedNonSensitiveVariables();
    }

    @Override
    protected void doNextStep(boolean success) {
        //Add header
        RWriter.adjustSyntheticData(getModel());
        runBat();
        if (RWriter.adjustSyntheticOutputFile(getModel())) {
            getView().showMessage("Synthetic data successfully generated");
        } else {
            getView().showErrorMessage(new ArgusException("No synthetic data generated. Check if R is properly installed.")); //TODO: beter foutmelding als R niet goed is geinstalleerd.
        }
        this.view.enableRunSyntheticDataButton(true);
        //Run the R script
        //RunRScript()
    }

    private void runBat() {
        try {
            String cmd = getModel().getRunRFileFile().getAbsolutePath();
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            //System.out.println(cmd);
        } catch (IOException | ArgusException | InterruptedException ex) {
            //Logger.getLogger(SyntheticDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeSyntheticData() {
        getCalculationService().makeReplacementFile(this);
    }

    /**
     * Gets the model and fills the model with the numeric variables if the
     * model is empty.
     */
    private void fillModel() {
        getModel().clear();
//        if (getModel().getAllVariables().isEmpty()) {
        for (VariableMu variable : this.metadata.getVariables()) {
            if (variable.isNumeric()) {
                getModel().getAllVariables().add(variable);
            }
        }
        // }
    }

}
