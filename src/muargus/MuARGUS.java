package muargus;

//import javax.swing.SwingUtilities;
import argus.model.Metadata;
import argus.utils.SystemUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.util.logging.Logger;
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

    private static Metadata metadata;
    static
    {
        metadata = new Metadata(false);
    }
    
    
    public static String getFullVersion() {
        return "" + MAJOR + "." + MINOR + "." + REVISION;
    }
   
    public static Metadata getMetadata() {
        return MuARGUS.metadata;
    }
    public static void setMetadata(Metadata metadata) {
        MuARGUS.metadata = metadata;
    }
    private static String tempDir;
    static {
        setTempDir(System.getProperty("java.io.tmpdir"));
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
        
    private static void sleepThread(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        }
        catch (InterruptedException ex) {
            // Do something, if there is a exception
            System.out.println(ex.toString());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
                /* Set the Windows Classic look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrameView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //batch = BATCH_NOBATCH;
        //getAnco();
        SystemUtils.setLogbook(SystemUtils.getRegString("general", "logbook", getTempFile("MuLogbook.txt")));
        SystemUtils.writeLogbook(" ");
        SystemUtils.writeLogbook("Start of MuArgus run");
        SystemUtils.writeLogbook("Version "+ MuARGUS.getFullVersion()+" build "+MuARGUS.BUILD);
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
