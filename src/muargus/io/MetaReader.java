/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.io;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import argus.utils.Tokenizer;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import muargus.controller.SpssUtils;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.model.VariableMu;

/**
 * Class for reading various meta-objects from file
 *
 * @author Argus
 */
public class MetaReader {

    /**
     * Reads the entire metafile and initalizes the variables. This method reads
     * the metadatafile line for line. It makes a new variable object for each
     * variable it finds and provides the relevant information of this variable
     * (is it recodable, what is it's ID_level etc).
     *
     * @param metadata
     * @param parent
     * @throws ArgusException Throws an ArgusException when the file cannot be
     * read.
     */
    public static void readRda(MetadataMu metadata, Frame parent) throws ArgusException {

        if (metadata.getFileNames().getMetaFileName().length() == 0) {
            String filename = metadata.getFileNames().getDataFileName();
            String extension = filename.substring(filename.length() - 3, filename.length());
            if (extension.equalsIgnoreCase("sav")) {
                metadata.setDataFileType(MetadataMu.DATA_FILE_TYPE_SPSS);
            }
            return;
        }

        metadata.setDataFileType(MetadataMu.DATA_FILE_TYPE_FIXED);
        VariableMu variable = null;

        try {
            File file = new File(metadata.getFileNames().getMetaFileName());
            BufferedReader reader = new BufferedReader(new FileReader(file));

            Tokenizer tokenizer = new Tokenizer(reader);
            while (tokenizer.nextLine() != null) {
                String value = tokenizer.nextToken();

                if (!value.substring(0, 1).equals("<")) {
                    variable = new VariableMu();
                    variable.setRecodable(false);
                    variable.setName(tokenizer.getValue());
                    metadata.getVariables().add(variable);
                    if (metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_FIXED
                            || metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS) {
                        try {
                            variable.setStartingPosition(Integer.parseInt(tokenizer.nextToken()));
                        } catch (NumberFormatException e) {
                            throw new ArgusException(String.format("Starting position of the variable %s is not an integer", variable.getName()));
                        }
                    } else {
                        variable.setStartingPosition(1);  //not relevant, but must be >0
                    }
                    variable.setVariableLength(Integer.parseInt(tokenizer.nextToken()));
                    variable.setMissing(0, tokenizer.nextToken());
                    variable.setMissing(1, tokenizer.nextToken());
                } else if (variable == null) {
                    switch (value) {
                        case "<SEPARATOR>":
                            metadata.setDataFileType(MetadataMu.DATA_FILE_TYPE_FREE);
                            metadata.setSeparator(tokenizer.nextToken());
                            break;
                        case "<SPSS>":
                            metadata.setDataFileType(MetadataMu.DATA_FILE_TYPE_SPSS);
                            break;
                        case "<NAMESINFRONT>":
                            metadata.setDataFileType(MetadataMu.DATA_FILE_TYPE_FREE_WITH_META);
                            break;
                    }
                } else {
                    switch (value) {
                        case "<RECODEABLE>":
                        case "<RECODABLE>":
                            variable.setRecodable(true);
                            break;
                        case "<CODELIST>":
                            variable.setCodelist(true);
                            variable.setCodeListFile(tokenizer.nextToken());
                            break;
                        case "<IDLEVEL>":
                            try {
                                variable.setIdLevel(Integer.parseInt(tokenizer.nextToken()));
                            } catch (NumberFormatException e) {
                                throw new ArgusException(String.format("id-level of the variable %s is not an integer", variable.getName()));
                            }
                            break;
                        case "<TRUNCABLE>":
                            variable.setTruncable(true);
                            break;
                        case "<NUMERIC>":
                            variable.setNumeric(true);
                            break;
                        case "<DECIMALS>":
                            try {
                                variable.setDecimals(Integer.parseInt(tokenizer.nextToken()));
                            } catch (NumberFormatException e) {
                                throw new ArgusException(String.format("Number of decimals of the variable %s is not an integer", variable.getName()));
                            }
                            break;
                        case "<WEIGHT>":
                            variable.setWeight(true);
                            break;
                        case "<HOUSE_ID>":
                            variable.setHouse_id(true);
                            break;
                        case "<HOUSEHOLD>":
                            variable.setHousehold(true);
                            break;
                        case "<SUPPRESSWEIGHT>":
                            try {
                                variable.setSuppressweight(Integer.parseInt(tokenizer.nextToken()));
                            } catch (NumberFormatException e) {
                                throw new ArgusException(String.format("Suppressweight of the variable %s is not an integer", variable.getName()));
                            }
                            break;
                        case "<RELATED>":
                            variable.setRelatedVariableName(tokenizer.nextToken());
                            break;
                        default:
                            break;
                    }
                }
            }
            tokenizer.close();

            metadata.linkRelatedVariables();
        } catch (FileNotFoundException ex) {
            //System.out.println("file not found");
            //logger.log(Level.SEVERE, null, ex);
            throw new ArgusException("Metadata file not found");
        }

        if (metadata.getDataFileType() == MetadataMu.DATA_FILE_TYPE_SPSS) {
            SpssUtils.checkMetadata(metadata, parent);
        }

    }

