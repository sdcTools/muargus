package muargus.controller;

import muargus.CalculationService;
import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.SelectCombinationsView;

/**
 * The Controller class of the SelectCombinations screen.
 *
 * @author Statistics Netherlands
 */
public class SelectCombinationsController extends ControllerBase<Combinations> {

    private final MetadataMu metadata;

    /**
     * Constructor for the SelectCombinationsController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SelectCombinationsController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SelectCombinationsView(parentView, true, this));
        this.metadata = metadata;

        getSettings();
        setModel(new Combinations(this.metadata.getCombinations()));

        getView().setMetadata(this.metadata);

        getSelectCombinationsView().setModel(getModel()); // the view gets a copy of the current Combinations
    }

    /**
     * Gets the SelectCombinationsView.
     *
     * @return SelectCombinationsView.
     */
    private SelectCombinationsView getSelectCombinationsView() {
        return (SelectCombinationsView) getView();
    }

    /**
     * Calculates the unsafe combinations for all tables.
     *
     * @throws ArgusException Throws an argusException when an error occurs
     * during setMetadata and/or exploreFile.
     */
    public void calculateTables() throws ArgusException {
        getSelectCombinationsView().enableCalculateTables(false);
        saveSettings();
        this.metadata.setCombinations(getModel());
        if (this.metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS) {
            MuARGUS.getSpssUtils().generateSpssData(this.metadata);
        }
        CalculationService service = MuARGUS.getCalculationService();
        service.setMetadata(this.metadata);
        service.exploreFile(this);
    }

    /**
     * Clears the tables.
     */
    public void clear() {
        int size = getModel().getTables().size();
        for (int i = size - 1; i >= 0; i--) {
            getModel().removeTable(i);
        }
    }

    /**
     * Checks if a threshold is valid.
     *
     * @param thresholdText String containing the user input for the threshold.
     * @return Boolean indicating whether the threshold is valid.
     */
    public boolean validThreshold(String thresholdText) {
        boolean valid;
        try {
            int threshold = Integer.parseInt(thresholdText);
            valid = threshold > 0;
        } catch (NumberFormatException e) {
            valid = false;
        }
        return valid;
    }

    /**
     * This function compares the different tables with a new table (VariableMu
     * array) if the table is different (enough), which depends on the
     * riskmodel, it returns true if the table has to much overlap, it returns
     * false
     *
     * @param tableMuNew TableMu instance of new table that will be added if the
     * table is valid.
     * @param tableMuOld TableMu instance of the already existing table
     * @return It returns if a table can be added
     */
    public boolean compareRows(TableMu tableMuNew, TableMu tableMuOld) {
        boolean isValid = true;
        int numberOfDoubleVariables = 0;

        for (VariableMu oldVariable : tableMuOld.getVariables()) {
            for (VariableMu newVariable : tableMuNew.getVariables()) {
                if (oldVariable.equals(newVariable)) {
                    numberOfDoubleVariables++;
                    if (tableMuOld.isRiskModel()) {
                        return false;
                    }
                }
            }
        }
        if (tableMuNew.getVariables().size() == tableMuOld.getVariables().size()
                && numberOfDoubleVariables == tableMuNew.getVariables().size()) {
            tableMuOld.setThreshold(tableMuNew.getThreshold());
            isValid = false;
        }
        return isValid;
    }

    /**
     * Starts the recursion equation that calculates the tables for the
     * specified number of dimensions. Assings an empty Arraylist of
     * variableMu's, the starting position and the threshold.
     *
     * @param allVariables ArrayList of VariableMu's containing all variables.
     * @param dimensions Integer containing the number of dimensions.
     */
    public void calculateTablesForDimensions(ArrayList<VariableMu> allVariables, int dimensions) {
        ArrayList<VariableMu> variableSubset = new ArrayList<>();
        int startPos = 0;
        int threshold = 0;
        calculateTablesForDimensions(startPos, allVariables, dimensions, variableSubset, threshold);
    }

