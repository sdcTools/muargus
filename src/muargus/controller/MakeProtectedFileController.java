/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.view.MakeProtectedFileView;

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileController extends ControllerBase {

    //private MakeProtectedFileView view;
    //MakeProtectedFileModel model;
    private final MetadataMu metadata;
    //Combinations selectCombinationsModel;
    private boolean fileCreated;
    private final CalculationService service;
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
        this.setView(new MakeProtectedFileView(parentView, true, this));
        this.metadata = metadata;
        this.getView().setMetadata(this.metadata);
        this.fileCreated = false;
        this.service = MuARGUS.getCalculationService();

    }

    public void showView() {
        this.getView().setVisible(true);
    }

//    public ProtectedFile getModel() {
//        return this.model;
//    }

    
    /**
     *
     */
    public void makeFile(File file) {
        this.metadata.getCombinations().getProtectedFile().initSafeMeta(file, this.metadata);
        this.service.makeProtectedFile(this);
    }
    
    private void saveSafeMeta() {
        
        this.service.fillSafeFileMetadata();
        MetadataMu safeMetadata = this.metadata.getCombinations().getProtectedFile().getSafeMeta();
        File file = new File(safeMetadata.getFileNames().getMetaFileName());
        try {
            safeMetadata.write(file, false);
            this.fileCreated = true;
        }
        catch (ArgusException ex) {
            this.getView().showErrorMessage(ex);
        }
    }

    public Combinations getCombinations() {
        return this.metadata.getCombinations();
    }

    public boolean isFileCreated() {
        return fileCreated;
    }

    @Override
    protected void doNextStep() {
        saveSafeMeta();
        this.getView().setVisible(!this.fileCreated);
    }

  
}
