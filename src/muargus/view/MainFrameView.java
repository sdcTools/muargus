package muargus.view;

import muargus.resources.ContextHelp;
import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.view.DialogOpenMicrodata;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import muargus.CodeTableCellRenderer;
import muargus.MuARGUS;
import muargus.controller.MainFrameController;
import muargus.model.Combinations;
import muargus.model.CodeInfo;
import muargus.model.VariableMu;

/**
 * View class of the main frame.
 *
 * @author Statistics Netherlands
 */
public class MainFrameView extends javax.swing.JFrame {

    private final MainFrameController controller;
    private Combinations model;

    /**
     * Creates new form MainFrameView
     */
    public MainFrameView() {
        initComponents();
        this.controller = new MainFrameController(this);
        this.setLocationRelativeTo(null);
        setHelpAction();
    }

    /**
     * Enables the buttons & menu items for the specified screen.
     *
     * @param action Enumerated option/screen.
     * @param enable Boolean indicating whether this option should be enabled.
     */
    public void enableAction(MainFrameController.Action action, boolean enable) {
        switch (action) {
            case SpecifyMetadata:
                doEnable(this.specifyMetaDataButton, this.metaDataMenuItem, enable);
                return;
            case SpecifyCombinations:
                doEnable(this.specifyCombinationsButton, this.combinationsMenuItem, enable);
                return;
            case GlobalRecode:
                doEnable(this.globalRecodeButton, this.globalRecodeMenuItem, enable);
                return;
            case ShowTableCollection:
                doEnable(this.showTableCollectionButton, this.showTableCollectionMenuItem, enable);
                return;
            case PramSpecification:
                doEnable(this.pramSpecificationButton, this.pramSpecificationMenuItem, enable);
                return;
            case IndividualRiskSpecification:
                doEnable(this.individualRiskSpecificationButton, this.individualRiskSpecificationMenuItem, enable);
                return;
            case HouseholdRiskSpecification:
                doEnable(this.householdRiskSpecificationButton, this.householdRiskSpecificationMenuItem, enable);
                return;
            case ModifyNumericalVariables:
                doEnable(this.modifyNumericalVariablesButton, this.numericalVariablesMenuItem, enable); //TODO: verander naamgeving voor deze klasses
                return;
            case NumericalMicroAggregation:
                doEnable(this.numericalMicroaggregationButton, this.numericalMicroaggregationMenuItem, enable);
                return;
            case NumericalRankSwapping:
                doEnable(this.numericalRankSwappingButton, this.numericalRankSwappingMenuItem, enable);
                return;
            case MakeProtectedFile:
                doEnable(this.makeProtectedFileButton, this.makeProtectedFileMenuItem, enable);
                return;
            case ViewReport:
                doEnable(this.viewReportButton, this.viewReportMenuItem, enable);
            case RScript:
                doEnable(this.rScriptButton, this.rScriptMenuItem, enable);
        }
    }

    /**
     * Enables/disables buttons and menu items.
     *
     * @param button JButton instance that will be enabled/disabled.
     * @param item JMenuItem instance that will be enabled/disabled.
     * @param enable Boolean indicating whether the button and the menu item
     * will be enabled.
     */
    private void doEnable(JButton button, JMenuItem item, boolean enable) {
        if (button != null) {
            button.setEnabled(enable);
        }
        if (item != null) {
            item.setEnabled(enable);
        }
    }

    /**
     * Gets the unsafe combinations table containing the number of unsafe
     * combinations for each categorical variable.
     *
     * @return JTable instance containing the number of unsafe combinations for
     * each categorical variable.
     */
    public JTable getUnsafeCombinationsTable() {
        return unsafeCombinationsTable;
    }

    /**
     * Gets the variables table containing the code specifications for the
     * selected variable.
     *
     * @return JTable instance containing the code specifications for the
     * selected variable.
     */
    public JTable getVariablesTable() {
        return variablesTable;
    }

    /**
     * Sets the variable name label.
     *
     * @param variableNameLabel String containing the name of selected the
     * variable.
     */
    public void setVariableNameLabel(String variableNameLabel) {
        this.variableNameLabel.setText(variableNameLabel);
    }

