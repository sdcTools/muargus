/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public class PramVariableSpec {
    private ArrayList<CodeInfo> codeInfo;
    private boolean useBandwidth;
    private int bandwidth;
    private boolean applied;
    private final VariableMu variable;
    private String[][] codesData;

    public PramVariableSpec(VariableMu variable) {
        this.variable = variable;
    }

    public ArrayList<CodeInfo> getCodeInfo() {
        return this.codeInfo;
    }

    public void setCodeInfo(ArrayList<CodeInfo> codeInfo) {
        this.codeInfo = codeInfo;
    }

    public boolean useBandwidth() {
        return this.useBandwidth;
    }

    public void setUseBandwidth(boolean useBandwidth) {
        this.useBandwidth = useBandwidth;
    }

    public int getBandwidth() {
        return this.bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public boolean isApplied() {
        return this.applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public VariableMu getVariable() {
        return this.variable;
    }

    public String[][] getCodesData() {
        return codesData;
    }

    public void setCodesData(String[][] codesData) {
        this.codesData = codesData;
    }
    
}
