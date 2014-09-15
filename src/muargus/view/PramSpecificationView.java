/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.controller.PramSpecificationController;
import muargus.model.CodeInfo;
import muargus.model.PramSpecification;
import muargus.model.PramVariableSpec;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class PramSpecificationView extends DialogBase {

    PramSpecificationController controller;
    PramSpecification model;
    private TableModel codesTableModel;
    private final int[] variablesColumnWidth = {20, 30, 80};
    private final int[] codesColumnWidth = {30, 70, 30};
    private int selectedRow;

    /**
     *
     * @param parent
     * @param modal
     * @param controller
     */
    public PramSpecificationView(java.awt.Frame parent, boolean modal, PramSpecificationController controller) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.controller = controller;
        this.variablesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.codesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     *
     */
    @Override
    public void initializeData() {
        this.model = getMetadata().getCombinations().getPramSpecification();
        this.pramOptionsPanel.setVisible(false); // this option is available for future options using global recode
        this.bandwidthComboBox.setEnabled(this.bandwidthCheckBox.isSelected());
        this.controller.makePramVariableSpecs();
        this.controller.setBandwidth();

        makeVariablesTable();
        variablesSelectionChanged();
        this.codesSlider.setValue(getSelectedCodeInfo().getPramProbability());
    }

    /**
     *
     */
    public void makeVariablesTable() {
        this.controller.makeVariablesData();

        TableModel variablesTableModel = new DefaultTableModel(this.model.getVariablesData(), this.model.getVariablesColumnNames()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.variablesTable.setModel(variablesTableModel);

        for (int i = 0; i < this.variablesColumnWidth.length; i++) {
            this.variablesTable.getColumnModel().getColumn(i).setMinWidth(this.variablesColumnWidth[i]);
            this.variablesTable.getColumnModel().getColumn(i).setPreferredWidth(this.variablesColumnWidth[i]);
        }

        this.variablesTable.getSelectionModel().setSelectionInterval(0, 0);
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
     *
     * @return
     */
    public PramVariableSpec getSelectedPramVariableSpec() {
        return this.controller.getSelectedPramVarSpec(
                (String) this.variablesTable.getValueAt(this.variablesTable.getSelectedRow(), 2));
    }

    /**
     *
     * @return
     */
    public CodeInfo getSelectedCodeInfo() {
        return getSelectedPramVariableSpec().getVariable().getCodeInfos().get(this.selectedRow);
    }

    public CodeInfo getSelectedCodeInfo(VariableMu variable) {
        return variable.getCodeInfos().get(this.selectedRow);
    }

    /**
     *
     */
    public void updateCodesTable() {
        if (this.codesTable.getSelectedRowCount() > 0) {
            selectedRow = this.codesTable.getSelectedRow();
        } else {
            selectedRow = 0;
        }

        String[][] codesData = this.controller.getCodesData(getSelectedPramVariableSpec().getVariable().getName());

        this.codesTableModel = new DefaultTableModel(codesData, this.model.getCodesColumnNames()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.codesTable.setModel(this.codesTableModel);

        for (int i = 0; i < this.codesColumnWidth.length; i++) {
            this.codesTable.getColumnModel().getColumn(i).setMinWidth(this.codesColumnWidth[i]);
            this.codesTable.getColumnModel().getColumn(i).setPreferredWidth(this.codesColumnWidth[i]);
        }
        this.codesTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);

        this.codesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    codesSelectionChanged();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pramOptionsButtonGroup = new javax.swing.ButtonGroup();
        leftPanel = new javax.swing.JPanel();
        variablesScrollPane = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        MiddlePanel = new javax.swing.JPanel();
        CodesPanel = new javax.swing.JPanel();
        codesScrollPane = new javax.swing.JScrollPane();
        codesTable = new javax.swing.JTable();
        codesSlider = new javax.swing.JSlider();
        applyButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        pramOptionsPanel = new javax.swing.JPanel();
        individualChancesRadioButton = new javax.swing.JRadioButton();
        globalRecodeRadioButton = new javax.swing.JRadioButton();
        defaultProbabilityPanel = new javax.swing.JPanel();
        defaultProbabilityButton = new javax.swing.JButton();
        defaultProbabilityComboBox = new javax.swing.JComboBox();
        bandwidthPanel = new javax.swing.JPanel();
        bandwidthCheckBox = new javax.swing.JCheckBox();
        bandwidthComboBox = new javax.swing.JComboBox();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PRAM Specification");

        leftPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));

        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "F", "BW", "Variable"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        variablesScrollPane.setViewportView(variablesTable);
        if (variablesTable.getColumnModel().getColumnCount() > 0) {
            variablesTable.getColumnModel().getColumn(0).setPreferredWidth(8);
            variablesTable.getColumnModel().getColumn(1).setPreferredWidth(8);
        }

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 208, Short.MAX_VALUE)
            .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(leftPanelLayout.createSequentialGroup()
                    .addGap(24, 24, 24)
                    .addComponent(variablesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addGap(24, 24, 24)))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(leftPanelLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        CodesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Codes"));

        codesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Code", "Label", "Prob."
            }
        ));
        codesScrollPane.setViewportView(codesTable);
        if (codesTable.getColumnModel().getColumnCount() > 0) {
            codesTable.getColumnModel().getColumn(0).setPreferredWidth(12);
            codesTable.getColumnModel().getColumn(1).setPreferredWidth(20);
            codesTable.getColumnModel().getColumn(2).setPreferredWidth(12);
        }

        codesSlider.setMajorTickSpacing(100);
        codesSlider.setMinorTickSpacing(5);
        codesSlider.setPaintTicks(true);
        codesSlider.setToolTipText("");
        codesSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                codesSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout CodesPanelLayout = new javax.swing.GroupLayout(CodesPanel);
        CodesPanel.setLayout(CodesPanelLayout);
        CodesPanelLayout.setHorizontalGroup(
            CodesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CodesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(CodesPanelLayout.createSequentialGroup()
                        .addComponent(codesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        CodesPanelLayout.setVerticalGroup(
            CodesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(codesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(codesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        undoButton.setText("Undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MiddlePanelLayout = new javax.swing.GroupLayout(MiddlePanel);
        MiddlePanel.setLayout(MiddlePanelLayout);
        MiddlePanelLayout.setHorizontalGroup(
            MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(applyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(undoButton))
            .addComponent(CodesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MiddlePanelLayout.setVerticalGroup(
            MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CodesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyButton)
                    .addComponent(undoButton))
                .addContainerGap())
        );

        pramOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("PRAM Options"));

        individualChancesRadioButton.setSelected(true);
        individualChancesRadioButton.setText("Individual chances");

        globalRecodeRadioButton.setText("<html>\nBased on Global <br>Recode");

        javax.swing.GroupLayout pramOptionsPanelLayout = new javax.swing.GroupLayout(pramOptionsPanel);
        pramOptionsPanel.setLayout(pramOptionsPanelLayout);
        pramOptionsPanelLayout.setHorizontalGroup(
            pramOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pramOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pramOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(individualChancesRadioButton)
                    .addComponent(globalRecodeRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pramOptionsPanelLayout.setVerticalGroup(
            pramOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pramOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(individualChancesRadioButton)
                .addGap(18, 18, 18)
                .addComponent(globalRecodeRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        defaultProbabilityPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Default Probability"));

        defaultProbabilityButton.setText("<html>\nSet all codes to <br>\n<center>default</center>");
        defaultProbabilityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultProbabilityButtonActionPerformed(evt);
            }
        });

        String[] numbers = new String[101];
        for(int i = 0; i< numbers.length; i++){
            numbers[i] = Integer.toString(i);
        }
        defaultProbabilityComboBox.setModel(new javax.swing.DefaultComboBoxModel(numbers)
        );
        defaultProbabilityComboBox.getModel().setSelectedItem(numbers[80]);

        javax.swing.GroupLayout defaultProbabilityPanelLayout = new javax.swing.GroupLayout(defaultProbabilityPanel);
        defaultProbabilityPanel.setLayout(defaultProbabilityPanelLayout);
        defaultProbabilityPanelLayout.setHorizontalGroup(
            defaultProbabilityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(defaultProbabilityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(defaultProbabilityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(defaultProbabilityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(defaultProbabilityButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        defaultProbabilityPanelLayout.setVerticalGroup(
            defaultProbabilityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(defaultProbabilityPanelLayout.createSequentialGroup()
                .addComponent(defaultProbabilityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(defaultProbabilityButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bandwidthPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("BandWidth"));

        bandwidthCheckBox.setText("Use bandwidth");
        bandwidthCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bandwidthCheckBoxStateChanged(evt);
            }
        });

        bandwidthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout bandwidthPanelLayout = new javax.swing.GroupLayout(bandwidthPanel);
        bandwidthPanel.setLayout(bandwidthPanelLayout);
        bandwidthPanelLayout.setHorizontalGroup(
            bandwidthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bandwidthPanelLayout.createSequentialGroup()
                .addComponent(bandwidthCheckBox)
                .addGap(0, 28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bandwidthPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bandwidthComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        bandwidthPanelLayout.setVerticalGroup(
            bandwidthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bandwidthPanelLayout.createSequentialGroup()
                .addComponent(bandwidthCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bandwidthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );

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
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MiddlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pramOptionsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(defaultProbabilityPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(closeButton)
                            .addContainerGap()))
                    .addComponent(bandwidthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MiddlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pramOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(defaultProbabilityPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bandwidthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeButton)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.controller.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void bandwidthCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bandwidthCheckBoxStateChanged
        this.bandwidthComboBox.setEnabled(this.bandwidthCheckBox.isSelected());
        this.model.setUseBandwidth(this.bandwidthCheckBox.isSelected());
    }//GEN-LAST:event_bandwidthCheckBoxStateChanged

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        if (!this.controller.areAllProbabilitiesZero(getSelectedPramVariableSpec())) {
            getSelectedPramVariableSpec().setApplied(true);
            getSelectedPramVariableSpec().setBandwidth(Integer.parseInt((String) this.bandwidthComboBox.getSelectedItem()));
            getSelectedPramVariableSpec().setUseBandwidth(this.bandwidthCheckBox.isSelected());
            int selected = this.variablesTable.getSelectedRow();
            this.variablesTable.setValueAt(getSelectedPramVariableSpec().getAppliedText(), selected, 0);
            this.variablesTable.setValueAt(getSelectedPramVariableSpec().getBandwidthText(), selected, 1);
            this.controller.apply(getSelectedPramVariableSpec());
        } else {
            String message = "All probabilities are zero";
            JOptionPane.showMessageDialog(null, message);
        }
    }//GEN-LAST:event_applyButtonActionPerformed

    private void codesSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_codesSliderStateChanged
        for (VariableMu v : getMetadata().getVariables()) {
            if (getSelectedPramVariableSpec().getVariable().equals(v)) {
                getSelectedCodeInfo(v).setPramProbability(this.codesSlider.getValue());
                break;
            }
        }

        this.codesTable.setValueAt(this.codesSlider.getValue(), this.codesTable.getSelectedRow(), 2);
    }//GEN-LAST:event_codesSliderStateChanged

    private void defaultProbabilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultProbabilityButtonActionPerformed
        setProbability(this.defaultProbabilityComboBox.getSelectedIndex());
    }//GEN-LAST:event_defaultProbabilityButtonActionPerformed

    public void setProbability(int probability){
        for (CodeInfo c : getSelectedPramVariableSpec().getVariable().getCodeInfos()) {
            c.setPramProbability(probability);
        }

        for (int i = 0; i < this.codesTableModel.getRowCount(); i++) {
            this.codesTable.setValueAt(Integer.toString(probability), i, 2);
        }
        this.codesSlider.setValue(getSelectedCodeInfo().getPramProbability());
    }
    
    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        if (getSelectedPramVariableSpec().isApplied()) {
            getSelectedPramVariableSpec().setApplied(false);
            getSelectedPramVariableSpec().setUseBandwidth(false);
            int selected = this.variablesTable.getSelectedRow();
            this.variablesTable.setValueAt("", selected, 0);
            this.variablesTable.setValueAt("", selected, 1);
            setProbability(0);
            //this.controller.undo(getSelectedPramVariableSpec());
        }
    }//GEN-LAST:event_undoButtonActionPerformed

    public void variablesSelectionChanged() {
        this.codesTable.getSelectionModel().setSelectionInterval(0, 0);
        this.selectedRow = 0;
        int value = getSelectedPramVariableSpec().getBandwidth();
        int max = getSelectedPramVariableSpec().getVariable().getCodeInfos().size() - getSelectedPramVariableSpec().getVariable().getNumberOfMissings();
        if (value > max) {
            value = max;
        }
        String[] numbers = new String[max];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.toString(i + 1);
        }
        this.bandwidthComboBox.setModel(new javax.swing.DefaultComboBoxModel(numbers));
        this.bandwidthComboBox.getModel().setSelectedItem(numbers[value - 1]);
        this.codesSlider.setValue(getSelectedCodeInfo().getPramProbability());

        updateCodesTable();
    }

    private void codesSelectionChanged() {
        if (this.codesTable.getSelectedRow() >= 0) {
            this.selectedRow = this.codesTable.getSelectedRow();
        }
        this.codesSlider.setValue(getSelectedCodeInfo().getPramProbability());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CodesPanel;
    private javax.swing.JPanel MiddlePanel;
    private javax.swing.JButton applyButton;
    private javax.swing.JCheckBox bandwidthCheckBox;
    private javax.swing.JComboBox bandwidthComboBox;
    private javax.swing.JPanel bandwidthPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane codesScrollPane;
    private javax.swing.JSlider codesSlider;
    private javax.swing.JTable codesTable;
    private javax.swing.JButton defaultProbabilityButton;
    private javax.swing.JComboBox defaultProbabilityComboBox;
    private javax.swing.JPanel defaultProbabilityPanel;
    private javax.swing.JRadioButton globalRecodeRadioButton;
    private javax.swing.JRadioButton individualChancesRadioButton;
    private javax.swing.JPanel leftPanel;
    private javax.swing.ButtonGroup pramOptionsButtonGroup;
    private javax.swing.JPanel pramOptionsPanel;
    private javax.swing.JButton undoButton;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JTable variablesTable;
    // End of variables declaration//GEN-END:variables
}
