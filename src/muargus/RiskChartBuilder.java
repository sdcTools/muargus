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

    public ChartPanel CreateChart(RiskSpecification riskSpec) {

        RiskModelClass first = riskSpec.getClasses().get(0);
        double offset = Math.log(first.getLeftValue());
        double mult = Math.log(first.getRightValue()/first.getLeftValue());
        LogarithmicNumberAxis domainAxis      = new LogarithmicNumberAxis(offset, mult);
        NumberAxis rangeAxis     = new NumberAxis("Freq");
        XYBarRenderer renderer   = new XYBarRenderer(0);
        XYDataset dataset        = getDataset(riskSpec);

        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        domainAxis.setMinorTickMarksVisible(false);
        Range range = new Range(0, riskSpec.getClasses().size());
        domainAxis.setTickUnit(new NumberTickUnit(1));
        //domainAxis.setNumberFormatOverride(new Numberformatter());
        domainAxis.setAutoRange(false);
        domainAxis.setRange(range);

        XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

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

