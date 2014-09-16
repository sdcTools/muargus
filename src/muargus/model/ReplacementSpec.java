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
public class ReplacementSpec {
    private final ArrayList<VariableMu> variables;
    private ReplacementFile replacementFile;

    public ReplacementSpec() {
        this.variables = new ArrayList<>();
    }
    
        public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    public ReplacementFile getReplacementFile() {
        return replacementFile;
    }

    public void setReplacementFile(ReplacementFile replacementFile) {
        this.replacementFile = replacementFile;
    }
}
