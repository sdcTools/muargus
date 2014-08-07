/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ambargus
 */
public class VariableMu {
    
    // Determines lengths of fixed sized arrays being used
    public static final int MAX_NUMBER_OF_MISSINGS = 2;
    
       
//    // Only used by variables of type 'Categorical'
    private String[] missing = new String[MAX_NUMBER_OF_MISSINGS];
    
//
//    // Only used in case of a recoded variable of type 'Categorical'
//    public boolean recoded;
//    public String currentRecodeFile = "";
//    public String currentRecodeCodeListFile = "";
//
    //public VariableMu originalVariable;

    //default values
    private String name = "";
    private int startingPosition = 0; // Only used if data file type is fixed format
    private int variableLength = 1;
    private int idLevel = 0;
    private int suppressweight = 50;
    private int decimals = 0;
    private String codeListFile = "";
    //    private int truncLevels;
    
    private boolean numeric = false;
    private boolean categorical = true;
    
    private boolean recodable = false;
    private boolean codelist = false;
    private boolean truncable = false;
    
    private boolean weight = false;
    private boolean house_id = false;
    private boolean household = false;
    private boolean other = true;
    private boolean related = false;
    private String relatedVariableName = null;
    private VariableMu relatedVariable = null;
    
    /**
     * Empty constructor
     */
    public VariableMu(){
    }
    
    public VariableMu(String name) {
        this();
        this.name = name;
    }

        public VariableMu(VariableMu variable) {
        this();
        this.categorical = variable.categorical;
        this.codelist = variable.codelist;
        this.codeListFile = variable.codeListFile;
        this.decimals = variable.decimals;
        this.house_id = variable.house_id;
        this.household = variable.household;
        this.idLevel = variable.idLevel;
        this.missing = Arrays.copyOf(variable.missing, variable.missing.length);
        this.name = variable.name;
        this.numeric = variable.numeric;
        this.recodable = variable.recodable;
        this.relatedVariableName = variable.relatedVariableName;
        this.startingPosition = variable.startingPosition;
        this.suppressweight = variable.suppressweight;
        this.truncable = variable.truncable;
        this.variableLength = variable.variableLength;
        this.weight = variable.weight;
    }

    /**
     * Return variable name
     * @return Returns the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * Set name variable
     * @param name name of the variable
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return 
     */
    public boolean isRecodable() {
        return recodable;
    }

    public void setRecodable(boolean recodable) {
        this.recodable = recodable;
    }

    public boolean isCodelist() {
        return codelist;
    }

    public void setCodelist(boolean codelist) {
        this.codelist = codelist;
    }

    public String getCodeListFile() {
        return codeListFile;
    }

