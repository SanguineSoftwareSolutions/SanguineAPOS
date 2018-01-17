package com.example.apos.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

public class clsSQLController
{
    private clsDBHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public clsSQLController(Context c) {
        ourcontext = c;
    }

    public clsSQLController open() throws SQLException {
        dbHelper = new clsDBHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }


    public void insert(String url, String kot,String bill) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(clsDBHelper.Config_Url, url);
        contentValue.put(clsDBHelper.Config_Kot, kot);
        contentValue.put(clsDBHelper.Config_Bill,bill);
        database.insert(clsDBHelper.TABLE_NAME, null, contentValue);
    }


    public String funFetchData()
    {
        String data="";
        Cursor cursor = database.rawQuery("SELECT * FROM tblconfig", null);

        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(1)+"#"+cursor.getString(2)+"#"+cursor.getString(3);
            }
            while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return data;
    }


    public int funDeleteQuery(String sql)
    {
        database.execSQL(sql);
        return 1;
    }

}