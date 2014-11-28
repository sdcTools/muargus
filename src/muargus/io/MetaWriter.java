/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.io;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.model.SyntheticDataSpec;
import muargus.model.VariableMu;
import org.apache.commons.lang3.StringUtils;

/**
 * Class for writing various meta-objects to file
 *
 * @author Argus
 */
public class MetaWriter {

    public static void writeRda(String path, MetadataMu metadata, boolean all) throws ArgusException {
        try {
            try (PrintWriter writer = new PrintWriter(new File(path))) {
                writeRda(writer, metadata, all);
            }
            metadata.setFileNames(new DataFilePair(metadata.getFileNames().getDataFileName(), path));
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    /**
     * Writes the global recode text as a .grc file. The global recode file
     * contains the recodings (old and new codes), missing values and if
     * available the codelist.
     *
     * @param file The file for the safe global recode file.
     * @param recode RecodeMu instance containing the variable specifications
     * for global recoding
     * @throws ArgusException Throws an ArgusException when an error during the
     * writing of the file occurs.
     */
    public static void writeGrc(File file, RecodeMu recode) throws ArgusException {
        BufferedWriter w;
        PrintWriter writer = null;
        try {
            w = new BufferedWriter(new FileWriter(file));
            writer = new PrintWriter(w);
            writer.println(recode.getGrcText());
            if (recode.getMissing_1_new().length() > 0 || recode.getMissing_2_new().length() > 0) {
                writer.println(String.format("<MISSING> %s %s", recode.getMissing_1_new(), recode.getMissing_2_new()));
            }
            if (recode.getCodeListFile() != null && !recode.getCodeListFile().equals(recode.getVariable().getCodeListFile())) {
                writer.println(String.format("<CODELIST> \"%s\"", recode.getCodeListFile()));
            }
        } catch (IOException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    /**
     * Writes the metadata to a .rda file when a BufferdWritier has been
     * initialized.
     *
     * @param w BufferedWriter already initiated with the neede file.
     * @param all Boolean variable stating whether all metadata needs to be
     * written. When writing the safe metadata, the suppressionweight is for
     * example not necessary.
     * @throws IOException Throws an IOException when an error occurs during
     * writing.
     */
    private static void writeRda(PrintWriter writer, MetadataMu metadata, boolean all) {
        switch (metadata.getDataFileType()) {
            case MetadataMu.DATA_FILE_TYPE_FREE:
                writer.println("   <SEPARATOR> " + StrUtils.quote(metadata.getSeparator()));
                break;
            case MetadataMu.DATA_FILE_TYPE_SPSS:
                writer.println("   <SPSS>");
                break;
        }
        for (VariableMu variable : metadata.getVariables()) {
            writeVariableToRda(writer, variable, metadata.getDataFileType(), all);
        }

    }

    private static void writeVariableToRda(PrintWriter writer, VariableMu variable, int dataFileType, boolean all) {
        writer.print(variable.getName());
        if (MetadataMu.DATA_FILE_TYPE_FIXED == dataFileType || dataFileType == MetadataMu.DATA_FILE_TYPE_SPSS) {
            writer.print(String.format(" %d", variable.getStartingPosition()));
        }
        writer.print(String.format(" %d", variable.getVariableLength()));
        //if (variable.isCategorical()) {
        for (int index = 0; index < VariableMu.MAX_NUMBER_OF_MISSINGS; index++) {
            String missingValue = variable.getMissing(index);
            if (!StringUtils.isNotBlank(missingValue)) {
                break;
            }
            writer.print(" " + StrUtils.quote(missingValue));
        }
        //}
        writer.println();
        if (variable.isRecodable()) {
            writer.println("    <RECODABLE>");
        }
        if (variable.isNumeric()) {
            writer.println("    <NUMERIC>");
        }
        if (variable.getDecimals() > 0) {
            writer.println("    <DECIMALS> " + variable.getDecimals());
        }
        if (variable.isWeight()) {
            writer.println("    <WEIGHT>");
        }
        if (variable.isHouse_id()) {
            writer.println("    <HOUSE_ID>");
        }
        if (variable.isHousehold()) {
            writer.println("    <HOUSEHOLD>");
        }
        if (variable.getRelatedVariable() != null) {
            writer.println("    <RELATED> " + StrUtils.quote(variable.getRelatedVariable().getName()));
        }
        if (variable.isCategorical()) {
            if (variable.isTruncable()) {
                writer.println("    <TRUNCABLE>");
            }
            if (all) {
                writer.println("    <IDLEVEL> " + variable.getIdLevel());
                writer.println("    <SUPPRESSWEIGHT> " + variable.getSuppressPriority());
            }
            if (variable.isCodelist()) {
                writer.println("    <CODELIST> " + StrUtils.quote(variable.getCodeListFile()));
            }
        }

    }

    /**
     * Writes the metadata to a .rda file.
     *
     * @param file File containing the metadata.
     * @param all all metadata wegschrijven ja of nee? --> safe metadata neemt
     * niet alles mee (suppressionweight)
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during writing.
     */
    
}
