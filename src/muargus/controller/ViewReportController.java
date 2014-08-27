/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import javax.swing.text.html.HTMLDocument;
import muargus.view.ViewReportView;

/**
 *
 * @author ambargus
 */
public class ViewReportController {
    
    private ViewReportView view;
    private HTMLDocument htmlDoc;

    /**
     * 
     * @param view 
     */
    public ViewReportController(java.awt.Frame parentView, HTMLDocument htmlDoc) {
        this.view = new ViewReportView(parentView, this, true);
        this.htmlDoc = htmlDoc;
    }
    
    public void showView() {
        this.view.showReport(htmlDoc);
        this.view.setVisible(true);
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
