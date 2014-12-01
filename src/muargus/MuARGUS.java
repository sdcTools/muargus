package muargus;

import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.view.MainFrameView;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Statistics Netherlands
 */
public class MuARGUS {

    private static final Logger logger = Logger.getLogger(MuARGUS.class.getName());

    // Version info
    public static final int MAJOR = 5;
    public static final int MINOR = 0;
    public static final String REVISION = "1 (beta)";
    public static final int BUILD = 1;

    public static final int MAXDIMS = 10;

    private static final String messageTitle = "Mu Argus";
    private static final int nHistogramClasses = 10;
    private static final int nCumulativeHistogramClasses = 100;
    private static final String defaultSeparator = ",";

    private static final String lookAndFeel = "Windows";
    private static final File manual = new File("resources/MUmanual4.3.pdf");
    private static final int sleepTime = 2000;
    private static Process helpViewerProcess;

    static {
        System.loadLibrary("lib/libmuargusdll");
        System.loadLibrary("lib/libnumericaldll");
    }

    private static final CalculationService calcService = new CalculationService(new CMuArgCtrl());
    private static SpssUtils spssUtils;
    private static String tempDir;

    static {
        setTempDir(System.getProperty("java.io.tmpdir"));
    }

    /**
     *
     * @return
     */
    public static CalculationService getCalculationService() {
        return MuARGUS.calcService;
    }

    /**
     *
     * @return
     */
    public static SpssUtils getSpssUtils() {
        if (MuARGUS.spssUtils == null) {
            MuARGUS.spssUtils = new SpssUtils();
        }
        return MuARGUS.spssUtils;
    }

    /**
     *
     * @return
     */
    public static String getFullVersion() {
        return "" + MuARGUS.MAJOR + "." + MuARGUS.MINOR + "." + MuARGUS.REVISION;
    }

    /**
     * 
     * @return 
     */
    public static String getMessageTitle() {
        return MuARGUS.messageTitle;
    }

    /**
     * 
     * @param cumulative
     * @return 
     */
    public static int getNHistogramClasses(boolean cumulative) {
        return cumulative ? MuARGUS.nCumulativeHistogramClasses : MuARGUS.nHistogramClasses;
    }

    /**
     * 
     * @return 
     */
    public static Locale getLocale() {
        return Locale.ENGLISH;
    }

    /**
     * 
     * @return 
     */
    public static String getDefaultSeparator() {
        return MuARGUS.defaultSeparator;
    }

    /**
     * 
     * @return 
     */
    public static String getTempDir() {
        return MuARGUS.tempDir;
    }

    /**
     * 
     * @param tempDir 
     */
    public static void setTempDir(String tempDir) {
        MuARGUS.tempDir = FilenameUtils.normalizeNoEndSeparator(tempDir);
    }

    /**
     * 
     * @param fileName
     * @return 
     */
    public static String getTempFile(String fileName) {
        return FilenameUtils.concat(MuARGUS.tempDir, fileName);
    }

    /**
     * 
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
                g.setColor(new Color(200, 0, 0));
                Font font = g.getFont().deriveFont(Font.BOLD, 14.0f);
                g.setFont(font);
                g.drawString("Version " + getFullVersion() + " (Build " + MuARGUS.BUILD + ")", (splash.getSize().width / 2) - 100, 230);
                splash.update();
                sleepThread(MuARGUS.sleepTime);
            }
        }
    }

    /**
     * 
     * @param namedDest
     * @throws ArgusException 
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
                    "lib\\ICEpdf.jar",
                    args);
        } catch (IOException | InterruptedException ex) {
            throw new ArgusException("Error trying to display help file");
        }
    }

    /**
     * 
     * @param className
     * @param classPath
     * @param arguments
     * @throws IOException
     * @throws InterruptedException 
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
     *
     * @param milliSecs
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
