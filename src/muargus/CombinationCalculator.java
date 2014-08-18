/*
 * This is a testing class for trying out making combinations and needs to be removed after testing
 */
package muargus;

import java.util.ArrayList;
import java.util.Collection;
import muargus.model.TableMu;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class CombinationCalculator {

    private String[] names = new String[]{"a", "b", "c", "d", "e", "f"};
    private ArrayList<TableMu> tables = new ArrayList<>();
    private ArrayList<String[]> namesAndID = new ArrayList<>();
    private int dimensions = 3;
    private ArrayList<VariableMu> data = new ArrayList<>();

    public CombinationCalculator() {
        //namesAndID.addAll(toArray(names[]));
        for (int i = 0; i < names.length / 2; i++) {
            String[] temp1 = {names[2 * i], Integer.toString(i + 1)};
            String[] temp2 = {names[2 * i + 1], Integer.toString(i + 1)};
            namesAndID.add(temp1);
            namesAndID.add(temp2);
        }

        for (String[] s : namesAndID) {
            System.out.printf("%s  %s\n", s[0], s[1]);
        }
        long something = 182*2*60*4;
        System.out.println(something);

//        for(int i = 0; i < 5; i++){
//            VariableMu v = new VariableMu(names[i]);
//            data.add(v);
//        }
    }

    public void calculate() {
        int idLevel = 1;
        int variableIndex = 0;
        
        while(namesAndID.get(variableIndex)[1].equals(Integer.toString(idLevel))){
            
        }
//        for (int i = 0; i < id_1.size(); i++) {
//            TableMu table = new TableMu();
//            VariableMu variable = new VariableMu();
//        }
    }

//    public void list(ArrayList<VariableMu> data){
//        ArrayList<VariableMu> variableSubset = new ArrayList<>();
//        list(0 , data, dimensions, variableSubset);
//    }
//    
//    public void list(int startPos, ArrayList<VariableMu> allVariables, int dimension, ArrayList<VariableMu> variableSubset){
//        if(dimension >= 0){
//            for(int i = startPos; i< allVariables.size(); i++){
//                //make variable array 
//                ArrayList<VariableMu> temp = new ArrayList<>();
//                VariableMu s = allVariables.get(i);
//                temp.addAll(variableSubset);
//                temp.add(s);
//                
//                //Make table, add the variable array and add this table to the table array
//                TableMu table = new TableMu();
//                table.setVariables(temp);
//                tables.add(table);
//                
//                int d = dimension - 1;
//                list(i+1, allVariables, d, temp);
//            }
//        }
//    }
    public void print() {
        for (TableMu t : tables) {
            for (VariableMu v : t.getVariables()) {
                System.out.print(v.getName() + " ");
            }
            System.out.println("");
        }
        System.out.println(tables.size());
    }

    public ArrayList<VariableMu> getList() {
        return data;
    }

    public static void main(String[] args) {
        CombinationCalculator c = new CombinationCalculator();
//        c.list(c.getList());
//        c.print();
//        
    }

    private Collection<? extends String> toArray(String[] names) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
