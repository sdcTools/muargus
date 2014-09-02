/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import muargus.model.MetadataMu;
import muargus.view.ShowTableCollectionView;

/**
 *
 * @author ambargus
 */
public class ShowTableCollectionController {
    
    ShowTableCollectionView view;
    MetadataMu metadataMu;

    public ShowTableCollectionController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new ShowTableCollectionView(parentView, true, this);
        this.metadataMu = metadata;
        this.view.setMetadataMu(this.metadataMu);
    }
    
    public void showView() {
        this.view.setVisible(true);
    }
    
    public void close() {
        this.view.setVisible(false);
    }
    
}
