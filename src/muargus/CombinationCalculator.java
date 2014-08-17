/*
 * This is a testing class for trying out making combinations and needs to be removed after testing
 */
package muargus;

import java.util.ArrayList;
import muargus.model.TableMu;
import muargus.model.VariableMu;

/**
 *
 * @author ambargus
 */
public class CombinationCalculator {
    private String [] names = new String[]{"a", "b", "c", "d", "e" };
    private ArrayList<TableMu> tables = new ArrayList<>();
    private int dimensions = 3;
    private ArrayList<VariableMu> data = new ArrayList<>();

    public CombinationCalculator() {
        for(int i = 0; i < 5; i++){
            VariableMu v = new VariableMu(names[i]);
            data.add(v);
        }
    }
   
    public void list(ArrayList<VariableMu> data){
        ArrayList<VariableMu> variableSubset = new ArrayList<>();
        list(0 , data, dimensions, variableSubset);
    }
    
    public void list(int startPos, ArrayList<VariableMu> allVariables, int dimension, ArrayList<VariableMu> variableSubset){
        if(dimension >= 0){
            for(int i = startPos; i< allVariables.size(); i++){
                //make variable array 
                ArrayList<VariableMu> temp = new ArrayList<>();
                VariableMu s = allVariables.get(i);
                temp.addAll(variableSubset);
                temp.add(s);
                
                //Make table, add the variable array and add this table to the table array
                TableMu table = new TableMu();
                table.setVariables(temp);
                tables.add(table);
                
                int d = dimension - 1;
                list(i+1, allVariables, d, temp);
            }
        }
    }
    
    public void print(){
        for(TableMu t: tables){
            for(VariableMu v: t.getVariables()){
                System.out.print(v.getName() + " ");
            }
            System.out.println("");
        }
        System.out.println(tables.size());
    }
    

    public ArrayList<VariableMu> getList() {
        return data;
    }

  
    public static void main (String[] args){
        CombinationCalculator c = new CombinationCalculator();
        c.list(c.getList());
        c.print();
        
    }
}
