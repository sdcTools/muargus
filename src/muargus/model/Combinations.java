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
     * Creates a new GlobalRecode class and assigns the variables used in the
     * tables from the SelectCombinations on.
     */
    public void createGlobalRecode() {
        this.globalRecode = new GlobalRecode();
        this.globalRecode.setVariables(this.getVariablesInTables());
    }

    /**
     * Gets the model class of the ShowTableCollection screen.
     * @return Returns the ShowTableCollection model class.
     */
    public TableCollection getTableCollection() {
        return this.tableCollection;
    }

    /**
     * Creates a new TableCollecton class and assigns the variables used in the
     * tables from the SelectCombinations.
     */
    public void createTableCollection() {
        this.tableCollection = new TableCollection();
        this.tableCollection.setVariables(this.getVariablesInTables());
    }

    /**
     * 
     * @return 
     */
    public ProtectedFile getProtectedFile() {
        return this.protectedFile;
    }

    /**
     * Creates a new ProtectedFile class, assigns the variables used in the
     * tables from the SelectCombinations and sets the riskmodel.
     */
    public void createProtectedFile() {
        this.protectedFile = new ProtectedFile();
        this.protectedFile.setVariables(this.getVariablesInTables());
        this.protectedFile.setRiskModel(this.isRiskModel());
    }

    /**
     * 
     * @return 
     */
    public PramSpecification getPramSpecification() {
        return pramSpecification;
    }

    /**
     * 
     */
    public void createPramSpecification() {
        this.pramSpecification = new PramSpecification();
    }

    /**
     * 
     * @return 
     */
    public RiskSpecification getRiskSpecification() {
        return riskSpecification;
    }

    /**
     * 
     */
    public void createRiskSpecification() {
        this.riskSpecification = new RiskSpecification();
    }

    /**
     * 
     * @return 
     */
    public ModifyNumericalVariables getModifyNumericalVariables() {
        return modifyNumericalVariables;
    }

    /**
     * 
     */
    public void createModifyNumericalVariables() {
        this.modifyNumericalVariables = new ModifyNumericalVariables();
    }

    /**
     * 
     * @return 
     */
    public NumericalMicroaggregation getNumericalMicroaggregation() {
        return numericalMicroaggregation;
    }

    /**
     * 
     */
    public void createNumericalMicroaggregation() {
        this.numericalMicroaggregation = new NumericalMicroaggregation();
    }

    /**
     * 
     * @return 
     */
    public NumericalRankSwapping getNumericalRankSwapping() {
        return numericalRankSwapping;
    }

    /**
     * 
     */
    public void createNumericalRankSwapping() {
        //this.numericalRankSwapping = new NumericalRankSwapping();
    }

    /**
     * 
     */
    public void clearUnsafeCombinations() {
        this.unsafeCombinations = new HashMap<>();
    }

    /**
     * 
     * @param variable
     * @return 
     */
    public int[] getUnsafeCombinations(VariableMu variable) {
        return this.unsafeCombinations.get(variable);
    }

    /**
     * 
     * @param variable
     * @param unsafeCount
     * @param length 
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
     *
     * @return
     */
    public String getThreshold() {
        return Integer.toString(threshold);
    }

    /**
     * 
     * @return 
     */
    public int[] getThresholds() {
        return thresholds;
    }

    /**
     * 
     * @param thresholds 
     */
    public void setThresholds(int[] thresholds) {
        this.thresholds = thresholds;
    }

    /**
     * 
     * @param thresholds
     * @param dimension 
     */
    public void setThresholds(int thresholds, int dimension) {
        this.thresholds[dimension - 1] = thresholds;
    }

    /**
     *
     * @param threshold
     */
    public void setThreshold(String threshold) {
        this.threshold = Integer.parseInt(threshold);
    }

    /**
     * 
     * @param threshold 
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<TableMu> getTables() {
        return tables;
    }

    /**
     * 
     * @param tables 
     */
    public void setTables(ArrayList<TableMu> tables) {
        this.tables = tables;
    }

    /**
     * 
     * @param table 
     */
    public void addTable(TableMu table) {
        this.tables.add(table);
    }

    /**
     * 
     * @param i 
     */
    public void removeTable(int i) {
        this.tables.remove(i);
    }

    /**
     * 
     * @param t 
     */
    public void removeTable(TableMu t) {
        this.tables.remove(t);
    }

    /**
     * 
     * @return 
     */
    public int getNumberOfRows() {
        return this.tables.size();
    }

    /**
     * 
     * @return 
     */
    public int getMaximumNumberOfTables() {
        return maximumNumberOfTables;
    }

    /**
     * 
     * @return 
     */
    public int getMaximumSizeBeforeUserConfirmation() {
        return maximumSizeBeforeUserConfirmation;
    }

    /**
     * 
     * @param n 
     */
    public void setNumberOfRows(int n) {
        //Deprecated, do not use
    }

    /**
     * 
     * @return 
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
     * 
     * @return 
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
     * 
     * @return 
     */
    public int getNumberOfColumns() {
        return this.calculateNumberOfColumns();
    }

    /**
     * 
     * @return 
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
     * 
     * @return 
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
     * 
     * @return 
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
