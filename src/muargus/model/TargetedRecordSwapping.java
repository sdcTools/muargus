/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author pwof
 */
public class TargetedRecordSwapping {
    
    private final ArrayList<VariableMu> variables;
    private final ArrayList<TargetSwappingSpec> targetSwappings;

    /**
     * Constructor of the model class TargetedRecordSwapping. Makes empty
     * array lists for the variables and the targetSwappings.
     */
    public TargetedRecordSwapping() {
        this.variables = new ArrayList<>();
        this.targetSwappings = new ArrayList<>();
    } 
    
    /**
     * Gets the ArrayList containing all the variables. If the
     * ArrayList is empty, use this method to add VariableMu's.
     *
     * @return ArrayList containing all the numerical variables.
     */
    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    /**
     * Gets the ArrayList containing the target swapping specifications. The
     * TargetSwappingSpec contains all relevant information for applying targeted record
     * swapping on the relevant variable(s). If the ArrayList is empty, use this
     * method to add TargeSwappingSpec's.
     *
     * @return ArrayList containing TargetSwappingSpec's.
     */
    public ArrayList<TargetSwappingSpec> getTargetSwappings() {
        return this.targetSwappings;
    }
}
