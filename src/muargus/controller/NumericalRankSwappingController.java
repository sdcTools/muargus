/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import muargus.model.MetadataMu;
import muargus.model.NumericalRankSwapping;
import muargus.view.NumericalRankSwappingView;

/**
 *
 * @author ambargus
 */
public class NumericalRankSwappingController {
    NumericalRankSwappingView view;
    NumericalRankSwapping model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public NumericalRankSwappingController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.view = new NumericalRankSwappingView(parentView, true, this);
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
    public void setModel(NumericalRankSwapping model) {
        this.model = model;
    }
}
