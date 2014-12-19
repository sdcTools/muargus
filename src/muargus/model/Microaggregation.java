/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
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
     * Gets the ArrayList containing all the numerical variables. If the
     * ArrayList is empty, use this method to add VariableMu's.
     *
     * @return ArrayList containing all the numerical variables.
     */
    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    /**
     * Gets the ArrayList containing the variable specifications for the
     * microaggregation. The MicroaggregationSpec contains all relevant
     * information for applying microaggreation on the relevant variable(s). If
     * the ArrayList is empty, use this method to add MicroaggregationSpec's.
     *
     * @return ArrayList containing MicroaggregationSpec's.
     */
    public ArrayList<MicroaggregationSpec> getMicroaggregations() {
        return microaggregations;
    }

}
