package com.example.apos.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsKotMenuItemsBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsSQLController
{
    private clsDBhelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public clsSQLController(Context c) {
        ourcontext = c;
    }

    public clsSQLController open() throws SQLException {
        dbHelper = new clsDBhelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String url, String kot,String bill) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(clsDBhelper.Config_Url, url);
        contentValue.put(clsDBhelper.Config_Kot, kot);
        contentValue.put(clsDBhelper.Config_Bill,bill);
        database.insert(clsDBhelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { clsDBhelper._ID, clsDBhelper.Config_Url, clsDBhelper.Config_Kot,
                clsDBhelper.Config_Bill };
        Cursor cursor = database.query(clsDBhelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String url,String name, String printerName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(clsDBhelper.Config_Url, url);
        contentValues.put(clsDBhelper.Config_Kot, name);
        contentValues.put(clsDBhelper.Config_Bill, printerName);
        int i = database.update(clsDBhelper.TABLE_NAME, contentValues,
                clsDBhelper._ID + " = " + _id, null);


        return i;
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

    public int funInsertQuery(String sql)
    {
        database.execSQL(sql);
        return 1;
    }


    public void delete(long _id) {
        database.delete(clsDBhelper.TABLE_NAME, clsDBhelper._ID + "=" + _id, null);
    }




    public List<clsKotMenuItemsBean> getWebServieURLList()
    {

        final String TABLE_NAME = "tblconfig";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = dbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        List<clsKotMenuItemsBean> arrListKOTItems=new ArrayList<clsKotMenuItemsBean>();

        if (cursor.moveToFirst())
        {
            do
            {
                clsKotMenuItemsBean objUrlDtl=new clsKotMenuItemsBean();
                objUrlDtl.setStrMenuItemName(cursor.getString(1));//url with ip address
                objUrlDtl.setStrMenuType(cursor.getString(2));  //kot printer name
                objUrlDtl.setStrItemImage(cursor.getString(3)); //bill printer name
                arrListKOTItems.add(objUrlDtl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrListKOTItems;
    }

}