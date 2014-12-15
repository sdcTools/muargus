package muargus.model;

import java.util.Objects;

/**
 * Model class containing the variable specifications for global recoding. An
 * instance for each categorical variable of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class RecodeMu {

    private boolean truncated;
    private int positionsTruncated;
    private boolean recoded;
    private String grcText;
    private final VariableMu variable;
    private String grcFile;
    private String codeListFile;
    private final String missing_1_original;
    private final String missing_2_original;
    private String missing_1_new;
    private String missing_2_new;
    private String appliedCodeListFile;

    /**
     * Constructor of the model class RecodeMu. Initializes the variable, sets
     * the original missing values and sets the default values.
     *
     * @param variable The variable for which global recoding can be applied.
     */
    public RecodeMu(VariableMu variable) {
        this.variable = variable;
        this.truncated = false;
        this.recoded = false;
        this.grcFile = "";
        this.grcText = "";
        this.codeListFile = "";
        this.appliedCodeListFile = "";
        this.missing_1_new = "";
        this.missing_2_new = "";
        this.missing_1_original = variable.getMissing(0);
        this.missing_2_original = variable.getMissing(1);
    }

    /**
     * Constructor of the model class RecodeMu used for cloning. Clones the
     * RecodeMu in a new RecodeMu.
     *
     * @param recode RecodeMu instance containing the original RecodeMu.
     */
    public RecodeMu(RecodeMu recode) {
        this.variable = recode.variable;
        this.truncated = recode.truncated;
        this.recoded = recode.recoded;
        this.grcFile = recode.grcFile;
        this.grcText = recode.grcText;
        this.codeListFile = recode.codeListFile;
        this.missing_1_new = recode.missing_1_new;
        this.missing_2_new = recode.missing_2_new;
        this.missing_1_original = recode.missing_1_original;
        this.missing_2_original = recode.missing_2_original;
        this.appliedCodeListFile = recode.appliedCodeListFile;
    }

    /**
     * Get the path of the global recode file.
     *
     * @return String containing the path of the global recode file.
     */
    public String getGrcFile() {
        return this.grcFile;
    }

    /**
     * Set the path of the global recode file.
     *
     * @param grcFile String containing the path of the global recode file.
     */
    public void setGrcFile(String grcFile) {
        this.grcFile = grcFile;
    }

    /**
     * Get the path of the code list file.
     *
     * @return String containing the the path of the code list file.
     */
    public String getCodeListFile() {
        return this.codeListFile;
    }

    /**
     * Set the path of the code list file.
     *
     * @param codeListFile String containing the the path of the code list file.
     */
    public void setCodeListFile(String codeListFile) {
        this.codeListFile = codeListFile;
    }

    /**
     * Get the path of the code list file that was used when applying the recode.
     *
     * @return String containing the the path of the code list file that was
     * used when applying the recpde
     */
    public String getAppliedCodeListFile() {
        return appliedCodeListFile;
    }

    /**
     * Set the path of the code list file that was used when applying the recode.
     *
     * @param appliedCodeListFile String containing the the path of the code list file
     * that was used when applying the recode.
     */
    public void setAppliedCodeListFile(String appliedCodeListFile) {
        this.appliedCodeListFile = appliedCodeListFile;
    }

    /**
     * Get the global recode text. The global recode text contains the recodings
     * for the codes. The text mostly consists of multiple lines and each lines
     * gives: 1) the new code, 2) a colon, 3) the original codes that will be
     * recoded. Example: "20:1-8".
     *
     * @return String containing the global recode text.
     */
    public String getGrcText() {
        return this.grcText;
    }

    /**
     * Set the global recode text. The global recode text contains the recodings
     * for the codes. The text mostly consists of multiple lines and each lines
     * gives: 1) the new code, 2) a colon, 3) the original codes that will be
     * recoded. Example: "20:1-8".
     *
     * @param grcText String containing the global recode text.
     */
    public void setGrcText(String grcText) {
        this.grcText = grcText;
    }

    /**
     * Gets the variable.
     *
     * @return VariableMu instance containing the variable.
     */
    public VariableMu getVariable() {
        return this.variable;
    }

    /**
     * Returns whether the variable is truncated.
     *
     * @return Boolean indicating whether the variable is truncated.
     */
    public boolean isTruncated() {
        return this.truncated;
    }

    /**
     * Sets whether the variable is truncated. If a variable is truncated the
     * boolean recoded is set to false. This does not mean that a truncated
     * variable can not also be recoded, but it is a way of knowing which form
     * of recoding has been done last.
     *
     * @param isTruncated Boolean indicating whether the variable is truncated.
     */
    public void setTruncated(boolean isTruncated) {
        this.truncated = isTruncated;
        if (isTruncated) {
            this.recoded = false;
        }
    }

    /**
     * Returns whether the variable is recoded.
     *
     * @return Boolean indicating whether the variable is recoded.
     */
    public boolean isRecoded() {
        return this.recoded;
    }

    /**
     * Sets whether the variable is recoded. If a variable is recoded the
     * boolean truncated is set to false. This does not mean that a recoded
     * variable can not also be truncated, but it is a way of knowing which form
     * of recoding has been done last.
     *
     * @param isRecoded Boolean indicating whether the variable is recoded.
     */
    public void setRecoded(boolean isRecoded) {
        this.recoded = isRecoded;
        if (isRecoded) {
            this.truncated = false;
        }
    }

    /**
     * Gets the new first missing value after recoding.
     *
     * @return String containing the new first missing value after recoding.
     */
    public String getMissing_1_new() {
        return this.missing_1_new;
    }

    /**
     * Sets the new first missing value after recoding.
     *
     * @param missing_1_new String containing the new first missing value after
     * recoding.
     */
    public void setMissing_1_new(String missing_1_new) {
        this.missing_1_new = missing_1_new;
    }

    /**
     * Gets the new second missing value after recoding.
     *
     * @return String containing the new second missing value after recoding.
     */
    public String getMissing_2_new() {
        return this.missing_2_new;
    }

    /**
     * Sets the new second missing value after recoding.
     *
     * @param missing_2_new String containing the new second missing value after
     * recoding.
     */
    public void setMissing_2_new(String missing_2_new) {
        this.missing_2_new = missing_2_new;
    }

    /**
     * Gets the original first missing value.
     *
     * @return String containing the original first missing value.
     */
    public String getMissing_1_original() {
        return this.missing_1_original;
    }

    /**
     * Gets the original second missing value.
     *
     * @return String containing the original second missing value.
     */
    public String getMissing_2_original() {
        return this.missing_2_original;
    }

    /**
     * Get the number of positions that are truncated.
     *
     * @return String containing the number of positions that are truncated.
     */
    public int getPositionsTruncated() {
        return positionsTruncated;
    }

    /**
     * Set the number of positions that are truncated
     *
     * @param positionsTruncated String containing the number of positions that
     * are truncated.
     */
    public void setPositionsTruncated(int positionsTruncated) {
        this.positionsTruncated = positionsTruncated;
    }

    /**
     * Gets the data shown in the variables table for one row.
     *
     * @return Array of Strings containing the a symbol for the form of recoding
     * in the first column and the variable name in the second column.
     */
    public String[] getTableRow() {
        String[] row = new String[2];
        if (this.isRecoded()) {
            row[0] = "R";
        } else if (this.isTruncated()) {
            row[0] = "T";
        } else {
            row[0] = "";
        }
        row[1] = this.getVariable().getName();

        return row;
    }

    //TODOD: staat nu bij MetaWriter. Misschien beter om te veranderen naar een aparte klasse?
