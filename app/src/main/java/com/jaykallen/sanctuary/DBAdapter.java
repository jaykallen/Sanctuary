package com.jaykallen.sanctuary;
// Created by Jay Kallen on 1/13/2017.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBAdapter {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Sanctuary";
    public static final String DATABASE_TABLE = "Schedule";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TIME =  "time";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_COMPLETE = "complete";
    public static final int COL_ROWID = 0;
    public static final int COL_TIME = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_COMPLETE = 4;
    public static final String[] All_KEYS = {KEY_ROWID, KEY_TIME, KEY_TITLE, KEY_IMAGE, KEY_COMPLETE};

    private Context context;
    private SQLiteDatabase db;
    private DatabaseHelper mDatabaseHelper;

    class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            String CREATE_DB_SQL = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " integer primary key autoincrement, " + KEY_TIME + " text not null, " +
                    KEY_TITLE + " text not null, " + KEY_IMAGE + " text not null, " + KEY_COMPLETE + " text not null );";
            database.execSQL(CREATE_DB_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("DROP TABLE IS EXISTS " + DATABASE_TABLE);
            onCreate(database);
        }
    }

    DBAdapter(Context context){
        this.context = context;
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public DBAdapter open(){
        db = mDatabaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDatabaseHelper.close();
    }

    public long insertRow(String time, String title, String image){
        ContentValues values = new ContentValues();
        values.put(KEY_TIME, time);
        values.put(KEY_TITLE, title);
        values.put(KEY_IMAGE, image);
        values.put(KEY_COMPLETE, "F");
        long rowid = db.insert(DATABASE_TABLE, null, values);
        return rowid;
    }

    public void initDB() {
        String mTimes[] = {"07:00", "08:30", "10:00", "11:30", "12:00", "13:00" ,"14:30", "16:00",
                "16:00", "17:30", "17:50",  "19:00", "19:30", "20:30", "21:30"};
        String mTitles[] = {"Psyllium Shake", "Herbs",
                "Psyllium Shake", "Herbs", "Pau D'Arco Tea",
                "Psyllium Shake", "Herbs",
                "Colonic", "Psyllium Shake", "Herbs", "Cucumber Juice",
                "Vegetable Broth", "Pau D'Arco Tea", "Herbs", "Probiotic", "Herbs", "Probiotic"};
        String mImages[] = {"psyllium", "herbs", "psyllium", "herbs", "paudarco", "psyllium", "herbs",
                "colonic", "psyllium","herbs", "cucumber", "veggiebroth", "paudarco", "herbs", "probiotic"};
        for (int i=0; i<15; i++) {
            long id =  insertRow(mTimes[i],mTitles[i],mImages[i]);
        }
    }

    public Cursor getAllRows(){
        Cursor cursor = db.query(DATABASE_TABLE, All_KEYS, null, null, null,null, KEY_TIME );
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteAll(){
        Cursor cursor = getAllRows();
        int rowidx = cursor.getColumnIndexOrThrow(KEY_ROWID);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                deleteRow(cursor.getLong(rowidx));
            }while (cursor.moveToNext());
        }
    }

    public boolean deleteRow(long rowid){
        String whereStr = KEY_ROWID + "=" + rowid;
        return db.delete(DATABASE_TABLE, whereStr, null)>0;
    }

    public Cursor getRow(long rowid){
        String whereStr = KEY_ROWID + "=" + rowid;
        Cursor cursor = db.query(DATABASE_TABLE, All_KEYS, whereStr, null, null,null, null );
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void updateData (String rowid, Appointment appointment, String Update) {
        ContentValues values = new ContentValues();
        values.put(KEY_ROWID, rowid);
        values.put(KEY_TIME, appointment.getTime());
        values.put(KEY_TITLE, appointment.getTitle());
        values.put(KEY_IMAGE, appointment.getImage());
        values.put(KEY_COMPLETE, Update);
        db.update(DATABASE_TABLE, values, "_id = ?", new String[]{rowid});
    }
}
