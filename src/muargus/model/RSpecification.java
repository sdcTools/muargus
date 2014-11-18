package muargus.model;

/**
 *
 * @author Statistics Netherlands
 */
public class RSpecification {
    
    private String rScriptPath;
    private String rScript;

    public RSpecification() {
    }

    public String getRScriptPath() {
        return rScriptPath;
    }

    public void setRScriptPath(String rScriptPath) {
        this.rScriptPath = rScriptPath;
    }

    public String getrScript() {
        return rScript;
    }

    public void setrScript(String rScript) {
        this.rScript = rScript;
    }

}
