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
    private int frequency;
    private int[] unsafe;
    
    public UnsafeCodeInfo() {
        
    }
    
    public int getUnsafe(int dimIndex) {
        return this.unsafe[dimIndex];
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
}
