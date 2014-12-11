package muargus.view;

import muargus.resources.ContextHelp;
import argus.model.ArgusException;
import java.awt.BorderLayout;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.GroupLayout;
import muargus.MuARGUS;
import muargus.RiskChartBuilder;
import muargus.controller.RiskSpecificationController;
import muargus.model.RiskSpecification;
import muargus.model.TableMu;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;

/**
 * View class of the RiskSpecification screen. This class is used for both the
 * individual risk specification and the household risk specification.
 *
 * @author Statistics Netherlands
 */
public class RiskSpecificationView extends DialogBase<RiskSpecificationController> implements ChartProgressListener {

    private RiskSpecification model;
    private TableMu riskTable;
    private ChartPanel cp = null;
    private boolean calculating = false;
    private final boolean isHousehold;

    private final int SLIDERKNOBSIZE = 16;

    /**
     * Creates new form RiskSpecificationView.
     *
     * @param parent the Frame of the mainFrame.
     * @param modal Boolean to set the modal status.
     * @param controller the controller of this view.
     * @param isHousehold Boolean indicating whether the data includes household
     * data.
     */
    public RiskSpecificationView(java.awt.Frame parent, boolean modal, RiskSpecificationController controller,
            boolean isHousehold) {
        super(parent, modal, controller);
        initComponents();
        setHouseholdComponents(isHousehold);
        setLocationRelativeTo(null);
        this.isHousehold = isHousehold;
    }

    /**
     * Gets the named destination linked to the visible view.
     *
     * @return String containing the named destination.
     */
    @Override
    protected String getHelpNamedDestination() {
        return ContextHelp.fromClassName(getClass().getName(), isHousehold);
    }

    /**
     * Shows the Histogram.
     */
    private void showChart() {
        if (this.jPanelChart.getComponentCount() > 0) {
            this.jPanelChart.remove(this.cp);
            this.jPanelChart.revalidate();
        }
        RiskChartBuilder builder = new RiskChartBuilder();
        this.jPanelChart.setLayout(new BorderLayout());
        this.cp = builder.CreateChart(this.model, getDecimals(), getMetadata().isHouseholdData());
        this.cp.getChart().addProgressListener(this);
        this.jPanelChart.add(this.cp, BorderLayout.CENTER);
        this.jPanelChart.repaint();
    }

    /**
     * Sets the components (in)visible depending whether the data is household
     * data.
     *
     * @param isHousehold Boolean indicating whether the data is household data.
     */
    private void setHouseholdComponents(boolean isHousehold) {
        this.reidentCalcButton.setVisible(!isHousehold);
        this.reidentThresholdTextField.setVisible(!isHousehold);
        this.maxReidentRateTextField.setVisible(!isHousehold);
        this.maxReidentLabel.setVisible(!isHousehold);
        this.reidentPercLabel.setVisible(!isHousehold);
        this.reidentThresholdLabel.setVisible(!isHousehold);
        this.riskLabel.setText(isHousehold ? "househ. risk" : "ind. risk");
        this.riskThresholdLabel.setText(isHousehold ? "<html>HH risk<br>threshold</html>" : "<html>ind. risk<br>threshold</html>");
        this.nUnsafeLabel.setText(isHousehold ? "# unsafe HH:" : "# unsafe records");
    }

    /**
     * Initializes the data. This method sets the model, the label of the table,
     * the values and the chart.
     */
    @Override
    public void initializeData() {
        this.model = getMetadata().getCombinations().getRiskSpecifications().get(this.riskTable);
        this.tableLabel.setText(this.riskTable.getTableTitle());
        updateValues();
        showChart();
    }

    /**
     * Sets the risk table.
     *
     * @param table TableMu instance for which the risk specification is made.
     */
    public void setRiskTable(TableMu table) {
        this.riskTable = table;
    }

    /**
     * Gets the number of decimals that will be shown.
     *
     * @return Integer containing the number of decimals.
     */
    private int getDecimals() {
        return Integer.parseInt(this.decimalsCombo.getSelectedItem().toString());
    }

    /**
     * Converts a double value to a string format. 
     *
     * @param d Double value that will be converted to a string.
     * @return String containing the double value as a string
     */
    private String formatDouble(double d) {
        String format = "%." + getDecimals() + "f";
        return String.format(MuARGUS.getLocale(), format, d);
    }

    /**
     * Converts a double percentage to a string format.
     *
     * @param d Double value that will be converted to a string.
     * @return String containing the double value as a string
     */
    private String formatDoublePrc(double d) {
        String format = "%." + Integer.toString(getDecimals() - 2) + "f";
        return String.format(MuARGUS.getLocale(), format, d * 100);
    }

