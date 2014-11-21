package muargus;

//import javax.swing.SwingUtilities;
import argus.model.ArgusException;
import argus.utils.SystemUtils;
import java.awt.Color;
import java.awt.Dimension;
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
import org.icepdf.ri.viewer.Launcher;


/**
 *
 * @author ambargus
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
    //private static final File manual = new File("./resources/MUmanual4.3.pdf");
    private static final File manual = new File("resources/MUmanual4.3.pdf");
    private static final String acrord32 = "acrord32.exe"; // finds the acrord32.exe
    private static final int sleepTime = 2000;
    private static Process helpViewerProcess;

    static {
        System.loadLibrary("lib/libmuargusdll");
        System.loadLibrary("lib/libnumericaldll");
    }

    //private static CMuArgCtrl muArgCrtl = new CMuArgCtrl();
    private static final CalculationService calcService = new CalculationService(new CMuArgCtrl());
    

    public static CalculationService getCalculationService() {
        return calcService;
    }
    
    private static  SpssUtils spssUtils;// = new SpssUtils();
    
    public static SpssUtils getSpssUtils() {
        if (spssUtils == null) {
            spssUtils = new SpssUtils();
        }
        return spssUtils;
    }

    public static String getFullVersion() {
        return "" + MAJOR + "." + MINOR + "." + REVISION;
    }

    private static String tempDir;

    static {
        setTempDir(System.getProperty("java.io.tmpdir"));
    }

    public static String getMessageTitle() {
        return messageTitle;
    }

    public static int getNHistogramClasses(boolean cumulative) {
        return cumulative ? nCumulativeHistogramClasses : nHistogramClasses;
    }

    public static Locale getLocale() {
        return Locale.ENGLISH;
    }

    public static String getDefaultSeparator() {
        return defaultSeparator;
    }

    public static String getTempDir() {
        return MuARGUS.tempDir;
    }

    public static void setTempDir(String tempDir) {
        MuARGUS.tempDir = FilenameUtils.normalizeNoEndSeparator(tempDir);
    }

    public static String getTempFile(String fileName) {
        return FilenameUtils.concat(tempDir, fileName);
    }

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
                g.drawString("Version " + getFullVersion() + " (Build " + BUILD + ")", (splash.getSize().width / 2) - 100, 230);
                //System.out.println(splash.getSize().width / 2);
                splash.update();
                // Sleep for 1/2 second, so people can see it
                sleepThread(MuARGUS.sleepTime);
            }
        }
    }

    public static void showHelp(String namedDest) throws ArgusException {
//        if (namedDest == null) {
//            Launcher.main(new String[] {"-loadfile", manual.getAbsolutePath()});
//        }
//        else{
//            Launcher.main(new String[] {"-loadfile", manual.getAbsolutePath(), "-nameddest", namedDest});
//        }
//    }
        //try {            
            ArrayList<String> args = new ArrayList<String>();
            args.add("-loadfile");
            args.add(manual.getAbsolutePath());
            if (namedDest != null) {
            args.add("-nameddest");
            args.add(namedDest);
            }

            try {
            execClass(
                    "org.icepdf.ri.viewer.Main",
                    "lib\\ICEpdf.jar",
                    args);
            }
            catch (IOException | InterruptedException ex) {
                throw new ArgusException("Error trying to display help file");
            }
    }
            //String cmdString = "taskkill /IM " + acrord32;
            //System.out.println(cmdString);
            //Process p = Runtim.e.getRuntime().exec(cmdString);
//        } catch (IOException ex) {
//        } catch (Exception ex2) {
//        }
//        try {
//            String cmdString = "cmd.exe /c start " + acrord32 + " /A \"nameddest=" + namedDest + "\" \"" + manual.getAbsolutePath() + "\"";
//            Process p = Runtime.getRuntime().exec(cmdString);
//        } catch (IOException ex) {
//        } catch (Exception ex2) {
//        }
    

        public static void execClass(String className, String classPath, List<String> arguments) throws IOException,
                                               InterruptedException {
        if (helpViewerProcess != null) {
            helpViewerProcess.destroy();
            helpViewerProcess = null;
        }    
            
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        //String classpath = System.getProperty("java.class.path");
        //String className = klass.getCanonicalName();
        arguments.add(0, javaBin);
        arguments.add(1, "-cp");
        arguments.add(2, classPath);
        arguments.add(3, className);
        ProcessBuilder builder = new ProcessBuilder( arguments );

        helpViewerProcess = builder.start();
    }
        
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
                if (lookAndFeel.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //batch = BATCH_NOBATCH;
        //getAnco();
        SystemUtils.setRegistryRoot("muargus");
        SystemUtils.setLogbook(SystemUtils.getRegString("general", "logbook", getTempFile("MuLogbook.txt")));
        SystemUtils.writeLogbook(" ");
        SystemUtils.writeLogbook("Start of MuArgus run");
        SystemUtils.writeLogbook("Version " + MuARGUS.getFullVersion() + " build " + MuARGUS.BUILD);
        SystemUtils.writeLogbook("--------------------------");
        //generalMaxHitasTime = SystemUtils.getRegInteger("optimal", "maxhitastime", 10);
        //anco = SystemUtils.getRegBoolean("general", "anco", false);

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
