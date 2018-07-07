import Database.Local;
import main.CommonData;
import main.MetaExtract;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TESTCLASS {
    public static void main(String[] args){
        //Local.saveBackupData();
        //MetaExtract.extract();
//        Local.insertDataLocal(CommonData.data);
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(out);
        System.out.println("text1\n");
        System.out.println("text2");
        System.out.println("text3");
    }
}
