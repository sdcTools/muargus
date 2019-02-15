/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author pwof
 */
public class TargetSwappingSpec extends ReplacementSpec {
    
    private final double swaprate;
    private final int kThreshold;
    private final int seed;
    private final int nSim;
    private final int nHier;
    private final int nRisk;
    private int hhID;
    private int[] similar;
    private int[] hierarchy;
    private int[] risk;
    
    
    /**
     * @param percentage Double containing the swaprate.
     * @param kThreshold Integer containing k-anonimity threshold to be used in 
     *                   Targeted Record Swapping
     * @param seed Integer to be used as seed in random number creation
     */
    public TargetSwappingSpec(int nSim, int nHier, int nRisk, double swaprate, int kThreshold, int seed) {
        this.hhID = 0;
        this.swaprate = swaprate;
        this.kThreshold = kThreshold;
        this.seed = seed;
        this.nSim = nSim;
        this.nHier = nHier;
        this.nRisk = nRisk;
        this.similar = new int[nSim];
        this.hierarchy = new int[nHier];
        this.risk = new int[nRisk];
    }

    public int getNSim(){
        return nSim;
    }

    public int getNHier(){
        return nHier;
    }

    public int getNRisk(){
        return nRisk;
    }
    
    /**
     * Gets the rank swapping percentage.
     *
     * @return Integer containing the rank swapping percentage.
     */
    public double getSwaprate() {
        return swaprate;
    }
    
    /**
     * Gets the k-anonymity threshold.
     *
     * @return Integer containing the k-anonymity threshold.
     */
    public int getkThreshold() {
        return kThreshold;
    }
    
    /**
     * Gets the seed.
     *
     * @return Integer containing the seed.
     */
    public int getSeed() {
        return seed;
    }
    
    /**
     * Gets the column index of the hhID.
     *
     * @return Integer containing the seed.
     */
    public int getHHID() {
        return hhID;
    }

    /**
     * Gets the column index of the hhID.
     *
     * @return Integer containing the seed.
     */
    public int[] getSimilarIndexes() {
        return similar;
    }

    /**
     * Gets the column index of the hhID.
     *
     * @return Integer containing the seed.
     */
    public int[] getHierarchyIndexes() {
        return hierarchy;
    }
    
    /**
     * Gets the column index of the hhID.
     *
     * @return Integer containing the seed.
     */
    public int[] getRiskIndexes() {
        return risk;
    }
    
    public void calculateSimilarIndexes(ArrayList<VariableMu> variables){
        int i=0;
        for (VariableMu variable : variables) similar[i++] = this.getOutputVariables().indexOf(variable);
    }

    public void calculateHierarchyIndexes(ArrayList<VariableMu> variables){
        int i=0;
        for (VariableMu variable : variables) hierarchy[i++] = this.getOutputVariables().indexOf(variable);
    }

    public void calculateRiskIndexes(ArrayList<VariableMu> variables){
        int i=0;
        for (VariableMu variable : variables) risk[i++] = this.getOutputVariables().indexOf(variable);
    }
    
    public void calculateHHIdIndex(VariableMu variable){
        this.hhID = this.getOutputVariables().indexOf(variable);
    }
}
