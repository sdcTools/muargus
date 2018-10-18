/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

/**
 *
 * @author pwof
 */
public class TargetSwappingSpec extends ReplacementSpec {
    
    private final double swaprate;
    private final int kThreshold;
    private final int seed;
    private final int hhID;
    
    
    /**
     * @param percentage Double containing the swaprate.
     * @param kThreshold Integer containing k-anonimity threshold to be used in 
     *                   Targeted Record Swapping
     * @param seed Integer to be used as seed in random number creation
     * @param hhiD Integer indicating column number of householdID variable
     */
    public TargetSwappingSpec(int hhID, double swaprate, int kThreshold, int seed) {
        this.hhID = hhID;
        this.swaprate = swaprate;
        this.kThreshold = kThreshold;
        this.seed = seed;
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
}
