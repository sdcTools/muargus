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
 * Model class of the replacement specifications.
 *
 * @author Statistics Netherlands
 */
public class ReplacementSpec {

    private final ArrayList<VariableMu> variables;
    private ReplacementFile replacementFile;

    /**
     * Constructor of the model class ReplacementSpec. Makes an empty arraylists
     * for the variables.
     */
    public ReplacementSpec() {
        this.variables = new ArrayList<>();
    }

    /**
     * Gets an ArrayList containing the variables. If the ArrayList is empty,
     * use this method to add variables.
     *
     * @return ArrayList containing the variables.
     */
    public ArrayList<VariableMu> getInputVariables() {
        return variables;
    }

    /**
     * Gets an ArrayList containing the variables. If the ArrayList is empty,
     * use this method to add variables.
     *
     * @return ArrayList containing the variables.
     */
    public ArrayList<VariableMu> getOutputVariables() {
        return variables;
    }

    /**
     * Gets the replacementFile.
     *
     * @return ReplacementFile containing the replacement type and the input &
     * output file.
     */
    public ReplacementFile getReplacementFile() {
        return replacementFile;
    }

    /**
     * Sets the replacementFile.
     *
     * @param replacementFile ReplacementFile containing the replacement type
     * and the input & output file.
     */
    public void setReplacementFile(ReplacementFile replacementFile) {
        this.replacementFile = replacementFile;
    }

}
