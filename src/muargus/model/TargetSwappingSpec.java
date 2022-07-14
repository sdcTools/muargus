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
    private final int nProfiles;
    private final int[] nSim;
    private final int nHier;
    private final int nRisk;
    private final int nCarry;
    private int hhID;
    private final int[] similar;
    private final int[] hierarchy;
    private final int[] risk;
    private final int[] carry;
    private int count_HID;
    private int count_records;
    private int count_nodonor;
    private boolean IsCalculated;
    
    /**
     * @param nProfiles  Number of similarity profiles (>= 1)
     * @param nSim       Array with number of similarity variables per profile
     * @param nHier      Number of hierarchy variables
     * @param nRisk      Number of risk variables
     * @param nCarry     Number of carr along variables
     * @param swaprate   Double containing the swaprate.
     * @param kThreshold Integer containing k-anonimity threshold to be used in 
     *                   Targeted Record Swapping
     * @param seed Integer to be used as seed in random number creation
     */
    public TargetSwappingSpec(int nProfiles, int[] nSim, int nHier, int nRisk, int nCarry, double swaprate, int kThreshold, int seed) {
        this.hhID = 0;
        this.swaprate = swaprate;
        this.kThreshold = kThreshold;
        this.seed = seed;
        this.nProfiles = nProfiles;
        this.nSim = nSim;
        this.nHier = nHier;
        this.nRisk = nRisk;
        this.nCarry = nCarry;
        int nSimTot=0;
        for (int i=0; i<this.nProfiles; i++) nSimTot += nSim[i];
        this.similar = new int[nSimTot];
        this.hierarchy = new int[this.nHier];
        this.risk = new int[this.nRisk];
        this.carry = new int[this.nCarry];
        this.count_HID = 0;
        this.count_records = 0;
        this.count_nodonor = 0;
        this.IsCalculated = false;
    }

    public boolean getIsCalculated(){
        return IsCalculated;
    }
    
    public void setIsCalculated(boolean b){
        IsCalculated = b;
    }
    
    public int getCountSwappedHID(){
        return count_HID;
    }

    public void setCountSwappedHID(int count){
        count_HID = count;
    }

    public int getCountSwappedRecords(){
        return count_records;
    }

    public void setCountSwappedRecords(int count){
        count_records = count;
    }
    
    public int getCountNoDonor(){
        return count_nodonor;
    }

    public void setCountNoDonor(int count){
        count_nodonor = count;
    }

    public int[] getNSim(){
        return nSim;
    }

    public int getNHier(){
        return nHier;
    }

    public int getNProfiles(){
        return nProfiles;
    }
    public int getNRisk(){
        return nRisk;
    }

    public int getNCarry(){
        return nCarry;
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
     * Gets the Seed
     * @return Integer containing the seed
     */
    public int getSeed() {
        return seed;
    }
    
    /** 
     * Gets the householdID
     * @return Integer contraining the index of the housholdID variable
     */
    public int getHHID() {
        return hhID;
    }

    /**
     * Gets the indexes of the similar variables
     * @return array of integers of the indices of the similar variables
     */
    public int[] getSimilarIndexes() {
        return similar;
    }

    /**
     * Gets the indexes of the hierarchy variables
     * @return array of integers of the indices of the hierarchy variables
     */
    public int[] getHierarchyIndexes() {
        return hierarchy;
    }

    /**
     * Gets the indexes of the risk variables
     * @return array of integers of the indices of the risk variables
     */
    public int[] getRiskIndexes() {
        return risk;
    }

    /**
     * Gets the indexes of the carry along variables
     * @return array of integers of the indices of the carry along variables
     */
    public int[] getCarryIndexes() {
        return carry;
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

    public void calculateCarryIndexes(ArrayList<VariableMu> variables){
        int i=0;
        for (VariableMu variable : variables) carry[i++] = this.getOutputVariables().indexOf(variable);
    }    

    public void calculateHHIdIndex(VariableMu variable){
        this.hhID = this.getOutputVariables().indexOf(variable);
    }
}
