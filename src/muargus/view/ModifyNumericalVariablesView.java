package muargus.view;

import java.awt.Component;
import java.awt.Container;
import java.text.DecimalFormat;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.HighlightTableCellRenderer;
import muargus.MuARGUS;
import muargus.controller.ModifyNumericalVariablesController;
import muargus.model.ModifyNumericalVariables;
import muargus.model.ModifyNumericalVariablesSpec;

/**
 * View class of the ModifyNumericalVariables screen.
 *
 * @author Statistics Netherlands
 */
public class ModifyNumericalVariablesView extends DialogBase<ModifyNumericalVariablesController> {

    ModifyNumericalVariables model;
    private final int[] variablesColumnWidth = {20, 80};
    private int selectedRow = 0;

    /**
     * Creates new form ModifyNumericalVariablesView.
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param controller the controller of this view.
     */
    public ModifyNumericalVariablesView(java.awt.Frame parent, boolean modal, ModifyNumericalVariablesController controller) {
        super(parent, modal, controller);
        initComponents();
        setLocationRelativeTo(null);
        this.variablesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the data. Sets the model, makes the variables table and
     * updates the values.
     */
    @Override
    public void initializeData() {
        this.model = getMetadata().getCombinations().getModifyNumericalVariables();
        if (this.model.getModifyNumericalVariablesSpec().isEmpty()) {
            getController().setModifyNumericalVariablesSpecs();
        }
        makeVariablesTable();
        updateValues();
    }

    /**
     * Gets the ModifyNumericalVariablesSpec belonging tot the selected
     * variable.
     *
     * @return ModifyNumericalVariablesSpec instance belonging tot the selected
     * variable.
     */
    private ModifyNumericalVariablesSpec getModifyNumericalVariablesSpec() {
        ModifyNumericalVariablesSpec selected = this.model.getModifyNumericalVariablesSpec().get(this.selectedRow);
        return selected;
    }

    /**
     * Updates the values.
     */
    private void updateValues() {
        int selectedIndex = this.variablesTable.convertRowIndexToModel(this.variablesTable.getSelectedRow());
        double[] min_max = getController().getMinMax(
                this.model.getModifyNumericalVariablesSpec().get(selectedIndex).getVariable());

        ModifyNumericalVariablesSpec selected = this.model.getModifyNumericalVariablesSpec().get(selectedIndex);
        selected.setMin_max(min_max);
        this.minimumTextField.setText(getController().getMin(selected));
        this.maximumTextField.setText(getController().getMax(selected));
        enableControls(this.weightNoisePanel, selected.getVariable().isWeight());
        enableControls(this.roundPanel, !selected.getVariable().isCategorical());
        this.bottomValueTextField.setText(formatDouble(selected.getBottomValue()));
        this.bottomCodingReplacementTextField.setText(selected.getBottomReplacement());
        this.topValueTextField.setText(formatDouble(selected.getTopValue()));
        this.topCodingReplacementTextField.setText(selected.getTopReplacement());
        this.roundingBaseTextField.setText(formatDouble(selected.getRoundingBase()));
        this.percentageTextField.setText(formatDouble(selected.getWeightNoisePercentage()));
    }

    /**
     * Gets the string format for a double value.
     *
     * @param d Double value that will be converted to a string.
     * @return String containing either the double value as a string or a empty
     * string if the double value is invalid.
     */
    private String formatDouble(double d) {
        DecimalFormat f = (DecimalFormat) DecimalFormat.getNumberInstance(MuARGUS.getLocale());
        f.setGroupingUsed(false);
        return Double.isNaN(d) ? "" : f.format(d);
    }

    /**
     * Makes the variables table and sets its values.
     */
    private void makeVariablesTable() {
        getController().setVariablesData();

        TableModel variablesTableModel = new DefaultTableModel(
                this.model.getVariablesData(), this.model.getVariablesColumnNames()) {
                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return false;
                    }
                };
        this.variablesTable.setDefaultRenderer(Object.class, new HighlightTableCellRenderer());
        this.variablesTable.setModel(variablesTableModel);

        for (int i = 0; i < this.variablesColumnWidth.length; i++) {
            this.variablesTable.getColumnModel().getColumn(i).setMinWidth(this.variablesColumnWidth[i]);
            this.variablesTable.getColumnModel().getColumn(i).setPreferredWidth(this.variablesColumnWidth[i]);
        }

        this.variablesTable.setDefaultRenderer(Object.class, new HighlightTableCellRenderer());
        for (int i = 0; i < this.model.getModifyNumericalVariablesSpec().size(); i++) {
            this.selectedRow = i;
            showModified();
        }

        this.variablesTable.getSelectionModel().setSelectionInterval(0, 0);
        this.selectedRow = 0;
        this.variablesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    variablesSelectionChanged();
                }
            }
        });
    }

    /**
     * Event handler when the selected variable has changed. If a different
     * variable is selected, the entered values are checked and the visible
     * values are updated.
     */
    private void variablesSelectionChanged() {
        if (this.variablesTable.convertRowIndexToModel(this.variablesTable.getSelectedRow()) != this.selectedRow) {
            checkValidAnswer();
        }
        updateValues();
    }

    /**
     * Checks whether the answers entered are valid. It is checked if values are
     * entered. If values are entered, the controller is called to check if the
     * values are valid and return warning message(s) if values are not valid.
     * If all values are valid the numerical variable is modified through the
     * apply method of the controller.
     *
     * @return Boolean indicating whether the answers entered are valid.
     */
    private boolean checkValidAnswer() {
        ModifyNumericalVariablesSpec selected = getModifyNumericalVariablesSpec();
        boolean hasValue = valueEntered();
        if (hasValue) {
            String message = getController().getWarningMessage(selected,
                    this.bottomValueTextField.getText(),
                    this.topValueTextField.getText(),
                    this.bottomCodingReplacementTextField.getText(),
                    this.topCodingReplacementTextField.getText(),
                    this.roundingBaseTextField.getText(),
                    this.percentageTextField.getText());
            if (!message.equals("")) {
                showMessage(message);
                this.variablesTable.getSelectionModel().setSelectionInterval(this.selectedRow, this.selectedRow);
                return false;
            }
        }
        setValuesInModel();
        selected.setModified(hasValue);
        showModified();
        getController().apply(selected);
        this.selectedRow = this.variablesTable.convertRowIndexToModel(this.variablesTable.getSelectedRow());
        return true;
    }

    /**
     * Sets whether this variable has been modified.
     */
    private void showModified() {
        this.variablesTable.getModel().setValueAt(getModifyNumericalVariablesSpec().getModifiedText(), this.selectedRow, 0);
        this.variablesTable.getModel().setValueAt(getModifyNumericalVariablesSpec().getVariable().getName(), this.selectedRow, 1);
    }

    /**
     * Sets the values in the model.
     */
    private void setValuesInModel() {
        ModifyNumericalVariablesSpec selected = getModifyNumericalVariablesSpec();
        selected.setBottomValue(this.bottomValueTextField.getText());
        selected.setBottomReplacement(this.bottomCodingReplacementTextField.getText());
        selected.setTopValue(this.topValueTextField.getText());
        selected.setTopReplacement(this.topCodingReplacementTextField.getText());
        selected.setRoundingBase(this.roundingBaseTextField.getText());
        selected.setWeightNoisePercentage(this.percentageTextField.getText());
    }

    /**
     * Checks whether a value has been entered.
     *
     * @return Boolean indicating whether at least one value has been entered.
     */
    private boolean valueEntered() {
        boolean valueEntered = false;
        if (!this.bottomValueTextField.getText().equals("")) {
            valueEntered = true;
        }
        if (!this.bottomCodingReplacementTextField.getText().equals("")) {
            valueEntered = true;
        }
        if (!this.topValueTextField.getText().equals("")) {
            valueEntered = true;
        }
        if (!this.topCodingReplacementTextField.getText().equals("")) {
            valueEntered = true;
        }
        if (!this.roundingBaseTextField.getText().equals("")) {
            valueEntered = true;
        }
        if (!this.percentageTextField.getText().equals("")) {
            valueEntered = true;
        }
        return valueEntered;
    }

    /**
     * Checks whether a double and a string are equal.
     *
     * @param text String containing the text to be compaired.
     * @param d Double containing the number to be compaired.
     * @return Boolean indicating whether the string and the double are equal.
     */
    private boolean doubleEquals(String text, double d) {
        return text.equals(formatDouble(d));
    }

    /**
     * Checks for all input if the value is changed.
     *
     * @return Boolean indicating whether a value has changed.
     */
    private boolean valueChanged() {
        ModifyNumericalVariablesSpec selected = getModifyNumericalVariablesSpec();
        boolean valueChanged = false;
        if (!doubleEquals(this.bottomValueTextField.getText(), selected.getBottomValue())) {
            valueChanged = true;
        }
        if (!this.bottomCodingReplacementTextField.getText().equals(selected.getBottomReplacement())) {
            valueChanged = true;
        }
        if (!doubleEquals(this.topValueTextField.getText(), selected.getTopValue())) {
            valueChanged = true;
        }
        if (!this.topCodingReplacementTextField.getText().equals(selected.getTopReplacement())) {
            valueChanged = true;
        }
        if (!doubleEquals(this.roundingBaseTextField.getText(), selected.getRoundingBase())) {
            valueChanged = true;
        }
        if (!doubleEquals(this.percentageTextField.getText(), selected.getWeightNoisePercentage())) {
            valueChanged = true;
        }
        return valueChanged;
    }

    /**
     * Enables/Disables all controls and sub controls from a component.
     *
     * @param control Component for which all sub-components should be
     * enabled/disabled.
     * @param enable Boolean indicating whether the component and its
     * sub-components should be enabled.
     */
    private void enableControls(Component control, boolean enable) {
        if (!(control instanceof JComponent) && !(control instanceof JDialog)) {
            return;
        }
        control.setEnabled(enable);
        if (control instanceof Container) {
            for (Component c : ((Container) control).getComponents()) {
                enableControls(c, enable);
            }
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

        variablesScrollPane = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        bottomCodingPanel = new javax.swing.JPanel();
        minimumLabel = new javax.swing.JLabel();
        minimumTextField = new javax.swing.JTextField();
        bottomValueLabel = new javax.swing.JLabel();
        bottomValueTextField = new javax.swing.JTextField();
        bottomCodingReplacementLabel = new javax.swing.JLabel();
        bottomCodingReplacementTextField = new javax.swing.JTextField();
        topCodingPanel = new javax.swing.JPanel();
        maximumLabel = new javax.swing.JLabel();
        maximumTextField = new javax.swing.JTextField();
        topValueLabel = new javax.swing.JLabel();
        topValueTextField = new javax.swing.JTextField();
        topCodingReplacementLabel = new javax.swing.JLabel();
        topCodingReplacementTextField = new javax.swing.JTextField();
        roundPanel = new javax.swing.JPanel();
        roundingBaseLabel = new javax.swing.JLabel();
        roundingBaseTextField = new javax.swing.JTextField();
        weightNoisePanel = new javax.swing.JPanel();
        percentageLabel = new javax.swing.JLabel();
        percentageTextField = new javax.swing.JTextField();
        applyButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Numerical Variables");
        setMinimumSize(new java.awt.Dimension(410, 430));
        setPreferredSize(new java.awt.Dimension(410, 430));

        variablesTable.setAutoCreateRowSorter(true);
        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "M", "Variable"
            }
        ));
        variablesScrollPane.setViewportView(variablesTable);
        if (variablesTable.getColumnModel().getColumnCount() > 0) {
            variablesTable.getColumnModel().getColumn(0).setPreferredWidth(8);
        }

        bottomCodingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bottom Coding"));

        minimumLabel.setText("Minimum:");

        minimumTextField.setEnabled(false);

        bottomValueLabel.setText("Bottom Value:");

        bottomCodingReplacementLabel.setText("Replacement");

        javax.swing.GroupLayout bottomCodingPanelLayout = new javax.swing.GroupLayout(bottomCodingPanel);
        bottomCodingPanel.setLayout(bottomCodingPanelLayout);
        bottomCodingPanelLayout.setHorizontalGroup(
            bottomCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomCodingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bottomCodingReplacementLabel)
                    .addComponent(bottomValueLabel)
                    .addComponent(minimumLabel)
                    .addComponent(minimumTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(bottomValueTextField)
                    .addComponent(bottomCodingReplacementTextField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bottomCodingPanelLayout.setVerticalGroup(
            bottomCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomCodingPanelLayout.createSequentialGroup()
                .addComponent(minimumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minimumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bottomValueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bottomCodingReplacementLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomCodingReplacementTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        topCodingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Top Coding"));

        maximumLabel.setText("Maximum:");

        maximumTextField.setEnabled(false);

        topValueLabel.setText("Top Value:");

        topCodingReplacementLabel.setText("Replacement:");

        javax.swing.GroupLayout topCodingPanelLayout = new javax.swing.GroupLayout(topCodingPanel);
        topCodingPanel.setLayout(topCodingPanelLayout);
        topCodingPanelLayout.setHorizontalGroup(
            topCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topCodingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(topCodingPanelLayout.createSequentialGroup()
                        .addGroup(topCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(topValueLabel)
                            .addComponent(topCodingReplacementLabel)
                            .addComponent(maximumLabel))
                        .addGap(0, 10, Short.MAX_VALUE))
                    .addComponent(maximumTextField)
                    .addComponent(topValueTextField)
                    .addComponent(topCodingReplacementTextField))
                .addContainerGap())
        );
        topCodingPanelLayout.setVerticalGroup(
            topCodingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topCodingPanelLayout.createSequentialGroup()
                .addComponent(maximumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maximumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(topValueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(topCodingReplacementLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topCodingReplacementTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roundPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Round"));

        roundingBaseLabel.setText("Rounding base:");

        javax.swing.GroupLayout roundPanelLayout = new javax.swing.GroupLayout(roundPanel);
        roundPanel.setLayout(roundPanelLayout);
        roundPanelLayout.setHorizontalGroup(
            roundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundingBaseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roundingBaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        roundPanelLayout.setVerticalGroup(
            roundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roundingBaseLabel)
                    .addComponent(roundingBaseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        weightNoisePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weight Noise"));

        percentageLabel.setText("Percentage:");

        javax.swing.GroupLayout weightNoisePanelLayout = new javax.swing.GroupLayout(weightNoisePanel);
        weightNoisePanel.setLayout(weightNoisePanelLayout);
        weightNoisePanelLayout.setHorizontalGroup(
            weightNoisePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weightNoisePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(percentageLabel)
                .addGap(27, 27, 27)
                .addComponent(percentageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        weightNoisePanelLayout.setVerticalGroup(
            weightNoisePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(weightNoisePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(weightNoisePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(percentageLabel)
                    .addComponent(percentageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(applyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(roundPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(bottomCodingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(topCodingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(weightNoisePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bottomCodingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(topCodingPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(roundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(weightNoisePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(closeButton)
                            .addComponent(applyButton)))
                    .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        if (valueEntered() && valueChanged()) {
            if (showConfirmDialog("Do you want to apply?")) {
                checkValidAnswer();
            }
        }
        //TODO: check if undo should be applied when no value has been entered.
//        else if (valueChanged() && !valueEntered()){
//            if (showConfirmDialog("Do you want to undo the modification of " 
//                    + getModifyNumericalVariablesSpec().getVariable().getName() + "?")) {
//                checkValidAnswer();
//            }
//        }
        getController().close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        checkValidAnswer();
        updateValues();
    }//GEN-LAST:event_applyButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel bottomCodingPanel;
    private javax.swing.JLabel bottomCodingReplacementLabel;
    private javax.swing.JTextField bottomCodingReplacementTextField;
    private javax.swing.JLabel bottomValueLabel;
    private javax.swing.JTextField bottomValueTextField;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel maximumLabel;
    private javax.swing.JTextField maximumTextField;
    private javax.swing.JLabel minimumLabel;
    private javax.swing.JTextField minimumTextField;
    private javax.swing.JLabel percentageLabel;
    private javax.swing.JTextField percentageTextField;
    private javax.swing.JPanel roundPanel;
    private javax.swing.JLabel roundingBaseLabel;
    private javax.swing.JTextField roundingBaseTextField;
    private javax.swing.JPanel topCodingPanel;
    private javax.swing.JLabel topCodingReplacementLabel;
    private javax.swing.JTextField topCodingReplacementTextField;
    private javax.swing.JLabel topValueLabel;
    private javax.swing.JTextField topValueTextField;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JTable variablesTable;
    private javax.swing.JPanel weightNoisePanel;
    // End of variables declaration//GEN-END:variables
}
