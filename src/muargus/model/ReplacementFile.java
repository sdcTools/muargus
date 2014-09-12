/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import argus.model.ArgusException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public class ReplacementFile {
    private final File file;
    private final ArrayList<VariableMu> variables;
    private final String replacementType;
    
    public ReplacementFile(String replacementType) throws ArgusException {
        this.replacementType = replacementType;
        this.file = createFile();
        this.variables = new ArrayList<>();
    }

    public String getFilePath() {
        return this.file.getPath();
    }

    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    public String getReplacementType() {
        return replacementType;
    }
    
    private File createFile() throws ArgusException {
        try {
            return File.createTempFile("MuArgus", ".rpl");
        }
        catch (IOException ex) {
            throw new ArgusException("Replacement file cannot be created: " + ex.getMessage());
        }
    }
    
    
    
    
    
    
}
