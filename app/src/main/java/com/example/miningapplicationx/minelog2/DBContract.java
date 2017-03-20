package com.example.miningapplicationx.minelog2;
import android.provider.BaseColumns;

/**
 * Created by Ismael Tiongson on 3/2/2017.
 */

public final class DBContract {

    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE          = " TEXT NOT NULL";
    private static final String TEXT_TYPE_UNIQUE   = " TEXT UNIQUE NOT NULL";
    private static final String COMMA_SEP          = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DBContract() {}

    public static abstract class Table1 implements BaseColumns {
        public static final String TABLE_NAME       = "EQUIPMENTLOG";
        public static final String COLUMN_NAME_COL1 = "EQUIPMENTNAME";
        public static final String COLUMN_NAME_COL2 = "EQUIPMENTTYPE";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_COL1 + TEXT_TYPE_UNIQUE + COMMA_SEP +
                COLUMN_NAME_COL2 + TEXT_TYPE +  ")";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
