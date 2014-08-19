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

    private String[] names = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private ArrayList<TableMu> tables = new ArrayList<>();
    //private ArrayList<String[]> namesAndID = new ArrayList<>();
    //private int dimensions = 3;
    private ArrayList<VariableMu> data = new ArrayList<>();
    private int[] idLevels = new int[6];
    private int numberOfLevels = 0;

    public CombinationCalculator() {
        idLevels[0] = 0;
        idLevels[1] = 1;
        idLevels[2] = 0;
        idLevels[3] = 0;
        idLevels[4] = 1;
        idLevels[5] = 0;
        

        for (int i : idLevels) {
            if (i > 0) {
                numberOfLevels++;
            }
        }
        
        for(String s: names){
            VariableMu v = new VariableMu(s);
            data.add(v);
        }
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    
    public void calculate(int numberOfLevels) {
        int index = 1; // don't add the variables with an ID number of 0
        int _size = 0;
        int currentLevel = 0;
        ArrayList<VariableMu> variableSubset = new ArrayList<>();
        
        calculate(0, index, _size, "", currentLevel, variableSubset, numberOfLevels);
    }
    
    public void calculate(int _i, int _index, int _size, String _s, int _currentLevel,ArrayList<VariableMu> variableSubset, int _numberOfLevels) {
        int currentLevel = _currentLevel + 1;
        if (currentLevel <= _numberOfLevels) {
            int index = _index;
            int size = _size;
            
            // find the next idLevel larger than zero and add the number of variables to the size
            for (int u = index; u < idLevels.length; u++) {
                if (idLevels[u] > 0) {
                    size = size + idLevels[u];
                    index = u+1;
                    break;
                }
            }
            
            for (int i = _i; i < size; i++) {
                ArrayList<VariableMu> temp = new ArrayList<>();
                temp.addAll(variableSubset);
                temp.add(data.get(i));
                        
                if (temp.size() == _numberOfLevels) {
                    TableMu tableMu = new TableMu();
                    tableMu.setVariables(temp);
                    tables.add(tableMu);
                }
                calculate(i+1, index, size, _s, currentLevel, temp, _numberOfLevels);
            }
        }
    }


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
        c.calculate(c.getNumberOfLevels());
        c.print();
    }
}
