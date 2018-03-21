/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus.view;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.resources.ContextHelp;

/**
 * Basic dialog class. This class contains standard methods useable by all view
 * classes that extend this class.
 *
 * @author Statistics Netherlands
 * @param <T>
 */
public class DialogBase<T> extends javax.swing.JDialog {

    private MetadataMu metadata;
    private final T controller;

    /**
     * Creates new form DialogBase
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param controller the controller of the view.
     */
    public DialogBase(java.awt.Frame parent, boolean modal, T controller) {
        super(parent, modal);
        this.controller = controller;
        initComponents();
        setHelpAction();
    }

    /**
     * Sets the metadata.
     *
     * @param metadata MetadataMu instance containing the metadata.
     */
    public void setMetadata(MetadataMu metadata) {
        this.metadata = metadata;
        initializeData();
    }

    /**
     * Gets the metadata.
     *
     * @return MetadataMu instance containing the metadata.
     */
    protected MetadataMu getMetadata() {
        return this.metadata;
    }

    /**
     * Initilizes the data. This class can be implemented inside the specific
     * view classes.
     */
    protected void initializeData() {
        //Base class implementation is empty
    }

    /**
     * Shows an error message.
     *
     * @param ex ArgusException.
     */
    public void showErrorMessage(ArgusException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), MuARGUS.getMessageTitle(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a confirm dialog.
     *
     * @param message String containing the message for which confirmation is
     * needed.
     * @return Boolean indicating whether the user confirmed the message.
     */
    public boolean showConfirmDialog(String message) {
        return (JOptionPane.showConfirmDialog(null, message, MuARGUS.getMessageTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
    }

    /**
     * Shows a message.
     *
     * @param message String containing the message
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, MuARGUS.getMessageTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a file chooser dialog.
     *
     * @param title String instance containing the title of the file chooser
     * dialog.
     * @param forSaving Boolean indicating whether the dialog is for saving.
     * @param filter Array of Strings containing the filter extensions. These
     * extensions are used to filter the files in the directory.
     * @param selectedFile File containing a suggested file. The name of this
     * file is already given as an option.
     * @return String containing the path of the chosen file.
     */
    public String showFileDialog(String title, boolean forSaving, String[] filter, File selectedFile) {
        JFileChooser fileChooser = new JFileChooser();
        String hs = SystemUtils.getRegString("general", "datadir", "");
        if (!hs.equals("")) {
            File file = new File(hs);
            fileChooser.setCurrentDirectory(file);
        }
        fileChooser.setDialogTitle(title);
        fileChooser.resetChoosableFileFilters();
        String[] firstFilter = splitFilter(filter[0]);
        fileChooser.setFileFilter(new FileNameExtensionFilter(firstFilter[0], firstFilter[1]));
        for (int index = 1; index < filter.length; index++) {
            String[] otherFilter = splitFilter(filter[index]);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(otherFilter[0], otherFilter[1]));
        }
        fileChooser.setSelectedFile(selectedFile);
        int result = forSaving ? fileChooser.showSaveDialog(this) : fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        hs = fileChooser.getSelectedFile().getParent();
        if (!"".equals(hs)) {
            SystemUtils.putRegString("general", "datadir", hs);
        }
        return fileChooser.getSelectedFile().getPath();
    }

    /**
     * Shows a file chooser dialog.
     *
     * @param title String instance containing the title of the file chooser
     * dialog.
     * @param forSaving Boolean indicating whether the dialog is for saving.
     * @param filter Array of Strings containing the filter extensions. These
     * extensions are used to filter the files in the directory.
     * @return String containing the path of the chosen file.
     */
    public String showFileDialog(String title, boolean forSaving, String[] filter) {
        return showFileDialog(title, forSaving, filter, null);
    }

    /**
     * Splits the extensions into separate strings.
     *
     * @param filter String containing all extensions.
     * @return Array of Strings containing the filter extensions.
     */
    private String[] splitFilter(String filter) {
        return filter.split("\\|");
    }

    /**
     * Sets the help action. When F1 is pushed the help action will be
     * performed.
     */
    private void setHelpAction() {

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        };
        this.rootPane.getActionMap().put("f1action", action);
        this.rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke("F1"), "f1action");
    }

    /**
     * Gets the named destination linked to the visible view.
     *
     * @return String containing the named destination.
     */
    protected String getHelpNamedDestination() {
        return ContextHelp.fromClassName(this.getClass().getName());
    }

    /**
     * Shows the content sensitive help.
     */
    private void showHelp() {
        try {
            MuARGUS.showHelp(getHelpNamedDestination());
        } catch (ArgusException ex) {
            showErrorMessage(ex);
        }
    }

    /**
     * Shows the step name.
     *
     * @param stepName Sting containing the step name.
     */
    public void showStepName(String stepName) {
        //Base class implementation is empty
    }

    /**
     * Sets the progress.
     *
     * @param progress Integer containing the progress
     */
    public void setProgress(int progress) {
        //Base class implementation is empty
    }

    /**
     * Gets the controller belonging to the view.
     *
     * @return Generic class containing the controller belonging to the view.
     */
    protected T getController() {
        return this.controller;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
