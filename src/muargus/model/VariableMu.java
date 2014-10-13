package muargus.model;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * Model class containing the variable specifications. An instance for each
 * selected variable will exist.
 *
 * @author Statistics Netherlands
 */
public class VariableMu {

    // Determines lengths of fixed sized arrays being used
    public static final int MAX_NUMBER_OF_MISSINGS = 2;

    // Only used by variables of type 'Categorical'
    private String[] missing;

    //default values
    private String name;
    private int startingPosition; // Only used if data file type is fixed format
    private int variableLength;
    private int idLevel;
    private int suppressweight;
    private int decimals;
    private String codeListFile;
    //    private int truncLevels;

    private boolean numeric;
    private boolean categorical;

    private boolean codelist;
    private boolean truncable;

    private boolean weight;
    private boolean house_id;
    private boolean household;
    private String relatedVariableName;
    private VariableMu relatedVariable;

    private int nOfCodes;
    //Only relevant for variables in safe file:
    //(not included in copy constructor, equals and hashcode)
    private int nOfSuppressions;
    private int bandwidth;
    private double entropy;

    private final ArrayList<CodeInfo> codeInfos;

    /**
     * Constructor of the model class VariableMu. Makes an empty arraylists for
     * the codeInfos, initializes the array of missings and sets the default
     * values.
     */
    public VariableMu() {
        this.codeInfos = new ArrayList<>();
        this.missing = new String[MAX_NUMBER_OF_MISSINGS];
        this.name = "";
        this.startingPosition = 0;
        this.variableLength = 1;
        this.idLevel = 0;
        this.suppressweight = 50;
        this.decimals = 0;
        this.codeListFile = "";
        this.numeric = false;
        this.categorical = true;
        this.codelist = false;
        this.truncable = false;
        this.weight = false;
        this.house_id = false;
        this.household = false;
        this.relatedVariableName = null;
        this.relatedVariable = null;
    }

    /**
     * 
     * @param name 
     */
    public VariableMu(String name) {
        this();
        this.name = name;
        Arrays.fill(missing, "");
    }

