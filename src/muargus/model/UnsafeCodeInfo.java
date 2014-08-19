/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

/**
 *
 * @author pibd05
 */
public class UnsafeCodeInfo {

    private String code;
    private boolean isMissing;
    private int frequency;
    private int[] unsafeCombinations;
    
    public UnsafeCodeInfo(String code, boolean isMissing) {
        this.code = code;
        this.isMissing = isMissing;
    }
    
    public int getUnsafe(int dimIndex) {
        return this.unsafeCombinations[dimIndex];
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    
    public void setUnsafeCombinations(int count, int[] unsafe) {
        this.unsafeCombinations = new int[count];
        System.arraycopy(unsafe, 0, this.unsafeCombinations, 0, count);
    }
    
}
