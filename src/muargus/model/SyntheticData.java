package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public class SyntheticData {
    
    private final ArrayList<VariableMu> sensitiveVariables;
    private final ArrayList<VariableMu> nonSensitiveVariables;

    public SyntheticData() {
        this.sensitiveVariables = new ArrayList<>();
        this.nonSensitiveVariables = new ArrayList<>();
    }

    public ArrayList<VariableMu> getSensitiveVariables() {
        return sensitiveVariables;
    }

    public ArrayList<VariableMu> getNonSensitiveVariables() {
        return nonSensitiveVariables;
    }
    
    
}
