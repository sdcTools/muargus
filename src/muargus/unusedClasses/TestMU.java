package muargus.unusedClasses;

public class TestMU {

 

    static{System.loadLibrary("libmuargusdll");} final private muargus.extern.dataengine.CMuArgCtrl MuArgusCtrl;

   

    public TestMU(){

        MuArgusCtrl = new muargus.extern.dataengine.CMuArgCtrl();

    }

   

    public static void main(String[] args) {

        // gebruik maken van de functie van MuArgusCtrl, bijvoorbeeld
        TestMU t = new TestMU();
        t.MuArgusCtrl.CleanAll();

        t.MuArgusCtrl.SetNumberVar(42);

    }

}