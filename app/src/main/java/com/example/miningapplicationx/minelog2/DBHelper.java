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
    /*At first you will need a Database object.Lets create it.*/
        SQLiteDatabase ourDatabase=this.getWritableDatabase();
        String KEY_ROWID="EQSHIFTID";
        String KEY_2TBL="EQSHIFTMD";
        String KEY_ONE="EQNUM";
    /*then call 'execSQL()' on it. Don't forget about using TableName Variable as tablename.*/
        ourDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TableNmae+ " ("
                + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_2TBL
                + " TEXT NOT NULL, " + KEY_ONE + " INTEGER);");
    }
}