package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsDirectBill;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsDirectBillerActListener;
import com.example.apos.listeners.clsOtherOptionsSelectionListener;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonObject;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class clsDirectBillerOptionsFragment extends Fragment
{
    private Button btnSave,btnReset,btnOk;
    private EditText edtMobileNo,edtCustomerName,edtAddress;
    TextView txtCustomerName;
    public clsDirectBillerActListener objDirectyBillerActListener;
    private  clsOtherOptionsSelectionListener objOptionsSelectionListener;
    private LinearLayout linearNewCustomerScreen,linearButtonsPanel,linearMobilePanel;
    private Spinner spinnerOfCustomerType;
    private String customerCode="";
    private String custType;
    public String custTypeCode;
    private Map hmCustomerType;
    private ArrayList<String>custTypeList;
    private Activity mActivity;
    private Dialog pgDialog;
    View rootView;
    public static clsDirectBillerOptionsFragment getInstance()
    {
        clsDirectBillerOptionsFragment mDirectBillerOptions = new clsDirectBillerOptionsFragment();
        return mDirectBillerOptions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        mActivity=getActivity();
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.optionscreenfordirectbiller, container, false);
        try
        {
           objDirectyBillerActListener=(clsDirectBillerActListener)getActivity();
        } catch (Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        widget(rootView);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CommonUtils.showKeyboard(edtMobileNo,true);
                if (edtMobileNo.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Please Enter Mobile No", Toast.LENGTH_LONG).show();
                }
                else if(edtMobileNo.getText().length()<10)
                {
                    Toast.makeText(getActivity(), "Please Enter 10 digit Mobile No", Toast.LENGTH_LONG).show();
                }
                else {
                    funSaveNewCustomer();
                }

            }
        });

        txtCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(edtCustomerName.isShown())){
                    Toast.makeText(getActivity(), "Please Press Ok Button", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                CommonUtils.showKeyboard(edtMobileNo,true);
                if (edtMobileNo.getText().toString().isEmpty())
                {
                   Toast.makeText(getActivity(), "Please Enter Mobile No", Toast.LENGTH_LONG).show();
                }
               else if(edtMobileNo.getText().length()<10)
                {
                    Toast.makeText(getActivity(), "Please Enter 10 digit Mobile No", Toast.LENGTH_LONG).show();
                }
                else {
                    //new GetCustomerInfo().execute(edtMobileNo.getText().toString());
                    edtCustomerName.setVisibility(View.VISIBLE);
                    funGetCustomerInfo(edtMobileNo.getText().toString());
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funGetResetField();
                CommonUtils.showKeyboard(edtMobileNo,true);
            }
        });

        return rootView;
    }


    private void widget(View rootView)
    {
        custTypeList= new ArrayList<String>();
        custType="";
        btnOk=(Button)rootView.findViewById(R.id.btnOk);
        btnSave=(Button)rootView.findViewById(R.id.btnSave);
        btnReset=(Button)rootView.findViewById(R.id.btnReset);
        edtMobileNo = (EditText) rootView.findViewById(R.id.edtMobileNo);
        CommonUtils.showKeyboard(edtMobileNo,true);
        edtCustomerName= (EditText) rootView.findViewById(R.id.edtCustomerName);
        txtCustomerName=(TextView) rootView.findViewById(R.id.txtCustomerName);
        edtCustomerName.setVisibility(View.INVISIBLE);
        spinnerOfCustomerType= (Spinner) rootView.findViewById(R.id.spinnerOfCustType);
        edtAddress= (EditText) rootView.findViewById(R.id.edtCustomerAddress);
        linearNewCustomerScreen = (LinearLayout) rootView.findViewById(R.id.linearNewCustomer);
        linearButtonsPanel = (LinearLayout) rootView.findViewById(R.id.linearButtonPanel);
        linearMobilePanel= (LinearLayout) rootView.findViewById(R.id.linearMobilePanel);

        if(!clsGlobalFunctions.gPhoneNo.isEmpty())
        {
            btnOk.setVisibility(View.INVISIBLE);
            edtMobileNo.setText(clsGlobalFunctions.gPhoneNo);
            linearNewCustomerScreen.setBackgroundColor(Color.LTGRAY);
            linearButtonsPanel.setBackgroundColor(Color.LTGRAY);
           // new LoadCustomerTypeList().execute();
            funLoadCustomerTypeList();
        }
    }
    private void funGetResetField()
    {
        edtCustomerName.setText("");
        edtAddress.setText("");
        edtMobileNo.setText("");
        linearNewCustomerScreen.setBackgroundColor(Color.WHITE);
        linearButtonsPanel.setBackgroundColor(Color.WHITE);
    }

     private void funSaveNewCustomer()
    {

        if(edtMobileNo.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Enter Mobile No",Toast.LENGTH_SHORT).show();
            return;
        }
        if(edtCustomerName.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Enter customer Name",Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtAddress.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(),"Enter customer Address",Toast.LENGTH_SHORT).show();
            return;
        }
        if((hmCustomerType==null)){
                Toast.makeText(getActivity(),"Customer Type Not Present",Toast.LENGTH_SHORT).show();
                return;

        }
          //new SaveNewCustomerDtl().execute();
        funSaveNewCustomerDtl();

    }


    private void funGetCustomerInfo(String... params)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetCustomerInfo(params[0],clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsCustomerMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsCustomerMaster> arrayList) {
                        dismissDialog();
                        if (null != arrayList) {
                            try{
                                if (arrayList.size()>0)
                                {
                                    hmCustomerType=new HashMap<String,String>();
                                    for(int cnt=0;cnt<arrayList.size();cnt++)
                                    {
                                        clsCustomerMaster objCustomerMaster = (clsCustomerMaster) arrayList.get(cnt);
                                        edtCustomerName.setText(objCustomerMaster.getCustomerName());
                                        clsGlobalFunctions.gCustName=objCustomerMaster.getCustomerName();
                                        clsDirectBill.customerName.setText(objCustomerMaster.getCustomerName()+"\n"+objCustomerMaster.getCustomerBuildingName());
                                        edtAddress.setText(objCustomerMaster.getCustomerBuildingName());
                                        //     CustDetails=objCustomerMaster.getCustomerName()+"\n From :"+objCustomerMaster.getCustomerBuildingName();
                                        ArrayList<String>CustTypeList= new ArrayList<String>();
                                        if(!objCustomerMaster.getCustomerType().isEmpty())
                                        {
                                            CustTypeList.add(objCustomerMaster.getCustomerType());
                                            hmCustomerType.put(objCustomerMaster.getCustomerType(), objCustomerMaster.getCustomerTypeCode());
                                        }
                                        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,CustTypeList);
                                        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnerOfCustomerType.setAdapter(dataAdapter1);
                                        customerCode=objCustomerMaster.getCustomerCode();
                                        clsDirectBill.customerCode=objCustomerMaster.getCustomerCode();
                                        clsDirectBill.customerType=objCustomerMaster.getCustomerTypeCode();
                                        custTypeCode=objCustomerMaster.getCustomerTypeCode();
                                    }
                                    objDirectyBillerActListener.setSelectedOptionResult(customerCode,custTypeCode);

                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Customer Not Found",Toast.LENGTH_LONG).show();
                                    linearNewCustomerScreen.setBackgroundColor(Color.LTGRAY);
                                    linearButtonsPanel.setBackgroundColor(Color.LTGRAY);
                                   // new LoadCustomerTypeList().execute();
                                    funLoadCustomerTypeList();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
                        dismissDialog();
                    }
                });


            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }

    private void funLoadCustomerTypeList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funLoadCustomerTypeList(clsGlobalFunctions.gClientCode,new BaseAPIHelper.OnRequestComplete<ArrayList<clsCustomerMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsCustomerMaster> arrayList) {
                        dismissDialog();
                        if (null != arrayList) {
                            try{
                                if (arrayList.size()>0){
                                    hmCustomerType=new HashMap<String,String>();
                                    for(int cnt=0;cnt<arrayList.size();cnt++)
                                    {
                                        clsCustomerMaster objCustomerMaster=(clsCustomerMaster)arrayList.get(cnt);
                                        hmCustomerType.put(objCustomerMaster.getCustomerType(), objCustomerMaster.getCustomerTypeCode());

                                    }
                                    Set setCustType=hmCustomerType.keySet();
                                    Iterator itrCustType=setCustType.iterator();

                                    while(itrCustType.hasNext())
                                    {
                                        custType= (String) itrCustType.next();
                                        custTypeList.add(custType);
                                    }
                                    custTypeCode=hmCustomerType.get(custType).toString();

                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                            (getActivity(), android.R.layout.simple_spinner_item,custTypeList);

                                    dataAdapter.setDropDownViewResource
                                            (android.R.layout.simple_spinner_dropdown_item);
                                    spinnerOfCustomerType.setAdapter(dataAdapter);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"Customer Type Not Found",Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
                        dismissDialog();
                    }
                });


            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }


    private void funSaveNewCustomerDtl()
    {
        try{
            if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
                if (ConnectivityHelper.isConnected()) {
                    String regStatus="false";
                    custTypeCode="";
                    if(custTypeList.size()>0)
                    {
                        custTypeCode = hmCustomerType.get(spinnerOfCustomerType.getSelectedItem().toString()).toString();
                    }
                    Date dt=new Date();
                    String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                    dateTime=dateTime.replaceAll(" ","%20");
                    String custname= edtCustomerName.getText().toString().replaceAll(" ","%20");
                    custname=new clsUtility().funCheckSpecialCharacters(custname);
                    String MobileNo=edtMobileNo.getText().toString();
                    String Address=new clsUtility().funCheckSpecialCharacters(edtAddress.getText().toString());
                    showDialog();
                    App.getAPIHelper().funSaveNewCustomerDtl(custname,MobileNo,custTypeCode,Address,clsGlobalFunctions.gClientCode,clsGlobalFunctions.gUserCode,dateTime,new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                        @Override
                        public void onSuccess(JsonObject jObj) {
                            dismissDialog();
                            if (null != jObj) {
                                try{
                                    String reason=jObj.get("Reason").getAsString().toString();//1
                                    String customerStatus=jObj.get("CustomerStatus").getAsString().toString();///0

                                    if (reason.equals("Exception"))
                                    {
                                        Toast.makeText(getActivity(),"problem while save new customer",Toast.LENGTH_LONG).show();

                                    }

                                    if (customerStatus.equals("false")) {

                                        if(reason.equals("MobileNo"))
                                        {
                                            Toast.makeText(getActivity(), "Mobile No is already registered!!!", Toast.LENGTH_LONG).show();
                                        }

                                        else
                                        {
                                            Toast.makeText(getActivity(), "Failed!!!", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    else
                                    {
                                        customerCode=customerStatus;
                                        clsDirectBill.customerCode=customerStatus;
                                        clsDirectBill.customerType=custTypeCode;
                                        objDirectyBillerActListener.setSelectedOptionResult(customerCode,custTypeCode);
                                        clsDirectBill.customerName.setText(edtCustomerName.getText().toString()+"\n"+edtAddress.getText().toString());
                                        funGetResetField();
                                        Toast.makeText(getActivity(), "Entry Added Successfully "+custTypeCode, Toast.LENGTH_LONG).show();
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(String errorMessage, int errorCode) {
                            dismissDialog();
                        }
                    });


                } else {
                    SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
                }
            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
            }
        }catch (Exception e){

        }

    }


    protected void showDialog() {
        if (null == pgDialog ) {
            if(mActivity ==null)
                mActivity=clsDirectBill.mActivity;
            pgDialog = CommonUtils.getProgressDialog(mActivity, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }


}
