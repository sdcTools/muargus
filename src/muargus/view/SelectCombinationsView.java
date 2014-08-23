package muargus.view;

import argus.model.ArgusException;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.VariableNameCellRenderer;
import muargus.controller.SelectCombinationsController;
import muargus.model.MetadataMu;
import muargus.model.SelectCombinationsModel;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.MuARGUS;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsView extends javax.swing.JDialog {

    private final SelectCombinationsController controller;
    private SelectCombinationsModel model;
    private MetadataMu metadataMu;
    private DefaultListModel variablesListModel;
    private DefaultListModel variablesSelectedListModel;
    private TableModel tableModel;
    private final Frame parent;
    //private int numberOfVariables;
    private long numberOfTables;

    /**
     * Creates new form SelectCombinationsView
     *
     * @param parent
     * @param modal
     * @param controller
     */
    public SelectCombinationsView(java.awt.Frame parent, boolean modal, SelectCombinationsController controller) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        this.controller = controller;
        this.model = this.controller.getModel();
        this.setLocationRelativeTo(null);
        variablesList.setCellRenderer(new VariableNameCellRenderer());
        variablesSelectedList.setCellRenderer(new VariableNameCellRenderer());
    }

    public void setModel(SelectCombinationsModel model) {
        this.model = model;
    }

    /**
     * Sets the metadata and calls the method to fill the variableList
     *
     * @param metadataMu Metadata Class containing all the metadata (variables
     * etc)
     */
    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
        makeVariables();
    }

    /**
     * Fills the selecCombinationsScreen with it's default values
     */
    public void makeVariables() {
        // make listModels and add the variables that are categorical
        variablesListModel = new DefaultListModel<>();
        variablesSelectedListModel = new DefaultListModel<>();
        for (VariableMu variable : metadataMu.getVariables()) {
            if (variable.isCategorical()) {
                variablesListModel.addElement(variable);
            }
        }
        variablesList.setModel(variablesListModel);

        variablesSelectedList.setModel(variablesSelectedListModel);
        if (variablesListModel.getSize() > 0) {
            variablesList.setSelectedIndex(0);
        }

        // set the default values and the size of the first two colums
        this.thresholdTextField.setText(this.model.getThreshold());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setMinWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);

        updateValues();
    }

    /**
     * Updates the table by filling it with the array of tables.
     */
    private void updateValues() {
        // gets the tables from SelectCombinationsModel and adds these to a double  array, containing the data
        ArrayList<TableMu> tables = model.getTables();
        String[][] data = new String[model.getTables().size()][model.getNumberOfColumns()];

        // nog bezig met de check
//        ArrayList<int[]> doubleIndices = new ArrayList<>();
//        for (int i = 0; i < tables.size(); i++) {
//            for (int j = i + 1; j < tables.size(); j++) {
//                if (!compaireRows(model.isRiskModel(), tables.get(i), tables.get(j))) {
//                    int[] temp = {i, j};
//                    doubleIndices.add(temp);
//                }
//            }
//        }
//        for (int[] d : doubleIndices) {
//            if (tables.get(d[1]).isRiskModel()) {
//                model.removeTable(d[0]);
//            } else {
//                model.removeTable(d[1]);
//            }
//            //System.out.printf("%d, %d\n", d[0], d[1]);
//        }
        int index = 0;
        for (TableMu t : tables) {
            data[index] = t.getTable();
            index++;
        }

        ArrayList<String> columnNames = new ArrayList<>(Arrays.asList("Risk", "Thres.", "Var 1"));
        if (model.getNumberOfColumns() > 3) {
            for (int i = 2; i <= model.getNumberOfColumns() - 2; i++) {
                columnNames.add("Var " + i);
            }
        }

        tableModel = new DefaultTableModel(data, columnNames.toArray());
        table.setModel(tableModel);

        // sets the size of each column
        for (int i = 2; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setMinWidth(70);
            table.getColumnModel().getColumn(i).setPreferredWidth(70);
        }

        // TODO: check how the rows should be selected
        if (tables.size() == 1) {
            table.getSelectionModel().setSelectionInterval(0, 0);
        }
    }

    /**
     * Removes all the tables
     */
    public void clear() {
        int size = model.getTables().size();
        for (int i = size - 1; i >= 0; i--) {
            model.removeTable(i);
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
        progressLabel = new javax.swing.JLabel();

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
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMinWidth(20);
            table.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addComponent(setTableRiskModelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(progressLabel)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(cancelButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(calculateTablesButton))
                                .addComponent(progressbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(automaticSpecificationButton, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(progressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        try {
            controller.calculateTables();
        } catch (ArgusException ex) {
            ;
        }
    }//GEN-LAST:event_calculateTablesButtonActionPerformed

    private void moveToSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToSelectedButtonActionPerformed
        int[] index = variablesList.getSelectedIndices();
        Object[] variableMu = variablesList.getSelectedValuesList().toArray();

        // checks for all variables if they are already in the variablesSelectedList and if not, adds them.
        for (Object variable : variableMu) {
            boolean variableAlreadyExists = false;
            for (Object o : variablesSelectedListModel.toArray()) {
                if (variable.equals(o)) {
                    variableAlreadyExists = true;
                }
            }
            if (!variableAlreadyExists) {
                variablesSelectedListModel.add(variablesSelectedListModel.getSize(), (VariableMu) variable);
            }
        }

        variablesList.setSelectedIndex(index[index.length - 1] + 1);
    }//GEN-LAST:event_moveToSelectedButtonActionPerformed

    private void removeFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromSelectedButtonActionPerformed
        for (Object o : variablesSelectedList.getSelectedValuesList()) {
            variablesSelectedListModel.removeElement(o);
        }
        variablesSelectedList.setSelectedIndex(0);
    }//GEN-LAST:event_removeFromSelectedButtonActionPerformed

    private void removeAllFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllFromSelectedButtonActionPerformed
        variablesSelectedListModel.removeAllElements();
        variablesList.setSelectedIndex(0);
    }//GEN-LAST:event_removeAllFromSelectedButtonActionPerformed

    public boolean validThreshold() {
        boolean valid;
        try {
            int threshold = Integer.parseInt(thresholdTextField.getText());
            valid = threshold > 0;
        } catch (NumberFormatException e) {
            valid = false;
        }
        return valid;
    }

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        if (variablesSelectedListModel.size() > MuARGUS.MAXDIMS) {
            JOptionPane.showMessageDialog(this, String.format("Cannot add more than %d variables", MuARGUS.MAXDIMS));
        } else if (!validThreshold()) {
            JOptionPane.showMessageDialog(this, "The threshold is not valid, please enter a positive integer");
        } else {
            // copy the selected variables into a VariableMu array
            VariableMu[] variableMu = new VariableMu[variablesSelectedListModel.size()];
            variablesSelectedListModel.copyInto(variableMu);
            TableMu tableMuNew = new TableMu();
            tableMuNew.setThreshold(model.getThreshold());
            for (VariableMu v : variableMu) {
                tableMuNew.addVariable(v);
            }

            boolean add = true;
            // only check for double tables when then number of tables is between 0 and 100
            //TODO: constant
            if (model.getNumberOfRows() > 0 && model.getNumberOfRows() < 100) {
                for (int i = 0; i < model.getNumberOfRows(); i++) {
                    TableMu tableMuOld = model.getTables().get(i);
                    add = compareRows(tableMuOld.isRiskModel(), tableMuNew, tableMuOld);
                    if (!add) {
                        if (tableMuOld.isRiskModel()) {
                            JOptionPane.showMessageDialog(this, 
                                    "The new table overlaps with a BIR table and cannot be added");
                        }
                        break;
                    }
                }
            }

            if (add) {
                if (variablesSelectedListModel.size() > 0) {
                    TableMu tableMu = new TableMu();
                    variablesSelectedListModel.removeAllElements();
                    tableMu.setThreshold(model.getThreshold());
                    tableMu.setVariables(variableMu);
                    model.addTable(tableMu);
                }
            } else if (!model.isRiskModel()) {
                variablesSelectedListModel.removeAllElements();
            }
            updateValues();
            table.getSelectionModel().setSelectionInterval(model.getNumberOfRows() - 1, model.getNumberOfRows() - 1);
        }
    }//GEN-LAST:event_addRowButtonActionPerformed

    /**
     * This function compares the different tables with a new table (VariableMu
     * array) if the table is different (enough), which depends on the
     * riskmodel, it returns true if the table has to much overlap, it returns
     * false
     *
     * @param riskModel boolean that tells if the riskModel is set for at least
     * one table
     * @param tableMuNew
     * @param tableMuOld
     * @return It returns if a table can be added
     */
    public boolean compareRows(boolean riskModel, TableMu tableMuNew, TableMu tableMuOld) {
        boolean isValid = true;
        boolean exit = false;
        int numberOfDoubleVariables = 0;

        for (VariableMu oldVariable : tableMuOld.getVariables()) {
            for (VariableMu newVariable : tableMuNew.getVariables()) {
                if (oldVariable.equals(newVariable)) {
                    numberOfDoubleVariables++;
                }
            }
            if (riskModel && numberOfDoubleVariables > 0) {
                isValid = false;
                exit = true;
            } else if (!riskModel && tableMuNew.getVariables().size() == tableMuOld.getVariables().size()
                    && numberOfDoubleVariables == tableMuNew.getVariables().size()) {
                int thresholdOld = tableMuOld.getThreshold();
                int thresholdNew = tableMuNew.getThreshold();
                if (thresholdNew > thresholdOld) {
                    tableMuOld.setThreshold(thresholdNew);
                } else {
                    tableMuNew.setThreshold(thresholdOld);
                }
                isValid = false;
                exit = true;
            }
            if (exit) {
                break;
            }
        }
        return isValid;
    }

    private void removeRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRowButtonActionPerformed
        if (model.getNumberOfRows() > 0) {
            try {
                int[] selectedRows = table.getSelectedRows();
                variablesSelectedListModel.removeAllElements();
                ArrayList<VariableMu> variableMu = model.getTables().get(selectedRows[selectedRows.length - 1]).getVariables();
                for (int j = 0; j < variableMu.size(); j++) {
                    variablesSelectedListModel.add(j, variableMu.get(j));
                }
                for (int i = selectedRows.length - 1; i > -1; i--) {
                    model.removeTable(selectedRows[i]);
                    model.setNumberOfRows(model.getNumberOfRows() - 1);
                }
            } catch (Exception e) {
                model.setNumberOfRows(model.getNumberOfRows() - 1);
                model.removeTable(model.getNumberOfRows());
            }
            if (model.getNumberOfRows() == 0) {
                this.clear();
            }
        }
        updateValues();
        table.getSelectionModel().setSelectionInterval(0, 0);
    }//GEN-LAST:event_removeRowButtonActionPerformed

    private void automaticSpecificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automaticSpecificationButtonActionPerformed
        // checks if there are tables and askes if they need to be removed
        int numberOfOldTables = model.getNumberOfRows();
        if (model.getNumberOfRows() > 0) {
            if (JOptionPane.showConfirmDialog(this, "Do you want to delete the current set of tables?", "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.clear();
            }
        }

        // make an array for each idLevel (0-5)
        ArrayList<ArrayList<VariableMu>> variables = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ArrayList<VariableMu> array = new ArrayList<>();
            variables.add(array);
        }

        // fill the appropriate array according to the idLevels of the variables
        for (int i = 0; i < variablesListModel.getSize(); i++) {
            VariableMu variable = (VariableMu) variablesListModel.getElementAt(i);
            variables.get(variable.getIdLevel()).add(variable);
        }

        // add all variables with an ID-level higher than 0 to the arrayList of variables.
        int numberOfLevels = 0; // the number of idLevels higher than 0
        ArrayList<VariableMu> allValidVariables = new ArrayList<>();
        for (int i = 1; i < variables.size(); i++) {
            allValidVariables.addAll(variables.get(i));
            if (variables.get(i).size() > 0) {
                numberOfLevels++;
            }
        }

        int numberOfVariables = allValidVariables.size();

        GenerateAutomaticTables generateAutomaticTables = new GenerateAutomaticTables(parent, true, this.model, numberOfVariables);
        generateAutomaticTables.setVisible(true);

        if (generateAutomaticTables.isValid()) {
            if (generateAutomaticTables.isMakeUpToDimensionRadioButton()) {
                int dimensions = generateAutomaticTables.getDimensionTextField();
                this.setNumberOfTables(dimensions, numberOfVariables);
                if (getNumberOfTables() > 100) {
                    if (JOptionPane.showConfirmDialog(this, "Are you sure that you want to generate " + getNumberOfTables() + " tables?", "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        calculateTablesForDimensions(allValidVariables, dimensions);
                    }
                }
            }
            if (generateAutomaticTables.isUseIdentificatinLevelRadioButton()) {
                calculateTablesForID(numberOfLevels, variables, allValidVariables);
            }
        }

        if (numberOfOldTables < 25000) { // tot dit aantal kan die het redelijk goed hebben, maar is die wel +/- 5 seconden aan het rekenen. progressbar laten zien? 
            //TODO: constant
            ArrayList<Integer> remove = new ArrayList<>();
            boolean risk = model.isRiskModel();
            for (int i = 0; i < numberOfOldTables; i++) {
                for (int j = numberOfOldTables; j < model.getNumberOfRows(); j++) {
                    if (!compareRows(risk, model.getTables().get(i), model.getTables().get(j))) {
                        remove.add(i);
                    }
                }
            }
            for (int i = remove.size() - 1; i >= 0; i--) {
                model.removeTable(i);
            }
        }

        updateValues();
    }//GEN-LAST:event_automaticSpecificationButtonActionPerformed