    /**
     * Shows an error message.
     *
     * @param ex ArgusException.
     */
    public void showErrorMessage(ArgusException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), MuARGUS.getMessageTitle(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a message.
     *
     * @param message String containing the message.
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, MuARGUS.getMessageTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show the open microdata dialob.
     *
     * @param filenames DataFilePair instance containing the filenames that are
     * currently loaded. This is empty if no filenames are specified.
     * @return DataFilePair instance containing the filnemas after
     * (re)specification of the filenames.
     */
    public DataFilePair showOpenMicrodataDialog(DataFilePair filenames) {
        DialogOpenMicrodata dialog = new DialogOpenMicrodata(this, true);
        dialog.setDataFileNames(filenames.getDataFileName(), filenames.getMetaFileName());
        if (dialog.showDialog() == DialogOpenMicrodata.APPROVE_OPTION) {
            return dialog.getMicrodataFilePair();
        }
        return null;
    }

    /**
     * Shows the unsafe combinations for each categorical variable.
     *
     * @param model Combinations model class.
     * @param selectedIndex Integer containing the index of the selected
     * variable.
     * @param redraw Boolean indicating whether the number of unsafe
     * combinations needs the be changed.
     */
    public void showUnsafeCombinations(Combinations model, int selectedIndex, boolean redraw) {
        this.model = model;
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("Variable");
        int nDims = model.getMaxDimsInTables();
        for (int dimNr = 1; dimNr <= nDims; dimNr++) {
            columnNames.add("dim " + dimNr);
        }

        Object[][] data = new Object[model.getVariablesInTables().size()][];
        int rowIndex = 0;
        for (VariableMu variable : model.getVariablesInTables()) {
            data[rowIndex] = toObjectArray(model, variable); //TODO mooier
            rowIndex++;
        }
        if (!redraw) {
            for (int varIndex = 0; varIndex < data.length; varIndex++) {
                for (int dimIndex = 1; dimIndex <= columnNames.size() - 1; dimIndex++) {
                    this.unsafeCombinationsTable.getModel().setValueAt(data[varIndex][dimIndex],
                            varIndex, dimIndex);
                }
            }
        } else {
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames.toArray()) {
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }

                @Override
                public Class getColumnClass(int columnIndex) {
                    return columnIndex == 0 ? String.class : Integer.class;
                }
            };
            this.unsafeCombinationsTable.setModel(tableModel);

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                    Object o2 = (new Integer(-1).equals(o) ? "-" : o);
                    return super.getTableCellRendererComponent(jtable, o2, bln, bln1, i, i1); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public int getHorizontalAlignment() {
                    return JLabel.RIGHT;
                }
            };

            //renderer.setHorizontalAlignment(JLabel.RIGHT);
            for (int index = 1; index < columnNames.size(); index++) {

                this.unsafeCombinationsTable.getColumn(String.format("dim %d", index)).setCellRenderer(renderer);
            }

