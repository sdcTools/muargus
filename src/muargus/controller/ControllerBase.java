package muargus.controller;

import muargus.CalculationService;
import argus.model.ArgusException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import muargus.view.DialogBase;

/**
 *
 * @author Statistics Netherlands
 * @param <T>
 */
public class ControllerBase<T> implements PropertyChangeListener {

    private DialogBase viewBase;
    private String stepName;
    private T model;

    /**
     *
     */
    public ControllerBase() {
    }

    /**
     *
     * @return
     */
    protected T getModel() {
        return model;
    }

    /**
     * Sets the Model class.
     * @param model
     */
    protected void setModel(T model) {
        this.model = model;
    }

    /**
     * Property change action. Executes the appropriate command after a
     * proertyChangeEvent has been fired.
     *
     * @param pce PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        switch (pce.getPropertyName()) {
            case "stepName":
                setCurrentStepName(pce.getNewValue().toString());
                break;
            case "progress":
                this.viewBase.setProgress((int) pce.getNewValue());
                break;
            case "result":
                boolean success = "success".equals(pce.getNewValue());
                doNextStep(success);
                break;
            case "error":
                //the new value always contains an ArgusException with the error details
                this.viewBase.showErrorMessage((ArgusException) pce.getNewValue());
                break;
        }
    }

    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        getView().setVisible(true);
    }

    /**
     * Gets the calculation service. The CalculationService contains methods
     * that call external dll's.
     *
     * @return CalculationsService instance.F
     */
    protected CalculationService getCalculationService() {
        return muargus.MuARGUS.getCalculationService();
    }

    /**
     * Sets the view belonging to the controller.
     *
     * @param view View belonging to the controller.
     */
    protected void setView(DialogBase view) {
        this.viewBase = view;
    }

    /**
     * Gets the view belonging to the controller.
     *
     * @return View belonging to the controller.
     */
    protected DialogBase getView() {
        return this.viewBase;
    }

    /**
     * Does the next step if the previous step was succesful.
     *
     * @param success Boolean indicating whether the previous step was
     * succesful.
     */
    protected void doNextStep(boolean success) {
        //Base class implementation is empty
    }

    /**
     * Sets the name of the current step.
     *
     * @param stepName String containing the name of the current step.
     */
    private void setCurrentStepName(String stepName) {
        this.stepName = stepName;
        this.viewBase.showStepName(this.stepName);
    }

    /**
     * Gets the name of the current step.
     *
     * @return String containing the name of the current step.
     */
    protected String getStepName() {
        return this.stepName;
    }

//    public double[][] readVariablesFromFile(File file, String separator, int nVariables) throws ArgusException {
//        NumberFormat format = NumberFormat.getInstance(MuARGUS.getLocale());
//        ArrayList<double[]> records = new ArrayList();
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] fields = line.split(separator);
//                if (fields.length != nVariables) {
//                    throw new ArgusException("Incorret number of fields");
//                }
//                double[] doubles = new double[nVariables];
//                for (int index=0; index < nVariables; index++) {
//                    doubles[index] = format.parse(fields[index]).doubleValue();
//                }
//                records.add(doubles);
//            }
//        }
//        catch (IOException | ParseException ex) {
//            throw new ArgusException("Error reading replacement file: " + ex.getMessage());
//        }
//        double[][] arr = new double[records.size()][];
//        return records.toArray(arr);
//    }
}
