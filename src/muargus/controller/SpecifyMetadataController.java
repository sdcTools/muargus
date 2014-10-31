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
 * Controller class of the SpecifyMetadata screen.
 *
 * @author Statistics Netherlands
 */
public class SpecifyMetadataController extends ControllerBase<MetadataMu> {

    private final MetadataMu metadataClone;

    //private static final Logger logger = Logger.getLogger(SpecifyMetadataController.class.getName());
    /**
     * Constructor for the SpecifyMetadataController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SpecifyMetadataController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SpecifyMetadataView(parentView, true, this));
        setModel(metadata);
        this.metadataClone = new MetadataMu(metadata);
        getView().setMetadata(this.metadataClone);
    }

    /**
     * Generates metadata for the free with metadata file-type.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @param defaultFieldLength Integer containing the default lenght of a
     * field.
     * @param defaultMissing String containing the default missing value.
     */
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
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Checks whether tables are specified.
     *
     * @return Boolean indicating whether tables are specified.
     */
    private boolean areTablesSpecified() {
        Combinations combinations = getModel().getCombinations();
        return (combinations != null && combinations.getTables().size() > 0);
    }

    /**
     * Actions performed when the Ok button is pressed. If changes have been
     * made the new metadata will be verified, it will be checked whether the
     * changes are significant and the user is asked if he/she wants to safe the
     * new metadata..
     */
    public void ok() {
        // verify the metadata if changes have been made.
        if (!getModel().equals(this.metadataClone)) {
            try {
                this.metadataClone.verify();
            } catch (ArgusException ex) {
                getView().showErrorMessage(ex);
                return;
            }

            /* check if the changes are significant. If they are significant 
             ask the user if he/she wishes to continue. */
            String message;
            boolean significantDifference = areTablesSpecified() && getModel().significantDifference(this.metadataClone);
            if (significantDifference) {
                message = "";
                if (!getView().showConfirmDialog("Changing the Metadata will result in losing already specified tables.\n"
                        + "Do you wish to continue?")) {
                    return;
                }
            } else {
                this.metadataClone.setCombinations(getModel().getCombinations());
                message = "Metadata has been changed. ";
            }

            // set the metadata and ask if the user want to save the metadata.
            setModel(this.metadataClone);
            if (getView().showConfirmDialog(message + "Save changes to file?")) {
                String filePath = getView().showFileDialog("Save ARGUS metadata", true, new String[]{"ARGUS metadata file (*.rda)|rda"});
                if (filePath != null) {
                    try {
                        MetaWriter.writeRda(filePath, getModel(), true);
                    } catch (ArgusException ex) {
                        getView().showErrorMessage(ex);
                    }
                }
            }
        }
        this.getView().setVisible(false);
    }

    /**
     * Gets the metadata.
     *
     * @return MetadataMu containing the metadata.
     */
    public MetadataMu getMetadata() {
        return getModel();
    }

    /**
     * Gets the tempory metadata.
     *
     * @return MetadataMu instance containing the temporary metadata.
     */
    public MetadataMu getMetadataClone() {
        return metadataClone;
    }

    /**
     * Cancels all changes made and closes the specifyMetadata screen after user
     * confirmation.
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
