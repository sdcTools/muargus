//TODO: fixen zodat sorteren goed werkt --> de variabele wordt gesorteerd en niet de variabele naam zoals de renderer laat zien.
package muargus.view;

import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import muargus.VariableNameCellRenderer;
import muargus.VariableNameTableCellRenderer;
import muargus.controller.SyntheticDataController;
import muargus.model.VariableMu;

/**
 *
 * @author pibd05
 */
public class SyntheticDataView extends DialogBase<SyntheticDataController> {

    private DefaultListModel variablesListModel;
    private DefaultListModel nonSensitiveVariablesListModel;
    private final int[] columnWidth = {80, 30};

    /**
     * Creates new form RView
     *
     * @param parent
     * @param modal
     * @param controller
     */
    public SyntheticDataView(java.awt.Frame parent, boolean modal, SyntheticDataController controller) {
        super(parent, modal, controller);
        initComponents();
        setLocationRelativeTo(null);
        this.variablesList.setCellRenderer(new VariableNameCellRenderer());
        this.nonSensitiveVariablesList.setCellRenderer(new VariableNameCellRenderer());
        this.sensitiveVariablesTable.setDefaultRenderer(Object.class, new VariableNameTableCellRenderer());
    }

    /**
     * Fills the selecCombinationsScreen with it's default values
     */
    @Override
    public void initializeData() {
        // make listModels and add the variables that are numeric
        this.variablesListModel = new DefaultListModel<>();
        this.nonSensitiveVariablesListModel = new DefaultListModel<>();
        for (VariableMu variable : getMetadata().getVariables()) {
            if (variable.isNumeric()) {
                this.variablesListModel.addElement(variable);
            }
        }
        this.variablesList.setModel(this.variablesListModel);

        this.nonSensitiveVariablesList.setModel(this.nonSensitiveVariablesListModel);
        if (this.variablesListModel.getSize() > 0) {
            this.variablesList.setSelectedIndex(0);
        }
        
        this.sensitiveVariablesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    variablesSelectionChanged();
                }
            }
        });
        
        updateValues();
    }

    private void updateTable() {

        this.sensitiveVariablesTable.setModel(new DefaultTableModel(getController().getSensitiveData(), new Object[]{"Variable", "Alpha"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Class getColumnClass(int i) {
                return (i == 1 ? Double.class : VariableMu.class); //TODO: fixen zodat sorteren goed werkt --> renderer zet het nu als string
            }
        });
        for (int i = 0; i < this.columnWidth.length; i++) {
            this.sensitiveVariablesTable.getColumnModel().getColumn(i).setMinWidth(this.columnWidth[i]);
            this.sensitiveVariablesTable.getColumnModel().getColumn(i).setPreferredWidth(this.columnWidth[i]);
        }

    }

    private void updateValues() {
        this.moveToNonSensitiveVariablesButton.setEnabled(this.variablesListModel.getSize() > 0);
        this.moveToSensitiveVariablesButton.setEnabled(this.variablesListModel.getSize() > 0);
        this.moveFromNonSensitiveVariablesButton.setEnabled(!getController().getNonSensitiveVariables().isEmpty());
        this.moveFromSensitiveVariablesButton.setEnabled(!getController().getSensitiveVariables().isEmpty());
        this.sensitiveVariablesSlider.setEnabled(!getController().getSensitiveVariables().isEmpty());
    }
    
    private void variablesSelectionChanged(){
        if (!getController().getSensitiveVariables().isEmpty()) {
            int[] index = this.sensitiveVariablesTable.getSelectedRows();
            for (int i : index) {
                this.sensitiveVariablesSlider.setValue((int) Math.round(
                        ((VariableMu) this.sensitiveVariablesTable.getValueAt(i, 0)).getAlpha() * 100));
                break;
            }
        }
    }

    /*  
     If nothing or more than one variable is selected, select the first variable. 
     If the last variable is removed, select the new last variable.
     Otherwise select the variable with the same index of the last.
     */
    private int getIndex(int[] index, int size) {
        int selected;
        if (index.length != 1) {
            selected = 0;
        } else if (size == index[index.length - 1]) {
            selected = size - 1;
        } else {
            selected = index[index.length - 1];
        }
        return selected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        variablesPanel = new javax.swing.JPanel();
        variablesScrollPane = new javax.swing.JScrollPane();
        variablesList = new javax.swing.JList();
        variablesLabel = new javax.swing.JLabel();
        moveToSensitiveVariablesButton = new javax.swing.JButton();
        moveFromSensitiveVariablesButton = new javax.swing.JButton();
        sensitiveVariablesLabel = new javax.swing.JLabel();
        sensitiveVariablesScrollPane = new javax.swing.JScrollPane();
        sensitiveVariablesTable = new javax.swing.JTable();
        sensitiveVariablesSlider = new javax.swing.JSlider();
        moveToNonSensitiveVariablesButton = new javax.swing.JButton();
        moveFromNonSensitiveVariablesButton = new javax.swing.JButton();
        nonSensitiveVariablesLabel = new javax.swing.JLabel();
        nonSensitiveVariablesScrollPane = new javax.swing.JScrollPane();
        nonSensitiveVariablesList = new javax.swing.JList();
        runSyntheticDataButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Synthetic data");
        setMinimumSize(new java.awt.Dimension(420, 450));
        setModal(true);
        setPreferredSize(new java.awt.Dimension(420, 450));

        variablesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Variables"));

        variablesScrollPane.setViewportView(variablesList);

        variablesLabel.setText("Variables");

        moveToSensitiveVariablesButton.setText(">");
        moveToSensitiveVariablesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToSensitiveVariablesButtonActionPerformed(evt);
            }
        });

        moveFromSensitiveVariablesButton.setText("<");
        moveFromSensitiveVariablesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveFromSensitiveVariablesButtonActionPerformed(evt);
            }
        });

        sensitiveVariablesLabel.setText("Sensitive Variables");

        sensitiveVariablesTable.setAutoCreateRowSorter(true);
        sensitiveVariablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Alpha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sensitiveVariablesScrollPane.setViewportView(sensitiveVariablesTable);

        sensitiveVariablesSlider.setMinimum(10);
        sensitiveVariablesSlider.setMinorTickSpacing(10);
        sensitiveVariablesSlider.setPaintTicks(true);
        sensitiveVariablesSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sensitiveVariablesSliderStateChanged(evt);
            }
        });

        moveToNonSensitiveVariablesButton.setText(">");
        moveToNonSensitiveVariablesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToNonSensitiveVariablesButtonActionPerformed(evt);
            }
        });

        moveFromNonSensitiveVariablesButton.setText("<");
        moveFromNonSensitiveVariablesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveFromNonSensitiveVariablesButtonActionPerformed(evt);
            }
        });

        nonSensitiveVariablesLabel.setText("Non Sensitive Variables");

        nonSensitiveVariablesScrollPane.setViewportView(nonSensitiveVariablesList);

        javax.swing.GroupLayout variablesPanelLayout = new javax.swing.GroupLayout(variablesPanel);
        variablesPanel.setLayout(variablesPanelLayout);
        variablesPanelLayout.setHorizontalGroup(
            variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(variablesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(variablesPanelLayout.createSequentialGroup()
                        .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(variablesPanelLayout.createSequentialGroup()
                                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(moveToNonSensitiveVariablesButton)
                                    .addComponent(moveFromNonSensitiveVariablesButton))
                                .addGap(18, 18, 18)
                                .addComponent(nonSensitiveVariablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(variablesPanelLayout.createSequentialGroup()
                                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(moveToSensitiveVariablesButton)
                                    .addComponent(moveFromSensitiveVariablesButton))
                                .addGap(18, 18, 18)
                                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sensitiveVariablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(sensitiveVariablesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addGroup(variablesPanelLayout.createSequentialGroup()
                                        .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(nonSensitiveVariablesLabel)
                                            .addComponent(sensitiveVariablesLabel))
                                        .addGap(0, 29, Short.MAX_VALUE))))))
                    .addGroup(variablesPanelLayout.createSequentialGroup()
                        .addComponent(variablesLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        variablesPanelLayout.setVerticalGroup(
            variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, variablesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variablesLabel)
                    .addComponent(sensitiveVariablesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(variablesScrollPane)
                    .addGroup(variablesPanelLayout.createSequentialGroup()
                        .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(variablesPanelLayout.createSequentialGroup()
                                .addComponent(moveToSensitiveVariablesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moveFromSensitiveVariablesButton))
                            .addComponent(sensitiveVariablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sensitiveVariablesSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nonSensitiveVariablesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(variablesPanelLayout.createSequentialGroup()
                                .addComponent(moveToNonSensitiveVariablesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moveFromNonSensitiveVariablesButton)
                                .addGap(0, 63, Short.MAX_VALUE))
                            .addComponent(nonSensitiveVariablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );

        runSyntheticDataButton.setText("Run Synthetic data");
        runSyntheticDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSyntheticDataButtonActionPerformed(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(variablesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(runSyntheticDataButton)
                        .addGap(18, 18, 18)
                        .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(runSyntheticDataButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moveFromSensitiveVariablesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveFromSensitiveVariablesButtonActionPerformed
        int[] index = this.sensitiveVariablesTable.getSelectedRows();
        for (int i : index) {
            getController().getSensitiveVariables().remove((VariableMu) this.sensitiveVariablesTable.getValueAt(i, 0));
            this.variablesListModel.addElement((VariableMu) this.sensitiveVariablesTable.getValueAt(i, 0));
        }
        updateTable();
        int selected = getIndex(index, getController().getSensitiveVariables().size());
        this.sensitiveVariablesTable.getSelectionModel().setSelectionInterval(selected, selected);
        updateValues();
    }//GEN-LAST:event_moveFromSensitiveVariablesButtonActionPerformed

    private void moveToSensitiveVariablesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToSensitiveVariablesButtonActionPerformed
        int[] index = this.variablesList.getSelectedIndices();
        Object[] variableMu = this.variablesList.getSelectedValuesList().toArray();

        for (Object variable : variableMu) {
            getController().getSensitiveVariables().add((VariableMu) variable);
            this.variablesListModel.removeElement((VariableMu) variable);
        }
        updateTable();
        int selected = getIndex(index, this.variablesListModel.size());
        this.variablesList.setSelectedIndex(selected);
        this.sensitiveVariablesTable.getSelectionModel().setSelectionInterval(
                getController().getSensitiveVariables().size() - 1, getController().getSensitiveVariables().size() - 1);
        updateValues();
    }//GEN-LAST:event_moveToSensitiveVariablesButtonActionPerformed

    private void moveToNonSensitiveVariablesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToNonSensitiveVariablesButtonActionPerformed
        int[] index = this.variablesList.getSelectedIndices();
        Object[] variableMu = this.variablesList.getSelectedValuesList().toArray();

        for (Object variable : variableMu) {
            this.nonSensitiveVariablesListModel.add(this.nonSensitiveVariablesListModel.getSize(), (VariableMu) variable);
            getController().getNonSensitiveVariables().add((VariableMu) variable);
            this.variablesListModel.removeElement((VariableMu) variable);
        }
        int selected = getIndex(index, this.variablesListModel.size());
        this.variablesList.setSelectedIndex(selected);
        updateValues();
    }//GEN-LAST:event_moveToNonSensitiveVariablesButtonActionPerformed

    private void moveFromNonSensitiveVariablesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveFromNonSensitiveVariablesButtonActionPerformed
        int[] index = this.nonSensitiveVariablesList.getSelectedIndices();
        for (Object o : this.nonSensitiveVariablesList.getSelectedValuesList()) {
            this.nonSensitiveVariablesListModel.removeElement(o);
            getController().getNonSensitiveVariables().remove((VariableMu) o);
            this.variablesListModel.addElement((VariableMu) o);
        }
        int selected = getIndex(index, this.nonSensitiveVariablesListModel.size());
        this.nonSensitiveVariablesList.setSelectedIndex(selected);
        updateValues();
    }//GEN-LAST:event_moveFromNonSensitiveVariablesButtonActionPerformed

    private void runSyntheticDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSyntheticDataButtonActionPerformed
        getController().runSyntheticData();
    }//GEN-LAST:event_runSyntheticDataButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        getController().close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void sensitiveVariablesSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sensitiveVariablesSliderStateChanged
        int[] index = this.sensitiveVariablesTable.getSelectedRows();
        for (int i : index) {
            ((VariableMu) this.sensitiveVariablesTable.getValueAt(i, 0)).setAlpha(this.sensitiveVariablesSlider.getValue() / 100.0);
            this.sensitiveVariablesTable.setValueAt(this.sensitiveVariablesSlider.getValue() / 100.0, i, 1);
        }
    }//GEN-LAST:event_sensitiveVariablesSliderStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton moveFromNonSensitiveVariablesButton;
    private javax.swing.JButton moveFromSensitiveVariablesButton;
    private javax.swing.JButton moveToNonSensitiveVariablesButton;
    private javax.swing.JButton moveToSensitiveVariablesButton;
    private javax.swing.JLabel nonSensitiveVariablesLabel;
    private javax.swing.JList nonSensitiveVariablesList;
    private javax.swing.JScrollPane nonSensitiveVariablesScrollPane;
    private javax.swing.JButton runSyntheticDataButton;
    private javax.swing.JLabel sensitiveVariablesLabel;
    private javax.swing.JScrollPane sensitiveVariablesScrollPane;
    private javax.swing.JSlider sensitiveVariablesSlider;
    private javax.swing.JTable sensitiveVariablesTable;
    private javax.swing.JLabel variablesLabel;
    private javax.swing.JList variablesList;
    private javax.swing.JPanel variablesPanel;
    private javax.swing.JScrollPane variablesScrollPane;
    // End of variables declaration//GEN-END:variables
}
