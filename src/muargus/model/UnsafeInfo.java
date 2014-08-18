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
    private UnsafeCodeInfo[] missingInfos;
    private int[] unsafe; 
    private int frequency;
            
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
    public void calculateUnsafe() {
        int nDims = getNDims();
        this.frequency = 0;
        this.unsafe = new int[nDims];
        for (UnsafeCodeInfo unsafeCode : this.unsafeCodeInfos) {
            for (int i=0; i < nDims; i++) {
                this.unsafe[i] += unsafeCode.getUnsafe(i);
            }
            this.frequency += unsafeCode.getFrequency();
        }
    }
}
 