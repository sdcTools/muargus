package muargus.model;

import java.util.ArrayList;
import java.util.HashMap;
//import muargus.MuARGUS;

/**
 * Model class of the SelectCombinations screen. Only a single instance of this
 * class will exist.
 *
 * @author Statistics Netherlands
 */
public class Combinations {

    private int threshold;
    //private boolean riskModel;
    private ArrayList<TableMu> tables;
    //private VariableMu[] variables;
    private int[] thresholds;
    private HashMap<VariableMu, int[]> unsafeCombinations;
    //private HashMap<VariableMu, UnsafeInfo> unsafe;

    // Model classes that need info from this model class
    private GlobalRecode globalRecode;
    private ProtectedFile protectedFile;
    private TableCollection tableCollection;
    private RiskSpecification riskSpecification;
    private ModifyNumericalVariables modifyNumericalVariables;
    private NumericalMicroaggregation numericalMicroaggregation;
    private NumericalRankSwapping numericalRankSwapping;
    private PramSpecification pramSpecification;

    // tot dit aantal kan die het redelijk goed hebben, maar is die wel +/- 5 seconden aan het rekenen. 
    private final int maximumNumberOfTables = 25000;

    private final int maximumSizeBeforeUserConfirmation = 100;

    /**
     * Constructor of the model class Combinations. Initializes the threshold to
     * one and makes an empty tables arraylist.
     */
    public Combinations() {
        this.threshold = 1;
        this.tables = new ArrayList<>();
    }

    /**
     * Constructor of the model class Combinations that will be used as a clone
     * of the already initiated model class.
     *
     * @param model The original Combinations model.
     */
    public Combinations(Combinations model) {
        this.threshold = model.threshold;
        this.thresholds = model.getThresholds();
        this.tables = new ArrayList<>();
        for (TableMu table : model.tables) {
            this.tables.add(new TableMu(table));
        }
    }

    /**
     * Gets the model class of the GlobalRecode screen.
     *
     * @return Returns the GlobalRecode model class.
     */
    public GlobalRecode getGlobalRecode() {
        return this.globalRecode;
    }

    /**
     * Creates a new instance of the GlobalRecode class and assigns the
     * variables used in the tables from the SelectCombinations.
     */
    public void createGlobalRecode() {
        this.globalRecode = new GlobalRecode();
        this.globalRecode.setVariables(this.getVariablesInTables());
    }

    /**
     * Gets the model class of the ShowTableCollection screen.
     *
     * @return Returns the ShowTableCollection model class.
     */
    public TableCollection getTableCollection() {
        return this.tableCollection;
    }

    /**
     * Creates a new instance of the TableCollecton class and assigns the
     * variables used in the tables from the SelectCombinations.
     */
    public void createTableCollection() {
        this.tableCollection = new TableCollection();
        this.tableCollection.setVariables(this.getVariablesInTables());
    }

    /**
     * Gets the model class of the MakeProtectedFile screen.
     *
     * @return Returns the MakeProtectedFile model class.
     */
    public ProtectedFile getProtectedFile() {
        return this.protectedFile;
    }

    /**
     * Creates a new instance of the ProtectedFile class, assigns the variables
     * used in the tables from the SelectCombinations and sets the riskmodel.
     */
    public void createProtectedFile() {
        this.protectedFile = new ProtectedFile();
        this.protectedFile.setVariables(this.getVariablesInTables());
        this.protectedFile.setRiskModel(this.isRiskModel());
    }

    /**
     * Gets the model class of the PramSpecification screen.
     *
     * @return Returns the PramSpecification model class.
     */
    public PramSpecification getPramSpecification() {
        return pramSpecification;
    }

    /**
     * Creates a new instance of the PramSpecification class.
     */
    public void createPramSpecification() {
        this.pramSpecification = new PramSpecification();
    }

    /**
     * Gets the model class of the RiskSpecification screen.
     *
     * @return Returns the RiskSpecification model class.
     */
    public RiskSpecification getRiskSpecification() {
        return riskSpecification;
    }

    /**
     * Creates a new instance of the RiskSpecification class.
     */
    public void createRiskSpecification() {
        this.riskSpecification = new RiskSpecification();
    }

    /**
     * Gets the model class of the ModifyNumericalVariables screen.
     *
     * @return Returns the ModifyNumericalVariables model class.
     */
    public ModifyNumericalVariables getModifyNumericalVariables() {
        return modifyNumericalVariables;
    }

