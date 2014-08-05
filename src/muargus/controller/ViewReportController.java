/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import muargus.view.ViewReportView;

/**
 *
 * @author ambargus
 */
public class ViewReportController {
    
    ViewReportView view;

    /**
     * 
     * @param view 
     */
    public ViewReportController(ViewReportView view) {
        this.view = view;
    }
    
    /**
     * 
     */
    public void close() {                                         
        view.setVisible(false);
    }                                        

    /**
     * 
     */
    public void print() {                                         
        // TODO add your handling code here:
    } 
    
}
