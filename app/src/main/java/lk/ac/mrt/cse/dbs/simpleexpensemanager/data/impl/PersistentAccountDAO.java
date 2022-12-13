package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final DatabaseHelper db_helper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.db_helper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ DatabaseHelper.acc_no +" FROM " + DatabaseHelper.tb_account, null);


        if (cursor.moveToFirst()) {
            do {
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.tb_account, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.tb_account + " WHERE " + DatabaseHelper.acc_no +" = " + accountNo, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "invalid " + accountNo + "Account";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = db_helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.acc_no, account.getAccountNo());
        values.put(DatabaseHelper.bank_name, account.getBankName());
        values.put(DatabaseHelper.hol_name, account.getAccountHolderName());
        values.put(DatabaseHelper.balance, account.getBalance());

        db.insert(DatabaseHelper.tb_account, null, values);
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = db_helper.getWritableDatabase();
        db.delete(DatabaseHelper.tb_account, DatabaseHelper.acc_no + " = ?", new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case INCOME:
                values.put(DatabaseHelper.balance, account.getBalance() + amount);
                break;
            case EXPENSE:
                values.put(DatabaseHelper.balance, account.getBalance() - amount);
                break;
        }
        db.update(DatabaseHelper.tb_account, values, DatabaseHelper.acc_no + " = ?", new String[] { accountNo });
    }

}
