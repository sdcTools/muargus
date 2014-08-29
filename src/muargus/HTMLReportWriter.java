/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus;

import java.util.HashSet;
import javax.swing.text.html.HTMLDocument;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.model.GlobalRecode;
import muargus.model.MetadataMu;
import muargus.model.Combinations;
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
            MetadataMu metadata) 
    {
            doc = document;
            Element html = addChildElement(doc, "html");
            html.appendChild(writeHeader());
            Element body = addChildElement(html, "body");
            addChildElement(body, "h1", "µ-ARGUS Report");
            addChildElement(body, "div", "abc");
//            el = el.appendChild(doc.createElement("script"));
//            el.setTextContent("blabla");
//            el = elm.appendChild(doc.createElement("body"));
//              
//            el = el.appendChild(doc.createElement("div"));
//            el = el.appendChild(doc.createElement("table"));
//            el = el.appendChild(doc.createElement("tr"));
//            el.appendChild(doc.createElement("td")).setTextContent("1");
//            el.appendChild(doc.createElement("td")).setTextContent("2");
            
            //el.setTextContent(Integer.toString(this.metadata.getVariables().size()));

    }
    
    private static Element writeHeader() {
        Element elm = doc.createElement("head");
        addChildElement(elm, "title", "µ-ARGUS Report");
        Element meta = addChildElement(elm, "META", "name", "author");
        meta.setAttribute("content", "Statistics; Netherlands");
        Element link = addChildElement(elm, "link", "rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", "file:///blabla.css");    //TODO
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
