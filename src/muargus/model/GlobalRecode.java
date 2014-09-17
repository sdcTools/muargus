package muargus.model;

import java.util.ArrayList;

/**
 * Model class of the GlobalRecode screen. Only a single instance of this class
 * will exist.
 *
 * @author Statistics Netherlands
 */
public class GlobalRecode {

    private final ArrayList<VariableMu> variables;
    private final ArrayList<RecodeMu> recodeMus;
    private final String[] columnNames;

    /**
     * Constructor of the model class GlobalRecode. Initializes the column names
     * and makes empty arraylists for the variables and the RecodeMu's.
     */
    public GlobalRecode() {
        this.variables = new ArrayList<>();
        this.recodeMus = new ArrayList<>();
        this.columnNames = new String[]{"R", "Variables"};
    }

    /**
     * Gets the ArrayList containing all the variables as being specified in the
     * selectCombinations screen.
     *
     * @return ArrayList containing all the variables as being specified in the
     * selectCombinations screen.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * Add a new instance of RecodeMu.
     *
     * @param recodeMu RecodeMu containing all the information necessary for
     * global recoding.
     */
    public void addRecodeMu(RecodeMu recodeMu) {
        this.recodeMus.add(recodeMu);
    }

    /**
     * Clears the ArrayList containing all the RecodeMu's.
     */
    public void clearRecodeMus() {
        this.recodeMus.clear();
    }

    /**
     * Gets the ArrayList containing all the RecodeMu's.
     *
     * @return ArrayList containing all the RecodeMu's.
     */
    public ArrayList<RecodeMu> getRecodeMus() {
        return recodeMus;
    }

    /**
     * Gets an Array containing the column names.
     *
     * @return Array of Strings containing the column names.
     */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
     * Gets the instance of RecodeMu using the variable name.
     *
     * @param varName String containing the name of the variable.
     * @return RecodeMu containing the global recode information of the
     * requested variable.
     */
    public RecodeMu getRecodeByVariableName(String varName) {
        for (RecodeMu recode : this.recodeMus) {
            if (recode.getVariable().getName().equals(varName)) {
                return recode;
            }
        }
        return null;
    }

}
