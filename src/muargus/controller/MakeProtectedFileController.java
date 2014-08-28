/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JRadioButton;
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
        this.selectCombinationsModel = selectCombinationsModel;
        this.model.setRiskModel(this.selectCombinationsModel.isRiskModel());
        this.view = new MakeProtectedFileView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);

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
    
    public JRadioButton getSuppressionRadioButton() {
        switch (this.model.getSuppressionType()) {
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
