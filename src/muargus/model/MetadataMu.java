/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import argus.model.ArgusException;
import argus.model.DataFilePair;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author ambargus
 */
public class MetadataMu implements Cloneable{
   
    private static final Logger logger = Logger.getLogger(MetadataMu.class.getName());
    
    // format 
    public static final int DATA_FILE_TYPE_FIXED = 1;
    public static final int DATA_FILE_TYPE_FREE = 2;
    public static final int DATA_FILE_TYPE_FREE_WITH_META = 3;
    public static final int DATA_FILE_TYPE_SPSS = 4;
    
    private static int dataFileType = DATA_FILE_TYPE_FIXED;
    
    private static boolean fixed = true;
    private static boolean free = false;
    private static boolean freeWithMeta = false;
    private static boolean spss = false;
    
    // default value
    private static String separator = ";";
    
    // TODO: remove this default/test metadatafile name
    private static String defaultmetaFile = "C:\\Program Files\\MU_ARGUS\\data\\Demodata.rda\\";
    
    
    private static String metadataFile = defaultmetaFile;
    private BufferedReader reader;
    private Tokenizer tokenizer;
    private Variables variables;
    private ArrayList<Variables> data;
    private static ArrayList<Variables> cloneData;
    private DataFilePair filenames;
    
    public MetadataMu() {
        data = new ArrayList<>();
        filenames = new DataFilePair(null, null);
    }
    
    public static ArrayList<Variables> makeClone(ArrayList<Variables> list) throws CloneNotSupportedException{
        ArrayList<Variables> clone = new ArrayList<>(list.size());
        for(Variables item: list){
            clone.add((Variables) item.clone());
        }
        return clone;
    }
    
    public static ArrayList<Variables> getClone(){
        return cloneData;
    }
    
