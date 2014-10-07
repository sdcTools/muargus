package muargus.controller;

import argus.model.ArgusException;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    //private final MetadataMu metadata;
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
        //this.metadata = metadata;
        getView().setMetadata(metadata);
        //this.selectCombinationsModel = selectCombinationsModel;
        //this.view = view;
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
     * Closes the screen.
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
        return getGlobalRecodeView().getSelectedIndex();
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
     *
     */
//    public void codelistRecode() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("Codelist (*.cdl)", "cdl"));
//        String hs = SystemUtils.getRegString("general", "datadir", "");
//        if (!hs.equals("")){
//            File file = new File(hs); 
//            fileChooser.setCurrentDirectory(file);
//        }        
//        fileChooser.showOpenDialog(null);
//
//        String filename;
//        File f = fileChooser.getSelectedFile();
//        if (fileChooser.getSelectedFile() == null) {
//            filename = "";
//        } else {
//            filename = f.getAbsolutePath();
//        }
//        view.setCodelistText(filename);
//    }
    /**
     * Reads the global recode file. Sets the recode codes, new missing values
     * and the codelist file.
     *
     * @param path String containing the path name of the global recode file.
     * @param recode RecodeMu containing the information on the variable for
     * which the global recode file will be read.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * during reading.
     */
    public void read(String path, RecodeMu recode) throws ArgusException {
        recode.setMissing_1_new("");
        recode.setMissing_2_new("");
        recode.setCodeListFile("");
        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            Tokenizer tokenizer = new Tokenizer(reader);
            while ((line = tokenizer.nextLine()) != null) {
                String token = tokenizer.nextToken();
                if (!token.startsWith("<")) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                } else if ("<MISSING>".equals(token)) {
                    token = tokenizer.nextToken();
                    recode.setMissing_1_new(token);
                    token = tokenizer.nextToken();
                    if (token != null) {
                        recode.setMissing_2_new(token);
                    }
                } else if ("<CODELIST>".equals(token)) {
                    recode.setCodeListFile(tokenizer.nextToken());
                } else {
                    throw new ArgusException("Error reading file, invalid token: " + token);
                }
            }
            reader.close();
            recode.setGrcText(sb.toString());
            recode.setGrcFile(path);
        } catch (IOException ex) {
            throw new ArgusException("Error during reading file");
        }

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
        recode.setPositionsTruncated(Integer.toString(positions));
    }

}
