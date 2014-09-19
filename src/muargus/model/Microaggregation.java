/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.util.ArrayList;

/**
 * Model class of the Microaggregation screen. Only a single instance of this
 * class will exist.
 *
 * @author Statistics Netherlands
 */
public class Microaggregation {

    private final ArrayList<VariableMu> variables;
    private final ArrayList<MicroaggregationSpec> microaggregations;

    /**
     * Constructor of the model class Microaggregation. Makes empty arraylists
     * for the variables and the microaggregations.
     */
    public Microaggregation() {
        this.microaggregations = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    /**
     * Gets the ArrayList containing all the numerical variables.
     *
     * @return ArrayList containing all the numerical variables.
     */
    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    /**
     * Gets the ArrayList containing the variable specifications for the
     * microaggregation. The MicroaggregationSpec contains all relevant
     * information for applying microaggreation on the relevant variable. If the
     * ArrayList is empty, use this method to add MicroaggregationSpec's.
     *
     * @return ArrayList containing MicroaggregationSpec's.
     */
    public ArrayList<MicroaggregationSpec> getMicroaggregations() {
        return microaggregations;
    }

}
