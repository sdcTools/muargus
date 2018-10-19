/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import muargus.VariableNameCellRenderer;
import muargus.VariablesTableRowRenderer;
import muargus.controller.TargetedRecordSwappingController;
import muargus.model.ReplacementSpec;
import muargus.model.TargetedRecordSwapping;
import muargus.model.VariableMu;

/**
 *
 * @author pwof
 */
public class TargetedRecordSwappingView extends DialogBase<TargetedRecordSwappingController> {

    private TargetedRecordSwapping model;
    private DefaultListModel<VariableMu> similarListModel;
    private DefaultListModel<VariableMu> hierarchyListModel;
    private DefaultListModel<VariableMu> riskListModel;
    private VariableMu hhIDVariable;
    
    /**
     * Creates new form NumericalRankSwappingView.
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param controller the controller of this view.
     */
    public TargetedRecordSwappingView(java.awt.Frame parent, boolean modal, TargetedRecordSwappingController controller) {
        super(parent, modal, controller);
        initComponents();
        setLocationRelativeTo(null);
        this.variablesTable.setDefaultRenderer(Object.class, new VariablesTableRowRenderer());
        this.similarList.setCellRenderer(new VariableNameCellRenderer());
        this.hierarchyList.setCellRenderer(new VariableNameCellRenderer());
        this.riskList.setCellRenderer(new VariableNameCellRenderer());
    }

    /**
     * Initializes the data. Sets the model, sets the table values and updates
     * the values.
     */
    @Override
    public void initializeData() {

        this.model = getMetadata().getCombinations().getTargetedRecordSwapping();
        //int numVar = getMetadata().isHouseholdData() ? this.model.getVariables().size() - 1 : this.model.getVariables().size();
        String[][] data = new String[this.model.getVariables().size()][2];
        int index = 0;
        int hhindex = 0;
        for (VariableMu variable : this.model.getVariables()) {
            data[index] = new String[]{getModifiedText(variable), variable.getName()};            
            if (variable.isHouse_id()){
                this.hhIDVariable = variable;
                this.hhIDTextField.setText(this.hhIDVariable.getName());
                data[index][0]="hhID";
                hhindex=index;
            }
            index++;
        }

        this.variablesTable.setModel(new DefaultTableModel(data, new Object[]{"Info", "Variable"}));
        this.variablesTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        
        if (hhindex ==0) this.variablesTable.getSelectionModel().setSelectionInterval(1, 1);
        else this.variablesTable.getSelectionModel().setSelectionInterval(0, 0);
        
        this.similarListModel = new DefaultListModel<>();
        this.hierarchyListModel = new DefaultListModel<>();
        this.riskListModel = new DefaultListModel<>();
        this.similarList.setModel(this.similarListModel);
        this.hierarchyList.setModel(this.hierarchyListModel);
        this.riskList.setModel(this.riskListModel);
        updateValues();
    }
    
