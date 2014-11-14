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
        this.setLocationRelativeTo(null);
        this.setTitle("Method for generating tables");
    }

    /**
     * 
     * @return 
     */
    public int getDimensionTextField() {
        return Integer.parseInt(this.dimensionTextField.getText());
    }

    /**
     * 
     * @return 
     */
    public boolean isMakeUpToDimensionRadioButton() {
        return this.makeUpToDimensionRadioButton.isSelected();
    }

    /**
     * 
     * @return 
     */
    public boolean isUseIdentificatinLevelRadioButton() {
        return this.useIdentificatinLevelRadioButton.isSelected();
    }

    /**
     * 
     * @return 
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * 
     * @param valid 
     */
    private void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * 
     * @param threshold
     * @param message
     * @return
     * @throws ArgusException 
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
     * 
     * @param dimensions
     * @return 
     */
    private int checkDimensions(int dimensions) {
        try {
            dimensions = Integer.parseInt(this.dimensionTextField.getText());

            if (dimensions <= 0) {
                JOptionPane.showMessageDialog(this, "Illegal value for the dimension, dimension cannot be less than 1");
                this.setValid(false);
            } else if (dimensions > MuARGUS.MAXDIMS) {
                JOptionPane.showMessageDialog(this, "Illegal value for the dimension, dimension cannot be greater than " + MuARGUS.MAXDIMS);
                this.setValid(false);
            } else if (dimensions > this.numberOfVariables) {
                JOptionPane.showMessageDialog(this, "Not enough identifying variables for this request");
                this.setValid(false);
            }
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Illegal value for the dimension, please enter a positive integer");
            this.setValid(false);
        }
        return dimensions;
    }

    /**
     * 
     */
    private void getThresholdIdLevel() {
        while (!this.isValid()) {
            try {
                int result = getThreshold(this.model.getThreshold(), "Threshold:");
                if (result > 0) {
                    this.model.setThreshold(result);
                    this.setValid(true);
                } else {
                    showErrorMessage(new ArgusException("Threshold needs to greater than 0"));
                }
            } catch (ArgusException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * 
     * @param dimensions 
     */
    private void getThresholdDimensions(int dimensions) {
        int[] thresholds = this.model.getThresholds();
        breakpoint:
        for (int i = 0; i < dimensions; i++) {
            this.setValid(false);
            while (!this.isValid()) {
                try {
                    int result = getThreshold(thresholds[i], "Threshold dimension " + (i + 1) + ":");
                    if (result == 0) {
                        this.setValid(false);
                        break breakpoint;
                    }
                    if (result > 0) {
                        if (i > 0 && result < thresholds[i - 1]) {
                            showErrorMessage(new ArgusException("The threshold needs to be equal to or larger than " + thresholds[i - 1]));
                        } else {
                            thresholds[i] = result;
                            this.setValid(true);
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
        useIdentificatinLevelRadioButton = new javax.swing.JRadioButton();
        makeUpToDimensionRadioButton = new javax.swing.JRadioButton();
        dimensionTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generate");

        methodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Method"));

        methodButtonGroup.add(useIdentificatinLevelRadioButton);
        useIdentificatinLevelRadioButton.setSelected(true);
        useIdentificatinLevelRadioButton.setText("Use identification level");
        useIdentificatinLevelRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                useIdentificatinLevelRadioButtonStateChanged(evt);
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
                        .addComponent(useIdentificatinLevelRadioButton)
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
                .addComponent(useIdentificatinLevelRadioButton)
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
        this.setValid(false);
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        this.setValid(true);
        int dimensions = 0;
        if (this.makeUpToDimensionRadioButton.isSelected()) {
            dimensions = checkDimensions(dimensions);
        }

        if (isValid()) {
            this.setValid(false);
            if (this.useIdentificatinLevelRadioButton.isSelected()) {
                getThresholdIdLevel();
            } else if (this.makeUpToDimensionRadioButton.isSelected()) {
                getThresholdDimensions(dimensions);
            }
        }

        if (isValid()) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void dimensionTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dimensionTextFieldFocusGained
        this.makeUpToDimensionRadioButton.setSelected(true);
    }//GEN-LAST:event_dimensionTextFieldFocusGained

    private void useIdentificatinLevelRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_useIdentificatinLevelRadioButtonStateChanged
        if (this.useIdentificatinLevelRadioButton.isSelected()) {
            this.dimensionTextField.setText("");
        }
    }//GEN-LAST:event_useIdentificatinLevelRadioButtonStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dimensionTextField;
    private javax.swing.JRadioButton makeUpToDimensionRadioButton;
    private javax.swing.ButtonGroup methodButtonGroup;
    private javax.swing.JPanel methodPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton useIdentificatinLevelRadioButton;
    // End of variables declaration//GEN-END:variables
}
