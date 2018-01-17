package com.example.apos.util.mach;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.bewo.mach.MACHPrinter;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.activity.clsGlobalFunctions;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sanguine on 8/25/2015.
 */
public class clsPrintFormatAPI {

    MACHServices machService;
    Context context;

    public void funGenerateBillFormat(String billNo, Context context)
    {
        this.context=context;
        new GenerateBillFormat().execute(billNo);
    }


    private class GenerateBillFormat extends AsyncTask<String, Void, JSONObject> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jObj=null;
            String billNo=params[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(clsGlobalFunctions.gAPOSWebSrviceURL+"/funPrintBill?billNo="+billNo);
            String text = null;

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                jObj = new JSONObject(text);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jObj;
        }

        protected void onPostExecute(JSONObject jObj) {
            //finish();
            try {

                JSONArray jsonArrBillHd=jObj.getJSONArray("BillHdInfo");

                String line = "--------------------------------";
                StringBuilder sbPrintBill = new StringBuilder();

                sbPrintBill.append(funGetStringWithAlignment("INVOICE"+"\n", "Center", 32));


            // Member Details
                JSONObject jObjBillHd=jsonArrBillHd.getJSONObject(0);
                if(!jObjBillHd.get("MemCode").toString().isEmpty()) {
                    sbPrintBill.append(funGetStringWithAlignment("Member Code   : ", "Left", 16));
                    sbPrintBill.append(funGetStringWithAlignment(jObjBillHd.get("MemCode") + "\n", "Left", 16));
                    sbPrintBill.append(funGetStringWithAlignment("Member Name   : ", "Left", 16));
                    sbPrintBill.append(funGetStringWithAlignment(jObjBillHd.get("MemName") + "\n", "Left", 16));
                }


            // Bill header level details
                sbPrintBill.append(funGetStringWithAlignment("Bill No   : " + jObjBillHd.get("BillNo")+"\n", "Left", 32));
                String billDate = jObjBillHd.get("BillDate").toString();
                sbPrintBill.append(funGetStringWithAlignment("Bill Date : " + billDate+"\n", "Left", 32));
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));