    /**
     * Updates the values.
     */
    private void updateValues() {
        this.maxRiskTextField.setText(formatDouble(this.model.getMaxRisk()));
        this.maxReidentRateTextField.setText(formatDoublePrc(this.model.getMaxReidentRate()) + "%");
        this.riskThresholdTextField.setText(formatDouble(this.model.getRiskThreshold()));
        this.reidentThresholdTextField.setText(formatDoublePrc(this.model.getReidentRateThreshold()));
        this.unsafeRecordsTextField.setText(Integer.toString(this.model.getUnsafeRecords()));
        setSliderPosition();
        this.calculating = false;
    }

    /**
     * Sets the position of the slider.
     */
    private void setSliderPosition() {
        double value = this.riskSlider.getMaximum() * Math.log(this.model.getRiskThreshold() / this.model.getMinRisk())
                / Math.log(this.model.getMaxRisk() / this.model.getMinRisk());
        this.riskSlider.setValue((int) value);
    }

    /**
     * Receives notification of a chart progress event.
     *
     * @param cpe ChartProgressEvent
     */
    @Override
    public void chartProgress(ChartProgressEvent cpe) {
        if (cpe.getPercent() == 100) {
            Rectangle2D rect = this.cp.getChartRenderingInfo().getPlotInfo().getDataArea();
            GroupLayout groupLayout = (GroupLayout) this.sliderPanel.getLayout();
            int gapLeft = (int) rect.getMinX() - this.SLIDERKNOBSIZE / 2;
            int gapRight = this.sliderPanel.getWidth() - (int) rect.getMaxX() - this.SLIDERKNOBSIZE / 2;
            groupLayout.setHorizontalGroup(
                    groupLayout.createSequentialGroup()
                    .addContainerGap(gapLeft, gapLeft)
                    .addComponent(this.riskSlider)
                    .addContainerGap(gapRight, gapRight));
            this.sliderPanel.setLayout(groupLayout);
        }
    }

