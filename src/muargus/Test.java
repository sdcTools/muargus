/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import java.util.ArrayList;

/**
 *
 * @author pibd05
 */
public final class Test {

    private final int[] id = {0, 2, 2, 0, 0, 0};
    private final ArrayList<Integer> getallen = new ArrayList<>();
    private final ArrayList<Integer> cumulatief = new ArrayList<>();
    private int dimensions = 0;
    public int tabellen = 0;

    public Test() {
        setArrays();
    }

    private void setArrays() {
        for (int i = 1; i < this.id.length; i++) {
            if (this.id[i] > 0) {
                this.getallen.add(this.id[i]);
            }
        }

        for (int i = 0; i < this.getallen.size(); i++) {
            this.cumulatief.add(i > 0 ? this.getallen.get(i) + this.cumulatief.get(i - 1) - 1 : this.getallen.get(i));
        }
        this.dimensions = this.getallen.size();
    }

    public void printArray(ArrayList<Integer> list) {
        System.out.println(list.toString());
    }

    public ArrayList<Integer> getGetallen() {
        return getallen;
    }

    public ArrayList<Integer> getCumulatief() {
        return cumulatief;
    }

    public void calculate() {
        int[] aantal = new int[getallen.get(1)];
        for (int i = 1; i <= getallen.get(0); i++) {
            for (int j = i - 1; j < getallen.get(1); j++) { // gebruik 
                aantal[j] += getallen.get(0) - 1 + getallen.get(1);
            }
        }
        int som = 0;
        for (int i : aantal) {
            som += i;
        }
        System.out.println("aantal = " + som);

//        calculate(0, 0);
    }

    public void getNumberOfTables() {
        System.out.println(this.tabellen);
    }

//    private void calculate(int dimensions, int size) {
//        if (dimensions < this.dimensions) {
//            for (Integer allen : getallen) {
//                calculate(dimensions + 1, size + 1);
//                this.tabellen++;
//            }
//        }
//
//    }
    public static void main(String arg[]) {
        Test test = new Test();
        test.printArray(test.getGetallen());
        System.out.println("");
        test.printArray(test.getCumulatief());
        test.calculate();

    }

}
