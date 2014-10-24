/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.controller;

import argus.model.ArgusException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import javax.swing.text.html.HTMLDocument;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import muargus.model.MetadataMu;
import muargus.view.ViewReportView;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;

/**
 *
 * @author Statistics Netherlands
 */
public class ViewReportController {

    private final ViewReportView view;
    private final String html;

    /**
     *
     * @param parentView
     * @param xmlDoc
     */
    public ViewReportController(java.awt.Frame parentView, Document xmlDoc) {
        this.view = new ViewReportView(parentView, this, true);
        
        this.html = createHtmlFromXml(xmlDoc);
    }

    /**
     *
     * @param parentView
     * @param html
     * @param title
     */
    public ViewReportController(java.awt.Frame parentView, String html, String title) {
        this.view = new ViewReportView(parentView, this, true);
        this.view.setTitle(title);
        this.html = html;
    }

    public void showView() {
        if (html != null) {
            this.view.showReport(html);
            this.view.setVisible(true);
        }
    }

    public void showView(HTMLDocument htmlDoc) {
        this.view.showReport(htmlDoc);
        this.view.setVisible(true);
    }

    public void saveReport(MetadataMu metadata) throws ArgusException {
        String path = metadata.getCombinations().getProtectedFile().getSafeMeta().getFileNames().getDataFileName();
        String htmlPath = FilenameUtils.removeExtension(path) + ".html";
        try (FileWriter writer = new FileWriter(new File(htmlPath))) {
            writer.write(html);
        } catch (IOException ex) {
            throw new ArgusException("Error saving report: " + ex.getMessage());
        }
    }

    /**
     *
     */
    public void close() {
        view.setVisible(false);
    }

    private String createHtmlFromXml(Document xmlDoc) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();

            tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter output = new StringWriter();

            tr.transform(new DOMSource(xmlDoc), new StreamResult(output));

            return output.toString();
        } catch (IllegalArgumentException | TransformerException ex) {
            view.showErrorMessage(new ArgusException("Error creating report: " + ex.getMessage()));
            return null;
        }
    }
}
