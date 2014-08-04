/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import argus.model.ArgusException;
import argus.model.Variable;
import argus.utils.Tokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import muargus.model.SpecifyMetadataModel;
import muargus.model.Variables;

/**
 *
 * @author ambargus
 */
public class Test {
    
    

    private static boolean spss = false;
    private static boolean free = false;
    private static boolean fixed = true;
    
    private static String separator = "";
    private String metaFile = "C:\\Program Files\\MU_ARGUS\\data\\Demofree.rda";
    private String dataFile = "C:\\Program Files\\MU_ARGUS\\data\\Demofree.asc";
    private BufferedReader reader;
    private Tokenizer tokenizer;
    private Variables variables;
    private ArrayList<Variables> data;
    
    public Test() {
        data = new ArrayList<>();
 
    }
    
    public void checkType(String type){
        
    }
    
    /**
     * Reads the entire data of the metafile
     * This method reads the metadatafile line for line. It makes a new variable
     * object for each variable it finds and provides the relevant information
     * of this variable (is it recodable, what is it's ID_level etc).
     * @param metadata the file name/path 
     */
    public void readMetadata(String metadata){
        try { 
            reader = new BufferedReader(new FileReader(new File(metaFile)));
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tokenizer = new Tokenizer(reader);
        
        while (tokenizer.nextLine() != null) {
            tokenizer.nextToken();
   
            if( !tokenizer.getValue().substring(0, 1).equals("<")){   
                variables = new Variables();
                variables.setName(tokenizer.getValue());
                data.add(variables);
                if(isFixed()){
                    variables.setbPos(Integer.parseInt(tokenizer.nextToken()));
                }
                variables.setVarLen(Integer.parseInt(tokenizer.nextToken()));
                variables.setMissing(0, tokenizer.nextToken());
                variables.setMissing(1, tokenizer.nextToken());
            } else { 
                switch(tokenizer.getValue()){
                    case "<SEPARATOR>":
                        setFree(true);
                        if(!tokenizer.nextToken().equals(""))
                            setSeparator(tokenizer.getValue());
                        break;
                    case "<SPSS>":
                        setSpss(true);
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
                        variables.setIdLevenl(tokenizer.nextToken());
                        break;
                    case "<TRUNCABLE>":
                        variables.setTruncable(true);
                        break;
                    case "<NUMERIC>":
                        variables.setNumeric(true);
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
                
            
            /*
             * <SEPARATOR>
             * <NAMESINFRONT>
             * <SPSS>
             */
                
        }
//        try {
//            for (Variables v : data) {
//                System.out.println("Variable name: " + v.getName());
//                System.out.println("\tRecodable is " + v.isRecodable());
//                System.out.println("\tCodelist is "+ v.isCodelist() + " "+ v.getCodeListFile());
//                System.out.println("\tIDLevel is " + v.getIdlevenl());
//                System.out.println("\tTruncable is " + v.isTruncable());
//                System.out.println("\tNumeric is " + v.isNumeric());
//                System.out.println("\tWeight is " + v.isWeight());
//                System.out.println("\tHouse_ID is " + v.isHouse_id());
//                System.out.println("\tHousehold is " + v.isHousehold());
//                System.out.println("\tSuppressWeight is " + v.getSuppressweight());
//                System.out.println("\tRelated is " + v.isRelated());
//            }
//        } catch (Exception e) {
//        }
        SpecifyMetadataModel.setVariables(data);
    }
    
    public String getMetadataFile(){
        return metaFile;
    }
    
    public static boolean isSpss() {
        return spss;
    }

    public void setSpss(boolean spss) {
        if(spss == true){
            Test.spss = spss;
            Test.free = !spss;
            Test.fixed = !spss;
        }
    }

    public static boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        if(free == true){
            Test.spss = !free;
            Test.free = free;
            Test.fixed = !free;
        }
    }

    public static boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        if(fixed == true){
            Test.spss = !fixed;
            Test.free = !fixed;
            Test.fixed = fixed;
        }
    }

    public static String getSeparator() {
        return separator;
    }

    public static void setSeparator(String separator) {
        Test.separator = separator;
    }

    
    
  
    
    
//    public static void main (String[] args) throws ArgusException{
//        Test t = new Test();
//        t.readMetadata(t.getMetadataFile());
//    }
    
}
