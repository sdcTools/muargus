/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author Statistics Netherlands
 */
public class RSpecification {
    
    private String rScriptPath;
    private final ArrayList<String> rScript;

    public RSpecification() {
        this.rScript = new ArrayList<>();
    }

    public String getRScriptPath() {
        return rScriptPath;
    }

    public void setRScriptPath(String rScriptPath) {
        this.rScriptPath = rScriptPath;
    }

    public ArrayList<String> getrScript() {
        return rScript;
    }
    
}