    public void setCodeListFile(String codeListFile) {
        this.codeListFile = codeListFile;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(String idLevel) {
        this.idLevel = Integer.parseInt(idLevel);
    }

    public int getSuppressweight() {
        return suppressweight;
    }

    public void setSuppressweight(String suppressweight) {
        this.suppressweight = Integer.parseInt(suppressweight);
    }

    public boolean isTruncable() {
        return truncable;
    }

    public void setTruncable(boolean truncable) {
        this.truncable = truncable;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public boolean isCategorical() {
        return categorical;
    }

    public void setCategorical(boolean categorical) {
        this.categorical = categorical;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = Integer.parseInt(decimals);
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public boolean isHouse_id() {
        return house_id;
    }

    public void setHouse_id(boolean house_id) {
        this.house_id = house_id;
    }

    public boolean isHousehold() {
        return household;
    }

    public void setHousehold(boolean household) {
        this.household = household;
    }

    public void linkRelatedVariable(ArrayList<VariableMu> variables) throws ArgusException {
        if (this.relatedVariableName == null) {
            this.relatedVariable = null;
        } else {
            for (VariableMu var : variables) {
                if (relatedVariableName.equalsIgnoreCase(var.getName())) {
                    this.relatedVariable = var;
                }
            }
            throw new ArgusException("Variable " + this.name + " related to non-specified variable " + this.relatedVariableName);
        }
    }
    
    public void setRelatedVariable(VariableMu relatedVariable){
        this.relatedVariable = relatedVariable;
    }
    
    public VariableMu getRelatedVariable() {
        return this.relatedVariable;
    }

    public void setRelatedVariableName(String relatedVariableName) {
        this.relatedVariableName = relatedVariableName;
    }

    public boolean isRelated() {
        return related;
    }

    public void setRelated(boolean related) {
        this.related = related;
        
            
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(String startingPosition) {
        this.startingPosition = Integer.parseInt(startingPosition);
    }

    public int getVariableLength() {
        return variableLength;
    }

    public void setVariableLength(String variableLength) {
        this.variableLength = Integer.parseInt(variableLength);
    }

    public String getMissing(int index) {
        return missing[index];
    }

    public void setMissing(int index, String value) {
        this.missing[index] = value;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }
    
    public void write(PrintWriter writer, final int dataFileType) {
        writer.print(this.name);
        if (MetadataMu.DATA_FILE_TYPE_FIXED == dataFileType) {
            writer.print(" " + this.startingPosition);
        } 
        writer.print(" " + this.variableLength);
        if (this.categorical) {
            for (String missingValue : this.missing) {
                if (StringUtils.isNotBlank(missingValue)) {
                    writer.print(" " + StrUtils.quote(missingValue));
                }
            }
        }
        writer.println();
        if (this.recodable) {
            writer.println("    <RECODABLE>");
        }
        if (this.numeric) {
            writer.println("    <NUMERIC>");
        }
        if (this.decimals > 0) {
            writer.println("    <DECIMALS> " + this.decimals);
        }
        if (this.weight) {
            writer.println("    <WEIGHT>");
        }
        if (this.house_id) {
            writer.println("    <HOUSE_ID>");
        }
        if (this.truncable)
            writer.println("    <TRUNCABLE>");
        writer.println("    <IDLEVEL> " + this.idLevel);
        writer.println("    <SUPPRESSWEIGHT> " + this.suppressweight);
        if (this.codelist)
            writer.println("    <CODELIST> " + StrUtils.quote(this.codeListFile));
        if (this.household) {
            writer.println("    <HOUSEHOLD>");
        }
        if (this.relatedVariable != null) { 
            writer.println("    <RELATED> "  + StrUtils.quote(this.relatedVariable.getName()));
        }
    }
    
    
        @Override
    public boolean equals(Object o) {
        VariableMu cmp = (VariableMu)o;
        return (this.categorical == cmp.categorical)
                && (this.codelist == cmp.codelist)
                && (this.codeListFile == null ? cmp.codeListFile == null : this.codeListFile.equals(cmp.codeListFile))
                && (this.decimals == cmp.decimals)
                && (this.house_id == cmp.house_id)
                && (this.household == cmp.household)
                && (this.idLevel == cmp.idLevel)
                && (Arrays.equals(this.missing, cmp.missing))
                && (this.name == null ? cmp.name == null : this.name.equals(cmp.name))
                && (this.numeric == cmp.numeric)
                && (this.recodable == cmp.recodable)
                && (this.relatedVariableName == cmp.relatedVariableName)
                && (this.startingPosition == cmp.startingPosition)
                && (this.suppressweight == cmp.suppressweight)
                && (this.truncable == cmp.truncable)
                && (this.variableLength == cmp.variableLength)
                && (this.weight == cmp.weight)
        ;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.categorical);
        hash = 41 * hash + Objects.hashCode(this.codelist);
        hash = 41 * hash + Objects.hashCode(this.codeListFile);
        hash = 41 * hash + this.decimals;
        hash = 41 * hash + Objects.hashCode(this.house_id);
        hash = 41 * hash + Objects.hashCode(this.household);
        hash = 41 * hash + this.idLevel;
        hash = 41 * hash + Objects.hashCode(this.missing);
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.numeric);
        hash = 41 * hash + Objects.hashCode(this.recodable);
        hash = 41 * hash + Objects.hashCode(this.relatedVariableName);
        hash = 41 * hash + this.startingPosition;
        hash = 41 * hash + Objects.hashCode(this.suppressweight);
        hash = 41 * hash + Objects.hashCode(this.truncable);
        hash = 41 * hash + this.variableLength;
        hash = 41 * hash + Objects.hashCode(this.weight);
        
        return hash;
    }
    
    
    
}
