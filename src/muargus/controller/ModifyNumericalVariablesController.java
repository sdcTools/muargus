/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.ModifyNumericalVariables;
import muargus.model.VariableMu;
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
        this.calculationService = MuARGUS.getCalculationService();
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

    /**
     *
     */
    public void setVariables() {
        ArrayList<VariableMu> variables = new ArrayList<>();
        for (VariableMu v : this.metadataMu.getVariables()) {
            if (v.isNumeric()) {
                variables.add(v);
            }
        }
        this.model.setVariables(variables);
    }

    public void setVariablesData() {
        if (this.model.getVariablesData() == null) {
            String variablesData[][] = new String[this.model.getVariables().size()][2];
            int index = 0;
            for (VariableMu v : this.model.getVariables()) {
                variablesData[index][0] = "";
                variablesData[index][1] = v.getName();
                index++;
            }
            this.model.setVariablesData(variablesData);
        }
    }
    
    public double[] getMinMax(VariableMu variable){
        double[] min_max = this.calculationService.getMinMax(variable);
        return min_max;
    }
}
