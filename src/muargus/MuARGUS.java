package muargus;

//import javax.swing.SwingUtilities;
import argus.model.Metadata;
import argus.utils.SystemUtils;
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

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                new MainFrameView().setVisible(true);
            }
        });
      
	}

}
