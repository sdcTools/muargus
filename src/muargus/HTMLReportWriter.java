/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import java.text.SimpleDateFormat;
import java.util.Date;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.model.MetadataMu;
import muargus.model.TableMu;
import muargus.model.VariableMu;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author pibd05
 */
public class HTMLReportWriter {

    private static CMuArgCtrl c = MuARGUS.getMuArgCtrl();
    private static Document doc;

    public static void createReportTree(
            Document document,
            MetadataMu metadata) {
        doc = document;
        Element html = addChildElement(doc, "html");
        html.appendChild(writeHeader());
        Element body = addChildElement(html, "body");
        addChildElement(body, "h1", "µ-ARGUS Report");

        body.appendChild(writeFilesTable(metadata));

        body.appendChild(writeIdVariablesTable(metadata));

        body.appendChild(writeFrequencyTablesTable(metadata));

            //body.appendChild(writeRelatedVariablesTable(metadata));
            //body.appendChild(writeGlobalRecodeTables(metadata));
            //body.appendChild(writeBaseIndividualRisk(metadata));
            //body.appendChild(writeSuppressionTable(metadata));
            //body.appendChild(writeSafeFileMetaTable(metadata));
        //body.appendChild(writeFooter());
    }

    private static Element writeFrequencyTablesTable(MetadataMu metadata) {
        Element p = doc.createElement("p");
        addChildElement(p, "h2", "Frequency tables used");
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "th", "Threshold");
        int size = metadata.getCombinations().getNumberOfColumns() - 2;
        for (int i = 1; i <= size; i++) {
            addChildElement(tr, "th", Integer.toString(i));
        }
        for(TableMu t: metadata.getCombinations().getTables()){
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", Integer.toString(t.getThreshold()));
            for(VariableMu v: t.getVariables()){
                addChildElement(tr, "td", v.getName());
            }
        }
        return p;
    }

    private static Element writeRelatedVariablesTable(MetadataMu metadata) {
        //TODO
        return null;
    }

    private static Element writeGlobalRecodeTables(MetadataMu metadata) {
        //TODO
        return null;
    }

    private static Element writeBaseIndividualRisk(MetadataMu metadata) {
        //TODO
        return null;
    }

    private static Element writeSuppressionTable(MetadataMu metadata) {
        //TODO
        return null;
    }

    private static Element writeSafeFileMetaTable(MetadataMu metadata) {
        //TODO
        return null;
    }

    private static Element writeFooter() {
        //TODO
        return null;
    }

    private static MetadataMu getSafeMeta(MetadataMu metadata) {
        return metadata.getCombinations().getProtectedFile().getSafeMeta();
    }
    
    private static Element writeIdVariablesTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);

        Element p = doc.createElement("p");
        addChildElement(p, "h2", "Identifying variables used");
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "th", "Variable");
        addChildElement(tr, "th", "No of categories (missings)");
        addChildElement(tr, "th", "Household var");
        
        for (VariableMu variable : metadata.getCombinations().getVariablesInTables()) {
            VariableMu safeVar = getSafeVar(safeMeta, variable.getName());
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", variable.getName());
            int missings = (safeVar.getMissing(1) != null && !"".equals(safeVar.getMissing(1))) ? 2 : 1;
            addChildElement(tr, "td", String.format("%d (%d)", safeVar.getnOfCodes(), missings));
            addChildElement(tr, "td", variable.isHousehold() ? "HHVar" : "");
        }
        
        return p;
    }

    private static VariableMu getSafeVar(MetadataMu safeMeta, String varName) {
        for (VariableMu variable : safeMeta.getVariables()) {
            if (variable.getName().equals(varName)) {
                return variable;
            }
        }
        return null;
    }
    
    private static Element writeFilesTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);
        Element p = doc.createElement("p");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ', time ' HH:mm:ss");
        addChildElement(p, "h2", String.format("Safe file created date: %s",
                format.format(new Date())));
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Original data file");
        addChildElement(tr, "td", metadata.getFileNames().getDataFileName());
        tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Original meta file");
        addChildElement(tr, "td", metadata.getFileNames().getMetaFileName());
        tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Number of records");
        addChildElement(tr, "td", Integer.toString(metadata.getRecordCount()));
        tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Safe data file");
        addChildElement(tr, "td", safeMeta.getFileNames().getDataFileName());
        tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Safe meta file");
        addChildElement(tr, "td", safeMeta.getFileNames().getMetaFileName());
        return p;
    }

    private static Element writeHeader() {
        Element elm = doc.createElement("head");
        addChildElement(elm, "title", "µ-ARGUS Report");
        Element meta = addChildElement(elm, "META", "name", "author");
        meta.setAttribute("content", "Statistics; Netherlands");
        Element link = addChildElement(elm, "link", "rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", "file:///c:/program files/mu_argus/muargus.css");    //TODO
        return elm;
    }

    private static Element addChildElement(Node parent, String name) {
        return addChildElement(parent, name, null, null);
    }

    private static Element addChildElement(Node parent, String name, String content) {
        Element elm = addChildElement(parent, name);
        elm.setTextContent(content);
        return elm;
    }

    private static Element addChildElement(Node parent, String name, String attrName, String attrValue) {
        Element elm = (Element) parent.appendChild(doc.createElement(name));
        if (attrName != null) {
            elm.setAttribute(attrName, attrValue);
        }
        return elm;
    }

}
