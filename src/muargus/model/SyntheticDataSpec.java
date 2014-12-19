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

import argus.model.ArgusException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Model class of the Synthetic data specifications.
 *
 * @author Statistics Netherlands
 */
public class SyntheticDataSpec extends ReplacementSpec {

    private final ArrayList<VariableMu> allVariables;
    private final ArrayList<VariableMu> sensitiveVariables;
    private final ArrayList<VariableMu> nonSensitiveVariables;
    private File alphaFile;
    private File rScriptFile;
    private File runRFile;

    /**
     * Constructor of the model class SyntheticDataSpec. Initializes the
     * ArrayLists for all variables, the sensitive variables and the
     * non-sensitive variable.
     */
    public SyntheticDataSpec() {
        this.sensitiveVariables = new ArrayList<>();
        this.nonSensitiveVariables = new ArrayList<>();
        this.allVariables = new ArrayList<>();
    }

    /**
     * Gets an ArrayList containing the non-sensitive variables. If the
     * ArrayList is empty, use this method to add variables.
     *
     * @return ArrayList of VariableMu's containing the non-sensitive variables.
     */
    public ArrayList<VariableMu> getNonSensitiveVariables() {
        return this.nonSensitiveVariables;
    }

    /**
     * Gets an ArrayList containing the sensitive variables. If the ArrayList is
     * empty, use this method to add variables.
     *
     * @return ArrayList of VariableMu's containing the sensitive variables.
     */
    public ArrayList<VariableMu> getSensitiveVariables() {
        return this.sensitiveVariables;
    }

    /**
     * Gets the file containing the alpa matrix.
     *
     * @return File containing the aplhaFile.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while creating the aplhaFile.
     */
    public File getAlphaFile() throws ArgusException {
        if (this.alphaFile == null) {
            this.alphaFile = createFile(".txt");
        }
        return this.alphaFile;
    }

    /**
     * Gets the file containing the r-script.
     *
     * @return File containing the rScriptFile.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while creating the rScriptFile.
     */
    public File getrScriptFile() throws ArgusException {
        if (this.rScriptFile == null) {
            this.rScriptFile = createFile(".R");
        }
        return this.rScriptFile;
    }

    /**
     * Gets the file containing the .bat file used to run the r-script.
     *
     * @return File containing the runRfile.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while creating the runRfile.
     */
    public File getRunRFileFile() throws ArgusException {
        if (this.runRFile == null) {
            this.runRFile = createFile(".bat");
        }
        return this.runRFile;
    }

    /**
     * Replaces slashes in filenames for double slashes.
     *
     * @param filename String containing the filename.
     * @return String containing the filename with double slashes instead of
     * single slashes.
     */
    public String doubleSlashses(String filename) {
        return filename.replace("\\", "\\\\");
    }

    /**
     * Creates a new temp file.
     *
     * @param extension String containing the extension of the temp file.
     * @return File containing the new temp file.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while creating the temp file.
     */
    private File createFile(String extension) throws ArgusException {
        try {
            File file = File.createTempFile("MuArgus", extension);
            file.deleteOnExit();
            return file;
        } catch (IOException ex) {
            throw new ArgusException("Temporary file cannot be created: " + ex.getMessage());
        }
    }

    /**
     * Gets an ArrayList containing the variables used as neither sensitive nor
     * non-sensitive variables.
     *
     * @return ArrayList of VariableMu's containing the variables used as
     * neither sensitive nor non-sensitive variables.
     */
    public ArrayList<VariableMu> getOtherVariables() {
        ArrayList<VariableMu> other = new ArrayList<>();
        for (VariableMu variable : this.allVariables) {
            if (!this.sensitiveVariables.contains(variable) && (!this.nonSensitiveVariables.contains(variable))) {
                other.add(variable);
            }
        }
        return other;
    }

    /**
     * Gets an ArrayList containing all numeric variables. If the ArrayList is
     * empty, use this method to add variables.
     *
     * @return ArrayList of VariableMu's containing the numeric variables.
     */
    public ArrayList<VariableMu> getAllVariables() {
        return this.allVariables;
    }

    /**
     * Clears all ArrayLists.
     */
    public void clear() {
        this.allVariables.clear();
        this.nonSensitiveVariables.clear();
        this.sensitiveVariables.clear();
    }

    /**
     * Gets an ArrayList containing the input variables. The input variables are
     * the sensitive and the non-sensitive variables. sensitive and the
     * non-sensitive variables.
     *
     * @return ArrayList of VariableMu's containing the the *sensitive and the
     * non-sensitive variables.
     */
    @Override
    public ArrayList<VariableMu> getInputVariables() {
        ArrayList<VariableMu> variables = new ArrayList<>(this.sensitiveVariables);
        variables.addAll(this.nonSensitiveVariables);
        return variables;
    }

    /**
     * Gets an ArrayList containing the output variables. The output variables
     * are the sensitive variables.
     *
     * @return ArrayList of VariableMu's containing the the sensitive variables.
     */
    @Override
    public ArrayList<VariableMu> getOutputVariables() {
        return this.sensitiveVariables;
    }

}
