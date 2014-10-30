package muargus.unusedClasses;

import java.io.File;
import java.io.IOException;

public class TestMU {

    public File tmp;

//    static{System.loadLibrary("libmuargusdll");} final private muargus.extern.dataengine.CMuArgCtrl MuArgusCtrl;
    public TestMU() {
        tmp = new File("C:\\Users\\Gebruiker\\Desktop\\Ge90.dat");
            //MuArgusCtrl = new muargus.extern.dataengine.CMuArgCtrl();
//        Action action = new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                delete();
//            }
//        };
//        this.rootPane.getActionMap().put("f1action", action);
//        this.rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
//                KeyStroke.getKeyStroke("F1"), "f1action");
    }

    public void make() {
        try {
            tmp = File.createTempFile("abctest", ".txt");
            tmp.deleteOnExit();
            System.out.println("Temp file : " + tmp.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("oepsie");
        }
    }

    public void delete() {
        tmp.delete();
    }

    public static void main(String[] args) throws IOException {

        // gebruik maken van de functie van MuArgusCtrl, bijvoorbeeld
        TestMU t = new TestMU();
        t.make();
        t.delete();
         //        t.MuArgusCtrl.CleanAll();
        //
        //        t.MuArgusCtrl.SetNumberVar(42);
        {
            
        }
    }

}
