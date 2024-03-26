package vn.edu.tdtu.moneyintel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.edu.tdtu.moneyintel.model.Category;
import vn.edu.tdtu.moneyintel.model.OverallTransaction;
import vn.edu.tdtu.moneyintel.model.TimeLine;
import vn.edu.tdtu.moneyintel.model.Transaction;
import vn.edu.tdtu.moneyintel.model.Wallet;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoneyIntel_Final.db";
    private static final int DATABASE_VERSION = 1;
    // Wallet table and columns
    private static final String WALLET_TABLE_NAME = "Wallets";
    private static final String WALLET_COLUMN_ID = "id";
    private static final String WALLET_COLUMN_MONEY_START = "money_start";
    private static final String WALLET_COLUMN_MONEY_END = "money_end";
    private static final String WALLET_COLUMN_DATE = "date";

    // Category table and columns
    private static final String CATEGORY_TABLE_NAME = "Categories";
    private static final String CATEGORY_COLUMN_ID = "id";
    private static final String CATEGORY_COLUMN_NAME = "name";
    private static final String CATEGORY_COLUMN_TYPE = "type";
    private static final String CATEGORY_COLUMN_ICON = "icon";

    // Transaction table and columns
    private static final String TRANSACTION_TABLE_NAME = "Transactions";
    private static final String TRANSACTION_COLUMN_ID = "id";
    private static final String TRANSACTION_COLUMN_AMOUNT = "amount";
    private static final String TRANSACTION_COLUMN_NOTE = "note";
    private static final String TRANSACTION_COLUMN_DATE = "date";
    private static final String TRANSACTION_COLUMN_CATEGORY_ID = "category_id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the wallet table
        db.execSQL("CREATE TABLE " + WALLET_TABLE_NAME + " (" +
                WALLET_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WALLET_COLUMN_MONEY_START + " INTEGER, " +
                WALLET_COLUMN_MONEY_END + " INTEGER, " +
                WALLET_COLUMN_DATE + " TEXT)");

        // Create the category table
        db.execSQL("CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_COLUMN_NAME + " TEXT, " +
                CATEGORY_COLUMN_TYPE + " INTEGER, " +
                CATEGORY_COLUMN_ICON + " TEXT)");

        // Create the transaction table
        db.execSQL("CREATE TABLE " + TRANSACTION_TABLE_NAME + " (" +
                TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TRANSACTION_COLUMN_AMOUNT + " INTEGER, " +
                TRANSACTION_COLUMN_NOTE + " TEXT, " +
                TRANSACTION_COLUMN_DATE + " TEXT, " +
                TRANSACTION_COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + TRANSACTION_COLUMN_CATEGORY_ID + ") REFERENCES " +
                CATEGORY_TABLE_NAME + "(" + CATEGORY_COLUMN_ID + "))");

        // Create sample wallet
        Wallet wallet1 = new Wallet(5000000, 4950000, "2023-04-19");
        Wallet wallet2 = new Wallet(4950000, 4880000, "2023-04-23");
        Wallet wallet3 = new Wallet(4880000, 4760000, "2023-04-27");
        Wallet wallet4 = new Wallet(4760000, 4522000, "2023-04-30");
        Wallet wallet5 = new Wallet(4522000, 4262000, "2023-05-01");
        Wallet wallet6 = new Wallet(4262000, 5762000, "2023-05-02");

        // Insert wallets into database
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet1));
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet2));
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet3));
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet4));
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet5));
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet6));

        // Create sample categories
        Category category1 = new Category(1, "Ăn uống", Category.TYPE_EXPENSE, "ic_an_uong");
        Category category2 = new Category(2, "Đi lại", Category.TYPE_EXPENSE, "ic_di_lai");
        Category category3 = new Category(3, "Mua sắm", Category.TYPE_EXPENSE, "ic_mua_sam");
        Category category4 = new Category(4, "Lương", Category.TYPE_INCOME, "ic_luong");

        // Insert categories into database
        db.insert(CATEGORY_TABLE_NAME, null, toContentValues(category1));
        db.insert(CATEGORY_TABLE_NAME, null, toContentValues(category2));
        db.insert(CATEGORY_TABLE_NAME, null, toContentValues(category3));
        db.insert(CATEGORY_TABLE_NAME, null, toContentValues(category4));

        // Create sample transactions
        Transaction transaction1 = new Transaction(50000, "", "2023-04-19", category1);
        Transaction transaction2 = new Transaction(70000, "", "2023-04-23", category2);
        Transaction transaction3 = new Transaction(120000, "", "2023-04-27", category3);
        Transaction transaction4 = new Transaction(150000, "Taxi đi đầm sen", "2023-04-30", category2);
        Transaction transaction5 = new Transaction(88000, "", "2023-04-30", category1);
        Transaction transaction6 = new Transaction(260000, "", "2023-05-01", category3);
        Transaction transaction7 = new Transaction(1500000, "Nhận lương sớm", "2023-05-02", category4);

        // Insert transactions into database
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction1));
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction2));
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction3));
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction4));
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction5));
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction6));
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction7));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    private ContentValues toContentValues(Wallet wallet) {
        ContentValues values = new ContentValues();
        values.put(WALLET_COLUMN_MONEY_START, wallet.getMoneyStart());
        values.put(WALLET_COLUMN_MONEY_END, wallet.getMoneyEnd());
        values.put(WALLET_COLUMN_DATE, wallet.getDate());
        return values;
    }

    public ContentValues toContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, category.getName());
        values.put(CATEGORY_COLUMN_TYPE, category.getType());
        values.put(CATEGORY_COLUMN_ICON, category.getIcon());
        return values;
    }

    public ContentValues toContentValues(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_COLUMN_AMOUNT, transaction.getAmount());
        values.put(TRANSACTION_COLUMN_NOTE, transaction.getNote());
        values.put(TRANSACTION_COLUMN_DATE, transaction.getDate());
        values.put(TRANSACTION_COLUMN_CATEGORY_ID, transaction.getCategory().getId());
        return values;
    }

    public void insertWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(WALLET_TABLE_NAME, null, toContentValues(wallet));
        db.close();
    }

    public void updateWallet(Wallet wallet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = toContentValues(wallet);
        String selection = WALLET_COLUMN_DATE + " = ?";
        String[] selectionArgs = {wallet.getDate()};
        db.update(WALLET_TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    public Wallet getWalletByDate(String date) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { WALLET_COLUMN_ID, WALLET_COLUMN_MONEY_START, WALLET_COLUMN_MONEY_END, WALLET_COLUMN_DATE };
        String selection = WALLET_COLUMN_DATE + " = ?";
        String[] selectionArgs = { date };
        Cursor cursor = db.query(
                WALLET_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        Wallet wallet = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_ID));
            int money_start = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_MONEY_START));
            int money_end = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_MONEY_END));
            String walletDate = cursor.getString(cursor.getColumnIndexOrThrow(WALLET_COLUMN_DATE));
            wallet = new Wallet(id, money_start, money_end, walletDate);
        }
        cursor.close();
        db.close();
        return wallet;
    }


    public void insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CATEGORY_TABLE_NAME, null, toContentValues(category));
        db.close();
    }

    public void insertTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TRANSACTION_TABLE_NAME, null, toContentValues(transaction));
        db.close();
    }


    public Wallet getCurrentMoney(String date) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = { WALLET_COLUMN_MONEY_START, WALLET_COLUMN_MONEY_END };
        String selection = WALLET_COLUMN_DATE + " <= ?";
        String[] selectionArgs = { date };
        String orderBy = WALLET_COLUMN_DATE + " DESC";

        Cursor cursor = db.query(
                WALLET_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy,
                "1"
        );

        Wallet wallet = null;
        if (cursor.moveToFirst()) {
            int money_start = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_MONEY_START));
            int money_end = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_MONEY_END));

            wallet = new Wallet(money_start, money_end);
        }

        cursor.close();
        db.close();

        return wallet;
    }


    public Wallet getMoneyInRange(String dateStart, String dateEnd, boolean order) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = { WALLET_COLUMN_MONEY_START, WALLET_COLUMN_MONEY_END };
        String selection = WALLET_COLUMN_DATE + " >= ? AND " + WALLET_COLUMN_DATE + " <= ?";
        String[] selectionArgs = { dateStart, dateEnd };
        String orderBy = WALLET_COLUMN_DATE;

        if (order) {
            orderBy += " DESC";
        }
        else {
            orderBy += " ASC";
        }


        Cursor cursor = db.query(
                WALLET_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy,
                "1"
        );

        Wallet wallet = null;
        if (cursor.moveToFirst()) {
            int money_start = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_MONEY_START));
            int money_end = cursor.getInt(cursor.getColumnIndexOrThrow(WALLET_COLUMN_MONEY_END));

            wallet = new Wallet(money_start, money_end);
        }

        cursor.close();
        db.close();

        return wallet;
    }


    public List<Transaction> getRecentTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_ID,
                TRANSACTION_COLUMN_AMOUNT,
                TRANSACTION_COLUMN_NOTE,
                TRANSACTION_COLUMN_DATE,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_NAME,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_TYPE,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ICON
        };

        String join = "JOIN " + CATEGORY_TABLE_NAME + " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_CATEGORY_ID + " = " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID;
        String orderBy = "strftime('%s', " + TRANSACTION_COLUMN_DATE + ") DESC, " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_ID + " DESC";

        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME + " " + join,
                projection,
                null,
                null,
                null,
                null,
                orderBy,
                "3"
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_ID));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_AMOUNT));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_NOTE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_DATE));
            String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_NAME));
            int categoryType = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_TYPE));
            String categoryIcon = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_ICON));

            Category category = new Category(categoryName, categoryType, categoryIcon);
            Transaction transaction = new Transaction(id, amount, note, date, category);
            transactions.add(transaction);
        }

        cursor.close();
        db.close();

        return transactions;
    }


    public List<Transaction> getTransactionsByDate(String _date) {
        List<Transaction> transactions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_ID,
                TRANSACTION_COLUMN_AMOUNT,
                TRANSACTION_COLUMN_NOTE,
                TRANSACTION_COLUMN_DATE,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_NAME,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_TYPE,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ICON
        };

        String join = "JOIN " + CATEGORY_TABLE_NAME + " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_CATEGORY_ID + " = " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID;

        String selection = TRANSACTION_COLUMN_DATE + " = ? ";

        String[] selectionArgs = {_date};

        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME + " " + join,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_ID));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_AMOUNT));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_NOTE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_DATE));
            String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_NAME));
            int categoryType = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_TYPE));
            String categoryIcon = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_ICON));

            Category category = new Category(categoryName, categoryType, categoryIcon);
            Transaction transaction = new Transaction(id, amount, note, date, category);
            transactions.add(transaction);
        }

        cursor.close();
        db.close();

        return transactions;
    }


    public List<OverallTransaction> getOverallTransactionsByDate(String dateStart, String dateEnd) {
        List<OverallTransaction> overallTransactions = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(dateStart);
            Date endDate = dateFormat.parse(dateEnd);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().compareTo(endDate) <= 0) {
                String dateString = dateFormat.format(calendar.getTime());

                List<Transaction> transactions = getTransactionsByDate(dateString);
                if (transactions.isEmpty()) {
                    calendar.add(Calendar.DATE, 1);
                    continue;
                }

                int totalAmount = 0;
                for (Transaction transaction : transactions) {
                    if (transaction.getCategory().getType() == Category.TYPE_EXPENSE) {
                        totalAmount -= transaction.getAmount();
                    } else {
                        totalAmount += transaction.getAmount();
                    }
                }

                OverallTransaction ot = new OverallTransaction(totalAmount, dateString, transactions);
                overallTransactions.add(ot);

                calendar.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return overallTransactions;
    }


    public List<Transaction> getTransactionsExpenseByCategoryAndDate(int categoryId, String _date) {
        List<Transaction> transactions = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_ID,
                TRANSACTION_COLUMN_AMOUNT,
                TRANSACTION_COLUMN_NOTE,
                TRANSACTION_COLUMN_DATE,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_NAME,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_TYPE,
                CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ICON
        };

        String join = "JOIN " + CATEGORY_TABLE_NAME + " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_CATEGORY_ID + " = " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID;

        String selection = TRANSACTION_COLUMN_CATEGORY_ID + " = ? AND " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_TYPE + " = 0 AND " + TRANSACTION_COLUMN_DATE + " = ? ";

        String[] selectionArgs = { String.valueOf(categoryId), _date };

        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME + " " + join,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_ID));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_AMOUNT));
            String note = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_NOTE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_DATE));
            String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_NAME));
            int categoryType = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_TYPE));
            String categoryIcon = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_ICON));

            Category category = new Category(categoryName, categoryType, categoryIcon);
            Transaction transaction = new Transaction(id, amount, note, date, category);
            transactions.add(transaction);
        }

        cursor.close();
        db.close();

        return transactions;
    }


    public List<OverallTransaction> getOverallTransactionsByCategory(String dateStart, String dateEnd) {
        List<OverallTransaction> overallTransactions = new ArrayList<>();
        List<Category> allCategory = getAllCategories();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(dateStart);
            Date endDate = dateFormat.parse(dateEnd);

            for (Category category : allCategory) {
                int totalAmount = 0;
                List<Transaction> transactions = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                while (calendar.getTime().compareTo(endDate) <= 0) {
                    String dateString = dateFormat.format(calendar.getTime());
                    List<Transaction> trans = getTransactionsExpenseByCategoryAndDate(category.getId(), dateString);

                    if (!trans.isEmpty()) {
                        transactions.addAll(trans);
                        for (Transaction i : trans) {
                            totalAmount += i.getAmount();
                        }
                    }
                    calendar.add(Calendar.DATE, 1);
                }

                if (!transactions.isEmpty()) {
                    OverallTransaction ot = new OverallTransaction(totalAmount, category, transactions);
                    overallTransactions.add(ot);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return overallTransactions;
    }


    public int getTotalExpenseByDate(String _date) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                TRANSACTION_COLUMN_AMOUNT
        };

        String join = "JOIN " + CATEGORY_TABLE_NAME + " ON " + TRANSACTION_TABLE_NAME + "." + TRANSACTION_COLUMN_CATEGORY_ID + " = " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_ID;

        String selection = TRANSACTION_COLUMN_DATE + " = ? AND " + CATEGORY_TABLE_NAME + "." + CATEGORY_COLUMN_TYPE + " = 0";

        String[] selectionArgs = {_date};

        Cursor cursor = db.query(
                TRANSACTION_TABLE_NAME + " " + join,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int expense = 0;
        while (cursor.moveToNext()) {
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(TRANSACTION_COLUMN_AMOUNT));
            expense += amount;
        }

        cursor.close();
        db.close();

        return expense;
    }


    public int getTotalExpenseInRange(String dateStart, String dateEnd) {
        int totalExpense = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = dateFormat.parse(dateStart);
            Date endDate = dateFormat.parse(dateEnd);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (calendar.getTime().compareTo(endDate) <= 0) {
                String dateString = dateFormat.format(calendar.getTime());

                int totalInDate = getTotalExpenseByDate(dateString);
                totalExpense += totalInDate;

                calendar.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return totalExpense;
    }


    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                CATEGORY_COLUMN_ID,
                CATEGORY_COLUMN_NAME,
                CATEGORY_COLUMN_TYPE,
                CATEGORY_COLUMN_ICON
        };

        Cursor cursor = db.query(
                CATEGORY_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_NAME));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_TYPE));
            String icon = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY_COLUMN_ICON));

            Category category = new Category(id, name, type, icon);
            categories.add(category);
        }

        cursor.close();
        db.close();

        return categories;
    }
}
