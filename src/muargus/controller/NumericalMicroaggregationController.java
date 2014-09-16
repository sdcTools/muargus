/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import muargus.model.MetadataMu;
import muargus.model.NumericalMicroaggregation;
import muargus.view.NumericalMicroaggregationView;

/**
 *
 * @author ambargus
 */
public class NumericalMicroaggregationController extends ControllerBase {
    private NumericalMicroaggregation model;
    private MetadataMu metadata;

    public NumericalMicroaggregationController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new NumericalMicroaggregationView(parentView, true, this));
        this.metadata = metadata;
        getView().setMetadata(this.metadata);
    }
    
    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }
}
