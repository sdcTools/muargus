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
    private String grcText;
    private final VariableMu variable;
    private String grcFile;
    private String codeListFile; // willen we dit als String of als File?
    private final String missing_1_original;
    private final String missing_2_original;
    private String missing_1_new;
    private String missing_2_new;
    

    public RecodeMu(VariableMu variable) {
        this.variable = variable;
        this.truncated = false;
        this.recoded = false;
        this.grcFile = null;
        this.grcText = null;
        this.codeListFile = variable.getCodeListFile();
        this.missing_1_new = "";
        this.missing_2_new = "";
        this.missing_1_original = variable.getMissing(0);
        this.missing_2_original = variable.getMissing(1);
    }

    public String getGrcFile() {
        return this.grcFile;
    }

    public void setGrcFile(String grcFile) {
        this.grcFile = grcFile;
    }

    public String getCodeListFile() {
        return this.codeListFile;
    }

    public void setCodeListFile(String codeListFile) {
        this.codeListFile = codeListFile;
    }


    public String getGrcText() {
        return this.grcText;
    }

    public void setGrcText(String grcText) {
        this.grcText = grcText;
    }
    
    public VariableMu getVariable() {
        return this.variable;
    }
    
    public boolean isTruncated() {
        return this.truncated;
    }

    public void setTruncated(boolean isTruncated) {
        this.truncated = isTruncated;
        if (isTruncated) {
            this.recoded = false;
        }
    }

    public boolean isRecoded() {
        return this.recoded;
    }
    
    public void setRecoded(boolean isRecoded) {
        this.recoded = isRecoded;
    }

    public String getMissing_1_new() {
        return this.missing_1_new;
    }

    public void setMissing_1_new(String missing_1_new) {
        this.missing_1_new = missing_1_new;
    }

    public String getMissing_2_new() {
        return this.missing_2_new;
    }

    public void setMissing_2_new(String missing_2_new) {
        this.missing_2_new = missing_2_new;
    }

    public String getMissing_1_original() {
        return this.missing_1_original;
    }

    public String getMissing_2_original() {
        return this.missing_2_original;
    }

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

}
