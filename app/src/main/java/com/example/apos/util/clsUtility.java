package com.example.apos.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.example.apos.activity.clsBillSettlement;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsMakeBillScreen;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.util.mach.clsPrintFormatAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Prashant on 12/1/2015.
 */
public class clsUtility {

    private String amtInWords="";

    public String funGetAmtInWords(long amt)
    {
        funDisplayAMtInWords(amt);
        return amtInWords;
    }


    private int funDisplayAMtInWords(long amt)
    {
        String strAmt=String.valueOf(amt);

        switch(strAmt.length())
        {
            case 1:
                amtInWords=amtInWords+" "+funZeroToNintyNineWords(amt);
                break;

            case 2:
                amtInWords=amtInWords+" "+funZeroToNintyNineWords(amt);
                break;

            case 3:
                long res=amt/100;
                if(res>0)
                {
                    String text = funZeroToNintyNineWords(res);
                    amtInWords=amtInWords+" "+text+" Hundred";
                    long rem=amt%100;
                    funDisplayAMtInWords(rem);
                }
                break;

            case 4:
                long res1=amt/1000;
                if(res1>0)
                {
                    String text = funZeroToNintyNineWords(res1);
                    amtInWords=amtInWords+" "+text+" Thousand";
                    long rem=amt%1000;
                    funDisplayAMtInWords(rem);
                }
                break;

            case 5:
                long res2=amt/1000;
                if(res2>0)
                {
                    String text = funZeroToNintyNineWords(res2);
                    amtInWords=amtInWords+" "+text+" Thousand";
                    long rem=amt%1000;
                    funDisplayAMtInWords(rem);
                }
                break;

            case 6:
                long res3=amt/100000;
                if(res3>0)
                {
                    String text = funZeroToNintyNineWords(res3);
                    amtInWords=amtInWords+" "+text+" Lac";
                    long rem=amt%100000;
                    funDisplayAMtInWords(rem);
                }
                break;

            case 7:
                long res4=amt/100000;
                if(res4>0)
                {
                    String text = funZeroToNintyNineWords(res4);
                    amtInWords=amtInWords+" "+text+" Lac";
                    long rem=amt%100000;
                    funDisplayAMtInWords(rem);
                }
                break;
        }

        //System.out.println(amtInWords);

        return 1;
    }


    private String funZeroToNintyNineWords(long index)
    {
        String words="";
        int ind=(int)index;
        switch(ind)
        {
            case 1:
                words="One";
                break;

            case 2:
                words="Two";
                break;

            case 3:
                words="Three";
                break;

            case 4:
                words="Four";
                break;

            case 5:
                words="Five";
                break;

            case 6:
                words="Six";
                break;

            case 7:
                words="Seven";
                break;

            case 8:
                words="Eight";
                break;

            case 9:
                words="Nine";
                break;

            case 10:
                words="Ten";
                break;

            case 11:
                words="Eleven";
                break;

            case 12:
                words="Twelve";
                break;

            case 13:
                words="Thirteen";
                break;

            case 14:
                words="Fourteen";
                break;

            case 15:
                words="Fifteen";
                break;

            case 16:
                words="Sixteen";
                break;

            case 17:
                words="Seventeen";
                break;

            case 18:
                words="Eighteen";
                break;

            case 19:
                words="Nineteen";
                break;

            case 20:
                words="Twenty";
                break;

            case 21:
                words="Twenty One";
                break;

            case 22:
                words="Twenty Two";
                break;

            case 23:
                words="Twenty Three";
                break;

            case 24:
                words="Twenty Four";
                break;

            case 25:
                words="Twenty Five";
                break;

            case 26:
                words="Twenty Six";
                break;

            case 27:
                words="Twenty Seven";
                break;

            case 28:
                words="Twenty Eight";
                break;

            case 29:
                words="Twenty Nine";
                break;

            case 30:
                words="Thirty";
                break;

            case 31:
                words="Thirty One";
                break;

            case 32:
                words="Thirty Two";
                break;

            case 33:
                words="Thirty Three";
                break;

            case 34:
                words="Thirty Four";
                break;

            case 35:
                words="Thirty Five";
                break;

            case 36:
                words="Thirty Six";
                break;

            case 37:
                words="Thirty Seven";
                break;

            case 38:
                words="Thirty Eight";
                break;

            case 39:
                words="Thirty Nine";
                break;

            case 40:
                words="Fourty";
                break;

            case 41:
                words="Fourty One";
                break;

            case 42:
                words="Fourty Two";
                break;

            case 43:
                words="Fourty Three";
                break;

            case 44:
                words="Fourty Four";
                break;

            case 45:
                words="Fourty Five";
                break;

            case 46:
                words="Fourty Six";
                break;

            case 47:
                words="Fourty Seven";
                break;

            case 48:
                words="Fourty Eight";
                break;

            case 49:
                words="Fourty Nine";
                break;

            case 50:
                words="Fifty";
                break;

            case 51:
                words="Fifty One";
                break;

            case 52:
                words="Fifty Two";
                break;

            case 53:
                words="Fifty Three";
                break;

            case 54:
                words="Fifty Four";
                break;

            case 55:
                words="Fifty Five";
                break;

            case 56:
                words="Fifty Six";
                break;

            case 57:
                words="Fifty Seven";
                break;

            case 58:
                words="Fifty Eight";
                break;

            case 59:
                words="Fifty Nine";
                break;

            case 60:
                words="Sixty";
                break;

            case 61:
                words="Sixty One";
                break;

            case 62:
                words="Sixty Two";
                break;

            case 63:
                words="Sixty Three";
                break;

            case 64:
                words="Sixty Four";
                break;

            case 65:
                words="Sixty Five";
                break;

            case 66:
                words="Sixty Six";
                break;

            case 67:
                words="Sixty Seven";
                break;

            case 68:
                words="Sixty Eight";
                break;

            case 69:
                words="Sixty Nine";
                break;

            case 70:
                words="Seventy";
                break;

            case 71:
                words="Seventy One";
                break;

            case 72:
                words="Seventy Two";
                break;

            case 73:
                words="Seventy Three";
                break;

            case 74:
                words="Seventy Four";
                break;

            case 75:
                words="Seventy Five";
                break;

            case 76:
                words="Seventy Six";
                break;

            case 77:
                words="Seventy Seven";
                break;

            case 78:
                words="Seventy Eight";
                break;

            case 79:
                words="Seventy Nine";
                break;

            case 80:
                words="Eighty";
                break;

            case 81:
                words="Eighty One";
                break;

            case 82:
                words="Eighty Two";
                break;

            case 83:
                words="Eighty Three";
                break;

            case 84:
                words="Eighty Four";
                break;

            case 85:
                words="Eighty Five";
                break;

            case 86:
                words="Eighty Six";
                break;

            case 87:
                words="Eighty Seven";
                break;

            case 88:
                words="Eighty Eight";
                break;

            case 89:
                words="Eighty Nine";
                break;

            case 90:
                words="Ninety";
                break;

            case 91:
                words="Ninety One";
                break;

            case 92:
                words="Ninety Two";
                break;

            case 93:
                words="Ninety Three";
                break;

            case 94:
                words="Ninety Four";
                break;

            case 95:
                words="Ninety Five";
                break;

            case 96:
                words="Ninety Six";
                break;

            case 97:
                words="Ninety Seven";
                break;

            case 98:
                words="Ninety Eight";
                break;

            case 99:
                words="Ninety Nine";
                break;
        }
        return words;
    }



