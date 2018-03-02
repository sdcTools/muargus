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
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import muargus.io.RWriter;
import muargus.model.MetadataMu;
import muargus.model.ReplacementFile;
import muargus.model.ReplacementSpec;
import muargus.model.AnonDataSpec;
import muargus.model.Combinations;
import muargus.model.PramVariableSpec;
import muargus.model.ProtectedFile;
import muargus.model.TableMu;
import muargus.model.VariableMu;

/**
 *
 * @author pwof
 */
public class AnonDataController extends ControllerBase<AnonDataSpec>{
    private final MetadataMu metadata;
    
    /**
     * Constructor for the AnonDataController.
     *
     * @param metadata the orginal metadata.
     */
    public AnonDataController(MetadataMu metadata) {
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getAnonData());
    }

    public AnonDataSpec setAnonData() {
        try {
            AnonDataSpec anonData = getModel();
            clean(anonData);
            
            fillKAnonVariables();
            fillKAnonCombinations();
            removeRedundantKAnonVariables();
            fillKAnonThresholds();
            fillKAnonRStrings();
            fillKAnonMissings();
            fillKAnonPriority();
            
            anonData.getdataFile();
            
            anonData.setReplacementFile(new ReplacementFile("(k+1)-anonymity"));
            this.metadata.getReplacementSpecs().add(anonData);
            
            RWriter.writeKAnon(anonData);
            RWriter.writeBatKAnon(anonData);
            
            SystemUtils.writeLogbook("Anon-files for (k+1)-anonymisation have been generated.");
            return anonData;
        } catch (ArgusException ex) {
            return null;
        }

    }
    
    public boolean runAnonData(){
        try {
            runR(); 
            return true;
        } catch (ArgusException ex){
            return false;
        }
    }
    
    public void setNumberSuppAnonData() throws ArgusException{
        String fn = getModel().doubleSlashses(getModel().getlogFile().getAbsolutePath());
        File SuppInfo = new File(fn);
        int[] Supps = new int[getKAnonVariables().size()];
        
        try (Scanner reader = new Scanner(new FileReader(SuppInfo))){
            int i=0;
            while (reader.hasNextInt()){
                Supps[i++] = reader.nextInt();
            }
        }catch(IOException ex){
            throw new ArgusException(String.format("Error reading number of suppressions from (k+1)-anonymity in %s.",fn));
        }
        
        for (VariableMu var : getKAnonVariables()){
            int i=0;
            var.setnOfSuppressions(Supps[i]);
        }
    }

    /**
     * Runs the R-script file.
     */
    private void runR() throws ArgusException {
        try {
            ArrayList<String> arguments = new ArrayList<>();
            arguments.add("R");
            arguments.add("CMD");
            arguments.add("BATCH");
            arguments.add("--no-save");
            arguments.add("--no-restore");
            arguments.add(getModel().getrScriptFile().getAbsolutePath());
            ProcessBuilder builder = new ProcessBuilder(arguments);

            Process p = builder.start();
            p.waitFor();
        } catch (InterruptedException | IOException ex) {
            throw new ArgusException("Error running R script: " + ex.getMessage());
        }
    }

   
    /**
     * Cleans the anon-data and removes previous replacement file
     * instances.
     *
     * @param anonData AnonDataSpec containing the replacement file.
     */
    private void clean(AnonDataSpec anonData) {
        anonData.getKAnonCombinations().getTables().clear();
        anonData.getKAnonVariables().clear();
        anonData.getKAnonMissings().clear();
        anonData.getKAnonPriority().clear();
        anonData.getKAnonRStrings().clear();
        anonData.getKAnonThresholds().clear();
        ArrayList<ReplacementSpec> r = this.metadata.getReplacementSpecs();
        for (int i = r.size() - 1; i >= 0; i--) {
            if (r.get(i) instanceof AnonDataSpec) {
                r.remove(i);
            }
        }
    }
    
    /**
     * Gets the selected variables for k-anonymity.
     * Removes the variables to which PRAM is applied.
     * @return List of VariableMu's containing the selected variables.
     */
    private ArrayList<VariableMu> getKAnonVariables() {
        return getModel().getKAnonVariables();
    }
    
    private void fillKAnonVariables(){
        ProtectedFile protectedFile = this.metadata.getCombinations().getProtectedFile();
        if (this.metadata.getCombinations().getPramSpecification() != null) {
            if (this.metadata.getCombinations().getPramSpecification().getPramVarSpec().isEmpty())
            {
              for (VariableMu var : protectedFile.getVariables()) this.getKAnonVariables().add(var);
            } else {
                for (PramVariableSpec pramSpec : this.metadata.getCombinations().getPramSpecification().getPramVarSpec()) {
                    if (!pramSpec.isApplied() & protectedFile.getVariables().contains(pramSpec.getVariable())) {
                        this.getKAnonVariables().add(pramSpec.getVariable());
                    }
                }
            }
        }
    }
    
    private Combinations getKAnonCombinations() {
        return getModel().getKAnonCombinations();
    }
    
    /**
     * Assumes that fillKAnonVariables() is run first
     */
    private void fillKAnonCombinations(){
        for (TableMu tab : this.metadata.getCombinations().getTables()){
            boolean pramCombi = false;
            for (VariableMu var : tab.getVariables()){
                if (!getKAnonVariables().contains(var)) { // was removed in previous step
                    pramCombi = true;
                    break;
                }
            }
            if (!pramCombi) {
                getKAnonCombinations().addTable(tab);
            }
        }
    }
    
    private void removeRedundantKAnonVariables(){
        getKAnonVariables().clear();
        for (TableMu tab : getKAnonCombinations().getTables()){
            for (VariableMu var : tab.getVariables()){
                if (!getKAnonVariables().contains(var)) getKAnonVariables().add(var);
            }
        }
    }
    
    
    
    private ArrayList<Integer> getKAnonThresholds() {
        return getModel().getKAnonThresholds();
    }
    
    private void fillKAnonThresholds(){
        for (TableMu tab : getKAnonCombinations().getTables())
                getKAnonThresholds().add(tab.getThreshold());
    }
    
    private void fillKAnonRStrings(){
        for (TableMu tab : getKAnonCombinations().getTables()){
            String hs ="c(";
            for (VariableMu var : tab.getVariables()){
                hs += (getKAnonVariables().indexOf(var) + 1) + ",";
            }
            hs = hs.substring(0, hs.length()-1) + ")";
            getModel().getKAnonRStrings().add(hs);
        }
    }
    
    private void fillKAnonMissings(){
        for (TableMu tab : getKAnonCombinations().getTables()){
            String hs ="c(";
            for (VariableMu var : tab.getVariables()){
                hs += "\"" + (var.getMissing(0)) + "\",";
            }
            hs = hs.substring(0, hs.length()-1) + ")";
            getModel().getKAnonMissings().add(hs);
        }
    }
    
    private void fillKAnonPriority(){
        ProtectedFile protectedFile = this.metadata.getCombinations().getProtectedFile();
        for (TableMu tab : getKAnonCombinations().getTables()){
            String hs;
            if (protectedFile.isWithPrior()){
                hs = "c(";
                for (VariableMu var : tab.getVariables()){
                    hs += var.getSuppressPriority() + ",";
                }
                hs = hs.substring(0, hs.length()-1) + ")";
            }
            else{
                hs = "NULL";
            }
            getModel().getKAnonPriority().add(hs);
        }
    }
 
}
