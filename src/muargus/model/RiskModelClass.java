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
    private double leftValue;
    private double rightValue;
    private int frequency;
    private int hhFrequency;
    
    public RiskModelClass(double leftValue, double rightValue, int frequency, int hhFrequency) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.frequency = frequency;
        this.hhFrequency = hhFrequency;
    }
    
}