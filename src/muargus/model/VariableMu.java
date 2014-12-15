package muargus.model;

import argus.model.SpssVariable;
import argus.model.ArgusException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
    private int suppressionPriority;
    private int decimals;
    private String codeListFile; 

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

    private double alpha; // is used for synthetic data

    private final ArrayList<CodeInfo> codeInfos;

    private SpssVariable spssVariable;

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
        this.suppressionPriority = 50;
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
        this.alpha = 0.9;
    }

    /**
     * Constructor of the model class VariableMu. Does the same as the empty
     * constructor and sets the name and missings.
     *
     * @param name String containing the variable name
     */
    public VariableMu(String name) {
        this();
        this.name = name;
        Arrays.fill(missing, "");
    }

    /**
     * Constructor of the model class VariableMu used to clone the instance
     * given as a parameter.
     *
     * @param variable VariableMu instance used to clone.
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
        this.suppressionPriority = variable.suppressionPriority;
        this.truncable = variable.truncable;
        this.variableLength = variable.variableLength;
        this.weight = variable.weight;
        this.spssVariable = variable.spssVariable;
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
     * Returns whether the variable is recodable.
     *
     * @return Boolean indicating whether the variable is recodable.
     */
    public boolean isRecodable() {
        return categorical;
    }

    /**
     * Sets whether the variable is recodable.
     *
     * @param recodable Boolean indicating whether the variable is recodable.
     */
    public void setRecodable(boolean recodable) {
        this.categorical = recodable;
    }

    /**
     * Returns whether the variable has a codelist.
     *
     * @return Boolean indicating whether the variable has a codelist.
     */
    public boolean isCodelist() {
        return codelist;
    }

    /**
     * Returns whether the variable has a codelist.
     *
     * @param codelist Boolean indicating whether the variable has a codelist.
     */
    public void setCodelist(boolean codelist) {
        this.codelist = codelist;
    }

    /**
     * Gets a String containing path from the codelist file.
     *
     * @return String containing path from the codelist file.
     */
    public String getCodeListFile() {
        return codeListFile;
    }

    /**
     * Sets the codelist file.
     *
     * @param codeListFile String containing the path from the codelist file.
     */
    public void setCodeListFile(String codeListFile) {
        this.codeListFile = codeListFile;
    }

    /**
     * Gets the identification level. The identification level is a measure of
     * how identifying a variable is and can range from 0 to 5. It is an option
     * to easily generate the set of tables to be inspected in the disclosure
     * control process. Zero (0) means that an individual cannot be identified
     * by this variable and it will not play a role in the disclosure control
     * proces. 1: the variable is most identifying. 2: the variable is more
     * identifying. 3: the variable is identifying. 4: the variable is somewhat
     * identifying. 5: the variable is least identifing.
     *
     * @return Integer containing the identification level.
     */
    public int getIdLevel() {
        return idLevel;
    }

    /**
     * Sets the identification level. The identification level is a measure of
     * how identifying a variable is and can range from 0 to 5. It is an option
     * to easily generate the set of tables to be inspected in the disclosure
     * control process. Zero (0) means that an individual cannot be identified
     * by this variable and it will not play a role in the disclosure control
     * proces. 1: the variable is most identifying. 2: the variable is more
     * identifying. 3: the variable is identifying. 4: the variable is somewhat
     * identifying. 5: the variable is least identifing.
     *
     * @param idLevel Integer containing the identification level.
     */
    public void setIdLevel(int idLevel) {
        this.idLevel = idLevel;
    }

    /**
     * Gets the suppression priority.
     *
     * @return Integer containing the suppression priority.
     */
    public int getSuppressPriority() {
        return suppressionPriority;
    }

    /**
     * Sets the suppression priority.
     *
     * @param suppressionPriority Integer containing the suppression priority.
     */
    public void setSuppressionPriority(int suppressionPriority) {
        this.suppressionPriority = suppressionPriority;
    }

    /**
     * Returns whether this variable is truncable.
     *
     * @return Boolean indicating whether this variable is truncable.
     */
    public boolean isTruncable() {
        return truncable;
    }

    /**
     * Sets whether this variable is truncable.
     *
     * @param truncable Boolean indicating whether this variable is truncable.
     */
    public void setTruncable(boolean truncable) {
        this.truncable = truncable;
    }

    /**
     * Returns whether this variable is numeric.
     *
     * @return Boolean indicating whether this variable is numeric.
     */
    public boolean isNumeric() {
        return numeric;
    }

    /**
     * Sets whether this variable is numeric.
     *
     * @param numeric Boolean indicating whether this variable is numeric.
     */
    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    /**
     * Returns whether this variable is categorical.
     *
     * @return Boolean indicating whether this variable is categorical.
     */
    public boolean isCategorical() {
        return categorical;
    }

    /**
     * Sets whether this variable is categorical.
     *
     * @param categorical Boolean indicating whether this variable is
     * categorical.
     */
    public void setCategorical(boolean categorical) {
        this.categorical = categorical;
    }

    /**
     * Gets the number of decimals.
     *
     * @return Integer containing the number of decimals.
     */
    public int getDecimals() {
        return decimals;
    }

    /**
     * Sets the number of decimals.
     *
     * @param decimals Integer containing the number of decimals.
     */
    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    /**
     * Returns whether this variable is a weight variable.
     *
     * @return Boolean indicating whether this variable is a weight variable.
     */
    public boolean isWeight() {
        return weight;
    }

    /**
     * Sets whether this variable is a weight variable.
     *
     * @param weight Boolean indicating whether this variable is a weight
     * variable.
     */
    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    /**
     * Returns whether this variable is a household identifier.
     *
     * @return Boolean indicating whether this variable is a household
     * identifier.
     */
    public boolean isHouse_id() {
        return house_id;
    }

    /**
     * Sets whether this variable is a household identifier.
     *
     * @param house_id Boolean indicating whether this variable is a household
     * identifier.
     */
    public void setHouse_id(boolean house_id) {
        this.house_id = house_id;
    }

    /**
     * Returns whether this variable is a household variable. A household
     * variable is a variable that is equal for the entire household.
     *
     * @return Boolean indicating whether this variable is a household variable.
     */
    public boolean isHousehold() {
        return household;
    }

    /**
     * Sets whether this variable is a household variable. A household variable
     * is a variable that is equal for the entire household.
     *
     * @param household Boolean indicating whether this variable is a household
     * variable.
     */
    public void setHousehold(boolean household) {
        this.household = household;
    }

    /**
     * Links this variable to it's related variable using the name of the
     * related variable.
     *
     * @param variables ArrayList containing all variables in the metadata.
     * @throws ArgusException Throws an ArgusException when the name of the
     * related variable does not equal any variable in the metadata.
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
     * Sets the related variable.
     *
     * @param relatedVariable VariableMu instance of the related variable.
     */
    public void setRelatedVariable(VariableMu relatedVariable) {
        this.relatedVariable = relatedVariable;
    }

    /**
     * Gets the related variable.
     *
     * @return VariableMu instance of the related variable.
     */
    public VariableMu getRelatedVariable() {
        return this.relatedVariable;
    }

    /**
     * Sets the related variable name.
     *
     * @param relatedVariableName String containing the name of the related
     * variable
     */
    public void setRelatedVariableName(String relatedVariableName) {
        this.relatedVariableName = relatedVariableName;
    }

    /**
     * Returns whether this variable is related to another variable.
     *
     * @return Boolean indicating whether this variable is related to another
     * variable.
     */
    public boolean isRelated() {
        return (this.relatedVariable != null);
    }

    /**
     * Gets the starting position. The starting position is the position of
     * variable inside the data file. The starting position is only relevant if
     * the data file has a fixed format.
     *
     * @return Integer containing the starting position.
     */
    public int getStartingPosition() {
        return startingPosition;
    }

    /**
     * Sets the starting position. The starting position is the position of
     * variable inside the data file. The starting position is only relevant if
     * the data file has a fixed format.
     *
     * @param startingPosition Integer containing the starting position.
     */
    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Gets the length of this variable as written in the data file.
     *
     * @return Integer containing the length of this variable.
     */
    public int getVariableLength() {
        return variableLength;
    }

    /**
     * Gets the length of this variable as written in the data file.
     *
     * @param variableLength Integer containing the length of this variable.
     */
    public void setVariableLength(int variableLength) {
        this.variableLength = variableLength;
    }

    /**
     * Gets the number of missing values.
     *
     * @return Integer containing the number of missing values.
     */
    public int getNumberOfMissings() {
        int numberOfMissings = 0;
        for (int i = 0; i < MAX_NUMBER_OF_MISSINGS; i++) {
            if (!this.missing[i].isEmpty()) {
                numberOfMissings++;
            }
        }
        return numberOfMissings;
    }

    /**
     * Gets a particular missing value.
     *
     * @param index Integer containig the index of the missing value.
     * @return String containing the missing value.
     */
    public String getMissing(int index) {
        return this.missing[index];
    }

    /**
     * Sets a particular missing value.
     *
     * @param index Integer containig the index of the missing value.
     * @param value String containing the missing value.
     */
    public void setMissing(int index, String value) {
        this.missing[index] = value;
    }

    /**
     * Returns whether a variable is not a houshold identifier, household
     * variable or weight variable.
     *
     * @return Boolean indicating whether a variable is not a houshold
     * identifier, household variable or weight variable.
     */
    public boolean isOther() {
        return !this.house_id && !this.household && !this.weight;
    }

    /**
     * Sets whether a variable is not a houshold identifier, household variable
     * or weight variable.
     *
     * @param other Boolean indicating whether a variable is not a houshold
     * identifier, household variable or weight variable.
     */
    public void setOther(boolean other) {
        if (other) {
            this.house_id = false;
            this.household = false;
            this.weight = false;
        }
    }

    /**
     * Gets the number of supressions.
     *
     * @return Integer containing the number of suppressions.
     */
    public int getnOfSuppressions() {
        return nOfSuppressions;
    }

    /**
     * Sets the number of supressions.
     *
     * @param nOfSuppressions Integer containing the number of suppressions.
     */
    public void setnOfSuppressions(int nOfSuppressions) {
        this.nOfSuppressions = nOfSuppressions;
    }

    /**
     * Sets the bandwidth
     *
     * @return Integer containing the bandwidth.
     */
    public int getBandwidth() {
        return bandwidth;
    }

    /**
     * Sets the bandwidth
     *
     * @param bandwidth Integer containing the bandwidth.
     */
    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    /**
     * Gets the number of codes. The number of codes are the number of different
     * values (including missings) that this variable has.
     *
     * @return Integer containing the number of codes.
     */
    public int getnOfCodes() {
        return nOfCodes;
    }

    /**
     * Sets the number of codes. The number of codes are the number of different
     * values (including missings) that this variable has.
     *
     * @param nOfCodes Integer containing the number of codes.
     */
    public void setnOfCodes(int nOfCodes) {
        this.nOfCodes = nOfCodes;
    }

    /**
     * Gets the entropy value or this variable. The entropy value is calculated
     * (in the .dll) through the entropy function. This entropy H(x) is defined
     * as H(x) = -(1/N) * Sum( f(x) * log2* f(x)/N) where f(x) is the frequency
     * of category x of variable X and N the total number of records. The
     * variable with the lowest value of the entropy function will be suppressed
     * when a safe file is made.
     *
     * @return Double containing the entropy value or this variable.
     */
    public double getEntropy() {
        return entropy;
    }

    /**
     * Sets the entropy value or this variable. The entropy value is calculated
     * (in the .dll) through the entropy function. This entropy H(x) is defined
     * as H(x) = -(1/N) * Sum( f(x) * log2* f(x)/N) where f(x) is the frequency
     * of category x of variable X and N the total number of records. The
     * variable with the lowest value of the entropy function will be suppressed
     * when a safe file is made.
     *
     * @param entropy Double containing the entropy value or this variable.
     */
    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    /**
     * Gets the ArryaList containing the instances of CodeInfo. An instance of
     * CodeInfo contains basic info for a single code. Each categorical variable
     * contains an array of CodeInfo instances.
     *
     * @return ArryaList containing the instances of CodeInfo.
     */
    public ArrayList<CodeInfo> getCodeInfos() {
        return this.codeInfos;
    }

    /**
     * Gets the SpssVariable. The SpssVariable contains all metadata available
     * inside the spss data file.
     *
     * @return SpssVariable instance containing all metadata available inside
     * the spss data file.
     */
    public SpssVariable getSpssVariable() {
        return spssVariable;
    }

    /**
     * Sets the SpssVariable. The SpssVariable contains all metadata available
     * inside the spss data file.
     *
     * @param spssVariable SpssVariable instance containing all metadata
     * available inside the spss data file.
     *
     */
    public void setSpssVariable(SpssVariable spssVariable) {
        this.spssVariable = spssVariable;
    }

    /**
     * Gets the alpha value. The alpha value is used for generating synthetic
     * data.
     *
     * @return Double containing the alpha value.
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Sets the alpha value. The alpha value is used for generating synthetic
     * data.
     *
     * @param alpha Double containing the alpha value.
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Appends the given variable(s) together with a comma and a space.
     *
     * @param list ArrayList containing the variable(s) that needs to be
     * appended.
     * @return String containing all variable names separated with a comma and a
     * space.
     */
    public static String printVariableNames(ArrayList<VariableMu> list) {
        StringBuilder b = new StringBuilder(list.get(0).getName());
        for (int i = 1; i < list.size(); i++) {
            b.append(", ");
            b.append(list.get(i).getName());
        }
        return b.toString();
    }

    /**
     * Checks whether an object is equal to this variable.
     *
     * @param o Object instance that will be compaired to this variable.
     * @return Boolean indicating whether the object is equal to this variable.
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
                && (this.suppressionPriority == cmp.suppressionPriority)
                && (this.truncable == cmp.truncable)
                && (this.variableLength == cmp.variableLength)
                && (this.weight == cmp.weight);
    }

    /**
     * Gets the hashcode. The hashcode is calculated as a addition of the
     * hashcodes from the relevant individual components.
     *
     * @return Integer containing the hashcode.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        String cmpRelatedVariableName = this.relatedVariable == null
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
        hash = 41 * hash + Objects.hashCode(cmpRelatedVariableName);
        hash = 41 * hash + this.startingPosition;
        hash = 41 * hash + Objects.hashCode(this.suppressionPriority);
        hash = 41 * hash + Objects.hashCode(this.truncable);
        hash = 41 * hash + this.variableLength;
        hash = 41 * hash + Objects.hashCode(this.weight);

        return hash;
    }

}
