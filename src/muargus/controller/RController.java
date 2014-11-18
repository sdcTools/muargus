package muargus.controller;

import argus.model.ArgusException;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import muargus.model.MetadataMu;
import muargus.model.RSpecification;
import muargus.view.RView;

/**
 *
 * @author Statistics Netherlands
 */
public class RController extends ControllerBase<RSpecification> {

    private final MetadataMu metadata;

    /**
     * Constructor for the RController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public RController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new RView(parentView, true, this));
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getRSpecification());
        getView().setMetadata(this.metadata);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     *
     * @return
     */
    public String getRScript() {
        return null;
    }
    
    /**
     * 
     * @param path
     * @throws ArgusException 
     */
    public void readRScript(String path) throws ArgusException {
        File file = new File(path);
        try {
            StringBuilder sb;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                sb = new StringBuilder();
                String line;
                Tokenizer tokenizer = new Tokenizer(reader);
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
            }
            getModel().setrScript(sb.toString());
        } catch (IOException ex) {
            throw new ArgusException("Error during reading file. Error message: " + ex.getMessage());
        }

    }

}