    /**
     * Change the histogram to cumulative/normal.
     *
     * @param cumulative Boolean indicating whether the cumulative histogram
     * will be shown.
     */
    private void showCumulative(boolean cumulative) {
        getController().fillModelHistogramData(cumulative);
        showChart();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton5 = new javax.swing.JButton();
        tableLabel = new javax.swing.JLabel();
        cumulativeCheckbox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        maxRiskTextField = new javax.swing.JTextField();
        maxReidentRateTextField = new javax.swing.JTextField();
        riskLabel = new javax.swing.JLabel();
        maxReidentLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        riskThresholdLabel = new javax.swing.JLabel();
        reidentThresholdLabel = new javax.swing.JLabel();
        reidentThresholdTextField = new javax.swing.JTextField();
        riskThresholdTextField = new javax.swing.JTextField();
        riskCalcButton = new javax.swing.JButton();
        reidentCalcButton = new javax.swing.JButton();
        reidentPercLabel = new javax.swing.JLabel();
        unsafeRecordsTextField = new javax.swing.JTextField();
        unsafeCalcButton = new javax.swing.JButton();
        nUnsafeLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        decimalsCombo = new javax.swing.JComboBox();
        jPanelChart = new javax.swing.JPanel();
        sliderPanel = new javax.swing.JPanel();
        riskSlider = new javax.swing.JSlider();
        okButton = new javax.swing.JButton();

        jButton5.setText("Cancel");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Risk Specification");
        setMinimumSize(new java.awt.Dimension(725, 500));

        tableLabel.setText("Dimensions");

        cumulativeCheckbox.setText("Cumulative chart");
        cumulativeCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cumulativeCheckboxActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Maximum levels in file"));

        maxRiskTextField.setEditable(false);

        maxReidentRateTextField.setEditable(false);

        riskLabel.setText("ind. risk");

        maxReidentLabel.setText("re ident rate");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxReidentLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(riskLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(maxRiskTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(maxReidentRateTextField))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxRiskTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(riskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxReidentRateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxReidentLabel)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Threshold setting"));

        riskThresholdLabel.setText("<html><P STYLE=\"text-align: right;\">\nind. risk<br>\nthreshold");

        reidentThresholdLabel.setText("<html><P STYLE=\"text-align: right;\">\nre ident rate <br>\nthreshold\n");

        riskCalcButton.setText("Calc");
        riskCalcButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                riskCalcButtonActionPerformed(evt);
            }
        });

        reidentCalcButton.setText("Calc");
        reidentCalcButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reidentCalcButtonActionPerformed(evt);
            }
        });

        reidentPercLabel.setText("%");

        unsafeCalcButton.setText("Calc");
        unsafeCalcButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unsafeCalcButtonActionPerformed(evt);
            }
        });

        nUnsafeLabel.setText("# unsafe records:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(riskThresholdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reidentThresholdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(reidentThresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reidentPercLabel))
                    .addComponent(riskThresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(riskCalcButton)
                    .addComponent(reidentCalcButton))
                .addGap(58, 58, 58)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(unsafeRecordsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unsafeCalcButton))
                    .addComponent(nUnsafeLabel)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(riskThresholdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(riskThresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(riskCalcButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(reidentThresholdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(reidentThresholdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(reidentCalcButton)
                                .addComponent(reidentPercLabel))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(nUnsafeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(unsafeRecordsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unsafeCalcButton))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel8.setText("<html>\n# decimals <br>\nshown");

        decimalsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "4", "5", "6", "7" }));
        decimalsCombo.setSelectedIndex(2);
        decimalsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decimalsComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(decimalsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(decimalsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelChartLayout = new javax.swing.GroupLayout(jPanelChart);
        jPanelChart.setLayout(jPanelChartLayout);
        jPanelChartLayout.setHorizontalGroup(
            jPanelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelChartLayout.setVerticalGroup(
            jPanelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );

        riskSlider.setMajorTickSpacing(1000);
        riskSlider.setMaximum(1000);
        riskSlider.setMinorTickSpacing(50);
        riskSlider.setPaintTicks(true);
        riskSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                riskSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout sliderPanelLayout = new javax.swing.GroupLayout(sliderPanel);
        sliderPanel.setLayout(sliderPanelLayout);
        sliderPanelLayout.setHorizontalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(riskSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        sliderPanelLayout.setVerticalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sliderPanelLayout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(riskSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        okButton.setText("Done");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(tableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cumulativeCheckbox)
                .addGap(74, 74, 74))
            .addComponent(jPanelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(sliderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableLabel)
                    .addComponent(cumulativeCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(sliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void riskSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_riskSliderStateChanged
        if (!this.riskSlider.getValueIsAdjusting() && !this.calculating) {

            double threshold = this.model.getMinRisk() * Math.exp(
                    (Math.log(this.model.getMaxRisk() / this.model.getMinRisk())) * this.riskSlider.getValue() / this.riskSlider.getMaximum());

            this.model.setRiskThreshold(threshold);
            getController().calculateByRiskThreshold();
            updateValues();
        }
    }//GEN-LAST:event_riskSliderStateChanged

    private void cumulativeCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cumulativeCheckboxActionPerformed
        showCumulative(this.cumulativeCheckbox.isSelected());
    }//GEN-LAST:event_cumulativeCheckboxActionPerformed

    private void decimalsComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decimalsComboActionPerformed
        showChart();
        updateValues();
    }//GEN-LAST:event_decimalsComboActionPerformed

    private void unsafeCalcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unsafeCalcButtonActionPerformed
        this.calculating = true;
        this.model.setUnsafeRecords(Integer.parseInt(this.unsafeRecordsTextField.getText()));
        getController().calculateByUnsafeRecords();
        updateValues();
    }//GEN-LAST:event_unsafeCalcButtonActionPerformed

    private void reidentCalcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reidentCalcButtonActionPerformed
        this.calculating = true;
        try {
            double d = NumberFormat.getInstance(MuARGUS.getLocale()).parse(this.reidentThresholdTextField.getText()).doubleValue();
            getController().calculateByReidentThreshold(d / 100, getDecimals());
        } catch (ParseException ex) {
            showErrorMessage(new ArgusException("Entered value is not valid"));
        } catch (ArgusException ex) {
            showErrorMessage(ex);
        }
        updateValues();
    }//GEN-LAST:event_reidentCalcButtonActionPerformed

    private void riskCalcButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_riskCalcButtonActionPerformed
        try {
            this.calculating = true;
            double d = NumberFormat.getInstance(MuARGUS.getLocale()).parse(this.riskThresholdTextField.getText()).doubleValue();
            this.model.setRiskThreshold(d);
            getController().calculateByRiskThreshold();
            updateValues();
        } catch (ParseException ex) {
            showErrorMessage(new ArgusException("Entered value is not valid"));
        }
    }//GEN-LAST:event_riskCalcButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cumulativeCheckbox;
    private javax.swing.JComboBox decimalsCombo;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelChart;
    private javax.swing.JLabel maxReidentLabel;
    private javax.swing.JTextField maxReidentRateTextField;
    private javax.swing.JTextField maxRiskTextField;
    private javax.swing.JLabel nUnsafeLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton reidentCalcButton;
    private javax.swing.JLabel reidentPercLabel;
    private javax.swing.JLabel reidentThresholdLabel;
    private javax.swing.JTextField reidentThresholdTextField;
    private javax.swing.JButton riskCalcButton;
    private javax.swing.JLabel riskLabel;
    private javax.swing.JSlider riskSlider;
    private javax.swing.JLabel riskThresholdLabel;
    private javax.swing.JTextField riskThresholdTextField;
    private javax.swing.JPanel sliderPanel;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JButton unsafeCalcButton;
    private javax.swing.JTextField unsafeRecordsTextField;
    // End of variables declaration//GEN-END:variables

}
