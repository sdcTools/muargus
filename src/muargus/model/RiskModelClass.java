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
 * Model class containing information on a single pillar of the risk model
 * histogram. An instance for each pillar will exist.
 *
 * @author Statistics Netherlands
 */
public class RiskModelClass {

    private final double leftValue;
    private final double rightValue;
    private final int frequency;
    private final int hhFrequency;

    /**
     * Constructor of the model class RiskModelClass.
     *
     * @param leftValue Double containing the left hand value of the pillar.
     * @param rightValue Double containing the right hand value of the pillar.
     * @param frequency Integer containing the individual risk frequency.
     * @param hhFrequency Integer containing the household frequency.
     */
    public RiskModelClass(double leftValue, double rightValue, int frequency, int hhFrequency) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.frequency = frequency;
        this.hhFrequency = hhFrequency;
    }

    /**
     * Gets the left hand value of the pillar.
     *
     * @return Double containing the left hand value of the pillar.
     */
    public double getLeftValue() {
        return this.leftValue;
    }

    /**
     * Gets the right hand value of the pillar.
     *
     * @return Double containing the right hand value of the pillar.
     */
    public double getRightValue() {
        return this.rightValue;
    }

    /**
     * Gets the individual risk frequency.
     *
     * @return Integer containing the individual risk frequency.
     */
    public int getFrequency() {
        return this.frequency;
    }

    /**
     * Gets the household frequency.
     *
     * @return Integer containing the household frequency.
     */
    public int getHhFrequency() {
        return this.hhFrequency;
    }

}
