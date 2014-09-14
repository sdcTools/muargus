/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author ambargus
 */
public class NumericalRankSwapping {
    private final ArrayList<VariableMu> variables;
    private ReplacementFile replacementFile;
    private final double percentage;
    
    public NumericalRankSwapping(double percentage) {
        this.variables = new ArrayList<>();
        this.percentage = percentage;
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

    public double getPercentage() {
        return percentage;
    }
    
    
}
