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

    /**
     *
     * @param variableName
     * @return
     */
    public PramVariableSpec getSelectedPramVarSpec(String variableName) {
        PramVariableSpec temp = null;
        for (PramVariableSpec p : this.model.getPramVarSpec()) {
            if (p.getVariable().getName().equals(variableName)) {
                temp = p;
            }
        }
        return temp;
    }

    /**
     *
     */
    public void makePramVariableSpecs() {
        ArrayList<PramVariableSpec> pramVarSpec = new ArrayList<>();
        for (VariableMu v : this.metadataMu.getVariables()) {
            if (v.isCategorical()) {
                PramVariableSpec p = new PramVariableSpec(v);
                pramVarSpec.add(p);
            }
        }
        this.model.setPramVarSpec(pramVarSpec);
    }

    /**
     *
     */
    public void makeVariablesData() {
        ArrayList<PramVariableSpec> pramVarSpec = this.model.getPramVarSpec();
        String[][] variablesData = new String[pramVarSpec.size()][3];
        int index = 0;
        for (PramVariableSpec p : pramVarSpec) {
            variablesData[index][0] = p.getAppliedText();
            if (p.isApplied() && this.model.useBandwidth()) {
                variablesData[index][1] = Integer.toString(p.getBandwidth());
            } else {
                variablesData[index][1] = "";
            }
            variablesData[index][2] = p.getVariable().getName();
            index++;
        }
        this.model.setVariablesData(variablesData);
    }

    public void setBandwidth() {
        for (VariableMu v : this.metadataMu.getVariables()) {
            if (v.isCategorical()) {
                int max = v.getCodeInfos().size() - v.getNumberOfMissings();
                int value;
                if (max > 5) {
                    value = 5;
                } else {
                    value = max;
                }
                getPramVariableSpec(v).setBandwidth(value);
            }

        }
    }

    public PramVariableSpec getPramVariableSpec(VariableMu variable) {
        PramVariableSpec temp = null;
        for (PramVariableSpec p : this.model.getPramVarSpec()) {
            if (p.getVariable().equals(variable)) {
                temp = p;
                break;
            }
        }
        return temp;
    }

    /**
     *
     * @param variableName
     */
    public String[][] getCodesData(String variableName) {
        PramVariableSpec pramVariableSpec = getSelectedPramVarSpec(variableName);
        VariableMu variable = null;
        for (VariableMu v : this.metadataMu.getVariables()) {
            if (v.getName().equals(variableName)) {
                variable = v;
            }
        }
        System.out.println(variable.getName());

        String[][] codesData = null;
        if (variable != null) {
            ArrayList<CodeInfo> codeInfo = variable.getCodeInfos();
            int numberOfCodes = codeInfo.size() - variable.getNumberOfMissings();
            codesData = new String[numberOfCodes][3];

            for (int i = 0; i < numberOfCodes; i++) {
                codesData[i][0] = codeInfo.get(i).getCode();
                codesData[i][1] = codeInfo.get(i).getLabel();
                codesData[i][2] = Integer.toString(codeInfo.get(i).getPramProbability());
            }

            //pramVariableSpec.setCodesData(codesData);
        }
        return codesData;
    }

    public boolean areAllProbabilitiesZero(PramVariableSpec pramVariableSpec) {
        boolean valid = true;
        for (CodeInfo c : pramVariableSpec.getVariable().getCodeInfos()) {
            if (c.getPramProbability() > 0) {
                valid = false;
            }
        }
        return valid;
    }
}
