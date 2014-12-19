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
 * Model class of the NumericalRankSwapping screen. Only a single instance of
 * this class will exist.
 *
 * @author Statistics Netherlands
 */
public class NumericalRankSwapping {

    private final ArrayList<VariableMu> variables;
    private final ArrayList<RankSwappingSpec> rankSwappings;

    /**
     * Constructor of the model class NumericalRankSwapping. Makes empty
     * arraylists for the variables and the rankSwappings.
     */
    public NumericalRankSwapping() {
        this.variables = new ArrayList<>();
        this.rankSwappings = new ArrayList<>();
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
     * Gets the ArrayList containing the rank swapping specifications. The
     * RankSwappingSpec contains all relevant information for applying rank
     * swapping on the relevant variable(s). If the ArrayList is empty, use this
     * method to add RankSwappingSpec's.
     *
     * @return ArrayList containing RankSwappingSpec's.
     */
    public ArrayList<RankSwappingSpec> getRankSwappings() {
        return this.rankSwappings;
    }
}
