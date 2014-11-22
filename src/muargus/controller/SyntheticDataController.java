package muargus.controller;

import argus.model.ArgusException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import muargus.io.MetaWriter;
import muargus.model.MetadataMu;
import muargus.model.ReplacementFile;
//import muargus.model.SyntheticData;
import muargus.model.SyntheticDataSpec;
import muargus.model.VariableMu;
import muargus.view.SyntheticDataView;

/**
 *
 * @author pibd05
 */
public class SyntheticDataController extends ControllerBase<SyntheticDataSpec> {

    private final MetadataMu metadata;
    //private final String pathAlpha = "C:\\Users\\Gebruiker\\Desktop\\Alpha.txt";
    //private final String pathSynthetic = "C:\\Users\\Gebruiker\\Desktop\\Synth.R";
    //public final static String pathSyntheticData = "C:\\Users\\Gebruiker\\Desktop\\SynthData.txt";
    private final static String pathRexe = "C:\\Program Files\\R\\R-2.15.0\\bin\\RScript.exe";

    /**
     * Constructor for the RController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SyntheticDataController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SyntheticDataView(parentView, true, this));
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

    public void runSyntheticData() {
        try {
            SyntheticDataSpec syntheticData = getModel();
                    
            syntheticData.getSensitiveVariables().addAll(getSensitiveVariables());
            syntheticData.getNonSensitiveVariables().addAll(getNonSensitiveVariables());
            syntheticData.setReplacementFile(new ReplacementFile("SyntheticData"));
            this.metadata.getReplacementSpecs().add(syntheticData);
            
            /* synthetic data: sensitive variables are numbered from x1 to xn,
             non-sensitive variables are numbered from s1 to sn.
             */
            //this.getModel().getSensitiveVariables().addAll(this)
            MetaWriter.writeAlpha(syntheticData);
            MetaWriter.writeSynthetic(syntheticData);
            writeSyntheticData();
            //adjustSyntheticData();
            close();
        } catch (ArgusException ex) {
            //Logger.getLogger(SyntheticDataController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private List<VariableMu> getSensitiveVariables() {
        return ((SyntheticDataView)getView()).getSelectedSensitiveVariables();
    }

    private List<VariableMu> getNonSensitiveVariables() {
        return ((SyntheticDataView)getView()).getSelectedNonSensitiveVariables();
    }

    @Override
    protected void doNextStep(boolean success) {
        //Add header
        adjustSyntheticData();
        //Run the R script
        //RunRScript()
    }

    private void writeSyntheticData() {
            getCalculationService().makeReplacementFile(this);
    }

    public void adjustSyntheticData() {
        //Adds a header containing the variable names that the R script expects
        File file = new File(this.metadata.getReplacementSpecs().get(this.metadata.getReplacementSpecs().size() - 1).getReplacementFile().getInputFilePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = "";
            for (int i = 0; i < getModel().getSensitiveVariables().size(); i++) {
                line += "x" + (i + 1) + " ,";
            }
            for (int i = 0; i < getModel().getNonSensitiveVariables().size(); i++) {
                line += "s" + (i + 1) + " ,";
            }
            line = line.substring(0, line.length() - 1);
            try (PrintWriter writer = new PrintWriter(new File(getModel().getReplacementFile().getInputFilePath() + "2"))) {
                writer.println(line);
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
            }
        } catch (IOException ex) {
            //throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }
    }
    
        /**
     * Gets the model and fills the model with the numeric variables if the
     * model is empty.
     */
    private void fillModel() {
        if (getModel().getAllVariables().isEmpty()) {
            for (VariableMu variable : this.metadata.getVariables()) {
                if (variable.isNumeric()) {
                    getModel().getAllVariables().add(variable);
                }
            }
        }
    }


}
