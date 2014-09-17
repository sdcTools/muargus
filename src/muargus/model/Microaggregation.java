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
public class Microaggregation {
    private final ArrayList<VariableMu> variables;
    private final ArrayList<MicroaggregationSpec> microaggregations;
    
    public Microaggregation() {
        this.microaggregations = new ArrayList<>();
        this.variables = new ArrayList<>();
    }
            
    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }
    
    public ArrayList<MicroaggregationSpec> getMicroaggregations() {
        return microaggregations;
    }   
    
}