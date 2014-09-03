package muargus.view;//GEN-FIRST:event_automaticSpecificationButtonActionPerformed
//GEN-LAST:event_automaticSpecificationButtonActionPerformed
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
import muargus.model.Combinations;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import muargus.MuARGUS;
import muargus.CombinationsTableCellRenderer;

/**
 *
 * @author ambargus
 */
public class SelectCombinationsView extends DialogBase {

    private final SelectCombinationsController controller;
    private Combinations model;
    private MetadataMu metadataMu;
    private DefaultListModel variablesListModel;
    private DefaultListModel variablesSelectedListModel;
    private TableModel tableModel;
    private final Frame parent;
    private long numberOfTables;
    private final int[] columnWidth = new int[]{30,50,70}; // gives the width of column 1, 2 and the final value is the width of all the other columns

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
        //this.model = this.controller.getModel();
        this.setLocationRelativeTo(null);
        variablesList.setCellRenderer(new VariableNameCellRenderer());
        variablesSelectedList.setCellRenderer(new VariableNameCellRenderer());
    }

    public void setModel(Combinations model) {
        this.model = model;
        makeVariables();
    }

    /**
     * Sets the metadata and calls the method to fill the variableList
     *
     * @param metadataMu Metadata Class containing all the metadata (variables
     * etc)
     */
    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.setDefaultRenderer(Object.class, new CombinationsTableCellRenderer());

        updateValues();
    }

    /**
     * Updates the table by filling it with the array of tables.
     */
    private void updateValues() {
        this.thresholdTextField.setText(this.model.getThreshold());
        // gets the tables from Combinations and adds these to a double  array, containing the data
        ArrayList<TableMu> tables = model.getTables();
        String[][] data = new String[model.getTables().size()][model.getNumberOfColumns()];

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

        tableModel = new DefaultTableModel(data, columnNames.toArray()){
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        table.setModel(tableModel);

        // sets the size of each column
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            int colWidth;
            if(i> 2){
                colWidth = this.columnWidth[2];
            } else{
                colWidth = this.columnWidth[i];
            }
            table.getColumnModel().getColumn(i).setMinWidth(colWidth);
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
                new Object[][]{
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String[]{
                    "R", "Thres.", "Var 1", "Var 2", "Var 3"
                }
        ));
        table.getTableHeader().setReorderingAllowed(false);
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
    }// </editor-fold>        
    
    public void enableCalculateTables(boolean enabled){
        this.calculateTablesButton.setEnabled(enabled);
    }

    private void calculateTablesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //TODO: does not work yet
        try {
            controller.calculateTables();
        } catch (ArgusException ex) {
            ;
        }
    }

    private void moveToSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
    }

    private void removeFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        for (Object o : variablesSelectedList.getSelectedValuesList()) {
            variablesSelectedListModel.removeElement(o);
        }
        variablesSelectedList.setSelectedIndex(0);
    }

    private void removeAllFromSelectedButtonActionPerformed(java.awt.event.ActionEvent evt) {
        variablesSelectedListModel.removeAllElements();
        variablesList.setSelectedIndex(0);
    }

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

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
            if (model.getNumberOfRows() > 0 && model.getNumberOfRows() < model.getMaximumSizeBeforeUserConfirmation()) {
                for (int i = 0; i < model.getNumberOfRows(); i++) {
                    TableMu tableMuOld = model.getTables().get(i);
                    add = compareRows(tableMuNew, tableMuOld);
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
    }

    /**
     * This function compares the different tables with a new table (VariableMu
     * array) if the table is different (enough), which depends on the
     * riskmodel, it returns true if the table has to much overlap, it returns
     * false
     *
     * @param tableMuNew
     * @param tableMuOld
     * @return It returns if a table can be added
     */
    public boolean compareRows(TableMu tableMuNew, TableMu tableMuOld) {
        boolean isValid = true;
        boolean exit = false;
        int numberOfDoubleVariables = 0;

        for (VariableMu oldVariable : tableMuOld.getVariables()) {
            for (VariableMu newVariable : tableMuNew.getVariables()) {
                if (oldVariable.equals(newVariable)) {
                    numberOfDoubleVariables++;
                    if (tableMuOld.isRiskModel()) {
                        return false;
                    }
                }
            }
        }
            if (tableMuNew.getVariables().size() == tableMuOld.getVariables().size()
                    && numberOfDoubleVariables == tableMuNew.getVariables().size()) {
                int thresholdOld = tableMuOld.getThreshold();
                int thresholdNew = tableMuNew.getThreshold();
                if (thresholdNew > thresholdOld) {
                    tableMuOld.setThreshold(thresholdNew);
                } else {
                    tableMuNew.setThreshold(thresholdOld);
                }
                isValid = false;
//                exit = true;
//            }
//            if (exit) {
//                break;
//            }
        }
        return isValid;
    }

    private void removeRowButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
    }

    private void automaticSpecificationButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
                if (getNumberOfTables() > this.model.getMaximumSizeBeforeUserConfirmation()) {
                    if (JOptionPane.showConfirmDialog(this, "Are you sure that you want to generate " + getNumberOfTables() + " tables?", "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        calculateTablesForDimensions(allValidVariables, dimensions);
                    }
                } else {
                    calculateTablesForDimensions(allValidVariables, dimensions);
                }
            }
            if (generateAutomaticTables.isUseIdentificatinLevelRadioButton()) {
                calculateTablesForID(numberOfLevels, variables, allValidVariables);
            }
        }

        //TODO: progressbar laten zien?
        // removes double tables
        if (numberOfOldTables < this.model.getMaximumNumberOfTables()) {
            for (int i = 0; i < numberOfOldTables; i++) {
                for (int j = model.getNumberOfRows() - 1; j >= numberOfOldTables; j--) {
                    if (!compareRows(model.getTables().get(i), model.getTables().get(j))) {
                        model.removeTable(model.getTables().get(j));
                    }
                }
            }
        }

        boolean risk = model.isRiskModel();
        if (risk && numberOfOldTables < this.model.getMaximumNumberOfTables()) {
            this.removeTableRiskModel(this.getListOfRemovedTables());
        }

        updateValues();
    }

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
        this.progressbar.setValue((Integer) value);
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

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.clear();
    }

    public ArrayList<TableMu> getListOfRemovedTables() {
        ArrayList<TableMu> toBeRemovedTables = new ArrayList<>();

        for (int i = model.getNumberOfRows() - 1; i >= 0; i--) {
            TableMu t = model.getTables().get(i);
            if (!t.isRiskModel()) {
                if (t.contains(this.model.getRiskModelVariables())) {
                    toBeRemovedTables.add(t);
                }
            }
        }
        return toBeRemovedTables;
    }

    public boolean overlappingTables(ArrayList<TableMu> toBeRemovedTables, TableMu tableMu) {
        boolean valid = false;
        if (toBeRemovedTables.size() > 0) {
            if (JOptionPane.showConfirmDialog(this, "Overlapping tables found with this risk table\nDo you want to remove them?", "Mu Argus", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                tableMu.setRiskModel(!tableMu.isRiskModel());  //Revert the change
                valid = true;
            }
        }
        return valid;
    }

    public void removeTableRiskModel(ArrayList<TableMu> toBeRemovedTables) {
        for (TableMu t : toBeRemovedTables) {
            model.removeTable(t);
        }
    }

    private boolean weightVariableExists() {
        for (VariableMu variable : this.metadataMu.getVariables()) {
            if (variable.isWeight()) {
                return true;
            }
        }
        return false;
    }
    
    private void setTableRiskModelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (model.getTables().size() > 0) {

            try { // afvangen geen tabel geselecteerd
                int index = table.getSelectedRow();
                TableMu tableMu = model.getTables().get(index);
                if (!weightVariableExists()) {
                    JOptionPane.showMessageDialog(this, "No weight variable has been specified, so the risk-model cannot be applied");
                    return;
                }
                tableMu.setRiskModel(!tableMu.isRiskModel());

                if (tableMu.isRiskModel()) {  //The table is added to the risk model
                    ArrayList<TableMu> toBeRemovedTables = this.getListOfRemovedTables();
                    this.overlappingTables(toBeRemovedTables, tableMu);
                    this.removeTableRiskModel(toBeRemovedTables);
                }

                updateValues();
                table.getSelectionModel().setSelectionInterval(index, index);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "No table is selected");
            }
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.clear();
        controller.cancel();
    }

    private void thresholdTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {
        try {
            model.setThreshold(this.thresholdTextField.getText());
        } catch (Exception e) {
        }
    }
    // Variables declaration - do not modify                     
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
    // End of variables declaration                   
}