//    /**
//     * Writes the global recode text as a .grc file. The global recode file
//     * contains the recodings (old and new codes), missing values and if
//     * available the codelist.
//     *
//     * @param file The file for the safe global recode file.
//     * @throws ArgusException Throws an ArgusException when an error during the
//     * writing of the file occurs.
//     */
//    public void write(File file) throws ArgusException {
//        BufferedWriter w;
//        PrintWriter writer = null;
//        try {
//            w = new BufferedWriter(new FileWriter(file));
//            writer = new PrintWriter(w);
//            writer.println(this.grcText);
//            if (this.missing_1_new.length() > 0 || this.missing_2_new.length() > 0) {
//                writer.println(String.format("<MISSING> %s %s", this.missing_1_new, this.missing_2_new));
//            }
//            if (this.codeListFile != null && !this.codeListFile.equals(this.variable.getCodeListFile())) {
//                writer.println(String.format("<CODELIST> \"%s\"", this.codeListFile));
//            }
//        } catch (IOException ex) {
//            throw new ArgusException("Error writing to file. Error message: " + ex.getMessage());
//        } finally {
//            if (writer != null) {
//                writer.close();
//            }
//        }
//    }

    /**
     * Checks if a RecodeMu object is equal to this instance of RecodeMu. Each
     * individual component of this RecodeMu instance is compaired to its
     * counterpart of the given RecodeMu instance.
     *
     * @param o Object containing the RecodeMu object that will be compaired to
     * this instance of RecodeMu.
     * @return Boolean indicating whether the given RecodeMu object is equal to
     * this instance of RecodeMu.
     */
    @Override
    public boolean equals(Object o) {
        RecodeMu cmp = (RecodeMu) o;
        if (cmp == null) {
            return false;
        }

        return //(this.recoded == cmp.recoded) &&
                //(this.truncated == cmp.truncated) &&
                (this.codeListFile == null ? cmp.codeListFile == null : this.codeListFile.equals(cmp.codeListFile))
                && (this.grcFile == null ? cmp.grcFile == null : this.grcFile.equals(cmp.grcFile))
                && (this.grcText == null ? cmp.grcText == null : this.grcText.equals(cmp.grcText))
                && (this.missing_1_new == null ? cmp.missing_1_new == null : this.missing_1_new.equals(cmp.missing_1_new))
                && (this.missing_2_new == null ? cmp.missing_2_new == null : this.missing_2_new.equals(cmp.missing_2_new))
                && (this.missing_1_original == null ? cmp.missing_1_original == null : this.missing_1_original.equals(cmp.missing_1_original))
                && (this.missing_2_original == null ? cmp.missing_2_original == null : this.missing_2_original.equals(cmp.missing_2_original));
    }

    /**
     * Gets the hashcode. The hashcode is calculated as a addition of the
     * hashcodes from the relevant individual components: codeListFile, grcFile,
     * grcText, missing_1_new, missing_2_new, missing_1_original and
     * missing_2_original.
     *
     * @return Integer containing the hashcode.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        //hash = 41 * hash + Objects.hashCode(this.recoded);
        //hash = 41 * hash + Objects.hashCode(this.truncated);
        hash = 41 * hash + Objects.hashCode(this.codeListFile);
        hash = 41 * hash + Objects.hashCode(this.grcFile);
        hash = 41 * hash + Objects.hashCode(this.grcText);
        hash = 41 * hash + Objects.hashCode(this.missing_1_new);
        hash = 41 * hash + Objects.hashCode(this.missing_2_new);
        hash = 41 * hash + Objects.hashCode(this.missing_1_original);
        hash = 41 * hash + Objects.hashCode(this.missing_2_original);

        return hash;
    }

}
