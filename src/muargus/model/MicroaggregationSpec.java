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
public class MicroaggregationSpec extends ReplacementSpec {
    
    private final int minimalNumberOfRecords;
    private final boolean optimal;
    
    public MicroaggregationSpec(int minimalNumberOfRecords, boolean optimal) {
        this.minimalNumberOfRecords = minimalNumberOfRecords;
        this.optimal = optimal;
    }

    public int getMinimalNumberOfRecords() {
        return this.minimalNumberOfRecords;
    }

    public boolean isOptimal() {
        return this.optimal;
    }
    
    
    
}
