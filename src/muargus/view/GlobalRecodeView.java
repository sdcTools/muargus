/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.view;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import muargus.controller.GlobalRecodeController;
import muargus.model.GlobalRecode;
import muargus.model.MetadataMu;
import muargus.model.RecodeMu;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class GlobalRecodeView extends javax.swing.JDialog {

    GlobalRecodeController controller;
    GlobalRecode model;
    private MetadataMu metadataMu;  
    private TableModel tableModel;
    private RecodeMu selectedRecode;
    private RecodeMu selectedRecodeClone;

    /**
     * Creates new form GlobalRecodeView
     *
     * @param parent
     * @param modal
     * @param controller
     */
    public GlobalRecodeView(java.awt.Frame parent, boolean modal, GlobalRecodeController controller) {
        super(parent, modal);
        initComponents();
        this.controller = controller;
        //this.model = this.controller.getModel();
        this.setLocationRelativeTo(null);
        this.variablesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    public void setMetadataMu(MetadataMu metadataMu) {
        this.metadataMu = metadataMu;
        this.model = this.metadataMu.getCombinations().getGlobalRecode();
        makeVariables();
    }

    public void makeVariables() {
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
        
        this.variablesTable.getColumnModel().getColumn(0).setMinWidth(30);
        this.variablesTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.variablesTable.getColumnModel().getColumn(1).setMinWidth(70);
        this.variablesTable.getColumnModel().getColumn(1).setPreferredWidth(70);

        updateValues();
    }

    public void updateTable() {

        String[][] data = new String[this.model.getRecodeMus().size()][2];
        int index = 0;
        for (RecodeMu r : model.getRecodeMus()) {
            data[index] = r.getTableRow();
            index++;
        }

        this.tableModel = new DefaultTableModel(data, this.model.getColumnNames()){
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        this.variablesTable.setModel(this.tableModel);
    }

    public void updateValues() {
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
        this.globalRecodeRecodeTextField.setText(selected.getGrcFile());
        this.truncateButton.setEnabled(selected.getVariable().isTruncable());
        enableApplyButton();
        this.undoButton.setEnabled(selected.isRecoded() || selected.isTruncated());
        this.editTextArea.setText(selected.getGrcText());
    }
    
    private void enableApplyButton() {
        //boolean equals = getSelectedRecode().equals(selectedRecodeClone);
        //Only enable apply when there is something in the Grc text box
        this.applyButton.setEnabled(this.editTextArea.getText().length() > 0);
    }

    private RecodeMu getSelectedRecode() {
        return this.selectedRecode;
    }

    private String askForGrcPath() {
        JFileChooser fileChooser = new JFileChooser();
        String hs = SystemUtils.getRegString("general", "datadir", "");
        if (!hs.equals("")){
            File file = new File(hs); 
            fileChooser.setCurrentDirectory(file);
        }
        fileChooser.setDialogTitle("Open Codelist File");
        fileChooser.setSelectedFile(new File(""));
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Recode files (*.grc)", "grc"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            //codelistfileTextField.setText(fileChooser.getSelectedFile().toString());
            hs = fileChooser.getSelectedFile().getPath();
            if (!hs.equals("")){
                SystemUtils.putRegString("general", "datadir", hs);
            }
            return fileChooser.getSelectedFile().toString();
        }
        return null;
    }
    
    private int getTruncatePositions(int varLength) {
        while (true) {
            String result = JOptionPane.showInputDialog(null, "Number of Digits");
            if (result == null || result.length() == 0)
                return 0;
            
            String message = "Illegal input value";
            Integer positions = 0;
            try {
                positions = Integer.parseInt(result);
                if (positions > 0) {
                    if (positions < varLength)
                        return positions;
                    message = "You cannot truncate more than the width of the field.";
                }
            }
            catch (NumberFormatException ex) {
                ; //No action needed
            }
            JOptionPane.showMessageDialog(null, message);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        editTextArea = new javax.swing.JTextArea();
        codelistRecodeLabel = new javax.swing.JLabel();
        codelistRecodeTextField = new javax.swing.JTextField();
        codelistRecodeButton = new javax.swing.JButton();
        globalRecodeRecodeTextField = new javax.swing.JTextField();
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
        jScrollPane1.setViewportView(editTextArea);

        editScrollPane.setViewportView(jScrollPane1);

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

        globalRecodeRecodeTextField.setEditable(false);

        warningLabel.setText("Warning");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(warningScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(codelistRecodeTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codelistRecodeButton))
                    .addComponent(editScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(globalRecodeRecodeTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codelistRecodeLabel1)
                            .addComponent(warningLabel)
                            .addComponent(editLabel)
                            .addComponent(codelistRecodeLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(midSectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(variablesScrollPane)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(editScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(codelistRecodeLabel)
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(codelistRecodeButton)
                                    .addComponent(codelistRecodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(codelistRecodeLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(globalRecodeRecodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(warningLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(warningScrollPane)))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        handleSelectionChanged();
        controller.close();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void codelistRecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codelistRecodeButtonActionPerformed
        controller.codelistRecode();
    }//GEN-LAST:event_codelistRecodeButtonActionPerformed

    private void truncateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_truncateButtonActionPerformed
        try {
            int positions = getTruncatePositions(getSelectedRecode().getVariable().getVariableLength());
            if (positions > 0) {
                controller.truncate(getSelectedRecode(), positions);
                int rowIndex = this.model.getVariables().indexOf(getSelectedRecode().getVariable());
                variablesTable.getModel().setValueAt("T", rowIndex, 0);
                updateValues();
            }
 
        }
        catch (ArgusException ex) {
            ;
        }
    }//GEN-LAST:event_truncateButtonActionPerformed

    
    private void readButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readButtonActionPerformed
        String path = askForGrcPath();
        if (path != null) {
            try {
                controller.read(path, this.getSelectedRecode());
                this.selectedRecodeClone = new RecodeMu(this.selectedRecode);
                updateValues();
            }
            catch (ArgusException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }//GEN-LAST:event_readButtonActionPerformed

    public void showWarning(String warning) {
        warningTextArea.setText(warning);
    }
    
    public void setSelectedIndex(int index) {
        this.variablesTable.getSelectionModel().setSelectionInterval(index, index);
    }
    
    public int getSelectedIndex() {
        return this.variablesTable.getSelectionModel().getMaxSelectionIndex();
    }
    
    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        try {
            controller.apply(getSelectedRecode());
            int rowIndex = this.model.getVariables().indexOf(getSelectedRecode().getVariable());
            variablesTable.getModel().setValueAt("R", rowIndex, 0);
            updateValues();
        }
        catch (ArgusException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }//GEN-LAST:event_applyButtonActionPerformed

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoButtonActionPerformed
        try {
            controller.undo(getSelectedRecode());
            int rowIndex = this.model.getVariables().indexOf(getSelectedRecode().getVariable());
            variablesTable.getModel().setValueAt("", rowIndex, 0);
        }
        catch (ArgusException ex) {
            ;
        }
    }//GEN-LAST:event_undoButtonActionPerformed

    private void saveGrcFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                this.selectedRecode.write(fileChooser.getSelectedFile());
            }
            catch (ArgusException ex) {
                JOptionPane.showMessageDialog(null, "Error saving grc file: " + ex.getMessage());
            }
        }
    }
        
    
    private void handleSelectionChanged() {
        if (this.selectedRecodeClone != null) {
            if (!selectedRecodeClone.equals(getSelectedRecode())) {
                int result = JOptionPane.showConfirmDialog(null, "Recode information has been changed.\nSave recode file?");
                if (result == JOptionPane.YES_OPTION) {
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
     *
     * @param filename
     */
    public void setCodelistText(String filename) {
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
    private javax.swing.JTextField globalRecodeRecodeTextField;
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
