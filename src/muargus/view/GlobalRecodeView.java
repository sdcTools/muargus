package muargus.view;

import argus.model.ArgusException;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.HighlightTableCellRenderer;
import muargus.controller.GlobalRecodeController;
import muargus.model.GlobalRecode;
import muargus.model.RecodeMu;
import muargus.model.VariableMu;

/**
 * View class of the GlobalRecode screen.
 *
 * @author Statistics Netherlands
 */
public class GlobalRecodeView extends DialogBase<GlobalRecodeController> {

    private GlobalRecode model;
    private RecodeMu selectedRecode;
    private RecodeMu selectedRecodeClone;
    private final int[] columnWidth = {30, 70}; // gives the width of column 1, 2

    /**
     * Creates new GlobalRecodeView.
     *
     * @param parent the Frame of the mainFrame.
     * @param modal boolean to set the modal status
     * @param controller the controller of this view.
     */
    public GlobalRecodeView(java.awt.Frame parent, boolean modal, GlobalRecodeController controller) {
        super(parent, modal, controller);
        initComponents();
        this.setLocationRelativeTo(null);
        this.variablesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the data. The model is set, recodeMu's are made for each
     * variable, the table is set (values, selectionModel, width and cell
     * renderer) and the values are set.
     */
    @Override
    public void initializeData() {
        this.model = getMetadata().getCombinations().getGlobalRecode();
        if (this.model.getRecodeMus().isEmpty()) {
            for (VariableMu v : this.model.getVariables()) {
                RecodeMu recodeMu = new RecodeMu(v);
                this.model.addRecodeMu(recodeMu);
            }
        }

        this.updateTable();
        this.variablesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!lse.getValueIsAdjusting()) {
                    handleSelectionChanged();
                }
            }
        });

        for (int i = 0; i < this.columnWidth.length; i++) {
            this.variablesTable.getColumnModel().getColumn(i).setMinWidth(this.columnWidth[i]);
            this.variablesTable.getColumnModel().getColumn(i).setPreferredWidth(this.columnWidth[i]);
        }

        this.variablesTable.setDefaultRenderer(Object.class, new HighlightTableCellRenderer());

        updateValues();
    }

    /**
     * Updates the table containing the type of recoding and the variable name.
     */
    public void updateTable() {
        String[][] data = new String[this.model.getRecodeMus().size()][2];
        int index = 0;
        for (RecodeMu r : model.getRecodeMus()) {
            data[index] = r.getTableRow();
            index++;
        }

        TableModel tableModel = new DefaultTableModel(data, this.model.getColumnNames()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.variablesTable.setModel(tableModel);
    }

    /**
     * Updates the values. Sets the missing values, file names, global recode
     * text and enables/disables the truncated and undo button.
     */
    private void updateValues() {
        //this.updateTable();
        //System.out.println(variablesTable.getSelectedRow());
        //this.variablesTable.getSelectionModel().setSelectionInterval(variablesTable.getSelectedRow(), variablesTable.getSelectedRow());

        RecodeMu selected = this.getSelectedRecode();
        if (selected == null) {
            return;
        }
        this.missing_1_originalTextField.setText(selected.getMissing_1_original());
        this.missing_2_originalTextField.setText(selected.getMissing_2_original());
        this.missing_1_newTextField.setText(selected.getMissing_1_new());
        this.missing_2_newTextField.setText(selected.getMissing_2_new());
        this.codelistRecodeTextField.setText(selected.getCodeListFile());
        this.globalRecodeTextField.setText(selected.getGrcFile());
        this.truncateButton.setEnabled(selected.getVariable().isTruncable());
        enableApplyButton();
        this.undoButton.setEnabled(selected.isRecoded() || selected.isTruncated());
        this.editTextArea.setText(selected.getGrcText());
    }

    /**
     * Enables the apply button. Only enable apply when there is something in
     * the Grc text box.
     */
    private void enableApplyButton() {
        this.applyButton.setEnabled(this.editTextArea.getText().length() > 0);
    }

    /**
     * Gets the selected RecodeMu containing the recoding information for this
     * variable.
     *
     * @return RecodeMu containing the recoding information for this variable.
     */
    private RecodeMu getSelectedRecode() {
        return this.selectedRecode;
    }

    /**
     * Gets the global recode file path.
     *
     * @return String containing the global recode file path.
     */
    private String askForGrcPath() {
        return showFileDialog("Open Recode File", false, new String[]{"Recode files (*.grc)|grc"});
//        JFileChooser fileChooser = new JFileChooser();
//        String hs = SystemUtils.getRegString("general", "datadir", "");
//        if (!hs.equals("")){
//            File file = new File(hs); 
//            fileChooser.setCurrentDirectory(file);
//        }
//        fileChooser.setDialogTitle("Open Codelist File");
//        fileChooser.setSelectedFile(new File(""));
//        fileChooser.resetChoosableFileFilters();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("Recode files (*.grc)", "grc"));
//        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//            //codelistfileTextField.setText(fileChooser.getSelectedFile().toString());
//            hs = fileChooser.getSelectedFile().getPath();
//            if (!hs.equals("")){
//                SystemUtils.putRegString("general", "datadir", hs);
//            }
//            return fileChooser.getSelectedFile().toString();
//        }
//        return null;
    }

    /**
     * Gets the user input for the number of positions that need to be
     * truncated.
     *
     * @param varLength Integer containing the length of the variable.
     * @return Integer containing the number of positions that need to be
     * truncated.
     */
    private int getTruncatePositions(int varLength) {
        while (true) {
            String result = JOptionPane.showInputDialog(null, "Number of Digits", 1);
            if (result == null || result.length() == 0) {
                return 0;
            }

            String message = "Illegal input value";
            try {
                Integer positions = Integer.parseInt(result);
                if (positions > 0) {
                    if (positions < varLength) {
                        return positions;
                    }
                    message = "You cannot truncate more than the width of the field.";
                }
            } catch (NumberFormatException ex) {
                //No action needed
            }
            showMessage(message);
        }
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
        variablesTable = new javax.swing.JTable();
        editLabel = new javax.swing.JLabel();
        editScrollPane = new javax.swing.JScrollPane();
        editTextArea = new javax.swing.JTextArea();
        codelistRecodeLabel = new javax.swing.JLabel();
        codelistRecodeTextField = new javax.swing.JTextField();
        codelistRecodeButton = new javax.swing.JButton();
        globalRecodeTextField = new javax.swing.JTextField();
        warningLabel = new javax.swing.JLabel();
        warningScrollPane = new javax.swing.JScrollPane();
        warningTextArea = new javax.swing.JTextArea();
        midSectionPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        readButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        truncateButton = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        missingValuesPanel = new javax.swing.JPanel();
        originalValuesLabel = new javax.swing.JLabel();
        missing_1_originalLabel = new javax.swing.JLabel();
        missing_1_originalTextField = new javax.swing.JTextField();
        missing_2_originalLabel = new javax.swing.JLabel();
        missing_2_originalTextField = new javax.swing.JTextField();
        valuesAfterRecoding = new javax.swing.JLabel();
        mising_1_newLabel = new javax.swing.JLabel();
        missing_1_newTextField = new javax.swing.JTextField();
        missing_2_newLabel = new javax.swing.JLabel();
        missing_2_newTextField = new javax.swing.JTextField();
        closePanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        codelistRecodeLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Global Recode");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(620, 530));

        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "R", "Variable"
            }
        ));
        variablesTable.setDragEnabled(true);
        variablesTable.getTableHeader().setReorderingAllowed(false);
        variablesScrollPane.setViewportView(variablesTable);
        if (variablesTable.getColumnModel().getColumnCount() > 0) {
            variablesTable.getColumnModel().getColumn(0).setPreferredWidth(6);
        }

        editLabel.setText("Edit box for global recode");

        editTextArea.setColumns(20);
        editTextArea.setRows(5);
        editTextArea.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                editTextAreaCaretUpdate(evt);
            }
        });
        editScrollPane.setViewportView(editTextArea);

        codelistRecodeLabel.setText("Codelist for recode");

        codelistRecodeTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                codelistRecodeTextFieldCaretUpdate(evt);
            }
        });

        codelistRecodeButton.setText("...");
        codelistRecodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codelistRecodeButtonActionPerformed(evt);
            }
        });

        globalRecodeTextField.setEditable(false);

        warningLabel.setText("Warning");

        warningTextArea.setEditable(false);
        warningTextArea.setBackground(new java.awt.Color(200, 200, 200));
        warningTextArea.setColumns(20);
        warningTextArea.setRows(5);
        warningScrollPane.setViewportView(warningTextArea);

        buttonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        readButton.setText("Read");
        readButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readButtonActionPerformed(evt);
            }
        });

        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        truncateButton.setText("Truncate");
        truncateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                truncateButtonActionPerformed(evt);
            }
        });

        undoButton.setText("Undo");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(readButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(applyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(truncateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(undoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(readButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(applyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(truncateButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(undoButton)
                .addContainerGap())
        );

        missingValuesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Missing Values"));

        originalValuesLabel.setText("Original values");

        missing_1_originalLabel.setText("1");

        missing_1_originalTextField.setEditable(false);

        missing_2_originalLabel.setText("2");

        missing_2_originalTextField.setEditable(false);

        valuesAfterRecoding.setText("Values after recoding");

        mising_1_newLabel.setText("1");

        missing_1_newTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                missing_1_newTextFieldCaretUpdate(evt);
            }
        });

        missing_2_newLabel.setText("2");

        missing_2_newTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                missing_2_newTextFieldCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout missingValuesPanelLayout = new javax.swing.GroupLayout(missingValuesPanel);
        missingValuesPanel.setLayout(missingValuesPanelLayout);
        missingValuesPanelLayout.setHorizontalGroup(
            missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(missingValuesPanelLayout.createSequentialGroup()
                .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(missingValuesPanelLayout.createSequentialGroup()
                        .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(missing_2_originalLabel)
                            .addComponent(missing_1_originalLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(missing_1_originalTextField)
                            .addComponent(missing_2_originalTextField)))
                    .addGroup(missingValuesPanelLayout.createSequentialGroup()
                        .addComponent(mising_1_newLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(missing_1_newTextField))
                    .addGroup(missingValuesPanelLayout.createSequentialGroup()
                        .addComponent(originalValuesLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(missingValuesPanelLayout.createSequentialGroup()
                        .addComponent(missing_2_newLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(missing_2_newTextField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, missingValuesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(valuesAfterRecoding)))
                .addContainerGap())
        );
        missingValuesPanelLayout.setVerticalGroup(
            missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(missingValuesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(originalValuesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missing_1_originalLabel)
                    .addComponent(missing_1_originalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(missing_2_originalLabel)
                    .addComponent(missing_2_originalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(valuesAfterRecoding)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missing_1_newTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mising_1_newLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(missingValuesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missing_2_newTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missing_2_newLabel))
                .addContainerGap())
        );

        closePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout closePanelLayout = new javax.swing.GroupLayout(closePanel);
        closePanel.setLayout(closePanelLayout);
        closePanelLayout.setHorizontalGroup(
            closePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(closeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        closePanelLayout.setVerticalGroup(
            closePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(closePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(closeButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout midSectionPanelLayout = new javax.swing.GroupLayout(midSectionPanel);
        midSectionPanel.setLayout(midSectionPanelLayout);
        midSectionPanelLayout.setHorizontalGroup(
            midSectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, midSectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(midSectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(closePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(missingValuesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        midSectionPanelLayout.setVerticalGroup(
            midSectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(midSectionPanelLayout.createSequentialGroup()
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(missingValuesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(closePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        codelistRecodeLabel1.setText("Global recode file");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midSectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(warningScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addComponent(editScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(globalRecodeTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codelistRecodeLabel1)
                            .addComponent(warningLabel)
                            .addComponent(editLabel)
                            .addComponent(codelistRecodeLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(codelistRecodeTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codelistRecodeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(midSectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(variablesScrollPane)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editScrollPane)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(codelistRecodeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(codelistRecodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(codelistRecodeButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(codelistRecodeLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(globalRecodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(warningLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(warningScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        handleSelectionChanged();
        getController().close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void codelistRecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codelistRecodeButtonActionPerformed
        String filePath = showFileDialog("Open Codelist File", false, new String[]{"Codelist (*.cdl)|cdl"});
        if (filePath != null) {
            setCodelistText(filePath);
        }
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("Codelist (*.cdl)", "cdl"));
//        String hs = SystemUtils.getRegString("general", "datadir", "");
//        if (!hs.equals("")){
//            File file = new File(hs); 
//            fileChooser.setCurrentDirectory(file);
//        }        
//        fileChooser.showOpenDialog(null);
//
//        String filename;
//        File f = fileChooser.getSelectedFile();
//        if (fileChooser.getSelectedFile() == null) {
//            filename = "";
//        } else {
//            filename = f.getAbsolutePath();
//        }
//        view.setCodelistText(filename);
    }//GEN-LAST:event_codelistRecodeButtonActionPerformed

    private void truncateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_truncateButtonActionPerformed
        try {
            int positions = getTruncatePositions(getSelectedRecode().getVariable().getVariableLength());
            if (positions > 0) {
                getController().truncate(getSelectedRecode(), positions);
                int rowIndex = this.model.getVariables().indexOf(getSelectedRecode().getVariable());
                variablesTable.getModel().setValueAt("T", rowIndex, 0);
                variablesTable.getModel().setValueAt(getSelectedRecode().getVariable().getName(), rowIndex, 1);
                updateValues();
            }

        } catch (ArgusException ex) {
            showErrorMessage(ex);
        }
    }//GEN-LAST:event_truncateButtonActionPerformed


    private void readButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readButtonActionPerformed
        String path = askForGrcPath();
        if (path != null) {
            try {
                getController().read(path, this.getSelectedRecode());
                this.selectedRecodeClone = new RecodeMu(this.selectedRecode);
                updateValues();
            } catch (ArgusException ex) {
                showErrorMessage(ex);
            }
        }
    }//GEN-LAST:event_readButtonActionPerformed

    /**
     * Shows the warning text in the warning textField.
     *
     * @param warning String containing the warning message.
     */
    public void showWarning(String warning) {
        warningTextArea.setText(warning);
    }

    /**
     * Sets the selected index.
     *
     * @param index Integer containing the selected index.
     */
    public void setSelectedIndex(int index) {
        this.variablesTable.getSelectionModel().setSelectionInterval(index, index);
    }

    /**
     * Gets the selected index.
     *
     * @return Integer containing the selected index.
     */
    public int getSelectedIndex() {
        return this.variablesTable.getSelectionModel().getMaxSelectionIndex();
    }

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        try {
            fixGrcReturns(getSelectedRecode());
            getController().apply(getSelectedRecode());
            int rowIndex = this.model.getVariables().indexOf(getSelectedRecode().getVariable());
            variablesTable.getModel().setValueAt("R", rowIndex, 0);
            variablesTable.getModel().setValueAt(getSelectedRecode().getVariable().getName(), rowIndex, 1);
            updateValues();
        } catch (ArgusException ex) {
            showErrorMessage(ex);
        }
    }//GEN-LAST:event_applyButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        try {
            getController().undo(getSelectedRecode());
            int rowIndex = this.model.getVariables().indexOf(getSelectedRecode().getVariable());
            variablesTable.getModel().setValueAt("", rowIndex, 0);
            variablesTable.getModel().setValueAt(getSelectedRecode().getVariable().getName(), rowIndex, 1);
        } catch (ArgusException ex) {
            showErrorMessage(ex);
        }
    }//GEN-LAST:event_undoButtonActionPerformed

    /**
     * Sets the global recode file text.
     *
     * @param recode Recodemu containing the information of the selected
     * variable.
     */
    private void fixGrcReturns(RecodeMu recode) {
        recode.setGrcText(recode.getGrcText().replace("\r\n", "\n").replace("\n", "\r\n"));
    }

    /**
     * Saves the global recode file.
     */
    private void saveGrcFile() {
        String filePath = showFileDialog("Save Recode File", true, new String[]{"Recode files (*.grc)|grc"});
        if (filePath != null) {
            try {
                this.selectedRecode.write(new File(filePath));
            } catch (ArgusException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Handler that is activated when a different variable is selected. Askes if
     * the recode information needs to be safed when it has been changed,
     * changes the selected recodeMu, selected recodeMu clone and updates the
     * values.
     */
    private void handleSelectionChanged() {
        if (this.selectedRecodeClone != null) {
            if (!selectedRecodeClone.equals(getSelectedRecode())) {
                if (showConfirmDialog("Recode information has been changed.\nSave recode file?")) {
                    saveGrcFile();
                }
            }
        }
        this.selectedRecode = model.getRecodeMus().get(variablesTable.getSelectedRow());
        this.selectedRecodeClone = new RecodeMu(getSelectedRecode());
        updateValues();
    }

    private void editTextAreaCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_editTextAreaCaretUpdate
        getSelectedRecode().setGrcText(this.editTextArea.getText());
        enableApplyButton();
    }//GEN-LAST:event_editTextAreaCaretUpdate

    private void missing_1_newTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_missing_1_newTextFieldCaretUpdate
        getSelectedRecode().setMissing_1_new(this.missing_1_newTextField.getText());
    }//GEN-LAST:event_missing_1_newTextFieldCaretUpdate

    private void globalRecodeRecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalRecodeRecodeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_globalRecodeRecodeButtonActionPerformed

    private void missing_2_newTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_missing_2_newTextFieldCaretUpdate
        getSelectedRecode().setMissing_2_new(this.missing_2_newTextField.getText());
    }//GEN-LAST:event_missing_2_newTextFieldCaretUpdate

    private void codelistRecodeTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_codelistRecodeTextFieldCaretUpdate
        getSelectedRecode().setCodeListFile(this.codelistRecodeTextField.getText());
    }//GEN-LAST:event_codelistRecodeTextFieldCaretUpdate

    /**
     * Sets the file name of the Codelist.
     *
     * @param filename String containing the file name of the Codelist.
     */
    private void setCodelistText(String filename) {
        codelistRecodeTextField.setText(filename);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel closePanel;
    private javax.swing.JButton codelistRecodeButton;
    private javax.swing.JLabel codelistRecodeLabel;
    private javax.swing.JLabel codelistRecodeLabel1;
    private javax.swing.JTextField codelistRecodeTextField;
    private javax.swing.JLabel editLabel;
    private javax.swing.JScrollPane editScrollPane;
    private javax.swing.JTextArea editTextArea;
    private javax.swing.JTextField globalRecodeTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel midSectionPanel;
    private javax.swing.JLabel mising_1_newLabel;
    private javax.swing.JPanel missingValuesPanel;
    private javax.swing.JTextField missing_1_newTextField;
    private javax.swing.JLabel missing_1_originalLabel;
    private javax.swing.JTextField missing_1_originalTextField;
    private javax.swing.JLabel missing_2_newLabel;
    private javax.swing.JTextField missing_2_newTextField;
    private javax.swing.JLabel missing_2_originalLabel;
    private javax.swing.JTextField missing_2_originalTextField;
    private javax.swing.JLabel originalValuesLabel;
    private javax.swing.JButton readButton;
    private javax.swing.JButton truncateButton;
    private javax.swing.JButton undoButton;
    private javax.swing.JLabel valuesAfterRecoding;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JTable variablesTable;
    private javax.swing.JLabel warningLabel;
    private javax.swing.JScrollPane warningScrollPane;
    private javax.swing.JTextArea warningTextArea;
    // End of variables declaration//GEN-END:variables

}
