package com.example.apos.fragments;

/**
 * Created by User on 31-03-2017.
 */

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
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
import java.util.List;
import java.util.Map;

public class clsPieChartReportFragment  extends Fragment
{
    public String fromDate,toDate,reportType;
    List<clsSalesReportDtl> arrListPOSWise = new ArrayList();
    public double totalSettleAmt,totalGrandAmt;
    public String taxAmt,disAmt,subTotalAmt,settelAmt;
    RelativeLayout LayoutToDisplayChart;
    ArrayList<String> arrPosNameList=new ArrayList<String>();
    List<clsSalesReportDtl> arrListMenuWise = new ArrayList();
    HashMap<String,clsSalesReportDtl> mapChartData=new HashMap<String,clsSalesReportDtl>();
    Context context;
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;
    private PieChart mChart;
    private Dialog pgDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        // TODO Auto-generated method stub
        View myFragmentView = inflater.inflate(R.layout.piechartexample, container, false);
        fromDate= getArguments().getString("FromDate");
        toDate= getArguments().getString("ToDate");
        reportType= getArguments().getString("ReportType");
        LayoutToDisplayChart= (RelativeLayout) (myFragmentView).findViewById(R.id.linear);
        mChart = new PieChart(this.getActivity());

        LayoutToDisplayChart.addView(mChart);
        LayoutToDisplayChart.setBackgroundColor(Color.parseColor("#ffffff"));