    /**
     * Main body of the recursion equation that calculates the tables for the
     * specified number of dimensions. This recursion equation starts with
     * looping through all variables. Whithin the loop the equation adds the
     * variable to the varibleSubset, makes a table, calls itself and passes the
     * variableSubset on. Every time this equation calls itself the
     * variableSubset will add a variable until the number of dimensions are
     * higher than the number of calls of this function.
     *
     * @param startPos
     * @param allVariables ArrayList of VariableMu's containing all variables.
     * @param dimensions Integer containing the number of dimensions.
     * @param variableSubset ArrayList of VariableMu's containing between one
     * variable and as many variables as the number of dimensions.
     * @param threshold Integer containing the threshold for the specified
     * dimension.
     */
    private void calculateTablesForDimensions(int startPos, ArrayList<VariableMu> allVariables, int dimension,
            ArrayList<VariableMu> variableSubset, int threshold) {
        if (dimension > 0) {
            for (int i = startPos; i < allVariables.size(); i++) {
                //make variable array 
                ArrayList<VariableMu> temp = new ArrayList<>();
                VariableMu s = allVariables.get(i);
                temp.addAll(variableSubset);
                temp.add(s);
                sort(temp);

                //Make table, add the variable array and add this table to the table array
                TableMu tableMu = new TableMu();
                tableMu.getVariables().addAll(temp);
                tableMu.setThreshold(getModel().getThresholds()[threshold]);
                getModel().getTables().add(tableMu);

                // call this method again
                calculateTablesForDimensions(i + 1, allVariables, dimension - 1, temp, threshold + 1);
            }
        }
    }

    /**
     * Starts the recursion equation that calculates the tables for the ID
     * levels.
     *
     * @param numberOfLevels Integer containing the number of ID-levels with at
     * least one variable.
     * @param variables ArrayList containing ArrayList's of VariableMu's for
     * each ID-level. variables.
     * @param allValidVariables ArrayList of VariableMu's containing all
     * variables with an ID-level higher than 0
     */
    public void calculateTablesForID(int numberOfLevels, ArrayList<ArrayList<VariableMu>> variables, ArrayList<VariableMu> allValidVariables) {
        int index = 1; // don't add the variables with an ID number of 0
        int _size = 0;
        int currentLevel = 0;
        ArrayList<VariableMu> variableSubset = new ArrayList<>();

        calculateTablesForID(0, index, _size, currentLevel, variableSubset, numberOfLevels, variables, allValidVariables);
    }

    /**
     * Starts the recursion equation that calculates the tables for the ID
     * levels. This equation first checks whether the number of ID-levels with
     * at least one variable that has been processed in this equation
     * (_currentLevel) is smaller or equal to the maximum number of ID-levels
     * with at least one variable. If this is the case, the next idLevel larger
     * than zero will be found and the number of variables whith that idLevel
     * will be added to the size. The size contains the amount of variables up
     * to the current ID-level. Following the equation will loop through all
     * variables up to the current id-level excluding variables that have
     * already been used. During each loop the current variable is added to a
     * tempory array and if the size of this array equals the numberOfLevels, a
     * table is added.
     *
     * @param _i Integer containing the starting point of the for loop.
     * @param _index Integer containing the index of the next to be searched
     * ArrayList. In this method it is checked if the next ArrayList with this
     * index has more than 0 variables.
     * @param _size Integer containing the number of variables up to the
     * corresponding ID-Level
     * @param _currentLevel Integer containing the current ID-level.
     * @param variableSubset ArrayList of VariableMu's containing between one
     * variable and as many variables as the number of different ID-levels.
     * @param numberOfLevels Integer containing the number of ID-levels with at
     * least one variable.
     * @param variables ArrayList containing ArrayList's of VariableMu's for
     * each ID-level. variables.
     * @param allValidVariables ArrayList of VariableMu's containing all
     * variables with an ID-level higher than 0
     */
    private void calculateTablesForID(int _i, int _index, int _size, int _currentLevel, ArrayList<VariableMu> variableSubset,
            int numberOfLevels, ArrayList<ArrayList<VariableMu>> variables, ArrayList<VariableMu> allVariables) {

        int currentLevel = _currentLevel + 1;
        if (currentLevel <= numberOfLevels) {
            int index = _index;
            int size = _size;

            // find the next idLevel larger than zero and add the number of variables to the size
            for (int u = index; u < variables.size(); u++) {
                if (variables.get(u).size() > 0) {
                    size = size + variables.get(u).size();
                    index = u + 1;
                    break;
                }
            }

            // loop through all variables and add a table if its size equals the number of id-levels with at least one variable.
            for (int i = _i; i < size; i++) {
                ArrayList<VariableMu> temp = new ArrayList<>();
                temp.addAll(variableSubset);
                temp.add(allVariables.get(i));
                sort(temp);

                if (temp.size() == numberOfLevels) {
                    TableMu tableMu = new TableMu();
                    tableMu.getVariables().addAll(temp);
                    tableMu.setThreshold(getModel().getThreshold());
                    getModel().addTable(tableMu);
                }
                // call this method again
                calculateTablesForID(i + 1, index, size, currentLevel, temp, numberOfLevels, variables, allVariables);
            }
        }
    }

