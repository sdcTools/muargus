/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import argus.model.ArgusException;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import muargus.controller.SpssUtils;
import muargus.io.MetaReader;
import muargus.model.CodeInfo;
import muargus.model.MetadataMu;
import muargus.model.MicroaggregationSpec;
import muargus.model.ModifyNumericalVariablesSpec;
import muargus.model.PramVariableSpec;
import muargus.model.ProtectedFile;
import muargus.model.RankSwappingSpec;
import muargus.model.RecodeMu;
import muargus.model.ReplacementSpec;
import muargus.model.RiskSpecification;
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

    private static Document doc;

    public static void createReportTree(Document document, MetadataMu metadata) {
        doc = document;
        Element html = addChildElement(doc, "html");
        html.appendChild(writeHeader());
        Element body = addChildElement(html, "body");
        addChildElement(body, "h1", "µ-ARGUS Report");

        body.appendChild(writeFilesTable(metadata));

        body.appendChild(writeIdVariablesTable(metadata));

        body.appendChild(writeFrequencyTablesTable(metadata));

        body.appendChild(writeRelatedVariablesTable(metadata));
        body.appendChild(writeGlobalRecodeTables(metadata));
        if (metadata.getCombinations().isRiskModel()) {
            body.appendChild(writeBaseIndividualRisk(metadata));
        }
        if (hasOtherModifications(metadata)) {
            body.appendChild(writeOtherModificationsTable(metadata));
        } else {
            Element p = doc.createElement("p");
            addChildElement(p, "h2", "No other modifications");
            body.appendChild(p);
        }
        //body.appendChild(writePRAMTable(metadata));
        body.appendChild(writeSuppressionTable(metadata));
        body.appendChild(writeSafeFileMetaTable(metadata));
        body.appendChild(writeFooter());
    }

    private static Element writeOtherModificationsTable(MetadataMu metadata) {
        Element p = doc.createElement("p");
        addChildElement(p, "h2", "Other modifications");
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "th", "Method");
        addChildElement(tr, "th", "Variables");
        addChildElement(tr, "th", "Parameters/Remarks");
        for (ModifyNumericalVariablesSpec spec : metadata.getCombinations().getModifyNumericalVariables().getModifyNumericalVariablesSpec()) {
            //Bottom coding
            if (!Double.isNaN(spec.getBottomValue())) {
                tr = addChildElement(table, "tr");
                addChildElement(tr, "td", "Bottom coding");
                addChildElement(tr, "td", spec.getVariable().getName());
                addChildElement(tr, "td", String.format(
                        "Top value: %s; Replacement: %s", formatDouble(spec.getBottomValue(), 2, false), spec.getBottomReplacement()));
            }
            //Top coding
            if (!Double.isNaN(spec.getTopValue())) {
                tr = addChildElement(table, "tr");
                addChildElement(tr, "td", "Top coding");
                addChildElement(tr, "td", spec.getVariable().getName());
                addChildElement(tr, "td", String.format(
                        "Top value: %s; Replacement: %s", formatDouble(spec.getTopValue(), 2, false), spec.getTopReplacement()));
            }
            //Rounding    
            if (!Double.isNaN(spec.getRoundingBase())) {
                tr = addChildElement(table, "tr");
                addChildElement(tr, "td", "Rounding");
                addChildElement(tr, "td", spec.getVariable().getName());
                addChildElement(tr, "td", String.format(
                        "Rounding base: %s", formatDouble(spec.getRoundingBase(), 2, false)));
            }
            //Weight noise
            if (!Double.isNaN(spec.getWeightNoisePercentage())) {
                tr = addChildElement(table, "tr");
                addChildElement(tr, "td", "Weight noise");
                addChildElement(tr, "td", spec.getVariable().getName());
                addChildElement(tr, "td", String.format(
                        "%s %% random noise has been added ", formatDouble(spec.getWeightNoisePercentage(), 2, false)));
            }
        }
        for (ReplacementSpec replacement : metadata.getReplacementSpecs()) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", replacement instanceof RankSwappingSpec
                    ? "Rank swapping" : "Numerical microaggregation");
            addChildElement(tr, "td", VariableMu.printVariableNames(replacement.getVariables()));
            if (replacement instanceof RankSwappingSpec) {
                addChildElement(tr, "td",
                        String.format("Percentage: %d %%", ((RankSwappingSpec) replacement).getPercentage()));
            } else {
                MicroaggregationSpec microAggr = (MicroaggregationSpec) replacement;
                String optimal = microAggr.getVariables().size() == 1
                        ? String.format("; Optimal: %s", (microAggr.isOptimal() ? "yes" : "no")) : "";
                addChildElement(tr, "td",
                        String.format("Group size: %d%s", microAggr.getMinimalNumberOfRecords(), optimal));

            }
        }
        ProtectedFile protectedFile = metadata.getCombinations().getProtectedFile();
        if (protectedFile.isRandomizeOutput()) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", "Make safe file");
            addChildElement(tr, "td", "All");
            addChildElement(tr, "td", "Records have been written in random order");
        } else if (protectedFile.isPrintBHR()) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", "Make safe file");
            addChildElement(tr, "td", "All");
            addChildElement(tr, "td", "Risk has been added to output file");
        }
        if (protectedFile.getHouseholdType() == ProtectedFile.CHANGE_INTO_SEQUENCE_NUMBER) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", "Make safe file");
            for (VariableMu v : metadata.getVariables()) {
                if (v.isHouse_id()) {
                    addChildElement(tr, "td", v.getName());
                    break;
                }
            }
            addChildElement(tr, "td", "HouseHold Identification variable has been changed into a sequence number");
        } else if (protectedFile.getHouseholdType() == ProtectedFile.REMOVE_FROM_SAFE_FILE){
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", "Make safe file");
            for (VariableMu v : metadata.getVariables()) {
                if (v.isHouse_id()) {
                    addChildElement(tr, "td", v.getName());
                    break;
                }
            }
            addChildElement(tr, "td", "HouseHold Identification variable has been removed from the safe file");
        }

        return writePRAMTable(metadata, table, tr, p);
    }

    private static Element writePRAMTable(MetadataMu metadata, Element table, Element tr, Element p) {
        boolean pram = false;
        if (metadata.getCombinations().getPramSpecification() != null) {
            for (PramVariableSpec pramSpec : metadata.getCombinations().getPramSpecification().getPramVarSpec()) {
                if (pramSpec.isApplied()) {
                    pram = true;
                }
            }
        }

        if (pram) {
            ArrayList<PramVariableSpec> unequalProbability = new ArrayList<>();
            for (PramVariableSpec pramSpec : metadata.getCombinations().getPramSpecification().getPramVarSpec()) {
                if (pramSpec.isApplied()) {
                    int probability = pramSpec.getVariable().getCodeInfos().get(0).getPramProbability();
                    boolean equalProbability = true;
                    for (CodeInfo c : pramSpec.getVariable().getCodeInfos()) {
                        if (probability != c.getPramProbability()) {
                            equalProbability = false;
                            break;
                        }
                    }
                    tr = addChildElement(table, "tr");
                    addChildElement(tr, "td", "PRAM");
                    addChildElement(tr, "td", pramSpec.getVariable().getName());
                    if (equalProbability) {
                        addChildElement(tr, "td", "Diagonal probabilities are all " + probability);
                    } else {
                        addChildElement(tr, "td", "Unequal probabilities. See specifications below.");
                        unequalProbability.add(pramSpec);
                    }
                }
            }

            for (PramVariableSpec pramSpec : unequalProbability) {
                addChildElement(p, "h2", "");
                addChildElement(p, "h2", "PRAM-probability overview for " + pramSpec.getVariable().getName());
                Element table2 = addChildElement(p, "table");
                Element tr2 = addChildElement(table2, "tr");
                addChildElement(tr2, "th", "Code");
                addChildElement(tr2, "th", "Label");
                addChildElement(tr2, "th", "Probability");
                for (CodeInfo c : pramSpec.getVariable().getCodeInfos()) {
                    boolean missing = false;
                    for (int i = 0; i < pramSpec.getVariable().getNumberOfMissings(); i++) {
                        if (c.getCode().equals(pramSpec.getVariable().getMissing(i))) {
                            missing = true;
                            break;
                        }
                    }
                    if (!missing) {
                        tr2 = addChildElement(table2, "tr");
                        addChildElement(tr2, "td", c.getCode());
                        addChildElement(tr2, "td", c.getLabel());
                        addChildElement(tr2, "td", "" + c.getPramProbability());
                    }
                }

            }
        }
        return p;

    }

    private static boolean hasOtherModifications(MetadataMu metadata) {
        ProtectedFile protectedFile = metadata.getCombinations().getProtectedFile();
        boolean pram = false;
        if (metadata.getCombinations().getPramSpecification() != null) {
            for (PramVariableSpec pramSpec : metadata.getCombinations().getPramSpecification().getPramVarSpec()) {
                if (pramSpec.isApplied()) {
                    pram = true;
                }
            }
        }
        if (metadata.getCombinations().getModifyNumericalVariables().getModifyNumericalVariablesSpec().size() > 0
                || protectedFile.isPrintBHR()
                || protectedFile.isRandomizeOutput()
                || pram
                || (protectedFile.getHouseholdType() != ProtectedFile.NOT_HOUSEHOLD_DATA
                && protectedFile.getHouseholdType() != ProtectedFile.KEEP_IN_SAFE_FILE)) {
            return true;
        }
        return metadata.getReplacementSpecs().size() > 0;
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
        for (TableMu t : metadata.getCombinations().getTables()) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", Integer.toString(t.getThreshold()));
            for (VariableMu v : t.getVariables()) {
                addChildElement(tr, "td", v.getName());
            }
        }
        return p;
    }

    private static Element writeRelatedVariablesTable(MetadataMu metadata) {
        boolean isRelated = false;
        for (VariableMu v : metadata.getVariables()) {
            if (v.isRelated()) {
                isRelated = true;
            }
        }
        Element p = doc.createElement("p");

        if (isRelated) {
            addChildElement(p, "h2", "Related Variables");
            Element table = addChildElement(p, "table");
            Element tr = addChildElement(table, "tr");
            addChildElement(tr, "th", "Variable");
            addChildElement(tr, "th", "Related to");

            for (VariableMu v : metadata.getVariables()) {
                if (v.isRelated()) {
                    tr = addChildElement(table, "tr");
                    addChildElement(tr, "td", v.getName());
                    addChildElement(tr, "td", v.getRelatedVariable().getName());
                }
            }
        }
        return p;

    }

    private static Element writeGlobalRecodeTables(MetadataMu metadata) {
        boolean recoded = false;
        if (metadata.getCombinations().getGlobalRecode() != null) {
            for (RecodeMu r : metadata.getCombinations().getGlobalRecode().getRecodeMus()) {
                if (r.isRecoded() || r.isTruncated()) {
                    recoded = true;
                }
            }
        }

        Element p = doc.createElement("p");
        if (recoded) {
            addChildElement(p, "h2", "GlobalRecodings that have been applied:");
            for (RecodeMu r : metadata.getCombinations().getGlobalRecode().getRecodeMus()) {
                if (r.isRecoded()) {
                    addChildElement(p, "h2", r.getVariable().getName());
                    Element table = addChildElement(p, "table");
                    Element tr = addChildElement(table, "tr");
                    addChildElement(tr, "th", "Code");
                    addChildElement(tr, "th", "Categories");
                    try {
                        MetaReader.readGrc(r.getGrcFile(), r);
                        BufferedReader reader = new BufferedReader(new StringReader(r.getGrcText()));
                        Tokenizer tokenizer = new Tokenizer(reader);
                        String line;
                        while ((line = tokenizer.nextLine()) != null && !line.substring(0, 1).equals("<")) {
                            tr = addChildElement(table, "tr");
                            addChildElement(tr, "td", line.substring(0, line.indexOf(":")));
                            addChildElement(tr, "td", line.substring(line.indexOf(":") + 1));
                        }
                        tr = addChildElement(table, "tr");
                        addChildElement(tr, "td", r.getMissing_1_new());
                        addChildElement(tr, "td", "Missing 1");
                        if (!r.getMissing_2_new().isEmpty()) {
                            tr = addChildElement(table, "tr");
                            addChildElement(tr, "td", r.getMissing_2_new());
                            addChildElement(tr, "td", "Missing 2");
                        }

                    } catch (ArgusException ex) {
                        //Logger.getLogger(HTMLReportWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (r.isTruncated()) {
                    addChildElement(p, "h2", r.getVariable().getName());
                    addChildElement(p, "h2", r.getPositionsTruncated() + " digit has been truncated");
                }
            }
        } else {
            addChildElement(p, "h2", "No global recodings have been applied");
        }
        return p;
//        } catch (Exception e) {
//            Element p = doc.createElement("p");
//            addChildElement(p, "h2", "No global recodings have been applied");
//            return p;
//        }

    }

    //private static String formatDouble(double d, int decimals) {
    //    String format = "%." + decimals + "f";
    //    return String.format(MuARGUS.getLocale(), format, d);
    //}
    private static Element writeBaseIndividualRisk(MetadataMu metadata) {
        Element p = doc.createElement("p");
        addChildElement(p, "h2", String.format("Base %s Risk has been applied:",
                metadata.isHouseholdData() ? "Household" : "Individual"));
        for (TableMu t : metadata.getCombinations().getTables()) {
            if (t.isRiskModel()) {
                String table = "Table: ";
                for (VariableMu v : t.getVariables()) {
                    table = table + v.getName() + " x ";
                }
                table = table.substring(0, table.length() - 3);
                addChildElement(p, "h2", table);
                RiskSpecification riskSpec = metadata.getCombinations().getRiskSpecifications().get(t);
                if (metadata.isHouseholdData()) {
                    addChildElement(p, "h2", "Household risk: " + formatDouble(riskSpec == null ? 0 : riskSpec.getRiskThreshold(), 5, true));
                } else {
                    addChildElement(p, "h2", "Ind. risk: " + formatDouble(riskSpec == null ? 0 : riskSpec.getRiskThreshold(), 5, true));
                    addChildElement(p, "h2", "Ind. re-ident rate: " + formatDouble(riskSpec == null ? 0 : riskSpec.getReidentRateThreshold() * 100, 3, true) + " %");
                }

                addChildElement(p, "h2", "");
            }
        }
        return p;
    }

    private static Element writeSuppressionTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);

        Element p = doc.createElement("p");
        addChildElement(p, "h2", "Suppression overview ");
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "th", "Name");
        if (metadata.getCombinations().getProtectedFile().isWithEntropy()) {
            addChildElement(tr, "th", "Entropy");
        } else {
            addChildElement(tr, "th", "Suppression Weight");
        }
        addChildElement(tr, "th", "Number of suppressions");

        int suppressions = 0;
        for (VariableMu v : safeMeta.getVariables()) {
            if (v.isCategorical()) {
                tr = addChildElement(table, "tr");
                addChildElement(tr, "td", v.getName());
                if (metadata.getCombinations().getProtectedFile().isWithEntropy()) {
                    addChildElement(tr, "td", formatDouble(v.getEntropy(), 2, true));
                } else {
                    addChildElement(tr, "td", Integer.toString(v.getSuppressweight()));
                }
                addChildElement(tr, "td", Integer.toString(v.getnOfSuppressions()));
                suppressions += v.getnOfSuppressions();
            }
        }
        tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Total");
        addChildElement(tr, "td", "");
        addChildElement(tr, "td", Integer.toString(suppressions));

        return p;
    }

    private static String formatDouble(double d, int decimals, boolean showIfZero) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(MuARGUS.getLocale());
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setMaximumFractionDigits(decimals);
        decimalFormat.setMinimumFractionDigits(showIfZero ? decimals : 0);
        return decimalFormat.format(d);

    }

    private static Element writeSafeFileMetaTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);

        Element p = doc.createElement("p");
        addChildElement(p, "h2", "Record description safe file");
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "th", "Name");
        addChildElement(tr, "th", "Starting position");
        addChildElement(tr, "th", "Length");
        addChildElement(tr, "th", "Decimal");
        for (VariableMu v : safeMeta.getVariables()) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", v.getName());
            addChildElement(tr, "td", Integer.toString(v.getStartingPosition()));
            addChildElement(tr, "td", Integer.toString(v.getVariableLength()));
            addChildElement(tr, "td", Integer.toString(v.getDecimals()));
        }
        return p;
    }

    private static Element writeFooter() {
        Element p = doc.createElement("p");
        addChildElement(p, "h2", String.format("μ-ARGUS version: %d.%d.%s (build: %d)",
                MuARGUS.MAJOR, MuARGUS.MINOR, MuARGUS.REVISION, MuARGUS.BUILD));
        return p;
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
        if (metadata.isSpss()) {
            addChildElement(tr, "td", SpssUtils.spssDataFileName);
        } else {
            addChildElement(tr, "td", metadata.getFileNames().getDataFileName());
        }
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
