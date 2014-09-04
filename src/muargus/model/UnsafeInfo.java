///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package muargus.model;
//
//import java.util.ArrayList;
//
///**
// *
// * @author pibd05
// */
//public class UnsafeInfo {
//    
//    
//            
//    public UnsafeInfo() {
//        
//    }
//    
//    public void clearCodeInfos() {
//        this.codeInfos = new ArrayList<>();
//    }
//    
//    public void addCodeInfo(CodeInfo info) {
//            this.codeInfos.add(info);
//    }
//    
//    public ArrayList<CodeInfo> getCodeInfos() {
//        return this.codeInfos;
//    }
//        
//    public void setUnsafeCombinations(int count, int[] unsafe) {
//        this.unsafeCombinations = new int[count];
//        System.arraycopy(unsafe, 0, this.unsafeCombinations, 0, count);
//    }
//    
//    public Object[] toObjectArray(VariableMu variable, int maxDims) {
//        Object[] objArr = new Object[maxDims + 1];
//        objArr[0] = variable.getName();
//        for (int dimNr=1; dimNr <= maxDims; dimNr++) {
//            objArr[dimNr] = this.unsafeCombinations.length < dimNr ?
//                    "-" : Integer.toString(this.unsafeCombinations[dimNr-1]);
//        }
//        return objArr;
//    }
//    
//}
// 