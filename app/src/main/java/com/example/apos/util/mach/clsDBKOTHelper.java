package com.example.apos.util.mach;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.apos.activity.clsGlobalFunctions;

public class clsDBKOTHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "tblkottemp";

    // Table columns
    public static final String _ID = "_id";
    public static final String strSerialNo= "serialNo";
    public static final String strTableNo = "tableNo";
    public static final String strCardNo = "cardNo";
    public static final String dblRedeemAmt= "reedemAmt";
    public static final String strHomeDelivery = "homeDelivery";
    public static final String strCustomerCode = "customerCode";
    public static final String strPOSCode= "posCode";
    public static final String strPOSName = "posName";
    public static final String strItemCode = "itemCode";
    public static final String strItemName = "itemName";
    public static final String dblItemQuantity= "quantity";
    public static final String dblAmount ="amount";
    public static final String strWaiterNo = "waiterNo";
    public static final String strKOTNo = "kotNo";
    public static final String intPaxNo = "pax";
    public static final String strPrintYN = "printYN";
    public static final String strManualKOTNo = "manualKOTNo";
    public static final String strUserCreated = "userCreated";
    public static final String strUserEdited = "userEdited";
    public static final String dteDateCreated = "dteCreated";
    public static final String dteDateEdited = "dteEdited";
    public static final String strOrderBefore = "orderBefore";
    public static final String strTakeAwayYesNo = "takeAwayYN";
    public static final String tdhComboItemYN = "comboItemYN";
    public static final String strDelBoyCode = "delBoyCode";
    public static final String strNCKotYN = "ncKOTYN";
    public static final String strCustomerName = "customerName";
    public static final String strActiveYN = "activeYN";
    public static final String dblBalance = "balance";
    public static final String dblCreditLimit = "creditLimit";
    public static final String strCounterCode = "counterCode";
    public static final String strPromoCode = "promoCode";
    public static final String dblRate = "rate";
    public static final String strRemark = "remark";
    public static final String strReasonCode = "reasonCode";
    public static final String strClientCode = "clientCode";
    public static final String strCardType = "cardType";



    // Database Information
    static final String DB_NAME = "KOT.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query

    String Create_table = "CREATE TABLE tblkottemp (_id INTEGER PRIMARY KEY AUTOINCREMENT,strSerialNo TEXT NOT NULL,strTableNo TEXT NOT NULL,strCardNo TEXT NOT NULL,dblRedeemAmt REAL, " +
            "strHomeDelivery TEXT NOT NULL,strCustomerCode TEXT NOT NULL,strPOSCode TEXT NOT NULL,strPOSName TEXT NOT NULL,strItemCode TEXT NOT NULL," +
            "strItemName TEXT NOT NULL,dblItemQuantity REAL,dblAmount REAL,strWaiterNo TEXT NOT NULL,strKOTNo TEXT NOT NULL,intPaxNo INTEGER," +
            "strPrintYN TEXT NOT NULL,strManualKOTNo TEXT NOT NULL,strUserCreated TEXT NOT NULL,strUserEdited TEXT NOT NULL,dteDateCreated TEXT NOT NULL," +
            "dteDateEdited TEXT NOT NULL,strOrderBefore TEXT NOT NULL,strTakeAwayYesNo TEXT NOT NULL,tdhComboItemYN TEXT NOT NULL,strDelBoyCode TEXT NOT NULL, " +
            "strNCKotYN TEXT NOT NULL,strCustomerName TEXT NOT NULL,strActiveYN TEXT NOT NULL,dblBalance REAL,dblCreditLimit REAL,strCounterCode TEXT NOT NULL," +
            "strPromoCode TEXT NOT NULL,dblRate REAL,strRemark TEXT NOT NULL,strReasonCode TEXT NOT NULL,strClientCode TEXT NOT NULL,strCardType TEXT NOT NULL)";
    public clsDBKOTHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Create_table);
        Log.v("INFO1","creating db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}