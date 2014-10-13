/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.model.CodeInfo;
import muargus.model.MetadataMu;
import muargus.model.PramSpecification;
import muargus.model.PramVariableSpec;
import muargus.model.VariableMu;
import muargus.view.PramSpecificationView;

/**
 *
 * @author Statistics Netherlands
 */
public class PramSpecificationController extends ControllerBase<PramSpecification> {

    private final MetadataMu metadata;

    public PramSpecificationController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new PramSpecificationView(parentView, true, this));
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getPramSpecification());
        getView().setMetadata(this.metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     *
     * @param variableName
     * @return
     */
    public PramVariableSpec getSelectedPramVarSpec(String variableName) {
        PramVariableSpec temp = null;
        for (PramVariableSpec p : getModel().getPramVarSpec()) {
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
        if (getModel().getPramVarSpec().isEmpty()) {
            for (VariableMu v : this.metadata.getVariables()) {
                if (v.isCategorical()) {
                    PramVariableSpec p = new PramVariableSpec(v);
                    getModel().getPramVarSpec().add(p);
                }
            }
        }
    }

    /**
     *
     */
    public void makeVariablesData() {
        ArrayList<PramVariableSpec> pramVarSpec = getModel().getPramVarSpec();
        String[][] variablesData = new String[pramVarSpec.size()][3];
        int index = 0;
        for (PramVariableSpec p : pramVarSpec) {
            variablesData[index][0] = p.getAppliedText();
            variablesData[index][1] = p.getBandwidthText();
            if (p.isApplied() && p.useBandwidth()) {
                variablesData[index][1] = Integer.toString(p.getBandwidth());
            } else {
                variablesData[index][1] = "";
            }
            variablesData[index][2] = p.getVariable().getName();
            index++;
        }
        getModel().setVariablesData(variablesData);
    }

    /**
     *
     */
    public void setBandwidth() {
        for (VariableMu v : this.metadata.getVariables()) {
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

    /**
     *
     * @param variable
     * @return
     */
    public PramVariableSpec getPramVariableSpec(VariableMu variable) {
        PramVariableSpec temp = null;
        for (PramVariableSpec p : getModel().getPramVarSpec()) {
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
        VariableMu variable = pramVariableSpec.getVariable();

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
        }
        return codesData;
    }

    /**
     *
     * @param pramVariableSpec
     * @return
     */
    public boolean areAllProbabilitiesZero(PramVariableSpec pramVariableSpec) {
        boolean valid = true;
        for (CodeInfo c : pramVariableSpec.getVariable().getCodeInfos()) {
            if (c.getPramProbability() > 0) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Apply the pram calculation. This method calls the external dll to apply
     * the PRAM-calculations
     *
     * @param pramVariableSpec instance of the pramVariableSpec class of one
     * variable containing the specific information necessary for
     * PRAM-calculations
     */
    public void apply(PramVariableSpec pramVariableSpec) {
        try {
            getCalculationService().setPramVariable(pramVariableSpec);
        } catch (ArgusException ex) {
            Logger.getLogger(PramSpecificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Undo the pram calculation. This method calls the external dll to undo the
     * PRAM-calculations
     *
     * @param pramVariableSpec instance of the pramVariableSpec class of one
     * variable containing the specific information necessary for
     * PRAM-calculations
     */
    public void undo(PramVariableSpec pramVariableSpec) {
        try {
            getCalculationService().undoSetPramVariable(pramVariableSpec);
        } catch (ArgusException ex) {
            Logger.getLogger(PramSpecificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
