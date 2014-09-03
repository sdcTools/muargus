/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.VariableNameCellRenderer;
import muargus.controller.ShowTableCollectionController;
import muargus.model.MetadataMu;
import muargus.model.ShowTableCollection;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class ShowTableCollectionView extends DialogBase {

    MetadataMu metadataMu;
    ShowTableCollectionController controller;
    ShowTableCollection model;
    private ArrayList<VariableMu> variables;
    private DefaultComboBoxModel variableListModel;
    private TableModel tableModel;
    
    /**
     * Creates new form ModifyShowTableCollection
     * @param parent
     * @param modal
     * @param controller
     */
    public ShowTableCollectionView(java.awt.Frame parent, boolean modal, ShowTableCollectionController controller) {
        super(parent, modal);
        this.initComponents(); // wil je bij methoden van de eigen klasse wel of geen 'this' ervoor?
        this.controller =  controller;
        this.setLocationRelativeTo(null);
        this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.selectVariableComboBox.setRenderer(new VariableNameCellRenderer());
    }
    
    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
        this.model = this.metadataMu.getCombinations().getShowTableCollection();
        this.initializeData();
    }
    
    public void initializeData(){
        variableListModel = new DefaultComboBoxModel<>();
        VariableMu all = new VariableMu("all");
        variableListModel.addElement(all);
        this.model.setSelectedVariable(all);
        for (VariableMu variable : model.getVariables()) {
            variableListModel.addElement(variable);
        }
        this.selectVariableComboBox.setModel(variableListModel);
        this.model.setTables(this.metadataMu.getCombinations().getTables());
        
        this.updateTable();
    }
    
    public void updateTable(){
        this.tableModel = new DefaultTableModel(this.model.getData(), this.model.getColumnames()) {
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.table.setModel(tableModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        showAllTablesCheckBox = new javax.swing.JCheckBox();
        closeButton = new javax.swing.JButton();
        selectVariableLabel = new javax.swing.JLabel();
        selectVariableComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Table Collection");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "# unsafe cells", "Var 1", "Var 2", "Var 3", "Var 4"
            }
        ));
        scrollPane.setViewportView(table);

        showAllTablesCheckBox.setText("Show all tables");
        showAllTablesCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                showAllTablesCheckBoxStateChanged(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        selectVariableLabel.setText("Select variable:");

        selectVariableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        selectVariableComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectVariableComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(closeButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showAllTablesCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(selectVariableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectVariableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showAllTablesCheckBox)
                    .addComponent(selectVariableLabel)
                    .addComponent(selectVariableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.controller.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void showAllTablesCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_showAllTablesCheckBoxStateChanged
        this.model.setShowAllTables(!this.model.isShowAllTables());
    }//GEN-LAST:event_showAllTablesCheckBoxStateChanged

    private void selectVariableComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectVariableComboBoxActionPerformed
        this.model.setSelectedVariable((VariableMu) this.selectVariableComboBox.getSelectedItem());
        this.updateTable();
    }//GEN-LAST:event_selectVariableComboBoxActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ShowTableCollectionView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ShowTableCollectionView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ShowTableCollectionView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ShowTableCollectionView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ShowTableCollectionView dialog = new ShowTableCollectionView(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JComboBox selectVariableComboBox;
    private javax.swing.JLabel selectVariableLabel;
    private javax.swing.JCheckBox showAllTablesCheckBox;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
