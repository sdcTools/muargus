/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import muargus.MuARGUS;
import muargus.model.GlobalRecode;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.view.GlobalRecodeView;

/**
 *
 * @author ambargus
 */
public class GlobalRecodeController {

    GlobalRecodeView view;
    //GlobalRecode model;
    MetadataMu metadata;
    //Combinations selectCombinationsModel;
    CalculationService service;

    /**
     *
     * @param parentView
     * @param metadata
     * @param model
     * @param selectCombinationsModel
     */
    public GlobalRecodeController(java.awt.Frame parentView, MetadataMu metadata) {
        //this.model = model;
        this.service = MuARGUS.getCalculationService();
        this.view = new GlobalRecodeView(parentView, true, this);
        this.metadata = metadata;
        this.view.setMetadataMu(this.metadata);
        
        //this.selectCombinationsModel = selectCombinationsModel;
        //this.view = view;
    }

    public void showView(int selectedRowIndex) {
        this.view.setSelectedIndex(selectedRowIndex);
        this.view.setVisible(true);
    }

//    public GlobalRecode getModel() {
//        return this.model;
//    }
    /**
     *
     */
    public void close() {
        view.setVisible(false);
    }

    public int getSelectedVariableIndex() {
        return view.getSelectedIndex();
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


    private GlobalRecode getGlobalRecode() {
        return this.metadata.getCombinations().getGlobalRecode();
    }

    /**
     *
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
     *
     */
    public void apply(RecodeMu recode) throws ArgusException {
        String warning = this.service.doRecode(this.metadata, recode);
        view.showWarning(warning);
        this.service.applyRecode();
        recode.setTruncated(false);
        recode.setRecoded(true);
    }

    public void undo(RecodeMu recode) throws ArgusException {
        this.service.undoRecode(recode);
        recode.setRecoded(false);
        recode.setTruncated(false);
    }

        /**
     *
     */
    public void truncate(RecodeMu recode, int positions) throws ArgusException {
        this.service.truncate(recode, positions);
        recode.setRecoded(false);
        recode.setTruncated(true);
        recode.setPositionsTruncated(Integer.toString(positions));
    }

    /**
     *
     */

}