    public void TestClone(){
        cloneData.get(1).setName("test");
        System.out.println(data.get(1).getName());
        System.out.println(cloneData.get(1).getName());
    }

    
    
    
    /**
     * Reads the entire data of the metafile
     * This method reads the metadatafile line for line. It makes a new variable
     * object for each variable it finds and provides the relevant information
     * of this variable (is it recodable, what is it's ID_level etc).
     * @param metadata the file name/path 
     */
    public void readMetadata(String metadata) throws ArgusException{
        dataFileType = DATA_FILE_TYPE_FIXED;
        try { 
            reader = new BufferedReader(new FileReader(new File(getMetadataFile())));
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            Logger.getLogger(MetadataMu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tokenizer = new Tokenizer(reader);
        
        while (tokenizer.nextLine() != null) {
            tokenizer.nextToken();
   
            if( !tokenizer.getValue().substring(0, 1).equals("<")){   
                variables = new Variables();
                variables.setName(tokenizer.getValue());
                data.add(variables);
                if(getDataFileType()== DATA_FILE_TYPE_FIXED){
                    variables.setStartingPosition(tokenizer.nextToken());
                }
                variables.setVariableLength(tokenizer.nextToken());
                
                // make fancier
                variables.setMissing(0, tokenizer.nextToken());
                variables.setMissing(1, tokenizer.nextToken());
            } else { 
                switch(tokenizer.getValue()){
                    case "<SEPARATOR>":
                        setDataFileType(DATA_FILE_TYPE_FREE);
                        //setFree(true);
                        if(!tokenizer.nextToken().equals(""))
                            setSeparator(tokenizer.getValue());
                        break;
                    case "<SPSS>":
                        setDataFileType(DATA_FILE_TYPE_SPSS);
                        //setSpss(true);
                        break;
                    case "<NAMESINFRONT>":
                        setDataFileType(DATA_FILE_TYPE_FREE_WITH_META);
                        //setFreeWithMeta(true);
                        break;
                    case "<RECODEABLE>":
                    case "<RECODABLE>":
                        variables.setRecodable(true);
                        break;
                    case "<CODELIST>":
                        variables.setCodelist(true);
                        variables.setCodeListFile(tokenizer.nextToken());
                        break;
                    case "<IDLEVEL>":
                        variables.setIdLevel(tokenizer.nextToken());
                        break;
                    case "<TRUNCABLE>":
                        variables.setTruncable(true);
                        break;
                    case "<NUMERIC>":
                        variables.setNumeric(true);
                        variables.setCategorical(false);
                        break;
                    case "<DECIMALS>":
                        variables.setDecimals(tokenizer.nextToken());
                        break;
                    case "<WEIGHT>":
                        variables.setWeight(true);
                        break;
                    case "<HOUSE_ID>":
                        variables.setHouse_id(true);
                        break;
                    case "<HOUSEHOLD>":
                        variables.setHousehold(true);
                        break;
                    case "<SUPPRESSWEIGHT>":
                        variables.setSuppressweight(tokenizer.nextToken());
                        break;
                    case "<RELATED>":
                        variables.setRelated(true);
                        break;
                    default:
                        break;
                }       
            }             
        }  
        SpecifyMetadataModel.setVariables(data);
        try {
            cloneData = makeClone(data);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MetadataMu.class.getName()).log(Level.SEVERE, null, ex);
        }
//        TestClone();
        verify();
    }  
    
    
    // TODO: add a message explaining whats the problem
    public void verify() throws ArgusException {
        for (int i = 0; i < data.size(); i++) {
            Variables variable = data.get(i);
            if (dataFileType == DATA_FILE_TYPE_FIXED) {
                int b1 = variable.getStartingPosition();
                int e1 = b1 + variable.getVariableLength();
                for (int j = i + 1; j < data.size(); j++) {
                    Variables variable2 = data.get(j);
                    int b2 = variable2.getStartingPosition();
                    int e2 = b2 + variable2.getVariableLength();
                    if (b2 < e1 && e2 > b1) {
                        throw new ArgusException("Variable " + variable.getName() + " and variable " + variable2.getName() + " overlap.");
                    }
                    if (StringUtils.equalsIgnoreCase(variable.getName(), variable2.getName())) {
                        throw new ArgusException("Variable" + i + " and variable" + j + " have the same name.");
                    }
                }
            }
        }
    }

    
    public static void setMetadataFile(String metaFile){
        MetadataMu.metadataFile = metaFile;
    }
    
    public static String getMetadataFile(){
        if(OpenMicrodataModel.getMetadataPath() == null){
            return metadataFile;
        } else {
            metadataFile = OpenMicrodataModel.getMetadataPath();
        }
        return metadataFile;
    }

    public static int getDataFileType() {
        return dataFileType;
    }

    public static void setDataFileType(int dataFileType) {
        MetadataMu.dataFileType = dataFileType;
    }
    
    public static String getSeparator() {
        return separator;
    }

    public static void setSeparator(String separator) {
        MetadataMu.separator = separator;
    }
    
    public DataFilePair getFileNames() {
        return this.filenames;
    }

    public void setFileNames(DataFilePair filenames) {
        this.filenames = filenames;
    }

        public void testPrintAll(){
        try {
            for (Variables v : data) {
                System.out.println("Variable name: " + v.getName());
                System.out.println("\tRecodable is " + v.isRecodable());
                System.out.println("\tCodelist is "+ v.isCodelist() + " "+ v.getCodeListFile());
                System.out.println("\tIDLevel is " + v.getIdLevel());
                System.out.println("\tTruncable is " + v.isTruncable());
                System.out.println("\tNumeric is " + v.isNumeric());
                System.out.println("\tWeight is " + v.isWeight());
                System.out.println("\tHouse_ID is " + v.isHouse_id());
                System.out.println("\tHousehold is " + v.isHousehold());
                System.out.println("\tSuppressWeight is " + v.getSuppressweight());
                System.out.println("\tRelated is " + v.isRelated());
            }
        } catch (Exception e) {
        }
    }
        
    @Override
    public Object clone() throws CloneNotSupportedException {
        MetadataMu metadataMu = (MetadataMu)super.clone(); 

        metadataMu.data = (ArrayList<Variables>)((ArrayList<Variables>)data).clone();
        
        return metadataMu;
    }
    
  
    
    
//    public static void main (String[] args) throws ArgusException{
//        MetadataMu t = new MetadataMu();
//        t.readMetadata(t.getMetadataFile());
//    }
        
    
}