//    public int getNumberOfVariables() {
//        return this.numberOfVariables;
//    }

    public long getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(int dimensions, int numberOfVariables) {
        this.numberOfTabels(1, dimensions, numberOfVariables);
    }

    public void numberOfTabels(long numberOfTables, int dimensions, int numberOfVariables) {
        if (dimensions > 0) {
            long tempNumber = numberOfTables * numberOfVariables;
            if (tempNumber < 0) {
                JOptionPane.showMessageDialog(this, "To ... many ... dimensions ...\nCan't ... visualize :-(");
            } else {
                int tempNumberOfVariables = numberOfVariables - 1;
                int tempDimensions = dimensions - 1;
                numberOfTabels(tempNumber, tempDimensions, tempNumberOfVariables);
            }

        } else if (dimensions == 0) {
            this.numberOfTables = numberOfTables;
        }
    }

    public void setProgress(Object value) {
        this.progressbar.setValue((Integer)value);
    }
    
    public void setStepName(Object value) {
        this.progressLabel.setText(value.toString());
    }
    
    public void calculateTablesForDimensions(ArrayList<VariableMu> data, int dimensions) {
        ArrayList<VariableMu> variableSubset = new ArrayList<>();
        int startPos = 0;
        int threshold = 0;
        calculateTablesForDimensions(startPos, data, dimensions, variableSubset, threshold);
    }

    public void calculateTablesForDimensions(int startPos, ArrayList<VariableMu> allVariables, int dimension,
            ArrayList<VariableMu> variableSubset, int threshold) {
        if (dimension > 0) {
            for (int i = startPos; i < allVariables.size(); i++) {
                //make variable array 
                ArrayList<VariableMu> temp = new ArrayList<>();
                VariableMu s = allVariables.get(i);
                temp.addAll(variableSubset);
                temp.add(s);

                //Make table, add the variable array and add this table to the table array
                TableMu tableMu = new TableMu();
                tableMu.setVariables(temp);
                tableMu.setThreshold(model.getThresholds()[threshold]);
                model.getTables().add(tableMu);

                int d = dimension - 1;
                calculateTablesForDimensions(i + 1, allVariables, d, temp, threshold + 1);
            }
        }
    }

    public void calculateTablesForID(int numberOfLevels, ArrayList<ArrayList<VariableMu>> variables, ArrayList<VariableMu> allValidVariables) {
        int index = 1; // don't add the variables with an ID number of 0
        int _size = 0;
        int currentLevel = 0;
        ArrayList<VariableMu> variableSubset = new ArrayList<>();

        calculateTablesForID(0, index, _size, currentLevel, variableSubset, numberOfLevels, variables, allValidVariables);
    }

    public void calculateTablesForID(int _i, int _index, int _size, int _currentLevel, ArrayList<VariableMu> variableSubset,
            int numberOfLevels, ArrayList<ArrayList<VariableMu>> variables, ArrayList<VariableMu> allVariables) {

        int currentLevel = _currentLevel + 1;
        if (currentLevel <= numberOfLevels) {
            int index = _index;
            int size = _size;

            // find the next idLevel larger than zero and add the number of variables to the size
            for (int u = index; u < variables.size(); u++) {
                if (variables.get(u).size() > 0) {
                    size = size + variables.get(u).size();
                    index = u + 1;
                    break;
                }
            }

            for (int i = _i; i < size; i++) {
                ArrayList<VariableMu> temp = new ArrayList<>();
                temp.addAll(variableSubset);
                temp.add(allVariables.get(i));

                if (temp.size() == numberOfLevels) {
                    TableMu tableMu = new TableMu();
                    tableMu.setVariables(temp);
                    tableMu.setThreshold(model.getThreshold());
                    model.addTable(tableMu);
                }
                calculateTablesForID(i + 1, index, size, currentLevel, temp, numberOfLevels, variables, allVariables);
            }
        }
    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        this.clear();
        //controller.clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void setTableRiskModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTableRiskModelButtonActionPerformed
        if (model.getTables().size() > 0) {
            int index = table.getSelectedRow();
            TableMu tableMu = model.getTables().get(index);
                       
            tableMu.setRiskModel(!tableMu.isRiskModel());
            if (tableMu.isRiskModel()) {  //The table is added to the risk model
                ArrayList<TableMu> toBeRemovedTables = new ArrayList<>();

                for (int i = model.getNumberOfRows() - 1; i >= 0; i--) {
                    TableMu t = model.getTables().get(i);
                    if (!t.isRiskModel()) {
                        if (t.contains(this.model.getRiskModelVariables())) {
                            toBeRemovedTables.add(t);
                        }
                    }
                }
                if (toBeRemovedTables.size() > 0) {
                    if (JOptionPane.showConfirmDialog(this, "Overlapping tables found with this risk table\nDo you want to remove them?", "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        tableMu.setRiskModel(!tableMu.isRiskModel());  //Revert the change
                        return;
                    }
                }
                
                for (TableMu t : toBeRemovedTables) {
                    model.removeTable(t);
                }
            }
            
            updateValues();
            table.getSelectionModel().setSelectionInterval(index, index);
            //table.getSelectionModel().setSelectionInterval(indices[indices.length - 1], indices[indices.length - 1]);
        }
    }//GEN-LAST:event_setTableRiskModelButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.clear();
        controller.cancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void thresholdTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_thresholdTextFieldCaretUpdate
        try {
            model.setThreshold(this.thresholdTextField.getText());
        } catch (Exception e) {
        }
    }//GEN-LAST:event_thresholdTextFieldCaretUpdate
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowButton;
    private javax.swing.JButton automaticSpecificationButton;
    private javax.swing.JButton calculateTablesButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton moveToSelectedButton;
    private javax.swing.JLabel progressLabel;
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
