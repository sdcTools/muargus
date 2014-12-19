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

public interface NumericalDllConstants {
  public final static int MAXRECORDLENGTH = NumericalDllJNI.MAXRECORDLENGTH_get();
  public final static int MIC_OK = NumericalDllJNI.MIC_OK_get();
  public final static int MIC_OUT_MEM = NumericalDllJNI.MIC_OUT_MEM_get();
  public final static int MIC_ERR_NEL = NumericalDllJNI.MIC_ERR_NEL_get();
  public final static int MIC_ERR_NVAR = NumericalDllJNI.MIC_ERR_NVAR_get();
  public final static int MIC_ERR_MEG = NumericalDllJNI.MIC_ERR_MEG_get();
  public final static int MIC_ERR_GOV = NumericalDllJNI.MIC_ERR_GOV_get();
  public final static int MIC_ERR_VPG = NumericalDllJNI.MIC_ERR_VPG_get();
  public final static int MIC_ERR_COL = NumericalDllJNI.MIC_ERR_COL_get();
  public final static int MIC_ERR_STD = NumericalDllJNI.MIC_ERR_STD_get();
}
