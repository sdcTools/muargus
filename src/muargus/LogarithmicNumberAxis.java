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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.ui.RectangleEdge;

/**
 * Class for generating a logarithmic number axis. This axis is used in the risk
 * model screen.
 *
 * @author Statistics Netherlands
 */
public class LogarithmicNumberAxis extends NumberAxis {

    private final double offset;
    private final double mult;
    private final int decimals;

    /**
     * Constructor of the LogarithmicNumberAxis class.
     *
     * @param offset Double containing the uppermost left value of the risk
     * chart.
     * @param mult Double containing the multiple for each bar.
     * @param decimals Integer containing the number of decimals.
     */
    public LogarithmicNumberAxis(double offset, double mult, int decimals) {
        super("");
        this.offset = offset;
        this.mult = mult;
        this.decimals = decimals;
    }

    /**
     * Represhes the ticks.
     *
     * @param g2 Graphics2D
     * @param state AxisState
     * @param dataArea Rectangle2D
     * @param edge RectangleEdge
     * @return List of NumberTicks.
     */
    @Override
    public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {
        List l = super.refreshTicks(g2, state, dataArea, edge);
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

    /**
     * Transforms a number to a String.
     *
     * @param value Number instance containing the value of the number tick.
     * @return String containing the value of the number tick.
     */
    private String transformString(Number value) {
        String format = "%." + Integer.toString(this.decimals) + "f";
        return String.format(MuARGUS.getLocale(), format, Math.exp(this.offset + this.mult * value.doubleValue()));
    }

}
