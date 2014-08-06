/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import muargus.model.Variables;

class TestModel {

private DefaultListModel lm_;
private ArrayList<Variables> array;
private Variables variable;

  public TestModel() {
    lm_ = new DefaultListModel();

    array = new ArrayList<>();
    //String[] testList = new String[] {"user1", "user2"};
    variable = new Variables("user1");
    array.add(variable);
    variable = new Variables("user2");
    array.add(variable);
    
    
    for(int i = 0; i< array.size(); i++){
        
        lm_.add(i, array.get(i));
    }
    

//    for(int i=0; i < testList.length; i++) {
//      lm_.add(i, testList[i]);
//    }
  }

  public ListModel getListModel() {
    return lm_;
  }
  
  public void add(int i, String s){
      variable = new Variables(s);
      array.add(i, variable);
      lm_.add(i, variable);
  }
  
  public void remove(int i){
      lm_.remove(i);
      array.remove(i);
  }
  
  public void moveUp(int i){
      if(i>0){
        Variables temp = (Variables) lm_.get(i-1);
        lm_.remove(i-1);
        lm_.add(i, temp);
      }
  }
  
  public void moveDown(int i){
     if(i<lm_.size()-1){
        Variables temp = (Variables) lm_.get(i);
        lm_.remove(i);
        lm_.add(i+1, temp);
      } 
  }
}

