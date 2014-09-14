/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus;

import muargus.model.RiskModelClass;
import muargus.model.RiskSpecification;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.data.Range;
import org.jfree.data.xy.XIntervalSeries;
import org.jfree.data.xy.XIntervalSeriesCollection;

public class RiskChartBuilder {

    public ChartPanel CreateChart(RiskSpecification riskSpec, int decimals) {

        RiskModelClass first = riskSpec.getClasses().get(0);
        double offset = Math.log(first.getLeftValue());
        double mult = Math.log(first.getRightValue()/first.getLeftValue());
        LogarithmicNumberAxis domainAxis = new LogarithmicNumberAxis(offset, mult, decimals);
        NumberAxis rangeAxisLeft = new NumberAxis("Frequency");
        XYBarRenderer renderer = new XYBarRenderer(0);
        XYDataset dataset = getDataset(riskSpec);
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        domainAxis.setMinorTickMarksVisible(false);
        Range range = new Range(0, riskSpec.getClasses().size());
        domainAxis.setTickUnit(new NumberTickUnit(riskSpec.getClasses().size()/10));
        //domainAxis.setNumberFormatOverride(new Numberformatter());
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

    private XYDataset getDataset(RiskSpecification spec) {
        XIntervalSeries series = new XIntervalSeries("1");
        int index=0;
        for (RiskModelClass cl : spec.getClasses()) {
            series.add(index, index+1, index, cl.getFrequency());
            index++;
        }
        XIntervalSeriesCollection dataset = new XIntervalSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }
}

