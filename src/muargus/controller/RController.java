package muargus.controller;

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

}