    /**
     * 
     * @param variable 
     */
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
        this.relatedVariableName = variable.relatedVariable == null
                ? null : variable.relatedVariable.getName();
        this.startingPosition = variable.startingPosition;
        this.suppressweight = variable.suppressweight;
        this.truncable = variable.truncable;
        this.variableLength = variable.variableLength;
        this.weight = variable.weight;
    }

    /**
     * Return variable name
     *
     * @return Returns the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * Set name variable
     *
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
        return categorical;
    }

    /**
     * 
     */
    public void clearCodeInfos() {
        this.codeInfos.clear();
    }

    /**
     * 
     * @param codeInfo 
     */
    public void addCodeInfo(CodeInfo codeInfo) {
        this.codeInfos.add(codeInfo);
    }

    /**
     * 
     * @param recodable 
     */
    public void setRecodable(boolean recodable) {
        this.categorical = recodable;
    }

    /**
     * 
     * @return 
     */
    public boolean isCodelist() {
        return codelist;
    }

    /**
     * 
     * @param codelist 
     */
    public void setCodelist(boolean codelist) {
        this.codelist = codelist;
    }

    /**
     * 
     * @return 
     */
    public String getCodeListFile() {
        return codeListFile;
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
    public int getIdLevel() {
        return idLevel;
    }

    /**
     * 
     * @param idLevel 
     */
    public void setIdLevel(String idLevel) {
        this.idLevel = Integer.parseInt(idLevel);
    }

    /**
     * 
     * @return 
     */
    public int getSuppressweight() {
        return suppressweight;
    }

    /**
     * 
     * @param suppressweight 
     */
    public void setSuppressweight(String suppressweight) {
        this.suppressweight = Integer.parseInt(suppressweight);
    }

    /**
     * 
     * @param suppressweight 
     */
    public void setSuppressweight(int suppressweight) {
        this.suppressweight = suppressweight;
    }

    /**
     * 
     * @return 
     */
    public boolean isTruncable() {
        return truncable;
    }

    /**
     * 
     * @param truncable 
     */
    public void setTruncable(boolean truncable) {
        this.truncable = truncable;
    }

    /**
     * 
     * @return 
     */
    public boolean isNumeric() {
        return numeric;
    }

    /**
     * 
     * @param numeric 
     */
    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    /**
     * 
     * @return 
     */
    public boolean isCategorical() {
        return categorical;
    }

    /**
     * 
     * @param categorical 
     */
    public void setCategorical(boolean categorical) {
        this.categorical = categorical;
    }

    /**
     * 
     * @return 
     */
    public int getDecimals() {
        return decimals;
    }

    /**
     * 
     * @param decimals 
     */
    public void setDecimals(String decimals) {
        this.decimals = Integer.parseInt(decimals);
    }

    /**
     * 
     * @return 
     */
    public boolean isWeight() {
        return weight;
    }

    /**
     * 
     * @param weight 
     */
    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    /**
     * 
     * @return 
     */
    public boolean isHouse_id() {
        return house_id;
    }
/**
 * 
 * @param house_id 
 */
    public void setHouse_id(boolean house_id) {
        this.house_id = house_id;
    }

    /**
     * 
     * @return 
     */
    public boolean isHousehold() {
        return household;
    }

    /**
     * 
     * @param household 
     */
    public void setHousehold(boolean household) {
        this.household = household;
    }

    /**
     * 
     * @param variables
     * @throws ArgusException 
     */
    public void linkRelatedVariable(ArrayList<VariableMu> variables) throws ArgusException {
        if (this.relatedVariableName == null) {
            this.relatedVariable = null;
        } else {
            for (VariableMu var : variables) {
                if (relatedVariableName.equalsIgnoreCase(var.getName())) {
                    this.relatedVariable = var;
                    return;
                }
            }
            throw new ArgusException("Variable " + this.name + " related to non-specified variable " + this.relatedVariableName);
        }
    }

    /**
     * 
     * @param relatedVariable 
     */
    public void setRelatedVariable(VariableMu relatedVariable) {
        this.relatedVariable = relatedVariable;
    }

    /**
     * 
     * @return 
     */
    public VariableMu getRelatedVariable() {
        return this.relatedVariable;
    }

    /**
     * 
     * @param relatedVariableName 
     */
    public void setRelatedVariableName(String relatedVariableName) {
        this.relatedVariableName = relatedVariableName;
    }

    /**
     * 
     * @return 
     */
    public boolean isRelated() {
        return (this.relatedVariable != null);
    }

    /**
     * 
     * @return 
     */
    public int getStartingPosition() {
        return startingPosition;
    }

    /**
     * 
     * @param startingPosition 
     */
    public void setStartingPosition(String startingPosition) {
        this.startingPosition = Integer.parseInt(startingPosition);
    }

    /**
     * 
     * @return 
     */
    public int getVariableLength() {
        return variableLength;
    }

    /**
     * 
     * @param variableLength 
     */
    public void setVariableLength(int variableLength) {
        this.variableLength = variableLength;
    }

    /**
     * 
     * @return 
     */
    public int getNumberOfMissings() {
        int numberOfMissings = 0;
        for (int i = 0; i < MAX_NUMBER_OF_MISSINGS; i++) {
            if (!missing[i].isEmpty()) {
                numberOfMissings++;
            }
        }
        return numberOfMissings;
    }

    /**
     * 
     * @param index
     * @return 
     */
    public String getMissing(int index) {
        return missing[index];
    }

    /**
     * 
     * @param index
     * @param value 
     */
    public void setMissing(int index, String value) {
        this.missing[index] = value;
    }

    /**
     * 
     * @return 
     */
    public boolean isOther() {
        return !this.house_id && !this.household && !this.weight;
    }

    /**
     * 
     * @param other 
     */
    public void setOther(boolean other) {
        if (other) {
            this.house_id = false;
            this.household = false;
            this.weight = false;
        }
    }

    /**
     * 
     * @return 
     */
    public int getnOfSuppressions() {
        return nOfSuppressions;
    }

    /**
     * 
     * @param nOfSuppressions 
     */
    public void setnOfSuppressions(int nOfSuppressions) {
        this.nOfSuppressions = nOfSuppressions;
    }

    /**
     * 
     * @return 
     */
    public int getBandwidth() {
        return bandwidth;
    }

    /**
     * 
     * @param bandwidth 
     */
    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    /**
     * 
     * @return 
     */
    public int getnOfCodes() {
        return nOfCodes;
    }

    /**
     * 
     * @param nOfCodes 
     */
    public void setnOfCodes(int nOfCodes) {
        this.nOfCodes = nOfCodes;
    }

    /**
     * 
     * @return 
     */
    public double getEntropy() {
        return entropy;
    }

    /**
     * 
     * @param entropy 
     */
    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    /**
     * 
     * @return 
     */
    public ArrayList<CodeInfo> getCodeInfos() {
        return this.codeInfos;
    }

    public static String printVariableNames(ArrayList<VariableMu> list) {
        StringBuilder b = new StringBuilder(list.get(0).getName());
        for (int i=1; i < list.size(); i++) {
            b.append(", ");
            b.append(list.get(i).getName());
        }
        return b.toString();
    }

    
    /**
     * 
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        VariableMu cmp = (VariableMu) o;
        if (cmp == null) {
            return false;
        }

        String cmpRelatedVariableName = cmp.relatedVariable == null
                ? null : cmp.relatedVariable.getName();
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
                && (this.relatedVariable == null ? cmp.relatedVariable == null
                : this.relatedVariable.getName().equals(cmpRelatedVariableName))
                && (this.startingPosition == cmp.startingPosition)
                && (this.suppressweight == cmp.suppressweight)
                && (this.truncable == cmp.truncable)
                && (this.variableLength == cmp.variableLength)
                && (this.weight == cmp.weight);
    }

    /**
     * 
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        String relatedVariableName = this.relatedVariable == null
                ? null : this.relatedVariable.getName();
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
        hash = 41 * hash + Objects.hashCode(relatedVariableName);
        hash = 41 * hash + this.startingPosition;
        hash = 41 * hash + Objects.hashCode(this.suppressweight);
        hash = 41 * hash + Objects.hashCode(this.truncable);
        hash = 41 * hash + this.variableLength;
        hash = 41 * hash + Objects.hashCode(this.weight);

        return hash;
    }

}
