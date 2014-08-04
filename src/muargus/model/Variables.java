/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

/**
 *
 * @author ambargus
 */
public class Variables {
    
    // Determines lengths of fixed sized arrays being used
    public static final int MAX_NUMBER_OF_MISSINGS = 2;
    
    
//    //public Type type;
    private int bPos = 1; // Only used if data file type is fixed format
    private int varLen = 0; 
//    public int nDecimals; // Only used if hasDecimals() returns true
//    
//    // Only used by variables of type 'Categorical'
    public String[] missing = new String[MAX_NUMBER_OF_MISSINGS];
    
//
//    // Only used in case of a recoded variable of type 'Categorical'
//    public boolean recoded;
//    public String currentRecodeFile = "";
//    public String currentRecodeCodeListFile = "";
//
//    public boolean truncatable;
//    public int truncLevels;
  
  
    private String name = "";
    private boolean recodable = false;
    private boolean codelist = false;
    private String codeListFile = "";
    private String idLevenl = "0";
    private String suppressweight = "50";
    private boolean truncable = false;
    private boolean numeric = false;
    private String decimals = "0";
    private boolean weight = false;
    private boolean house_id = false;
    private boolean household = false;
    private boolean related = false;
    // check how many variables a variable can be related to
    
    public Variables(){
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getIdLevenl() {
        return idLevenl;
    }

    public void setIdLevenl(String idLevenl) {
        this.idLevenl = idLevenl;
    }

    public String getSuppressweight() {
        return suppressweight;
    }

    public void setSuppressweight(String suppressweight) {
        this.suppressweight = suppressweight;
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

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
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

    public boolean isRelated() {
        return related;
    }

    public void setRelated(boolean related) {
        this.related = related;
    }

    public int getbPos() {
        return bPos;
    }

    public void setbPos(int bPos) {
        this.bPos = bPos;
    }

    public int getVarLen() {
        return varLen;
    }

    public void setVarLen(int varLen) {
        this.varLen = varLen;
    }

    public String getMissing(int index) {
        return missing[index];
    }

    public void setMissing(int index, String value) {
        this.missing[index] = value;
    }

 
    
}
