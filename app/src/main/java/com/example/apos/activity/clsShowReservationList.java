package com.example.apos.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class clsShowReservationList extends Activity implements clsReservationListAdapter.customButtonListener,View.OnClickListener
{
    private EditText edtFromDate,edtFromTime,edtToDate,edtToTime;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservationlist);
        mActivity=this;
        widgetInit();

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
                new DatePickerDialog(clsShowReservationList.this, datePickerFromDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        imgViewToDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(clsShowReservationList.this, datePickerToDate, calendar
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
                TimePickerDialog tpd = new TimePickerDialog(clsShowReservationList.this,
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
                TimePickerDialog tpd = new TimePickerDialog(clsShowReservationList.this,
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

        //new ShowReservationListWS().execute();
        funShowReservationListWS();

    }

    private void widgetInit()
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

        imgViewFromDate=(ImageView) findViewById(R.id.imgViewFromDate);
        imgViewToDate=(ImageView) findViewById(R.id.imgViewToDate);
        imgViewFromTime=(ImageView) findViewById(R.id.imgViewFromTime);
        imgViewToTime=(ImageView) findViewById(R.id.imgViewToTime);
        edtFromDate=(EditText) findViewById(R.id.edtFromDate);
        edtFromTime=(EditText) findViewById(R.id.edtFromTime);
        edtToDate=(EditText) findViewById(R.id.edtToDate);
        edtToTime=(EditText) findViewById(R.id.edtToTime);
        btnExecute=(Button) findViewById(R.id.btnExecute);
        btnCancel=(Button) findViewById(R.id.btnCancel);
        btnReset=(Button) findViewById(R.id.btnReset);
        btnCancel=(Button) findViewById(R.id.btnCancel);
        gvReservationList = (GridView) findViewById(R.id.gvReservationList);

        String posDate[]=clsGlobalFunctions.gPOSDate.split("-");
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
                finish();
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
        reservationListAdapter = new clsReservationListAdapter(clsShowReservationList.this, arrlist);
        reservationListAdapter.setCustomButtonListner(clsShowReservationList.this);
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
