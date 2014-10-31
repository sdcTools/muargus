package muargus.model;

import argus.model.ArgusException;
import java.io.File;
import java.io.IOException;

/**
 * Model class of the replacement file.
 *
 * @author Statistics Netherlands
 */
public class ReplacementFile {

    private final File inputFile;
    private final File outputFile;
    private final String replacementType; //TODO: wordt dit gebruikt? Kan het ook een int zijn?

    /**
     * Constructor of the model class ReplacementFile.
     *
     * @param replacementType String containing the replacement type.
     * @throws ArgusException Throws an ArgusException when the replacement file
     * cannot be created.
     */
    public ReplacementFile(String replacementType) throws ArgusException {
        this.replacementType = replacementType;
        this.inputFile = createFile();
        this.outputFile = createFile();
    }

    /**
     * Gets the input file path.
     *
     * @return String containing the input file path.
     */
    public String getInputFilePath() {
        return this.inputFile.getPath();
    }

    /**
     * Gets the output file path.
     *
     * @return String containing the output file path.
     */
    public String getOutputFilePath() {
        return this.outputFile.getPath();
    }

    /**
     * Gets the replacement type.
     *
     * @return String containing the replacement type.
     */
    public String getReplacementType() {
        return replacementType;
    }

    /**
     * Creates a temporary replacement file. The replacement file is placed in
     * the temp directory and removed when the java virtual machine is closed.
     *
     * @return File instance containing the replacement file.
     * @throws ArgusException Throws an ArgusException when the replacement file
     * cannot be created.
     */
    private File createFile() throws ArgusException {
        try {
            File file = File.createTempFile("MuArgus", ".rpl");
            file.deleteOnExit();
            return file;
        } catch (IOException ex) {
            throw new ArgusException("Replacement file cannot be created: " + ex.getMessage());
        }
    }

}
