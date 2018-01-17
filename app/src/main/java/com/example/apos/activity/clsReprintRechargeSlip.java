package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bewo.mach.MACHPrinter;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.adapter.clsReprintDocumentListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsReprintDocumentBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.util.clsPrintDemo;
import com.example.apos.util.clsUtility;
import com.example.apos.util.mach.clsPrintFormatAPI;

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
import java.util.ArrayList;

public class clsReprintRechargeSlip extends Activity implements clsReprintDocumentListAdapter.customButtonListener {
    EditText edtCardHolderName;
    TextView textCardNo;
    Button btnSwipe;
    String strcardNo;
    ArrayList arrListCardTransaction = new ArrayList();
    GridView gvRechargeReprintList;
   // clsShowCardDtlAdapter rechargeadapter;
    MACHServices machService;
    Button btnPrint;
    Button btnClose;
    TextView txtTotalAmount;
    private ConnectivityManager connectivityManager;
    private BluetoothAdapter mBluetoothAdapter;
    private clsReprintDocumentListAdapter docListAdapter;
    private String rechargeNo;
    private Dialog pgDialog;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showreprintdocumentsliplist);
        rechargeNo="";
        mActivity=this;
        if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled())
            {
                Toast.makeText(clsReprintRechargeSlip.this,"Bluetooth is disable",Toast.LENGTH_LONG).show();

            }
            else
            {
                new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
            }
        }
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
       // btnSwipe = (Button) findViewById(R.id.btnReprintSwipe);
        textCardNo = (TextView) findViewById(R.id.textReprintCardNo);
        gvRechargeReprintList = (GridView) findViewById(R.id.gvReprintSlipList);
        funShowRechargeSlipList();

        btnClose = (Button) findViewById(R.id.btnReprintClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funClearObjects();
                try {
                    if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
                        new clsPrintDemo().closeBT(clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream, clsPrintDemo.socket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

        btnPrint = (Button) findViewById(R.id.btnReprintSlip);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rechargeNo.isEmpty())
                {
                    String cardString=textCardNo.getText().toString();
                  //  new ShowGetRechargeSlipDataWS().execute(rechargeNo);
                    funPrintRechargeSlip(rechargeNo);
                    //funGenerateSlip();
                } else {
                    Toast.makeText(clsReprintRechargeSlip.this, "Recharge Printing failed", Toast.LENGTH_LONG).show();
                }

            }
        });


    }



    private void funClearObjects() {
        arrListCardTransaction = null;
    }


    @Override
    public void onBackPressed() {

        try {
            new clsPrintDemo().closeBT(clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream, clsPrintDemo.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();

    }
    private void funGenerateSlip() {
        //funPrintSlipForPOSGoldDevice(sbRechargeDetails.toString());
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        machService = new MACHServices(getApplicationContext());
        machService.mach_initialize(getApplicationContext());

        try {

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int connectStatus = machService.mach_connect();
        Toast.makeText(getApplicationContext(),
                "Connection Status : " + connectStatus, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        machService.mach_release(getApplicationContext());
    }


    private void funShowRechargeSlipList() {
        // Web Servioce Call to Fetch Member Details
        if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
           // new ShowRechargeSlipListWS().execute();
            funGetRechargeSlip();

        } else {
            Toast.makeText(clsReprintRechargeSlip.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onButtonClickListner(int position, String value) {
        if (value.split("#")[1].equals("selectedrow")) {

            rechargeNo = value.split("#")[0];
        }
    }

    private void funFillGridView(ArrayList arrlist) {
        docListAdapter = new clsReprintDocumentListAdapter(clsReprintRechargeSlip.this, arrlist);
        docListAdapter.setCustomButtonListner(clsReprintRechargeSlip.this);
        gvRechargeReprintList.setAdapter(docListAdapter);
    }

    private void funGenerateSlip(String rechargeNo,String rechargAmt,String rechargeSlipNo,String date,String time,String cutomerName)
    {
        StringBuilder sbRechargeDetails = new StringBuilder();

        clsPrintFormatAPI objPrint=new clsPrintFormatAPI();

        sbRechargeDetails.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gPOSName, "Left", 32));
        sbRechargeDetails.append("\n");

        sbRechargeDetails.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 32));
        sbRechargeDetails.append("\n");

        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Recharge Details", "Left", 32));
        sbRechargeDetails.append("\n");

        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Date   : "+date, "Left", 32));
        sbRechargeDetails.append("\n");


        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Time   : "+time, "Left", 32));
        sbRechargeDetails.append("\n");

        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Card No:", "Left", 16));
        sbRechargeDetails.append(objPrint.funGetStringWithAlignment(strcardNo, "Left", 16));
        sbRechargeDetails.append("\n");
        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Customer Name:", "Left", 13));
        sbRechargeDetails.append(objPrint.funGetStringWithAlignment(cutomerName, "Left", 19));
        sbRechargeDetails.append("\n");

        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Recharge Slip No:", "Left", 18));
        sbRechargeDetails.append(objPrint.funGetStringWithAlignment(rechargeSlipNo, "Left", 14));
        sbRechargeDetails.append("\n");



        double rechargeAmount=Double.parseDouble(rechargAmt);
        sbRechargeDetails.append(objPrint.funGetStringWithAlignment("Recharge AMT:", "Left", 16));
        sbRechargeDetails.append(objPrint.funGetStringWithAlignment(String.valueOf(Math.rint(rechargeAmount)) , "Left", 16));
        sbRechargeDetails.append("\n");


        sbRechargeDetails.append("\n");
        sbRechargeDetails.append("\n");
        sbRechargeDetails.append("\n");
        sbRechargeDetails.append("\n");

        System.out.println(sbRechargeDetails);


        if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))
        {
            try {
                new clsPrintDemo().sendData(sbRechargeDetails.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            sbRechargeDetails.append("\n");
            sbRechargeDetails.append("\n");
            sbRechargeDetails.append("\n");
            sbRechargeDetails.append("\n");
            sbRechargeDetails.append("\n");
            funPrintSlipForMachDevice(sbRechargeDetails.toString());
        }
        //funPrintSlipForPOSGoldDevice(sbRechargeDetails.toString());
    }


    private void funPrintSlipForMachDevice(String rechargeDetails)
    {
        if (machService.isMachActive())
        {
            byte print_return = machService.print_text(rechargeDetails,(byte) 0x00, (byte) 0x02, false, (byte) 0x00, false, false, (byte) 0x00);

            switch (print_return) {
                case MACHPrinter.BAT_LOW_ERROR:
                    Toast.makeText(getApplicationContext(),"Printer Status : Battery Low ",Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PLATEN:
                    Toast.makeText(getApplicationContext(),"Printer Status : No Paper ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PAPER:
                    Toast.makeText(getApplicationContext(),"Printer Status : No Platen ",Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PRINTER:
                    Toast.makeText(getApplicationContext(),"Printer Status : No Printer Module ",Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_STATUS_OK:
                    Toast.makeText(getApplicationContext(),"Printer Status : OK ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_TIMEOUT_APP:
                    Toast.makeText(getApplicationContext(),"Printer Status : Timeout from the Application ",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"Printer Status : " + print_return,Toast.LENGTH_LONG).show();
                    break;
            }
        } else
            Toast.makeText(getApplicationContext(),"Printer Status : MACH is not active",Toast.LENGTH_LONG).show();
    }


    private void funGetRechargeSlip()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetRechargeSlip(clsGlobalFunctions.gPOSCode,clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReprintDocumentBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsReprintDocumentBean> arrayList) {
                        dismissDialog();
                        if (null != arrayList) {
                            try{
                                ArrayList arrListRechargeMaster = new ArrayList();

                                clsReprintDocumentBean obBean=new clsReprintDocumentBean();
                                for(int i=0;i<arrayList.size();i++){
                                    obBean=arrayList.get(i);

                                    if (!(obBean.getStrDocNo().toString().equals(""))) {
                                        strcardNo=obBean.getStrCardNo();//("CardNo").toString();
                                        textCardNo.setText(obBean.getStrCardString());
                                    }
                                }
                                if (arrayList.size() > 0) {
                                    funFillGridView(arrayList);
                                }

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage, int errorCode) {

                    }
                });


            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }


    private void funPrintRechargeSlip(String... params)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                String cardRechargeNo = params[0];
                showDialog();
                // "/funPrintRechargeSlip?POSCode=" + clsGlobalFunctions.gPOSCode + "&ClientCode=" + clsGlobalFunctions.gClientCode + "&RechargeNo=" + cardRechargeNo);
                App.getAPIHelper().funPrintRechargeSlip(clsGlobalFunctions.gPOSCode,clsGlobalFunctions.gClientCode,cardRechargeNo, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReprintDocumentBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsReprintDocumentBean> arrayList) {
                        dismissDialog();
                        if (null != arrayList) {
                            try{
                                clsReprintDocumentBean obBean=new clsReprintDocumentBean();
                                for(int i=0;i<arrayList.size();i++){
                                    obBean=arrayList.get(i);
                                    if (!(obBean.getStrDocNo().toString().equals(""))) {
                                        strcardNo=obBean.getStrCardNo();//("CardNo").toString();
                                        textCardNo.setText(obBean.getStrCardString());
                                    }
                                }
                                if (arrayList.size() > 0)
                                {
                                    for (int cnt = 0; cnt < arrayList.size(); cnt++)
                                    {
                                        clsReprintDocumentBean objReprint = (clsReprintDocumentBean) arrayList.get(cnt);
                                        funGenerateSlip(objReprint.getStrDocNo(),objReprint.getStrAmount(),objReprint.getStrDocSlipNo(),objReprint.getStrDate(),objReprint.getStrTime(),objReprint.getStrCustomerName());
                                       // Toast.makeText(clsReprintRechargeSlip.this,"Printed Successfully",Toast.LENGTH_LONG).show();
                                    }
                                }

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage, int errorCode) {

                    }
                });


            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }

    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(this, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }
}