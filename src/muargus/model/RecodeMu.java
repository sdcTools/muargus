/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import argus.model.ArgusException;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;

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
        this.grcFile = "";
        this.grcText = "";
        this.codeListFile = variable.getCodeListFile();
        this.missing_1_new = "";
        this.missing_2_new = "";
        this.missing_1_original = variable.getMissing(0);
        this.missing_2_original = variable.getMissing(1);
    }
    
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

    public void write(File file) throws ArgusException {
        //TODO implement;
    }
    
    @Override
    public boolean equals(Object o) {
        RecodeMu cmp = (RecodeMu)o;
        if (cmp == null)
            return false;
        
        return (this.recoded == cmp.recoded)
                && (this.truncated == cmp.truncated)
                && (this.codeListFile == null ? cmp.codeListFile == null : this.codeListFile.equals(cmp.codeListFile))
                && (this.grcFile == null ? cmp.grcFile == null : this.grcFile.equals(cmp.grcFile))
                && (this.grcText == null ? cmp.grcText == null : this.grcText.equals(cmp.grcText))
                && (this.missing_1_new == null ? cmp.missing_1_new == null : this.missing_1_new.equals(cmp.missing_1_new))
                && (this.missing_2_new == null ? cmp.missing_2_new == null : this.missing_2_new.equals(cmp.missing_2_new))
                && (this.missing_1_original == null ? cmp.missing_1_original == null : this.missing_1_original.equals(cmp.missing_1_original))
                && (this.missing_2_original == null ? cmp.missing_2_original == null : this.missing_2_original.equals(cmp.missing_2_original));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.recoded);
        hash = 41 * hash + Objects.hashCode(this.truncated);
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
