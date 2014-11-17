package muargus.view;

import argus.model.ArgusException;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import muargus.MuARGUS;
import muargus.model.Combinations;

/**
 * Dialog used for generating tables automatically.
 *
 * @author Statistics Netherlands
 */
public class GenerateAutomaticTables extends DialogBase {

    private final Combinations model;
    private boolean valid; // is used to continue with the calculation
    private final int numberOfVariables;

    /**
     * Creates new form GenerateAutomaticTables
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param model Combinations instance containing the model class of the
     * select combinations screen
     * @param numberOfVariables Integer containing the number of categorical
     * variables.
     */
    public GenerateAutomaticTables(java.awt.Frame parent, boolean modal, Combinations model,
            int numberOfVariables) {
        super(parent, modal, null);
        this.model = model;
        this.valid = false;
        this.numberOfVariables = numberOfVariables;
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
            return 0;
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
                JOptionPane.showMessageDialog(this, "Not enough identifying variables for this request");
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
                if (result > 0) {
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
                    if (result == 0) {
                        setInputValid(false);
                        break breakpoint;
                    }
                    if (result > 0) {
                        if (i > 0 && result < thresholds[i - 1]) {
                            showErrorMessage(new ArgusException("The threshold needs to be equal to or larger than " + thresholds[i - 1]));
                        } else {
                            thresholds[i] = result;
                            setInputValid(true);
                        }
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
                getThresholdIdLevel();
            } else if (this.makeUpToDimensionRadioButton.isSelected()) {
                getThresholdDimensions(dimensions);
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
