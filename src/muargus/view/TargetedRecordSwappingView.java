/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import muargus.VariableNameCellRenderer;
import muargus.VariablesTableRowRenderer;
import muargus.controller.TargetedRecordSwappingController;
import muargus.model.ReplacementSpec;
import muargus.model.TargetSwappingSpec;
import muargus.model.TargetedRecordSwapping;
import muargus.model.VariableMu;

/**
 *
 * @author pwof
 */
public class TargetedRecordSwappingView extends DialogBase<TargetedRecordSwappingController> {

    private TargetedRecordSwapping model;
    private ArrayList<DefaultListModel<VariableMu>> similarListModels;
    private DefaultListModel<VariableMu> hierarchyListModel;
    private DefaultListModel<VariableMu> riskListModel;
    private DefaultListModel<VariableMu> carryListModel;    
    private VariableMu hhIDVariable;
    private int hhindex;

    private ArrayList<javax.swing.JScrollPane> similarScrollPanes;
    private ArrayList<javax.swing.JList<VariableMu>> similarLists;
        
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
        this.similarScrollPanes = new ArrayList<>();
        this.similarLists = new ArrayList<>();
        // Should always have at least one similarity profile: the one that is initialized in initComponents 
        this.similarScrollPanes.add(this.similarScrollPane);
        this.similarLists.add(this.similarList);
        
