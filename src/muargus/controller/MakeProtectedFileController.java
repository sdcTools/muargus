/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import muargus.model.MakeProtectedFileModel;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.view.MakeProtectedFileView;

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileController implements PropertyChangeListener {

    MakeProtectedFileView view;
    MakeProtectedFileModel model;
    MetadataMu metadata;
    SelectCombinationsModel selectCombinationsModel;

    /**
     *
     * @param parentView
     * @param metadata
     * @param model
     * @param selectCombinationsModel
     */
    public MakeProtectedFileController(java.awt.Frame parentView, MetadataMu metadata,
            MakeProtectedFileModel model, SelectCombinationsModel selectCombinationsModel) {
        this.model = model;
        this.view = new MakeProtectedFileView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);
        this.selectCombinationsModel = selectCombinationsModel;
    }

    public void showView() {
        this.view.setVisible(true);
    }

    public MakeProtectedFileModel getModel() {
        return this.model;
    }

    /**
     *
     */
    public void makeFile() {
        TableService service = new TableService();
        service.setPropertyChangeListener(this);
        service.makeProtectedFile(model, metadata, selectCombinationsModel);
    }

    public SelectCombinationsModel getSelectCombinationsModel() {
        return selectCombinationsModel;
    }

    public void setValuesForDLL() {
        
        if (this.view.getChangeIntoSequenceNumberRadioButton().isSelected()) {
            this.model.setWithEntropy(false);
            this.model.setWithPrior(false);
        } else if (this.view.getUseEntropyRadioButton().isSelected()) {
            this.model.setWithEntropy(true);
            this.model.setWithPrior(false);
        } else if (this.view.getUseWeightRadioButton().isSelected()){
            this.model.setWithEntropy(false);
            this.model.setWithPrior(true);
        }
        
        int houseHoldOption = 0;
        if(this.view.isHouseholdData()){
            houseHoldOption = 1;
        } else if(this.view.getKeepInSafeFileRadioButton().isSelected()){
            houseHoldOption = 2;
        } else if(this.view.getChangeIntoSequenceNumberRadioButton().isSelected()){
            houseHoldOption = 3;
        } else if(this.view.getRemoveFromSafeFileRadioButton().isSelected()){
            houseHoldOption = 4;
        }
        this.model.setHhOption(houseHoldOption);
        this.model.setRandomizeOutput(this.view.getWriteRecordRandomOrderCheckBox().isSelected());
        
        
        

    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case "progress":
                view.setProgress(pce.getNewValue());
                break;
            case "status":
                view.setVisible(pce.getNewValue() != "done");
        }
    }

}
