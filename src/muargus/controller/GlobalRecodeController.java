package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import muargus.model.GlobalRecode;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.view.GlobalRecodeView;

/**
 * Controller class of the GlobalRecode screen.
 *
 * @author Statistics Netherlands
 */
public class GlobalRecodeController extends ControllerBase<GlobalRecode> {

    /**
     * Constructor for the GlobalRecodeController. This constructor makes a new
     * view and sets the metadata for the view.
     *
     * @param parentView the Frame of the mainFrame that is given to the
     * ShowTableCollectionView.
     * @param metadata the orginal metadata.
     */
    public GlobalRecodeController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new GlobalRecodeView(parentView, true, this));
        getView().setMetadata(metadata);
    }

    /**
     * Shows the view and sets the selected variable in the GlobalRecodeView
     * equal to the selected variable in the main frame.
     *
     * @param selectedRowIndex Integer containing the index of the selected
     * variable in the main frame.
     */
    public void showView(int selectedRowIndex) {
        getGlobalRecodeView().setSelectedIndex(selectedRowIndex);
        getView().setVisible(true);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        getView().setVisible(false);
    }

    /**
     * Gets the index of the selected variable.
     *
     * @return Integer containing the index of the selected variable.
     */
    public int getSelectedVariableIndex() {
        return getGlobalRecodeView().getSelectedVariableIndex();
    }

    /**
     * Gets the GlobalRecodeView.
     *
     * @return GlobalRecodeView.
     */
    private GlobalRecodeView getGlobalRecodeView() {
        return (GlobalRecodeView) getView();
    }

    /**
     * Apply's the recoding on the selected variable.
     *
     * @param recode RecodeMu containing the information on the variable for
     * which the recoding will be done.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during recoding.
     */
    public void apply(RecodeMu recode) throws ArgusException {
        String warning = getCalculationService().doRecode(recode);
        getGlobalRecodeView().showWarning(warning);
        getCalculationService().applyRecode();
        recode.setTruncated(false);
        recode.setRecoded(true);
        recode.setAppliedCodeListFile(recode.getCodeListFile());
        SystemUtils.writeLogbook("Recoding has been done.");
    }

    /**
     * Undo's the recoding of the selected variable.
     *
     * @param recode RecodeMu containing the information on the variable for
     * which the recoding needs to be undone.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while undoing the recoding.
     */
    public void undo(RecodeMu recode) throws ArgusException {
        getCalculationService().undoRecode(recode);
        recode.setRecoded(false);
        recode.setTruncated(false);
        recode.setAppliedCodeListFile("");
        SystemUtils.writeLogbook("Recode/truncation has been undone.");
    }

    /**
     * Truncates the selected variable.
     *
     * @param recode RecodeMu containing the information on the variable that
     * will be truncated.
     * @param positions Integer containing the number of positions that will be
     * truncated.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during truncating.
     */
    public void truncate(RecodeMu recode, int positions) throws ArgusException {
        getCalculationService().truncate(recode, positions);
        recode.setRecoded(false);
        recode.setTruncated(true);
        recode.setPositionsTruncated(positions);
        recode.setAppliedCodeListFile(recode.getCodeListFile());
        SystemUtils.writeLogbook("Truncation has been done.");
    }

}
