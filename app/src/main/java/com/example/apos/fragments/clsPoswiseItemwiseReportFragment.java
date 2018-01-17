package com.example.apos.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.adapter.clsPOSwiseAdapter;
import com.example.apos.adapter.clsPoswiseSalesMenuAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.bean.clsSalesReportDtl;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
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
import java.util.List;


public class clsPoswiseItemwiseReportFragment extends Fragment
{
    private GridView gItemWiseGridView;
    clsPoswiseSalesMenuAdapter objAdapter;
    List<clsSalesReportDtl> arrListItemWise = new ArrayList();
    public String fromDate,toDate;
    public double totalSettleAmt=0;
    ArrayList<String> arrPosNameList=new ArrayList<String>();
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        // String fromDate = getArguments().getString("FromDate");
        View myFragmentView = inflater.inflate(R.layout.poswisesalemenureport, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        fromDate= getArguments().getString("FromDate");
        toDate= getArguments().getString("ToDate");
        gItemWiseGridView= (GridView) (myFragmentView).findViewById(R.id.gvPoswiseSalesList);
        funGetItemWiseReportDtl();

        return myFragmentView;
    }


    public void funGetItemWiseReportDtl()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funPOSSalesReport(fromDate,toDate,"ItemWise", new BaseAPIHelper.OnRequestComplete<ArrayList<clsSalesReportDtl>>() {
                    @Override
                    public void onSuccess(ArrayList<clsSalesReportDtl> arrListTemp)
                    {
                        dismissDialog();
                        if (null != arrListTemp)
                        {
                            if (arrListTemp.size()>0)
                            {
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    clsSalesReportDtl objSalesReportDtl=arrListTemp.get(cnt);
                                    List<clsPosSelectionMaster> arrPOSList=objSalesReportDtl.getArrListPosDtl();
                                    for (int k = 0; k < arrPOSList.size(); k++)
                                    {
                                        clsPosSelectionMaster objPos=arrPOSList.get(k);
                                       if(cnt==0)
                                        {
                                            arrPosNameList.add(objPos.getStrPosName());
                                        }
                                    }
                                }

                                arrListItemWise=arrListTemp;
                                if(arrListItemWise.size()>0)
                                {
                                    arrPosNameList.add("Total");
                                    objAdapter=new clsPoswiseSalesMenuAdapter(getActivity(),arrListItemWise,arrPosNameList,"ItemWise");
                                    gItemWiseGridView.setAdapter(objAdapter);
                                }
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
                SnackBarUtils.showSnackBar(getActivity(), R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(getActivity(), R.string.setup_your_server_settings);
        }
    }

    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(getActivity(), 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}