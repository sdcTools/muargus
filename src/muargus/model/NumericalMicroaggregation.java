/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author ambargus
 */
public class NumericalMicroaggregation {
    
    private final ArrayList<MicroaggregationSpec> microaggregations;
    
    public NumericalMicroaggregation() {
        this.microaggregations = new ArrayList<>();
    }
            
    
     public ArrayList<MicroaggregationSpec> getMicroaggregations() {
         return microaggregations;
     }   
    
}