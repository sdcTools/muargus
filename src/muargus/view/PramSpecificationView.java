/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.controller.PramSpecificationController;
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

    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
        this.model = this.metadataMu.getCombinations().getPramSpecification();
        this.controller.setModel(this.model);
        initializeData();
    }

    public void initializeData() {
        this.pramOptionsPanel.setVisible(false); // this option is available for future options using global recode
        this.bandwidthSpinner.setEnabled(this.bandwidthCheckBox.isSelected());
        updateValues();
    }

    public void updateValues() {

    }

    public void updateVariablesTable() {
        this.variablesTableModel = new DefaultTableModel(this.model.getVariablesData(), this.model.getColumnNames()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.variablesTable.setModel(this.variablesTableModel);
    }
    
    public void updateCodesTable(){
        PramVariableSpec selected = this.controller.getSelectedVariable(
                (String) this.variablesTable.getValueAt(this.variablesTable.getSelectedRow(), 2));
        
        this.codesTableModel = new DefaultTableModel(selected.getCodesData(), this.model.getColumnNames()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.variablesTable.setModel(this.codesTableModel);
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
        variablesScrollPane = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        variablesLabel = new javax.swing.JLabel();
        MiddlePanel = new javax.swing.JPanel();
        CodesPanel = new javax.swing.JPanel();
        codesScrollPane = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
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
        variablesScrollPane.setViewportView(variablesTable);
        if (variablesTable.getColumnModel().getColumnCount() > 0) {
            variablesTable.getColumnModel().getColumn(0).setPreferredWidth(8);
            variablesTable.getColumnModel().getColumn(1).setPreferredWidth(8);
        }

        variablesLabel.setText("Variables:");

        CodesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Codes"));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        codesScrollPane.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(12);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(20);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(12);
        }

        codesSlider.setMajorTickSpacing(100);
        codesSlider.setMinorTickSpacing(5);
        codesSlider.setPaintTicks(true);
        codesSlider.setToolTipText("");

        javax.swing.GroupLayout CodesPanelLayout = new javax.swing.GroupLayout(CodesPanel);
        CodesPanel.setLayout(CodesPanelLayout);
        CodesPanelLayout.setHorizontalGroup(
            CodesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CodesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CodesPanelLayout.createSequentialGroup()
                        .addComponent(codesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(13, Short.MAX_VALUE))
                    .addComponent(codesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        CodesPanelLayout.setVerticalGroup(
            CodesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CodesPanelLayout.createSequentialGroup()
                .addComponent(codesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(codesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        applyButton.setText("Apply");

        undoButton.setText("Undo");

        javax.swing.GroupLayout MiddlePanelLayout = new javax.swing.GroupLayout(MiddlePanel);
        MiddlePanel.setLayout(MiddlePanelLayout);
        MiddlePanelLayout.setHorizontalGroup(
            MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addComponent(CodesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(applyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(undoButton)
                .addContainerGap())
        );
        MiddlePanelLayout.setVerticalGroup(
            MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CodesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(MiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(undoButton)
                    .addComponent(applyButton))
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(variablesLabel)
                    .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(MiddlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pramOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(defaultProbabilityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bandwidthPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(closeButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MiddlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pramOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(defaultProbabilityPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bandwidthPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(variablesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.controller.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void bandwidthCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bandwidthCheckBoxStateChanged
        this.bandwidthSpinner.setEnabled(this.bandwidthCheckBox.isSelected());
    }//GEN-LAST:event_bandwidthCheckBoxStateChanged


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
    private javax.swing.JButton defaultProbabilityButton;
    private javax.swing.JPanel defaultProbabilityPanel;
    private javax.swing.JSpinner defaultProbabilitySpinner;
    private javax.swing.JRadioButton globalRecodeRadioButton;
    private javax.swing.JRadioButton individualChancesRadioButton;
    private javax.swing.JTable jTable2;
    private javax.swing.ButtonGroup pramOptionsButtonGroup;
    private javax.swing.JPanel pramOptionsPanel;
    private javax.swing.JButton undoButton;
    private javax.swing.JLabel variablesLabel;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JTable variablesTable;
    // End of variables declaration//GEN-END:variables
}
