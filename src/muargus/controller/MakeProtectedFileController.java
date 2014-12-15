package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.io.File;
import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.io.MetaWriter;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
import muargus.model.MicroaggregationSpec;
import muargus.model.ProtectedFile;
import muargus.model.RankSwappingSpec;
import muargus.model.ReplacementSpec;
import muargus.model.SyntheticDataSpec;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.view.MakeProtectedFileView;

/**
 * Controller class of the MakeProtectedFile screen.
 *
 * @author Statistics Netherlands
 */
public class MakeProtectedFileController extends ControllerBase<ProtectedFile> {

    private final MetadataMu metadata;
    private boolean fileCreated;

    /**
     * Constructor for the MakeProtectedFileController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public MakeProtectedFileController(java.awt.Frame parentView, MetadataMu metadata) {
        this.setView(new MakeProtectedFileView(parentView, true, this));
        this.metadata = metadata;
        this.fileCreated = false;

        getView().setMetadata(this.metadata);
    }

    public MetadataMu getMetadata() {
        return this.metadata;
    }

    /**
     * Makes the protected file.
     *
     * @param file File instance containing the new .saf file. To this file, the
     * safe data will be written.
     */
    public void makeFile(File file) {
        if (!isRiskThresholdSpecified()) {
            return;
        }
        this.metadata.getCombinations().getProtectedFile().initSafeMeta(file, this.metadata);
        removeRedundentReplacementSpecs();
        if (this.metadata.isSpss()) {
            MuARGUS.getSpssUtils().safFile = file;
            String path = this.metadata.getCombinations().getProtectedFile().getSafeMeta().getFileNames().getDataFileName();
            String safeSpssFile = path.substring(0, path.lastIndexOf(".")) + ".sav";
            MuARGUS.getSpssUtils().safeSpssFile = new File(safeSpssFile);

        }
        getCalculationService().makeProtectedFile(this);
        SystemUtils.writeLogbook("Protected file has been written.");
    }

    /**
     * Returns whether the risk threshold is specified.
     *
     * @return Boolean indicating whether the risk threshold is specified.
     */
    private boolean isRiskThresholdSpecified() {
        Combinations comb = this.metadata.getCombinations();
        for (TableMu table : comb.getTables()) {
            if (table.isRiskModel()) {
                if (!comb.getRiskSpecifications().containsKey(table)
                        || comb.getRiskSpecifications().get(table).getRiskThreshold() == 0) {
                    if (!notSpecifiedIsOk(table)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns whether it is ok to continue even though the risk threshold was
     * not specified.
     *
     * @param table TableMu instance for which the risk model was specified.
     * @return Boolean indicating whether it is ok to continue even though the
     * risk threshold was not specified.
     */
    private boolean notSpecifiedIsOk(TableMu table) {
        String message = String.format("Table %s was specified in the Risk Model, but no Risk threshold was specified.\nContinue anyway?",
                table.getTableTitle());
        return (getView().showConfirmDialog(message));
    }

    /**
     * Removes the redundent ReplacementSpecs. It check if there are replacement
     * specs with the same variables. If this is the case, the replacement specs
     * are removed.
     */
    private void removeRedundentReplacementSpecs() {
        ArrayList<ReplacementSpec> toRemove = new ArrayList<>();
        int index = 0;
        for (ReplacementSpec replacement : this.metadata.getReplacementSpecs()) {
            ArrayList<VariableMu> variablesFound = new ArrayList<>();
            for (int index2 = index + 1; index2 < this.metadata.getReplacementSpecs().size(); index2++) {
                ReplacementSpec replacement2 = this.metadata.getReplacementSpecs().get(index2);
                for (VariableMu variable : replacement2.getOutputVariables()) {
                    if (replacement.getOutputVariables().contains(variable) && !variablesFound.contains(variable)) {
                        variablesFound.add(variable);
                    }
                }
            }
            if (variablesFound.size() == replacement.getOutputVariables().size()) {
                toRemove.add(replacement);
            }
            index++;
        }
        for (ReplacementSpec replacement : toRemove) {
            if (replacement instanceof RankSwappingSpec) {
                this.metadata.getCombinations().getNumericalRankSwapping().getRankSwappings().remove((RankSwappingSpec) replacement);
            } else if (replacement instanceof MicroaggregationSpec) {
                MicroaggregationSpec spec = (MicroaggregationSpec) replacement;
                this.metadata.getCombinations().getMicroaggregation().getMicroaggregations().remove(spec);
            } else if (replacement instanceof SyntheticDataSpec) {
                //No action needed, SyntheticDataSpec's are not stored
            }
        }
    }

    /**
     * Saves the safe metadata.
     */
    private void saveSafeMeta() {
        getCalculationService().fillSafeFileMetadata();
        MetadataMu safeMetadata = this.metadata.getCombinations().getProtectedFile().getSafeMeta();

        try {
            if (this.metadata.isSpss()) {
                MuARGUS.getSpssUtils().makeSafeFileSpss(safeMetadata);
            }
            MetaWriter.writeRda(safeMetadata.getFileNames().getMetaFileName(),
                    safeMetadata, false);
            this.fileCreated = true;
        } catch (ArgusException ex) {
            this.getView().showErrorMessage(ex);
        }
        SystemUtils.writeLogbook("Safe meta file has been written.");
    }

    /**
     * Gets the combinations model.
     *
     * @return Combinations model.
     */
    public Combinations getCombinations() {
        return this.metadata.getCombinations();
    }

    /**
     * Returns whether the save file is created.
     *
     * @return Boolean indicating whether the save file is created.
     */
    public boolean isFileCreated() {
        return this.fileCreated;
    }

    /**
     * Does the next step if the previous step was succesful.
     *
     * @param success Boolean indicating whether the previous step was
     * succesful.
     */
    @Override
    protected void doNextStep(boolean success) {
        if (success) {
            saveSafeMeta();
            if (this.fileCreated) {
                this.getView().setVisible(false);
            }
        }
    }

}
