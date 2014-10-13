// TODO: Combine buttonlist methods
package muargus.controller;

import argus.model.ArgusException;
import java.util.logging.Logger;
import muargus.io.MetaReader;
import muargus.io.MetaWriter;
import muargus.model.Combinations;
import muargus.model.MetadataMu;
import muargus.model.VariableMu;
import muargus.view.SpecifyMetadataView;

/**
 *
 * @author ambargus
 */
public class SpecifyMetadataController extends ControllerBase<MetadataMu> {

    private final MetadataMu metadataClone;

    private static final Logger logger = Logger.getLogger(SpecifyMetadataController.class.getName());

    /**
     *
     * @param parentView
     * @param metadata
     */
    public SpecifyMetadataController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SpecifyMetadataView(parentView, true, this));
        setModel(metadata);
        this.metadataClone = new MetadataMu(metadata);
        getView().setMetadata(this.metadataClone);
    }

    /**
     * Generates meta from the Spss data file
     * 
     */
    public void generateSpss() {
            //TODO: spss
    }
    
    public void generateFromHeader(MetadataMu metadata, int defaultFieldLength, String defaultMissing) {
        try {
            String[] fieldnames = MetaReader.readHeader(metadata.getFileNames().getDataFileName(), 
                    metadata.getSeparator());
            metadata.getVariables().clear();
            for (String fieldname : fieldnames) {
                VariableMu variable = new VariableMu(fieldname);
                variable.setVariableLength(defaultFieldLength);
                if (defaultMissing.length() > 0) {
                    variable.setMissing(0, defaultMissing);
                }
                metadata.getVariables().add(variable);
            }
        }
        catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }
    
    private boolean areTablesSpecified() {
        Combinations combinations = getModel().getCombinations();
        return (combinations != null && combinations.getTables().size() > 0);
    }
    /**
     *
     */
    public void ok() {
        if (!getModel().equals(this.metadataClone)) {
            try {
                this.metadataClone.verify();
            } catch (ArgusException ex) {
                getView().showErrorMessage(ex);
                return;
            }

            String message;
            boolean significantDifference = areTablesSpecified() && getModel().significantDifference(this.metadataClone);
            if (significantDifference)
            {
                message = "";
                if (!getView().showConfirmDialog("Changing the Metadata will result in losing already specified tables.\n"
                        + "Do you wish to continue?")) {
                    return;
                }
                //this.metadata.setCombinations(null);
                //Not necessary, since the clone doesnt contain the combinations
            } else {
                this.metadataClone.setCombinations(getModel().getCombinations());
                message = "Metadata has been changed. ";
            }

            setModel(this.metadataClone);
            if (getView().showConfirmDialog(message + "Save changes to file?")) {
                String filePath = getView().showFileDialog("Save ARGUS metadata", true, new String[] {"ARGUS metadata file (*.rda)|rda"});
                if (filePath != null) {
                    try {
                        MetaWriter.writeRda(filePath, getModel(), true);
                    }
                    catch (ArgusException ex) {
                        getView().showErrorMessage(ex);
                    }
                }

//                JFileChooser fileChooser = new JFileChooser();
//                String hs = SystemUtils.getRegString("general", "datadir", "");
//                if (!hs.equals("")){
//                    File file = new File(hs); 
//                    fileChooser.setCurrentDirectory(file);
//                }
//                if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
//                    try {
//                        this.metadata.write(fileChooser.getSelectedFile(), true);
//                    }
//                    catch (ArgusException ex) {
//                        JOptionPane.showMessageDialog(null, ex.getMessage());
//                    }
//                }
            }
        }
        this.getView().setVisible(false);
    }

    public MetadataMu getMetadata() {
        return getModel();
    }

    /**
     *
     */
    public void cancel() {
        if (!getMetadata().equals(this.metadataClone)) {
            if (!getView().showConfirmDialog("All changes will be discarded. Are you sure?")) {
                return;
            }
        }
        getView().setVisible(false);
    }

}
