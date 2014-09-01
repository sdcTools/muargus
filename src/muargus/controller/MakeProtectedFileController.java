/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.view.MakeProtectedFileView;

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileController implements PropertyChangeListener {

    private MakeProtectedFileView view;
    //MakeProtectedFileModel model;
    private MetadataMu metadata;
    //Combinations selectCombinationsModel;
    private boolean fileCreated;
    /**
     *
     * @param parentView
     * @param metadata
     * @param model
     * @param selectCombinationsModel
     */
    public MakeProtectedFileController(java.awt.Frame parentView, MetadataMu metadata) {
        //this.model = model;
        //this.selectCombinationsModel = selectCombinationsModel;
        //this.model.setRiskModel(this.selectCombinationsModel.isRiskModel());
        this.view = new MakeProtectedFileView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);
        this.fileCreated = false;

    }

    public void showView() {
        this.view.setVisible(true);
    }

//    public ProtectedFile getModel() {
//        return this.model;
//    }

    
    /**
     *
     */
    public void makeFile(File file) {
        this.metadata.getCombinations().getProtectedFile().initSafeMeta(file, this.metadata);
        TableService service = new TableService();
        service.setPropertyChangeListener(this);
        service.makeProtectedFile(this.metadata);
    }
    
    private void saveSafeMeta() {
        
        new TableService().fillSafeFileMetadata(this.metadata);
        MetadataMu safeMetadata = this.metadata.getCombinations().getProtectedFile().getSafeMeta();
        File file = new File(safeMetadata.getFileNames().getMetaFileName());
        try {
            safeMetadata.write(file);
            this.fileCreated = true;
        }
        catch (ArgusException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public Combinations getCombinations() {
        return this.metadata.getCombinations();
    }

    public boolean isFileCreated() {
        return fileCreated;
    }

    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case "progress":
                view.setProgress(pce.getNewValue());
                break;
            case "status":
                if (pce.getNewValue() == "done") {
                    saveSafeMeta();
                    view.setVisible(!this.fileCreated);
                }
        }
    }
    
    public JRadioButton getSuppressionRadioButton() {
        switch (getCombinations().getProtectedFile().getSuppressionType()) {
            case (0):
                return this.view.getNoSuppressionRadioButton();
            case (1):
                return this.view.getUseWeightRadioButton();
            case(2):
                return this.view.getUseEntropyRadioButton();
        }
        return null;
    }
}
