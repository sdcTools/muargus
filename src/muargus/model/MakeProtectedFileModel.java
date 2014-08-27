/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author ambargus
 */
public class MakeProtectedFileModel {

    private ArrayList<VariableMu> variables;
    private String[][] data;
    private final String[] columnames;
    private boolean riskModel;
    
    // values for the dll
    private boolean withPrior;
    private boolean withEntropy;
    private int hhOption;
    private boolean randomizeOutput;
    private boolean printBHR; 
    private String nameOfSafeFile;

    /**
     *
     */
    public MakeProtectedFileModel() {
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
        return withPrior;
    }

    public void setWithPrior(boolean withPrior) {
        this.withPrior = withPrior;
    }

    public boolean isWithEntropy() {
        return withEntropy;
    }

    public void setWithEntropy(boolean withEntropy) {
        this.withEntropy = withEntropy;
    }

    public int getHhOption() {
        return hhOption;
    }

    public void setHhOption(int hhOption) {
        this.hhOption = hhOption;
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

    public String getNameOfSafeFile() {
        return nameOfSafeFile;
    }

    public void setNameOfSafeFile(File file) {
        try {
            this.nameOfSafeFile = file.getPath();
        } catch (Exception e){
            
        }
    }
    
}
