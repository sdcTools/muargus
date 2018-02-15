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
package muargus.io;

import argus.model.ArgusException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import muargus.model.AnonDataSpec;
import muargus.model.MetadataMu;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.model.ProtectedFile;
import muargus.model.SyntheticDataSpec;

/**
 * Class for writing R related objects to file.
 *
 * @author Statistics Netherlands
 */
public class RWriter {

    /**
     * Writes the alpha file. The Alpha file contains a diagonal matrix with the
     * alpha values of the sensitive variables in the diagonal.
     *
     * @param synthData SyntheticDataSpec instance containig the replacement
     * file for synthetic data.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    public static void writeAlpha(SyntheticDataSpec synthData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(synthData.getAlphaFile())) {
            for (int i = 0; i < synthData.getSensitiveVariables().size(); i++) {
                String line = "";
                for (int j = 0; j < synthData.getSensitiveVariables().size(); j++) {
                    if (i == j) {
                        line += Double.toString(synthData.getSensitiveVariables().get(i).getAlpha());
                    } else {
                        line += "0.0";
                    }
                    if (j != synthData.getSensitiveVariables().size() - 1) {
                        line += ", ";
                    }
                }
                writer.println(line);
            }
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     * Writes the r-Script for generating synthetic data.
     *
     * @param synthData SyntheticDataSpec instance containig the replacement
     * file for synthetic data.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    public static void writeSynthetic(SyntheticDataSpec synthData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(synthData.getrScriptFile())) {
            writer.println("require(\"hybridIPSO\")");
            writer.println(String.format("hybrid_IPSO(\"%s\",\"%s\", K=%d,  out=TRUE, out_file=\"%s\", separator=\",\")",
                    synthData.doubleSlashses(synthData.getAlphaFile().getAbsolutePath()),
                    synthData.doubleSlashses(synthData.getReplacementFile().getInputFilePath()) + "2",
                    synthData.getSensitiveVariables().size(),
                    synthData.doubleSlashses(synthData.getReplacementFile().getOutputFilePath())));
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     *
     * @param synthData SyntheticDataSpec instance containig the replacement
     * file for synthetic data.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    public static void writeBatSynthetic(SyntheticDataSpec synthData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(synthData.getRunRFileFile())) {
            writer.println(String.format("R CMD BATCH \"%s\"", synthData.getrScriptFile().getAbsolutePath()));
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     * Adjusts the synthetic data output file to the format used by Mu-Argus.
     * Removes the first row containing the variable names used by R and the
     * beginning of each row containing the record number.
     *
     * @param synthData SyntheticDataSpec instance containig the replacement
     * file for synthetic data.
     * @return Boolean indicating whether the synthetic data was succesfully
     * adjusted.
     */
    public static boolean adjustSyntheticOutputFile(SyntheticDataSpec synthData) {
        boolean valid = true;
        File inputFile = new File(synthData.getReplacementFile().getOutputFilePath());
        File outputFile = new File(synthData.getReplacementFile().getOutputFilePath() + "2");
        outputFile.deleteOnExit();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                if (reader.readLine() == null) {
                    valid = false;
                }
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line.substring(line.indexOf(",") + 1, line.length()));
                }
                synthData.getReplacementFile().setOutputFilePath(outputFile);
            }
        } catch (IOException ex) {
            valid = false;
        }
        return valid;
    }

    /**
     * Ads a row to the synthetic data file. The row contains a header
     * containing the variable names that the R script expects. Sensitive
     * variables are numbered from x1 to xn, non-sensitive variables are
     * numbered from s1 to sn.
     *
     * @param synthData SyntheticDataSpec instance containig the replacement
     * file for synthetic data.
     */
    public static void adjustSyntheticData(SyntheticDataSpec synthData) throws ArgusException {
        //Adds a header containing the variable names that the R script expects
        File inputFile = new File(synthData.getReplacementFile().getInputFilePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line = "";
            for (int i = 0; i < synthData.getSensitiveVariables().size(); i++) {
                line += "x" + (i + 1) + " ,";
            }
            for (int i = 0; i < synthData.getNonSensitiveVariables().size(); i++) {
                line += "s" + (i + 1) + " ,";
            }
            line = line.substring(0, line.length() - 1);
            File outputFile = new File(synthData.getReplacementFile().getInputFilePath() + "2");
            outputFile.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                writer.println(line);
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
            }
        } catch (IOException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     * Writes the r-Script for generating (k+1)-anonymised data.
     *
     * @param anonDataSpec instance containing k-anonymisation info
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    public static void writeKAnon(AnonDataSpec anonData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(anonData.getrScriptFile())) {
            writer.println("require(\"sdcMicro\")");
            writer.println(String.format("ppin <- read.csv(\"%s\",sep=\";\",header=FALSE)",
                                            anonData.doubleSlashses(anonData.getdataFile().getAbsolutePath())));
            
            for (int i=0; i<anonData.getKAnonRStrings().size(); i++){
                writer.println(String.format("keyVars <- %s",anonData.getKAnonRStrings().get(i)));
                writer.println(String.format("missings <- %s",anonData.getKAnonMissings().get(i)));
                writer.println("importance <- NULL"); // Nog vullen m.b.v. priorities
                writer.println("if (!is.null(importance)) importance <- round((100 - importance) * length(importance) / 100)");
                writer.println(String.format("k <- %d",anonData.getKAnonThresholds().get(i)));
                writer.println("ppin[,keyVars] <- kAnon(ppin, keyVars=keyVars, importance=importance, k=k+1)$xAnon");
                writer.println("for (i in keyVars){");
                writer.println("\tppin[[i]][is.na(ppin[[i]])] <- missings[i]");
                writer.println("}");
            }
            
            File tmp2File = new File(anonData.doubleSlashses(anonData.getdataFile().getAbsolutePath())+2);
            tmp2File.deleteOnExit();
            writer.println(String.format("write.table(ppin,\"%s2\",row.names=FALSE,col.names=FALSE,quote=FALSE,sep=\";\")",
                    anonData.doubleSlashses(anonData.getdataFile().getAbsolutePath())));
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     *
     * @param kAnonData ProtectedFile instance containing Data 
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    public static void writeBatKAnon(AnonDataSpec anonData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(anonData.getRunRFileFile())) {
            writer.println(String.format("R CMD BATCH --no-save --no-restore \"%s\"", anonData.getrScriptFile().getAbsolutePath()));
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }
   
}