    /**
     * Creates a new instance of the ModifyNumericalVariables class.
     */
    public void createModifyNumericalVariables() {
        this.modifyNumericalVariables = new ModifyNumericalVariables();
    }

    /**
     * Gets the model class of the NumbericalMicroaggregation screen.
     *
     * @return Returns the NumbericalMicroaggregation model class.
     */
    public NumericalMicroaggregation getNumericalMicroaggregation() {
        return numericalMicroaggregation;
    }

    /**
     * Creates a new instance of the NumbericalMicroaggregation class.
     */
    public void createNumericalMicroaggregation() {
        this.numericalMicroaggregation = new NumericalMicroaggregation();
    }

    /**
     * Gets the model class of the NumericalRankSwapping screen.
     *
     * @return Returns the NumericalRankSwapping model class.
     */
    public NumericalRankSwapping getNumericalRankSwapping() {
        return numericalRankSwapping;
    }

    /**
     * Creates a new instance of the NumericalRankSwapping class.
     */
    public void createNumericalRankSwapping() {
        //this.numericalRankSwapping = new NumericalRankSwapping();
    }

    /**
     * Clearing the unsafeCombinations by assinging a new HashMap.
     */
    public void clearUnsafeCombinations() {
        this.unsafeCombinations = new HashMap<>();
    }

    /**
     * Gets an array of integers containing the unsafeCombinations for each
     * dimensions.
     *
     * @param variable VariableMu instance containing the informating on this
     * particular variable.
     * @return Array of integers containing the number of unsafe combinations
     * per dimension.
     */
    public int[] getUnsafeCombinations(VariableMu variable) {
        return this.unsafeCombinations.get(variable);
    }

    /**
     * Sets the number of unsafe combinations
     *
     * @param variable VariableMu instance containing the informating on this
     * particular variable.
     * @param unsafeCount Array of integers containing the number of unsafe
     * combinations for each dimension
     * @param length Integer containing the number of dimensions for a
     * particular variable.
     */
    public void setUnsafeCombinations(VariableMu variable, int[] unsafeCount, int length) {
        int[] unsafe = new int[length];
        System.arraycopy(unsafeCount, 0, unsafe, 0, length);
        this.unsafeCombinations.put(variable, unsafe);
    }
//    public void clearUnsafe() {
//        this.unsafe = new HashMap<>();
//    }
//
//    public void setUnsafe(VariableMu variable, UnsafeInfo unsafe) {
//        this.unsafe.put(variable, unsafe);
//    }
//
//    public UnsafeInfo getUnsafe(VariableMu variable) {
//        return this.unsafe.get(variable);
//    }

    /**
     * Gets the threshold.
     *
     * @return String containing the threshold.
     */
    public String getThreshold() {
        return Integer.toString(threshold);
    }

    /**
     * Sets the threshold using a String.
     *
     * @param threshold String containing the threshold.
     */
    public void setThreshold(String threshold) {
        this.threshold = Integer.parseInt(threshold);
    }

    /**
     * Sets the threshold using an integer.
     *
     * @param threshold Integer containing the threshold.
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Gets the array of thresholds for each dimension.
     *
     * @return Array of integers containing the thresholds for each dimension.
     */
    public int[] getThresholds() {
        return thresholds;
    }

    /**
     * Sets the array of thresholds for each dimension.
     *
     * @param thresholds Array of integers containing the thresholds for each
     * dimension.
     */
    public void setThresholds(int[] thresholds) {
        this.thresholds = thresholds;
    }

//    /**
//     * Sets the threshold for each dimension at the same threshold.
//     * @param thresholds
//     * @param dimension
//     */
//    public void setThresholds(int thresholds, int dimension) {
//        this.thresholds[dimension - 1] = thresholds;
//    }
    /**
     * Gets all the specified tables.
     *
     * @return ArrayList containing instances of the TableMu class. Each table
     * is specified by an TableMu instance.
     */
    public ArrayList<TableMu> getTables() {
        return tables;
    }

    /**
     * Sets all the specified tables.
     *
     * @param tables ArrayList containing instances of the TableMu class. Each
     * table is specified by an TableMu instance
     */
    public void setTables(ArrayList<TableMu> tables) {
        this.tables = tables;
    }

