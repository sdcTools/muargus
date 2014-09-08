/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus;

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

    public ChartPanel CreateChart() {

        LogarithmicNumberAxis domainAxis      = new LogarithmicNumberAxis("");
        NumberAxis rangeAxis     = new NumberAxis("y");
        XYBarRenderer renderer   = new XYBarRenderer(0);
        XYDataset dataset        = getDataset();
//        XYDataset dataset        = getDayDataset();
//        XYDataset dataset        = getSecondDataset();

        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        domainAxis.setMinorTickMarksVisible(false);
        domainAxis.setTickUnit(new NumberTickUnit(1));
        //domainAxis.setNumberFormatOverride(new Numberformatter());
        domainAxis.setAutoRange(false);
        domainAxis.setRange(new Range(0,10));
        //domainAxis.setVerticalTickLabels(true);
        XYPlot mainPlot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);

        mainPlot.setDomainGridlinesVisible(true);
        //domainAxis.setTsetTickMarkPosition( DateTickMarkPosition.MIDDLE );

        
        JFreeChart chart = new JFreeChart(null, null, mainPlot, false);
        ChartPanel chartPanel = new ChartPanel(chart);
       //chartPanel.setPreferredSize(new Dimension(400, 300));
        

        //this.pack();
        
        return chartPanel;
    }

    private XYDataset getDataset() {

            XIntervalSeries series = new XIntervalSeries("1");
            series.add(0, 1, 0, 100);
            series.add(1, 2, 1, 80);
            series.add(2, 3, 2, 120);
            series.add(3, 4, 3, 20);
            series.add(4, 5, 4, 10);
            series.add(5, 6, 5, 70);
            series.add(6, 7, 6, 90);
            series.add(7, 8, 7, 35);
            series.add(8, 9, 8, 20);
            series.add(9, 10, 9, 5);

            
            XIntervalSeriesCollection dataset = new XIntervalSeriesCollection();
            dataset.addSeries(series);

            return dataset;
    }


}

