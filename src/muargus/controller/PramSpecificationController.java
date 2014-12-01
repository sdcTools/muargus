package muargus.controller;

import argus.model.ArgusException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.model.CodeInfo;
import muargus.model.MetadataMu;
import muargus.model.PramSpecification;
import muargus.model.PramVariableSpec;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.PramSpecificationView;

/**
 * The Controller class of the PramSpecification screen.
 *
 * @author Statistics Netherlands
 */
public class PramSpecificationController extends ControllerBase<PramSpecification> {

    private final MetadataMu metadata;

    /**
     * Constructor for the PramSpecificationController
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
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
     * Gets the PramVariableSpec of to the selected variable.
     *
     * @param variableName String containing the variable name for which the
     * PramvariableSpec is requested.
     * @return PramVariableSpec instance of to the selected variable.
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
     * Makes a PramVariableSpec for all variable that are categorical and for
     * which no risk model is set.
     */
    public void makePramVariableSpecs() {
        ArrayList<VariableMu> riskVariables = new ArrayList<>();
        for (TableMu table : this.metadata.getCombinations().getTables()) {
            if (table.isRiskModel()) {
                for (VariableMu variable : table.getVariables()) {
                    if (!riskVariables.contains(variable)) {
                        riskVariables.add(variable);
                    }
                }
            }
        }
        if (getModel().getPramVarSpec().isEmpty()) {
            for (VariableMu v : this.metadata.getVariables()) {
                if (!riskVariables.contains(v) && v.isCategorical()) {
                    PramVariableSpec p = new PramVariableSpec(v);
                    getModel().getPramVarSpec().add(p);
                }
            }
        }
    }

    /**
     * Makes a double String array containing the data for the variables table.
     * Each row contains an indicator indicating whether the variable has been
     * applied, the bandwidth used and the variable name.
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
     * Sets the bandwidth.
     */
    public void setBandwidth() {
        for (PramVariableSpec p : getModel().getPramVarSpec()) {
            int max = p.getVariable().getCodeInfos().size() - p.getVariable().getNumberOfMissings();
            int value;
            if (max > 5) {
                value = 5;
            } else {
                value = max;
            }
            p.setBandwidth(value);
        }
    }

    /**
     * Gets a double Object array containing the data for the codes table. Each
     * row contains the code, it's label and the PRAM probability.
     *
     * @param variableName String containing the variable name for which the
     * codesData should be generated.
     * @return Double Array of Objects containing the data for the codes table.
     * Each row contains the code, it's label and the PRAM probability.
     */
    public Object[][] getCodesData(String variableName) {
        PramVariableSpec pramVariableSpec = getSelectedPramVarSpec(variableName);
        VariableMu variable = pramVariableSpec.getVariable();

        Object[][] codesData = null;
        if (variable != null) {
            ArrayList<CodeInfo> codeInfo = variable.getCodeInfos();
            int numberOfCodes = codeInfo.size() - variable.getNumberOfMissings();
            codesData = new Object[numberOfCodes][3];

            for (int i = 0; i < numberOfCodes; i++) {
                codesData[i][0] = codeInfo.get(i).getCode();
                codesData[i][1] = codeInfo.get(i).getLabel();
                codesData[i][2] = codeInfo.get(i).getPramProbability();
            }
        }
        return codesData;
    }

    /**
     * Checks whether all probabilities are zero.
     *
     * @param pramVariableSpec PramVariableSpec instance for which the PRAM
     * probabilities need to be checked.
     * @return Boolean indicating whether all probabilities are zero.
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
