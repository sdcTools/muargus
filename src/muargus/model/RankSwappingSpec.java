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
 * Model class containing relevant information for rank swapping.
 *
 * @author Statistics Netherlands
 */
public class RankSwappingSpec extends ReplacementSpec {

    private final int percentage;

    /**
     * Constructor of the model class Microaggregation. Makes an empty arraylist
     * for the microaggregations.
     *
     * @param percentage Integer containing the rank swapping percentage.
     */
    public RankSwappingSpec(int percentage) {
        this.percentage = percentage;
    }

    /**
     * Gets the rank swapping percentage.
     *
     * @return Integer containing the rank swapping percentage.
     */
    public int getPercentage() {
        return percentage;
    }

}