    public static HashMap<String, String> readCodelist(String path, MetadataMu metadata) throws ArgusException {
        BufferedReader reader = null;
        try {
            File file = new File(path);
            if (!file.isAbsolute()) {
                File dir = new File(metadata.getFileNames().getMetaFileName()).getParentFile();
                file = new File(dir, path);
            }
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            //System.out.println("file not found");
            //logger.log(Level.SEVERE, null, ex);
            throw new ArgusException(String.format("Codelist %s not found", path));
        }
        HashMap<String, String> codelist = new HashMap<>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    codelist.put(parts[0].trim(), StrUtils.unQuote(parts[1].trim()));
                }
            }
            reader.close();
        } catch (IOException ex) {
            //logger.log(Level.SEVERE, null, ex);
            //Try to close the reader
            try {
                reader.close();
            } catch (IOException e) {

            }
            throw new ArgusException(String.format("Error in codelist file (%s)", path));
        }
        return codelist;
    }

    /**
     * Reads the global recode file. Sets the recode codes, new missing values
     * and the codelist file.
     *
     * @param path String containing the path name of the global recode file.
     * @param recode RecodeMu containing the information on the variable for
     * which the global recode file will be read.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during reading.
     */
    public static void readGrc(String path, RecodeMu recode) throws ArgusException {
        recode.setMissing_1_new("");
        recode.setMissing_2_new("");
        recode.setCodeListFile("");
        File file = new File(path);
        try {
            StringBuilder sb;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                sb = new StringBuilder();
                String line;
                Tokenizer tokenizer = new Tokenizer(reader);
                while ((line = tokenizer.nextLine()) != null) {
                    String token = tokenizer.nextToken();
                    if (!token.startsWith("<")) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                    } else if ("<MISSING>".equals(token)) {
                        token = tokenizer.nextToken();
                        recode.setMissing_1_new(token);
                        token = tokenizer.nextToken();
                        if (token != null) {
                            recode.setMissing_2_new(token);
                        }
                    } else if ("<CODELIST>".equals(token)) {
                        recode.setCodeListFile(tokenizer.nextToken());
                    } else {
                        throw new ArgusException("Error reading file, invalid token: " + token);
                    }
                }
            }
            recode.setGrcText(sb.toString());
            recode.setGrcFile(path);
        } catch (IOException ex) {
            throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }

    }

    /**
     * Reads the first line from a file, and return the content of this line as
     * a String array, using the supplied separator
     *
     * @param path Path to the file that is to be read
     * @param separator Separator separating the field names in the first line
     * @return String array containing the field names
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during reading.
     */
    public static String[] readHeader(String path, String separator) throws ArgusException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String firstLine = reader.readLine();
            return firstLine.split(separator);

        } catch (IOException ex) {
            //logger.log(Level.SEVERE, null, ex);
            throw new ArgusException("Error reading data file");
        }
    }

}
