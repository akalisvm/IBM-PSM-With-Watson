package ucl.ac.uk.ibmpsmwithwatson.dao;

import com.bangdb.BangDBDatabase;
import com.bangdb.BangDBEnv;
import com.bangdb.DBParam;
import com.bangdb.TransactionType;

public class Test {
    public static void main(String[] args) {
        System.loadLibrary("bangdb-client-java");
        DBParam dbp = new DBParam();
        dbp.set_host("43.157.89.132");
        dbp.set_port("10101");
        dbp.setTransactionType(TransactionType.DB_MULTIOPS_TRANSACTION_NONE);
        BangDBEnv dbenv = new BangDBEnv(dbp);
        BangDBDatabase bdb = new BangDBDatabase("mydb", dbenv, dbp);
        if(bdb == null) {
            System.out.println("db could not be created, quitting");
        } else {
            System.out.println("db is existing");
        }
    }
}
