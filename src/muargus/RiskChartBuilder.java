/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus;

import muargus.model.RiskModelClass;
import muargus.model.RiskSpecification;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XIntervalSeries;
import org.jfree.data.xy.XIntervalSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * Class for creating a risk chart.
 *
 * @author Statistics Netherlands
 */
public class RiskChartBuilder {

    /**
     * Gets the risk chart.
     *
     * @param riskSpec RiskSpecification instance containing the specifications
     * for the risk chart.
     * @param decimals Integer containing the number of decimals.
     * @param household Boolean indicating whether this variable is a household
     * variable.
     * @return ChartPanel containing the risk chart.
     */
    public ChartPanel CreateChart(RiskSpecification riskSpec, int decimals, boolean household) {
        RiskModelClass first = riskSpec.getClasses().get(0);
        double offset = Math.log(first.getLeftValue());
        double mult = Math.log(first.getRightValue() / first.getLeftValue());
        LogarithmicNumberAxis domainAxis = new LogarithmicNumberAxis(offset, mult, decimals);
        NumberAxis rangeAxisLeft = new NumberAxis("Frequency");
        XYBarRenderer renderer = new XYBarRenderer(0);
        XYDataset dataset = getDataset(riskSpec, household);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        domainAxis.setMinorTickMarksVisible(false);
        Range range = new Range(0, riskSpec.getClasses().size());
        domainAxis.setTickUnit(new NumberTickUnit(riskSpec.getClasses().size() / 10));
        domainAxis.setAutoRange(false);
        domainAxis.setRange(range);
        rangeAxisLeft.setStandardTickUnits(NumberAxis.createStandardTickUnits(MuARGUS.getLocale()));

        XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxisLeft, renderer);
        NumberAxis rangeAxisRight = new NumberAxis();
        rangeAxisRight.setAutoRange(false);
        rangeAxisRight.setRange(rangeAxisLeft.getRange());
        rangeAxisRight.setStandardTickUnits(NumberAxis.createStandardTickUnits(MuARGUS.getLocale()));
        mainPlot.setRangeAxis(1, rangeAxisRight);
        mainPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);

        mainPlot.setDomainGridlinesVisible(true);

        JFreeChart chart = new JFreeChart(null, null, mainPlot, false);
        ChartPanel chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    /**
     * Gets the dataset containing the x and y coordinates of the risk chart.
     *
     * @param spec RiskSpecification instance containing the specifications for
     * the risk chart.
     * @param household Boolean indicating whether this variable is a household
     * variable.
     * @return XYDataset containing the x and y coordinates of the risk chart.
     */
    private XYDataset getDataset(RiskSpecification spec, boolean household) {
        XIntervalSeries series = new XIntervalSeries("1");
        int index = 0;
        for (RiskModelClass cl : spec.getClasses()) {
            series.add(index, index + 1, index, household ? cl.getHhFrequency() : cl.getFrequency());
            index++;
        }
        XIntervalSeriesCollection dataset = new XIntervalSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }
}
