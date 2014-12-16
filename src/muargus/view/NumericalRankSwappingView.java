package muargus.view;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import muargus.HighlightTableCellRenderer;
import muargus.VariableNameCellRenderer;
import muargus.controller.NumericalRankSwappingController;
import muargus.model.NumericalRankSwapping;
import muargus.model.ReplacementSpec;
import muargus.model.VariableMu;

/**
 * View class of the NumericalRankSwapping screen.
 *
 * @author Statistics Netherlands
 */
public class NumericalRankSwappingView extends DialogBase<NumericalRankSwappingController> {

    private NumericalRankSwapping model;
    private DefaultListModel<VariableMu> selectedListModel;

    /**
     * Creates new form NumericalRankSwappingView.
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param controller the controller of this view.
     */
    public NumericalRankSwappingView(java.awt.Frame parent, boolean modal, NumericalRankSwappingController controller) {
        super(parent, modal, controller);
        initComponents();
        setLocationRelativeTo(null);
        this.variablesTable.setDefaultRenderer(Object.class, new HighlightTableCellRenderer());
        this.selectedVariableList.setCellRenderer(new VariableNameCellRenderer());
    }

    /**
     * Initializes the data. Sets the model, sets the table values and updates
     * the values.
     */
    @Override
    public void initializeData() {
        this.model = getMetadata().getCombinations().getNumericalRankSwapping();
        String[][] data = new String[this.model.getVariables().size()][2];
        int index = 0;
        for (VariableMu variable : this.model.getVariables()) {
            data[index] = new String[]{getModifiedText(variable), variable.getName()};
            index++;
        }

        this.variablesTable.setModel(new DefaultTableModel(data, new Object[]{"Modified", "Variable"}));
        this.variablesTable.getSelectionModel().setSelectionInterval(0, 0);
        this.selectedListModel = new DefaultListModel<>();
        this.selectedVariableList.setModel(this.selectedListModel);
        updateValues();
    }

    /**
     * Enables/disables the calculate button.
     */
    private void updateValues() {
        this.calculateButton.setEnabled(getSelectedVariables().size() > 0);
    }

    /**
     * Updates the values inside the table.
     *
     * @param replacement ReplacementSpec instance containing the
     * RankSwappingSpec.
     */
    public void updateVariableRows(ReplacementSpec replacement) {
        for (VariableMu variableMu : replacement.getOutputVariables()) {
            int index = this.model.getVariables().indexOf(variableMu);
            this.variablesTable.setValueAt(getModifiedText(variableMu), index, 0);
            this.variablesTable.setValueAt(variableMu.getName(), index, 1);
        }
    }

    /**
     * Gets the modification text belonging to this particular variable. If a
     * variable is modified "X" is returned, otherwise an empty string is
     * returned.
     *
     * @param variable VariableMu instance of the varible for which the modified
     * text is requested.
     * @return String containing the modified text. If a variable is modified
     * "X" is returned, otherwise an empty string is returned.
     */
    private String getModifiedText(VariableMu variable) {
        for (ReplacementSpec spec : this.model.getRankSwappings()) {
            if (spec.getOutputVariables().contains(variable)) {
                return "X";
            }
        }
        return "";
    }

    /**
     * Gets the selected variables.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedVariables() {
        ArrayList<VariableMu> selected = new ArrayList<>();
        for (Object variable : ((DefaultListModel) this.selectedVariableList.getModel()).toArray()) {
            selected.add((VariableMu) variable);
        }
        return selected;
    }

    /**
     * Gets the rank swapping percentage.
     *
     * @return Integer containing the rank swapping percentage.
     */
    public int getPercentage() {
        try {
            return Integer.parseInt(this.percentageTextField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * Sets the progress bar's current value to the give value.
     *
     * @param progress Integer containing the value of the progress.
     */
    @Override
    public void setProgress(int progress) {
        this.progressBar.setValue(progress);
    }

    /**
     * Shows the step name.
     *
     * @param stepName Sting containing the step name.
     */
    @Override
    public void showStepName(String stepName) {
        this.stepNameLabel.setText(stepName);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        selectedVariableList = new javax.swing.JList();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        percentageTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        okButton = new javax.swing.JButton();
        calculateButton = new javax.swing.JButton();
        toSelectedButton = new javax.swing.JButton();
        fromSelectedButton = new javax.swing.JButton();
        stepNameLabel = new javax.swing.JLabel();
        undoButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Numerical Rank Swapping");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Variable"));

        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Modified", "Variable"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(variablesTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 224, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected"));