            // Item Level Details
                sbPrintBill.append(funGetStringWithAlignment("QTY       MRP    Rate      TOTAL\n", "Left", 32));
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));
                JSONArray jsonArrBillDtl = jObj.getJSONArray("BillDtlInfo");
                for (int i = 0; i < jsonArrBillDtl.length(); i++) {
                    JSONObject jObjItemDtl = (JSONObject) jsonArrBillDtl.get(i);
                    String itemName=jObjItemDtl.get("ItemName").toString();
                    String itemQty=jObjItemDtl.get("Quantity").toString();
                    String itemRate=jObjItemDtl.get("Rate").toString();
                    String totalAmount=jObjItemDtl.get("Amount").toString();
                    sbPrintBill.append(funGetStringWithAlignment(itemName+"\n", "Left", 32));
                    sbPrintBill.append(funGetStringWithAlignment(itemQty, "Right", 8));
                    sbPrintBill.append(funGetStringWithAlignment(itemRate, "Right", 7));
                    sbPrintBill.append(funGetStringWithAlignment(itemRate, "Right", 7));
                    sbPrintBill.append(funGetStringWithAlignment(totalAmount, "Right", 10));
                    sbPrintBill.append("\n");
                }
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));

            // Sub Total
                sbPrintBill.append(funGetStringWithAlignment("Sub Total   : ", "Left", 14));
                sbPrintBill.append(funGetStringWithAlignment(jObjBillHd.get("SubTotal")+"\n", "Right", 18));
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));


            // TaxLevel Details
                JSONArray jsonArrBillTaxDtl = jObj.getJSONArray("BillTaxDtlInfo");
                for (int i = 0; i < jsonArrBillTaxDtl.length(); i++) {
                    JSONObject jObjTaxDtl = (JSONObject) jsonArrBillTaxDtl.get(i);
                    String taxDesc=jObjTaxDtl.get("TaxDesc").toString();
                    String taxAmt=jObjTaxDtl.get("TaxAmt").toString();
                    sbPrintBill.append(funGetStringWithAlignment(taxDesc+"\n", "Left", 20));
                    sbPrintBill.append(funGetStringWithAlignment(taxAmt, "Right", 12));
                    sbPrintBill.append("\n");
                }
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));

            // Grand Total
                sbPrintBill.append(funGetStringWithAlignment("Total       : ", "Left", 14));
                sbPrintBill.append(funGetStringWithAlignment(jObjBillHd.get("GrandTotal")+"\n", "Right", 18));
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));

            // Bill Settlement Details
                JSONArray jsonArrBillSettlementDtl = jObj.getJSONArray("BillSettleDtlInfo");
                for (int i = 0; i < jsonArrBillSettlementDtl.length(); i++) {
                    JSONObject jObjSettlementDtl = (JSONObject) jsonArrBillSettlementDtl.get(i);
                    String settlementDesc=jObjSettlementDtl.get("SettlementDesc").toString();
                    String settlementAmt=jObjSettlementDtl.get("SettlementAmt").toString();
                    sbPrintBill.append(funGetStringWithAlignment(settlementDesc+"\n", "Left", 20));
                    sbPrintBill.append(funGetStringWithAlignment(settlementAmt, "Right", 12));
                    sbPrintBill.append("\n");
                }
                sbPrintBill.append(funGetStringWithAlignment(line+"\n", "Left", 32));

                sbPrintBill.append(funGetStringWithAlignment("(INCLUSIVE OF ALL TAXES)\n", "Center", 32));

                //System.out.println("Bill Data=\n" + sbPrintBill);

                funPrintBill(sbPrintBill.toString());

            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }



    private void funPrintBill(String billFormat)
    {
        if (machService.isMachActive())
        {
            byte print_return = machService.print_text(billFormat,(byte) 0x00, (byte) 0x02, false, (byte) 0x00, false, false, (byte) 0x00);
            //For Magnetic Swipe
            //byte[] track = machService.getMSRTrackData(20);

            switch (print_return) {
                case MACHPrinter.BAT_LOW_ERROR:
                    Toast.makeText(context,"Printer Status : Battery Low ",Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PLATEN:
                    Toast.makeText(context,"Printer Status : No Paper ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PAPER:
                    Toast.makeText(context,"Printer Status : No Platen ",Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PRINTER:
                    Toast.makeText(context,"Printer Status : No Printer Module ",Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_STATUS_OK:
                    Toast.makeText(context,"Printer Status : OK ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_TIMEOUT_APP:
                    Toast.makeText(context,"Printer Status : Timeout from the Application ",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(context,"Printer Status : " + print_return,Toast.LENGTH_LONG).show();
                    break;
            }
        } else
            Toast.makeText(context,"Printer Status : MACH is not active",Toast.LENGTH_LONG).show();
    }



    public StringBuilder funGetStringWithAlignment(String text, String alignMent, int totalLength)
    {
        StringBuilder sbOutputString=new StringBuilder(text);
        if (alignMent.equalsIgnoreCase("Center"))
        {
            sbOutputString.setLength(0);
            int textLength = text.length();
            int totalSpace = (totalLength - textLength) / 2;
             for (int i = 0; i < totalSpace; i++)
            {
                sbOutputString.append(" ");
            }
            sbOutputString.append(text);

        }

        else if (alignMent.equalsIgnoreCase("Left"))
        {
            sbOutputString.setLength(0);
            int textLength = text.length();
            int totalSpace = (totalLength - textLength);

            if (totalSpace < 0)
            {
                sbOutputString.append(text.substring(0, totalLength));
            }
            else
            {
                sbOutputString.append(text);

                for (int i = 0; i < totalSpace; i++)
                {
                    sbOutputString.append(" ");
                }
            }

        }
        else
        {
            sbOutputString.setLength(0);
            int textLength = text.length();
            int totalSpace = (totalLength - textLength);

            if (totalSpace < 0)
            {
                sbOutputString.append(text.substring(0, totalLength));
            }
            else
            {
                for (int i = 0; i < totalSpace; i++)
                {
                    sbOutputString.append(" ");
                }
                sbOutputString.append(text);
            }

        }
        return sbOutputString;
    }
}
