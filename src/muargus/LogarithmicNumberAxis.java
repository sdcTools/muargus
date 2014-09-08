/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.ui.RectangleEdge;

/**
 *
 * 
 */
public class LogarithmicNumberAxis extends NumberAxis {
    private final double offset;
    private final double mult;
    
    public LogarithmicNumberAxis(double offset, double mult) {
        super("");
        this.offset = offset;
        this.mult = mult;
    }
    
    @Override
    public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
        List l =  super.refreshTicks(g2, state, dataArea, edge);
        List newList = new ArrayList<>();
        for (Object tick : l) {
            NumberTick numberTick = (NumberTick) tick;
            newList.add(new NumberTick(numberTick.getNumber(), 
                        transformString(numberTick.getNumber()),
                        numberTick.getTextAnchor(),
                        numberTick.getRotationAnchor(), 
                        numberTick.getAngle()));
        }
        return newList; //To change body of generated methods, choose Tools | Templates.
    }
    
    private String transformString(Number value) {
        int nDecimals = 4;
        String format = "%." + Integer.toString(nDecimals) + "f";
        return String.format(format, Math.exp(offset + mult*value.doubleValue()));
    }
    
}
