package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsReasonMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsNCKOTActListener;
import com.example.apos.listeners.clsOtherOptionsSelectionListener;
import com.example.apos.util.clsUtility;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class clsKotOtherOptionsFragment extends Fragment implements clsOtherOptionsSelectionListener {
    public clsNCKOTActListener objNCKOTActListener;
    private  clsOtherOptionsSelectionListener objOptionsSelectionListener;
    public static String waiterNo="";
    private Spinner spinnerReason;
    private String strNcKOTYN;
    public static CheckBox chkNcKOT;
    public static EditText edtRemark;
    ArrayList arrReasonList;
    private Map hmReason;
    private String reasonCode;
    public String pos;
    private String reasonName="";
    ArrayList<String> reasonList;
    private Button btnOK;
    private  String keyCase="upperCase";
    public  Activity mActivity;
    private Dialog pgDialog;


    public static clsKotOtherOptionsFragment getInstance()
    {

        clsKotOtherOptionsFragment mKotOptions = new clsKotOtherOptionsFragment();
        return mKotOptions;
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
        View rootView = inflater.inflate(R.layout.kototheroptionsscreen, container, false);
        try {
            objOptionsSelectionListener = (clsOtherOptionsSelectionListener) clsKotOtherOptionsFragment.this;
            objNCKOTActListener=(clsNCKOTActListener)getActivity();
        } catch (Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }

        strNcKOTYN="N";
        reasonCode="";
        reasonList= new ArrayList<String>();
        btnOK=(Button)rootView.findViewById(R.id.btnOptionsOk);
        spinnerReason = (Spinner) rootView.findViewById(R.id.reasonSpinner);
        chkNcKOT = (CheckBox) rootView.findViewById(R.id.chkBoxNcKot);
        edtRemark=(EditText)rootView.findViewById(R.id.edtKotRemarks);
        widgetInit();
        // Toast.makeText(getActivity(),"IN OPTIONS TABBBBBBBBBBBBBB",Toast.LENGTH_LONG).show();
        //new ReasonWebService().execute();
        funGetReasonList();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                funGetReasonCode();
            }
        });

        return rootView;
    }

    public void widgetInit()
    {
        edtRemark.setText("");
        chkNcKOT.setChecked(false);
    }

    @Override
    public void getOptionsSelectionResult(String strNcKot)
    {

    }

    private void funGetReasonCode()
    {
        String remark="";
        try{
            remark=new clsUtility().funCheckSpecialCharacters(edtRemark.getText().toString());
            if(reasonList.size()>0)
            {
                reasonCode=hmReason.get(spinnerReason.getSelectedItem().toString()).toString();
                if (chkNcKOT.isChecked())
                {
                    strNcKOTYN="Y";
                    objNCKOTActListener.setSelectedOtherOptionsResult(strNcKOTYN,remark ,reasonCode);
                    Toast.makeText(getActivity(),"Selected NcKot",Toast.LENGTH_LONG).show();
                }
                else
                {
                    strNcKOTYN="N";
                    objNCKOTActListener.setSelectedOtherOptionsResult(strNcKOTYN, remark,reasonCode);
                }
            }
            else
            {
                Toast.makeText(getActivity(),"please add reasons",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"please add reasons",Toast.LENGTH_LONG).show();
        }
    }

    public void funGetReasonList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetReasonList("strNCKOT", clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReasonMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsReasonMaster> arrListTemp)
                    {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0)
                            {
                                arrReasonList=arrListTemp;
                                hmReason=new HashMap<String,String>();
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    clsReasonMaster objReason=(clsReasonMaster)arrListTemp.get(cnt);
                                    hmReason.put(objReason.getStrReasonName(), objReason.getStrReasonCode());

                                }
                                Set setReason=hmReason.keySet();
                                Iterator itrReason=setReason.iterator();

                                while(itrReason.hasNext())
                                {
                                    reasonName= (String) itrReason.next();
                                    reasonList.add(reasonName);
                                }
                                reasonCode=hmReason.get(reasonName).toString();

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_spinner_item,reasonList);

                                dataAdapter.setDropDownViewResource
                                        (android.R.layout.simple_spinner_dropdown_item);
                                spinnerReason.setAdapter(dataAdapter);

                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
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

    protected void showDialog() {
        if (null == pgDialog) {
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
