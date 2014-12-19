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

public class IProgressListener2 {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IProgressListener2(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IProgressListener2 obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        NumericalDllJNI.delete_IProgressListener2(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    NumericalDllJNI.IProgressListener2_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    NumericalDllJNI.IProgressListener2_change_ownership(this, swigCPtr, true);
  }

  public void UpdateProgress(int perc) {
    NumericalDllJNI.IProgressListener2_UpdateProgress(swigCPtr, this, perc);
  }

  public IProgressListener2() {
    this(NumericalDllJNI.new_IProgressListener2(), true);
    NumericalDllJNI.IProgressListener2_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

}
