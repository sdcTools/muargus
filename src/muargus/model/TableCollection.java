package muargus.model;

import java.util.ArrayList;

/**
 * The model class of the ShowTableCollection screen.
 *
 * @author ambargus
 */
public class TableCollection {

    private boolean showAllTables;
    private VariableMu selectedVariable;
    private ArrayList<VariableMu> variables;
    private ArrayList<TableMu> originalTables;
    private ArrayList<TableMu> allTables;
    private String[] columnNames;
    private String[][] data;
    private String[][] subdata;
    private int dimensions;

    /**
     * Constructor of the model class TableCollection. Makes a new ArraLists for
     * originalTables and allTables
     */
    public TableCollection() {
        this.originalTables = new ArrayList<>();
        this.allTables = new ArrayList<>();
    }

    /**
     * Gets the variables that are being used in the tables.
     *
     * @return ArrayList of the variables that are being used in the tables.
     */
    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    /**
     * Sets the variables that are being used in the tables.
     *
     * @param variables ArrayList containing all the variables that are used in
     * the tables.
     */
    public void setVariables(ArrayList<VariableMu> variables) {
        this.variables = variables;
    }

    /**
     * Returns if all tables should be shown or not.
     *
     * @return showAllTables: a boolean telling if all tables should be shown.
     */
    public boolean isShowAllTables() {
        return showAllTables;
    }

    /**
     * Sets if all tables should be shown or not.
     *
     * @param showAllTables boolean telling if all tables should be shown.
     */
    public void setShowAllTables(boolean showAllTables) {
        this.showAllTables = showAllTables;
    }

    /**
     * Gets the ArrayList containing all tables.
     *
     * @return allTables: ArrayList containing all tables.
     */
    public ArrayList<TableMu> getAllTables() {
        return allTables;
    }

    /**
     * Sets the ArrayList containing all tables.
     *
     * @param allTables ArrayList containing all tables.
     */
    public void setAllTables(ArrayList<TableMu> allTables) {
        this.allTables = allTables;
    }

    /**
     * Gets the maximum number of dimensions used in all the tables.
     *
     * @return the maximum number of dimensions used in all the tables.
     */
    public int getDimensions() {
        return dimensions;
    }

    /**
     * Sets the maximum number of dimensions used in all the tables.
     *
     * @param dimensions the maximum number of dimensions used in all the
     * tables.
     */
    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Gets the selected variable of which tables should be shown.
     *
     * @return the selected variable of which tables should be shown.
     */
    public VariableMu getSelectedVariable() {
        return selectedVariable;
    }

    /**
     * Sets the selected variable of which tables should be shown.
     *
     * @param selectedVariable the selected variable of which tables should be
     * shown.
     */
    public void setSelectedVariable(VariableMu selectedVariable) {
        this.selectedVariable = selectedVariable;
    }

    /**
     * Gets the ArrayList containing the original tables specified in
     * SelectCombinations.
     *
     * @return the ArrayList containing the original tables specified in
     * SelectCombinations.
     */
    public ArrayList<TableMu> getOriginalTables() {
        return this.originalTables;
    }

    /**
     * Sets the ArrayList containing the original tables specified in
     * SelectCombinations.
     *
     * @param tables ArrayList containing the original tables specified in
     * SelectCombinations.
     */
    public void setOriginalTables(ArrayList<TableMu> tables) {
        this.originalTables = tables;
    }

    /**
     * Gets the Array containing all the column names.
     *
     * @return Array containing all the column names.
     */
    public String[] getColumnNames() {

        return this.columnNames;
    }

    /**
     * Sets the Array containing all the column names.
     *
     * @param columnNames Array containing all the column names.
     */
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    /**
     * Gets the double Array containing all the data from all tables.
     *
     * @return the double Array containing all the data from all tables.
     */
    public String[][] getData() {
        return this.data;
    }

    /**
     * Sets the double Array containing all the data from all tables.
     *
     * @param data the double Array containing all the data from all tables.
     */
    public void setData(String[][] data) {
        this.data = data;
    }

    /**
     * Gets the double Array containing the data from the tables that will be
     * displayed.
     *
     * @return the double Array containing the data from the tables that will be
     * displayed.
     */
    public String[][] getSubdata() {
        return subdata;
    }

    /**
     * Sets the double Array containing the data from the tables that will be
     * displayed.
     *
     * @param subdata the double Array containing the data from the tables that
     * will be displayed.
     */
    public void setSubdata(String[][] subdata) {
        this.subdata = subdata;
    }

}
