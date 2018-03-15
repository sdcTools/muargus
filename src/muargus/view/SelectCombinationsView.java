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
import java.awt.Frame;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.HighlightTableCellRenderer;
import muargus.MuARGUS;
import muargus.VariableNameCellRenderer;
import muargus.controller.SelectCombinationsController;
import muargus.model.Combinations;
import muargus.model.TableMu;
import muargus.model.VariableMu;

/**
 * View class of the SelectCombinations screen.
 *
 * @author Statistics Netherlands
 */
public class SelectCombinationsView extends DialogBase<SelectCombinationsController> {

    private Combinations model;
    private DefaultListModel variablesListModel;
    private DefaultListModel variablesSelectedListModel;
    private TableModel tableModel;
    private final Frame parent;
    // gives the width of column 1, 2 and the final value is the width of all the other columns
    private final int[] columnWidth = {45, 45, 65};

    /**
     * Creates new form SelectCombinationsView
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param controller the controller of this view.
     */
    public SelectCombinationsView(java.awt.Frame parent, boolean modal, SelectCombinationsController controller) {
        super(parent, modal, controller);
        this.parent = parent;
        initComponents();
        setLocationRelativeTo(null);
        this.variablesList.setCellRenderer(new VariableNameCellRenderer());
        this.variablesSelectedList.setCellRenderer(new VariableNameCellRenderer());
    }

    /**
     * Sets the model.
     *
     * @param model Model class of the SelectCombinations screen.
     */
    public void setModel(Combinations model) {
        this.model = model;
        updateValues();
    }

    /**
     * Fills the selecCombinationsScreen with it's default values
     */
    @Override
    public void initializeData() {
        // make listModels and add the variables that are categorical
        this.variablesListModel = new DefaultListModel<>();
        this.variablesSelectedListModel = new DefaultListModel<>();
        for (VariableMu variable : getMetadata().getVariables()) {
            if (variable.isCategorical()) {
                this.variablesListModel.addElement(variable);
            }
        }
        this.variablesList.setModel(this.variablesListModel);

        this.variablesSelectedList.setModel(this.variablesSelectedListModel);
        if (this.variablesListModel.getSize() > 0) {
            this.variablesList.setSelectedIndex(0);
        }

        // set the default values and the size of the first two colums
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.table.setDefaultRenderer(Object.class, new HighlightTableCellRenderer());
        this.table.setDefaultRenderer(Integer.class, new HighlightTableCellRenderer());
    }

    /**
     * Updates the table by filling it with the array of tables.
     */
    private void updateValues() {
        this.thresholdTextField.setText(Integer.toString(this.model.getThreshold()));
        // gets the tables from Combinations and adds these to a double array, containing the data
        ArrayList<TableMu> tables = this.model.getTables();
        Object[][] data = new Object[this.model.getTables().size()][this.model.getNumberOfColumns()];

        int index = 0;
        for (TableMu t : tables) {
            data[index] = t.getTableData();
            index++;
        }

        // fills the ArrayList containing the column names with the appropriate number of column names.
        ArrayList<String> columnNames = new ArrayList<>(Arrays.asList("Risk", "Thres.", "Var 1"));
        if (this.model.getNumberOfColumns() > 3) {
            for (int i = 2; i <= this.model.getNumberOfColumns() - 2; i++) {
                columnNames.add("Var " + i);
            }
        }

        // fill the table with the data and column names.
        this.tableModel = new DefaultTableModel(data, columnNames.toArray()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Class getColumnClass(int i) {
                return (i == 1 ? Integer.class : String.class);
            }
        };
        this.table.setModel(this.tableModel);

        // sets the size of each column
        for (int i = 0; i < this.table.getColumnModel().getColumnCount(); i++) {
            int colWidth;
            if (i > 2) {
                colWidth = this.columnWidth[2];
            } else {
                colWidth = this.columnWidth[i];
            }
            this.table.getColumnModel().getColumn(i).setMinWidth(colWidth);
            this.table.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
        }

        if (tables.size() == 1) {
            this.table.getSelectionModel().setSelectionInterval(0, 0);
        }
    }

    /**
     * Sets the progress of the progressbar.
     *
     * @param progress Integer indicating the progress made.
     */
    @Override
    public void setProgress(int progress) {
        this.progressbar.setValue(progress);
    }

