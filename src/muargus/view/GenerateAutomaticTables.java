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
import java.awt.HeadlessException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import muargus.MuARGUS;
import muargus.model.Combinations;
import muargus.model.MetadataMu;
import muargus.model.VariableMu;

/**
 * Dialog used for generating tables automatically.
 *
 * @author Statistics Netherlands
 */
public class GenerateAutomaticTables extends DialogBase {

    private final Combinations model;
    private boolean valid; // is used to continue with the calculation
    private final int numberOfVariables;
    private long numberOfTables;
    private final MetadataMu metadata;
    private final int CANCEL_OPTION = -123456; // this value is given when no answer is given

    /**
     * Creates new form GenerateAutomaticTables
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param model Combinations instance containing the model class of the
     * select combinations screen
     * @param numberOfVariables Integer containing the number of categorical
     * variables.
     * @param metadata MetadataMu instance containing the metadata.
     */
    public GenerateAutomaticTables(java.awt.Frame parent, boolean modal, Combinations model,
            int numberOfVariables, MetadataMu metadata) {
        super(parent, modal, null);
        this.model = model;
        this.valid = false;
        this.numberOfVariables = numberOfVariables;
        this.metadata = metadata;
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Method for generating tables");
    }

    /**
     * Gets the number of dimensions.
     *
     * @return Integer containing the number of dimensions.
     */
    public int getDimensions() {
        return Integer.parseInt(this.dimensionTextField.getText());
    }

    /**
     * Returns whether tables should be generated up to a defined number of
     * dimensions.
     *
     * @return Boolean indicating whether tables should be made up to a defined
     * number of dimensions.
     */
    public boolean isMakeUpToDimensionRadioButton() {
        return this.makeUpToDimensionRadioButton.isSelected();
    }

    /**
     * Returns whether tables should be generated using the identification
     * level.
     *
     * @return Boolean indicating whether tables should be generated using the
     * identification level.
     */
    public boolean isUseIdentificationLevelRadioButton() {
        return this.useIdentificationLevelRadioButton.isSelected();
    }

    /**
     * Returns whether the values entered are valid.
     *
     * @return Boolean indicating whether the values entered are valid.
     */
    public boolean isInputValid() {
        return this.valid;
    }

