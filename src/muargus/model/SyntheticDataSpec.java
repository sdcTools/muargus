/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public class SyntheticDataSpec extends ReplacementSpec {
    
    private final ArrayList<VariableMu> variables;
    private ReplacementFile replacementFile;

    /**
     * Constructor of the model class SyntheticDataSpec. Makes an empty arraylists
     * for the variables.
     */
    public SyntheticDataSpec() {
        this.variables = new ArrayList<>();
    }

    /**
     * Gets an ArrayList containing the variables. If the ArrayList is empty,
     * use this method to add variables.
     *
     * @return ArrayList containing the variables.
     */
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    /**
     * Gets the replacementFile.
     *
     * @return ReplacementFile containing the replacement type and the input &
     * output file.
     */
    public ReplacementFile getReplacementFile() {
        return replacementFile;
    }

    /**
     * Sets the replacementFile.
     *
     * @param replacementFile ReplacementFile containing the replacement type
     * and the input & output file.
     */
    public void setReplacementFile(ReplacementFile replacementFile) {
        this.replacementFile = replacementFile;
    }
    
}
