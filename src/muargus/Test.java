/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import javax.swing.JList;

/**
 *
 * @author ambargus
 */
public class Test {

  public Test() {

    TestModel tm = new TestModel();

    JList list = new JList(tm.getListModel());
  }
  
  public static void main (String[] args){
      new Test();
  }
}

