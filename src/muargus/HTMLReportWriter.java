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
package muargus;

import argus.model.ArgusException;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import muargus.io.MetaReader;
import muargus.model.AnonDataSpec;
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
import muargus.model.SyntheticDataSpec;
import muargus.model.TableMu;
import muargus.model.TargetSwappingSpec;
import muargus.model.VariableMu;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Class for writing the HTML report.
 *
 * @author Statistics Netherlands
 */
public class HTMLReportWriter {

    private static Document doc;
    public static final File css = new File("./resources/muargus.css");

    /**
     * Creates the report.
     *
     * @param document Document containing the html report.
     * @param metadata MetadataMu instance containing the metadata.
     */
    public static void createReportTree(Document document, MetadataMu metadata) {
        HTMLReportWriter.doc = document;
        Element html = addChildElement(HTMLReportWriter.doc, "html");
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
            Element p = HTMLReportWriter.doc.createElement("p");
            addChildElement(p, "h2", "No other modifications");
            body.appendChild(p);
        }
        body.appendChild(writeSuppressionTable(metadata));
        body.appendChild(writeSafeFileMetaTable(metadata));
        body.appendChild(writeFooter());
    }

    /**
     * Writes the other modifications table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the other modifications table.
     */
    private static Element writeOtherModificationsTable(MetadataMu metadata) {
        Element p = HTMLReportWriter.doc.createElement("p");
        addChildElement(p, "h2", "Other modifications");
        Element TRS, tr1, tr2;
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
            if (replacement instanceof AnonDataSpec){
                ArrayList<TableMu> TableSet = ((AnonDataSpec) replacement).getKAnonCombinations().getTables();
                for (int i=0; i < TableSet.size();i++){
                    tr = addChildElement(table,"tr");
                    addChildElement(tr, "td", TableSet.get(i).getThreshold()+1+"-anonymity");
                    addChildElement(tr, "td", VariableMu.printVariableNames(TableSet.get(i).getVariables()));
                    addChildElement(tr, "td", "Suppressions by sdcMicro");
                }
            }
            else{
                tr1 = addChildElement(table, "tr");
                addChildElement(tr1, "td", replacement.getReplacementFile().getReplacementType()); 
                if (replacement instanceof TargetSwappingSpec){
                    TRS=addChildElement(tr1,"td");
                    tr2=addChildElement(TRS,"table","class","inrow");
                    tr=addChildElement(tr2,"tr");
                    addChildElement(tr,"td","Similarity:");
                    String Info="";
                    for (int index : ((TargetSwappingSpec) replacement).getSimilarIndexes()){
                        Info = Info + " " + replacement.getOutputVariables().get(index).getName() + ",";
                    }
                    addChildElement(tr,"td",Info.substring(0,Info.length()-1));
                    tr=addChildElement(tr2,"tr");
                    addChildElement(tr,"td","Hierarchy:");
                    Info="";
                    for (int index : ((TargetSwappingSpec) replacement).getHierarchyIndexes()){
                        Info = Info + " " + replacement.getOutputVariables().get(index).getName() + ",";
                    }
                    addChildElement(tr,"td",Info.substring(0,Info.length()-1));
                    tr=addChildElement(tr2,"tr");
                    addChildElement(tr,"td","Risk:");
                    Info="";
                    for (int index : ((TargetSwappingSpec) replacement).getRiskIndexes()){
                        Info = Info + " " + replacement.getOutputVariables().get(index).getName() + ",";
                    }
                    addChildElement(tr,"td",Info.substring(0,Info.length()-1));
                    tr=addChildElement(tr2,"tr");
                    addChildElement(tr,"td","CarryOver:");
                    Info="";
                    for (int index : ((TargetSwappingSpec) replacement).getCarryIndexes()){
                        Info = Info + " " + replacement.getOutputVariables().get(index).getName() + ",";
                    }
                    addChildElement(tr,"td",Info.substring(0,Info.length()-1));
                    tr=addChildElement(tr2,"tr");
                    addChildElement(tr,"td","HouseholdID:");
                    addChildElement(tr,"td",replacement.getOutputVariables().get(((TargetSwappingSpec) replacement).getHHID()).getName());
                } else addChildElement(tr, "td", VariableMu.printVariableNames(replacement.getOutputVariables()));
                if (replacement instanceof RankSwappingSpec) {
                    addChildElement(tr, "td", String.format("Percentage: %d %%", ((RankSwappingSpec) replacement).getPercentage()));
                } else if (replacement instanceof MicroaggregationSpec) {
                    MicroaggregationSpec microAggr = (MicroaggregationSpec) replacement;
                    String optimal = microAggr.getOutputVariables().size() == 1
                        ? String.format("; Optimal: %s", (microAggr.isOptimal() ? "yes" : "no")) : "";
                    addChildElement(tr, "td", String.format("Group size: %d%s", microAggr.getMinimalNumberOfRecords(), optimal));
                } else if (replacement instanceof SyntheticDataSpec) {
                    String alpha = "Alpha values:";
                    for (VariableMu v : replacement.getOutputVariables()) {
                        alpha = alpha + " " + v.getAlpha() + ",";
                    }
                    addChildElement(tr, "td", alpha.substring(0, alpha.length() - 1));
                } else if (replacement instanceof TargetSwappingSpec){
                        TRS=addChildElement(tr1,"td");
                        tr2=addChildElement(TRS,"table","class","inrow");
                        tr=addChildElement(tr2,"tr");
                        addChildElement(tr,"td","Seed:");
                        addChildElement(tr,"td",String.format("%d",((TargetSwappingSpec) replacement).getSeed()));
                        tr=addChildElement(tr2,"tr");
                        addChildElement(tr,"td","Swaprate:");
                        addChildElement(tr,"td",String.format("%5.3f",((TargetSwappingSpec) replacement).getSwaprate()));
                        tr=addChildElement(tr2,"tr");
                        addChildElement(tr,"td","RiskThreshold (k):");
                        addChildElement(tr,"td",String.format("%d",((TargetSwappingSpec) replacement).getkThreshold()));
                }
                
                if (replacement instanceof TargetSwappingSpec){
                    addChildElement(p, "h2", "Info on Targeted Record Swapping");
                    table = addChildElement(p, "table");
                    tr=addChildElement(table,"tr");
                    addChildElement(tr,"td","Number of swapped households:");
                    addChildElement(tr,"td",String.format("%d",((TargetSwappingSpec) replacement).getCountSwappedHID()));
                    tr=addChildElement(table,"tr");
                    addChildElement(tr,"td","Number of swapped records:");
                    addChildElement(tr,"td",String.format("%d",((TargetSwappingSpec) replacement).getCountSwappedRecords()));
                    if (((TargetSwappingSpec) replacement).getCountNoDonor()>0){
                        tr=addChildElement(table,"tr");
                        addChildElement(tr,"td","Number of households without donor:");
                        addChildElement(tr,"td",String.format("%d",((TargetSwappingSpec) replacement).getCountNoDonor()));
                        table = addChildElement(p,"table");
                        tr=addChildElement(table,"tr");
                        addChildElement(tr,"td","See "+MuARGUS.getTempDir()+"\\NonSwappedHID.txt for HIDs of households for which no donor was found.");
                    }
                }
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
        } else if (protectedFile.getHouseholdType() == ProtectedFile.REMOVE_FROM_SAFE_FILE) {
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

    /**
     * Writes the PRAM specification table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @param table Element for the table.
     * @param tr Element for the table row.
     * @param p Element containing the other modifications table.
     * @return Element containing the PRAM specification table.
     */
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

    /**
     * Checks whether there are other modifications.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Boolean indicating whether there are other modifications.
     */
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

    /**
     * Writes the frequency table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the frequency table.
     */
    private static Element writeFrequencyTablesTable(MetadataMu metadata) {
        Element p = HTMLReportWriter.doc.createElement("p");
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
            addChildElement(tr, "td", "k = " + Integer.toString(t.getThreshold()));
            for (VariableMu v : t.getVariables()) {
                addChildElement(tr, "td", v.getName());
            }
        }
        return p;
    }

    /**
     * Writes the related variables table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the related variables table.
     */
    private static Element writeRelatedVariablesTable(MetadataMu metadata) {
        boolean isRelated = false;
        for (VariableMu v : metadata.getVariables()) {
            if (v.isRelated()) {
                isRelated = true;
            }
        }
        Element p = HTMLReportWriter.doc.createElement("p");

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

    /**
     * Writes the global recode table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the global recode table.
     */
    private static Element writeGlobalRecodeTables(MetadataMu metadata) {
        boolean recoded = false;
        if (metadata.getCombinations().getGlobalRecode() != null) {
            for (RecodeMu r : metadata.getCombinations().getGlobalRecode().getRecodeMus()) {
                if (r.isRecoded() || r.isTruncated()) {
                    recoded = true;
                }
            }
        }

        Element p = HTMLReportWriter.doc.createElement("p");
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
    }

    /**
     * Writes base individual risk.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the base individual risk.
     */
    private static Element writeBaseIndividualRisk(MetadataMu metadata) {
        Element p = HTMLReportWriter.doc.createElement("p");
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

    /**
     * Writes the suppression table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the suppression table.
     */
    private static Element writeSuppressionTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);

        Element p = HTMLReportWriter.doc.createElement("p");
        addChildElement(p, "h2", "Suppression overview ");
        
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        if (metadata.getCombinations().getProtectedFile().getSuppressionType()==0){
            addChildElement(tr,"td","\"No suppression\" selected");
        }
        else{        
        addChildElement(tr, "th", "Name");
        if (metadata.getCombinations().getProtectedFile().isWithEntropy()) {
            addChildElement(tr, "th", "Entropy");
        } else {
            addChildElement(tr, "th", "Suppression Priority");
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
                    addChildElement(tr, "td", Integer.toString(v.getSuppressPriority()));
                }
                addChildElement(tr, "td", Integer.toString(v.getnOfSuppressions()));
                suppressions += v.getnOfSuppressions();
            }
        }
        tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Total");
        addChildElement(tr, "td", "");
        addChildElement(tr, "td", Integer.toString(suppressions));
        }
        return p;
    }

    /**
     * Converts a double to a String.
     *
     * @param d Double to be converted.
     * @param decimals Integer containing the number of decimels.
     * @param showIfZero Boolean indicating whether a double should be shown if
     * it is zero.
     * @return String containing the converted double.
     */
    private static String formatDouble(double d, int decimals, boolean showIfZero) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(MuARGUS.getLocale());
        decimalFormat.setGroupingUsed(false);
        decimalFormat.setMaximumFractionDigits(decimals);
        decimalFormat.setMinimumFractionDigits(showIfZero ? decimals : 0);
        return decimalFormat.format(d);
    }

    /**
     * Writes the safe file meta table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the safe file meta table.
     */
    private static Element writeSafeFileMetaTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);

        Element p = HTMLReportWriter.doc.createElement("p");
        addChildElement(p, "h2", "Record description safe file");
        String format="Fixed format";
        switch(metadata.getDataFileType()){
            case MetadataMu.DATA_FILE_TYPE_FREE:
            case MetadataMu.DATA_FILE_TYPE_FREE_WITH_META:
                format = "Free format";
                break;
            case MetadataMu.DATA_FILE_TYPE_SPSS:
                format = "SPSS";
                break;
        }
        addChildElement(p, "h2", String.format("Safe file format = %s",format));
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "th", "Name");
        if (metadata.getDataFileType()==MetadataMu.DATA_FILE_TYPE_FIXED){
            addChildElement(tr, "th", "Starting position");
        }
        addChildElement(tr, "th", "Length");
        addChildElement(tr, "th", "Decimal");
        for (VariableMu v : safeMeta.getVariables()) {
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", v.getName());
            if (metadata.getDataFileType()==MetadataMu.DATA_FILE_TYPE_FIXED){
                addChildElement(tr, "td", Integer.toString(v.getStartingPosition()));
            }
            addChildElement(tr, "td", Integer.toString(v.getVariableLength()));
            addChildElement(tr, "td", Integer.toString(v.getDecimals()));
        }
        return p;
    }

    /**
     * Writes the Footer.
     *
     * @return Element containing the Footer.
     */
    private static Element writeFooter() {
        Element p = HTMLReportWriter.doc.createElement("p");
        addChildElement(p, "h2", "\u03bc-ARGUS version: " + String.format("%d.%d.%s (build: %d)",
                MuARGUS.MAJOR, MuARGUS.MINOR, MuARGUS.REVISION, MuARGUS.BUILD));
        return p;
    }

    /**
     * Gets the safe metadata.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return MetadataMu instance containing the safe metadata.
     */
    private static MetadataMu getSafeMeta(MetadataMu metadata) {
        return metadata.getCombinations().getProtectedFile().getSafeMeta();
    }

    /**
     * Writes the variables table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the variables table.
     */
    private static Element writeIdVariablesTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);

        Element p = HTMLReportWriter.doc.createElement("p");
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

    /**
     * Gets the safe variable.
     *
     * @param safeMeta MetadataMu instance containing the safe metadata.
     * @param varName String containing the variable name.
     * @return VariableMu instance containing the safe variable.
     */
    private static VariableMu getSafeVar(MetadataMu safeMeta, String varName) {
        for (VariableMu variable : safeMeta.getVariables()) {
            if (variable.getName().equals(varName)) {
                return variable;
            }
        }
        return null;
    }

    /**
     * Writes the files table.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @return Element containing the files table.
     */
    private static Element writeFilesTable(MetadataMu metadata) {
        MetadataMu safeMeta = getSafeMeta(metadata);
        Element p = HTMLReportWriter.doc.createElement("p");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ', time ' HH:mm:ss");
        addChildElement(p, "h2", String.format("Safe file created date: %s",
                format.format(new Date())));
        Element table = addChildElement(p, "table");
        Element tr = addChildElement(table, "tr");
        addChildElement(tr, "td", "Original data file");
        if (metadata.isSpss()) {
            addChildElement(tr, "td", MuARGUS.getSpssUtils().spssDataFileName);
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
        if (metadata.isSpss()) {
            addChildElement(tr, "td", MuARGUS.getSpssUtils().safeSpssFile.getAbsolutePath());
        } else {
            addChildElement(tr, "td", safeMeta.getFileNames().getDataFileName());
            tr = addChildElement(table, "tr");
            addChildElement(tr, "td", "Safe meta file");
            addChildElement(tr, "td", safeMeta.getFileNames().getMetaFileName());
        }
        return p;
    }

    /**
     * Writes the header.
     *
     * @return Element containing the header.
     */
    private static Element writeHeader() {
        Element elm = HTMLReportWriter.doc.createElement("head");
        addChildElement(elm, "title", "\u03bc-ARGUS Report");
        Element meta = addChildElement(elm, "META", "name", "author");
        meta.setAttribute("content", "Statistics; Netherlands");
        Element link = addChildElement(elm, "link", "rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", "file:///" + HTMLReportWriter.css.getAbsolutePath());
        return elm;
    }

    /**
     * Adds a child element.
     *
     * @param parent Node from the parent.
     * @param name String containing the name of the element.
     * @return Element containing the added child.
     */
    private static Element addChildElement(Node parent, String name) {
        return addChildElement(parent, name, null, null);
    }

    /**
     * Adds a child element.
     *
     * @param parent Node from the parent.
     * @param name String containing the name of the element.
     * @param content String containing the content.
     * @return Element containing the added child.
     */
    private static Element addChildElement(Node parent, String name, String content) {
        Element elm = addChildElement(parent, name);
        elm.setTextContent(content);
        return elm;
    }

    /**
     * Adds a child element.
     *
     * @param parent Node from the parent.
     * @param name String containing the name of the element.
     * @param attrName String containing the attribute name.
     * @param attrValue String containing the attribute value.
     * @return Element containing the added child.
     */
    private static Element addChildElement(Node parent, String name, String attrName, String attrValue) {
        Element elm = (Element) parent.appendChild(HTMLReportWriter.doc.createElement(name));
        if (attrName != null) {
            elm.setAttribute(attrName, attrValue);
        }
        return elm;
    }

}
