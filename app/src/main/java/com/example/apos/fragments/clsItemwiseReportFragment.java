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
import com.example.apos.activity.clsKotScreen;
import com.example.apos.adapter.clsSalesMenuReportAdapter;
import com.example.apos.adapter.clsTotalListAdapter;
import com.example.apos.api.BaseAPIHelper;
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

/**
 * Created by sanguine on 10/24/2015.
 */
public class clsItemwiseReportFragment extends Fragment
{
    private GridView gvItemwiseGridView,gvTotalGridView;
    public String fromDate,toDate,date1,date2;;
    public String posCode;
    ArrayList arrListItemwise = new ArrayList();
    clsSalesMenuReportAdapter objItemWiseAdapter;
    clsTotalListAdapter objTotalAdapter;
    public double totalDiscAmt,totalSubTotal,totalSaleAmt,totalSQty;
    public String disAmt,subTotalAmt;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        // String fromDate = getArguments().getString("FromDate");
        View myFragmentView = inflater.inflate(R.layout.salesmenureportlist, container, false);
        if(!clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
        {
            clsGlobalFunctions.gAPOSWebSrviceURL=clsGlobalFunctions.gAPOSWebSrviceURL+"prjSanguineWebService/APOSIntegration";
        }
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        date1= getArguments().getString("FromDate");
        date2= getArguments().getString("ToDate");

        String []fromdate1=date1.split("/");
        fromDate = fromdate1[2] + "-" + (fromdate1[1]) + "-" + (fromdate1[0]);
        String []todate1=date2.split("/");
        toDate = todate1[2] + "-" + (todate1[1]) + "-" + (todate1[0]);
        posCode=getArguments().getString("PosCode");
        gvItemwiseGridView= (GridView) (myFragmentView).findViewById(R.id.gvSaleReportList);
        gvTotalGridView= (GridView) (myFragmentView).findViewById(R.id.gvSalesTotalList);

        totalDiscAmt = 0;
        totalSubTotal = 0;
        totalSaleAmt = 0;
        totalSQty = 0;

        funGetItemWiseReportData();

        return myFragmentView;
    }





    public void funGetItemWiseReportData()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funSalesReport(posCode,clsGlobalFunctions.gUserCode,fromDate,toDate,"ItemWiseSales", new BaseAPIHelper.OnRequestComplete<ArrayList<clsSalesReportDtl>>() {
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
                                    totalDiscAmt += Double.parseDouble(objSalesReportDtl.getDiscountAmt());
                                    totalSubTotal += Double.parseDouble(objSalesReportDtl.getSubTotal());
                                    totalSaleAmt += Double.parseDouble(objSalesReportDtl.getGrandTotal());
                                    totalSQty += Double.parseDouble(objSalesReportDtl.getStrQuantity());
                                }

                                arrListItemwise = arrListTemp;
                                List<clsSalesReportDtl> arrListItemWise = new ArrayList();
                                List<clsSalesReportDtl> arrTotalList = new ArrayList();

                                clsSalesReportDtl objSaleDtl = new clsSalesReportDtl();
                                objSaleDtl.setStrQuantity(String.valueOf(Math.rint(totalSQty)));
                                objSaleDtl.setGrandTotal(String.valueOf(Math.rint(totalSaleAmt)));
                                objSaleDtl.setSubTotal(String.valueOf(Math.rint(totalSubTotal)));
                                objSaleDtl.setDiscountAmt(String.valueOf(Math.rint(totalDiscAmt)));
                                arrTotalList.add(objSaleDtl);

                                objItemWiseAdapter=new clsSalesMenuReportAdapter(getActivity(),arrListItemwise,"ItemWise");
                                gvItemwiseGridView.setAdapter(objItemWiseAdapter);
                                objTotalAdapter=new clsTotalListAdapter(getActivity(),arrTotalList,"ItemWise");
                                gvTotalGridView.setAdapter(objTotalAdapter);
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
