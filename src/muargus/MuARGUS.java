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
package muargus;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import com.ibm.statistics.util.Utility;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.view.MainFrameView;
import org.apache.commons.io.FilenameUtils;
import muargus.model.ClassPathHack;

/**
 * Main class of Mu-Argus.
 *
 * @author Statistics Netherlands
 */
public class MuARGUS {

    // Version info
    public static final int MAJOR = 5;
    public static final int MINOR = 1;
    public static final String REVISION = "2";
    public static final int BUILD = 1;

    public static final int MAXDIMS = 10;

    private static final String messageTitle = "Mu Argus";
    private static final int nHistogramClasses = 10;
    private static final int nCumulativeHistogramClasses = 100;
    private static final String defaultSeparator = ",";

    private static final String lookAndFeel = "Windows";
    private static final File manual = new File("resources/MUmanual5.1.pdf");
    private static final int sleepTime = 2000;
    private static Process helpViewerProcess;

    static {
        System.loadLibrary("muargusdll");
        System.loadLibrary("numericaldll");
    }

    private static final CalculationService calcService = new CalculationService(new CMuArgCtrl());
    private static SpssUtils spssUtils;
    private static String tempDir;

    static {
        setTempDir(System.getProperty("java.io.tmpdir"));
    }

    /**
     * Gets the calculations service.
     *
     * @return CalculationService.
     */
    public static CalculationService getCalculationService() {
        return MuARGUS.calcService;
    }

    
    private static String getSpssVersion()
    {
        Utility FindSpss = new Utility();
        return FindSpss.getStatisticsLocationLatest();
    }
    
    static{
        try{
            ClassPathHack.addFile(getSpssVersion() + "\\spssjavaplugin.jar");
        }catch (IOException ex){System.out.print(ex.toString());};
    }
   
    /**
     * Gets the instance of SpssUtils.
     *
     * @return SpssUtils, or null if the Spss plugin cannot be loaded
     */
    public static SpssUtils getSpssUtils() {
        try {
            if (MuARGUS.spssUtils == null) {
                MuARGUS.spssUtils = new SpssUtils();
            }
            return MuARGUS.spssUtils;
        } catch (NoClassDefFoundError err) {
            return null;
        }
    }

    /**
     * Gets the full version.
     *
     * @return String containing the full version.
     */
    public static String getFullVersion() {
        return "" + MuARGUS.MAJOR + "." + MuARGUS.MINOR + "." + MuARGUS.REVISION;
    }

    /**
     * Gets the message title.
     *
     * @return String containing the message title.
     */
    public static String getMessageTitle() {
        return MuARGUS.messageTitle;
    }

    /**
     * Gets the number of histogram classes.
     *
     * @param cumulative Boolean indicating whether the number of cumulative or
     * normal histogram classes are requested.
     * @return Integer containing the number of histogram classes.
     */
    public static int getNHistogramClasses(boolean cumulative) {
        return cumulative ? MuARGUS.nCumulativeHistogramClasses : MuARGUS.nHistogramClasses;
    }

    /**
     * Gets the Locale.
     *
     * @return Locale containing the English locale.
     */
    public static Locale getLocale() {
        return Locale.ENGLISH;
    }

    /**
     * Gets the default separator.
     *
     * @return String containing the default separator.
     */
    public static String getDefaultSeparator() {
        return MuARGUS.defaultSeparator;
    }

    /**
     * Gets the temp directory.
     *
     * @return String containing the temp directory.
     */
    public static String getTempDir() {
        return MuARGUS.tempDir;
    }

    /**
     * Sets the temp directory.
     *
     * @param tempDir String containing the temp directory.
     */
    public static void setTempDir(String tempDir) {
        MuARGUS.tempDir = FilenameUtils.normalizeNoEndSeparator(tempDir);
    }

    /**
     * Gets a temp file.
     *
     * @param fileName String containing the temp file name.
     * @return String containing the path to the filename.
     */
    public static String getTempFile(String fileName) {
        return FilenameUtils.concat(MuARGUS.tempDir, fileName);
    }

    /**
     * Shows the build info on the splash screen.
     */
    public static void showBuildInfoInSplashScreen() {
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
        } else {
            Graphics2D g = splash.createGraphics();
            if (g == null) {
                System.out.println("g is null");
            } else {
                g.setPaintMode();
                g.setColor(new Color(255, 0, 0));
                Font font = g.getFont().deriveFont(Font.BOLD, 14.0f);
                g.setFont(font);
                String version = "Version " + getFullVersion() + " (Build " + MuARGUS.BUILD + ")";
                g.drawString(version, (splash.getSize().width / 2) - (version.length()*3), (3*splash.getSize().height/4));
                splash.update();
                sleepThread(MuARGUS.sleepTime);
            }
        }
    }

    /**
     * Shows the content sensitive help.
     *
     * @param namedDest String containing the named destination.
     * @throws ArgusException Throws an ArgusException when an error occurs
     * while trying to display the help file.
     */
    public static void showHelp(String namedDest) throws ArgusException {
        ArrayList<String> args = new ArrayList<>();
        args.add("-loadfile");
        args.add(MuARGUS.manual.getAbsolutePath());
        if (namedDest != null) {
            args.add("-nameddest");
            args.add(namedDest);
        }

        try {
            execClass(
                    "org.icepdf.ri.viewer.Main",
                    "lib/ICEpdf.jar",
                    args);
        } catch (IOException | InterruptedException ex) {
            throw new ArgusException("Error trying to display help file");
        }
    }

    /**
     * Creates a new process starting with the class with the given name and
     * path
     *
     * @param className Fully qualified name of the class
     * @param classPath Path to the directory or jar file containing the class
     * @param arguments List of commandline arguments given to the new instance
     * @throws IOException Occurs when de class cannot be loaded
     * @throws InterruptedException Occurs when the new process is interrupted
     */
    public static void execClass(String className, String classPath, List<String> arguments) throws IOException,
            InterruptedException {
        if (MuARGUS.helpViewerProcess != null) {
            MuARGUS.helpViewerProcess.destroy();
            MuARGUS.helpViewerProcess = null;
        }

        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome
                + File.separator + "bin"
                + File.separator + "java";
        arguments.add(0, javaBin);
        arguments.add(1, "-cp");
        arguments.add(2, classPath);
        arguments.add(3, className);
        ProcessBuilder builder = new ProcessBuilder(arguments);

        MuARGUS.helpViewerProcess = builder.start();
    }

    /**
     * Starts a sleep thread for a given amount of time.
     *
     * @param milliSecs Integer containing the number of milli seconds that the
     * program will sleep.
     */
    private static void sleepThread(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (InterruptedException ex) {
            // Do something, if there is a exception
            System.out.println(ex.toString());
        }
    }

    /**
     * Main method for running Mu-Argus.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the  look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (MuARGUS.lookAndFeel.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        SystemUtils.setRegistryRoot("muargus");
        SystemUtils.setLogbook(SystemUtils.getRegString("general", "logbook", getTempFile("MuLogbook.txt")));
        SystemUtils.writeLogbook(" ");
        SystemUtils.writeLogbook("Start of MuArgus run");
        SystemUtils.writeLogbook("Version " + MuARGUS.getFullVersion() + " build " + MuARGUS.BUILD);
        SystemUtils.writeLogbook("--------------------------");

        showBuildInfoInSplashScreen();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrameView().setVisible(true);
            }
        });
    }

}
