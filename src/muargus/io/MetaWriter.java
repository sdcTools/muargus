/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.io;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import muargus.model.MetadataMu;
import static muargus.model.MetadataMu.DATA_FILE_TYPE_FREE;
import static muargus.model.MetadataMu.DATA_FILE_TYPE_SPSS;
import muargus.model.RecodeMu;
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
            PrintWriter writer = new PrintWriter(new File(path));
            writeRda(writer, metadata, all);
            metadata.setFileNames(new DataFilePair(metadata.getFileNames().getDataFileName(), path));
        } catch (IOException ex) {
            //logger.log(Level.SEVERE, null, ex);
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }
    
    public static void writeGrc(String path, RecodeMu recode) throws ArgusException {
    
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
    private static void writeRda(PrintWriter writer, MetadataMu metadata, boolean all)  {
            switch (metadata.getDataFileType()) {
                case DATA_FILE_TYPE_FREE:
                    writer.println("   <SEPARATOR> " + StrUtils.quote(metadata.getSeparator()));
                    break;
                case DATA_FILE_TYPE_SPSS:
                    writer.println("   <SPSS>");
                    break;
            }
            for (VariableMu variable : metadata.getVariables()) {
                writeVariableToRda(writer, variable, metadata.getDataFileType(), all);
            }
        
    }
    
    private static void writeVariableToRda(PrintWriter writer, VariableMu variable, int dataFileType, boolean all) {
        writer.print(variable.getName());
        if (MetadataMu.DATA_FILE_TYPE_FIXED == dataFileType) {
            writer.print(String.format(" %d", variable.getStartingPosition()));
        }
        writer.print(String.format(" %d", variable.getVariableLength()));
        if (variable.isCategorical()) {
            for (int index = 0; index < VariableMu.MAX_NUMBER_OF_MISSINGS; index++) {
                String missingValue = variable.getMissing(index);
                if (!StringUtils.isNotBlank(missingValue)) {
                    break;
                }
                writer.print(" " + StrUtils.quote(missingValue));
            }
        }
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
                writer.println("    <SUPPRESSWEIGHT> " + variable.getSuppressweight());
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
