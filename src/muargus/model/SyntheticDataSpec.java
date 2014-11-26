/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import argus.model.ArgusException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public class SyntheticDataSpec extends ReplacementSpec {
    
    private final ArrayList<VariableMu> allVariables;
    private final ArrayList<VariableMu> sensitiveVariables;
    private final ArrayList<VariableMu> nonSensitiveVariables;
    private File alphaFile;
    private File rScriptFile; 
    private File runRFile; 
    //public final static String pathRexe = "C:\\Program Files\\%USERPROFILE%\\AppData\\Local\\Temp\\R";


    /**
     * Constructor of the model class SyntheticDataSpec. 
     * Initializes its variables.
     */
    public SyntheticDataSpec() {
        this.sensitiveVariables = new ArrayList<>();
        this.nonSensitiveVariables = new ArrayList<>();
        this.allVariables = new ArrayList<>();
    }

    /**
     * Gets an ArrayList containing the variables. If the ArrayList is empty,
     * use this method to add variables.
     *
     * @return ArrayList containing the variables.
     */
    public ArrayList<VariableMu> getNonSensitiveVariables() {
        return this.nonSensitiveVariables;
    }
    
    public ArrayList<VariableMu> getSensitiveVariables() {
        return this.sensitiveVariables;
    }

    public File getAlphaFile() throws ArgusException {
        if (alphaFile == null) {
            alphaFile = createFile(".txt");
        }
        return alphaFile;
    }

    public File getrScriptFile() throws ArgusException {
        if (rScriptFile == null) {
            rScriptFile = createFile(".R");
        }
        return rScriptFile;
    }
    
    public File getRunRFileFile() throws ArgusException {
        if (runRFile == null) {
            runRFile = createFile(".bat");
        }
        return runRFile;
    }
    
    public String doubleSlashses(String filename){
        return filename.replace("\\", "\\\\");
    }
    
    private File createFile(String extension) throws ArgusException {
        try {
            File file = File.createTempFile("MuArgus", extension);
            file.deleteOnExit();
            return file;
        } catch (IOException ex) {
            throw new ArgusException("Temporary file cannot be created: " + ex.getMessage());
        }
    }

    public ArrayList<VariableMu> getOtherVariables() {
        ArrayList<VariableMu> other = new ArrayList<>();
        for (VariableMu variable : allVariables) {
            if (!this.sensitiveVariables.contains(variable) && (!this.nonSensitiveVariables.contains(variable))) {
                other.add(variable);
            }
        }
        return other;
    }

    public ArrayList<VariableMu> getAllVariables() {
        return allVariables;
    }

    @Override
    public ArrayList<VariableMu> getInputVariables() {
        ArrayList<VariableMu> variables = new ArrayList<>(this.sensitiveVariables);
        variables.addAll(this.nonSensitiveVariables);
        return variables;
    }

    @Override
    public ArrayList<VariableMu> getOutputVariables() {
        return this.sensitiveVariables;
    }
    
    

        
}
