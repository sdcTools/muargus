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

    private String[] names = new String[]{"a", "b", "c", "d", "e", "f", "g"};
    private ArrayList<TableMu> tables = new ArrayList<>();
    private ArrayList<String[]> namesAndID = new ArrayList<>();
    private int dimensions = 3;
    private ArrayList<VariableMu> data = new ArrayList<>();
    private int id_1 = 3;
    private int id_2 = 2;
    private int id_3 = 2;
    private int[] idLevels = new int[6];

    public CombinationCalculator() {
        idLevels[1] = 3;
        idLevels[2] = 2;
        idLevels[3] = 2;


        //namesAndID.addAll(toArray(names[]));
        String id;
        for (int i = 0; i < names.length; i++) {
            if (i < id_1) {
                id = "1";
            } else if (i < id_1 + id_2) {
                id = "2";
            } else {
                id = "3";
            }

            String[] temp = {names[i], id};
            namesAndID.add(temp);
        }

//        for (String[] s : namesAndID) {
//            System.out.printf("%s  %s\n", s[0], s[1]);
//        }

//        for(int i = 0; i < 5; i++){
//            VariableMu v = new VariableMu(names[i]);
//            data.add(v);
//        }
    }

    public void calculate() {
        int index = 0;
        int _size = 0;
        for (int i = 0; i < idLevels.length; i++) {
            if (idLevels[i] > 0) {
                index = i;
                _size = idLevels[i];
                break;
            }
        }
        calculate(0, index, _size, "");
    }

    public void calculate(int _i, int _index, int _size, String _s) {
//        if (idLevels[_index] == 0) {
//            //calculate(_i, _index++, _size, _s);
//        } else {
        
        //int size = _size;
        int index = _index;
        for (int i = _i; i < _size; i++) {
            String s1 = _s + namesAndID.get(i)[0];
            if(s1.length() == 3){
                System.out.println(s1);
            }
            int size1 = _size + idLevels[index+1];
            //index = index + 1;
            for (int j = i+1; j < size1; j++) {
                String s2 = s1 + namesAndID.get(j)[0];
                if(s2.length() == 3){
                    System.out.println(s2);
                }
                int size2 = size1 + idLevels[index+2];
                //index = index + 1;
                for (int k = j+1; k < size2; k++) {
                    String s3 = s2 + namesAndID.get(k)[0];
                    if(s3.length() == 3){
                        System.out.println(s3);
                    }
//                    size = size + idLevels[index+3];
//                    index = index + 1;
                }
            }
        }
    }


//            calculate(i+1, index, size, s);
//
//
////                for (int j = i + 1; j < id_1 + id_2; j++) {
////                    for (int k = j + 1; k < id_1 + id_2 + id_3; k++) {
////                        System.out.printf("%s %s %s\n", namesAndID.get(i)[0], namesAndID.get(j)[0], namesAndID.get(k)[0]);
////                    }
////                }
//            }
       // }
//        int idLevel = 1;
//        int variableIndex = 0;
//        
//        while(namesAndID.get(variableIndex)[1].equals(Integer.toString(idLevel))){
//            
//        }
//        for (int i = 0; i < id_1.size(); i++) {
//            TableMu table = new TableMu();
//            VariableMu variable = new VariableMu();
////        }
//    }

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
        c.calculate();
//        
    }

    private Collection<? extends String> toArray(String[] names) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
