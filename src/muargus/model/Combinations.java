/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;
import java.util.HashMap;
//import muargus.MuARGUS;

/**
 *
 * @author ambargus
 */
public class Combinations {

    private int threshold;
    private boolean riskModel;
    private ArrayList<TableMu> tables;
    private VariableMu[] variables;
    private int[] thresholds;
    private HashMap<VariableMu, int[]> unsafeCombinations; 
    //private HashMap<VariableMu, UnsafeInfo> unsafe;
    private GlobalRecode globalRecode;
    private ProtectedFile protectedFile;
    private TableCollection showTableCollection;
    private RiskSpecification riskSpecification;
    private ModifyNumericalVariables modifyNumericalVariables;
    private NumericalMicroaggregation numericalMicroaggregation;
    private NumericalRankSwapping numericalRankSwapping;
    private PramSpecification pramSpecification;

    // tot dit aantal kan die het redelijk goed hebben, maar is die wel +/- 5 seconden aan het rekenen. 
    private final int maximumNumberOfTables = 25000;

    private final int maximumSizeBeforeUserConfirmation = 100;

    /**
     *
     */
    public Combinations() {
        this.threshold = 1;
        this.tables = new ArrayList<>();
    }

    public Combinations(Combinations model) {
        this.threshold = model.threshold;
        this.thresholds = model.getThresholds();
        this.tables = new ArrayList<>();
        for (TableMu table : model.tables) {
            this.tables.add(new TableMu(table));
        }
    }

    public GlobalRecode getGlobalRecode() {
        return this.globalRecode;
    }
    
    public void createGlobalRecode() {
        this.globalRecode = new GlobalRecode();
        this.globalRecode.setVariables(this.getVariablesInTables());
    }
    
    public TableCollection getShowTableCollection() {
        return this.showTableCollection;
    }
    
    public void createShowTableCollection() {
        this.showTableCollection = new TableCollection();
        this.showTableCollection.setVariables(this.getVariablesInTables());
    }
    
    public ProtectedFile getProtectedFile() {
        return this.protectedFile;
    }
    
    public void createProtectedFile() {
        this.protectedFile = new ProtectedFile();
        this.protectedFile.setVariables(this.getVariablesInTables());
        this.protectedFile.setRiskModel(this.isRiskModel());
    }

    public PramSpecification getPramSpecification() {
        return pramSpecification;
    }

    public void createPramSpecification() {
        this.pramSpecification = new PramSpecification();
    }

    public RiskSpecification getRiskSpecification() {
        return riskSpecification;
    }

    public void createRiskSpecification() {
        this.riskSpecification = new RiskSpecification();
    }

    public ModifyNumericalVariables getModifyNumericalVariables() {
        return modifyNumericalVariables;
    }

    public void createModifyNumericalVariables() {
        this.modifyNumericalVariables = new ModifyNumericalVariables();
    }

    public NumericalMicroaggregation getNumericalMicroaggregation() {
        return numericalMicroaggregation;
    }

    public void createNumericalMicroaggregation() {
        this.numericalMicroaggregation = new NumericalMicroaggregation();
    }

    public NumericalRankSwapping getNumericalRankSwapping() {
        return numericalRankSwapping;
    }

    public void createNumericalRankSwapping() {
        this.numericalRankSwapping = new NumericalRankSwapping();
    }
    
    public void clearUnsafeCombinations() {
        this.unsafeCombinations = new HashMap<>();
    }
            
    public int[] getUnsafeCombinations(VariableMu variable) {
        return this.unsafeCombinations.get(variable);
    }
          
    public void setUnsafeCombinations(VariableMu variable, int[] unsafeCount, int length) {
        int[] unsafe = new int[length];
        System.arraycopy( unsafeCount, 0, unsafe, 0, length);
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

    public int[] getThresholds() {
        return thresholds;
    }

    public void setThresholds(int[] thresholds) {
        this.thresholds = thresholds;
    }

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

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public ArrayList<TableMu> getTables() {
        return tables;
    }

    public void setTables(ArrayList<TableMu> tables) {
        this.tables = tables;
    }

    public void addTable(TableMu table) {
        this.tables.add(table);
    }

    public void removeTable(int i) {
        this.tables.remove(i);
    }

    public void removeTable(TableMu t) {
        this.tables.remove(t);
    }

    public int getNumberOfRows() {
        return this.tables.size();
    }

    public int getMaximumNumberOfTables() {
        return maximumNumberOfTables;
    }

    public int getMaximumSizeBeforeUserConfirmation() {
        return maximumSizeBeforeUserConfirmation;
    }

    public void setNumberOfRows(int n) {
        //Deprecated, do not use
    }

    public boolean isRiskModel() {
        for (TableMu t : this.tables) {
            if (t.isRiskModel()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<VariableMu> getRiskModelVariables() {
        ArrayList<VariableMu> riskVariables = new ArrayList<>();
        for (TableMu table : this.tables) {
            if (table.isRiskModel()) {
                riskVariables.addAll(table.getVariables());
            }
        }
        return riskVariables;
    }

    public int getNumberOfColumns() {
        return this.calculateNumberOfColumns();
    }

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
