package muargus.view;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.VariableNameCellRenderer;
import muargus.controller.SelectCombinationsController;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.model.TableMu;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsView extends javax.swing.JDialog {

    SelectCombinationsController controller;
    SelectCombinationsModel model;
    private MetadataMu metadataMu;
    private DefaultListModel variablesListModel;
    private DefaultListModel variablesSelectedListModel;
    private TableModel tableModel;
    private ArrayList<String> columnNames;
    private String[][] data;
    
    /**
     * Creates new form SelectCombinationsView
     */
    public SelectCombinationsView(java.awt.Frame parent, boolean modal, SelectCombinationsController controller, SelectCombinationsModel model) {
        super(parent, modal);
        initComponents();
        this.controller = controller;
        this.model = model;
        this.setLocationRelativeTo(null);
        variablesList.setCellRenderer(new VariableNameCellRenderer());
        variablesSelectedList.setCellRenderer(new VariableNameCellRenderer());
    }

//    public MetadataMu getMetadataMu() {
//        return metadataMu;
//    }

    /**
     * Sets the metadata and calls the method to fill the variableList
     * @param metadataMu Metadata Class containing all the metadata (variables etc)
     */
    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
        makeVariables();
    }
    
    /**
     * Fills the selecCombinationsScreen with it's default values
     */
    public void makeVariables(){
        // make listModels and add the variables that are categorical
        variablesListModel = new DefaultListModel<>(); 
        variablesSelectedListModel = new DefaultListModel<>();
        for (VariableMu variable : metadataMu.getVariables()) {
            if(variable.isCategorical()){
                variablesListModel.addElement(variable);
            }
        }
        variablesList.setModel(variablesListModel);
        variablesSelectedList.setModel(variablesSelectedListModel);
        if (variablesListModel.getSize() > 0) {
            variablesList.setSelectedIndex(0);
        }
        
        // set the default values and the size of the first two colums
        columnNames = new ArrayList<>(Arrays.asList("Risk", "Thres.", "Var 1"));
        this.thresholdTextField.setText(this.model.getThreshold());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setMinWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        
        updateValues();
    }
    
    /**
     * Updates the table by filling it with the array of tables.
     */
    private void updateValues(){
        // gets the tables from SelectCombinationsModel and adds these to a double 
        // array, containing the data
        // TODO: remove the try catch after testing
        ArrayList<TableMu> tables = model.getTables();
        data = new String[model.getTables().size()][];
        try{
            int index = 0;
            for(TableMu t: tables){
                data[index] = t.getTable();
                index++;
            }
        }catch(Exception e){
            System.out.println("something is wrong here");
            System.out.println(model.getTables().size());
        }
        
        // TODO: add a check here that sets the columns to the size of the biggest table (+ 2)
        
        tableModel = new DefaultTableModel(data, columnNames.toArray());
        table.setModel(tableModel);
       
        // sets the size of each column
        for(int i = 2; i < table.getColumnModel().getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setMinWidth(70);
            table.getColumnModel().getColumn(i).setPreferredWidth(70);
        }
        
        // TODO: check how the rows should be selected
        if(tables.size() == 1){
            table.getSelectionModel().setSelectionInterval(0, 0);
        }
    }
    
    /**
     * Removes all the tables
     */
    public void clear(){
        int size =  model.getTables().size();
        for(int i = size - 1; i >=  0; i--){
           model.removeTable(i); 
        }      
        //model.setNumberOfRows(0); // should not be neccesary
        
        // TODO: replace this to an updateColumns method
        int columns = columnNames.size();
        for(int i = columns - 1; i > 2; i--){
            this.columnNames.remove(i);
        }
        
        updateValues();
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
        thresholdLabel = new javax.swing.JLabel();
        thresholdTextField = new javax.swing.JTextField();
        automaticSpecificationButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        setTableRiskModelButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        calculateTablesButton = new javax.swing.JButton();
        progressbar = new javax.swing.JProgressBar();
        tablesScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Specify Combinations");

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

        thresholdLabel.setText("Threshold:");

        thresholdTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                thresholdTextFieldCaretUpdate(evt);
            }
        });

        automaticSpecificationButton.setText("Automatic specification of tables");
        automaticSpecificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automaticSpecificationButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        setTableRiskModelButton.setText("Set table for Risk model");
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

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "R", "Thres.", "Var 1", "Var 2", "Var 3"
            }
        ));
        tablesScrollPane.setViewportView(table);
        table.getColumnModel().getColumn(0).setMinWidth(20);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cancelButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(calculateTablesButton))
                            .addComponent(progressbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(automaticSpecificationButton, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(removeFromSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(moveToSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeAllFromSelectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(thresholdLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(thresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(variablesSelectedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addRowButton)
                            .addComponent(removeRowButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(setTableRiskModelButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(variablesScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(moveToSelectedButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeFromSelectedButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeAllFromSelectedButton))
                                    .addComponent(variablesSelectedScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(thresholdLabel)
                                    .addComponent(thresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(addRowButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeRowButton)))
                        .addGap(18, 18, 18)
                        .addComponent(automaticSpecificationButton)
                        .addGap(18, 18, 18)
                        .addComponent(clearButton)
                        .addGap(18, 18, 18)
                        .addComponent(setTableRiskModelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelButton)
                            .addComponent(calculateTablesButton)))
                    .addComponent(tablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateTablesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateTablesButtonActionPerformed
        //TODO: does not work yet
        controller.calculateTables();
    }//GEN-LAST:event_calculateTablesButtonActionPerformed

    private void moveToSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToSelectedButtonActionPerformed
        int[] index = variablesList.getSelectedIndices();
        Object[] variableMu =  variablesList.getSelectedValuesList().toArray();
        
        // checks for all variables if they are already in the variablesSelectedList and if not, adds them.
        for(Object variable: variableMu){
            boolean variableAlreadyExists = false;
            for(Object o: variablesSelectedListModel.toArray()){
                if(variable.equals(o)){
                    variableAlreadyExists = true;
                }
            }
            if(!variableAlreadyExists){
                variablesSelectedListModel.add(variablesSelectedListModel.getSize(), (VariableMu) variable);
            }
        }
        
        variablesList.setSelectedIndex(index[index.length - 1]+1);
    }//GEN-LAST:event_moveToSelectedButtonActionPerformed

    private void removeFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromSelectedButtonActionPerformed
        for(Object o: variablesSelectedList.getSelectedValuesList()){
                variablesSelectedListModel.removeElement(o);
        }
        variablesSelectedList.setSelectedIndex(0);
    }//GEN-LAST:event_removeFromSelectedButtonActionPerformed

    private void removeAllFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllFromSelectedButtonActionPerformed
        variablesSelectedListModel.removeAllElements();
        variablesList.setSelectedIndex(0);
    }//GEN-LAST:event_removeAllFromSelectedButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        // TODO: hier ben ik mee bezig
        
        if(variablesSelectedListModel.size()> 10){
            // place warning
        } else {

            ArrayList<TableMu> tables = model.getTables();
            VariableMu[] variableMu = new VariableMu[variablesSelectedListModel.size()];
            variablesSelectedListModel.copyInto(variableMu);

            boolean riskModel = false;
            boolean isValid = true;

            for(TableMu t: tables){
                if(t.isRiskModel() == true){
                    riskModel = true;
                }
            }

            if(tables.size()> 0){
                isValid = compaireRows(riskModel, variableMu);
            }

            if(isValid){
                if(variablesSelectedListModel.size()>0){
                    model.setNumberOfRows(model.getNumberOfRows() + 1);
                    int columns = columnNames.size();
                    int sizeNewRow = variablesSelectedListModel.getSize();
                    int addedColumns = (sizeNewRow + 2) - columns;
                    if(addedColumns > 0){
                        for(int i = 0; i< addedColumns; i++){
                            this.columnNames.add("Var " + (columns - 1 + i));
                        }
                    }
                    updateValues();

                    TableMu tableMu = new TableMu();
        //            VariableMu[] variableMu = new VariableMu[variableSelectedListModel.size()];
        //            variableSelectedListModel.copyInto(variableMu);
                    variablesSelectedListModel.removeAllElements();
                    tableMu.setThreshold(model.getThreshold());
                    tableMu.setVariables(variableMu);
                    String[] output = tableMu.getTable();
                    model.addTable(tableMu);
                    int index = 0;
                    for(String s: output){
                        table.setValueAt(s, model.getNumberOfRows() -1, index);
                        index++;
                    }

                    table.updateUI();

                }
            } else {
                variablesSelectedListModel.removeAllElements();
            }
            updateValues();
            table.getSelectionModel().setSelectionInterval(model.getNumberOfRows()-1, model.getNumberOfRows()-1);
        }
    }//GEN-LAST:event_addRowButtonActionPerformed

    //TODO: check how this works when it is combined with automatically generated data and when the riskmodel is set.
    
    /**
     * This function compaires the different tables with a new table (VariableMu array)
     * if the table is different (enough), which depends on the riskmodel, it returns true
     * if the table has to much overlap, it returns false
     * @param riskModel boolean that tells if the riskModel is set for at least one table
     * @param variableMu an array of variables from the to be added table
     * @return It returns if a table can be added
     */
    public boolean compaireRows(boolean riskModel, VariableMu[] variableMu){
        boolean isValid = true;
        boolean exit = false;
        ArrayList<TableMu> tables = model.getTables();
        int numberOfTables = tables.size();
        int numberOfDoubleVariables = 0;
            
        for(int i = 0; i < numberOfTables; i++){
            TableMu table_1 = tables.get(i);
            for(VariableMu v_1: table_1.getVariables()){
                for(VariableMu v_2: variableMu){
                    if(v_1.equals(v_2)){
                        numberOfDoubleVariables++;
                    }
                }
                if(!riskModel && variableMu.length == table_1.getVariables().size() 
                        && numberOfDoubleVariables == variableMu.length){
                    table_1.setThreshold(thresholdTextField.getText());
                    isValid = false;
                    exit = true;
                }
                if(riskModel && numberOfDoubleVariables > 0 ){
                    isValid = false;
                    exit = true;
                }
                if(exit){
                    break;
                }
            }
            if(exit){
                break;
            }
        }
        return isValid;
    }
    
    private void removeRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRowButtonActionPerformed
        if(model.getNumberOfRows() > 0){
            try{
                int[] selectedRows = table.getSelectedRows();
                variablesSelectedListModel.removeAllElements();
                ArrayList<VariableMu> variableMu = model.getTables().get(selectedRows[selectedRows.length - 1]).getVariables();
                for(int j = 0; j < variableMu.size(); j++){
                    variablesSelectedListModel.add(j, variableMu.get(j));
                }
                for(int i = selectedRows.length -1; i > -1; i--){
                    model.removeTable(selectedRows[i]);
                    model.setNumberOfRows(model.getNumberOfRows()-1);
                }
            } catch(Exception e) {
                model.setNumberOfRows(model.getNumberOfRows()-1);
                model.removeTable(model.getNumberOfRows());
            }
            if(model.getNumberOfRows()== 0){
                this.clear();
            }
        }
        updateValues();
        table.getSelectionModel().setSelectionInterval(0, 0);
    }//GEN-LAST:event_removeRowButtonActionPerformed

    private void automaticSpecificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticSpecificationButtonActionPerformed
        // what to do when there are less than 3 variables?
        
        // make an array for each idLevel (0-5)
        ArrayList<ArrayList<VariableMu>> variables = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            ArrayList<VariableMu> array = new ArrayList<>();
            variables.add(array);
        }
        
        // fill the appropriate array according to the idLevels of the variables
        for(int i = 0; i < variablesListModel.getSize(); i++){
            VariableMu variable = (VariableMu) variablesListModel.getElementAt(i);
            variables.get(variable.getIdLevel()).add(variable);
        }
        
        // add all variables with an ID-level higher than 0 to the arrayList of variables.
        ArrayList<VariableMu> allValidVariables = new ArrayList<>();
        for(int i = 1; i< variables.size(); i++){
            allValidVariables.addAll(variables.get(i));
        }
        
        // get the number of idLevels higher than 0
        int idLevels = 0;
        for(ArrayList<VariableMu> v: variables){
            if(v.size()>0){
                idLevels++;
            }
        }
        
        list(allValidVariables, 2);
        
        //prints the variables of each table and the number of tables
        //TODO: remove after testing
