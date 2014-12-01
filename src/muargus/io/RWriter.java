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
            writer.println("require(\"hybribISO3\")");//TODO: change to hybridIPSO3 when a new hybridIPSO is compiled.
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
            //throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
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
    public static void adjustSyntheticData(SyntheticDataSpec synthData) {
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
            //throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }
    }

}
