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
     * @param offset
     * @param mult
     * @param decimals Integer containing the number of decimals.
     */
    public LogarithmicNumberAxis(double offset, double mult, int decimals) {
        super("");
        this.offset = offset;
        this.mult = mult;
        this.decimals = decimals;
    }

    /**
     *
     * @param g2
     * @param state
     * @param dataArea
     * @param edge
     * @return
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
     *
     * @param value
     * @return
     */
    private String transformString(Number value) {
        String format = "%." + Integer.toString(this.decimals) + "f";
        return String.format(MuARGUS.getLocale(), format, Math.exp(this.offset + this.mult * value.doubleValue()));
    }

}
