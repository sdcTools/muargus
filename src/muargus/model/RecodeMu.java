package muargus.model;

import argus.model.ArgusException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * Model class containing the variable specifications for global recoding. An
 * instance for each categorical variable of this class will exist.
 *
 * @author Statistics Netherlands
 */
public class RecodeMu {

    private boolean truncated;
    private String positionsTruncated;
    private boolean recoded;
    private String grcText;
    private final VariableMu variable;
    private String grcFile;
    private String codeListFile;
    private final String missing_1_original;
    private final String missing_2_original;
    private String missing_1_new;
    private String missing_2_new;

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
    }

    /**
     * 
     * @return 
     */
    public String getGrcFile() {
        return this.grcFile;
    }

    /**
     * 
     * @param grcFile 
     */
    public void setGrcFile(String grcFile) {
        this.grcFile = grcFile;
    }

    /**
     * 
     * @return 
     */
    public String getCodeListFile() {
        return this.codeListFile;
    }

    /**
     * 
     * @param codeListFile 
     */
    public void setCodeListFile(String codeListFile) {
        this.codeListFile = codeListFile;
    }

    /**
     * 
     * @return 
     */
    public String getGrcText() {
        return this.grcText;
    }

    /**
     * 
     * @param grcText 
     */
    public void setGrcText(String grcText) {
        this.grcText = grcText;
    }

    /**
     * 
     * @return 
     */
    public VariableMu getVariable() {
        return this.variable;
    }

    /**
     * 
     * @return 
     */
    public boolean isTruncated() {
        return this.truncated;
    }

    /**
     * 
     * @param isTruncated 
     */
    public void setTruncated(boolean isTruncated) {
        this.truncated = isTruncated;
        if (isTruncated) {
            this.recoded = false;
        }
    }

    /**
     * 
     * @return 
     */
    public boolean isRecoded() {
        return this.recoded;
    }

    /**
     * 
     * @param isRecoded 
     */
    public void setRecoded(boolean isRecoded) {
        this.recoded = isRecoded;
        if (isRecoded) {
            this.truncated = false;
        }
    }

    /**
     * 
     * @return 
     */
    public String getMissing_1_new() {
        return this.missing_1_new;
    }

    /**
     * 
     * @param missing_1_new 
     */
    public void setMissing_1_new(String missing_1_new) {
        this.missing_1_new = missing_1_new;
    }

    /**
     * 
     * @return 
     */
    public String getMissing_2_new() {
        return this.missing_2_new;
    }

    /**
     * 
     * @param missing_2_new 
     */
    public void setMissing_2_new(String missing_2_new) {
        this.missing_2_new = missing_2_new;
    }

    /**
     * 
     * @return 
     */
    public String getMissing_1_original() {
        return this.missing_1_original;
    }

    /**
     * 
     * @return 
     */
    public String getMissing_2_original() {
        return this.missing_2_original;
    }

    /**
     * 
     * @return 
     */
    public String getPositionsTruncated() {
        return positionsTruncated;
    }

    /**
     * 
     * @param positionsTruncated 
     */
    public void setPositionsTruncated(String positionsTruncated) {
        this.positionsTruncated = positionsTruncated;
    }

    /**
     * 
     * @return 
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

    /**
     * 
     * @param file
     * @throws ArgusException 
     */
    public void write(File file) throws ArgusException {
        BufferedWriter w;
        PrintWriter writer = null;
        try {
            w = new BufferedWriter(new FileWriter(file));
            writer = new PrintWriter(w);
            writer.println(this.grcText);
            if (this.missing_1_new.length() > 0 || this.missing_2_new.length() > 0) {
                writer.println(String.format("<MISSING> %s %s", this.missing_1_new, this.missing_2_new));
            }
            if (this.codeListFile != null && !this.codeListFile.equals(this.variable.getCodeListFile())) {
                writer.println(String.format("<CODELIST> \"%s\"", this.codeListFile));
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
     * 
     * @param o
     * @return 
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
     * 
     * @return 
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
