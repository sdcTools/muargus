/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

/**
 *
 * @author pibd05
 */
public class RiskModelClass {
    private final double leftValue;
    private final double rightValue;
    private final int frequency;
    private final int hhFrequency;
    
    public RiskModelClass(double leftValue, double rightValue, int frequency, int hhFrequency) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.frequency = frequency;
        this.hhFrequency = hhFrequency;
    }

    public double getLeftValue() {
        return this.leftValue;
    }

    public double getRightValue() {
        return this.rightValue;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int getHhFrequency() {
        return this.hhFrequency;
    }
    
    
    
}
