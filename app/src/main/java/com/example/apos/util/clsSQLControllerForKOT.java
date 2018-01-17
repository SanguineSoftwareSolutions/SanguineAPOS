package com.example.apos.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.util.mach.clsDBKOTHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsSQLControllerForKOT
{
    private clsDBKOTHelper dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public clsSQLControllerForKOT(Context c) {
        ourcontext = c;
    }

    public clsSQLControllerForKOT open() throws SQLException {
        dbHelper = new clsDBKOTHelper(ourcontext);
        database = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String serialNo, String tableNo,String cardNo,double dblReedemAmt, String homeDelivery,String customerCode,
        String posCode,String posName,String itemCode,String itemName, double dblItemQuantity,double dblAmount,String strWaiterNo,
        String strKOTNo, int intPaxNo,String strPrintYN,String strManualKOTNo, String strUserCreated,String strUserEdited,String dteDateCreated,
        String dteDateEdited, String strOrderBefore,String strTakeAwayYesNo,String tdhComboItemYN, String strDelBoyCode,String strNCKotYN, String strCustomerName,
        String strActiveYN, double dblBalance,double dblCreditLimit,String strCounterCode, String strPromoCode,double dblRate, String strRemark,
        String strReasonCode, String strClientCode,String strCardType ) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(clsDBKOTHelper.strSerialNo, serialNo);
        contentValue.put(clsDBKOTHelper.strTableNo, tableNo);
        contentValue.put(clsDBKOTHelper.strCardNo,cardNo);
        contentValue.put(clsDBKOTHelper.dblRedeemAmt, dblReedemAmt);
        contentValue.put(clsDBKOTHelper.strHomeDelivery, homeDelivery);
        contentValue.put(clsDBKOTHelper.strCustomerCode,customerCode);
        contentValue.put(clsDBKOTHelper.strPOSCode, posCode);
        contentValue.put(clsDBKOTHelper.strPOSName, posName);
        contentValue.put(clsDBKOTHelper.strItemCode,itemCode);
        contentValue.put(clsDBKOTHelper.strItemName, itemName);
        contentValue.put(clsDBKOTHelper.dblItemQuantity, dblItemQuantity);
        contentValue.put(clsDBKOTHelper.dblAmount,dblAmount);
        contentValue.put(clsDBKOTHelper.strWaiterNo, strWaiterNo);
        contentValue.put(clsDBKOTHelper.strKOTNo, strKOTNo);
        contentValue.put(clsDBKOTHelper.intPaxNo, intPaxNo);
        contentValue.put(clsDBKOTHelper.strPrintYN,strPrintYN);
        contentValue.put(clsDBKOTHelper.strManualKOTNo, strManualKOTNo);
        contentValue.put(clsDBKOTHelper.strUserCreated, strUserCreated);
        contentValue.put(clsDBKOTHelper.strUserEdited, strUserEdited);
        contentValue.put(clsDBKOTHelper.dteDateCreated,dteDateCreated);
        contentValue.put(clsDBKOTHelper.dteDateEdited,dteDateEdited);
        contentValue.put(clsDBKOTHelper.strOrderBefore, strOrderBefore);
        contentValue.put(clsDBKOTHelper.strTakeAwayYesNo, strTakeAwayYesNo);
        contentValue.put(clsDBKOTHelper.tdhComboItemYN, tdhComboItemYN);
        contentValue.put(clsDBKOTHelper.strDelBoyCode,strDelBoyCode);
        contentValue.put(clsDBKOTHelper.strNCKotYN, strNCKotYN);
        contentValue.put(clsDBKOTHelper.strCustomerName, strCustomerName);
        contentValue.put(clsDBKOTHelper.strActiveYN, strActiveYN);
        contentValue.put(clsDBKOTHelper.dblBalance,dblBalance);
        contentValue.put(clsDBKOTHelper.dblCreditLimit, dblCreditLimit);
        contentValue.put(clsDBKOTHelper.strCounterCode, strCounterCode);
        contentValue.put(clsDBKOTHelper.strPromoCode, strPromoCode);
        contentValue.put(clsDBKOTHelper.dblRate,dblRate);
        contentValue.put(clsDBKOTHelper.strRemark, strRemark);
        contentValue.put(clsDBKOTHelper.strReasonCode, strReasonCode);
        contentValue.put(clsDBKOTHelper.strClientCode,strClientCode);
        contentValue.put(clsDBKOTHelper.strCardType,strCardType);
        database.insert(clsDBKOTHelper.TABLE_NAME, null, contentValue);


    }

    public Cursor fetch()
    {
        String[] columns = new String[] { clsDBKOTHelper.strSerialNo, clsDBKOTHelper.strTableNo,clsDBKOTHelper.strCardNo,
                clsDBKOTHelper.dblRedeemAmt, clsDBKOTHelper.strHomeDelivery,clsDBKOTHelper.strCustomerCode,clsDBKOTHelper.strPOSCode,
                clsDBKOTHelper.strItemCode, clsDBKOTHelper.strItemName, clsDBKOTHelper.dblItemQuantity,clsDBKOTHelper.dblAmount,clsDBKOTHelper.strWaiterNo,
                clsDBKOTHelper.strKOTNo, clsDBKOTHelper.intPaxNo, clsDBKOTHelper.strPrintYN,clsDBKOTHelper.strManualKOTNo,clsDBKOTHelper.strUserCreated,
                clsDBKOTHelper.strUserEdited, clsDBKOTHelper.dteDateCreated, clsDBKOTHelper.dteDateEdited,clsDBKOTHelper.strOrderBefore,clsDBKOTHelper.strTakeAwayYesNo,
                clsDBKOTHelper.tdhComboItemYN, clsDBKOTHelper.strDelBoyCode, clsDBKOTHelper.strNCKotYN,clsDBKOTHelper.strCustomerName,clsDBKOTHelper.strActiveYN,
                clsDBKOTHelper.dblBalance, clsDBKOTHelper.dblCreditLimit, clsDBKOTHelper.strCounterCode,clsDBKOTHelper.strPromoCode,clsDBKOTHelper.dblRate,
                clsDBKOTHelper.strRemark, clsDBKOTHelper.strReasonCode, clsDBKOTHelper.strClientCode,clsDBKOTHelper.strCardType};
        Cursor cursor = database.query(clsDBKOTHelper.TABLE_NAME, columns, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String serialNo, String tableNo,String cardNo,double dblReedemAmt, String homeDelivery,String customerCode,
                      String posCode,String posName,String itemCode,String itemName, double dblItemQuantity,double dblAmount,String strWaiterNo,
                      String strKOTNo, int intPaxNo,String strPrintYN,String strManualKOTNo, String strUserCreated,String strUserEdited,String dteDateCreated,
                      String dteDateEdited, String strOrderBefore,String strTakeAwayYesNo,String tdhComboItemYN, String strDelBoyCode,String strNCKotYN, String strCustomerName,
                      String strActiveYN, double dblBalance,double dblCreditLimit,String strCounterCode, String strPromoCode,double dblRate, String strRemark,
                      String strReasonCode, String strClientCode,String strCardType)
    {
        ContentValues contentValue = new ContentValues();
        contentValue.put(clsDBKOTHelper.strSerialNo, serialNo);
        contentValue.put(clsDBKOTHelper.strTableNo, tableNo);
        contentValue.put(clsDBKOTHelper.strCardNo,cardNo);
        contentValue.put(clsDBKOTHelper.dblRedeemAmt, dblReedemAmt);
        contentValue.put(clsDBKOTHelper.strHomeDelivery, homeDelivery);
        contentValue.put(clsDBKOTHelper.strCustomerCode,customerCode);
        contentValue.put(clsDBKOTHelper.strPOSCode, posCode);
        contentValue.put(clsDBKOTHelper.strPOSName, posName);
        contentValue.put(clsDBKOTHelper.strItemCode,itemCode);
        contentValue.put(clsDBKOTHelper.strItemName, itemName);
        contentValue.put(clsDBKOTHelper.dblItemQuantity, dblItemQuantity);
        contentValue.put(clsDBKOTHelper.dblAmount,dblAmount);
        contentValue.put(clsDBKOTHelper.strWaiterNo, strWaiterNo);
        contentValue.put(clsDBKOTHelper.strKOTNo, strKOTNo);
        contentValue.put(clsDBKOTHelper.intPaxNo, intPaxNo);
        contentValue.put(clsDBKOTHelper.strPrintYN,strPrintYN);
        contentValue.put(clsDBKOTHelper.strManualKOTNo, strManualKOTNo);
        contentValue.put(clsDBKOTHelper.strUserCreated, strUserCreated);
        contentValue.put(clsDBKOTHelper.strUserEdited, strUserEdited);
        contentValue.put(clsDBKOTHelper.dteDateCreated,dteDateCreated);
        contentValue.put(clsDBKOTHelper.dteDateEdited,dteDateEdited);
        contentValue.put(clsDBKOTHelper.strOrderBefore, strOrderBefore);
        contentValue.put(clsDBKOTHelper.strTakeAwayYesNo, strTakeAwayYesNo);
        contentValue.put(clsDBKOTHelper.tdhComboItemYN, tdhComboItemYN);
        contentValue.put(clsDBKOTHelper.strDelBoyCode,strDelBoyCode);
        contentValue.put(clsDBKOTHelper.strNCKotYN, strNCKotYN);
        contentValue.put(clsDBKOTHelper.strCustomerName, strCustomerName);
        contentValue.put(clsDBKOTHelper.strActiveYN, strActiveYN);
        contentValue.put(clsDBKOTHelper.dblBalance,dblBalance);
        contentValue.put(clsDBKOTHelper.dblCreditLimit, dblCreditLimit);
        contentValue.put(clsDBKOTHelper.strCounterCode, strCounterCode);
        contentValue.put(clsDBKOTHelper.strPromoCode, strPromoCode);
        contentValue.put(clsDBKOTHelper.dblRate,dblRate);
        contentValue.put(clsDBKOTHelper.strRemark, strRemark);
        contentValue.put(clsDBKOTHelper.strReasonCode, strReasonCode);
        contentValue.put(clsDBKOTHelper.strClientCode,strClientCode);
        contentValue.put(clsDBKOTHelper.strCardType,strCardType);
        int i = database.update(clsDBKOTHelper.TABLE_NAME, contentValue,
                clsDBKOTHelper._ID + " = " + _id, null);


        return i;
    }


    public List<clsKOTItemDtlBean>  funFetchData()
    {
        List<clsKOTItemDtlBean> arrListKOTItems=new ArrayList<clsKOTItemDtlBean>();;
        arrListKOTItems=getAppCategoryDetail();
        return arrListKOTItems;


    }

    public List<clsKOTItemDtlBean> getAppCategoryDetail() {

        final String TABLE_NAME = "tblkottemp";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = dbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        List<clsKOTItemDtlBean> arrListKOTItems=new ArrayList<clsKOTItemDtlBean>();

        if (cursor.moveToFirst())
        {
            do
            {

              clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3),cursor.getString(4),
                        cursor.getString(5),cursor.getString(6),cursor.getString(8) ,cursor.getString(9),cursor.getDouble(10),cursor.getDouble(11),cursor.getString(12),
                        cursor.getString(13),cursor.getInt(14),cursor.getString(15),cursor.getString(16),cursor.getString(17), cursor.getString(18), cursor.getString(19),
                        cursor.getString(20),cursor.getString(21),cursor.getString(22), cursor.getString(23), cursor.getString(24), cursor.getString(25), cursor.getString(26),
                        cursor.getString(27),cursor.getString(28),cursor.getString(29),cursor.getString(30), cursor.getString(31),false, cursor.getDouble(33), cursor.getString(34));
                arrListKOTItems.add(objItemDtl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrListKOTItems;
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
       // database.delete(clsDBKOTHelper.TABLE_NAME, clsDBKOTHelper._ID + "=" + _id, null);
    }
}