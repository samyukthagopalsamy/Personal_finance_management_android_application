package com.thriftyApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "thrifty.db";
    private static final String TABLE_SIGNUP = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_BUDGET = "budget";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD= "password";

    private static final String TABLE_TRANSACT = "transactions";
    private static final String COL_TID = "id";
    private static final String COL_U_ID = "uid";
    private static final String COL_TAG = "tag";
    private static final String COL_EXIN = "exin";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_DATETIME = "created_at";

    private static final String TABLE_ALERTS = "alerts_table";
    private static final String COL_ALERT_ID = "alert_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_ALERT_MESSAGE= "alert_message";
    private static final String COL_ALERT_TIME= "alert_time";

    private SQLiteDatabase db;
    private static final String CREATE_TABLE_SIGNUP = "CREATE TABLE " + TABLE_SIGNUP  + "( " + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL , " + COLUMN_NAME + " TEXT NOT NULL , " + COLUMN_EMAIL +" TEXT NOT NULL , " + COLUMN_MOBILE +" INTEGER NOT NULL, " + COLUMN_BUDGET + " INTEGER NOT NULL, " + COLUMN_PASSWORD + " TEXT NOT NULL );";

    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACT  + "( " + COL_TID + " INTEGER PRIMARY KEY NOT NULL , " + COL_U_ID + " INTEGER NOT NULL " + " , " + COL_TAG + " TEXT NOT NULL , " + COL_EXIN +" INTEGER NOT NULL, " + COL_DATETIME +" DATETIME  NOT NULL, " + COL_AMOUNT + " INTEGER NOT NULL );";

    private static final String CREATE_TABLE_ALERTS = "CREATE TABLE " + TABLE_ALERTS  + "( " + COL_ALERT_ID + " INTEGER PRIMARY KEY NOT NULL , " + COL_USER_ID + " INTEGER NOT NULL " + " , " + COL_ALERT_MESSAGE + " TEXT NOT NULL , " + COL_ALERT_TIME +" DATETIME  NOT NULL );";

    @Override
    public void onCreate(SQLiteDatabase db2) {
        db = db2;
        db.execSQL (CREATE_TABLE_SIGNUP);
        db.execSQL (CREATE_TABLE_TRANSACTION);
        db.execSQL (CREATE_TABLE_ALERTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query1 = "DROP TABLE IF EXISTS " + TABLE_SIGNUP;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_TRANSACT;
        String query3 = "DROP TABLE IF EXISTS " + TABLE_ALERTS;
        db.execSQL (query1);
        db.execSQL (query2);
        db.execSQL (query3);
    }

    public DatabaseHelper (Context context) {

        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    public void insertContact(Contact c) {

        db = this.getWritableDatabase ();
        ContentValues values = new ContentValues ();
        values.put (COLUMN_NAME, c.getName ());
        values.put (COLUMN_EMAIL, c.getEmailId ());
        values.put (COLUMN_MOBILE, c.getMobile ());
        values.put (COLUMN_BUDGET, c.getBudget ());
        values.put (COLUMN_PASSWORD, c.getPassword ());
        db.insert (TABLE_SIGNUP, null, values);
    }

    public void insertTransaction(Transactions t) {

        db = this.getWritableDatabase ();
        ContentValues values = new ContentValues ();
        values.put (COL_U_ID, t.getUid());
        values.put (COL_TAG, t.getTag ());
        values.put (COL_AMOUNT, t.getAmount());
        values.put (COL_EXIN, t.getExin ());
        values.put (COL_DATETIME, DatabaseHelper.getDateTime ());
        db.insert (TABLE_TRANSACT, null, values);
    }
    public void insertRemainder(AlertsTable t) {

        db = this.getWritableDatabase ();
        ContentValues values = new ContentValues ();
        values.put (COL_USER_ID, t.getUid ());
        values.put (COL_ALERT_MESSAGE, t.getMessage ());
        values.put (COL_ALERT_TIME, t.getalert_at ());
        db.insert (TABLE_ALERTS, null, values);
    }

    public List<String> searchPass (String user) {

        String u, id = null, pass = "Not Found", budget = "1000";
        List<String> list = new ArrayList<> ();
        db = this.getReadableDatabase ();
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD +  ", " + COLUMN_NAME + ", " + COLUMN_BUDGET + " FROM " + TABLE_SIGNUP;
        Cursor cursor = db.rawQuery (query, null);
        if (cursor.getCount () > 0) {
            if (cursor.moveToFirst ( )) {
                do {
                    u = cursor.getString (1);
                    Log.i ("user", u);
                    if (u.equals (user)) {
                        pass = cursor.getString (2);
                        id = cursor.getString (0);
                        budget = cursor.getString (4);
                        Utils.userName = cursor.getString (3);
                        Log.i ("Password & UID", pass + id);
                        break;
                    }
                } while (cursor.moveToNext ( ));
            }
            list.add (pass);
            list.add (id);
            list.add (budget);
        }
        cursor.close ();
        db.close ();
        return list;
    }

    public Contact getUser () {

        db = this.getReadableDatabase ();
        Contact c = new Contact ();
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD +  ", " + COLUMN_NAME + ", " + COLUMN_BUDGET +  ", "+COLUMN_MOBILE +  " FROM " + TABLE_SIGNUP + " WHERE " + COLUMN_ID + " = " + Utils.userId + ";";
        Cursor cursor = db.rawQuery (query, null);
        if (cursor.moveToFirst ()) {

                c.setId (Integer.parseInt (cursor.getString (0)));
                c.setEmailId (cursor.getString (1));
                c.setPassword (cursor.getString (2));
                c.setMobile (Long.parseLong (cursor.getString (5)));
                c.setBudget (Integer.parseInt (cursor.getString (4)));
                c.setName (cursor.getString (3));
        }
        return c;
    }


    public void changeBudget () {
        db = this.getWritableDatabase ();

        SQLiteDatabase db = this.getWritableDatabase();
        Contact c = getUser ();
        c.setBudget (Long.parseLong (Utils.budget));
        ContentValues values = new ContentValues ();
        values.put (COLUMN_NAME, c.getName ());
        values.put (COLUMN_EMAIL, c.getEmailId ());
        values.put (COLUMN_MOBILE, c.getMobile ());
        values.put (COLUMN_BUDGET, c.getBudget ());
        values.put (COLUMN_PASSWORD, c.getPassword ());

        // updating row
        db.update(TABLE_SIGNUP, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(c.getId ()) });

    }
    public ArrayList<String> getTransactions () {

        ArrayList<String> list = new ArrayList<> ();
        db = this.getReadableDatabase ();
        String query = "SELECT " + COL_AMOUNT + ", " + COL_TAG + " , " + COL_DATETIME +" , " + COL_EXIN + ", " + COL_U_ID +" FROM " + TABLE_TRANSACT +" WHERE " + COL_U_ID + " = " + Utils.userId + " ORDER BY " + COL_DATETIME + " DESC ;" ;
        Cursor cursor = db.rawQuery (query, null);

        String tag, amount, exin, uid, timeA, timeB;
        if (cursor.moveToFirst ()) {
            if(cursor.getCount () > 0) {
                do {
                    tag = cursor.getString (1);
                    amount = cursor.getString (0);
                    timeA = cursor.getString (2);
                    exin = cursor.getString (3);
                    uid = cursor.getString (4);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault ());
                    Date sourceDate = null;
                    try {
                        sourceDate = dateFormat.parse(timeA);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm:ss a",Locale.getDefault ());
                    timeB = targetFormat.format(sourceDate);

                    Log.i("PreethisTransaction",tag +" " + amount+ " " + timeB +" " + exin + " "+uid );
                    if ("0".equals (exin)) {
                       amount = "- ₹ "+ amount;
                    }
                    else
                        amount = " ₹ "+amount;
                    if(tag != null)
                        list.add("\n" + tag + "\n" + amount + "\n" + timeB + "\n");
                }while(cursor.moveToNext ());
            }
        }
        db.close ();
        cursor.close ();
        return list;
    }

    public ArrayList<Transactions> getTransactionsPDF () {

        ArrayList<Transactions> list = new ArrayList<> ();
        db = this.getReadableDatabase ();
        Transactions t;
        String query = "SELECT " + COL_AMOUNT + ", " + COL_TAG + " , " + COL_DATETIME +" , " + COL_EXIN + ", " + COL_U_ID +" FROM " + TABLE_TRANSACT +" WHERE " + COL_U_ID + " = " + Utils.userId + " ORDER BY " + COL_DATETIME + " DESC ;" ;
        Cursor cursor = db.rawQuery (query, null);

        String tag, amount, exin, uid, timeA, timeB;
        if (cursor.moveToFirst ()) {
            do {
                t = new Transactions ();
                tag = cursor.getString (1);
                amount = cursor.getString (0);
                timeA = cursor.getString (2);
                exin = cursor.getString (3);
                uid = cursor.getString (4);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault ());
                Date sourceDate = null;
                try {
                    sourceDate = dateFormat.parse(timeA);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm a",Locale.getDefault ());
                timeB = targetFormat.format(sourceDate);

                Log.i("PreethisTransaction",tag +" " + amount+ " " + timeB+" " + exin + " "+uid );
                t.setUid (Integer.parseInt (uid));
                t.setTag (tag);
                t.setExin (Integer.parseInt (exin));
                t.setAmount (Integer.parseInt (amount));
                t.setCreated_at (timeB);
                list.add (t);
            }while(cursor.moveToNext ());
        }
        db.close ();
        cursor.close ();
        return list;
    }

    public void setIncomeExpenses () {
        db = this.getReadableDatabase ();

        String query2 = "SELECT " + " SUM(" + COL_AMOUNT + ")" + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 1 AND " + COL_DATETIME + " > datetime('now', 'start of month');";
        Cursor c1 = db.rawQuery (query2, null);

        if (c1.moveToFirst ()) {
            String income = c1.getString (0);
            if (income != null) {
                Utils.income = Integer.parseInt (income);
                Log.i("INCOME", String.valueOf (Utils.income));
            }

        }
        c1.close ();

        String query3 = "SELECT " + " SUM(" + COL_AMOUNT + ")" + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 0 AND " + COL_DATETIME + " > datetime('now', 'start of month');";
        Cursor c2 = db.rawQuery (query3, null);

        if (c2.moveToFirst () ) {
            String expense = c2.getString (0);
            if (expense != null)
            Utils.expense = Integer.parseInt (expense);
            Log.i("EXPENSE", String.valueOf (Utils.expense));
        }
        c2.close ();
    }
    public HashMap<String, Integer> getExpenses () {

        db = this.getReadableDatabase ();

        HashMap<String, Integer> list = new HashMap<> ();

        String query = "SELECT " + " SUM(" + COL_AMOUNT + ")" + ", " + COL_TAG + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 0  AND " +  COL_DATETIME + " > datetime('now', 'start of month')" + " GROUP BY " + COL_TAG ;
        Cursor cursor = db.rawQuery (query, null);
        String tag, amount;
        if (cursor.moveToFirst ()) {
            do {
                tag = cursor.getString (1);
                amount = cursor.getString (0);
                list.put(tag, Integer.parseInt (amount));
                Log.i("PreethisExpenses",tag +" " + amount);
            }while(cursor.moveToNext ());
        }
        db.close ();
        cursor.close ();
        return list;
    }
    public ArrayList<String> getReminders () {

        db = this.getReadableDatabase ();
        ArrayList<String> list = new ArrayList<> ();
        String query = "SELECT " + COL_ALERT_TIME + ", " + COL_ALERT_MESSAGE+ " , " + COL_USER_ID  + " FROM " + TABLE_ALERTS  + " WHERE " + COL_USER_ID + " = " + Utils.userId  + " AND ALERT_TIME " + " > datetime('now')" + " ORDER BY ALERT_TIME ASC"; //+ " BETWEEN  datetime('now', 'start of day') AND datetime('now', 'localtime');";
        Cursor cursor = db.rawQuery (query, null);
        String timeA, timeB, almsg, uid;
        if (cursor.moveToFirst ()) {
            do {
                timeA = cursor.getString (0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault ());
                Date sourceDate = null;
                try {
                    sourceDate = dateFormat.parse(timeA);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm a",Locale.getDefault ());
                timeB = targetFormat.format(sourceDate);

                almsg = cursor.getString (1);
                uid = cursor.getString (2);
                list.add("\n" + almsg + "\n" + timeB + "\n");
                Log.i("PreethisAlert",timeB +" " + almsg+ " "+uid );
            }while(cursor.moveToNext ());
        }
        db.close ();
        cursor.close ();
        return list;

    }

    private static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        //String dat = "2019-04-04 12:30:30";
       return dateFormat.format(date);
       // return  dat;
    }
}