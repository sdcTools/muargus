/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import muargus.model.MetadataMu;
import muargus.model.RSpecification;
import muargus.view.RView;

/**
 *
 * @author Statistics Netherlands
 */
public class RController extends ControllerBase<RSpecification> {
    
    private final MetadataMu metadata;

    public RController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new RView(parentView, true, this));
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getRSpecification());
        getView().setMetadata(this.metadata);
    }
    
     /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }
    
    public String getRScript(){
        return null;
    }
    
}
