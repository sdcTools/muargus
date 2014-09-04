/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import muargus.model.MetadataMu;
import muargus.model.ModifyNumericalVariables;
import muargus.view.ModifyNumericalVariablesView;

/**
 *
 * @author ambargus
 */
public class ModifyNumericalVariablesController {
    ModifyNumericalVariablesView view;
    ModifyNumericalVariables model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public ModifyNumericalVariablesController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.view = new ModifyNumericalVariablesView(parentView, true, this);
        this.metadataMu = metadataMu;
        this.view.setMetadataMu(this.metadataMu);
    }
    
    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        this.view.setVisible(true);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        this.view.setVisible(false);
    }

    /**
     * Fuction for setting the model. This function is used by the view after
     * setting the model itself
     *
     * @param model the model class of the ShowTableCollection screen
     */
    public void setModel(ModifyNumericalVariables model) {
        this.model = model;
    }
    
}
