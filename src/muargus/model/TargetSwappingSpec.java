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
    
    /**
     * @param percentage Double containing the swaprate.
     * @param kThreshold Integer containing k-anonimity threshold to be used in 
     *                   Targeted Record Swapping
     */
    public TargetSwappingSpec(double swaprate, int kThreshold) {
        this.swaprate = swaprate;
        this.kThreshold = kThreshold;
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
}
