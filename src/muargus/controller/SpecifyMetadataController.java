// TODO: Combine buttonlist methods
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
//import javax.swing.DefaultListModel;
import muargus.model.Combinations;
import muargus.model.MetadataMu;
import muargus.view.SpecifyMetadataView;

/**
 *
 * @author ambargus
 */
public class SpecifyMetadataController {

    SpecifyMetadataView view;
    MetadataMu metadata;
    MetadataMu metadataClone;
    ArrayList<String> list;

    private static final Logger logger = Logger.getLogger(SpecifyMetadataController.class.getName());

    /**
     *
     * @param parentView
     * @param metadata
     * @param controller
     */
    public SpecifyMetadataController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new SpecifyMetadataView(parentView, true, this);
        this.metadata = metadata;
        this.metadataClone = new MetadataMu(metadata);
        this.view.setMetadataMu(this.metadataClone);
    }

    public void showView() {
        this.view.setVisible(true);

    }

    /**
     *
     */
    public void generate() {
        //TODO: Wat moet hier gebeuren?

        //Generate generate; = new Generate(view, true);
        //generate.setVisible(true);
    }

    private boolean areTablesSpecified() {
        Combinations combinations = this.metadata.getCombinations();
        return (combinations != null && combinations.getTables().size() > 0);
    }
    /**
     *
     */
    public void ok() {
        if (!this.metadata.equals(this.metadataClone)) {
            try {
                this.metadataClone.verify();
            } catch (ArgusException ex) {
                view.showErrorMessage(ex);
                return;
            }

            String message = "";
            boolean significantDifference = areTablesSpecified() && this.metadata.significantDifference(this.metadataClone);
            if (significantDifference)
            {
                message = "";
                if (!view.showConfirmDialog("Changing the Metadata will result in losing already specified tables.\n"
                        + "Do you wish to continue?")) {
                    return;
                }
                //this.metadata.setCombinations(null);
                //Not necessary, since the clone doesnt contain the combinations
            } else {
                this.metadataClone.setCombinations(this.metadata.getCombinations());
                message = "Metadata has been changed. ";
            }

            this.metadata = this.metadataClone;
            if (view.showConfirmDialog(message + "Save changes to file?")) {
                String filePath = view.showFileDialog("Save ARGUS metadata", true, new String[] {"ARGUS metadata file (*.rda)|rda"});
                if (filePath != null) {
                    try {
                        this.metadata.write(new File(filePath), true);
                    }
                    catch (ArgusException ex) {
                        view.showErrorMessage(ex);
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
        this.view.setVisible(false);
    }

    public MetadataMu getMetadata() {
        return this.metadata;
    }

    /**
     *
     */
    public void cancel() {
        if (!this.metadata.equals(this.metadataClone)) {
            if (!view.showConfirmDialog("All changes will be discarded. Are you sure?")) {
                return;
            }
        }
        this.view.setVisible(false);
    }

}