//        for(TableMu t: model.getTables()){
//            for(VariableMu v: t.getVariables()){
//                System.out.print(v.getName() + " ");
//            }
//            System.out.println("");
//        }
//        System.out.println(model.getTables().size());
        
        updateValues();
    }//GEN-LAST:event_automaticSpecificationButtonActionPerformed
    
    
    public void list(ArrayList<VariableMu> data, int dimensions){
        ArrayList<VariableMu> variableSubset = new ArrayList<>();
        list(0 , data, dimensions, variableSubset);
    }
    
    public void list(int startPos, ArrayList<VariableMu> allVariables, int dimension, ArrayList<VariableMu> variableSubset){
        if(dimension > 0){
            for(int i = startPos; i< allVariables.size(); i++){
                //make variable array 
                ArrayList<VariableMu> temp = new ArrayList<>();
                VariableMu s = allVariables.get(i);
                temp.addAll(variableSubset);
                temp.add(s);
                
                //Make table, add the variable array and add this table to the table array
                TableMu tableMu = new TableMu();
                tableMu.setVariables(temp);
                model.getTables().add(tableMu);
                
                int d = dimension - 1;
                list(i+1, allVariables, d, temp);
            }
        }
    }
    
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        this.clear();
        controller.clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void setTableRiskModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTableRiskModelButtonActionPerformed
        int[] indices = table.getSelectedRows();
        for(int i = 0;  i < indices.length; i++){
            TableMu tableMu = model.getTables().get(indices[i]);
            tableMu.setRiskModel(!tableMu.isRiskModel());
        }
        updateValues();
        table.getSelectionModel().setSelectionInterval(indices[indices.length -1], indices[indices.length -1]);
    }//GEN-LAST:event_setTableRiskModelButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.clear();
        controller.cancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void thresholdTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_thresholdTextFieldCaretUpdate
        try {
            model.setThreshold(this.thresholdTextField.getText());
        } catch (Exception e){}
    }//GEN-LAST:event_thresholdTextFieldCaretUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowButton;
    private javax.swing.JButton automaticSpecificationButton;
    private javax.swing.JButton calculateTablesButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton moveToSelectedButton;
    private javax.swing.JProgressBar progressbar;
    private javax.swing.JButton removeAllFromSelectedButton;
    private javax.swing.JButton removeFromSelectedButton;
    private javax.swing.JButton removeRowButton;
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