        // configure pie chart
        mChart.setUsePercentValues(false);
        mChart.setDescription("");

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(0);
        mChart.setTransparentCircleRadius(0);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;


            }

            @Override
            public void onNothingSelected() {

            }
        });



        try
        {

            if(reportType.equals("POSWise"))
            {
                funGetPOSWiseReportDtl();
            }

            else if(reportType.equals("ItemWise"))
            {
                funGetItemWiseReportDtl("ItemWise");
            }

            else if(reportType.equals("MenuHeadWise"))
            {
                funGetItemWiseReportDtl("MenuHeadWise");
            }

            else if(reportType.equals("GroupWise"))
            {
                funGetItemWiseReportDtl("GroupWise");
            }

            else if(reportType.equals("SubGroupWise"))
            {
                funGetItemWiseReportDtl("SubGroupWise");
            }

        }
        catch (Exception e)
        {
         e.printStackTrace();
        }

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
        if(reportType.equals("ItemWise"))
        {
            l.setTextSize(6f);
        }
        else if(reportType.equals("MenuHeadWise"))
        {
            l.setTextSize(7f);
        }
        else if(reportType.equals("GroupWise"))
        {
            l.setTextSize(8f);
        }
        else if(reportType.equals("SubGroupWise"))
        {
            l.setTextSize(7f);
        }

        return myFragmentView;
    }



    private void addData()
    {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        if(reportType.equals("POSWise"))
        {
            for(int cnt=0;cnt<arrListPOSWise.size();cnt++)
            {
                clsSalesReportDtl obj = arrListPOSWise.get(cnt);
                yVals1.add(new Entry(Float.valueOf(obj.getDblSettleAmt()), cnt));
                double per=Math.rint(((Double.parseDouble(obj.getDblSettleAmt())/totalSettleAmt)*100));
                String[] perValue=String.valueOf(Math.rint(per)).split("\\.");
                xVals.add(obj.getPosName()+" ("+ perValue[0]+"%)" );

                PieDataSet dataSet = new PieDataSet(yVals1, "");
                dataSet.setSliceSpace(0);
                dataSet.setSelectionShift(5);



                dataSet.setColors(colors);
                PieData data = new PieData(xVals, dataSet);
                // data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(9f);
                data.setValueTextColor(Color.BLACK);

                mChart.setData(data);

                // undo all highlights
                mChart.highlightValues(null);
                mChart.setDrawSliceText(false);
                mChart.setDrawCenterText(false);

                // update pie chart
                mChart.invalidate();
                mChart.animateXY(1400, 1400);
                mChart.getLegend().setEnabled(true);
            }
        }
        else
        {
            if(mapChartData.size()>0)
            {
                int cnt=0;
                for (Map.Entry<String, clsSalesReportDtl> entry : mapChartData.entrySet())
                {
                    clsSalesReportDtl obj = entry.getValue();
                    double totalAmt=0;
                    yVals1.add(new Entry(Float.valueOf(String.valueOf(obj.getDblSettleAmt())), cnt));
                    totalAmt=Double.valueOf(obj.getDblSettleAmt());
                    double per=Math.rint(((totalAmt/totalSettleAmt)*100));
                    String[] perValue=String.valueOf(Math.rint(per)).split("\\.");
                    xVals.add(obj.getStrItemName()+" ("+ perValue[0]+"%)" );


                    PieDataSet dataSet = new PieDataSet(yVals1, "");
                    dataSet.setSliceSpace(0);
                    dataSet.setSelectionShift(5);

                    dataSet.setColors(colors);

                    // instantiate pie data object now
                    PieData data = new PieData(xVals, dataSet);
                    // data.setValueFormatter(new PercentFormatter());
                if(reportType.equals("ItemWise"))
                {
                    data.setValueTextSize(5f);
                }
                else if(reportType.equals("MenuHeadWise"))
                {
                    data.setValueTextSize(6f);
                }
                else if(reportType.equals("GroupWise"))
                {
                    data.setValueTextSize(7f);
                }
                else if(reportType.equals("SubGroupWise"))
                {
                    data.setValueTextSize(6f);
                }


                    if(mapChartData.size()>5)
                    {
                        data.setValueTextColor(Color.WHITE);
                    }
                    else
                    {
                        data.setValueTextColor(Color.BLACK);
                    }


                    mChart.setData(data);

                    // undo all highlights
                    mChart.highlightValues(null);
                    mChart.setDrawSliceText(false);
                    mChart.setDrawCenterText(false);

                    // update pie chart
                    mChart.invalidate();
                    mChart.animateXY(1400, 1400);
                    mChart.getLegend().setEnabled(true);
                    cnt++;
                }
            }

        }


    }









    protected DefaultRenderer buildCategoryRenderer(int[] colors)
    {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();

            r.setColor(color);
            renderer.addSeriesRenderer(r);

        }
        return renderer;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mChartView == null) {
            mChartView = ChartFactory.getPieChartView(this.getActivity(), mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);

            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();

                }
            });

            mChartView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    return true;
                }
            });
            LayoutToDisplayChart.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        else {
            mChartView.repaint();
        }
    }


    public void funGetPOSWiseReportDtl()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funPOSSalesReport(fromDate,toDate,"POSWise", new BaseAPIHelper.OnRequestComplete<ArrayList<clsSalesReportDtl>>() {
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
                                    totalSettleAmt+=Double.parseDouble(objSalesReportDtl.getDblSettleAmt());
                                    totalGrandAmt += Double.parseDouble(objSalesReportDtl.getGrandTotal());
                                }

                                arrListPOSWise=arrListTemp;
                                addData();
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

    public void funGetItemWiseReportDtl(String reportType)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funPOSSalesReport(fromDate,toDate,reportType, new BaseAPIHelper.OnRequestComplete<ArrayList<clsSalesReportDtl>>() {
                    @Override
                    public void onSuccess(ArrayList<clsSalesReportDtl> arrListTemp)
                    {
                        dismissDialog();
                        if (null != arrListTemp)
                        {
                            if (arrListTemp.size()>0)
                            {
                                clsSalesReportDtl objSaleDtl = null;
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    objSaleDtl=arrListTemp.get(cnt);
                                    if(mapChartData.size()>0)
                                    {
                                        if(mapChartData.containsKey(objSaleDtl.getStrItemCode()))
                                        {
                                            objSaleDtl = mapChartData.get(objSaleDtl.getStrItemCode());
                                            double amt=Double.valueOf(objSaleDtl.getDblSettleAmt());
                                            List<clsPosSelectionMaster> arrPOSList=objSaleDtl.getArrListPosDtl();
                                            double totalAmt=0;
                                            for (int k = 0; k < arrPOSList.size(); k++)
                                            {
                                                clsPosSelectionMaster objPos = arrPOSList.get(k);
                                                totalSettleAmt+=objPos.getDblAmount();
                                                totalAmt+=objPos.getDblAmount();
                                            }
                                            objSaleDtl.setDblSettleAmt(String.valueOf(totalAmt));
                                            mapChartData.put(objSaleDtl.getStrItemCode(),objSaleDtl);

                                        }
                                        else
                                        {
                                            List<clsPosSelectionMaster> arrPOSList=objSaleDtl.getArrListPosDtl();
                                            double totalAmt=0;
                                            for (int k = 0; k < arrPOSList.size(); k++)
                                            {
                                                clsPosSelectionMaster objPos = arrPOSList.get(k);
                                                totalSettleAmt+=objPos.getDblAmount();
                                                totalAmt+=objPos.getDblAmount();
                                            }
                                            objSaleDtl.setDblSettleAmt(String.valueOf(totalAmt));
                                            mapChartData.put(objSaleDtl.getStrItemCode(),objSaleDtl);
                                        }

                                    }
                                    else
                                    {
                                        List<clsPosSelectionMaster> arrPOSList=objSaleDtl.getArrListPosDtl();
                                        double totalAmt=0;
                                        for (int k = 0; k < arrPOSList.size(); k++)
                                        {
                                            clsPosSelectionMaster objPos = arrPOSList.get(k);
                                            totalSettleAmt+=objPos.getDblAmount();
                                            totalAmt+=objPos.getDblAmount();;
                                        }
                                        objSaleDtl.setDblSettleAmt(String.valueOf(totalAmt));
                                        mapChartData.put(objSaleDtl.getStrItemCode(),objSaleDtl);
                                    }
                                }

                                addData();
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





