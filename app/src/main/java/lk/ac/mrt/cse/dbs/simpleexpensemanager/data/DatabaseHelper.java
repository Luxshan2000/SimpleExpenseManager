package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String db_name = "200359K";
    private static final int db_version = 1;
    public static final String tb_transaction = "transactions";
    public static final String tb_account = "accounts";

    private static final String tran_id = "id";
    public static final String exp_type = "expenseType";
    public static final String amount = "amount";
    public static final String date = "date";
    public static final String acc_no = "accountNo";
    public static final String bank_name = "bankName";
    public static final String hol_name = "accountHolderName";
    public static final String balance = "balance";



    public DatabaseHelper(@Nullable Context context) {
        super(context, db_name, null, db_version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tb_account + "("
                + acc_no + " TEXT PRIMARY KEY," + bank_name + " TEXT,"
                + hol_name + " TEXT," + balance + " REAL" + ")");
        db.execSQL("CREATE TABLE " + tb_transaction + "("
                + tran_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + date + " TEXT," + acc_no + " TEXT,"
                + exp_type + " TEXT," + amount + " REAL," + "FOREIGN KEY(" + acc_no +
                ") REFERENCES "+ tb_account +"(" + acc_no + ") )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("DROP TABLE IF EXISTS '" + tb_account + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + tb_transaction + "'");
        onCreate(db);
    }
}