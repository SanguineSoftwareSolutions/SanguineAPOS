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


public class clsReprintRefundSlip extends Activity implements clsReprintDocumentListAdapter.customButtonListener {
    EditText edtCardHolderName;
    TextView textCardNo;
    Button btnSwipe;
    String strcardNo;
    ArrayList arrListCardTransaction = new ArrayList();
    GridView gvRechargeReprintList;
    //clsShowCardDtlAdapter rechargeadapter;
    MACHServices machService;
    Button btnPrint;
    Button btnClose;
    TextView txtTotalAmount;
    private ConnectivityManager connectivityManager;
    private BluetoothAdapter mBluetoothAdapter;
    private clsReprintDocumentListAdapter docListAdapter;
    private String refundNo;
    private Dialog pgDialog;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showreprintdocumentsliplist);
        refundNo="";
        mActivity=this;
        if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))
        {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled())
            {
                Toast.makeText(clsReprintRefundSlip.this,"Bluetooth is disable",Toast.LENGTH_LONG).show();

            }
            else
            {
                new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
            }

        }
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //btnSwipe = (Button) findViewById(R.id.btnReprintSwipe);
        textCardNo = (TextView) findViewById(R.id.textReprintCardNo);
        gvRechargeReprintList = (GridView) findViewById(R.id.gvReprintSlipList);
        funShowRefundSlipList();

        btnClose=(Button)findViewById(R.id.btnReprintClose);
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

        btnPrint = (Button)findViewById(R.id.btnReprintSlip);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!refundNo.isEmpty())
                {

                   // new ShowGetRefundSlipDataWS().execute(refundNo);
                    funPrintRefundSlip(refundNo);
                    //funGenerateSlip();
                } else {
                    Toast.makeText(clsReprintRefundSlip.this, "Refund Printing failed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private void funClearObjects()
    {
        arrListCardTransaction=null;
    }


    @Override
    public void onBackPressed()
    {

        try {
            new clsPrintDemo().closeBT(clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream,clsPrintDemo.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();

    }


    private void funGenerateSlip()
    {


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


    private void funShowRefundSlipList() {
        // Web Servioce Call to Fetch Member Details
        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            //new ShowRefundSlipListWS().execute();
            funGetRefundSlip();


        }
        else
        {
            Toast.makeText(clsReprintRefundSlip.this,"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onButtonClickListner(int position, String value)
    {
        if(value.split("#")[1].equals("selectedrow"))
        {

            refundNo=value.split("#")[0];
            Toast.makeText(clsReprintRefundSlip.this, "item selected" + refundNo, Toast.LENGTH_LONG).show();

        }

    }


    private void funFillGridView(ArrayList arrlist)
    {
        docListAdapter=new clsReprintDocumentListAdapter(clsReprintRefundSlip.this,arrlist);
        docListAdapter.setCustomButtonListner(clsReprintRefundSlip.this);
        gvRechargeReprintList.setAdapter(docListAdapter);
    }

    private void funGenerateSlip(String refundNo,String refundAmt,String refundSlipNo,String date,String time,String cutomerName)
    {
        StringBuilder sbRefundDetails = new StringBuilder();

        clsPrintFormatAPI objPrint=new clsPrintFormatAPI();

        sbRefundDetails.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gPOSName, "Left", 32));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Left", 32));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment("Refund Details", "Left", 32));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment("Date   : "+date, "Left", 32));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment("Time   : "+time, "Left", 32));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment("Card No:", "Left", 16));
        sbRefundDetails.append(objPrint.funGetStringWithAlignment(strcardNo, "Left", 16));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment("Customer Name:", "Left", 13));
        sbRefundDetails.append(objPrint.funGetStringWithAlignment(cutomerName, "Left", 19));
        sbRefundDetails.append("\n");

        sbRefundDetails.append(objPrint.funGetStringWithAlignment("RefundSlip No:", "Left", 18));
        sbRefundDetails.append(objPrint.funGetStringWithAlignment(refundSlipNo, "Left", 14));
        sbRefundDetails.append("\n");


        double refundAmount=Double.parseDouble(refundAmt);
        sbRefundDetails.append(objPrint.funGetStringWithAlignment("Refund AMT:", "Left", 16));
        sbRefundDetails.append(objPrint.funGetStringWithAlignment(String.valueOf(Math.rint(refundAmount)) , "Left", 16));
        sbRefundDetails.append("\n");


        sbRefundDetails.append("\n");
        sbRefundDetails.append("\n");
        sbRefundDetails.append("\n");
        sbRefundDetails.append("\n");

        System.out.println(sbRefundDetails);


        if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))
        {
            try {
                System.out.println("OuttputStream="+clsPrintDemo.mmOutputStream);
                System.out.println("InputStream="+clsPrintDemo.mmInputStream);
                System.out.println("String="+sbRefundDetails.toString());
                new clsPrintDemo().sendData(sbRefundDetails.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            sbRefundDetails.append("\n");
            sbRefundDetails.append("\n");
            sbRefundDetails.append("\n");
            sbRefundDetails.append("\n");
            sbRefundDetails.append("\n");
            funPrintSlipForMachDevice(sbRefundDetails.toString());
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

    private void funGetRefundSlip()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetRefundSlip(clsGlobalFunctions.gPOSCode,clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReprintDocumentBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsReprintDocumentBean> arrayList) {
                    dismissDialog();
                        if (null != arrayList) {
                            try{
                                ArrayList arrListRechargeMaster = new ArrayList();

                                clsReprintDocumentBean obBean=new clsReprintDocumentBean();
                                for(int i=0;i<arrayList.size();i++){
                                    obBean=arrayList.get(i);

                                    if (obBean.getStrDocNo().toString().equals("")) {
                                    }
                                    else
                                    {
                                        strcardNo=obBean.getStrCardNo();//("CardNo").toString();
                                        textCardNo.setText(obBean.getStrCardString());
                                    }
                                }
                                if (arrayList.size() > 0)
                                {
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

    private void funPrintRefundSlip(String... params)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                String cardRefundNo = params[0];
                App.getAPIHelper().funPrintRefundSlip(clsGlobalFunctions.gPOSCode,clsGlobalFunctions.gClientCode,cardRefundNo, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReprintDocumentBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsReprintDocumentBean> arrayList) {
                        dismissDialog();
                        if (null != arrayList) {
                            try{
                                if (arrayList.size() > 0)
                                {
                                    for (int cnt = 0; cnt < arrayList.size(); cnt++)
                                    {
                                        String cardType="";
                                        clsReprintDocumentBean objReprint = (clsReprintDocumentBean) arrayList.get(cnt);


                                        funGenerateSlip(objReprint.getStrDocNo(),objReprint.getStrAmount(),objReprint.getStrDocSlipNo(),objReprint.getStrDate(),objReprint.getStrTime(),objReprint.getStrCustomerName());
                                        Toast.makeText(clsReprintRefundSlip.this,"Printed Successfully",Toast.LENGTH_LONG).show();
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