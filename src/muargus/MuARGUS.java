package muargus;

//import javax.swing.SwingUtilities;
import argus.utils.SystemUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import muargus.extern.dataengine.CMuArgCtrl;
import muargus.view.MainFrameView;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ambargus
 */
public class MuARGUS {

    private static final Logger logger = Logger.getLogger(MuARGUS.class.getName());

    // Version info
    public static final int MAJOR = 4;
    public static final int MINOR = 0;
    public static final String REVISION = "1 (beta)";
    public static final int BUILD = 1;

    public static final int MAXDIMS = 10;

    private static final String messageTitle = "Mu Argus";
    private static final int nHistogramClasses = 10;
    private static final int nCumulativeHistogramClasses = 100;
    private static final String defaultSeparator = ",";

    private static final String lookAndFeel = "Windows";

    private static final String manualPath = "C:\\Users\\Gebruiker\\Desktop\\MUmanual4.3.pdf";
    private static final String acrordPath = "C:\\Program Files\\Adobe\\Reader 11.0\\Reader\\AcroRd32.exe";

    static {
        System.loadLibrary("libmuargusdll");
        System.loadLibrary("libnumericaldll");
    }

    //private static CMuArgCtrl muArgCrtl = new CMuArgCtrl();
    private static CalculationService calcService = new CalculationService(new CMuArgCtrl());

    public static CalculationService getCalculationService() {
        return calcService;
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
                g.setColor(new Color(0, 0, 200));
                Font font = g.getFont().deriveFont(Font.BOLD, 16.0f);
                g.setFont(font);
//             Even geen gevogel aan het splash screen               
//               g.drawString("Version " + Application.getFullVersion() + " (Build " + Application.BUILD + ")", 160, 105 /*230*/);        
//               splash.update();
                // Sleep for 1/2 second, so people can see it
                sleepThread(500);
            }
        }
    }

    public static void showHelp(String namedDest) {
        try {
            String cmdString = "\"" + acrordPath + "\" /A \"nameddest=" + namedDest + "\" \"" + manualPath + "\"";
            System.out.println(cmdString);
            Process p = Runtime.getRuntime().exec(cmdString);
        } catch (IOException ex) {
        } catch (Exception ex2) {
        }
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