    public void funPrintBillFormatWise(JSONObject jObj,String printType)
    {
//        if(clsGlobalFunctions.gBillFormatType.equals("Format 1"))
//        {
//            funPrintBillFormat1(jObj,printType);
//        }
//        else if(clsGlobalFunctions.gBillFormatType.equals("Format 7"))
//        {
//            funPrintBillFormat7(jObj,printType);
//        }
//        else if(clsGlobalFunctions.gBillFormatType.equals("Format 8"))
//        {
//            funPrintBillFormat8(jObj,printType);
//        }
//        else
//        {
//            funPrintBillFormat1(jObj,printType);
//        }

        funPrintBillFormat1(jObj,printType);

    }



    // For All Clients
    private void funPrintBillFormat1(JSONObject jObj,String printType)
    {
        try {

            JSONArray jsonArrBillHd = jObj.getJSONArray("BillHdInfo");

            String line = "----------------------------------------";
            StringBuilder sbPrintBill = new StringBuilder();

            clsPrintFormatAPI objPrint = new clsPrintFormatAPI();

            sbPrintBill.append(objPrint.funGetStringWithAlignment("INVOICE", "Center", 40));

            if(clsGlobalFunctions.gClientName.length()>40)
            {
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName.substring(0, 30), "Center", 40));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName.substring(30), "Center", 40));
            }
            else
            {
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Center", 40));
            }


            sbPrintBill.append("\n");

            JSONObject jObjBillHd = jsonArrBillHd.getJSONObject(0);

            // Bill header level details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill No : " + jObjBillHd.get("BillNo").toString(), "Left", 20));
            String billDate = jObjBillHd.get("BillDate").toString();
            String []arrBillDate=billDate.split(" ");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Date : " + arrBillDate[0], "Left", 20));
            sbPrintBill.append("\n");
            String tableName=jObjBillHd.get("Table").toString();
            if(!tableName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Table : " + tableName, "Left", 20));
                String waiterName=jObjBillHd.get("Waiter").toString();
                if(!waiterName.isEmpty())
                {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("Waiter : " + waiterName, "Left", 20));
                }
                sbPrintBill.append("\n");
            }

            String PAX=jObjBillHd.get("PAX").toString();
            if(!PAX.isEmpty())
            {
                if(Integer.parseInt(PAX)>0) {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("PAX : " + PAX, "Left", 40));
                    sbPrintBill.append("\n");
                }
            }
            String user=jObjBillHd.get("User").toString();
            if(!user.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Cashier : " + user, "Left", 40));
                sbPrintBill.append("\n");
            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));



            // Item Level Details
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("ITEM NAME ","Left", 40));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("     QTY         RATE             AMOUNT","Left", 40));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));


            JSONArray jsonArrBillDtl = jObj.getJSONArray("BillDtlInfo");
            for (int i = 0; i < jsonArrBillDtl.length(); i++) {
                JSONObject jObjItemDtl = (JSONObject) jsonArrBillDtl.get(i);
                String itemName = jObjItemDtl.get("ItemName").toString();
                String itemQty = jObjItemDtl.get("Quantity").toString();
                String itemRate = jObjItemDtl.get("Rate").toString();
                String totalAmount = jObjItemDtl.get("Amount").toString();



                if(itemName.length()>40)
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName.substring(40,itemName.length()), "Left", 40));
                }
                else
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                }
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(" " + itemQty + " ", "Right", 9));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate + " ", "Right", 13));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount + " ", "Right", 19));
            }

            JSONArray jsonArrModBillDtl = jObj.getJSONArray("ModBillDtlInfo");
            if(jsonArrModBillDtl.length()>0)
            {
                for (int i = 0; i < jsonArrModBillDtl.length(); i++) {
                    JSONObject jObjItemDtl = (JSONObject) jsonArrModBillDtl.get(i);
                    String itemName = jObjItemDtl.get("ModItemName").toString();
                    String itemQty = jObjItemDtl.get("Quantity").toString();
                    String itemRate = jObjItemDtl.get("Rate").toString();
                    String totalAmount = jObjItemDtl.get("Amount").toString();

//                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 18));
//                    // sbPrintBill.append("\n");
//                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 7));
//                    // sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 10));
//                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
//                    sbPrintBill.append("\n");



                    if(itemName.length()>40)
                    {
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName.substring(40,itemName.length()), "Left", 40));
                    }
                    else
                    {
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                    }
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(" " + itemQty + " ", "Right", 9));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate + " ", "Right", 13));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount + " ", "Right", 19));
                }


            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
            sbPrintBill.append("\n");

            // Sub Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Sub Total   : ", "Left", 30));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("SubTotal").toString(), "Right", 10));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
            sbPrintBill.append("\n");


            // TaxLevel Details
            JSONArray jsonArrBillTaxDtl = jObj.getJSONArray("BillTaxDtlInfo");
            boolean flgTaxDtl = false;
            for (int i = 0; i < jsonArrBillTaxDtl.length(); i++) {
                JSONObject jObjTaxDtl = (JSONObject) jsonArrBillTaxDtl.get(i);
                String taxDesc = jObjTaxDtl.get("TaxDesc").toString();
                String taxAmt = jObjTaxDtl.get("TaxAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxDesc, "Left", 30));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxAmt, "Right", 10));
                sbPrintBill.append("\n");
                flgTaxDtl = true;
            }
            if (flgTaxDtl) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
                sbPrintBill.append("\n");
            }

            // Grand Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Total       : ", "Left", 30));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("GrandTotal").toString(), "Right", 10));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
            sbPrintBill.append("\n");

            // Bill Settlement Details
            boolean flgBillSettlement=false;
            JSONArray jsonArrBillSettlementDtl = jObj.getJSONArray("BillSettleDtlInfo");
            for (int i = 0; i < jsonArrBillSettlementDtl.length(); i++) {
                JSONObject jObjSettlementDtl = (JSONObject) jsonArrBillSettlementDtl.get(i);
                String settlementDesc = jObjSettlementDtl.get("SettlementDesc").toString();
                String settlementAmt = jObjSettlementDtl.get("SettlementAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementDesc, "Left", 30));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementAmt, "Right", 10));
                sbPrintBill.append("\n");
                flgBillSettlement=true;
            }

            if(flgBillSettlement) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
                sbPrintBill.append("\n");
            }

            /*
            JSONObject jsonObjCardBalance = jObj.getJSONObject("CardDetails");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Card Balance : ", "Left", 20));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jsonObjCardBalance.get("Balance").toString(), "Right", 12));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");*/

            sbPrintBill=funPrintVatAndServiceTaxNo(sbPrintBill);

            // Member Details

            boolean flgMemberDtls=false;
            if (!jObjBillHd.get("MemCode").toString().isEmpty()) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Code   : ", "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemCode").toString(), "Left", 20));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Name   : ", "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemName").toString(), "Left", 20));
                sbPrintBill.append("\n");
                flgMemberDtls=true;
            }

            sbPrintBill.append(objPrint.funGetStringWithAlignment("(INCLUSIVE OF ALL TAXES)", "Left",40));

            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");



            if(printType.equals("makeBill"))
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))   // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
                }
                else //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    clsMakeBillScreen.funPrintBillForMachDevice(sbPrintBill.toString());

                }


            }
            else //To print bill on Mach Device
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))    // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
                }
                else  //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                  //  new clsBillSettlement().funPrintBillForMachDevice(sbPrintBill.toString());
                }

            }

            // Function to print bill on POS Gold Device.
            //funPrintBillForPOSGoldDevice(sbPrintBill.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // For Format 7
    private void funPrintBillFormat7(JSONObject jObj,String printType) // Boat Club
    {
        try {

            JSONArray jsonArrBillHd = jObj.getJSONArray("BillHdInfo");

            String line = "--------------------------------";
            StringBuilder sbPrintBill = new StringBuilder();

            clsPrintFormatAPI objPrint = new clsPrintFormatAPI();

            sbPrintBill.append(objPrint.funGetStringWithAlignment("INVOICE", "Left", 32));
            sbPrintBill.append("\n");

            /*sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 32));
            sbPrintBill.append("\n");*/

            JSONObject jObjBillHd = jsonArrBillHd.getJSONObject(0);

            if(clsGlobalFunctions.gClientCode.equals("047.001") )
            {
                if(clsGlobalFunctions.gPOSCode.equals("P02") || clsGlobalFunctions.gPOSCode.equals("P03"))
                {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("SHRI SHAM CATERERS", "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("Flat No.7, Mon Amour,", "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("Thorat Colony,Prabhat Road,", "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(" Erandwane, Pune 411 004.", "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("Approved Caterers of", "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("ROYAL CONNAUGHT BOAT CLUB", "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                    sbPrintBill.append("\n");
                }
                else
                {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                    sbPrintBill.append("\n");
                }
            }

            // Bill header level details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill No   : " + jObjBillHd.get("BillNo").toString(), "Left", 32));
            sbPrintBill.append("\n");
            String billDate = jObjBillHd.get("BillDate").toString();
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill Date : " + billDate, "Left", 32));
            sbPrintBill.append("\n");
            String tableName=jObjBillHd.get("Table").toString();
            if(!tableName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Table : " + tableName, "Left", 32));
                sbPrintBill.append("\n");
            }
            String waiterName=jObjBillHd.get("Waiter").toString();
            if(!waiterName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Waiter : " + waiterName, "Left", 32));
                sbPrintBill.append("\n");
            }
            String PAX=jObjBillHd.get("PAX").toString();
            if(!PAX.isEmpty())
            {
                if(Integer.parseInt(PAX)>0) {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("PAX : " + PAX, "Left", 32));
                    sbPrintBill.append("\n");
                }
            }
            String user=jObjBillHd.get("User").toString();
            if(!user.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Cashier : " + user, "Left", 32));
                sbPrintBill.append("\n");
            }

            // Member Details


            boolean flgMemberDtls=false;
            if (!jObjBillHd.get("MemCode").toString().isEmpty()) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Code   : ", "Left", 16));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemCode").toString(), "Left", 16));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Name   : ", "Left", 16));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemName").toString(), "Left", 16));
                sbPrintBill.append("\n");
                flgMemberDtls=true;
            }

            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Item Level Details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("QTY       Rate      TOTAL     \n", "Left", 32));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");
            JSONArray jsonArrBillDtl = jObj.getJSONArray("BillDtlInfo");
            for (int i = 0; i < jsonArrBillDtl.length(); i++) {
                JSONObject jObjItemDtl = (JSONObject) jsonArrBillDtl.get(i);
                String itemName = jObjItemDtl.get("ItemName").toString();
                String itemQty = jObjItemDtl.get("Quantity").toString();
                String itemRate = jObjItemDtl.get("Rate").toString();
                String totalAmount = jObjItemDtl.get("Amount").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 32));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 10));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 10));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 10));
                sbPrintBill.append("\n");
            }

            JSONArray jsonArrModBillDtl = jObj.getJSONArray("ModBillDtlInfo");
            if(jsonArrModBillDtl.length()>0)
            {
                for (int i = 0; i < jsonArrModBillDtl.length(); i++) {
                    JSONObject jObjItemDtl = (JSONObject) jsonArrModBillDtl.get(i);
                    String itemName = jObjItemDtl.get("ModItemName").toString();
                    String itemQty = jObjItemDtl.get("Quantity").toString();
                    //String itemRate = jObjItemDtl.get("Rate").toString();
                    String totalAmount = jObjItemDtl.get("Amount").toString();
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 18));
                    // sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 7));
                    // sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 10));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
                    sbPrintBill.append("\n");
                }


            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Sub Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Sub Total   : ", "Left", 14));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("SubTotal").toString(), "Right", 18));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");


            // TaxLevel Details
            JSONArray jsonArrBillTaxDtl = jObj.getJSONArray("BillTaxDtlInfo");
            boolean flgTaxDtl = false;
            for (int i = 0; i < jsonArrBillTaxDtl.length(); i++) {
                JSONObject jObjTaxDtl = (JSONObject) jsonArrBillTaxDtl.get(i);
                String taxDesc = jObjTaxDtl.get("TaxDesc").toString();
                String taxAmt = jObjTaxDtl.get("TaxAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxDesc, "Left", 20));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxAmt, "Right", 12));
                sbPrintBill.append("\n");
                flgTaxDtl = true;
            }
            if (flgTaxDtl) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                sbPrintBill.append("\n");
            }

            // Grand Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Total       : ", "Left", 14));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("GrandTotal").toString(), "Right", 18));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Bill Settlement Details
            boolean flgBillSettlement=false;
            JSONArray jsonArrBillSettlementDtl = jObj.getJSONArray("BillSettleDtlInfo");
            for (int i = 0; i < jsonArrBillSettlementDtl.length(); i++) {
                JSONObject jObjSettlementDtl = (JSONObject) jsonArrBillSettlementDtl.get(i);
                String settlementDesc = jObjSettlementDtl.get("SettlementDesc").toString();
                String settlementAmt = jObjSettlementDtl.get("SettlementAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementDesc, "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementAmt, "Right", 12));
                sbPrintBill.append("\n");
                flgBillSettlement=true;
            }

            if(flgBillSettlement) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                sbPrintBill.append("\n");
            }

            /*
            JSONObject jsonObjCardBalance = jObj.getJSONObject("CardDetails");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Card Balance : ", "Left", 20));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jsonObjCardBalance.get("Balance").toString(), "Right", 12));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");*/

            sbPrintBill=funPrintVatAndServiceTaxNo(sbPrintBill);
            sbPrintBill.append(objPrint.funGetStringWithAlignment("(INCLUSIVE OF ALL TAXES)", "Left", 32));

            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");


            if(printType.equals("makeBill"))
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))   // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
                }
                else //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    clsMakeBillScreen.funPrintBillForMachDevice(sbPrintBill.toString());

                }


            }
            else //To print bill on Mach Device
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))    // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
                }
                else  //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                  // new clsBillSettlement().funPrintBillForMachDevice(sbPrintBill.toString());
                }

            }
            // Function to print bill on POS Gold Device.
            //funPrintBillForPOSGoldDevice(sbPrintBill.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    // Bill Format For All Clients with 'Rate' columnname
    private void funPrintBillFormat11(JSONObject jObj,String printType)
    {
        try {

            JSONArray jsonArrBillHd = jObj.getJSONArray("BillHdInfo");

            String line = "--------------------------------";
            StringBuilder sbPrintBill = new StringBuilder();

            clsPrintFormatAPI objPrint = new clsPrintFormatAPI();

            sbPrintBill.append(objPrint.funGetStringWithAlignment("INVOICE", "Left", 32));
            sbPrintBill.append("\n");

            sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 32));
            sbPrintBill.append("\n");

            JSONObject jObjBillHd = jsonArrBillHd.getJSONObject(0);

            // Bill header level details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill No   : " + jObjBillHd.get("BillNo").toString(), "Left", 32));
            sbPrintBill.append("\n");
            String billDate = jObjBillHd.get("BillDate").toString();
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill Date : " + billDate, "Left", 32));
            sbPrintBill.append("\n");
            String tableName=jObjBillHd.get("Table").toString();
            if(!tableName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Table : " + tableName, "Left", 32));
                sbPrintBill.append("\n");
            }
            String waiterName=jObjBillHd.get("Waiter").toString();
            if(!waiterName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Waiter : " + waiterName, "Left", 32));
                sbPrintBill.append("\n");
            }
            String PAX=jObjBillHd.get("PAX").toString();
            if(!PAX.isEmpty())
            {
                if(Integer.parseInt(PAX)>0) {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("PAX : " + PAX, "Left", 32));
                    sbPrintBill.append("\n");
                }
            }
            String user=jObjBillHd.get("User").toString();
            if(!user.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Cashier : " + user, "Left", 32));
                sbPrintBill.append("\n");
            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Item Level Details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("ItemName         QTY    Rate    Amount  \n", "Left", 32));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");
            JSONArray jsonArrBillDtl = jObj.getJSONArray("BillDtlInfo");
            for (int i = 0; i < jsonArrBillDtl.length(); i++) {
                JSONObject jObjItemDtl = (JSONObject) jsonArrBillDtl.get(i);
                String itemName = jObjItemDtl.get("ItemName").toString();
                String itemQty = jObjItemDtl.get("Quantity").toString();
                String itemRate = jObjItemDtl.get("Rate").toString();
                String totalAmount = jObjItemDtl.get("Amount").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left",15));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 5));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 5));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
                sbPrintBill.append("\n");
            }

            JSONArray jsonArrModBillDtl = jObj.getJSONArray("ModBillDtlInfo");
            if(jsonArrModBillDtl.length()>0)
            {
                for (int i = 0; i < jsonArrModBillDtl.length(); i++) {
                    JSONObject jObjItemDtl = (JSONObject) jsonArrModBillDtl.get(i);
                    String itemName = jObjItemDtl.get("ModItemName").toString();
                    String itemQty = jObjItemDtl.get("Quantity").toString();
                    String itemRate = jObjItemDtl.get("Rate").toString();
                    String totalAmount = jObjItemDtl.get("Amount").toString();
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 15));
                    // sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 5));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 5));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
                    sbPrintBill.append("\n");
                }


            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Sub Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Sub Total   : ", "Left", 14));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("SubTotal").toString(), "Right", 18));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");


            // TaxLevel Details
            JSONArray jsonArrBillTaxDtl = jObj.getJSONArray("BillTaxDtlInfo");
            boolean flgTaxDtl = false;
            for (int i = 0; i < jsonArrBillTaxDtl.length(); i++) {
                JSONObject jObjTaxDtl = (JSONObject) jsonArrBillTaxDtl.get(i);
                String taxDesc = jObjTaxDtl.get("TaxDesc").toString();
                String taxAmt = jObjTaxDtl.get("TaxAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxDesc, "Left", 20));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxAmt, "Right", 12));
                sbPrintBill.append("\n");
                flgTaxDtl = true;
            }
            if (flgTaxDtl) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                sbPrintBill.append("\n");
            }

            // Grand Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Total       : ", "Left", 14));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("GrandTotal").toString(), "Right", 18));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Bill Settlement Details
            boolean flgBillSettlement=false;
            JSONArray jsonArrBillSettlementDtl = jObj.getJSONArray("BillSettleDtlInfo");
            for (int i = 0; i < jsonArrBillSettlementDtl.length(); i++) {
                JSONObject jObjSettlementDtl = (JSONObject) jsonArrBillSettlementDtl.get(i);
                String settlementDesc = jObjSettlementDtl.get("SettlementDesc").toString();
                String settlementAmt = jObjSettlementDtl.get("SettlementAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementDesc, "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementAmt, "Right", 12));
                sbPrintBill.append("\n");
                flgBillSettlement=true;
            }

            if(flgBillSettlement) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                sbPrintBill.append("\n");
            }

            /*
            JSONObject jsonObjCardBalance = jObj.getJSONObject("CardDetails");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Card Balance : ", "Left", 20));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jsonObjCardBalance.get("Balance").toString(), "Right", 12));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");*/

            sbPrintBill=funPrintVatAndServiceTaxNo(sbPrintBill);

            // Member Details

            boolean flgMemberDtls=false;
            if (!jObjBillHd.get("MemCode").toString().isEmpty()) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Code   : ", "Left", 16));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemCode").toString(), "Left", 16));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Name   : ", "Left", 16));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemName").toString(), "Left", 16));
                sbPrintBill.append("\n");
                flgMemberDtls=true;
            }

            sbPrintBill.append(objPrint.funGetStringWithAlignment("(INCLUSIVE OF ALL TAXES)", "Left", 32));

            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");



            if(printType.equals("makeBill"))
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))   // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
                }
                else //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    clsMakeBillScreen.funPrintBillForMachDevice(sbPrintBill.toString());

                }


            }
            else //To print bill on Mach Device
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))    // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
                }
                else  //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
               //     new clsBillSettlement().funPrintBillForMachDevice(sbPrintBill.toString());
                }

            }

            // Function to print bill on POS Gold Device.
            //funPrintBillForPOSGoldDevice(sbPrintBill.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    // For Format 8
    private void funPrintBillFormat8(JSONObject jObj,String printType) // Poona Club
    {
        try {

            JSONArray jsonArrBillHd = jObj.getJSONArray("BillHdInfo");

            String line = "--------------------------------";
            StringBuilder sbPrintBill = new StringBuilder();

            clsPrintFormatAPI objPrint = new clsPrintFormatAPI();

            // sbPrintBill.append(objPrint.funGetStringWithAlignment("INVOICE", "Left", 32));
            // sbPrintBill.append("\n");

            /*sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 32));
            sbPrintBill.append("\n");*/

            JSONObject jObjBillHd = jsonArrBillHd.getJSONObject(0);

            if(clsGlobalFunctions.gClientCode.equals("074.001") )
            {
                if(clsGlobalFunctions.gPOSCode.equals("P18") || clsGlobalFunctions.gPOSCode.equals("P19")
                        || clsGlobalFunctions.gPOSCode.equals("P20") || clsGlobalFunctions.gPOSCode.equals("P21")
                        || clsGlobalFunctions.gPOSCode.equals("P22") || clsGlobalFunctions.gPOSCode.equals("P23"))
                {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gPOSName, "Left", 32));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                    sbPrintBill.append("\n");
                }
            }

            String KOTNo="";
            Set<String> set =new HashSet<String>();
            JSONArray jsonArrBillDtl1 = jObj.getJSONArray("BillDtlInfo");

            for (int i = 0; i < jsonArrBillDtl1.length(); i++) {
                JSONObject jObjItemDtl = (JSONObject) jsonArrBillDtl1.get(i);
                set.add(jObjItemDtl.get("Kot").toString());
            }

            Iterator<String> itKOTNo=set.iterator();
            while(itKOTNo.hasNext())
            {
                KOTNo+=","+itKOTNo.next();
            }

            if(!KOTNo.isEmpty()) {
                KOTNo=KOTNo.substring(1,KOTNo.length());
                sbPrintBill.append(objPrint.funGetStringWithAlignment("KOT No:", "left", 8));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(KOTNo, "Left", 24));
                sbPrintBill.append("\n");
                System.out.println(KOTNo);
            }

            String operationType=jObjBillHd.get("OperationType").toString();
            if(operationType.equalsIgnoreCase("Home Delivery"))
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(operationType, "Left", 32));
                sbPrintBill.append("\n");
            }

            // HomeDelivery Details
            JSONArray jsonArrHomeDeliveryDtl = jObj.getJSONArray("HomeDeliveryDtl");
            if(jsonArrHomeDeliveryDtl.length()>0)
            {

                    for (int i = 0; i < jsonArrHomeDeliveryDtl.length(); i++)
                    {
                        JSONObject jObjHomeDeliveryDtl = (JSONObject) jsonArrHomeDeliveryDtl.get(i);
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("Customer Name", "Left", 16));
                        sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjHomeDeliveryDtl.get("CustomerName").toString(), "Left", 16));
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("Mobile No", "Left", 16));
                        sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjHomeDeliveryDtl.get("MobileNo").toString(), "Left", 16));
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("Address", "Left", 16));
                        sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjHomeDeliveryDtl.get("Address").toString(), "Left", 16));
                        sbPrintBill.append("\n");

                    }

            }


            // Bill header level details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill No   : " + jObjBillHd.get("BillNo").toString(), "Left", 32));
            sbPrintBill.append("\n");
            String billDate = jObjBillHd.get("BillDate").toString();
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill Date : " + billDate, "Left", 32));
            sbPrintBill.append("\n");
            String tableName=jObjBillHd.get("Table").toString();
            if(!tableName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Table : " + tableName, "Left", 32));
                sbPrintBill.append("\n");
            }
            String waiterName=jObjBillHd.get("Waiter").toString();
            if(!waiterName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Waiter : " + waiterName, "Left", 32));
                sbPrintBill.append("\n");
            }
            String PAX=jObjBillHd.get("PAX").toString();
            if(!PAX.isEmpty())
            {
                if(Integer.parseInt(PAX)>0) {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("PAX : " + PAX, "Left", 32));
                    sbPrintBill.append("\n");
                }
            }
            String user=jObjBillHd.get("User").toString();
            if(!user.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Cashier : " + user, "Left", 32));
                sbPrintBill.append("\n");
            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Item Level Details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("ItemName       QTY        Amount  \n", "Left", 32));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");
            JSONArray jsonArrBillDtl = jObj.getJSONArray("BillDtlInfo");
            for (int i = 0; i < jsonArrBillDtl.length(); i++) {
                JSONObject jObjItemDtl = (JSONObject) jsonArrBillDtl.get(i);
                String itemName = jObjItemDtl.get("ItemName").toString();
                String itemQty = jObjItemDtl.get("Quantity").toString();
                //String itemRate = jObjItemDtl.get("Rate").toString();
                String totalAmount = jObjItemDtl.get("Amount").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 18));
                // sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 7));
                // sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 10));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
                sbPrintBill.append("\n");
            }

            JSONArray jsonArrModBillDtl = jObj.getJSONArray("ModBillDtlInfo");
            if(jsonArrModBillDtl.length()>0)
            {
                for (int i = 0; i < jsonArrModBillDtl.length(); i++) {
                    JSONObject jObjItemDtl = (JSONObject) jsonArrModBillDtl.get(i);
                    String itemName = jObjItemDtl.get("ModItemName").toString();
                    String itemQty = jObjItemDtl.get("Quantity").toString();
                    //String itemRate = jObjItemDtl.get("Rate").toString();
                    String totalAmount = jObjItemDtl.get("Amount").toString();
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 18));
                    // sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 7));
                    // sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 10));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
                    sbPrintBill.append("\n");
                }


            }

            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Sub Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Sub Total   : ", "Left", 14));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("SubTotal").toString(), "Right", 18));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");


            // TaxLevel Details
            JSONArray jsonArrBillTaxDtl = jObj.getJSONArray("BillTaxDtlInfo");
            boolean flgTaxDtl = false;
            for (int i = 0; i < jsonArrBillTaxDtl.length(); i++) {
                JSONObject jObjTaxDtl = (JSONObject) jsonArrBillTaxDtl.get(i);
                String taxDesc = jObjTaxDtl.get("TaxDesc").toString();
                String taxAmt = jObjTaxDtl.get("TaxAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxDesc, "Left", 20));
               // sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxAmt, "Right", 12));
                sbPrintBill.append("\n");
                flgTaxDtl = true;
            }
            if (flgTaxDtl) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                sbPrintBill.append("\n");
            }

            // Grand Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Total       : ", "Left", 14));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("GrandTotal").toString(), "Right", 18));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");

            // Bill Settlement Details
            boolean flgBillSettlement=false;
            JSONArray jsonArrBillSettlementDtl = jObj.getJSONArray("BillSettleDtlInfo");
            for (int i = 0; i < jsonArrBillSettlementDtl.length(); i++) {
                JSONObject jObjSettlementDtl = (JSONObject) jsonArrBillSettlementDtl.get(i);
                String settlementDesc = jObjSettlementDtl.get("SettlementDesc").toString();
                String settlementAmt = jObjSettlementDtl.get("SettlementAmt").toString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementDesc, "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementAmt, "Right", 12));
                sbPrintBill.append("\n");
                flgBillSettlement=true;
            }

            if(flgBillSettlement) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
                sbPrintBill.append("\n");
            }

            /*
            JSONObject jsonObjCardBalance = jObj.getJSONObject("CardDetails");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Card Balance : ", "Left", 20));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jsonObjCardBalance.get("Balance").toString(), "Right", 12));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");*/
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Location:", "Left", 12));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 20));
            sbPrintBill.append("\n");

            sbPrintBill=funPrintVatAndServiceTaxNo(sbPrintBill);

            // Member Details
            try {

                if(null!=jObj.getString("CardDetails"))
                {
                    JSONObject jObjCardDtl = jObj.getJSONObject("CardDetails");
                    if (jObjCardDtl.getString("CardType").equals("CashCard")) {

                        String cardNo = jObjCardDtl.get("CardNo").toString();
                        String OpeningBalance = jObjCardDtl.get("Opening Balance").toString();
                        String closingBalance = jObjCardDtl.get("Closing Balance").toString();
                        String memberCode = jObjCardDtl.get("MemberCode").toString();
                        String memberName = jObjCardDtl.get("MemberName").toString();

                        sbPrintBill.append(objPrint.funGetStringWithAlignment("Debit Card No:", "Left", 16));
                        sbPrintBill.append(objPrint.funGetStringWithAlignment(cardNo, "Right", 16));
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("Opening Bal: ", "Left", 16));
                        sbPrintBill.append(objPrint.funGetStringWithAlignment(OpeningBalance, "Right", 16));
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("Closing Bal: ", "Left", 16));
                        sbPrintBill.append(objPrint.funGetStringWithAlignment(closingBalance, "Right", 16));
                        sbPrintBill.append("\n");
                        if (!memberCode.isEmpty())
                        {
                            sbPrintBill.append(objPrint.funGetStringWithAlignment("Ref Member Code: ", "Left", 16));
                            sbPrintBill.append(objPrint.funGetStringWithAlignment(memberCode, "left", 1));
                            sbPrintBill.append("\n");
                            sbPrintBill.append(objPrint.funGetStringWithAlignment("Ref Member Name: ", "Left", 15));
                            sbPrintBill.append(objPrint.funGetStringWithAlignment(memberName, "left", 17));
                            // sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Card No: ", "Left", 16));
                            // sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjCardDtl.get("MemberCardNo").toString(), "left", 16));

                            sbPrintBill.append("\n");
                        }
                    }
                    else
                    {
                        boolean flgMemberDtls = false;
                        if (!jObjCardDtl.get("MemberCode").toString().isEmpty()) {
                            sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Code   : ", "Left", 16));
                            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjCardDtl.get("MemberCode").toString(), "Left", 16));
                            sbPrintBill.append("\n");
                            sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Name   : ", "Left", 13));
                            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjCardDtl.get("MemberName").toString(), "Left", 19));
                            sbPrintBill.append("\n");
                            flgMemberDtls = true;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");


            if(printType.equals("makeBill"))
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))   // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
                }
                else //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    clsMakeBillScreen.funPrintBillForMachDevice(sbPrintBill.toString());

                }


            }
            else //To print bill on Mach Device
            {
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))    // To print bill on Bluetooth Device
                {

                    new clsPrintDemo().sendData(sbPrintBill.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
                }
                else  //To print bill on Mach Device
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    sbPrintBill.append("\n");
                    System.out.println("print data:"+sbPrintBill.toString());
                 //   new clsBillSettlement().funPrintBillForMachDevice(sbPrintBill.toString());
                }

            }

            // Function to print bill on POS Gold Device.
            //funPrintBillForPOSGoldDevice(sbPrintBill.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private StringBuilder funPrintVatAndServiceTaxNo(StringBuilder sb)
    {
        if(clsGlobalFunctions.gUseVatAndServiceTaxNoFromPOS.equals("Y"))
        {
            if(clsGlobalFunctions.gPrintPOSVatNo.equals("Y"))
            {
                sb.append("VATTIN No : "+clsGlobalFunctions.gPOSVatNo);
                sb.append("\n");
            }
            if(clsGlobalFunctions.gPrintPOSServiceTaxNo.equals("Y"))
            {
                sb.append("SER.TAX : "+clsGlobalFunctions.gPOSServiceTaxNo);
                sb.append("\n");
            }
        }
        else
        {
            if(clsGlobalFunctions.gPrintVatNo.equals("Y"))
            {
                sb.append("VATTIN No : "+clsGlobalFunctions.gVatNo);
                sb.append("\n");
            }
            if(clsGlobalFunctions.gPrintServiceTaxNo.equals("Y"))
            {
                sb.append("SER.TAX : "+clsGlobalFunctions.gServiceTaxNo);
                sb.append("\n");
            }
        }

        return sb;
    }



    public Boolean isWifiAvailable(ConnectivityManager connectivityManager) {

        try
        {
            NetworkInfo wifiInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (wifiInfo.isConnected())
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public Boolean isInternetConnectionAvailable(ConnectivityManager connectivityManager)
    {

        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isAvailable() == true)
        {
            new clsBroadcastCall();
            return true;
        }
        else if (mMobile.isAvailable() == true)
        {
            return true;
        }
        else
            return false;
    }


    public String funGetHostName()
    {
        String hostName = "";
        try
        {
           // InetAddress ipAddress = InetAddress.getLocalHost();
            hostName = android.os.Build.MODEL;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return hostName;
        }
    }

    public java.util.Date funGetDateToSetCalenderDate()
    {
        java.util.Date date = null;

        try
        {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(clsGlobalFunctions.gPOSDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return date;
    }



    public String funGetDayForPricing()
    {
        String day = "";
        String dayNames[] = new DateFormatSymbols().getWeekdays();
        Calendar date2 = Calendar.getInstance();
        String tempday = dayNames[date2.get(Calendar.DAY_OF_WEEK)];
        switch (tempday)
        {
            case "Sunday":
                day = "PriceSunday";
                break;

            case "Monday":
                day = "PriceMonday";
                break;

            case "Tuesday":
                day = "PriceTuesday";
                break;

            case "Wednesday":
                day = "PriceWenesday";
                break;

            case "Thursday":
                day = "PriceThursday";
                break;

            case "Friday":
                day = "PriceFriday";
                break;

            case "Saturday":
                day = "PriceSaturday";
                break;

            default:
                day = "strPriceSunday";
        }
        return day;
    }


    public String funGetCurrentMACAddress(Activity activity)
    {
        String currentMACAddress = "";
        try {
            currentMACAddress = Settings.Secure.getString(activity.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            System.out.println(currentMACAddress.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return currentMACAddress;
        }
    }


    public double funGetitemPriceOnDay(String day, clsKotItemsListBean obBean) {

        double itemPrice = 0;
        switch (day) {
            case "PriceSunday":
                itemPrice = obBean.getDblSalePriceSun();
                break;

            case "PriceMonday":
                itemPrice = obBean.getDblSalePriceMon();
                break;

            case "PriceTuesday":
                itemPrice = obBean.getDblSalePriceTues();
                break;

            case "PriceWenesday":
                itemPrice = obBean.getDblSalePriceWend();
                break;

            case "PriceThursday":
                itemPrice = obBean.getDblSalePriceThu();
                break;

            case "PriceFriday":
                itemPrice = obBean.getDblSalePriceFri();
                break;

            case "PriceSaturday":
                itemPrice = obBean.getDblSalePriceSat();
                break;

            case "strPriceSunday":
                itemPrice = obBean.getDblSalePriceSun();
                break;

            default:
                itemPrice = obBean.getDblSalePrice();
        }
        return itemPrice;
    }

    public String funCheckSpecialCharacters(String inputString) throws Exception
    {
        String outputString="";
        if(null!=inputString)
        {
            if(inputString.contains("\\"))
                outputString=inputString.replace("\\", "\\\\");
            else
                outputString=inputString;
        }
        return outputString;
    }

}
