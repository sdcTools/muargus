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
public class UnsafeInfo {
    
    private ArrayList<UnsafeCodeInfo> unsafeCodeInfos = new ArrayList<>();
    //private UnsafeCodeInfo[] missingInfos;
    private int[] unsafeCombinations; 
            
    public UnsafeInfo() {
        
    }
    
    public void clearUnsafeCodeInfos() {
        this.unsafeCodeInfos = new ArrayList<>();
    }
    
    public void addUnsafeCodeInfo(UnsafeCodeInfo info) {
        this.unsafeCodeInfos.add(info);
    }
    
    private int getNDims() {
        return 3; //TODO
    }
    
    public void setUnsafeCombinations(int count, int[] unsafe) {
        this.unsafeCombinations = new int[count];
        System.arraycopy(unsafe, 0, this.unsafeCombinations, 0, count);
    }
    
}
 