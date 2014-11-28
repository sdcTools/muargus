/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.io;

import argus.model.ArgusException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import muargus.model.SyntheticDataSpec;

/**
 *
 * @author pibd05
 */
public class RWriter {

    /**
     *
     * @param synthData
     * @throws ArgusException
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

    public static void writeSynthetic(SyntheticDataSpec synthData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(synthData.getrScriptFile())) {
            writer.println("require(\"hybribISO3\")");
            writer.println(String.format("hybrid_IPSO(\"%s\",\"%s\", K=%d,  out=TRUE, out_file=\"%s\", separator=\",\")",
                    synthData.doubleSlashses(synthData.getAlphaFile().getAbsolutePath()),
                    synthData.doubleSlashses(synthData.getReplacementFile().getInputFilePath()) + "2",
                    synthData.getSensitiveVariables().size(),
                    synthData.doubleSlashses(synthData.getReplacementFile().getOutputFilePath())));

        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    public static void writeBatSynthetic(SyntheticDataSpec synthData) throws ArgusException {
        try (PrintWriter writer = new PrintWriter(synthData.getRunRFileFile())) {
            writer.println(String.format("R CMD BATCH \"%s\"", synthData.getrScriptFile().getAbsolutePath()));
        } catch (FileNotFoundException ex) {
            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
        }
    }

    public static boolean adjustSyntheticOutputFile(SyntheticDataSpec model) {
        boolean valid = true;
        File inputFile = new File(model.getReplacementFile().getOutputFilePath());
        File outputFile = new File(model.getReplacementFile().getOutputFilePath() + "2");
        outputFile.deleteOnExit();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                if (reader.readLine() == null) {
                    valid = false;
                }
                //reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.println(line.substring(line.indexOf(",") + 1, line.length()));
                }
                model.getReplacementFile().setOutputFilePath(outputFile);
            }
        } catch (IOException ex) {
            valid = false;
            //throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }
        return valid;
    }

    public static void adjustSyntheticData(SyntheticDataSpec model) {
        //Adds a header containing the variable names that the R script expects
        File inputFile = new File(model.getReplacementFile().getInputFilePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line = "";
            for (int i = 0; i < model.getSensitiveVariables().size(); i++) {
                line += "x" + (i + 1) + " ,";
            }
            for (int i = 0; i < model.getNonSensitiveVariables().size(); i++) {
                line += "s" + (i + 1) + " ,";
            }
            line = line.substring(0, line.length() - 1);
            File outputFile = new File(model.getReplacementFile().getInputFilePath() + "2");
            outputFile.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(outputFile)) {
                writer.println(line);
                while ((line = reader.readLine()) != null) {
                    writer.println(line);
                }
            }
        } catch (IOException ex) {
            //throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }
    }

}
