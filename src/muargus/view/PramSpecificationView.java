/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.controller.PramSpecificationController;
import muargus.model.CodeInfo;
import muargus.model.MetadataMu;
import muargus.model.PramSpecification;
import muargus.model.PramVariableSpec;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class PramSpecificationView extends DialogBase {

    MetadataMu metadataMu;
    PramSpecificationController controller;
    PramSpecification model;
    private TableModel variablesTableModel;
    private TableModel codesTableModel;
    private final int[] variablesColumnWidth = {20, 30, 80};
    private final int[] codesColumnWidth = {30, 70, 30};

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
    }

    /**
     * 
     * @param metadataMu 
     */
    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
        this.model = this.metadataMu.getCombinations().getPramSpecification();
        this.controller.setModel(this.model);
        initializeData();
    }

    /**
     * 
     */
    public void initializeData() {
        this.pramOptionsPanel.setVisible(false); // this option is available for future options using global recode
        this.bandwidthSpinner.setEnabled(this.bandwidthCheckBox.isSelected());
        this.controller.makePramVariableSpecs();

        updateValues();
    }

    /**
     * 
     */
    public void updateValues() {
        updateVariablesTable();
        updateCodesTable();
        this.codesSlider.setValue(getSelectedCodeInfo().getPramProbability());
    }

    /**
     * 
     */
    public void updateVariablesTable() {
        this.controller.makeVariablesData();
        int selectedRow;
        if (this.variablesTable.getSelectedRowCount() > 0) {
            selectedRow = this.variablesTable.getSelectedRow();
        } else {
            selectedRow = 0;
        }

        this.variablesTableModel = new DefaultTableModel(this.model.getVariablesData(), this.model.getVariablesColumnNames()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.variablesTable.setModel(this.variablesTableModel);

        for (int i = 0; i < this.variablesColumnWidth.length; i++) {
            this.variablesTable.getColumnModel().getColumn(i).setMinWidth(this.variablesColumnWidth[i]);
            this.variablesTable.getColumnModel().getColumn(i).setPreferredWidth(this.variablesColumnWidth[i]);
        }
        this.variablesTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
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
    public CodeInfo getSelectedCodeInfo(){
        return getSelectedPramVariableSpec().getVariable().getCodeInfos().get(this.codesTable.getSelectedRow());
    }
    
    public CodeInfo getSelectedCodeInfo(VariableMu variable){
        return variable.getCodeInfos().get(this.codesTable.getSelectedRow());
    }

    /**
     * 
     */
    public void updateCodesTable() {
        int selectedRow;
        if (this.codesTable.getSelectedRowCount() > 0) {
            selectedRow = this.codesTable.getSelectedRow();
        } else {
            selectedRow = 0;
        }
        
        this.controller.makeCodesData(getSelectedPramVariableSpec().getVariable().getName());

        this.codesTableModel = new DefaultTableModel(
                getSelectedPramVariableSpec().getCodesData(), this.model.getCodesColumnNames()) {
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
        defaultProbabilitySpinner = new javax.swing.JSpinner();
        defaultProbabilityButton = new javax.swing.JButton();
        bandwidthPanel = new javax.swing.JPanel();
        bandwidthCheckBox = new javax.swing.JCheckBox();
        bandwidthSpinner = new javax.swing.JSpinner();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PRAM Specification");

        leftPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));

        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
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
        variablesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                variablesTableMouseClicked(evt);
            }
        });
        variablesTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                variablesTableKeyReleased(evt);
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
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Code", "Label", "Prob."
            }
        ));
        codesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                codesTableMouseClicked(evt);
            }
        });
        codesTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codesTableKeyReleased(evt);
            }
        });
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        defaultProbabilitySpinner.setModel(new javax.swing.SpinnerNumberModel(80, 0, 100, 1));

        defaultProbabilityButton.setText("<html>\nSet all codes to <br>\n<center>default</center>");
        defaultProbabilityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultProbabilityButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout defaultProbabilityPanelLayout = new javax.swing.GroupLayout(defaultProbabilityPanel);
        defaultProbabilityPanel.setLayout(defaultProbabilityPanelLayout);
        defaultProbabilityPanelLayout.setHorizontalGroup(
            defaultProbabilityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(defaultProbabilityPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(defaultProbabilityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(defaultProbabilitySpinner)
                    .addComponent(defaultProbabilityButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        defaultProbabilityPanelLayout.setVerticalGroup(
            defaultProbabilityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(defaultProbabilityPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(defaultProbabilitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        bandwidthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(5), Integer.valueOf(1), null, Integer.valueOf(1)));

        javax.swing.GroupLayout bandwidthPanelLayout = new javax.swing.GroupLayout(bandwidthPanel);
        bandwidthPanel.setLayout(bandwidthPanelLayout);
        bandwidthPanelLayout.setHorizontalGroup(
            bandwidthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bandwidthPanelLayout.createSequentialGroup()
                .addComponent(bandwidthCheckBox)
                .addGap(0, 28, Short.MAX_VALUE))
            .addGroup(bandwidthPanelLayout.createSequentialGroup()
                .addComponent(bandwidthSpinner)
                .addContainerGap())
        );
        bandwidthPanelLayout.setVerticalGroup(
            bandwidthPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bandwidthPanelLayout.createSequentialGroup()
                .addComponent(bandwidthCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bandwidthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(pramOptionsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(defaultProbabilityPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bandwidthPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(closeButton)
                        .addContainerGap())))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bandwidthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(closeButton)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.controller.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void bandwidthCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bandwidthCheckBoxStateChanged
        this.bandwidthSpinner.setEnabled(this.bandwidthCheckBox.isSelected());
        getSelectedPramVariableSpec().setUseBandwidth(this.bandwidthCheckBox.isSelected());
    }//GEN-LAST:event_bandwidthCheckBoxStateChanged

    private void variablesTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_variablesTableKeyReleased
        this.codesTable.getSelectionModel().setSelectionInterval(0, 0);
        updateValues();
    }//GEN-LAST:event_variablesTableKeyReleased

    private void variablesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_variablesTableMouseClicked
        this.codesTable.getSelectionModel().setSelectionInterval(0, 0);
        updateValues();
    }//GEN-LAST:event_variablesTableMouseClicked

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        getSelectedPramVariableSpec().setApplied(!getSelectedPramVariableSpec().isApplied());
        updateValues();
    }//GEN-LAST:event_applyButtonActionPerformed

    private void codesSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_codesSliderStateChanged
        for(VariableMu v: this.metadataMu.getVariables()){
            if(getSelectedPramVariableSpec().getVariable().equals(v)){
                VariableMu variable = v;
                getSelectedCodeInfo(variable).setPramProbability(this.codesSlider.getValue());
                break;
            }
        }
        
        updateValues();
    }//GEN-LAST:event_codesSliderStateChanged

    private void codesTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codesTableKeyReleased
        updateValues();
    }//GEN-LAST:event_codesTableKeyReleased

    private void codesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_codesTableMouseClicked
        updateValues();
    }//GEN-LAST:event_codesTableMouseClicked

    private void defaultProbabilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultProbabilityButtonActionPerformed
        int probability = (int) this.defaultProbabilitySpinner.getValue();
        for(CodeInfo c: getSelectedPramVariableSpec().getVariable().getCodeInfos()){
            c.setPramProbability(probability);
        }
        updateValues();
    }//GEN-LAST:event_defaultProbabilityButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CodesPanel;
    private javax.swing.JPanel MiddlePanel;
    private javax.swing.JButton applyButton;
    private javax.swing.JCheckBox bandwidthCheckBox;
    private javax.swing.JPanel bandwidthPanel;
    private javax.swing.JSpinner bandwidthSpinner;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane codesScrollPane;
    private javax.swing.JSlider codesSlider;
    private javax.swing.JTable codesTable;
    private javax.swing.JButton defaultProbabilityButton;
    private javax.swing.JPanel defaultProbabilityPanel;
    private javax.swing.JSpinner defaultProbabilitySpinner;
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
