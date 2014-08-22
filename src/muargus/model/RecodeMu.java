/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.io.File;

/**
 *
 * @author ambargus
 */
public class RecodeMu {

    private boolean truncated;
    private boolean recoded;
    private boolean read;
    private final VariableMu variable;
    private File grcFile;
    private String codeListFile; // willen we dit als String of als File?
    private final String missing_1_original;
    private final String missing_2_original;
    private String missing_1_new;
    private String missing_2_new;
    

    public RecodeMu(VariableMu variable) {
        this.variable = variable;
        this.truncated = false;
        this.read = false;
        this.recoded = false;
        this.grcFile = null;
        this.codeListFile = variable.getCodeListFile();
        this.missing_1_new = "";
        this.missing_2_new = "";
        this.missing_1_original = variable.getMissing(0);
        this.missing_2_original = variable.getMissing(1);
    }

    public File getGrcFile() {
        return grcFile;
    }

    public void setGrcFile(File grcFile) {
        this.grcFile = grcFile;
    }

    public String getCodeListFile() {
        return codeListFile;
    }

    public void setCodeListFile(String codeListFile) {
        this.codeListFile = codeListFile;
    }

    public VariableMu getVariable() {
        return variable;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
        if (read) {
            this.truncated = false;
        }
    }
    
    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean isTruncated) {
        this.truncated = isTruncated;
        if (isTruncated) {
            this.recoded = false;
        }
    }

    public boolean isRecoded() {
        return isTruncated() || isRead();
    }

    public String getMissing_1_new() {
        return missing_1_new;
    }

    public void setMissing_1_new(String missing_1_new) {
        this.missing_1_new = missing_1_new;
    }

    public String getMissing_2_new() {
        return missing_2_new;
    }

    public void setMissing_2_new(String missing_2_new) {
        this.missing_2_new = missing_2_new;
    }

    public String getMissing_1_original() {
        return missing_1_original;
    }

    public String getMissing_2_original() {
        return missing_2_original;
    }

    public String[] getTableRow() {
        String[] row = new String[2];
        if (this.isRead()) {
            row[0] = "R";
        } else if (this.isTruncated()) {
            row[0] = "T";
        } else {
            row[0] = "";
        }
        row[1] = this.getVariable().getName();

        return row;
    }

}
