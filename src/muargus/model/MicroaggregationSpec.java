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

/**
 * Model class containing relevant information for microaggregation.
 *
 * @author Statistics Netherlands
 */
public class MicroaggregationSpec extends ReplacementSpec {

    private final int minimalNumberOfRecords;
    private final boolean optimal;

    /**
     * Constructor of the model class Microaggregation.
     *
     * @param minimalNumberOfRecords Integer containing the minimum number of
     * records per group.
     * @param optimal Boolean indicating whether the optimal method is used.
     */
    public MicroaggregationSpec(int minimalNumberOfRecords, boolean optimal) {
        this.minimalNumberOfRecords = minimalNumberOfRecords;
        this.optimal = optimal;
    }

    /**
     * Gets the minimum number of records per group.
     *
     * @return Integer containing the minimum number of records per group.
     */
    public int getMinimalNumberOfRecords() {
        return this.minimalNumberOfRecords;
    }

    /**
     * Boolean whether the optimal method is used.
     *
     * @return Boolean indicating whether the optimal method is used.
     */
    public boolean isOptimal() {
        return this.optimal;
    }

}