        jScrollPane2.setViewportView(selectedVariableList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );

        upButton.setText("↑");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText("↓");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setText("<html>\nRank Swapping<br>percentage");

        percentageTextField.setText("15");

        jLabel1.setText("%");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(percentageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(percentageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        calculateButton.setText("Calculate");
        calculateButton.setEnabled(false);
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        toSelectedButton.setText("→");
        toSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toSelectedButtonActionPerformed(evt);
            }
        });

        fromSelectedButton.setText("←");
        fromSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromSelectedButtonActionPerformed(evt);
            }
        });

        stepNameLabel.setText(" ");

        undoButton.setText("Undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(stepNameLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(undoButton)
                            .addGap(18, 18, 18)
                            .addComponent(calculateButton))
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(toSelectedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(fromSelectedButton))
                                    .addGap(10, 10, 10)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(downButton)
                                        .addComponent(upButton)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(42, 42, 42)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(toSelectedButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fromSelectedButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(upButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calculateButton)
                    .addComponent(stepNameLabel)
                    .addComponent(undoButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(okButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void toSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toSelectedButtonActionPerformed
        boolean added = false;
        int index[] = this.variablesTable.getSelectedRows();
        for (int i : index) {
            VariableMu variable = this.model.getVariables().get(i);
            if (!this.selectedListModel.contains(variable)) {
                this.selectedListModel.addElement(variable);
                added = true;
            }
        }

        updateValues();
        if (added) {
            int selected;
            if (index.length < 1 || this.variablesTable.getRowCount() == index[index.length - 1] + 1) {
                selected = 0;
            } else {
                selected = index[index.length - 1] + 1;
            }
            this.variablesTable.getSelectionModel().setSelectionInterval(selected, selected);
        }
    }//GEN-LAST:event_toSelectedButtonActionPerformed

    private void fromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromSelectedButtonActionPerformed
        int[] index = this.selectedVariableList.getSelectedIndices();
        for (Object variable : this.selectedVariableList.getSelectedValuesList()) {
            this.selectedListModel.removeElement((VariableMu) variable);
        }
        if (this.selectedVariableList.getModel().getSize() > 0) {
            int selected;
            if (index.length != 1) {
                selected = 0;
            } else if (this.selectedListModel.size() == index[index.length - 1]) {
                selected = this.selectedListModel.size() - 1;
            } else {
                selected = index[index.length - 1];
            }

            this.selectedVariableList.setSelectedIndex(selected);
        }

        updateValues();
    }//GEN-LAST:event_fromSelectedButtonActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        getController().calculate();
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int index = this.selectedVariableList.getSelectedIndex();
        if (index > 0) {
            VariableMu variable = this.selectedListModel.remove(index);
            this.selectedListModel.add(index - 1, variable);
            this.selectedVariableList.setSelectedIndex(index - 1);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int index = this.selectedVariableList.getSelectedIndex();
        if (index > -1 && index < this.selectedListModel.getSize() - 1) {
            VariableMu variable = this.selectedListModel.remove(index);
            this.selectedListModel.add(index + 1, variable);
            this.selectedVariableList.setSelectedIndex(index + 1);
        }
    }//GEN-LAST:event_downButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        getController().undo();
    }//GEN-LAST:event_undoButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton fromSelectedButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField percentageTextField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JList selectedVariableList;
    private javax.swing.JLabel stepNameLabel;
    private javax.swing.JButton toSelectedButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton upButton;
    private javax.swing.JTable variablesTable;
    // End of variables declaration//GEN-END:variables
}
