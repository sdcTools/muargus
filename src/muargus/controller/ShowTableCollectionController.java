/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.ShowTableCollection;
import muargus.model.TableMu;
import muargus.view.ShowTableCollectionView;

/**
 *
 * @author ambargus
 */
public class ShowTableCollectionController {
    
    ShowTableCollectionView view;
    ShowTableCollection model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public ShowTableCollectionController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new ShowTableCollectionView(parentView, true, this);
        this.metadataMu = metadata;
        this.view.setMetadataMu(this.metadataMu);
        this.calculationService = MuARGUS.getCalculationService();
    }
    
    public void showView() {
        this.view.setVisible(true);
    }
    
    public void close() {
        this.view.setVisible(false);
    }
    
    public void setModel(ShowTableCollection model){
        this.model = model;
    }
    
    public void setAllTables(){
        ArrayList<TableMu> tables = new ArrayList<>();
        this.setDimensions();
        //for(int i = 0; );
        
        this.model.setAllTables(tables);
    }
    
    public void setDimensions(){
        int dimensions = 0;
        for(TableMu t: this.model.getOriginalTables()){
            if(t.getVariables().size() > dimensions){
                dimensions = t.getVariables().size();
            }
        }
        this.model.setDimensions(dimensions);
    }
    
}
