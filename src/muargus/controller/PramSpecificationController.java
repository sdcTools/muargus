/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import java.util.ArrayList;
import muargus.model.CodeInfo;
import muargus.model.MetadataMu;
import muargus.model.PramSpecification;
import muargus.model.PramVariableSpec;
import muargus.model.VariableMu;
import muargus.view.PramSpecificationView;

/**
 *
 * @author ambargus
 */
public class PramSpecificationController {

    PramSpecificationView view;
    PramSpecification model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    public PramSpecificationController(java.awt.Frame parentView, MetadataMu metadataMu) {
        this.view = new PramSpecificationView(parentView, true, this);
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
    public void setModel(PramSpecification model) {
        this.model = model;
    }

    public PramVariableSpec getSelectedPramVarSpec(String variableName) {
        PramVariableSpec temp = null;
        for (PramVariableSpec p : this.model.getPramVarSpec()) {
            if (p.getVariable().getName().equals(variableName)) {
                temp = p;
            }
        }
        return temp;
    }

    public void makePramVariableSpecs() {
        ArrayList<PramVariableSpec> pramVarSpec = new ArrayList<>();
        for (VariableMu v : this.metadataMu.getVariables()) {
            if (v.isCategorical()) {
                PramVariableSpec p = new PramVariableSpec(v);
                //ArrayList<CodeInfo>
                //p.setCodeInfo(null);
                pramVarSpec.add(p);
            }
        }
        this.model.setPramVarSpec(pramVarSpec);
    }

    public void makeVariablesData() {
        ArrayList<PramVariableSpec> pramVarSpec = this.model.getPramVarSpec();
        String[][] variablesData = new String[pramVarSpec.size()][3];
        int index = 0;
        for (PramVariableSpec p : pramVarSpec) {
            variablesData[index][0] = p.getAppliedText();
            if (p.isApplied() && p.useBandwidth()) {
                variablesData[index][1] = Integer.toString(p.getBandwidth());
            } else {
                variablesData[index][1] = "";
            }
            variablesData[index][2] = p.getVariable().getName();
            index++;
        }
        this.model.setVariablesData(variablesData);
    }

    public void makeCodesData(String variableName) {
        PramVariableSpec pramVariableSpec = getSelectedPramVarSpec(variableName);
        ArrayList<CodeInfo> codeInfo = pramVariableSpec.getCodeInfo();
        String[][] codesData = new String[codeInfo.size()][3];
        int index = 0;
        for (CodeInfo c : codeInfo) {
            codesData[index][0] = c.getCode();
            codesData[index][1] = c.getLabel();
            codesData[index][2] = Integer.toString(c.getPramProbability());
            index++;
        }

        pramVariableSpec.setCodesData(codesData);
    }
}