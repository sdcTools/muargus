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
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
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
 * Controller class of the ViewReport screen.
 *
 * @author Statistics Netherlands
 */
public class ViewReportController {

    private final ViewReportView view;
    private final String html;

    /**
     * Constructor for the ViewReportController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param xmlDoc Document containing the report as a xml document.
     */
    public ViewReportController(java.awt.Frame parentView, Document xmlDoc) {
        this.view = new ViewReportView(parentView, this, true, false);
        this.html = createHtmlFromXml(xmlDoc);
    }

    /**
     * Constructor for the ViewReportController. This constructor is used for
     * generating the news report.
     *
     * @param parentView the Frame of the mainFrame.
     * @param html String containing the html news report.
     * @param title String containing the title of the screen.
     */
    public ViewReportController(java.awt.Frame parentView, String html, String title) {
        this.view = new ViewReportView(parentView, this, true, true);
        this.view.setTitle(title);
        this.html = html;
    }

    /**
     * Shows the view.
     */
    public void showView() {
        if (this.html != null) {
            this.view.showReport(this.html);
            this.view.setVisible(true);

        }
    }

    /**
     * Saves the report.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while writing/saving the report.
     */
    public void saveReport(MetadataMu metadata) throws ArgusException {
        String path = metadata.getCombinations().getProtectedFile().getSafeMeta().getFileNames().getDataFileName();
        String htmlPath = FilenameUtils.removeExtension(path) + ".html";
        try (FileWriter writer = new FileWriter(new File(htmlPath))) {
            writer.write(this.html);
            SystemUtils.writeLogbook("Report has been written.");
        } catch (IOException ex) {
            throw new ArgusException("Error saving report: " + ex.getMessage());
        }
    }

    /**
     * Closes the view.
     */
    public void close() {
        this.view.setVisible(false);
    }

    /**
     * Creates a html code from a xml document.
     *
     * @param xmlDoc Document containing the report as a xml document.
     * @return String containing the converted xml document as html.
     */
    private String createHtmlFromXml(Document xmlDoc) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();

            tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter output = new StringWriter();

            tr.transform(new DOMSource(xmlDoc), new StreamResult(output));

            return output.toString();
        } catch (IllegalArgumentException | TransformerException ex) {
            this.view.showErrorMessage(new ArgusException("Error creating report: " + ex.getMessage()));
            return null;
        }
    }
}
