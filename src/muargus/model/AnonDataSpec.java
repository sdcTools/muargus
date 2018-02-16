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
 *
 * @author pwof
 */
public class AnonDataSpec extends ReplacementSpec{
    private final ArrayList<VariableMu> KAnonVars;
    private final Combinations KAnonCombinations;
    private final ArrayList<String> RStrings;
    private final ArrayList<String> missings;
    private final ArrayList<String> priorities;
    private final ArrayList<Integer> thresholds;
    private File dataFile;
    private File rScriptFile;
    private File runRFile;
    
    /**
     * Constructor of the model class SyntheticDataSpec. Initializes the
     * ArrayLists for all variables, the sensitive variables and the
     * non-sensitive variable.
    */
    public AnonDataSpec() {
        this.KAnonVars = new ArrayList<>();
        this.KAnonCombinations = new Combinations();
        this.thresholds = new ArrayList<>();
        this.RStrings = new ArrayList<>();
        this.missings = new ArrayList<>();
        this.priorities = new ArrayList<>();
    }
    
    /**
     * Clears all ArrayLists.
    */
    public void clear() {
        this.KAnonVars.clear();
        //this.KAnonCombinations.clear();
        this.RStrings.clear();
    }
    
    /**
     * Gets an ArrayList containing all strings concerning keyVars needed for R-script. 
     * If the ArrayList is empty, use this method to add strings.
     *
     * @return ArrayList of Strings containing the strings for R-script.
     */
    public ArrayList<String> getKAnonRStrings() {
        return this.RStrings;
    }
    
    /**
     * Gets an ArrayList containing all strings concerning keyVars needed for R-script. 
     * If the ArrayList is empty, use this method to add strings.
     *
     * @return ArrayList of Strings containing the strings for R-script.
     */
    public ArrayList<String> getKAnonMissings() {
        return this.missings;
    }
    
    
    public ArrayList<String> getKAnonPriority(){
        return this.priorities;
    }

    /**
     * Gets an ArrayList containing all KAnon variables. If the ArrayList is
     * empty, use this method to add variables.
     *
     * @return ArrayList of VariableMu's containing the variables.
     */
    public ArrayList<VariableMu> getKAnonVariables() {
        return this.KAnonVars;
    }

    /**
     * Gets an ArrayList containing all KAnon combinations. If the ArrayList is
     * empty, use this method to add variables.
     *
     * @return ArrayList of TableMu's containing the combinations to apply k-anonymisation to.
     */
    public Combinations getKAnonCombinations() {
        return this.KAnonCombinations;
    }
    
    /**
     * Gets an integer array containing thresholds for the anonymisation. If the array is
     * empty, use this method to add thresholds.
     *
     * @return integer array of thresholds
     */
    public ArrayList<Integer> getKAnonThresholds() {
        return this.thresholds;
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
     * Gets the file containing part the microdata needed for the (k+1)-anonymisation
     *
     * @return File containing the rScriptFile.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while creating the rScriptFile.
     */
    public File getdataFile() throws ArgusException {
        if (this.dataFile == null) {
            this.dataFile = createFile(".asc");
        }
        return this.dataFile;
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

}
