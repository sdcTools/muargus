/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

/**
 *
 * @author ambargus
 */
public class Variables implements Cloneable {

    // Determines lengths of fixed sized arrays being used
    public static final int MAX_NUMBER_OF_MISSINGS = 2;
//    // Only used by variables of type 'Categorical'
    public String[] missing = new String[MAX_NUMBER_OF_MISSINGS];
//
//    // Only used in case of a recoded variable of type 'Categorical'
//    public boolean recoded;
//    public String currentRecodeFile = "";
//    public String currentRecodeCodeListFile = "";
//
    public Variables originalVariable;
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
    private boolean related = false; // check how many variables a variable can be related to
    // private Variable relatedVariable = null;

    /**
     * Empty constructor
     */
    public Variables() {
    }

    public Variables(String name) {
        this.name = name;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        Variables variable = (Variables) super.clone();

//        if (requestCode != null) {
//            variable.requestCode = (String[])requestCode.clone();
//        }
//        if (distanceFunction != null) {
//            variable.distanceFunction = (int[])distanceFunction.clone();
//        }
//        if (hierLevels != null) {
//            variable.hierLevels = (int[])hierLevels.clone();
//        }
//        if (missing != null) {
//            variable.missing = (String[])missing.clone();
//        }
        variable.originalVariable = this;
        return variable;
    }
}
