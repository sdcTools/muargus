/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import argus.model.DataFilePair;
import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ambargus
 */
public class ProtectedFile {
    
    private int suppressionType = 2;
    public final int NO_SUPPRESSION = 0;
    public final int USE_WEIGHT = 1;
    public final int USE_ENTROPY = 2;
    
    private int householdType = 0;
    public final int NOT_HOUSEHOLD_DATA = 0;
    public final int KEEP_IN_SAFE_FILE = 1;
    public final int CHANGE_INTO_SEQUENCE_NUMBER = 2;
    public final int REMOVE_FROM_SAFE_FILE = 3;

    private ArrayList<VariableMu> variables;
    private String[][] data;
    private final String[] columnames;
    private boolean riskModel;
    
    // values for the dll
    private boolean withPrior;
    private boolean withEntropy;
    private boolean randomizeOutput;
    private boolean printBHR;
    private MetadataMu safeMeta;
    
    private int selectedRow = 0;

    /**
     *
     */
    public ProtectedFile() {
        this.variables = new ArrayList<>();
        this.columnames = new String[]{"Variables", "Priority"};
    }

    public void setVariables(ArrayList<VariableMu> variables) {
        this.variables = variables;
    }

    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    public String[] getColumnames() {
        return columnames;
    }

    public String[][] getData() {
        if (this.data == null) {
            this.data = new String[this.variables.size()][2];
            int index = 0;
            for (VariableMu v : variables) {
                this.data[index][0] = v.getName();
                this.data[index][1] = Integer.toString(v.getSuppressweight());
                index++;
            }
        }
        return this.data;
    }

    public void setPriority(int selectedRow, int priority) {
        this.data[selectedRow][1] = Integer.toString(priority);
        this.variables.get(selectedRow).setSuppressweight(priority);
    }

    public boolean isWithPrior() {
        return this.suppressionType == this.USE_WEIGHT;
    }

    public void setWithPrior(boolean withPrior) {
        this.withPrior = withPrior;
    }

    public boolean isWithEntropy() {
        return this.suppressionType == this.USE_ENTROPY;
    }

    public void setWithEntropy(boolean withEntropy) {
        this.withEntropy = withEntropy;
    }

    public boolean isRandomizeOutput() {
        return randomizeOutput;
    }

    public void setRandomizeOutput(boolean randomizeOutput) {
        this.randomizeOutput = randomizeOutput;
    }

    public boolean isPrintBHR() {
        return printBHR;
    }

    public void setPrintBHR(boolean printBHR) {
        this.printBHR = printBHR;
    }

    public boolean isRiskModel() {
        return riskModel;
    }

    public void setRiskModel(boolean riskModel) {
        this.riskModel = riskModel;
    }

    public void initSafeMeta(File file, MetadataMu meta) {
        this.safeMeta = new MetadataMu(meta);
        String path = getNameOfSafeFile(file);
        DataFilePair pair = new DataFilePair(path, FilenameUtils.removeExtension(path) + ".rds");
        this.safeMeta.setFileNames(pair);
    }
    
    public MetadataMu getSafeMeta() {
        return this.safeMeta;
    }
    
//    public String getNameOfSafeFile() {
//        return nameOfSafeFile;
//    }
//
//    public String getNameOfSafeMetaFile() {
//        return FilenameUtils.removeExtension(nameOfSafeFile) + ".rds";
//    }

    private String getNameOfSafeFile(File file) {
        if(file.getName().contains(".")){
            return file.getPath();
        } else {
            return file.getPath() + ".saf";
        }
    }

    public int getSuppressionType() {
        return suppressionType;
    }

    public void setSuppressionType(int suppressionType) {
        this.suppressionType = suppressionType;
    }

    public int getHouseholdType() {
        return householdType;
    }

    public void setHouseholdType(int householdType) {
        this.householdType = householdType;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public boolean isHouseholdData() {
        return this.householdType != this.NOT_HOUSEHOLD_DATA;
    }

    public void setHouseholdData(boolean householdData) {
        if(!householdData){
            this.householdType = this.NOT_HOUSEHOLD_DATA;
        } else {
            //TODO: zorgen dat onthouden wordt wat de optie is
            this.householdType = this.KEEP_IN_SAFE_FILE;
        }
    }
    
}
