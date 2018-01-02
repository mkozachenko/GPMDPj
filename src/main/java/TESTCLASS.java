import Database.Local;
import main.CommonData;
import main.MetaExtract;

public class TESTCLASS {
    public static void main(String[] args){
        //Local.saveBackupData();
        //MetaExtract.extract();
        Local.insertDataLocal(CommonData.data);

    }
}
