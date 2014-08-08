/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.StrUtils;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ambargus
 */
public class MetadataMu {

    private static final Logger logger = Logger.getLogger(MetadataMu.class.getName());

    // file types 
    public static final int DATA_FILE_TYPE_FIXED = 1;
    public static final int DATA_FILE_TYPE_FREE = 2;
    public static final int DATA_FILE_TYPE_FREE_WITH_META = 3;
    public static final int DATA_FILE_TYPE_SPSS = 4;

    //default
    private int dataFileType = DATA_FILE_TYPE_FIXED;

    //private static boolean fixed = true;
    //private static boolean free = false;
    //private static boolean freeWithMeta = false;
    //private static boolean spss = false;
    // default value
    private String separator = ";";

    // TODO: remove this default/test metadatafile name
    private String defaultmetaFile = "C:\\Program Files\\MU_ARGUS\\data\\Demodata.rda\\";

    private BufferedReader reader;
    private Tokenizer tokenizer;
    private ArrayList<VariableMu> variables;
    //private static ArrayList<Variables> cloneData;
    private DataFilePair filenames;
    //private String metadataFile;

    public MetadataMu() {
        variables = new ArrayList<>();
        filenames = new DataFilePair(null, null);
    }
    
    public MetadataMu(MetadataMu metadata) {
        this();    

        this.dataFileType = metadata.dataFileType;
        this.filenames = new DataFilePair(
                metadata.filenames.getDataFileName(),  metadata.filenames.getMetaFileName());
        this.separator = metadata.separator;
        for (VariableMu var : metadata.variables) {
            this.variables.add(new VariableMu(var));
        }
        try {
            linkRelatedVariables();
        }
        catch (ArgusException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
            

//    public static ArrayList<VariableMu> makeClone(ArrayList<VariableMu> list) throws CloneNotSupportedException {
//        ArrayList<VariableMu> clone = new ArrayList<>(list.size());
//        for (VariableMu item : list) {
//            clone.add((VariableMu) item.clone());
//        }
//        return clone;
//    }

//    public static ArrayList<Variables> getClone(){
//        return cloneData;
//    }
//    public void TestClone(){
//        cloneData.get(1).setName("test");
//        System.out.println(variables.get(1).getName());
//        System.out.println(cloneData.get(1).getName());
//    }
    /**
     * Reads the entire variables of the metafile This method reads the
     * metadatafile line for line. It makes a new variable object for each
     * variable it finds and provides the relevant information of this variable
     * (is it recodable, what is it's ID_level etc).
     *
     * @param metadata the file name/path
     * @throws ArgusException when verify() of the read model fails
     */
    public void readMetadata() throws ArgusException {
        dataFileType = DATA_FILE_TYPE_FIXED;
        VariableMu variable = null;

        try {
            reader = new BufferedReader(new FileReader(new File(this.filenames.getMetaFileName())));
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            logger.log(Level.SEVERE, null, ex);
        }

        tokenizer = new Tokenizer(reader);
        while (tokenizer.nextLine() != null) {
            String value = tokenizer.nextToken();

            if (!value.substring(0, 1).equals("<")) {
                variable = new VariableMu();
                variable.setName(tokenizer.getValue());
                variables.add(variable);
                if (getDataFileType() == DATA_FILE_TYPE_FIXED) {
                    variable.setStartingPosition(tokenizer.nextToken());
                }
                variable.setVariableLength(tokenizer.nextToken());

                // make fancier
                variable.setMissing(0, tokenizer.nextToken());
                variable.setMissing(1, tokenizer.nextToken());
            } else if (variable == null) {
                switch (value) {
                    case "<SEPARATOR>":
                        setDataFileType(DATA_FILE_TYPE_FREE);
                        //setFree(true);
                        //if(!tokenizer.nextToken().equals(""))
                        setSeparator(tokenizer.nextToken());
                        break;
                    case "<SPSS>":
                        setDataFileType(DATA_FILE_TYPE_SPSS);
                        //setSpss(true);
                        break;
                    case "<NAMESINFRONT>":
                        setDataFileType(DATA_FILE_TYPE_FREE_WITH_META);
                        //setFreeWithMeta(true);
                        break;
                }
            } else {
                switch (value) {
                    case "<RECODEABLE>":
                    case "<RECODABLE>":
                        variable.setRecodable(true);
                        break;
                    case "<CODELIST>":
                        variable.setCodelist(true);
                        variable.setCodeListFile(tokenizer.nextToken());
                        break;
                    case "<IDLEVEL>":
                        variable.setIdLevel(tokenizer.nextToken());
                        break;
                    case "<TRUNCABLE>":
                        variable.setTruncable(true);
                        break;
                    case "<NUMERIC>":
                        variable.setNumeric(true);
                        variable.setCategorical(false);
                        break;
                    case "<DECIMALS>":
                        variable.setDecimals(tokenizer.nextToken());
                        break;
                    case "<WEIGHT>":
                        variable.setWeight(true);
                        break;
                    case "<HOUSE_ID>":
                        variable.setHouse_id(true);
                        break;
                    case "<HOUSEHOLD>":
                        variable.setHousehold(true);
                        break;
                    case "<SUPPRESSWEIGHT>":
                        variable.setSuppressweight(tokenizer.nextToken());
                        break;
                    case "<RELATED>":
                        variable.setRelatedVariableName(tokenizer.nextToken());
                        break;
                    default:
                        break;
                }
            }
        }
        tokenizer.close();

        linkRelatedVariables();
        verify();
    }

    private void writeVariable(Writer w, VariableMu variable) {
    
    }
    private void write(Writer w) throws IOException, ArgusException {
// Anco 1.6
// try with resources verwijderd.        
//        try (PrintWriter writer = new PrintWriter(w)) {
        PrintWriter writer = null;
        try {  writer = new PrintWriter(w);
            if (dataFileType == DATA_FILE_TYPE_FREE) {
                writer.println("   <SEPARATOR> " + StrUtils.quote(separator));
            }
            if (dataFileType == DATA_FILE_TYPE_SPSS) {
                writer.println("   <SPSS>");
            }
//            if (dataOrigin != DATA_ORIGIN_MICRO) {
//                writer.println("   <SAFE> " + StrUtils.quote(safeStatus));
//                writer.println("   <UNSAFE> " + StrUtils.quote(unSafeStatus));
//                writer.println("   <PROTECT> " + StrUtils.quote(protectStatus));
//            }
            for (VariableMu variable : this.variables) {
                variable.write(writer, this.dataFileType);
            }
        }
        finally {
            if (writer != null) 
                writer.close();
        }
    }

    public void write(File file) {
        try {
            write(new BufferedWriter(new FileWriter(file)));
            this.filenames = new DataFilePair(this.filenames.getDataFileName(), file.getPath());
// anco 1            
//        } catch (ArgusException | IOException ex) {
          } catch (ArgusException ex) {
            logger.log(Level.SEVERE, null, ex);
          } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    private void linkRelatedVariables() throws ArgusException{
        for (VariableMu var : variables) {
            var.linkRelatedVariable(variables);
        }
            
    }
    
    
    // TODO: add a message explaining whats the problem
    public void verify() throws ArgusException {
        for (VariableMu var : variables) {
            
            //Check for duplicate variable names
            for (VariableMu var2 : variables) {
                if (!var.equals(var2) && var.getName().equalsIgnoreCase(var2.getName())) {
                    throw new ArgusException("Duplicate variable name: " + var.getName());
                }
            }
            
            //Fixed file checks
            if (dataFileType == DATA_FILE_TYPE_FIXED) {
                int length = var.getVariableLength();

                //Check if variableLength > 0
                if (length <= 0)
                    throw new ArgusException("Length of variable " + var.getName() + " must be > 0");
                
                //Check for overlap of variables in fixed file
                int start = var.getStartingPosition();
                int end = start + length;
                for (VariableMu var2 : variables) {
                    int start2 = var2.getStartingPosition();
                    int end2 = start2 + var2.getVariableLength();
                    if (!var.equals(var2) && start2 < end && start < end2)
                        throw new ArgusException("Variables " + var.getName() + " and " + var2.getName() + " overlap");
                }
                         
                //Check if length of missings is correct
                for (int index=0; index < VariableMu.MAX_NUMBER_OF_MISSINGS; index++) {
                    String missing = var.getMissing(index);
                    if (missing != null && missing.length() > length)
                        throw new ArgusException("Missing value for variable " + var.getName() + " too long");
                }
                    
            }
            
                 
        }
        
//        for (int i = 0; i < variables.size(); i++) {
//            VariableMu variable = variables.get(i);
//            if (dataFileType == DATA_FILE_TYPE_FIXED) {
//                int b1 = variable.getStartingPosition();
//                int e1 = b1 + variable.getVariableLength();
//                for (int j = i + 1; j < variables.size(); j++) {
//                    VariableMu variable2 = variables.get(j);
//                    int b2 = variable2.getStartingPosition();
//                    int e2 = b2 + variable2.getVariableLength();
//                    if (b2 < e1 && e2 > b1) {
//                        throw new ArgusException("Variable " + variable.getName() + " and variable " + variable2.getName() + " overlap.");
//                    }
//                }
//                if (StringUtils.equalsIgnoreCase(variable.getName(), variable2.getName())) {
//                    throw new ArgusException("Variable" + i + " and variable" + j + " have the same name.");
//                }
//            }
//        }
    }

//    public void setMetadataFile(String metaFile){
//        this.metadataFile = metaFile;
//    }
//    
//    public String getMetadataFile(){      
//        if (filenames.getMetaFileName() != null){
//            metadataFile = getFileNames().getMetaFileName();
//        } else if(metadataFile == null || metadataFile.equals(defaultmetaFile)){ 
//            metadataFile = defaultmetaFile;
//        }
//        return metadataFile;
//    }
    public int getDataFileType() {
        return this.dataFileType;
    }

    public void setDataFileType(int dataFileType) {
        this.dataFileType = dataFileType;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public DataFilePair getFileNames() {
        return this.filenames;
    }

    public void setFileNames(DataFilePair filenames) {
        this.filenames = filenames;
    }

    // TODO: remove after testing
    public void testPrintAll() {
        try {
            for (VariableMu v : variables) {
                System.out.println("Variable name: " + v.getName());
                System.out.println("\tRecodable is " + v.isRecodable());
                System.out.println("\tCodelist is " + v.isCodelist() + " " + v.getCodeListFile());
                System.out.println("\tIDLevel is " + v.getIdLevel());
                System.out.println("\tTruncable is " + v.isTruncable());
                System.out.println("\tNumeric is " + v.isNumeric());
                System.out.println("\tWeight is " + v.isWeight());
                System.out.println("\tHouse_ID is " + v.isHouse_id());
                System.out.println("\tHousehold is " + v.isHousehold());
                System.out.println("\tSuppressWeight is " + v.getSuppressweight());
                //System.out.println("\tRelatedVariable is " + v.getRelatedVariable().getName());
            }
        } catch (Exception e) {
        }
    }

    public ArrayList<VariableMu> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<VariableMu> variables) {
        this.variables = variables;
    }



    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.separator);
        hash = 41 * hash + this.dataFileType;
        hash = 41 * hash + Objects.hashCode(this.filenames.getDataFileName());
        hash = 41 * hash + Objects.hashCode(this.filenames.getMetaFileName());
        hash = 41 * hash + Objects.hashCode(this.variables);
        return hash;
    }

    

//    public static void main (String[] args) throws ArgusException{
//        MetadataMu t = new MetadataMu();
//        t.readMetadata(t.getMetadataFile());
//    }
   

    @Override
    public boolean equals(Object o) {
        MetadataMu cmp = (MetadataMu)o;
        if (!this.separator.equals(cmp.separator))
            return false;
        if (this.dataFileType != cmp.dataFileType)
            return false;
        if (this.filenames.getDataFileName() == null ? cmp.filenames.getDataFileName() != null : 
                !this.filenames.getDataFileName().equals(cmp.filenames.getDataFileName())) {
            return false;
        }
        if (this.filenames.getMetaFileName() == null ? cmp.filenames.getMetaFileName() != null : 
                !this.filenames.getMetaFileName().equals(cmp.filenames.getMetaFileName())) {
            return false;
        }
        return this.variables.equals(cmp.variables);
    }
}
