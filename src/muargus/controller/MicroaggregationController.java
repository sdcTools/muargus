package muargus.controller;

import argus.model.ArgusException;
import java.util.ArrayList;
import muargus.extern.dataengine.Numerical;
import muargus.model.MetadataMu;
import muargus.model.MicroaggregationSpec;
import muargus.model.Microaggregation;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.VariableMu;
import muargus.view.MicroaggregationView;

/**
 * The Controller class of the Microaggregation screen.
 *
 * @author Statistics Netherlands
 */
public class MicroaggregationController extends ControllerBase<Microaggregation> {

    private final MetadataMu metadata;

    /**
     * Constructor for the MicroaggregationController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public MicroaggregationController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new MicroaggregationView(parentView, true, this));
        this.metadata = metadata;
        fillModel();
        getView().setMetadata(this.metadata);
    }

    /**
     * Gets the model and fills the model with the numeric variables if the
     * model is empty.
     */
    private void fillModel() {
        Microaggregation model = this.metadata.getCombinations().getMicroaggregation();
        if (model.getVariables().isEmpty()) {
            for (VariableMu variable : this.metadata.getVariables()) {
                if (variable.isNumeric()) {
                    model.getVariables().add(variable);
                }
            }
        }
        setModel(model);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     * Does the next step if the previous step was succesful.
     *
     * @param success Boolean indicating whether the previous step was
     * succesful.
     */
    @Override
    protected void doNextStep(boolean success) {
        MicroaggregationSpec microaggregation = getModel().getMicroaggregations().get(getModel().getMicroaggregations().size() - 1);
        Numerical num = new Numerical();
        int[] errorCode = new int[1];
        int[] nColPerGroup = new int[]{microaggregation.getVariables().size()};
        num.DoMicroAggregation(microaggregation.getReplacementFile().getInputFilePath(),
                microaggregation.getReplacementFile().getOutputFilePath(),
                muargus.MuARGUS.getDefaultSeparator(),
                microaggregation.getVariables().size(),
                microaggregation.getMinimalNumberOfRecords(),
                1,
                microaggregation.isOptimal() ? 1 : 0,
                nColPerGroup,
                errorCode);
        if (errorCode[0] != 0) {
            getView().showErrorMessage(new ArgusException("Error during MicroAggregation"));
        }
        getView().showMessage("Micro aggregation successfully completed");
        getView().setProgress(0);
        getView().showStepName("");
        getMicroaggregationView().updateVariableRows(microaggregation);
    }

    /**
     * Gets the Microaggregation view.
     *
     * @return MicroaggregationView
     */
    private MicroaggregationView getMicroaggregationView() {
        return (MicroaggregationView) getView();
    }

    /**
     * Checks whether there are enough records per group.
     *
     * @return Boolean indicating whether there are enough records per group.
     */
    private boolean checkFields() {
        int nRecords = getMicroaggregationView().getMinimalNumberOfRecords();
        if (nRecords < 2) {
            getView().showErrorMessage(new ArgusException("Illegal value for Minimum Number of Records per Group"));
            return false;
        }
        return true;
    }

    /**
     * Undo's the microaggregation.
     */
    public void undo() {
        ArrayList<VariableMu> selected = getMicroaggregationView().getSelectedVariables();
        if (selected.isEmpty()) {
            return;
        }
        if (!getView().showConfirmDialog(String.format("The micro aggregation involving %s will be removed. Continue?",
                VariableMu.printVariableNames(selected)))) {
            return;
        }
        for (MicroaggregationSpec microaggregation : getModel().getMicroaggregations()) {
            if (microaggregation.getVariables().size() == selected.size()) {
                boolean difference = false;
                for (VariableMu variable : microaggregation.getVariables()) {
                    if (!selected.contains(variable)) {
                        difference = true;
                        break;
                    }
                }
                if (!difference) {
                    getModel().getMicroaggregations().remove(microaggregation);
                    this.metadata.getReplacementSpecs().remove(microaggregation);
                    getMicroaggregationView().updateVariableRows(microaggregation);
                    return;
                }
            }
        }
        getView().showMessage(String.format("Micro aggregation involving %s not found", VariableMu.printVariableNames(selected)));
    }

    /**
     * Calculates the microaggregation.
     */
    public void calculate() {
        if (!checkFields()) {
            return;
        }
        ArrayList<VariableMu> selectedVariables = getMicroaggregationView().getSelectedVariables();
        if (variablesAreUsed(selectedVariables)) {
            if (!getView().showConfirmDialog("One or more of the variables are already modified. Continue?")) {
                return;
            }
        }
        try {
            MicroaggregationSpec microaggregation = new MicroaggregationSpec(
                    getMicroaggregationView().getMinimalNumberOfRecords(),
                    getMicroaggregationView().getOptimal());
            //numerical);
            microaggregation.getVariables().addAll(selectedVariables);
            microaggregation.setReplacementFile(new ReplacementFile("Microaggregation"));
            getModel().getMicroaggregations().add(microaggregation);
            this.metadata.getReplacementSpecs().add(microaggregation);
            getCalculationService().makeReplacementFile(this);
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Returns whether at least one variable is used.
     *
     * @param variables Arraylist of VariableMu's.
     * @return Boolean indicating whether at least one variable is used.
     */
    private boolean variablesAreUsed(ArrayList<VariableMu> variables) {
        for (VariableMu variable : variables) {
            for (ReplacementSpec replacement : this.metadata.getReplacementSpecs()) {
                if (replacement.getVariables().contains(variable)) {
                    return true;
                }
            }
        }
        return false;
    }

}
