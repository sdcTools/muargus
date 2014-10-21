// TODO: Combine buttonlist methods
package muargus.controller;

import argus.model.ArgusException;
import argus.utils.StrUtils;
import java.util.logging.Logger;
import muargus.io.MetaReader;
import muargus.io.MetaWriter;
import muargus.model.Combinations;
import muargus.model.MetadataMu;
import muargus.model.VariableMu;
import muargus.view.SpecifyMetadataView;

/**
 * Controller class of the SpecifyMetadata screen.
 *
 * @author Statistics Netherlands
 */
public class SpecifyMetadataController extends ControllerBase<MetadataMu> {

    private final MetadataMu metadataClone;

    private static final Logger logger = Logger.getLogger(SpecifyMetadataController.class.getName());

    /**
     * Constructor for the SpecifyMetadataController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SpecifyMetadataController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SpecifyMetadataView(parentView, true, this));
        setModel(metadata);
        this.metadataClone = new MetadataMu(metadata);
        getView().setMetadata(this.metadataClone);
    }

//    /**
//     * Generates meta from the Spss data file
//     *
//     * @return
//     */
//    public ArrayList<SpssVariable> readSpssMetaFile() {
//        ArrayList<SpssVariable> variables = new ArrayList<>();
//        ArrayList<ArrayList<String>> data = new ArrayList<>();
//        BufferedReader reader = null;
//        try {
//
//            File file = new File("C:\\\\Users\\\\Gebruiker\\\\Desktop\\\\metadata.txt");
//            reader = new BufferedReader(new FileReader(file));
//            Tokenizer tokenizer = new Tokenizer(reader);
//            tokenizer.nextLine();
//            int numberOfVariables = 0;
//            for (int i = 0; i < 4; i++) {
//                String line = tokenizer.nextLine();
//                System.out.println(line);
//                String value;
//                ArrayList<String> temp = new ArrayList<>();
//                data.add(temp);
//                if (i == 0) {
//                    do {
//                        value = tokenizer.nextToken();
//                        data.get(i).add(value);
//                        numberOfVariables++;
//                    } while (!value.equals(""));
//                } else {
//                    for(int j  = 0; j < numberOfVariables; j++){
//                        value = tokenizer.nextToken();
////                        if(value = null){
////                            System.out.println("dafad");
////                        }
//                        data.get(i).add(value);
//                    }
//                }
//
//            }
////            while (tokenizer.nextLine() != null) {
////                String value;
////                ArrayList<String> temp = new ArrayList<>();
////                data.add(temp);
////                do {
////                    value = tokenizer.nextToken();
////                    data.get(column).add(value);
////                } while (!value.equals(""));
////                column++;
////            }
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(SpecifyMetadataController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                reader.close();
//            } catch (IOException ex) {
//                Logger.getLogger(SpecifyMetadataController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        // wat te doen bij lege waardes
//        for (int i = 0; i < data.get(1).size(); i++) {
//            if (!data.get(1).get(i).equals("")) {
//                SpssVariable variable = new SpssVariable(data.get(0).get(i), data.get(1).get(i),
//                        data.get(2).get(i), data.get(3).get(i));
//                variables.add(variable);
//            }
//        }
//
//        return variables;
//    }
//
    public boolean doesVariableExist(VariableMu variable) {
        boolean doubleVariable = false;
        for (VariableMu v : this.metadataClone.getVariables()) {
            if (v.getName().equals(variable.getName())) {
                doubleVariable = true;
            }
        }
        return doubleVariable;
    }
    
    public void removeVariable(String variableName){
        for (VariableMu v : this.metadataClone.getVariables()) {
            if (v.getName().equals(variableName)) {
                this.metadataClone.getVariables().remove(v);
                break;
            }
        }
    }
    
    public String getIntIfPossible(double value) {
        double value_double;
        String value_String = null;
        try {
            value_double = StrUtils.toDouble(Double.toString(value));
            if ((value_double == Math.floor(value_double)) && !Double.isInfinite(value_double)) {
                int value_int = (int) value_double;
                value_String = Integer.toString(value_int);
            } else {
                value_String = Double.toString(value_double);
            }
        } catch (ArgusException ex) {
            System.out.println("warning");
        }
        return value_String;
    }

    //TODO: verander zodat een file ingelezen wordt.

//    public String[] getCommand() {
//        String[] command = {
//            "********************************************************************************.",
//            "* Title/Objective: selectie van metadata uit actieve datafile wegschrijven.",
//            "* Context/Project: compatibiliteitsproblemen muArgus na upgrade naar Spss 20.",
//            "* Description: work-around die zorgt dat muArgus weer .sav bestanden accepteert.",
//            "* Author: ARSM.",
//            "* Maintainer: ARSM.",
//            "* Syntax: \\\\cbsp.nl\\Profiel\\Productie\\ARSM\\Desktop\\argus.sps.",
//            "* Last saved (yyyy-mm-dd @ hh:mm:ss): 2013-09-04 @ 16:26:50.",
//            "* SPSS & OS version: 20.0.0.2 on Windows 7.",
//            "********************************************************************************.",
//            "",
//            "",
//            "* testdata.",
//            "get file = \"" + metadataClone.getFileNames().getDataFileName() + "\".",
//            "* missing values jobcat (1, 3).", "", "*****.",
//            "* metadata voor Argus in %temp%/metadata.txt.",
//            "* regel 1: encoding",
//            "* regel 2: variabele namen (tab-separated)",
//            "* regel 3: formats.",
//            "* regel 4: missing value(s) (comma-separated indien van toepassing).",
//            "* regel 5. meetniveaus",
//            "* regel 6: value labels 'value'='label' (comma-separated indien van toepassing).",
//            "begin program.",
//            "import os, codecs",
//            "import spss, spssaux",
//            "",
//            "# scheidingstekens voor metadata resp. regels",
//            "SEP, LINESEP = \"\\t\", os.linesep",
//            "",
//            "# gebruikte encoding afleiden.",
//            "codepage, utf8mode = map(spss.GetSetting, [\"locale\", \"unicode\"]) ",
//            "codepage = codepage.split(\".\")[-1]",
//            "encoding = \"utf-8\" if utf8mode == \"Yes\" else codepage",
//            "\n",
//            "# valuelabels: <SEP>-gescheiden tussen vars, comma-gescheiden binnen vars",
//            "# value en label gescheiden door een = teken en zijn single-quoted ivm",
//            "# eventuele embedded =-tekens", "vardict = spssaux.VariableDict()",
//            "varValueLabels = []", "for v in vardict:", "    valuelabels = []",
//            "    for key, var in v.ValueLabels.items():",
//            "        valuelabels.append(\"%r=%r\" % (key, var))",
//            "    varValueLabels.append(\", \".join(valuelabels))",
//            "varValueLabels = SEP.join(varValueLabels)", "",
//            "# schrijf het bestand weg",
//            "with codecs.open(R\"C:\\\\Users\\\\Gebruiker\\\\Desktop\\\\metadata.txt\", \"wb\", ",
//            "                          encoding=encoding) as outfile:",
//            "    outfile.write(encoding + LINESEP)",
//            "    outfile.write(SEP.join([v.VariableName for v in vardict]) + LINESEP)",
//            "    outfile.write(SEP.join([v.VariableFormat for v in vardict]) + LINESEP)",
//            "    outfile.write(SEP.join([v.MissingValues for v in vardict]) + LINESEP)",
//            "    outfile.write(SEP.join([v.VariableLevel for v in vardict]) + LINESEP)",
//            //"    outfile.write(varValueLabels + LINESEP)", 
//            "end program."
//        };
//        return command;
//
//    }

    /**
     * Generates metadata for the free with metadata file-type.
     *
     * @param metadata MetadataMu instance containing the metadata.
     * @param defaultFieldLength Integer containing the default lenght of a
     * field.
     * @param defaultMissing String containing the default missing value.
     */
    public void generateFromHeader(MetadataMu metadata, int defaultFieldLength, String defaultMissing) {
        try {
            String[] fieldnames = MetaReader.readHeader(metadata.getFileNames().getDataFileName(),
                    metadata.getSeparator());
            metadata.getVariables().clear();
            for (String fieldname : fieldnames) {
                VariableMu variable = new VariableMu(fieldname);
                variable.setVariableLength(defaultFieldLength);
                if (defaultMissing.length() > 0) {
                    variable.setMissing(0, defaultMissing);
                }
                metadata.getVariables().add(variable);
            }
        } catch (ArgusException ex) {
            getView().showErrorMessage(ex);
        }
    }

    /**
     * Checks whether tables are specified.
     *
     * @return Boolean indicating whether tables are specified.
     *
     */
    private boolean areTablesSpecified() {
        Combinations combinations = getModel().getCombinations();
        return (combinations != null && combinations.getTables().size() > 0);
    }

    /**
     * Actions performed when the Ok button is pressed. If changes have been
     * made the new metadata will be verified, it will be checked whether the
     * changes are significant and the user is asked if he/she wants to safe the
     * new metadata..
     */
    public void ok() {
        // verify the metadata if changes have been made.
        if (!getModel().equals(this.metadataClone)) {
            try {
                this.metadataClone.verify();
            } catch (ArgusException ex) {
                getView().showErrorMessage(ex);
                return;
            }

            /* check if the changes are significant. If they are significant 
             ask the user if he/she wishes to continue. */
            String message;
            boolean significantDifference = areTablesSpecified() && getModel().significantDifference(this.metadataClone);
            if (significantDifference) {
                message = "";
                if (!getView().showConfirmDialog("Changing the Metadata will result in losing already specified tables.\n"
                        + "Do you wish to continue?")) {
                    return;
                }
            } else {
                this.metadataClone.setCombinations(getModel().getCombinations());
                message = "Metadata has been changed. ";
            }

            // set the metadata and ask if the user want to save the metadata.
            setModel(this.metadataClone);
            if (getView().showConfirmDialog(message + "Save changes to file?")) {
                String filePath = getView().showFileDialog("Save ARGUS metadata", true, new String[]{"ARGUS metadata file (*.rda)|rda"});
                if (filePath != null) {
                    try {
                        MetaWriter.writeRda(filePath, getModel(), true);
                    } catch (ArgusException ex) {
                        getView().showErrorMessage(ex);
                    }
                }
            }
        }
        this.getView().setVisible(false);
    }

    /**
     * Gets the metadata.
     *
     * @return MetadataMu containing the metadata.
     */
    public MetadataMu getMetadata() {
        return getModel();
    }

    /**
     * Cancels all changes made and closes the specifyMetadata screen after user
     * confirmation.
     */
    public void cancel() {
        if (!getMetadata().equals(this.metadataClone)) {
            if (!getView().showConfirmDialog("All changes will be discarded. Are you sure?")) {
                return;
            }
        }
        getView().setVisible(false);
    }

}