    /**
     * Shows the progress step name.
     *
     * @param value String containing the progress step name.
     */
    @Override
    public void showStepName(String value) {
        this.progressLabel.setText(value);
    }

    /**
     * Enables/Disables the calculate tables button.
     *
     * @param enabled Boolean indicating whether the calculate tables button
     * needs to be enabled.
     */
    public void enableCalculateTables(boolean enabled) {
        this.calculateTablesButton.setEnabled(enabled);
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
        variablesList = new javax.swing.JList();
        variablesSelectedScrollPane = new javax.swing.JScrollPane();
        variablesSelectedList = new javax.swing.JList();
        moveToSelectedButton = new javax.swing.JButton();
        removeFromSelectedButton = new javax.swing.JButton();
        removeAllFromSelectedButton = new javax.swing.JButton();
        addRowButton = new javax.swing.JButton();
        removeRowButton = new javax.swing.JButton();
        kLabel = new javax.swing.JLabel();
        thresholdTextField = new javax.swing.JTextField();
        automaticSpecificationButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        setTableRiskModelButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        calculateTablesButton = new javax.swing.JButton();
        progressbar = new javax.swing.JProgressBar();
        tablesScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        progressLabel = new javax.swing.JLabel();
        setKAnonButton = new javax.swing.JButton();
        thresholdLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Specify Combinations");
        setMinimumSize(new java.awt.Dimension(715, 415));
        setPreferredSize(new java.awt.Dimension(715, 415));
        setSize(new java.awt.Dimension(0, 0));

        variablesList.setName(""); // NOI18N
        variablesScrollPane.setViewportView(variablesList);

        variablesSelectedScrollPane.setViewportView(variablesSelectedList);

        moveToSelectedButton.setText("→");
        moveToSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToSelectedButtonActionPerformed(evt);
            }
        });

        removeFromSelectedButton.setText("←");
        removeFromSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromSelectedButtonActionPerformed(evt);
            }
        });

        removeAllFromSelectedButton.setText("<<");
        removeAllFromSelectedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllFromSelectedButtonActionPerformed(evt);
            }
        });

        addRowButton.setText("→");
        addRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowButtonActionPerformed(evt);
            }
        });

        removeRowButton.setText("←");
        removeRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRowButtonActionPerformed(evt);
            }
        });

        kLabel.setText("k =");

        thresholdTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        thresholdTextField.setToolTipText("<html>Threshold k:<br>\n&le; k is unsafe according to traditional approach<br>\n&le; k is unsafe according to (k+1)-anonymity");
        thresholdTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                thresholdTextFieldCaretUpdate(evt);
            }
        });
        thresholdTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                thresholdTextFieldMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                thresholdTextFieldMouseExited(evt);
            }
        });

        automaticSpecificationButton.setText("Automatic specification of combinations");
        automaticSpecificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticSpecificationButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.setToolTipText("Clear list of combinations");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        setTableRiskModelButton.setText("Set combination for Risk model");
        setTableRiskModelButton.setToolTipText(""); // NOI18N
        setTableRiskModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTableRiskModelButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        calculateTablesButton.setText("Calculate tables");
        calculateTablesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateTablesButtonActionPerformed(evt);
            }
        });

        tablesScrollPane.setMinimumSize(new java.awt.Dimension(275, 402));
        tablesScrollPane.setName(""); // NOI18N
        tablesScrollPane.setPreferredSize(new java.awt.Dimension(275, 402));

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Risk", "Thres.", "Var 1", "Var 2", "Var 3"
            }
        ));
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.getTableHeader().setReorderingAllowed(false);
        tablesScrollPane.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(20);
            table.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        setKAnonButton.setText("Toggle combinations for (k+1)-anonymity");
        setKAnonButton.setToolTipText("<html>\n&le; k is unsafe according to traditional approach<br>\n&le; k is unsafe according to (k+1)-anonymity");
        setKAnonButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setKAnonButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setKAnonButtonMouseExited(evt);
            }
        });
        setKAnonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setKAnonButtonActionPerformed(evt);
            }
        });

        thresholdLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        thresholdLabel.setText("Threshold");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(81, 81, 81)
                                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(removeFromSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(moveToSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(removeAllFromSelectedButton))
                                    .addGap(12, 12, 12)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(19, 19, 19)
                                            .addComponent(kLabel)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(thresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                .addComponent(thresholdLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(variablesSelectedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                            .addGap(12, 12, 12)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(addRowButton)
                                                .addComponent(removeRowButton)))))
                                .addComponent(automaticSpecificationButton, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(setTableRiskModelButton))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(calculateTablesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(setKAnonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tablesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(variablesScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(setKAnonButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(334, 334, 334)
                                .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(moveToSelectedButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeFromSelectedButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeAllFromSelectedButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(addRowButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeRowButton))
                                    .addComponent(variablesSelectedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(thresholdLabel)
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(thresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(kLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(automaticSpecificationButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(setTableRiskModelButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cancelButton)
                                    .addComponent(calculateTablesButton))
                                .addComponent(progressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateTablesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateTablesButtonActionPerformed
        try {
            if (this.model.getTables().isEmpty()) {
                showMessage("No combinations specified");
            } else {
                getController().calculateTables();
            }
        } catch (ArgusException ex) {
            showErrorMessage(ex);
        }
    }//GEN-LAST:event_calculateTablesButtonActionPerformed

    private void moveToSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToSelectedButtonActionPerformed
        int[] index = this.variablesList.getSelectedIndices();
        Object[] variableMu = this.variablesList.getSelectedValuesList().toArray();

        // checks for all variables if they are already in the variablesSelectedList and if not, adds them.
        for (Object variable : variableMu) {
            boolean variableAlreadyExists = false;
            for (Object o : this.variablesSelectedListModel.toArray()) {
                if (variable.equals(o)) {
                    variableAlreadyExists = true;
                }
            }
            if (!variableAlreadyExists) {
                this.variablesSelectedListModel.add(this.variablesSelectedListModel.getSize(), (VariableMu) variable);
            }
        }

        this.variablesList.setSelectedIndex(index[index.length - 1] + 1);
    }//GEN-LAST:event_moveToSelectedButtonActionPerformed

    private void removeFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromSelectedButtonActionPerformed
        for (Object o : this.variablesSelectedList.getSelectedValuesList()) {
            this.variablesSelectedListModel.removeElement(o);
        }
        this.variablesSelectedList.setSelectedIndex(0);
    }//GEN-LAST:event_removeFromSelectedButtonActionPerformed

    private void removeAllFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllFromSelectedButtonActionPerformed
        this.variablesSelectedListModel.removeAllElements();
        this.variablesList.setSelectedIndex(0);
    }//GEN-LAST:event_removeAllFromSelectedButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        /* checks if the number of variables is not bigger than the maximum number  
         of dimensions and if the threshold is valid. */
        if (this.variablesSelectedListModel.size() > MuARGUS.MAXDIMS) {
            JOptionPane.showMessageDialog(this, String.format("Cannot add more than %d variables", MuARGUS.MAXDIMS));
        } else if (!getController().validThreshold(this.thresholdTextField.getText())) {
            JOptionPane.showMessageDialog(this, "The threshold is not valid, please enter a positive integer");
        } else {
            /* copy the selected variables into a VariableMu array and make a 
             TableMu instance containing all selected variables and the threshold*/
            VariableMu[] variableMu = new VariableMu[this.variablesSelectedListModel.size()];
            this.variablesSelectedListModel.copyInto(variableMu);
            TableMu tableMuNew = new TableMu();
            tableMuNew.setThreshold(this.model.getThreshold());
            tableMuNew.getVariables().addAll(Arrays.asList(variableMu));

            boolean add = true;
            // only check for double tables when then number of tables is between 0 and the MaximumSizeBeforeUserConfirmation
            if (this.model.getNumberOfRows() > 0 && this.model.getNumberOfRows() < this.model.getMaximumSizeBeforeUserConfirmation()) {
                for (int i = 0; i < this.model.getNumberOfRows(); i++) {
                    TableMu tableMuOld = this.model.getTables().get(i);
                    add = getController().compareRows(tableMuNew, tableMuOld);
                    if (!add) {
                        if (tableMuOld.isRiskModel()) {
                            JOptionPane.showMessageDialog(this,
                                    "The new table overlaps with a BIR table and cannot be added");
                        }
                        break;
                    }
                }
            }

            /* If the new table is valid, it will be added. If the table is not valid and
             the riskmodel (BIR) is not used, it means that new table already excists 
             and thus the list of selected variables will be emptied.*/
            if (add) {
                if (this.variablesSelectedListModel.size() > 0) {
                    this.variablesSelectedListModel.removeAllElements();
                    this.model.addTable(tableMuNew);
                }
            } else if (!this.model.isRiskModel()) {
                this.variablesSelectedListModel.removeAllElements();
            }

            updateValues();
            this.table.getSelectionModel().setSelectionInterval(this.model.getNumberOfRows() - 1, this.model.getNumberOfRows() - 1);
            this.table.scrollRectToVisible(new Rectangle(this.table.getCellRect(this.model.getNumberOfRows() - 1, 0, true)));
        }
    }//GEN-LAST:event_addRowButtonActionPerformed

    private void removeRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRowButtonActionPerformed
        if (this.model.getNumberOfRows() > 0) {
            try {
                int[] selectedRows = this.table.getSelectedRows();
                for (int i = 0; i < selectedRows.length; i++) {
                    selectedRows[i] = this.table.convertRowIndexToModel(selectedRows[i]);
                }
                this.variablesSelectedListModel.removeAllElements();
                ArrayList<VariableMu> variableMu = this.model.getTables().get(selectedRows[selectedRows.length - 1]).getVariables();
                for (int j = 0; j < variableMu.size(); j++) {
                    this.variablesSelectedListModel.add(j, variableMu.get(j));
                }
                for (int i = selectedRows.length - 1; i > -1; i--) {
                    this.model.removeTable(selectedRows[i]);
                }
            } catch (Exception e) {
                this.model.removeTable(this.model.getNumberOfRows());
            }
            if (this.model.getNumberOfRows() == 0) {
                getController().clear();
                updateValues();
            }
        }
        updateValues();
        this.table.getSelectionModel().setSelectionInterval(0, 0);
    }//GEN-LAST:event_removeRowButtonActionPerformed

    private void automaticSpecificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticSpecificationButtonActionPerformed
        // checks if there are tables and askes if they need to be removed
        int numberOfOldTables = this.model.getNumberOfRows();
        if (this.model.getNumberOfRows() > 0) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to delete the current set of tables?", "Mu Argus",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                getController().clear();
                updateValues();
            }
        }

        // make an array for each idLevel (0-5)
        ArrayList<ArrayList<VariableMu>> variables = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ArrayList<VariableMu> array = new ArrayList<>();
            variables.add(array);
        }

        // fill the appropriate array according to the idLevels of the variables
        for (int i = 0; i < this.variablesListModel.getSize(); i++) {
            VariableMu variable = (VariableMu) this.variablesListModel.getElementAt(i);
            variables.get(variable.getIdLevel()).add(variable);
        }

        // add all variables with an ID-level higher than 0 to the arrayList of variables.
        int numberOfLevels = 0; // the number of idLevels with at least one variable
        ArrayList<VariableMu> allValidVariables = new ArrayList<>();
        for (int i = 1; i < variables.size(); i++) {
            allValidVariables.addAll(variables.get(i));
            if (variables.get(i).size() > 0) {
                numberOfLevels++;
            }
        }

        int numberOfVariables = allValidVariables.size();

        GenerateAutomaticTables generateAutomaticTables = new GenerateAutomaticTables(this.parent, true, this.model, numberOfVariables, getMetadata());
        generateAutomaticTables.setVisible(true);

        if (generateAutomaticTables.isInputValid()) {
            if (generateAutomaticTables.isMakeUpToDimensionRadioButton()) {
                int dimensions = generateAutomaticTables.getDimensions();
                getController().calculateTablesForDimensions(allValidVariables, dimensions);
            } else if (generateAutomaticTables.isUseIdentificationLevelRadioButton()) {
                getController().calculateTablesForID(numberOfLevels, variables, allValidVariables);
            }
        }

        // removes double tables
        if (numberOfOldTables < this.model.getMaximumNumberOfTables()) {
            for (int i = 0; i < numberOfOldTables; i++) {
                for (int j = this.model.getNumberOfRows() - 1; j >= numberOfOldTables; j--) {
                    if (!getController().compareRows(this.model.getTables().get(i), this.model.getTables().get(j))) {
                        this.model.removeTable(this.model.getTables().get(j));
                    }
                }
            }
        }

        boolean risk = this.model.isRiskModel();
        if (risk && numberOfOldTables < this.model.getMaximumNumberOfTables()) {
            getController().removeTableRiskModel(getController().getListOfRemovedTables());
        }

        updateValues();
        this.table.getSelectionModel().setSelectionInterval(0, 0);
    }//GEN-LAST:event_automaticSpecificationButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        getController().clear();
        updateValues();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void setTableRiskModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTableRiskModelButtonActionPerformed
        if (this.model.getTables().size() > 0) {
            
                int index = this.table.getSelectedRow();
                if(index < 0){
                    showMessage("No combination is selected");
                    return;
                }
                index = this.table.convertRowIndexToModel(index);
                TableMu tableMu = this.model.getTables().get(index);
                if (!getController().weightVariableExists()) {
                    showMessage("No weight variable has been specified, so the risk-model cannot be applied");
                    return;
                }
                tableMu.setRiskModel(!tableMu.isRiskModel());
                //tableMu.setKAnon(false);

                if (tableMu.isRiskModel()) {  //The table is added to the risk model
                    ArrayList<TableMu> toBeRemovedTables = getController().getListOfRemovedTables();
                    getController().overlappingTables(toBeRemovedTables, tableMu);
                    getController().removeTableRiskModel(toBeRemovedTables);
                }

                updateValues();
                this.table.getSelectionModel().setSelectionInterval(index, index);

        }
    }//GEN-LAST:event_setTableRiskModelButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        getController().clear();
        updateValues();
        getController().cancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void thresholdTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_thresholdTextFieldCaretUpdate
        try {
            this.model.setThreshold(Integer.parseInt(this.thresholdTextField.getText()));
        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_thresholdTextFieldCaretUpdate

    private void setKAnonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setKAnonButtonActionPerformed
        boolean SetkAnon;
        if (this.model.getTables().size() > 0) {
                int index = this.table.getSelectedRow();
                if (index < 0){
                    SetkAnon = this.model.getTables().get(0).isKAnon();
                }else{
                    SetkAnon = this.model.getTables().get(index).isKAnon();
                }
                for (TableMu tableMu : this.model.getTables()){
                    tableMu.setKAnon(!SetkAnon);
                    tableMu.setRiskModel(false);
                }

                updateValues();
                this.table.getSelectionModel().setSelectionInterval(index, index);
        }
    }//GEN-LAST:event_setKAnonButtonActionPerformed

    private void setKAnonButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setKAnonButtonMouseEntered
        ToolTipManager.sharedInstance().setInitialDelay((int) 10);
        ToolTipManager.sharedInstance().setDismissDelay((int) TimeUnit.MINUTES.toMillis(1));
    }//GEN-LAST:event_setKAnonButtonMouseEntered

    private void setKAnonButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setKAnonButtonMouseExited
        ToolTipManager.sharedInstance().setDismissDelay(ToolTipManager.sharedInstance().getDismissDelay());
    }//GEN-LAST:event_setKAnonButtonMouseExited

    private void thresholdTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thresholdTextFieldMouseEntered
        ToolTipManager.sharedInstance().setDismissDelay((int) TimeUnit.MINUTES.toMillis(1));
    }//GEN-LAST:event_thresholdTextFieldMouseEntered

    private void thresholdTextFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_thresholdTextFieldMouseExited
        ToolTipManager.sharedInstance().setDismissDelay(ToolTipManager.sharedInstance().getDismissDelay());
    }//GEN-LAST:event_thresholdTextFieldMouseExited
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowButton;
    private javax.swing.JButton automaticSpecificationButton;
    private javax.swing.JButton calculateTablesButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel kLabel;
    private javax.swing.JButton moveToSelectedButton;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JProgressBar progressbar;
    private javax.swing.JButton removeAllFromSelectedButton;
    private javax.swing.JButton removeFromSelectedButton;
    private javax.swing.JButton removeRowButton;
    private javax.swing.JButton setKAnonButton;
    private javax.swing.JButton setTableRiskModelButton;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tablesScrollPane;
    private javax.swing.JLabel thresholdLabel;
    private javax.swing.JTextField thresholdTextField;
    private javax.swing.JList variablesList;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JList variablesSelectedList;
    private javax.swing.JScrollPane variablesSelectedScrollPane;
    // End of variables declaration//GEN-END:variables
}