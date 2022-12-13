package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private final DatabaseHelper db_helper;
    public PersistentExpenseManager(DatabaseHelper databaseHelper) {
        this.db_helper = databaseHelper;
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(db_helper);
        AccountDAO persistentAccountDAO = new PersistentAccountDAO(db_helper);

        setTransactionsDAO(persistentTransactionDAO);
        setAccountsDAO(persistentAccountDAO);
    }
}
