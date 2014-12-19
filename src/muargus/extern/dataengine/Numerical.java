/*
 * Argus Open Source
 * Software to apply Statistical Disclosure Control techniques
 *
 * Copyright 2014 Statistics Netherlands
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the European Union Public Licence 
 * (EUPL) version 1.1, as published by the European Commission.
 *
 * You can find the text of the EUPL v1.1 on
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * This software is distributed on an "AS IS" basis without 
 * warranties or conditions of any kind, either express or implied.
 */
package muargus.extern.dataengine;

public class Numerical {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Numerical(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Numerical obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        NumericalDllJNI.delete_Numerical(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public Numerical() {
    this(NumericalDllJNI.new_Numerical(), true);
  }

  public void DoRankSwap(String inFileName, String outFileName, String separator, int numVar, int perc, int[] errorCode) {
    NumericalDllJNI.Numerical_DoRankSwap(swigCPtr, this, inFileName, outFileName, separator, numVar, perc, errorCode);
  }

  public void DoMicroAggregation(String inFileName, String outFileName, String separator, int nVar, int nMinRecPerGr, int nGroupsPerVar, int typeSolution, int[] nColumnsPerGr, int[] errorCode) {
    NumericalDllJNI.Numerical_DoMicroAggregation(swigCPtr, this, inFileName, outFileName, separator, nVar, nMinRecPerGr, nGroupsPerVar, typeSolution, nColumnsPerGr, errorCode);
  }

  public void SetProgressListener(IProgressListener2 progressListener) {
    NumericalDllJNI.Numerical_SetProgressListener(swigCPtr, this, IProgressListener2.getCPtr(progressListener), progressListener);
  }

}
