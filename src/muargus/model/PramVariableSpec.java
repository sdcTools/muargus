package muargus.model;

/**
 *
 * @author pibd05
 */
public class PramVariableSpec {

    private boolean useBandwidth = false;
    private int bandwidth;
    private boolean applied = false;
    private String appliedText;
    private final VariableMu variable;

    /**
     * 
     * @param variable 
     */
    public PramVariableSpec(VariableMu variable) {
        this.variable = variable;
    }

    /**
     * 
     * @return 
     */
    public boolean useBandwidth() {
        return this.useBandwidth;
    }

    /**
     * 
     * @param useBandwidth 
     */
    public void setUseBandwidth(boolean useBandwidth) {
        this.useBandwidth = useBandwidth;
    }
    
    /**
     * 
     * @return 
     */
    public int getBandwidth() {
        return this.bandwidth;
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
    public boolean isApplied() {
        return this.applied;
    }

    /**
     * 
     * @param applied 
     */
    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    /**
     * 
     * @return 
     */
    public VariableMu getVariable() {
        return this.variable;
    }

    /**
     * 
     * @return 
     */
    public String getAppliedText() {
        if (isApplied()) {
            this.appliedText = "X";
        } else {
            this.appliedText = "";
        }
        return appliedText;
    }
    
    /**
     * 
     * @return 
     */
    public String getBandwidthText(){
        if(isApplied() && useBandwidth()){
            return Integer.toString(this.bandwidth);
        } else {
            return ""; 
        }
    }

}