    /**
     * Sorts an Arraylist of VariableMu's on the startingposition.
     *
     * @param variables ArrayList of VariableMu's.
     */
    private void sort(ArrayList<VariableMu> variables) {
        Collections.sort(variables, new Comparator<VariableMu>() {
            @Override
            public int compare(VariableMu v1, VariableMu v2) {
                if (metadata.getVariables().indexOf(v1) < metadata.getVariables().indexOf(v2)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * Gets a list of to be removed tables.
     *
     * @return Arraylist of TableMu's containing the removed tables.
     */
    public ArrayList<TableMu> getListOfRemovedTables() {
        ArrayList<TableMu> toBeRemovedTables = new ArrayList<>();

        for (int i = getModel().getNumberOfRows() - 1; i >= 0; i--) {
            TableMu t = getModel().getTables().get(i);
            if (!t.isRiskModel()) {
                if (t.contains(getModel().getRiskModelVariables())) {
                    toBeRemovedTables.add(t);
                }
            }
        }
        return toBeRemovedTables;
    }

    /**
     * Returns whether a weight variable exists.
     *
     * @return Boolean indicating whether a weight variable exists.
     */
    public boolean weightVariableExists() {
        for (VariableMu variable : this.metadata.getVariables()) {
            if (variable.isWeight()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there are overlapping tables and askes if they need to be
     * removed.
     *
     * @param toBeRemovedTables ArrayList of TableMu's that overlap the given
     * tableMu.
     * @param tableMu TableMu instance that overlap the toBeRemovedTables.
     * @return Boolean indicating whether the overlapping tables will be
     * removed.
     */
    public boolean overlappingTables(ArrayList<TableMu> toBeRemovedTables, TableMu tableMu) {
        boolean valid = false;
        if (toBeRemovedTables.size() > 0) {
            if (JOptionPane.showConfirmDialog(getView(), "Overlapping tables found with this risk table\nDo you want to remove them?",
                    "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                tableMu.setRiskModel(!tableMu.isRiskModel());  //Revert the change
                valid = true;
            }
        }
        return valid;
    }

    /**
     * Removes overlapping tables.
     *
     * @param toBeRemovedTables ArrayList of TableMu's containing the tables
     * that will be removed.
     */
    public void removeTableRiskModel(ArrayList<TableMu> toBeRemovedTables) {
        for (TableMu t : toBeRemovedTables) {
            getModel().removeTable(t);
        }
    }

    /**
     * Gets the settings for the thresholds from the registry.
     */
    private void getSettings() {
        int[] thresholds = new int[MuARGUS.MAXDIMS];
        for (int t = 1; t <= MuARGUS.MAXDIMS; t++) {
            thresholds[t - 1] = SystemUtils.getRegInteger("general", "threshold" + Integer.toString(t), 1);
        }
        this.metadata.getCombinations().setThresholds(thresholds);
    }

    /**
     * Saves the settings for the threshold to the registry.
     */
    private void saveSettings() {
        int[] thresholds = this.metadata.getCombinations().getThresholds();
        if (thresholds == null || thresholds.length < MuARGUS.MAXDIMS) {
            return;
        }
        for (int t = 1; t <= MuARGUS.MAXDIMS; t++) {
            SystemUtils.putRegInteger("general", "threshold" + Integer.toString(t), thresholds[t - 1]);
        }
    }

    /**
     * Closes the view.
     */
    public void cancel() {
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
        if (success && getStepName().equals("ExploreFile")) {
            MuARGUS.getCalculationService().calculateTables(this);
        } else {
            getSelectCombinationsView().enableCalculateTables(true);
            if (success) {
                getView().setVisible(false);
            }
        }
    }

}
