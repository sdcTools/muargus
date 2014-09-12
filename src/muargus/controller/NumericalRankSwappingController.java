/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.controller;

import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.NumericalRankSwapping;
import muargus.model.ReplacementFile;
import muargus.model.VariableMu;
import muargus.view.NumericalRankSwappingView;

/**
 *
 * @author ambargus
 */
public class NumericalRankSwappingController extends ControllerBase {
    NumericalRankSwappingView view;
    //NumericalRankSwapping model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public NumericalRankSwappingController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.view = new NumericalRankSwappingView(parentView, true, this);
        super.setView(this.view);
        this.metadataMu = metadataMu;
        this.view.setMetadataMu(this.metadataMu);
        this.calculationService = MuARGUS.getCalculationService();
        
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
    
    public void calculate() {
        ArrayList<VariableMu> selectedVariables = view.getSelectedVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!view.showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        try {
            NumericalRankSwapping rankSwapping = new NumericalRankSwapping(view.getPercentage());
            rankSwapping.getVariables().addAll(selectedVariables);
            
            ReplacementFile replacement = new ReplacementFile("RankSwapping");
            replacement.getVariables().addAll(selectedVariables);
            this.metadataMu.getReplacementFiles().add(replacement);
            this.calculationService.makeReplacementFile(this);
            //TODO: de catalaan
        }
        catch (ArgusException ex) {
            view.showErrorMessage(ex);
        }
    }
    
    private boolean variablesAreUsed(ArrayList<VariableMu> variables) {
        for (VariableMu variable : variables) {
            for (ReplacementFile replacement : this.metadataMu.getReplacementFiles()) {
                if (replacement.getVariables().contains(variable)) {
                    return true;
                }
            }
        }
        return false;
    }

    
    
}
