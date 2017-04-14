package com.example.miningapplicationx.minelog2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ismael Tiongson on 3/2/2017.
 */public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.Table1.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.Table1.DELETE_TABLE);
        onCreate(db);
    }
    public void AddDesiredTable(String TableNmae){
        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String KEY_ROWID="EQSHIFTID";
        String KEY_2TBL="EQSHIFTMD";
        String KEY_ONE="EQNUM";
        ourDatabase.execSQL("CREATE TABLE IF NOT EXISTS '" + TableNmae+ "' ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_2TBL
                + " TEXT NOT NULL, " + KEY_ONE + " INTEGER, " + "UNIQUE("+KEY_2TBL+"));");
    }
    public void AddActivityLog(String TableNmae){
        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String KEY_ROWID="AID";
        String KEY_2TBL="ACTIVITY";
        String KEY_ONE="TIME";
        String KEY_TWO="TYPE";
        String KEY_THREE="OPERATOR";
        ourDatabase.execSQL("CREATE TABLE IF NOT EXISTS '" + TableNmae+ "' ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_2TBL
                + " TEXT NOT NULL, " + KEY_ONE + " TEXT NOT NULL, " +KEY_TWO + " TEXT NOT NULL, " +  KEY_THREE +  " TEXT NOT NULL);");
    }
    public void AddActivityList(String TableNmae){
        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String KEY_ROWID="AID";
        String KEY_2TBL="ACTIVITY";
        String KEY_ONE="TYPE";
        ourDatabase.execSQL("CREATE TABLE IF NOT EXISTS '" + TableNmae+ "' ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_2TBL
                + " TEXT NOT NULL, " + KEY_ONE +  " TEXT NOT NULL, " +
                "UNIQUE("+KEY_2TBL+"));");
    }
    public void UsernameTables(){

        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String KEY_ROWID="ID";
        String KEY_2TBL="USERNAME";
        String KEY_ONE="PASSWORD";
        String KEY_TWO="TYPE";
        ourDatabase.execSQL("CREATE TABLE IF NOT EXISTS 'USERNAMETABLES' ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_2TBL
                + " TEXT NOT NULL, " + KEY_ONE +  " TEXT NOT NULL, "+KEY_TWO + " TEXT NOT NULL, "+
                "UNIQUE("+KEY_2TBL+"));");

    }
}