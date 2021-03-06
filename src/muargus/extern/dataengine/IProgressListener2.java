/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.10
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package muargus.extern.dataengine;

public class IProgressListener2 {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

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