            this.unsafeCombinationsTable.getSelectionModel().addListSelectionListener(
                    new javax.swing.event.ListSelectionListener() {
                        @Override
                        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                            selectionChanged(evt);
                        }
                    });

        }
        int i = this.unsafeCombinationsTable.convertRowIndexToView(selectedIndex);
        this.unsafeCombinationsTable.getSelectionModel().setSelectionInterval(i, i);
    }

    /**
     * Gets an array containing the variable name and the number of unsafe
     * combinations for each dimension.
     *
     * @param combinations Combinations model class.
     * @param variable VariableMu instance for which data will be shown.
     * @return Array of objects containing the variable name and the number of
     * unsafe combinations for each dimension.
     */
    private Object[] toObjectArray(Combinations combinations, VariableMu variable) {
        int nDims = combinations.getMaxDimsInTables();
        Object[] objArr = new Object[nDims + 1];
        objArr[0] = variable.getName();
        for (int dimNr = 1; dimNr <= nDims; dimNr++) {
            objArr[dimNr] = combinations.getUnsafeCombinations().get(variable).length < dimNr
                    ? -1 : combinations.getUnsafeCombinations().get(variable)[dimNr - 1];
        }
        return objArr;
    }

    /**
     * Selection changed event handler. Updates the variables table containing
     * the code specification if a different variable is selected.
     *
     * @param evt
     */
    private void selectionChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }
        int j = ((ListSelectionModel) evt.getSource()).getMinSelectionIndex();
        if (0 > j || j >= this.model.getVariablesInTables().size()) {
            return;
        }
        updateVariablesTable(j);
    }

    /**
     * Updates the variables table.
     */
    public void updateVariablesTable() {
        updateVariablesTable(this.unsafeCombinationsTable.getSelectedRow());
    }

    /**
     * Updates the variables table.
     *
     * @param j Integer containing the selected variable for which the code
     * specification needs to be shown.
     */
    private void updateVariablesTable(int j) {
        j = this.unsafeCombinationsTable.convertRowIndexToModel(j);
        VariableMu variable = this.model.getVariablesInTables().get(j);
        //UnsafeInfo unsafeInfo = this.model.getUnsafe(variable);
        this.variableNameLabel.setText(variable.getName());

        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("Code");
        columnNames.add("Label");
        columnNames.add("Freq");
        int nDims = this.model.getMaxDimsInTables();
        for (int dimNr = 1; dimNr <= nDims; dimNr++) {
            columnNames.add("dim " + dimNr);
        }

        Object[][] data = new Object[variable.getCodeInfos().size()][];
        int rowIndex = 0;
        for (CodeInfo codeInfo : variable.getCodeInfos()) {
            data[rowIndex] = codeInfo.toObjectArray(nDims);
            rowIndex++;
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames.toArray()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return CodeInfo.class;
                    case 1:
                        return String.class;
                    default:
                        return Integer.class;
                }
            }
        };
        this.variablesTable.setModel(tableModel);
        this.variablesTable.setDefaultRenderer(Integer.class, new CodeTableCellRenderer());
        this.variablesTable.setDefaultRenderer(Object.class, new CodeTableCellRenderer());
    }

    /**
     * Sets the help action event. If the F1 key is pushed this event is fired.
     */
    private void setHelpAction() {

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        };
        this.rootPane.getActionMap().put("f1action", action);
        this.rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke("F1"), "f1action");
    }

    /**
     * Gets the named destination belonging to this screen.
     *
     * @return String containing the named destination belonging to this screen.
     */
    private String getHelpNamedDestination() {
        return ContextHelp.fromClassName(this.getClass().getName());
    }

    /**
     * Shows the content sensitive help.
     */
    private void showHelp() {
        MuARGUS.showHelp(getHelpNamedDestination());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBar = new javax.swing.JToolBar();
        openMicrodataButton = new javax.swing.JButton();
        toolBarSeparator1 = new javax.swing.JToolBar.Separator();
        specifyMetaDataButton = new javax.swing.JButton();
        specifyCombinationsButton = new javax.swing.JButton();
        toolBarSeparator2 = new javax.swing.JToolBar.Separator();
        showTableCollectionButton = new javax.swing.JButton();
        globalRecodeButton = new javax.swing.JButton();
        pramSpecificationButton = new javax.swing.JButton();
        individualRiskSpecificationButton = new javax.swing.JButton();
        householdRiskSpecificationButton = new javax.swing.JButton();
        modifyNumericalVariablesButton = new javax.swing.JButton();
        numericalMicroaggregationButton = new javax.swing.JButton();
        numericalRankSwappingButton = new javax.swing.JButton();
        toolBarSeparator3 = new javax.swing.JToolBar.Separator();
        makeProtectedFileButton = new javax.swing.JButton();
        viewReportButton = new javax.swing.JButton();
        toolBarSeparator4 = new javax.swing.JToolBar.Separator();
        contentsButton = new javax.swing.JButton();
        newsButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();
        toolBarSeparator5 = new javax.swing.JToolBar.Separator();
        rScriptButton = new javax.swing.JButton();
        unsafeCombinationsPanel = new javax.swing.JPanel();
        unsafeCombinationsLabel = new javax.swing.JLabel();
        unsafeCombinationsScrollPane = new javax.swing.JScrollPane();
        unsafeCombinationsTable = new javax.swing.JTable();
        variablesPanel = new javax.swing.JPanel();
        variableLabel = new javax.swing.JLabel();
        variablesScrollPane = new javax.swing.JScrollPane();
        variablesTable = new javax.swing.JTable();
        variableNameLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMicrodataMenuItem = new javax.swing.JMenuItem();
        fileSeparator = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        specifyMenu = new javax.swing.JMenu();
        metaDataMenuItem = new javax.swing.JMenuItem();
        combinationsMenuItem = new javax.swing.JMenuItem();
        modifyMenu = new javax.swing.JMenu();
        showTableCollectionMenuItem = new javax.swing.JMenuItem();
        globalRecodeMenuItem = new javax.swing.JMenuItem();
        modifySeparator1 = new javax.swing.JPopupMenu.Separator();
        pramSpecificationMenuItem = new javax.swing.JMenuItem();
        individualRiskSpecificationMenuItem = new javax.swing.JMenuItem();
        householdRiskSpecificationMenuItem = new javax.swing.JMenuItem();
        modifySeparator2 = new javax.swing.JPopupMenu.Separator();
        numericalVariablesMenuItem = new javax.swing.JMenuItem();
        numericalMicroaggregationMenuItem = new javax.swing.JMenuItem();
        numericalRankSwappingMenuItem = new javax.swing.JMenuItem();
        outputMenu = new javax.swing.JMenu();
        makeProtectedFileMenuItem = new javax.swing.JMenuItem();
        viewReportMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        newsMenuItem = new javax.swing.JMenuItem();
        helpSeparator = new javax.swing.JPopupMenu.Separator();
        aboutMenuItem = new javax.swing.JMenuItem();
        externMenu = new javax.swing.JMenu();
        rScriptMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Main Frame");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/mu.png")).getImage());
        setMinimumSize(new java.awt.Dimension(650, 400));

        toolBar.setRollover(true);
        toolBar.setEnabled(false);

        openMicrodataButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/OpenMicrodata.png"))); // NOI18N
        openMicrodataButton.setToolTipText("Open micro data");
        openMicrodataButton.setFocusable(false);
        openMicrodataButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openMicrodataButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openMicrodataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMicrodataMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(openMicrodataButton);
        toolBar.add(toolBarSeparator1);

        specifyMetaDataButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/SpecifyMetadata.png"))); // NOI18N
        specifyMetaDataButton.setToolTipText("Specify metadata");
        specifyMetaDataButton.setEnabled(false);
        specifyMetaDataButton.setFocusable(false);
        specifyMetaDataButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        specifyMetaDataButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        specifyMetaDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metaDataMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(specifyMetaDataButton);

        specifyCombinationsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/SpecifyCombinations.png"))); // NOI18N
        specifyCombinationsButton.setToolTipText("Specify combinations");
        specifyCombinationsButton.setEnabled(false);
        specifyCombinationsButton.setFocusable(false);
        specifyCombinationsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        specifyCombinationsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        specifyCombinationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combinationsMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(specifyCombinationsButton);
        toolBar.add(toolBarSeparator2);

        showTableCollectionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/ShowTable.png"))); // NOI18N
        showTableCollectionButton.setToolTipText("Show table collection");
        showTableCollectionButton.setEnabled(false);
        showTableCollectionButton.setFocusable(false);
        showTableCollectionButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showTableCollectionButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showTableCollectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTableCollectionMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(showTableCollectionButton);

        globalRecodeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/globrec.png"))); // NOI18N
        globalRecodeButton.setToolTipText("Global recode");
        globalRecodeButton.setEnabled(false);
        globalRecodeButton.setFocusable(false);
        globalRecodeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        globalRecodeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        globalRecodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalRecodeMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(globalRecodeButton);

        pramSpecificationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/pram.png"))); // NOI18N
        pramSpecificationButton.setToolTipText("PRAM specification");
        pramSpecificationButton.setEnabled(false);
        pramSpecificationButton.setFocusable(false);
        pramSpecificationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pramSpecificationButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        pramSpecificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pramSpecificationMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(pramSpecificationButton);

        individualRiskSpecificationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Risk.png"))); // NOI18N
        individualRiskSpecificationButton.setToolTipText("Individual risk specification");
        individualRiskSpecificationButton.setEnabled(false);
        individualRiskSpecificationButton.setFocusable(false);
        individualRiskSpecificationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        individualRiskSpecificationButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        individualRiskSpecificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                individualRiskSpecificationMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(individualRiskSpecificationButton);

        householdRiskSpecificationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/hr.png"))); // NOI18N
        householdRiskSpecificationButton.setToolTipText("Household risk specification");
        householdRiskSpecificationButton.setEnabled(false);
        householdRiskSpecificationButton.setFocusable(false);
        householdRiskSpecificationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        householdRiskSpecificationButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        householdRiskSpecificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                householdRiskSpecificationMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(householdRiskSpecificationButton);

        modifyNumericalVariablesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Numeric.png"))); // NOI18N
        modifyNumericalVariablesButton.setToolTipText("Modify numerical variables");
        modifyNumericalVariablesButton.setEnabled(false);
        modifyNumericalVariablesButton.setFocusable(false);
        modifyNumericalVariablesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        modifyNumericalVariablesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        modifyNumericalVariablesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numericalVariablesMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(modifyNumericalVariablesButton);

        numericalMicroaggregationButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/MA.png"))); // NOI18N
        numericalMicroaggregationButton.setToolTipText("Numerical microaggregation");
        numericalMicroaggregationButton.setEnabled(false);
        numericalMicroaggregationButton.setFocusable(false);
        numericalMicroaggregationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        numericalMicroaggregationButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        numericalMicroaggregationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numericalMicroaggregationMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(numericalMicroaggregationButton);

        numericalRankSwappingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/RS.png"))); // NOI18N
        numericalRankSwappingButton.setToolTipText("Numerical rank swapping");
        numericalRankSwappingButton.setEnabled(false);
        numericalRankSwappingButton.setFocusable(false);
        numericalRankSwappingButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        numericalRankSwappingButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        numericalRankSwappingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numericalRankSwappingMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(numericalRankSwappingButton);
        toolBar.add(toolBarSeparator3);

        makeProtectedFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/makeSafeFile.png"))); // NOI18N
        makeProtectedFileButton.setToolTipText("Make protected file");
        makeProtectedFileButton.setEnabled(false);
        makeProtectedFileButton.setFocusable(false);
        makeProtectedFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        makeProtectedFileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        makeProtectedFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeProtectedFileMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(makeProtectedFileButton);

        viewReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/ViewReport.png"))); // NOI18N
        viewReportButton.setToolTipText("View report");
        viewReportButton.setEnabled(false);
        viewReportButton.setFocusable(false);
        viewReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        viewReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReportMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(viewReportButton);
        toolBar.add(toolBarSeparator4);

        contentsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Contents.png"))); // NOI18N
        contentsButton.setToolTipText("Content help");
        contentsButton.setFocusable(false);
        contentsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        contentsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        contentsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentsMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(contentsButton);

        newsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/news.png"))); // NOI18N
        newsButton.setToolTipText("News");
        newsButton.setFocusable(false);
        newsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newsMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(newsButton);

        aboutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/about.png"))); // NOI18N
        aboutButton.setToolTipText("About");
        aboutButton.setFocusable(false);
        aboutButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        aboutButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(aboutButton);
        toolBar.add(toolBarSeparator5);

        rScriptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Rlogo.png"))); // NOI18N
        rScriptButton.setToolTipText("R script");
        rScriptButton.setEnabled(false);
        rScriptButton.setFocusable(false);
        rScriptButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        rScriptButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rScriptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rScriptMenuItemActionPerformed(evt);
            }
        });
        toolBar.add(rScriptButton);

        unsafeCombinationsPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        unsafeCombinationsLabel.setText("# unsafe combinations in each dimension");

        unsafeCombinationsTable.setAutoCreateRowSorter(true);
        unsafeCombinationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Variable", "dim 1", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        unsafeCombinationsTable.setShowHorizontalLines(false);
        unsafeCombinationsTable.setShowVerticalLines(false);
        unsafeCombinationsTable.getTableHeader().setReorderingAllowed(false);
        unsafeCombinationsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                unsafeCombinationsTableMouseClicked(evt);
            }
        });
        unsafeCombinationsScrollPane.setViewportView(unsafeCombinationsTable);

        javax.swing.GroupLayout unsafeCombinationsPanelLayout = new javax.swing.GroupLayout(unsafeCombinationsPanel);
        unsafeCombinationsPanel.setLayout(unsafeCombinationsPanelLayout);
        unsafeCombinationsPanelLayout.setHorizontalGroup(
            unsafeCombinationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(unsafeCombinationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unsafeCombinationsLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, unsafeCombinationsPanelLayout.createSequentialGroup()
                .addComponent(unsafeCombinationsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        unsafeCombinationsPanelLayout.setVerticalGroup(
            unsafeCombinationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(unsafeCombinationsPanelLayout.createSequentialGroup()
                .addComponent(unsafeCombinationsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unsafeCombinationsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
        );

        variablesPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        variableLabel.setText("Variable:");

        variablesTable.setAutoCreateRowSorter(true);
        variablesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Label", "Freq", "dim1", ""
            }
        ));
        variablesTable.setShowHorizontalLines(false);
        variablesTable.setShowVerticalLines(false);
        variablesTable.getTableHeader().setReorderingAllowed(false);
        variablesScrollPane.setViewportView(variablesTable);

        javax.swing.GroupLayout variablesPanelLayout = new javax.swing.GroupLayout(variablesPanel);
        variablesPanel.setLayout(variablesPanelLayout);
        variablesPanelLayout.setHorizontalGroup(
            variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(variablesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(variableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variableNameLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        variablesPanelLayout.setVerticalGroup(
            variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(variablesPanelLayout.createSequentialGroup()
                .addGroup(variablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variableLabel)
                    .addComponent(variableNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        fileMenu.setText("File");

        openMicrodataMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        openMicrodataMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/OpenMicrodata.png"))); // NOI18N
        openMicrodataMenuItem.setText("Open micro data");
        openMicrodataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMicrodataMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMicrodataMenuItem);
        fileMenu.add(fileSeparator);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        specifyMenu.setText("Specify");

        metaDataMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK));
        metaDataMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/SpecifyMetadata.png"))); // NOI18N
        metaDataMenuItem.setText("MetaData");
        metaDataMenuItem.setEnabled(false);
        metaDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metaDataMenuItemActionPerformed(evt);
            }
        });
        specifyMenu.add(metaDataMenuItem);

        combinationsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        combinationsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/SpecifyCombinations.png"))); // NOI18N
        combinationsMenuItem.setText("Combinations");
        combinationsMenuItem.setEnabled(false);
        combinationsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combinationsMenuItemActionPerformed(evt);
            }
        });
        specifyMenu.add(combinationsMenuItem);

        menuBar.add(specifyMenu);

        modifyMenu.setText("Modify");

        showTableCollectionMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        showTableCollectionMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/ShowTable.png"))); // NOI18N
        showTableCollectionMenuItem.setText("Show Table Collection");
        showTableCollectionMenuItem.setEnabled(false);
        showTableCollectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTableCollectionMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(showTableCollectionMenuItem);

        globalRecodeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK));
        globalRecodeMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/globrec.png"))); // NOI18N
        globalRecodeMenuItem.setText("Global Recode");
        globalRecodeMenuItem.setEnabled(false);
        globalRecodeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalRecodeMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(globalRecodeMenuItem);
        modifyMenu.add(modifySeparator1);

        pramSpecificationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        pramSpecificationMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/pram.png"))); // NOI18N
        pramSpecificationMenuItem.setText("PRAM Specification");
        pramSpecificationMenuItem.setEnabled(false);
        pramSpecificationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pramSpecificationMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(pramSpecificationMenuItem);

        individualRiskSpecificationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_MASK));
        individualRiskSpecificationMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Risk.png"))); // NOI18N
        individualRiskSpecificationMenuItem.setText("Individual Risk Specification");
        individualRiskSpecificationMenuItem.setEnabled(false);
        individualRiskSpecificationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                individualRiskSpecificationMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(individualRiskSpecificationMenuItem);

        householdRiskSpecificationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK));
        householdRiskSpecificationMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/hr.png"))); // NOI18N
        householdRiskSpecificationMenuItem.setText("Household Risk Specification");
        householdRiskSpecificationMenuItem.setEnabled(false);
        householdRiskSpecificationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                householdRiskSpecificationMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(householdRiskSpecificationMenuItem);
        modifyMenu.add(modifySeparator2);

        numericalVariablesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_MASK));
        numericalVariablesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Numeric.png"))); // NOI18N
        numericalVariablesMenuItem.setText("Modify Numerical Variables");
        numericalVariablesMenuItem.setEnabled(false);
        numericalVariablesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numericalVariablesMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(numericalVariablesMenuItem);

        numericalMicroaggregationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        numericalMicroaggregationMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/MA.png"))); // NOI18N
        numericalMicroaggregationMenuItem.setText("Numerical Micro Aggregation");
        numericalMicroaggregationMenuItem.setEnabled(false);
        numericalMicroaggregationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numericalMicroaggregationMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(numericalMicroaggregationMenuItem);

        numericalRankSwappingMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        numericalRankSwappingMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/RS.png"))); // NOI18N
        numericalRankSwappingMenuItem.setText("Numerical Rank Swapping");
        numericalRankSwappingMenuItem.setEnabled(false);
        numericalRankSwappingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numericalRankSwappingMenuItemActionPerformed(evt);
            }
        });
        modifyMenu.add(numericalRankSwappingMenuItem);

        menuBar.add(modifyMenu);

        outputMenu.setText("Output");

        makeProtectedFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        makeProtectedFileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/makeSafeFile.png"))); // NOI18N
        makeProtectedFileMenuItem.setText("Make protected file");
        makeProtectedFileMenuItem.setEnabled(false);
        makeProtectedFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeProtectedFileMenuItemActionPerformed(evt);
            }
        });
        outputMenu.add(makeProtectedFileMenuItem);

        viewReportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK));
        viewReportMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/ViewReport.png"))); // NOI18N
        viewReportMenuItem.setText("View report");
        viewReportMenuItem.setEnabled(false);
        viewReportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReportMenuItemActionPerformed(evt);
            }
        });
        outputMenu.add(viewReportMenuItem);

        menuBar.add(outputMenu);

        helpMenu.setText("Help");

        contentsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        contentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Contents.png"))); // NOI18N
        contentsMenuItem.setText("Content help");
        contentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentsMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(contentsMenuItem);

        newsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/news.png"))); // NOI18N
        newsMenuItem.setText("News");
        newsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newsMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(newsMenuItem);
        helpMenu.add(helpSeparator);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/about.png"))); // NOI18N
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        externMenu.setText("Extern");

        rScriptMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/muargus/resources/icons/Rlogo.png"))); // NOI18N
        rScriptMenuItem.setText("load R script");
        rScriptMenuItem.setEnabled(false);
        rScriptMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rScriptMenuItemActionPerformed(evt);
            }
        });
        externMenu.add(rScriptMenuItem);

        menuBar.add(externMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(unsafeCombinationsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variablesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unsafeCombinationsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(variablesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openMicrodataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMicrodataMenuItemActionPerformed
        this.controller.openMicrodata();
    }//GEN-LAST:event_openMicrodataMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.controller.exit();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void metaDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metaDataMenuItemActionPerformed
        this.controller.specifyMetaData();
    }//GEN-LAST:event_metaDataMenuItemActionPerformed

    private void combinationsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combinationsMenuItemActionPerformed
        this.controller.specifyCombinations();
    }//GEN-LAST:event_combinationsMenuItemActionPerformed

    private void showTableCollectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTableCollectionMenuItemActionPerformed
        this.controller.showTableCollection();
    }//GEN-LAST:event_showTableCollectionMenuItemActionPerformed

    private void globalRecodeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalRecodeMenuItemActionPerformed
        int j = this.unsafeCombinationsTable.getSelectionModel().getMinSelectionIndex();
        if (j >= 0) {
            this.controller.globalRecode(this.unsafeCombinationsTable.convertRowIndexToModel(j));
        }
    }//GEN-LAST:event_globalRecodeMenuItemActionPerformed

    private void pramSpecificationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pramSpecificationMenuItemActionPerformed
        this.controller.pramSpecification();
    }//GEN-LAST:event_pramSpecificationMenuItemActionPerformed

    private void individualRiskSpecificationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_individualRiskSpecificationMenuItemActionPerformed
        this.controller.individualRiskSpecification();
    }//GEN-LAST:event_individualRiskSpecificationMenuItemActionPerformed

    private void householdRiskSpecificationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_householdRiskSpecificationMenuItemActionPerformed
        this.controller.householdRiskSpecification();
    }//GEN-LAST:event_householdRiskSpecificationMenuItemActionPerformed

    private void numericalVariablesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numericalVariablesMenuItemActionPerformed
        this.controller.numericalVariables();
    }//GEN-LAST:event_numericalVariablesMenuItemActionPerformed

    private void numericalMicroaggregationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numericalMicroaggregationMenuItemActionPerformed
        this.controller.numericalMicroaggregation();
    }//GEN-LAST:event_numericalMicroaggregationMenuItemActionPerformed

    private void numericalRankSwappingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numericalRankSwappingMenuItemActionPerformed
        this.controller.numericalRankSwapping();
    }//GEN-LAST:event_numericalRankSwappingMenuItemActionPerformed

    private void makeProtectedFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeProtectedFileMenuItemActionPerformed
        this.controller.makeProtectedFile();
    }//GEN-LAST:event_makeProtectedFileMenuItemActionPerformed

    private void viewReportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReportMenuItemActionPerformed
        this.controller.viewReport(false);
    }//GEN-LAST:event_viewReportMenuItemActionPerformed

    private void contentsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contentsMenuItemActionPerformed
        this.controller.contents();
    }//GEN-LAST:event_contentsMenuItemActionPerformed

    private void newsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newsMenuItemActionPerformed
        this.controller.news();
    }//GEN-LAST:event_newsMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        this.controller.about();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void unsafeCombinationsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unsafeCombinationsTableMouseClicked
        if (evt.getClickCount() == 2) {
            int j = this.unsafeCombinationsTable.getSelectionModel().getMinSelectionIndex();
            this.controller.globalRecode(this.unsafeCombinationsTable.convertRowIndexToModel(j));
        }
    }//GEN-LAST:event_unsafeCombinationsTableMouseClicked

    private void rScriptMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rScriptMenuItemActionPerformed
        this.controller.rScript();
    }//GEN-LAST:event_rScriptMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutButton;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem combinationsMenuItem;
    private javax.swing.JButton contentsButton;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu externMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileSeparator;
    private javax.swing.JButton globalRecodeButton;
    private javax.swing.JMenuItem globalRecodeMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPopupMenu.Separator helpSeparator;
    private javax.swing.JButton householdRiskSpecificationButton;
    private javax.swing.JMenuItem householdRiskSpecificationMenuItem;
    private javax.swing.JButton individualRiskSpecificationButton;
    private javax.swing.JMenuItem individualRiskSpecificationMenuItem;
    private javax.swing.JButton makeProtectedFileButton;
    private javax.swing.JMenuItem makeProtectedFileMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem metaDataMenuItem;
    private javax.swing.JMenu modifyMenu;
    private javax.swing.JButton modifyNumericalVariablesButton;
    private javax.swing.JPopupMenu.Separator modifySeparator1;
    private javax.swing.JPopupMenu.Separator modifySeparator2;
    private javax.swing.JButton newsButton;
    private javax.swing.JMenuItem newsMenuItem;
    private javax.swing.JButton numericalMicroaggregationButton;
    private javax.swing.JMenuItem numericalMicroaggregationMenuItem;
    private javax.swing.JButton numericalRankSwappingButton;
    private javax.swing.JMenuItem numericalRankSwappingMenuItem;
    private javax.swing.JMenuItem numericalVariablesMenuItem;
    private javax.swing.JButton openMicrodataButton;
    private javax.swing.JMenuItem openMicrodataMenuItem;
    private javax.swing.JMenu outputMenu;
    private javax.swing.JButton pramSpecificationButton;
    private javax.swing.JMenuItem pramSpecificationMenuItem;
    private javax.swing.JButton rScriptButton;
    private javax.swing.JMenuItem rScriptMenuItem;
    private javax.swing.JButton showTableCollectionButton;
    private javax.swing.JMenuItem showTableCollectionMenuItem;
    private javax.swing.JButton specifyCombinationsButton;
    private javax.swing.JMenu specifyMenu;
    private javax.swing.JButton specifyMetaDataButton;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JToolBar.Separator toolBarSeparator1;
    private javax.swing.JToolBar.Separator toolBarSeparator2;
    private javax.swing.JToolBar.Separator toolBarSeparator3;
    private javax.swing.JToolBar.Separator toolBarSeparator4;
    private javax.swing.JToolBar.Separator toolBarSeparator5;
    private javax.swing.JLabel unsafeCombinationsLabel;
    private javax.swing.JPanel unsafeCombinationsPanel;
    private javax.swing.JScrollPane unsafeCombinationsScrollPane;
    private javax.swing.JTable unsafeCombinationsTable;
    private javax.swing.JLabel variableLabel;
    private javax.swing.JLabel variableNameLabel;
    private javax.swing.JPanel variablesPanel;
    private javax.swing.JScrollPane variablesScrollPane;
    private javax.swing.JTable variablesTable;
    private javax.swing.JButton viewReportButton;
    private javax.swing.JMenuItem viewReportMenuItem;
    // End of variables declaration//GEN-END:variables
}
