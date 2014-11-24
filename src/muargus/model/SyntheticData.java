package muargus.model;

import java.util.ArrayList;
//import jdk.nashorn.internal.ir.VarNode;

/**
 *
 * @author pibd05
 */
public class SyntheticData {
    
    private final ArrayList<VariableMu> variables;
    private final ArrayList<VariableMu> sensitiveVariables;
    private final ArrayList<VariableMu> nonSensitiveVariables;

    public SyntheticData() {
        this.sensitiveVariables = new ArrayList<>();
        this.nonSensitiveVariables = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public ArrayList<VariableMu> getSensitiveVariables() {
        return sensitiveVariables;
    }

    public ArrayList<VariableMu> getNonSensitiveVariables() {
        return nonSensitiveVariables;
    }
    
    public ArrayList<VariableMu> getVariables() {
        return variables;
    }
    
    public ArrayList<VariableMu> getOtherVariables() {
        ArrayList<VariableMu> other = new ArrayList<>();
        for (VariableMu variable : variables) {
            if (!this.sensitiveVariables.contains(variable) && (!this.nonSensitiveVariables.contains(variable))) {
                other.add(variable);
            }
        }
        return other;
    }
}