    /**
     * Enables/disables the calculate button.
     */
    private void updateValues() {
        this.calculateButton.setEnabled(getSelectedRiskVariables().size()*getSelectedSimilarVariables().size()*getSelectedHierarchyVariables().size() > 0);
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
     * @param variable VariableMu instance of the variable for which the modified
     * text is requested.
     * @return String containing the modified text. If a variable is modified
     * "X" is returned, otherwise an empty string is returned.
     */
    private String getModifiedText(VariableMu variable) {
        for (ReplacementSpec spec : this.model.getTargetSwappings()) {
            if (spec.getOutputVariables().contains(variable)) {
                return "X";
            }
        }
        return "";
    }

    /**
     * Gets the selected variables in similarList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedSimilarVariables() {
        ArrayList<VariableMu> selected = new ArrayList<>();
        for (Object variable : ((DefaultListModel) this.similarList.getModel()).toArray()) {
            selected.add((VariableMu) variable);
        }
        return selected;
    }
    
    /**
     * Gets the selected variables in hierarchyList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedHierarchyVariables() {
        ArrayList<VariableMu> selected = new ArrayList<>();
        for (Object variable : ((DefaultListModel) this.hierarchyList.getModel()).toArray()) {
            selected.add((VariableMu) variable);
        }
        return selected;
    }
    /**
     * Gets the selected variables in riskList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedRiskVariables() {
        ArrayList<VariableMu> selected = new ArrayList<>();
        for (Object variable : ((DefaultListModel) this.riskList.getModel()).toArray()) {
            selected.add((VariableMu) variable);
        }
        return selected;
    }
    
    /**
     * Gets the rank swapping percentage.
     *
     * @return double containing the rank swapping percentage.
     */
    public double getSwaprate() {
        try {
            return Double.parseDouble(this.swaprateTextField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    /**
     * Gets the k anonymity threshold.
     *
     * @return Integer containing the k anonymity threshold.
     */
    public int getkanonThreshold() {
        try {
            return Integer.parseInt(this.kthresholdTextField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    /**
     * Gets the seed.
     *
     * @return Integer containing the seed.
     */
    public int getSeed() {
        try {
            return Integer.parseInt(this.seedTextField.getText());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    /**
     * Gets the household ID variable.
     *
     * @return VariableMu containing the household ID variable.
     */
    public VariableMu getHHIDVar() {
            return this.hhIDVariable;
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        similarList = new javax.swing.JList();
        upsimilar = new javax.swing.JButton();
        downsimilar = new javax.swing.JButton();
        unSimilarButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        hierarchyList = new javax.swing.JList();
        downhierarchy = new javax.swing.JButton();
        uphierarchy = new javax.swing.JButton();
        unHierarchyButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        riskList = new javax.swing.JList();
        uprisk = new javax.swing.JButton();
        downrisk = new javax.swing.JButton();
        unRiskButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        hhIDTextField = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        kthresholdTextField = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        swaprateTextField = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        seedTextField = new javax.swing.JTextField();
        tosimilarButton = new javax.swing.JButton();
        tohierarchyButton = new javax.swing.JButton();
        toriskButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        calculateButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Targeted Record Swapping");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));
        jPanel1.setPreferredSize(new java.awt.Dimension(132, 247));

        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Info", "Variable"
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Similar"));
        jPanel2.setPreferredSize(new java.awt.Dimension(112, 275));

        jScrollPane2.setViewportView(similarList);

        upsimilar.setText("↑");
        upsimilar.setToolTipText("");
        upsimilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upsimilarActionPerformed(evt);
            }
        });

        downsimilar.setText("↓");
        downsimilar.setToolTipText("");
        downsimilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downsimilarActionPerformed(evt);
            }
        });

        unSimilarButton.setText("<<");
        unSimilarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unSimilarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel2Layout.createSequentialGroup()
                .addComponent(upsimilar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downsimilar))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unSimilarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upsimilar)
                    .addComponent(downsimilar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(unSimilarButton))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Hierarchy"));

        jScrollPane3.setViewportView(hierarchyList);

        downhierarchy.setText("↓");
        downhierarchy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downhierarchyActionPerformed(evt);
            }
        });

        uphierarchy.setText("↑");
        uphierarchy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uphierarchyActionPerformed(evt);
            }
        });

        unHierarchyButton.setText("<<");
        unHierarchyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unHierarchyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(uphierarchy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downhierarchy))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unHierarchyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uphierarchy)
                    .addComponent(downhierarchy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(unHierarchyButton))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Risk"));
        jPanel3.setPreferredSize(new java.awt.Dimension(112, 275));

        jScrollPane4.setViewportView(riskList);

        uprisk.setText("↑");
        uprisk.setToolTipText("");
        uprisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upriskActionPerformed(evt);
            }
        });

        downrisk.setText("↓");
        downrisk.setToolTipText("");
        downrisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downriskActionPerformed(evt);
            }
        });

        unRiskButton.setText("<<");
        unRiskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unRiskButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(unRiskButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel3Layout.createSequentialGroup()
                .addComponent(uprisk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downrisk))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uprisk)
                    .addComponent(downrisk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unRiskButton))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Household ID"));

        hhIDTextField.setEditable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hhIDTextField)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hhIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Threshold (k-anon)"));

        kthresholdTextField.setText("3");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kthresholdTextField)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kthresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Swaprate"));

        swaprateTextField.setText("0.15");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swaprateTextField)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swaprateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Seed"));

        seedTextField.setText("12345");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        tosimilarButton.setText(">> Similar");
        tosimilarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tosimilarButtonActionPerformed(evt);
            }
        });

        tohierarchyButton.setText(">> Hierarchy");
        tohierarchyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tohierarchyButtonActionPerformed(evt);
            }
        });

        toriskButton.setText(">> Risk");
        toriskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toriskButtonActionPerformed(evt);
            }
        });

        undoButton.setText("Undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(605, 605, 605)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(605, 605, 605)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(605, 605, 605)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tohierarchyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tosimilarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(toriskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(109, 109, 109)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(undoButton)
                            .addComponent(calculateButton)
                            .addComponent(okButton))))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(calculateButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(undoButton)
                                .addGap(13, 13, 13)
                                .addComponent(okButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(tosimilarButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tohierarchyButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(toriskButton))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void toSelectionList(DefaultListModel<VariableMu> ListModel){
        boolean added = false;
        int index[] = this.variablesTable.getSelectedRows();
        for (int i : index) {
            VariableMu variable = this.model.getVariables().get(i);
            if (!ListModel.contains(variable) && !variable.isHouse_id()) {
                ListModel.addElement(variable);
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
    }
    
    private void fromSelectionList(DefaultListModel<VariableMu> ListModel, JList List){
        int[] index = List.getSelectedIndices();
        for (Object variable : List.getSelectedValuesList()) {
            ListModel.removeElement((VariableMu) variable);
        }
    }
    
    private void tosimilarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tosimilarButtonActionPerformed
        toSelectionList(this.similarListModel);
    }//GEN-LAST:event_tosimilarButtonActionPerformed

    private void tohierarchyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tohierarchyButtonActionPerformed
        toSelectionList(this.hierarchyListModel);
    }//GEN-LAST:event_tohierarchyButtonActionPerformed

    private void toriskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toriskButtonActionPerformed
        toSelectionList(this.riskListModel);
    }//GEN-LAST:event_toriskButtonActionPerformed

    private void unSimilarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unSimilarButtonActionPerformed
        fromSelectionList(this.similarListModel, this.similarList);
    }//GEN-LAST:event_unSimilarButtonActionPerformed

    private void unHierarchyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unHierarchyButtonActionPerformed
        fromSelectionList(this.hierarchyListModel, this.hierarchyList);
    }//GEN-LAST:event_unHierarchyButtonActionPerformed

    private void unRiskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unRiskButtonActionPerformed
        fromSelectionList(this.riskListModel, this.riskList);
    }//GEN-LAST:event_unRiskButtonActionPerformed

    private void moveUpinList(DefaultListModel<VariableMu> ListModel, JList List){
        int index = List.getSelectedIndex();
        if (index > 0) {
            VariableMu variable = ListModel.remove(index);
            ListModel.add(index - 1, variable);
            List.setSelectedIndex(index - 1);
        }
    }
    
    private void moveDowninList(DefaultListModel<VariableMu> ListModel, JList List){
        int index = List.getSelectedIndex();
        if (index > -1 && index < ListModel.getSize() - 1) {
            VariableMu variable = ListModel.remove(index);
            ListModel.add(index + 1, variable);
            List.setSelectedIndex(index + 1);
        }
    }
    
    private void upsimilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upsimilarActionPerformed
        moveUpinList(this.similarListModel,this.similarList);
    }//GEN-LAST:event_upsimilarActionPerformed

    private void uphierarchyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uphierarchyActionPerformed
        moveUpinList(this.hierarchyListModel,this.hierarchyList);
    }//GEN-LAST:event_uphierarchyActionPerformed

    private void upriskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upriskActionPerformed
        moveUpinList(this.riskListModel,this.riskList);
    }//GEN-LAST:event_upriskActionPerformed

    private void downsimilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downsimilarActionPerformed
        moveDowninList(this.similarListModel,this.similarList);
    }//GEN-LAST:event_downsimilarActionPerformed

    private void downhierarchyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downhierarchyActionPerformed
        moveDowninList(this.hierarchyListModel,this.hierarchyList);
    }//GEN-LAST:event_downhierarchyActionPerformed

    private void downriskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downriskActionPerformed
        moveDowninList(this.riskListModel,this.riskList);
    }//GEN-LAST:event_downriskActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        getController().calculate();
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        getController().undo();
    }//GEN-LAST:event_undoButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton downhierarchy;
    private javax.swing.JButton downrisk;
    private javax.swing.JButton downsimilar;
    private javax.swing.JTextField hhIDTextField;
    private javax.swing.JList hierarchyList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField kthresholdTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JList riskList;
    private javax.swing.JTextField seedTextField;
    private javax.swing.JList similarList;
    private javax.swing.JTextField swaprateTextField;
    private javax.swing.JButton tohierarchyButton;
    private javax.swing.JButton toriskButton;
    private javax.swing.JButton tosimilarButton;
    private javax.swing.JButton unHierarchyButton;
    private javax.swing.JButton unRiskButton;
    private javax.swing.JButton unSimilarButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton uphierarchy;
    private javax.swing.JButton uprisk;
    private javax.swing.JButton upsimilar;
    private javax.swing.JTable variablesTable;
    // End of variables declaration//GEN-END:variables
}