    /**
     * Adds a new table to the ArrayList containing all tables.
     *
     * @param table TableMu instance containing the table that needs to be
     * added.
     */
    public void addTable(TableMu table) {
        this.tables.add(table);
    }

    /**
     * Removes a table using the index in the ArrayList.
     *
     * @param index Integer containing the index of the table that needs to be
     * removed from the ArrayList.
     */
    public void removeTable(int index) {
        this.tables.remove(index);
    }

    /**
     * Removes a table using the instance of the to be removed table.
     *
     * @param table Tablemu instance of the to be removed table.
     */
    public void removeTable(TableMu table) {
        this.tables.remove(table);
    }

    /**
     * Gets the number of rows. The number of rows equals the number of tables
     * that are specified.
     *
     * @return Integer containing the number of rows.
     */
    public int getNumberOfRows() {
        return this.tables.size();
    }

//     /**
//     * Sets the number of rows.
//     * @param n Integer containing the number of rows.
//     */
//    public void setNumberOfRows(int n) {
//        //Deprecated, do not use
//    }
    /**
     * Gets the maximum numbers of tables for which double tables will be
     * removed if the riskModel is not active.
     *
     * @return Integer containing the maximum number of tables for which double
     * tables will be removed.
     */
    public int getMaximumNumberOfTables() {
        return maximumNumberOfTables;
    }

    /**
     * Gets the maximum number of tables whithout the user being asked if he/she
     * would like to specy this ammount of tables and for which double tables
     * will be removed if the riskModel is active.
     *
     * @return Integer containing the maximum number of tables whithout the user
     * being asked if he/she would like to specy this ammount of tables and for
     * which double tables will be removed if the riskModel is active.
     */
    public int getMaximumSizeBeforeUserConfirmation() {
        return maximumSizeBeforeUserConfirmation;
    }

    /**
     * Returns if the RiskModel is specified for at least one table.
     *
     * @return Boolean whether the RiskModel is specified for at least one table
     * or not.
     */
    public boolean isRiskModel() {
        for (TableMu t : this.tables) {
            if (t.isRiskModel()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an ArrayList containing al the variables for which the riskModel is
     * specified.
     *
     * @return Returns an ArrayList containing al the variables for which the
     * riskModel is specified.
     */
    public ArrayList<VariableMu> getRiskModelVariables() {
        ArrayList<VariableMu> riskVariables = new ArrayList<>();
        for (TableMu table : this.tables) {
            if (table.isRiskModel()) {
                riskVariables.addAll(table.getVariables());
            }
        }
        return riskVariables;
    }

    /**
     * Gets the number of Columns.
     *
     * @return Integer containing the number of Columns
     */
    public int getNumberOfColumns() {
        return this.calculateNumberOfColumns();
    }

    //TODO: naar controller?
    /**
     * Calculates the number of Columns. It sets the numberOfVariables to the
     * size of the largest table and adds two columns (one for the riskModel and
     * one for the threshold).
     *
     * @return Integer containing the number of columns.
     */
    private int calculateNumberOfColumns() {
        int numberOfVariables = 0;
        for (TableMu t : this.tables) {
            if (t.getVariables().size() > numberOfVariables) {
                numberOfVariables = t.getVariables().size();
            }
        }
        int numberOfColumns = numberOfVariables + 2;
        return numberOfColumns;
    }

    /**
     * Gets an ArrayList containing all the variables that are specified in on
     * of the tables. This method loops for each table through all variables in
     * that table and if a variable is not already in the ArrayList, this
     * variable will be added.
     *
     * @return ArrayList containing all the variables that are specified in on
     * of the tables.
     */
    public ArrayList<VariableMu> getVariablesInTables() {
        ArrayList<VariableMu> variablesMu = new ArrayList<>();
        for (TableMu table : this.tables) {
            for (VariableMu variable : table.getVariables()) {
                if (!variablesMu.contains(variable)) {
                    variablesMu.add(variable);
                }
            }
        }
        return variablesMu;
    }

    /**
     * Gets the maximum number of dimensions of all tables. The maximum number
     * of dimensions equals the size of the biggest table.
     *
     * @return Integer containing the maximum number of dimensions of all
     * tables.
     */
    public int getMaxDimsInTables() {
        int max = 0;
        for (TableMu table : this.tables) {
            if (table.getVariables().size() > max) {
                max = table.getVariables().size();
            }
        }
        return max;
    }

}