    /**
     * Sets whether the values entered are valid.
     *
     * @param valid Boolean indicating whether the values entered are valid.
     */
    private void setInputValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets the user input for the threshold. Returns a value of zero if no or
     * an empty value is entered. Non-valid input throw an exception.
     *
     * @param threshold Integer containign the default threshold as been spefor
     * the given dimension.
     * @param message String containing the input message.
     * @return Integer containing the user input for the threshold.
     * @throws ArgusException Throws an ArgusException when a non-integer value
     * is entered.
     */
    private int getThreshold(int threshold, String message) throws ArgusException {
        String result = JOptionPane.showInputDialog(null, message, threshold);
        if (result == null || result.length() == 0) {
            return this.CANCEL_OPTION;
        }
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            throw new ArgusException("Illegal value for the threshold, please enter a positive integer");
        }
    }

    /**
     * Checks if the entered value for dimensions is valid. The number of
     * dimensions cannot be less than 1 or greater than the maximum number of
     * dimensions or the number of identifying variables.
     *
     * @return Integer containing the number of dimensions.
     */
    private int checkDimensions() {
        int dimensions = 0;
        try {
            dimensions = Integer.parseInt(this.dimensionTextField.getText());

            if (dimensions <= 0) {
                JOptionPane.showMessageDialog(this, "Illegal value for the dimension, dimension cannot be less than 1");
                setInputValid(false);
            } else if (dimensions > MuARGUS.MAXDIMS) {
                JOptionPane.showMessageDialog(this, "Illegal value for the dimension, dimension cannot be greater than " + MuARGUS.MAXDIMS);
                setInputValid(false);
            } else if (dimensions > this.numberOfVariables) {
                JOptionPane.showMessageDialog(this, "Not enough identifying variables for this request\nVariables with ID-Level = 0 are excluded");
                setInputValid(false);
            }
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Illegal value for the dimension, please enter a positive integer");
            setInputValid(false);
        }
        return dimensions;
    }

    /**
     * Gets the threshold value for generating tables using the identification
     * level.
     */
    private void getThresholdIdLevel() {
        while (!isInputValid()) {
            try {
                int result = getThreshold(this.model.getThreshold(), "Threshold:");
                if (result == this.CANCEL_OPTION) {
                    return;
                } else if (result > 0) {
                    this.model.setThreshold(result);
                    setInputValid(true);
                } else {
                    showErrorMessage(new ArgusException("Threshold needs to greater than 0"));
                }
            } catch (ArgusException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Gets the threshold values for generating tables up to a given number of
     * dimensions.
     *
     * @param dimensions Integer containing the number of dimensions for which
     * tables should be generated.
     */
    private void getThresholdDimensions(int dimensions) {
        int[] thresholds = this.model.getThresholds();
        breakpoint:
        for (int i = 0; i < dimensions; i++) {
            setInputValid(false);
            while (!isInputValid()) {
                try {
                    int result = getThreshold(thresholds[i], "Threshold dimension " + (i + 1) + ":");
                    if (result == this.CANCEL_OPTION) {
                        return;
                    } else if (result == 0) {
                        setInputValid(false);
                        break breakpoint;
                    }
                    if (result > 0) {
                        thresholds[i] = result;
                        setInputValid(true);
                    } else {
                        showErrorMessage(new ArgusException("Threshold needs to greater than 0"));
                    }
                } catch (ArgusException ex) {
                    showErrorMessage(ex);
                }
            }
        }
        this.model.setThresholds(thresholds);
        this.model.setThreshold(thresholds[0]);
    }

    /**
     * Asks the user to continue generating tables if more than the maximum
     * number of tables before user confirmation are to be generated.
     *
     * @return Boolean indicating whether the tables should be generated.
     */
    private boolean generateTables() {
        if (isMakeUpToDimensionRadioButton()) {
            setNumberOfTables(getDimensions(), this.numberOfVariables);
        } else if (isUseIdentificationLevelRadioButton()) {
            if (!setNumberOfTablesIdLevel()) {
                return false;
            }
        }
        if (this.numberOfTables > this.model.getMaximumSizeBeforeUserConfirmation()
                && this.numberOfTables < this.model.getMaximumNumberOfTables()) {
            return JOptionPane.showConfirmDialog(this, "Are you sure that you want to generate "
                    + this.numberOfTables + " tables?", "Mu Argus",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        } else if (this.numberOfTables > this.model.getMaximumSizeBeforeUserConfirmation()) {
            return JOptionPane.showConfirmDialog(this, "Are you sure that you want to generate "
                    + this.numberOfTables + " tables?\nGenerating this many tables will probably result in memory problems.", "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        }
        return true;
    }

    /**
     * Sets the number of tables that will be generated for a given number of
     * dimensions. This is the start of a recursive method. It wil call the
     * method numberOfTables and sets the initial value for numberOfTables to
     * one.
     *
     * @param dimensions Integer containing the number of dimensions for which
     * tables need to be generated.
     * @param numberOfVariables Integere containing the number of variables.
     */
    private void setNumberOfTables(int dimensions, int numberOfVariables) {
        numberOfTabels(1, dimensions, numberOfVariables);
    }

    /**
     * Calculates the number of tables that will be generated for a given number
     * of dimensions. This is a recursive method that will calculate the
     * equation: N * (N-1) * ... (N-D+1) where N is the number of variables and
     * D is the number of dimensions.
     *
     * @param numberOfTables Long containing the number of tables.
     * @param dimensions Integer containing the number of dimensions.
     * @param numberOfVariables Integer containing the number of variables.
     */
    private void numberOfTabels(long numberOfTables, int dimensions, int numberOfVariables) {
        if (dimensions > 0) {
            long tempNumber = numberOfTables * numberOfVariables;
            int tempNumberOfVariables = numberOfVariables - 1;
            int tempDimensions = dimensions - 1;
            numberOfTabels(tempNumber, tempDimensions, tempNumberOfVariables);
        } else if (dimensions == 0) {
            this.numberOfTables = numberOfTables;
        }
    }

    /**
     * Sets the number of tables using ID-level. This method first makes an
     * array containing the number of categorical variables for each dimension.
     * Following it makes a cumulative array of integers controlling for the
     * dimension. This will result in an array giving the maximum number of
     * variables used per dimension.
     *
     *
     * calls the method calculatefirst creates an integer array containing the
     * number op variables per id-level. Following it will generate an ArrayList
     * of integers containing the maximum number of variables used per
     * dimension. the number of check if there is at least one variable with an
     * id-levels greater than 0. Finally the number of variables per id-level
     * (greater than 0) are multiplied with each other resulting in the number
     * of tables. Then it will generate an array with the number of tables per
     * variable up to the first dimension. This is similar to setting all values
     * to one and uses this to start the recursive method, which will calculate
     * the number of tables.
     *
     * @return Boolean indicating whether tables will be generated.
     */
    private boolean setNumberOfTablesIdLevel() {
        this.numberOfTables = 0;

        // make an array containing the number of categorical variables for each dimension.
        int[] id = new int[6];
        for (VariableMu v : this.metadata.getVariables()) {
            if (v.isCategorical()) {
                id[v.getIdLevel()]++;
            }
        }
        // make a cumulative array of integers controlling for the dimension. 
        // This will result in an array giving the maximum number of variables 
        //used per dimension.
        ArrayList<Integer> cumulative = new ArrayList<>();
        for (int i = 1; i < id.length; i++) {
            if (id[i] > 0) {
                cumulative.add(cumulative.size() > 0
                        ? cumulative.get(cumulative.size() - 1) + id[i] - 1 : id[i]);
            }
        }

        // fills the array with the number of tables per variable up to the 
        // first dimension. This is similar to setting all values to one.
        int[] list = new int[cumulative.get(0)];
        for (int i = 0; i < list.length; i++) {
            list[i] = 1;
        }
        // call the recursive function
        setNumberOfTablesIdLevel(1, list, cumulative.size(), cumulative);
        // give a warning when no tables will be generated
        if (this.numberOfTables <= 0) {
            JOptionPane.showMessageDialog(this, "No variables found with an id-level greater than 0");
            return false;
        }
        return true;
    }

    /**
     * Recursive method for calculating the number of tabels using ID-level.
     * This method uses the idea that the number of tables with a particular
     * variable as last variable is equal to the sum of the number of tables
     * that can be generated for all possible second to last variables when
     * using one dimension fewer. If we have for example two variables with
     * ID-level=1 (A & B) and two variables with ID-level=2(C % D) then all the
     * possibel tables are {AB, AC, AD, BC, BD}. Here we have 3 possible last
     * variables {B, C, D} and 2 possible second to last variables {A, B}. Note
     * that lower ID-levels can be used for higher dimensions but higher
     * ID-levels can't be used for lower dimensions. All tables for the second
     * to last variables are 1-dimensional and therefore only 1 table for each
     * variable exists. For the last variables, B has only one second to last
     * variable {A} and both C and D have two second to last variables {A, B}.
     * The number of tables containing a particular last variable can be
     * obtained by summing the number of tables that can be genarated for the
     * second te last variables. Resulting that the total number of tables
     * ending with B is the total number of tables having a second last variable
     * A equals 1. For tables having a final variable C, the total number of
     * tabels equals the number of tables having a second last variable A plus
     * the number of tables having a second last variable B, resulting in 2
     * tables (and similarly for D). To get the total number of tables one
     * simply sums the number of tables for the last dimension {1 + 2 + 2 = 5}.
     *
     * @param _dimension Integer containing the current dimension.
     * @param _list Array of integers containing the number of tables made per
     * variable up to the current dimension.
     * @param dimensions Integer containing the maximum number
     * ofcumulativedimensions.
     * @param cumulative ArrayList of integers containing the maximum number of
     * variables used per dimension.
     */
    public void setNumberOfTablesIdLevel(int _dimension, int[] _list, int dimensions, ArrayList<Integer> cumulative) {
        int dimension = _dimension;
        if (dimension < dimensions) {
            int[] list = new int[cumulative.get(dimension)];

            for (int i = 0; i < _list.length; i++) {
                for (int j = i; j < list.length; j++) {
                    list[j] += _list[i];
                }
            }
            dimension++;
            setNumberOfTablesIdLevel(dimension, list, dimensions, cumulative);
        } else {
            for (int i : _list) {
                this.numberOfTables += i;
            }
            dimension++;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        methodButtonGroup = new javax.swing.ButtonGroup();
        methodPanel = new javax.swing.JPanel();
        useIdentificationLevelRadioButton = new javax.swing.JRadioButton();
        makeUpToDimensionRadioButton = new javax.swing.JRadioButton();
        dimensionTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generate");

        methodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Method"));

        methodButtonGroup.add(useIdentificationLevelRadioButton);
        useIdentificationLevelRadioButton.setSelected(true);
        useIdentificationLevelRadioButton.setText("Use identification level");
        useIdentificationLevelRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                useIdentificationLevelRadioButtonStateChanged(evt);
            }
        });

        methodButtonGroup.add(makeUpToDimensionRadioButton);
        makeUpToDimensionRadioButton.setText("Make all tables up to dimension");

        dimensionTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dimensionTextFieldFocusGained(evt);
            }
        });

        javax.swing.GroupLayout methodPanelLayout = new javax.swing.GroupLayout(methodPanel);
        methodPanel.setLayout(methodPanelLayout);
        methodPanelLayout.setHorizontalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(methodPanelLayout.createSequentialGroup()
                        .addComponent(useIdentificationLevelRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(methodPanelLayout.createSequentialGroup()
                        .addComponent(makeUpToDimensionRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dimensionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        methodPanelLayout.setVerticalGroup(
            methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(methodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(useIdentificationLevelRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(methodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(makeUpToDimensionRadioButton)
                    .addComponent(dimensionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(methodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(methodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setInputValid(false);
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setInputValid(true);
        int dimensions = 0;
        if (this.makeUpToDimensionRadioButton.isSelected()) {
            dimensions = checkDimensions();
        }

        if (isInputValid()) {
            setInputValid(false);
            if (this.useIdentificationLevelRadioButton.isSelected()) {
                if (generateTables()) {
                    getThresholdIdLevel();
                }
            } else if (this.makeUpToDimensionRadioButton.isSelected()) {
                if (generateTables()) {
                    getThresholdDimensions(dimensions);
                }
            }
        }

        if (isInputValid()) {
            setVisible(false);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void dimensionTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dimensionTextFieldFocusGained
        this.makeUpToDimensionRadioButton.setSelected(true);
    }//GEN-LAST:event_dimensionTextFieldFocusGained

    private void useIdentificationLevelRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_useIdentificationLevelRadioButtonStateChanged
        if (this.useIdentificationLevelRadioButton.isSelected()) {
            this.dimensionTextField.setText("");
        }
    }//GEN-LAST:event_useIdentificationLevelRadioButtonStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dimensionTextField;
    private javax.swing.JRadioButton makeUpToDimensionRadioButton;
    private javax.swing.ButtonGroup methodButtonGroup;
    private javax.swing.JPanel methodPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton useIdentificationLevelRadioButton;
    // End of variables declaration//GEN-END:variables
}
