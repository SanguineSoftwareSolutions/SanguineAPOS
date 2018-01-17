package com.example.apos.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class clsDBHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "tblconfig";

    // Table columns

    public static final String _ID = "_id";
    public static final String Config_Url= "url";
    public static final String Config_Kot = "kot";
    public static final String Config_Bill = "bill";
    // Database Information
    static final String DB_NAME = "Config.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Config_Url + " TEXT NOT NULL, " + Config_Kot + " TEXT," + Config_Bill + " TEXT);";

    public clsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}