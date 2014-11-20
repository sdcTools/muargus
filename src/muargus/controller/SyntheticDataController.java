package muargus.controller;

import argus.model.ArgusException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import muargus.io.MetaWriter;
import muargus.model.MetadataMu;
import muargus.model.ReplacementFile;
import muargus.model.SyntheticData;
import muargus.model.SyntheticDataSpec;
import muargus.model.VariableMu;
import muargus.view.SyntheticDataView;

/**
 *
 * @author pibd05
 */
public class SyntheticDataController extends ControllerBase<SyntheticData> {

    private final MetadataMu metadata;
    private final String pathAlpha = "C:\\Users\\Gebruiker\\Desktop\\Alpha.txt";
    private final String pathSynthetic = "C:\\Users\\Gebruiker\\Desktop\\Synth.R";
    public final static String pathSyntheticData = "C:\\Users\\Gebruiker\\Desktop\\SynthData.txt";

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
        getView().setMetadata(this.metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    public ArrayList<VariableMu> getNonSensitiveVariables() {
        return getModel().getNonSensitiveVariables();
    }

    public ArrayList<VariableMu> getSensitiveVariables() {
        return getModel().getSensitiveVariables();
    }

    public Object[][] getSensitiveData() {
        Object[][] data = new Object[getSensitiveVariables().size()][2];
        for (int i = 0; i < getSensitiveVariables().size(); i++) {
            data[i][0] = getSensitiveVariables().get(i);
            data[i][1] = getSensitiveVariables().get(i).getAlpha();
        }
        return data;
    }

    public void runSyntheticData() {
        try {
            /* synthetic data: sensitive variables are numbered from x1 to xn,
             non-sensitive variables are numbered from s1 to sn.
             */
            MetaWriter.writeAlpha(this.pathAlpha, getSensitiveVariables());
            MetaWriter.writeSynthetic(this.pathSynthetic, this.pathAlpha, this.pathSyntheticData, getNonSensitiveVariables().size());
            writeSyntheticData();
            //adjustSyntheticData();
            close();
        } catch (ArgusException ex) {
            //Logger.getLogger(SyntheticDataController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void doNextStep(boolean success) {
        super.doNextStep(success); //To change body of generated methods, choose Tools | Templates.
        adjustSyntheticData();
    }

    public void writeSyntheticData() {
        try {
            SyntheticDataSpec syntheticData = new SyntheticDataSpec();
            syntheticData.getVariables().addAll(getSensitiveVariables());
            syntheticData.getVariables().addAll(getNonSensitiveVariables());
            syntheticData.setReplacementFile(new ReplacementFile("SyntheticData"));
            this.metadata.getReplacementSpecs().add(syntheticData);
            getCalculationService().makeReplacementFile(this);
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    public void adjustSyntheticData() {
        File file = new File(this.metadata.getReplacementSpecs().get(this.metadata.getReplacementSpecs().size() - 1).getReplacementFile().getInputFilePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = "";
            for (int i = 0; i < this.metadata.getCombinations().getSyntheticData().getSensitiveVariables().size(); i++) {
                line += "x" + (i + 1) + " ,";
            }
            for (int i = 0; i < this.metadata.getCombinations().getSyntheticData().getNonSensitiveVariables().size(); i++) {
                line += "s" + (i + 1) + " ,";
            }
            line = line.substring(0, line.length() - 1);
            try (PrintWriter writer = new PrintWriter(new File(SyntheticDataController.pathSyntheticData))) {
                writer.println(line);
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
            }
        } catch (IOException ex) {
            //throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }
    }

}
