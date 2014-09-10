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
import muargus.model.ModifyNumericalVariablesSpec;
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
    public void setModifyNumericalVariablesSpecs() {
        ArrayList<ModifyNumericalVariablesSpec> specs = new ArrayList<>();
        for (VariableMu v : this.metadataMu.getVariables()) {
            if (v.isNumeric()) {
                ModifyNumericalVariablesSpec spec = new ModifyNumericalVariablesSpec(v);
                specs.add(spec);
            }
        }
        this.model.setModifyNumericalVariablesSpec(specs);
    }

    public void setVariablesData() {
        if (this.model.getVariablesData() == null) {
            String variablesData[][] = new String[this.model.getModifyNumericalVariablesSpec().size()][2];
            int index = 0;
            for (ModifyNumericalVariablesSpec m : this.model.getModifyNumericalVariablesSpec()) {
                variablesData[index][0] = (m.isModified() ? "X" : "");
                variablesData[index][1] = m.getVariable().getName();
                index++;
            }
            this.model.setVariablesData(variablesData);
        }
    }

    public double[] getMinMax(VariableMu variable) {
        double[] min_max = this.calculationService.getMinMax(variable);
        return min_max;
    }

    public String getMin(ModifyNumericalVariablesSpec selected) {
        double min_double = selected.getMin();
        String min_String;
        if ((min_double == Math.floor(min_double)) && !Double.isInfinite(min_double)) {
            int min_int = (int) min_double;
            min_String = Integer.toString(min_int);
        } else {
            min_String = Double.toString(min_double);
        }
        return min_String;
    }

    public String getMax(ModifyNumericalVariablesSpec selected) {
        double max_double = selected.getMin();
        String max_String;
        if ((max_double == Math.floor(max_double)) && !Double.isInfinite(max_double)) {
            int max_int = (int) max_double;
            max_String = Integer.toString(max_int);
        } else {
            max_String = Double.toString(max_double);
        }
        return max_String;
    }
}