        setLocationRelativeTo(null);
        this.variablesTable.setDefaultRenderer(Object.class, new VariablesTableRowRenderer());
        this.similarList.setCellRenderer(new VariableNameCellRenderer());
        this.hierarchyList.setCellRenderer(new VariableNameCellRenderer());
        this.riskList.setCellRenderer(new VariableNameCellRenderer());
        this.carryList.setCellRenderer(new VariableNameCellRenderer());
    }

    /**
     * Initializes the data. Sets the model, sets the table values and updates
     * the values.
     */
    @Override
    public void initializeData() {
        
        this.similarListModels = new ArrayList<>();
        this.similarListModels.add(new DefaultListModel<>());
        
        this.hierarchyListModel = new DefaultListModel<>();
        this.riskListModel = new DefaultListModel<>();
        this.carryListModel = new DefaultListModel<>();
        
        this.similarLists.get(0).setModel(this.similarListModels.get(0));
        this.hierarchyList.setModel(this.hierarchyListModel);
        this.riskList.setModel(this.riskListModel);
        this.carryList.setModel(this.carryListModel);
        
        this.model = getMetadata().getCombinations().getTargetedRecordSwapping();
        //this.model = getMetadata().getTargetedRecordSwapping();
        String[][] data = new String[this.model.getVariables().size()][2];
        int index = 0;
        for (VariableMu variable : this.model.getVariables()) {
            data[index] = new String[]{getModifiedText(variable), variable.getName()};            
            if (variable.isHouse_id()){
                this.hhIDVariable = variable;
                this.hhIDTextField.setText(this.hhIDVariable.getName());
                data[index][0]="hhID";
                this.hhindex=index;
            }
            index++;
        }
        this.variablesTable.setModel(new DefaultTableModel(data, new Object[]{"Info", "Variable"}));
        this.variablesScrollPane.getColumnHeader().setVisible(false);
        this.variablesTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        // Disable selection of hhID row
        this.variablesTable.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public boolean isSelectedIndex(final int index) {
            boolean isSelected;
            if ( index == hhindex ) {
                isSelected = false;
            } else {
                isSelected = super.isSelectedIndex(index);
            }
                return isSelected;
            }
        }); 
        
        if (this.hhindex == 0) this.variablesTable.getSelectionModel().setSelectionInterval(1, 1);
        else this.variablesTable.getSelectionModel().setSelectionInterval(0, 0);
        // Fill displayed lists with last TargetSwappingSpec
        if (this.model.getTargetSwappings().size() > 0){ 
            TargetSwappingSpec spec = this.model.getTargetSwappings().get(this.model.getTargetSwappings().size() - 1);
            updateVariableRows(spec);
            // First add tab for each additional profile
            for (int i=1; i<spec.getNProfiles();i++){
                this.similarScrollPanes.add(new javax.swing.JScrollPane());
                this.similarLists.add(new javax.swing.JList<>());
                this.similarScrollPanes.get(i).setViewportView(this.similarLists.get(i));
                this.similarListModels.add(new DefaultListModel<>());
                this.similarLists.get(i).setModel(this.similarListModels.get(i));
                this.similarLists.get(i).setCellRenderer(new VariableNameCellRenderer());
                this.similarTabbedPane.addTab(Integer.toString(i+1), this.similarScrollPanes.get(i));
            }
            // Then fill each tab with content
            int k=0;
            for (int i=0; i<spec.getNProfiles();i++){
                for (int j=0; j<spec.getNSim()[i]; j++){
                    this.similarListModels.get(i).addElement(spec.getOutputVariables().get(spec.getSimilarIndexes()[k+j]));
                }
                k += spec.getNSim()[i];
            }
            
            fillList(this.hierarchyListModel,spec.getHierarchyIndexes(),spec);
            fillList(this.riskListModel,spec.getRiskIndexes(),spec);
            fillList(this.carryListModel,spec.getCarryIndexes(),spec);
            this.updateVariableRows(spec);
            
            // Finally set the used parameters
            this.kthresholdTextField.setText(Integer.toString(spec.getkThreshold()));
            this.seedTextField.setText(Integer.toString(spec.getSeed()));
            this.swaprateTextField.setText(Double.toString(spec.getSwaprate()));
        }
        updateValues();
    }
    
    private void fillList(DefaultListModel<VariableMu> ListModel, int[] Indexes, TargetSwappingSpec spec){
        for (int i=0;i<Indexes.length;i++){
            ListModel.addElement(spec.getOutputVariables().get(Indexes[i]));
        }
    }
    
    /**
     * Enables/disables the calculate and undo button.
     */
    private void updateValues() {
        TargetedRecordSwapping TRS = this.getMetadata().getCombinations().getTargetedRecordSwapping();
        boolean b = false, bc = false, bb;
        if (TRS.getTargetSwappings().size() > 0){
            b = (TRS.getTargetSwappings().get(TRS.getTargetSwappings().size() - 1).getReplacementFile() != null);
            bc = TRS.getTargetSwappings().get(TRS.getTargetSwappings().size() - 1).getIsCalculated();
        }

        bb = getSelectedVariables(this.riskListModel).size()*getSelectedVariables(this.similarListModels.get(0)).size()*getSelectedVariables(this.hierarchyListModel).size() > 0;
        
        this.enableMoveButtons(!bc);
        this.undoButton.setEnabled(bc);        
        this.calculateButton.setEnabled(bb && !bc);
    }

    /**
     * Updates the values inside the table.
     *
     * @param replacement ReplacementSpec instance containing the
     * RankSwappingSpec.
     */
    public void updateVariableRows(ReplacementSpec replacement) {
        int index;
        for (VariableMu variableMu : replacement.getOutputVariables()) {
            index = this.model.getVariables().indexOf(variableMu);
            this.variablesTable.setValueAt(getModifiedText(variableMu), index, 0);
            this.variablesTable.setValueAt(variableMu.getName(), index, 1);
        }
    }

    /**
     * Gets the modification text belonging to this particular variable. If a
     * variable is modified a string is returned containing info on being part of
     * "S"imilarList, "H"ierarchyList, "R"iskList, "C"arryList
     * otherwise an empty string is returned.
     * HouseholdIdentifier always returns "hhID"
     *
     * @param variable VariableMu instance of the variable for which the modified
     * text is requested.
     * @return String containing the modified text. 
     */
    private String getModifiedText(VariableMu variable) {
        if (variable == this.getHHIDVar())
            return "hhID";
        for (ReplacementSpec spec : this.model.getTargetSwappings()) {
            if (spec.getOutputVariables().contains(variable)) {
                String hs = "";
                if (this.getSelectedVariables(this.similarListModels.get(0)).contains(variable))
                    hs += "S";
                if (this.getSelectedVariables(this.hierarchyListModel).contains(variable))
                    hs += "H";
                if (this.getSelectedVariables(this.riskListModel).contains(variable))
                    hs += "R";
                if (this.getSelectedVariables(this.carryListModel).contains(variable))
                    hs += "C";                
                return hs;              
            }
        }
        return "";
    }

    public int getNumberofProfiles(){
        return this.similarLists.size();
    }
    
    /**
     * Gets the variables displayed in a list in this view
     * 
     * @param DisplayedList The ListModel to look at
     * @return ArrayList of VaiableMu's containing the displayed variables
     */
    
    public ArrayList<VariableMu> getSelectedVariables(DefaultListModel DisplayedList) {
        ArrayList<VariableMu> selected = new ArrayList<>();
        for (Object variable : DisplayedList.toArray()) {
            selected.add((VariableMu) variable);
        }
        return selected;
    }
    
    /**
     * Gets the selected variables in similarList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedSimilarVariables() {
        ArrayList<VariableMu> similarVars = new ArrayList<>();
        for (int i=0; i<this.getNumberofProfiles(); i++){
            for (Object variable : similarListModels.get(i).toArray()){
                similarVars.add((VariableMu) variable);
            }
        }
        //int SelectedProfile = this.similarTabbedPane.getSelectedIndex();
        //return getSelectedVariables(this.similarListModels.get(SelectedProfile));
        return similarVars;
    }
    
    public int[] getNSim(){
        int[] nSimPerProfile = new int[this.getNumberofProfiles()];
        for (int i=0; i<this.getNumberofProfiles(); i++){
            nSimPerProfile[i] = similarListModels.get(i).size();
        }
        return nSimPerProfile;
    }
    
    /**
     * Gets the selected variables in hierarchyList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedHierarchyVariables() {
        return getSelectedVariables(this.hierarchyListModel);
    }
    /**
     * Gets the selected variables in riskList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedRiskVariables() {
        return getSelectedVariables(this.riskListModel);
    }
    /**
     * Gets the selected variables in carryList.
     *
     * @return Arraylist of VariableMu's containing the selected variables.
     */
    public ArrayList<VariableMu> getSelectedCarryVariables() {
        return getSelectedVariables(this.carryListModel);
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

    public void enableMoveButtons(boolean buttonstatus){
        this.AddProfileButton.setEnabled(buttonstatus);
        this.DelProfileButton.setEnabled(buttonstatus);
        this.MutipleProfilesPanel.setEnabled(buttonstatus);
        this.unSimilarButton.setEnabled(buttonstatus);
        this.unHierarchyButton.setEnabled(buttonstatus);
        this.unRiskButton.setEnabled(buttonstatus);
        this.unCarryButton.setEnabled(buttonstatus);
        this.tosimilarButton.setEnabled(buttonstatus);
        this.tohierarchyButton.setEnabled(buttonstatus);
        this.toriskButton.setEnabled(buttonstatus);
        this.tocarryButton.setEnabled(buttonstatus);
        this.downsimilar.setEnabled(buttonstatus);
        this.downhierarchy.setEnabled(buttonstatus);
        this.downrisk.setEnabled(buttonstatus);
        this.downcarry.setEnabled(buttonstatus);
        this.upsimilar.setEnabled(buttonstatus);
        this.uphierarchy.setEnabled(buttonstatus);
        this.uprisk.setEnabled(buttonstatus);
        this.upcarry.setEnabled(buttonstatus);
        this.calculateButton.setEnabled(buttonstatus);
        this.undoButton.setEnabled(!buttonstatus);
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

        variablesPanel = new javax.swing.JPanel();
        variablesScrollPane = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        similarPanel = new javax.swing.JPanel();
        similarTabbedPane = new javax.swing.JTabbedPane();
        similarScrollPane = new javax.swing.JScrollPane();
        similarList = new javax.swing.JList<>();
        upsimilar = new javax.swing.JButton();
        downsimilar = new javax.swing.JButton();
        unSimilarButton = new javax.swing.JButton();
        hierachyPanel = new javax.swing.JPanel();
        hierarchyScrollPane = new javax.swing.JScrollPane();
        hierarchyList = new javax.swing.JList<>();
        downhierarchy = new javax.swing.JButton();
        uphierarchy = new javax.swing.JButton();
        unHierarchyButton = new javax.swing.JButton();
        riskPanel = new javax.swing.JPanel();
        riskScrollPane = new javax.swing.JScrollPane();
        riskList = new javax.swing.JList<>();
        uprisk = new javax.swing.JButton();
        downrisk = new javax.swing.JButton();
        unRiskButton = new javax.swing.JButton();
        hidPanel = new javax.swing.JPanel();
        hhIDTextField = new javax.swing.JTextField();
        kanonPanel = new javax.swing.JPanel();
        kthresholdTextField = new javax.swing.JTextField();
        swapPanel = new javax.swing.JPanel();
        swaprateTextField = new javax.swing.JTextField();
        seedPanel = new javax.swing.JPanel();
        seedTextField = new javax.swing.JTextField();
        tosimilarButton = new javax.swing.JButton();
        tohierarchyButton = new javax.swing.JButton();
        toriskButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        calculateButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        okButton = new javax.swing.JButton();
        carryalongPanel = new javax.swing.JPanel();
        carryalongScrollPane = new javax.swing.JScrollPane();
        carryList = new javax.swing.JList<>();
        downcarry = new javax.swing.JButton();
        unCarryButton = new javax.swing.JButton();
        upcarry = new javax.swing.JButton();
        tocarryButton = new javax.swing.JButton();
        MutipleProfilesPanel = new javax.swing.JPanel();
        AddProfileButton = new javax.swing.JButton();
        DelProfileButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Targeted Record Swapping");
        setResizable(false);

        variablesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Variables"));
        variablesPanel.setPreferredSize(new java.awt.Dimension(132, 247));

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
        variablesScrollPane.setViewportView(variablesTable);

        javax.swing.GroupLayout variablesPanelLayout = new javax.swing.GroupLayout(variablesPanel);
        variablesPanel.setLayout(variablesPanelLayout);
        variablesPanelLayout.setHorizontalGroup(
            variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(variablesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        variablesPanelLayout.setVerticalGroup(
            variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(variablesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        );

        similarPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Similarity profiles"));
        similarPanel.setPreferredSize(new java.awt.Dimension(112, 275));

        similarTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        similarScrollPane.setViewportView(similarList);

        similarTabbedPane.addTab("1", similarScrollPane);

        upsimilar.setText("↑");
        upsimilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upsimilarActionPerformed(evt);
            }
        });

        downsimilar.setText("↓");
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

        javax.swing.GroupLayout similarPanelLayout = new javax.swing.GroupLayout(similarPanel);
        similarPanel.setLayout(similarPanelLayout);
        similarPanelLayout.setHorizontalGroup(
            similarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(similarTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, similarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(similarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unSimilarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(similarPanelLayout.createSequentialGroup()
                        .addComponent(upsimilar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downsimilar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        similarPanelLayout.setVerticalGroup(
            similarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(similarPanelLayout.createSequentialGroup()
                .addComponent(similarTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(similarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upsimilar)
                    .addComponent(downsimilar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(unSimilarButton))
        );

        similarTabbedPane.getAccessibleContext().setAccessibleDescription("");

        hierachyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Hierarchy"));

        hierarchyScrollPane.setViewportView(hierarchyList);

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

        javax.swing.GroupLayout hierachyPanelLayout = new javax.swing.GroupLayout(hierachyPanel);
        hierachyPanel.setLayout(hierachyPanelLayout);
        hierachyPanelLayout.setHorizontalGroup(
            hierachyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hierarchyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hierachyPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(hierachyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(unHierarchyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(hierachyPanelLayout.createSequentialGroup()
                        .addComponent(uphierarchy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downhierarchy)))
                .addContainerGap())
        );
        hierachyPanelLayout.setVerticalGroup(
            hierachyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hierachyPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(hierarchyScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(hierachyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uphierarchy)
                    .addComponent(downhierarchy))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unHierarchyButton))
        );

        riskPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Risk"));
        riskPanel.setPreferredSize(new java.awt.Dimension(112, 275));

        riskScrollPane.setViewportView(riskList);

        uprisk.setText("↑");
        uprisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upriskActionPerformed(evt);
            }
        });

        downrisk.setText("↓");
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

        javax.swing.GroupLayout riskPanelLayout = new javax.swing.GroupLayout(riskPanel);
        riskPanel.setLayout(riskPanelLayout);
        riskPanelLayout.setHorizontalGroup(
            riskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(riskScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(riskPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(riskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unRiskButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(riskPanelLayout.createSequentialGroup()
                        .addComponent(uprisk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downrisk)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        riskPanelLayout.setVerticalGroup(
            riskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(riskPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(riskScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(riskPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uprisk)
                    .addComponent(downrisk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unRiskButton))
        );

        hidPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Household ID"));

        hhIDTextField.setEditable(false);

        javax.swing.GroupLayout hidPanelLayout = new javax.swing.GroupLayout(hidPanel);
        hidPanel.setLayout(hidPanelLayout);
        hidPanelLayout.setHorizontalGroup(
            hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hhIDTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        );
        hidPanelLayout.setVerticalGroup(
            hidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidPanelLayout.createSequentialGroup()
                .addComponent(hhIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        kanonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("K-anonymity"));

        kthresholdTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        kthresholdTextField.setText("3");
        kthresholdTextField.setPreferredSize(new java.awt.Dimension(6, 20));

        javax.swing.GroupLayout kanonPanelLayout = new javax.swing.GroupLayout(kanonPanel);
        kanonPanel.setLayout(kanonPanelLayout);
        kanonPanelLayout.setHorizontalGroup(
            kanonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kthresholdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        kanonPanelLayout.setVerticalGroup(
            kanonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kthresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        swapPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Swaprate"));

        swaprateTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        swaprateTextField.setText("0.05");
        swaprateTextField.setPreferredSize(new java.awt.Dimension(6, 20));

        javax.swing.GroupLayout swapPanelLayout = new javax.swing.GroupLayout(swapPanel);
        swapPanel.setLayout(swapPanelLayout);
        swapPanelLayout.setHorizontalGroup(
            swapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swaprateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        swapPanelLayout.setVerticalGroup(
            swapPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swaprateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        seedPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Seed"));

        seedTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        seedTextField.setText("12345");
        seedTextField.setPreferredSize(new java.awt.Dimension(6, 20));

        javax.swing.GroupLayout seedPanelLayout = new javax.swing.GroupLayout(seedPanel);
        seedPanel.setLayout(seedPanelLayout);
        seedPanelLayout.setHorizontalGroup(
            seedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        seedPanelLayout.setVerticalGroup(
            seedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(seedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        tosimilarButton.setText("Add to Similarity Profile");
        tosimilarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tosimilarButtonActionPerformed(evt);
            }
        });

        tohierarchyButton.setText("Add to Hierarchy");
        tohierarchyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tohierarchyButtonActionPerformed(evt);
            }
        });

        toriskButton.setText("Add to Risk");
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

        okButton.setText("Close");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        carryalongPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Carry along"));
        carryalongPanel.setPreferredSize(new java.awt.Dimension(112, 275));

        carryalongScrollPane.setViewportView(carryList);

        downcarry.setText("↓");
        downcarry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downcarryActionPerformed(evt);
            }
        });

        unCarryButton.setText("<<");
        unCarryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unCarryButtonActionPerformed(evt);
            }
        });

        upcarry.setText("↑");
        upcarry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upcarryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout carryalongPanelLayout = new javax.swing.GroupLayout(carryalongPanel);
        carryalongPanel.setLayout(carryalongPanelLayout);
        carryalongPanelLayout.setHorizontalGroup(
            carryalongPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(carryalongScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(carryalongPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(carryalongPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, carryalongPanelLayout.createSequentialGroup()
                        .addComponent(unCarryButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(carryalongPanelLayout.createSequentialGroup()
                        .addComponent(upcarry)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downcarry)
                        .addGap(8, 10, Short.MAX_VALUE))))
        );
        carryalongPanelLayout.setVerticalGroup(
            carryalongPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(carryalongPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(carryalongScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(carryalongPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upcarry)
                    .addComponent(downcarry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(unCarryButton))
        );

        tocarryButton.setText("Add to Carry along");
        tocarryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tocarryButtonActionPerformed(evt);
            }
        });

        MutipleProfilesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Multiple similarity profiles"));

        AddProfileButton.setText("Create New");
        AddProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddProfileButtonActionPerformed(evt);
            }
        });

        DelProfileButton.setText("Remove Selected");
        DelProfileButton.setToolTipText("");
        DelProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelProfileButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MutipleProfilesPanelLayout = new javax.swing.GroupLayout(MutipleProfilesPanel);
        MutipleProfilesPanel.setLayout(MutipleProfilesPanelLayout);
        MutipleProfilesPanelLayout.setHorizontalGroup(
            MutipleProfilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MutipleProfilesPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(MutipleProfilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(AddProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DelProfileButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        MutipleProfilesPanelLayout.setVerticalGroup(
            MutipleProfilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MutipleProfilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AddProfileButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DelProfileButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tosimilarButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(tohierarchyButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toriskButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tocarryButton, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MutipleProfilesPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(similarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hierachyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(riskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carryalongPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(hidPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kanonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(swapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(seedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(calculateButton)
                    .addComponent(undoButton)
                    .addComponent(okButton))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(hidPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kanonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(swapPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(calculateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(undoButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(variablesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(carryalongPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                                    .addComponent(riskPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                                    .addComponent(hierachyPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(similarPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(tosimilarButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tohierarchyButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toriskButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tocarryButton)
                                .addGap(15, 15, 15)
                                .addComponent(MutipleProfilesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (this.model.getTargetSwappings().size() > 0){ 
            TargetSwappingSpec spec = this.model.getTargetSwappings().get(this.model.getTargetSwappings().size() - 1);
            if (!spec.getIsCalculated()){
                if (!this.showConfirmDialog("TRS is not applied yet. Are you sure you want to close this window?"))
                    return;
            }
        }
        else if (this.calculateButton.isEnabled()){
                if(!this.showConfirmDialog("TRS is not applied yet. Are you sure you want to close this window?"))
                    return;
        }
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
    
    private void tosimilarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tosimilarButtonActionPerformed
        int SelectedProfile = this.similarTabbedPane.getSelectedIndex();
        toSelectionList(this.similarListModels.get(SelectedProfile));
        updateValues();
    }//GEN-LAST:event_tosimilarButtonActionPerformed

    private void tohierarchyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tohierarchyButtonActionPerformed
        toSelectionList(this.hierarchyListModel);
        updateValues();
    }//GEN-LAST:event_tohierarchyButtonActionPerformed

    private void toriskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toriskButtonActionPerformed
        toSelectionList(this.riskListModel);
        updateValues();
    }//GEN-LAST:event_toriskButtonActionPerformed

    private void unSimilarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unSimilarButtonActionPerformed
        int SelectedProfile = this.similarTabbedPane.getSelectedIndex();
        fromSelectionList(this.similarListModels.get(SelectedProfile), this.similarLists.get(SelectedProfile));
        updateValues();
    }//GEN-LAST:event_unSimilarButtonActionPerformed

    private void unHierarchyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unHierarchyButtonActionPerformed
        fromSelectionList(this.hierarchyListModel, this.hierarchyList);
        updateValues();
    }//GEN-LAST:event_unHierarchyButtonActionPerformed

    private void unRiskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unRiskButtonActionPerformed
        fromSelectionList(this.riskListModel, this.riskList);
        updateValues();
    }//GEN-LAST:event_unRiskButtonActionPerformed

    private void upsimilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upsimilarActionPerformed
        int SelectedProfile = this.similarTabbedPane.getSelectedIndex();
        moveUpinList(this.similarListModels.get(SelectedProfile),this.similarLists.get(SelectedProfile));
    }//GEN-LAST:event_upsimilarActionPerformed

    private void uphierarchyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uphierarchyActionPerformed
        moveUpinList(this.hierarchyListModel,this.hierarchyList);
    }//GEN-LAST:event_uphierarchyActionPerformed

    private void upriskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upriskActionPerformed
        moveUpinList(this.riskListModel,this.riskList);
    }//GEN-LAST:event_upriskActionPerformed

    private void downsimilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downsimilarActionPerformed
        int SelectedProfile = this.similarTabbedPane.getSelectedIndex();
        moveDowninList(this.similarListModels.get(SelectedProfile),this.similarLists.get(SelectedProfile));
    }//GEN-LAST:event_downsimilarActionPerformed

    private void downhierarchyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downhierarchyActionPerformed
        moveDowninList(this.hierarchyListModel,this.hierarchyList);
    }//GEN-LAST:event_downhierarchyActionPerformed

    private void downriskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downriskActionPerformed
        moveDowninList(this.riskListModel,this.riskList);
    }//GEN-LAST:event_downriskActionPerformed

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        getController().calculate();
        updateValues();
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        getController().undo();
        updateValues();
    }//GEN-LAST:event_undoButtonActionPerformed

    private void upcarryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upcarryActionPerformed
        moveUpinList(this.carryListModel,this.carryList);
    }//GEN-LAST:event_upcarryActionPerformed

    private void downcarryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downcarryActionPerformed
        moveDowninList(this.carryListModel,this.carryList);
    }//GEN-LAST:event_downcarryActionPerformed

    private void unCarryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unCarryButtonActionPerformed
        fromSelectionList(this.carryListModel, this.carryList);
        updateValues();
    }//GEN-LAST:event_unCarryButtonActionPerformed

    private void tocarryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tocarryButtonActionPerformed
        toSelectionList(this.carryListModel);
        updateValues();
    }//GEN-LAST:event_tocarryButtonActionPerformed

    private void AddProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddProfileButtonActionPerformed
        this.similarScrollPanes.add(new javax.swing.JScrollPane());
        this.similarLists.add(new javax.swing.JList<>());
        this.similarScrollPanes.get(this.similarScrollPanes.size()-1).setViewportView(this.similarLists.get(this.similarLists.size()-1));
        this.similarListModels.add(new DefaultListModel<>());
        
        this.similarLists.get(this.similarLists.size()-1).setModel(this.similarListModels.get(this.similarListModels.size()-1));
        this.similarLists.get(this.similarLists.size()-1).setCellRenderer(new VariableNameCellRenderer());
        
        this.similarTabbedPane.addTab(Integer.toString(this.similarScrollPanes.size()), this.similarScrollPanes.get(this.similarScrollPanes.size()-1));
        this.similarTabbedPane.setSelectedIndex(this.similarScrollPanes.size()-1);
    }//GEN-LAST:event_AddProfileButtonActionPerformed

    private void DelProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelProfileButtonActionPerformed
        if (this.similarTabbedPane.getTabCount()>1) {
            int removeID = this.similarTabbedPane.getSelectedIndex();
            this.similarTabbedPane.remove(removeID);
            this.similarScrollPanes.remove(removeID);
            this.similarLists.remove(removeID);
            this.similarListModels.remove(removeID);
            for (int i=removeID;i<this.similarTabbedPane.getTabCount();i++){
                this.similarTabbedPane.setTitleAt(i, Integer.toString(Integer.parseInt(this.similarTabbedPane.getTitleAt(i))-1));
            }
        }
    }//GEN-LAST:event_DelProfileButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddProfileButton;
    private javax.swing.JButton DelProfileButton;
    private javax.swing.JPanel MutipleProfilesPanel;
    private javax.swing.JButton calculateButton;
    private javax.swing.JList<VariableMu> carryList;
    private javax.swing.JPanel carryalongPanel;
    private javax.swing.JScrollPane carryalongScrollPane;
    private javax.swing.JButton downcarry;
    private javax.swing.JButton downhierarchy;
    private javax.swing.JButton downrisk;
    private javax.swing.JButton downsimilar;
    private javax.swing.JTextField hhIDTextField;
    private javax.swing.JPanel hidPanel;
    private javax.swing.JPanel hierachyPanel;
    private javax.swing.JList<VariableMu> hierarchyList;
    private javax.swing.JScrollPane hierarchyScrollPane;
    private javax.swing.JPanel kanonPanel;
    private javax.swing.JTextField kthresholdTextField;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JList<VariableMu> riskList;
    private javax.swing.JPanel riskPanel;
    private javax.swing.JScrollPane riskScrollPane;
    private javax.swing.JPanel seedPanel;
    private javax.swing.JTextField seedTextField;
    private javax.swing.JList<VariableMu> similarList;
    private javax.swing.JPanel similarPanel;
    private javax.swing.JScrollPane similarScrollPane;
    private javax.swing.JTabbedPane similarTabbedPane;
    private javax.swing.JPanel swapPanel;
    private javax.swing.JTextField swaprateTextField;
    private javax.swing.JButton tocarryButton;
    private javax.swing.JButton tohierarchyButton;
    private javax.swing.JButton toriskButton;
    private javax.swing.JButton tosimilarButton;
    private javax.swing.JButton unCarryButton;
    private javax.swing.JButton unHierarchyButton;
    private javax.swing.JButton unRiskButton;
    private javax.swing.JButton unSimilarButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JButton upcarry;
    private javax.swing.JButton uphierarchy;
    private javax.swing.JButton uprisk;
    private javax.swing.JButton upsimilar;
    private javax.swing.JPanel variablesPanel;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JTable variablesTable;
    // End of variables declaration//GEN-END:variables
}
