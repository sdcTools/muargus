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
    private String label = "";
    private boolean isMissing;
    private int frequency;
    private int[] unsafeCombinations;
    
    public UnsafeCodeInfo(String code, boolean isMissing) {
        this.code = code;
        this.isMissing = isMissing;
    }
    
    public void setLabel(String label) {
        this.label = label;
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
 
    public Object[] toObjectArray(int maxDims) {
        Object[] objArr = new Object[maxDims + 3];
        objArr[0] = this.code;
        objArr[1] = this.label;
        objArr[2] = this.frequency; 
        for (int dimNr=1; dimNr <= maxDims; dimNr++) {
            objArr[dimNr+2] = this.unsafeCombinations.length < dimNr ?
                    "-" : Integer.toString(this.unsafeCombinations[dimNr-1]);
        }
        return objArr;
    }

}
