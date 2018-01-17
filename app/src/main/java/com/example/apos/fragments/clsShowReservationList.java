package com.example.apos.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsReservation;
import com.example.apos.adapter.clsReservationListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsReprintDocumentBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.util.clsPrintDemo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class clsShowReservationList extends Fragment implements clsReservationListAdapter.customButtonListener,View.OnClickListener
{
    private TextView edtFromDate,edtFromTime,edtToDate,edtToTime;
    private Button btnExecute,btnCancel,btnReset;
    ImageView imgViewFromDate,imgViewFromTime,imgViewToDate,imgViewToTime;
    SimpleDateFormat sdf;
    String myFormat ="";
    Calendar calendar;
    Dialog dialog = null;
    DatePickerDialog.OnDateSetListener datePickerFromDate,datePickerToDate;
    GridView gvReservationList;
    MACHServices machService;
    Button btnClose;
    private clsReservationListAdapter reservationListAdapter;
    public static Activity mActivity;
    private Dialog pgDialog;
    private ConnectivityManager connectivityManager;

    public static clsShowReservationList getInstance() {
        clsShowReservationList mshowResFragment = new clsShowReservationList();
        return mshowResFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reservationlist, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mActivity= clsReservation.mActivity;

        InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        myFormat = "dd-MM-yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String AM_PM ;
        if(hour < 12)
        {
            AM_PM = "AM";
        }
        else
        {
            if(hour!=12)
            {
                hour=hour-12;
            }
            AM_PM = "PM";
        }

        imgViewFromDate=(ImageView) rootView.findViewById(R.id.imgViewFromDate);
        imgViewToDate=(ImageView) rootView.findViewById(R.id.imgViewToDate);
        imgViewFromTime=(ImageView) rootView.findViewById(R.id.imgViewFromTime);
        imgViewToTime=(ImageView) rootView.findViewById(R.id.imgViewToTime);
        edtFromDate=(TextView) rootView.findViewById(R.id.edtFromDate);
        edtFromTime=(TextView) rootView.findViewById(R.id.edtFromTime);
        edtToDate=(TextView) rootView.findViewById(R.id.edtToDate);
        edtToTime=(TextView) rootView.findViewById(R.id.edtToTime);
        btnExecute=(Button) rootView.findViewById(R.id.btnExecute);
        btnCancel=(Button) rootView.findViewById(R.id.btnCancel);
        btnReset=(Button) rootView.findViewById(R.id.btnReset);
        btnCancel=(Button) rootView.findViewById(R.id.btnCancel);
        gvReservationList = (GridView) rootView.findViewById(R.id.gvReservationList);

        String posDate[]= clsGlobalFunctions.gPOSDate.split("-");
        edtFromDate.setText(posDate[2]+"-"+posDate[1]+"-"+posDate[0]);//sdf.format(calendar.getTime())
        edtToDate.setText(posDate[2]+"-"+posDate[1]+"-"+posDate[0]);//(sdf.format(calendar.getTime()));
        String printHour="";
        String printMinute="";
        if(hour<10)
        {
            printHour="0"+hour;
        }
        else
        {
            printHour=String.valueOf(hour);
        }
        if(minute<10)
        {
            printMinute="0"+minute;
        }
        else
        {
            printMinute=String.valueOf(minute);
        }
        edtFromTime.setText("12" + ":" + "01" + " " + "AM");
        edtToTime.setText(printHour + ":" + printMinute + " " + AM_PM);

        btnExecute.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnReset.setOnClickListener(this);



        datePickerFromDate= new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edtFromDate.setText(sdf.format(calendar.getTime()));
            }

        };

        datePickerToDate= new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edtToDate.setText(sdf.format(calendar.getTime()));
            }

        };

        imgViewFromDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mActivity, datePickerFromDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        imgViewToDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mActivity, datePickerToDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        imgViewFromTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(mActivity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                            {
                                String AM_PM ;
                                if(hourOfDay < 12)
                                {
                                    AM_PM = "AM";
                                }
                                else
                                {
                                    if(hourOfDay!=12)
                                    {
                                        hourOfDay=hourOfDay-12;
                                    }
                                    AM_PM = "PM";
                                }
                                String printHour="";
                                String printMinute="";
                                if(hourOfDay<10)
                                {
                                    printHour="0"+hourOfDay;
                                }
                                else
                                {
                                    printHour=String.valueOf(hourOfDay);
                                }
                                if(minute<10)
                                {
                                    printMinute="0"+minute;
                                }
                                else
                                {
                                    printMinute=String.valueOf(minute);
                                }
                                edtFromTime.setText(printHour + ":" + printMinute + " " + AM_PM);

                            }
                        }, hour, minute, false);
                tpd.show();

            }
        });

        imgViewToTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(mActivity,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                            {
                                String AM_PM ;
                                if(hourOfDay < 12)
                                {
                                    AM_PM = "AM";
                                }
                                else
                                {
                                    if(hourOfDay!=12)
                                    {
                                        hourOfDay=hourOfDay-12;
                                    }
                                    AM_PM = "PM";
                                }
                                String printHour="";
                                String printMinute="";
                                if(hourOfDay<10)
                                {
                                    printHour="0"+hourOfDay;
                                }
                                else
                                {
                                    printHour=String.valueOf(hourOfDay);
                                }
                                if(minute<10)
                                {
                                    printMinute="0"+minute;
                                }
                                else
                                {
                                    printMinute=String.valueOf(minute);
                                }
                                edtToTime .setText(printHour + ":" + printMinute + " " + AM_PM);

                            }
                        }, hour, minute, false);
                tpd.show();

            }
        });
        funShowReservationListWS();
        return rootView;
    }

    @Override
    public void onButtonClickListner(int position, String value) {

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnExecute:
                //new ShowReservationListWS().execute();
                funShowReservationListWS();
                break;

            case R.id.btnCancel:
                mActivity.finish();
                break;

            case R.id.btnReset:
                funResetFields();
                break;

            default:
                break;
        }
    }

    private void funResetFields()
    {
        myFormat = "dd-MM-yyyy";
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String AM_PM ;
        if(hour < 12)
        {
            AM_PM = "AM";
        }
        else
        {
            if(hour!=12)
            {
                hour=hour-12;
            }
            AM_PM = "PM";
        }
        edtFromDate.setText(sdf.format(calendar.getTime()));
        edtToDate.setText(sdf.format(calendar.getTime()));
        String printHour="";
        String printMinute="";
        if(hour<10)
        {
            printHour="0"+hour;
        }
        else
        {
            printHour=String.valueOf(hour);
        }
        if(minute<10)
        {
            printMinute="0"+minute;
        }
        else
        {
            printMinute=String.valueOf(minute);
        }
        edtFromTime.setText(printHour + ":" + printMinute + " " + AM_PM);
        edtToTime.setText(printHour + ":" + printMinute + " " + AM_PM);
        funFillGridView(new ArrayList());

    }


    private void funFillGridView(ArrayList arrlist)
    {
        reservationListAdapter = new clsReservationListAdapter(mActivity, arrlist);
        reservationListAdapter.setCustomButtonListner(this);
        gvReservationList.setAdapter(reservationListAdapter);
    }


    private void funShowReservationListWS()
    {

        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {

                String recordType="",fromDate="",toDate="",fromTime="",toTime="";
                String [] spFromDate=edtFromDate.getText().toString().split("-");
                fromDate=spFromDate[2]+"-"+spFromDate[1]+"-"+spFromDate[0];
                String [] spToDate=edtToDate.getText().toString().split("-");
                toDate= spToDate[2]+"-"+spToDate[1]+"-"+spToDate[0];
                String [] spFromTime=edtFromTime.getText().toString().split(" ");
                fromTime=spFromTime[0]+ ":00";
                String [] spToTime=edtToTime.getText().toString().split(" ");
                toTime=spToTime[0]+ ":00";

                showDialog();

                App.getAPIHelper().funGetReservationDetail(clsGlobalFunctions.gPOSCode,fromDate,toDate,fromTime,toTime,new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray mJsonArray) {
                        dismissDialog();
                        if (null != mJsonArray) {
                            try
                            {
                                ArrayList arrListReservation= new ArrayList();
                                if(mJsonArray.size()>0){
                                    JsonObject mJsonObject=new JsonObject();
                                    for (int i = 0; i < mJsonArray.size(); i++) {
                                        mJsonObject = (JsonObject) mJsonArray.get(i);
                                        if (mJsonObject.get("ReservationNo").toString().equals("")) {
                                        }
                                        else
                                        {
                                            clsReprintDocumentBean objReservation = new clsReprintDocumentBean();
                                            objReservation.setStrDocNo(mJsonObject.get("ReservationNo").getAsString().toString());
                                            objReservation.setStrCardNo(mJsonObject.get("MobileNo").getAsString().toString());
                                            objReservation.setStrCustomerName(mJsonObject.get("CustomerName").getAsString().toString());
                                            objReservation.setStrMode(mJsonObject.get("SmokingY/N").getAsString().toString());
                                            objReservation.setStrMemberCode(mJsonObject.get("TableName").getAsString().toString());
                                            objReservation.setStrUser(mJsonObject.get("Pax").getAsString().toString());
                                            objReservation.setStrDate(mJsonObject.get("ResDate").getAsString().toString());
                                            objReservation.setStrTime(mJsonObject.get("ResTime").getAsString().toString());
                                            objReservation.setStrDocSlipNo(mJsonObject.get("Comments").getAsString().toString());
                                            arrListReservation.add(objReservation);

                                        }
                                    }
                                    if (arrListReservation.size() > 0)
                                    {
                                        funFillGridView(arrListReservation);
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
            pgDialog = CommonUtils.getProgressDialog(clsShowReservationList.mActivity, 0, false);
        }
        pgDialog.show();
    }
    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}
