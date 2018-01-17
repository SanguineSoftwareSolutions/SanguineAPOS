package com.example.apos.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsReservationSearchDialogAdapter;
import com.example.apos.adapter.clsUnReservedTableListDialogAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.bean.clsReprintDocumentBean;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.views.CustomSearchView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import butterknife.BindView;


public class clsTableReservation extends Activity implements View.OnClickListener
{
    private ConnectivityManager connectivityManager;
    private List<clsTableMaster> arrListTableMaster =new ArrayList<clsTableMaster>();
    private List<clsReprintDocumentBean> arrListReservation =new ArrayList<clsReprintDocumentBean>();
    private Context context;
    private EditText edtPhoneNo,edtCustomerName,edtPax,edtComments,edtReservationCode;
    private TextView edtShowDate,edtShowTime;
    private Button btnAdd,btnCancel,btnCheckPhoneNO,btnSelectTable,btnReset;
    ImageView imgViewDate,imgViewTime;
    SimpleDateFormat sdf;
    String myFormat ="";
    Calendar calendar;
    GridView dialoglist = null;
    Dialog dialog = null;
    DatePickerDialog.OnDateSetListener datePicker;
    TimePickerDialog.OnClickListener timePicker;
    private String customerCode="",tableNo="",reservationNo="";
    private String keyCase="upperCase";
    private String flgCheck="N";
    public static Activity mActivity;
    private Dialog pgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablereservation);
        mActivity=this;
        widgetInit();
        edtPhoneNo.setFocusable(true);
        CommonUtils.showKeyboard(edtPhoneNo,true);

        datePicker= new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                /*String posDate[]=clsGlobalFunctions.gPOSDate.split("-");
                Calendar cal = Calendar.getInstance();
                int xxday = Integer.parseInt(posDate[2]);
                int xxmonth =Integer.parseInt(posDate[1]);
                int xxyear = Integer.parseInt(posDate[0]);

                cal.set(xxyear, xxmonth, xxday);
                cal.add(Calendar.MONTH, 1);*/

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edtShowDate.setText(sdf.format(calendar.getTime()));
            }

        };

        imgViewDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(clsTableReservation.this, datePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        imgViewTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(clsTableReservation.this,
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
                                edtShowTime.setText(printHour + ":" + printMinute + " " + AM_PM);

                            }
                        }, hour, minute, false);
                tpd.show();

            }
        });
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

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        imgViewDate=(ImageView) findViewById(R.id.imgViewCaledar);
        imgViewTime=(ImageView) findViewById(R.id.imgViewTime);
        edtShowDate=(TextView) findViewById(R.id.edtSelectedDate);
        edtShowTime=(TextView) findViewById(R.id.edtSelectedTime);
        edtReservationCode=(EditText) findViewById(R.id.edtReservationNo);
        edtPhoneNo=(EditText) findViewById(R.id.edtPhoneNo);

        edtCustomerName=(EditText) findViewById(R.id.edtCustomerName);
        edtPax=(EditText) findViewById(R.id.edtPax);

        edtComments=(EditText) findViewById(R.id.edtComments);
        btnSelectTable=(Button) findViewById(R.id.btnAssignTable);
        btnCheckPhoneNO=(Button) findViewById(R.id.btnCheckPhoneNo);
        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnCancel=(Button) findViewById(R.id.btnCancel);
        btnReset=(Button) findViewById(R.id.btnReset);
        edtShowDate=(TextView) findViewById(R.id.edtSelectedDate);
        edtShowTime=(TextView) findViewById(R.id.edtSelectedTime);
        String posDate[]=clsGlobalFunctions.gPOSDate.split("-");
        edtShowDate.setText(posDate[2]+"-"+posDate[1]+"-"+posDate[0]);
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
        edtShowTime.setText(printHour + ":" + printMinute + " " + AM_PM);



        edtReservationCode.setOnClickListener(this);
        edtPhoneNo.setOnClickListener(this);
        edtPax.setOnClickListener(this);
        btnSelectTable.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCheckPhoneNO.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        edtCustomerName.setOnClickListener(this);
    }




    private void funGetClickedPaxNo()
    {
        edtPax.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||(actionId ==0) ) {
                    CommonUtils.hideKeyboard(edtPax);
                    if (edtPax.getText() != null) {
                        if (!edtPax.getText().toString().equals("")) {
                            if(Integer.parseInt(edtPax.getText().toString())>100)
                                Toast.makeText(clsTableReservation.this, "please Enter Valied PAX", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(clsTableReservation.this, "please Enter PAX", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                }
                return false;
            }

        });

    }


    private void funLoadTableSearchDailog()
    {
        funGetUnReservedTableList();
    }

    private void funLoadReservationSearchDailog()
    {
        funGetReservationDetailList();
    }

    private void funResetFields()
    {
        flgCheck="Y";
        btnAdd.setText("Save");
        edtReservationCode.setText("");
        edtPhoneNo.setText("");
        edtCustomerName.setText("");
        edtPax.setText("");
        edtComments.setText("");
        edtShowDate.setText("");
        edtShowTime.setText("");
        btnSelectTable.setText("Select Table");
        customerCode="";
        tableNo="";
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
        edtShowDate.setText(sdf.format(calendar.getTime()));
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
        edtShowTime.setText(printHour + ":" + printMinute + " " + AM_PM);
    }

    private void funSaveReservation()
    {
        if (edtPhoneNo.getText().toString().isEmpty())
        {
            Toast.makeText(clsTableReservation.this, "Please Enter Mobile No", Toast.LENGTH_LONG).show();
        }
        else if(edtPhoneNo.getText().length()<10 || edtPhoneNo.getText().length()>10)
        {
            Toast.makeText(clsTableReservation.this, "Please Enter 10 digit Mobile No", Toast.LENGTH_LONG).show();
        }
        else if(flgCheck.equals("N"))
        {
            Toast.makeText(clsTableReservation.this, "Please Check for existence of customer by mobile no", Toast.LENGTH_LONG).show();
        }
        else if(edtCustomerName.toString().isEmpty())
        {
            Toast.makeText(clsTableReservation.this, "Please Enter customer Name", Toast.LENGTH_LONG).show();
        }
        else if(edtPax.getText().toString().isEmpty())
        {
            Toast.makeText(clsTableReservation.this, "Please Enter pax no", Toast.LENGTH_LONG).show();
        }
        else if(edtShowDate.getText().toString().isEmpty())
        {
            Toast.makeText(clsTableReservation.this, "Please Enter date in proper format", Toast.LENGTH_LONG).show();
        }
        else if(edtShowTime.getText().toString().isEmpty())
        {
            Toast.makeText(clsTableReservation.this, "Please Enter time in proper format", Toast.LENGTH_LONG).show();
        }
        else if(btnSelectTable.getText().toString().equals("Assign Table"))
        {
            Toast.makeText(clsTableReservation.this, "Please select table!!!!", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(customerCode.equals("No Customer Found"))
            {
                //new SaveNewCustomerDtl().execute();
                funSaveNewCustomerDtl();
            }
            else if(customerCode.equals("")){
                funSaveNewCustomerDtl();
            }
            else if(customerCode.contains("C0")){
                funDoneReservation();
            }
        }
    }

    private void funFillTableListDialog(List<clsTableMaster> arrList)
    {
        final List<clsTableMaster> arrListTempTableInfo=arrList;
        ArrayList<String> arrListTableInfo=new ArrayList<String>();
        for(int cnt=0;cnt<arrList.size();cnt++)
        {
            clsTableMaster objTableMaster=arrList.get(cnt);
            arrListTableInfo.add(objTableMaster.getStrTableNo()+"#"+objTableMaster.getStrTableName()
                    +"#"+objTableMaster.getStrTableStatus());
        }

        clsUnReservedTableListDialogAdapter objOrderAdapter = new clsUnReservedTableListDialogAdapter(clsTableReservation.this, arrListTableInfo);
        dialoglist.setAdapter(objOrderAdapter);
        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clsTableMaster objTableMaster = (clsTableMaster) arrListTempTableInfo.get(position);
                btnSelectTable.setText(objTableMaster.getStrTableName());
                tableNo=objTableMaster.getStrTableNo();
                onResume();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.edtReservationNo:
                funLoadReservationSearchDailog();
                break;

            case R.id.edtPhoneNo:
                edtPhoneNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((actionId == EditorInfo.IME_ACTION_DONE) ||(actionId ==0) ) {
                            CommonUtils.hideKeyboard(edtPhoneNo);
                            if (edtPhoneNo.getText() != null) {
                                if (!edtPhoneNo.getText().toString().equals("")) {
                                    if(!(edtPhoneNo.getText().toString().length()==10)){
                                        Toast.makeText(clsTableReservation.this, "Invalied Mobile No", Toast.LENGTH_LONG).show();
                                    }else{
                                        funGetCustomerInfo(edtPhoneNo.getText().toString());
                                    }

                                } else {
                                    Toast.makeText(clsTableReservation.this, "please Mobile No", Toast.LENGTH_LONG).show();
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });
                break;

            case R.id.edtPax:
                funGetClickedPaxNo();
                break;

            case R.id.btnCheckPhoneNo:
                flgCheck="Y";
                if (edtPhoneNo.getText().toString().isEmpty())
                {
                    Toast.makeText(clsTableReservation.this, "Please Enter Mobile No", Toast.LENGTH_LONG).show();
                }
                else if(edtPhoneNo.getText().length()<10)
                {
                    Toast.makeText(clsTableReservation.this, "Please Enter 10 digit Mobile No", Toast.LENGTH_LONG).show();
                }
                else if(edtPhoneNo.getText().length()>10)
                {
                    Toast.makeText(clsTableReservation.this, "Please Enter 10 digit Mobile No", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //new GetCustomerInfo().execute(edtPhoneNo.getText().toString());
                    funGetCustomerInfo(edtPhoneNo.getText().toString());
                }
                break;

            case R.id.btnAssignTable:
                funLoadTableSearchDailog();
                break;

            case R.id.btnAdd:
                funSaveReservation();
                break;

            case R.id.btnCancel:
                funResetFields();
                finish();
                break;

            case R.id.btnReset:
                funResetFields();
                break;

            default:
                break;
        }

    }

    private void funFillTableSearchDailog(List<clsTableMaster> arrList)
    {
        CustomSearchView customSearchView;
        dialog = new Dialog(clsTableReservation.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.unreservedtablesearchmaindialogform);
        customSearchView=(CustomSearchView) dialog.findViewById(R.id.edtSearchTable);
        final EditText searchTable= customSearchView.getEditText();
        dialoglist = (GridView) dialog.findViewById(R.id.gvtablesearchdialoglistview);

        searchTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ArrayList arrayListtemp = new ArrayList();
                for (int cnt = 0; cnt <arrListTableMaster.size(); cnt++)
                {
                    clsTableMaster objTable = (clsTableMaster) arrListTableMaster.get(cnt);
                    if (objTable.getStrTableName().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        arrayListtemp.add(arrListTableMaster.get(cnt));
                    }
                }
                funFillTableListDialog(arrayListtemp);

}

    @Override
    public void afterTextChanged(Editable s) {

    }
});


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(500, 750);
        funFillTableListDialog(arrList);
        }

    private void funFillReservationListSearchDailog(List<clsReprintDocumentBean> arrList)
    {
        dialog = new Dialog(clsTableReservation.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reservationlistsearchmaindialogform);
        CustomSearchView customSearchView=(CustomSearchView) dialog.findViewById(R.id.edtSearchReservation);
        final EditText searchReservation=customSearchView.getEditText();
        dialoglist = (GridView) dialog.findViewById(R.id.gvreservationsearchdialoglistview);

        searchReservation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ArrayList arrayListtemp = new ArrayList();
                for (int cnt = 0; cnt <arrListReservation.size(); cnt++)
                {
                    clsReprintDocumentBean objreservation= (clsReprintDocumentBean) arrListReservation.get(cnt);
                    if (objreservation.getStrDocNo().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        arrayListtemp.add(arrListReservation.get(cnt));
                    }
                }
                funFillReservationListDialog(arrayListtemp);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(500, 750);
        funFillReservationListDialog(arrList);
    }

    private void funFillReservationListDialog(List<clsReprintDocumentBean> arrList)
    {
        final List<clsReprintDocumentBean> arrListTempReservationInfo=arrList;
        ArrayList<String> arrListReservationInfo=new ArrayList<String>();
        for(int cnt=0;cnt<arrList.size();cnt++)
        {
            clsReprintDocumentBean objResrvation=arrList.get(cnt);
            arrListReservationInfo.add(objResrvation.getStrDocNo()+"#"+objResrvation.getStrCustomerName()
                    +"#"+objResrvation.getStrMemberCode()+"#"+objResrvation.getStrDate());
        }

        clsReservationSearchDialogAdapter objAdapter = new clsReservationSearchDialogAdapter(clsTableReservation.this, arrListReservationInfo);
        dialoglist.setAdapter(objAdapter);
        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clsReprintDocumentBean objRes = (clsReprintDocumentBean) arrListTempReservationInfo.get(position);
                edtReservationCode.setText(objRes.getStrDocNo());
                edtPhoneNo.setText(objRes.getStrCardNo());
                edtCustomerName.setText(objRes.getStrCustomerName());
                edtPax.setText(objRes.getStrUser());
                edtComments.setText(objRes.getStrDocSlipNo());
                String []spDate=objRes.getStrDate().split("-");
                edtShowDate.setText(spDate[2]+"-"+spDate[1]+"-"+spDate[0]);
                edtShowTime.setText(objRes.getStrTime());
                btnSelectTable.setText(objRes.getStrMemberCode());
                customerCode=objRes.getStrMode();
                tableNo = objRes.getStrCardTypeCode();
                reservationNo=objRes.getStrDocNo();
                btnAdd.setText("Update");
                onResume();
                dialog.dismiss();
            }
        });
    }

    private void funSendSMS()
    {
        JsonObject objSendSMSDtl=new JsonObject();
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                try {
                    JsonArray jArr=new JsonArray();
                    String messageData=edtPhoneNo.getText().toString()+"_"+edtCustomerName.getText().toString()+"_"+"Hello...Welcome To "+clsGlobalFunctions.gClientName;
                    // arrMessageDtlList.add(messageData);
                    jArr.add(messageData);
                    JSONObject obj=new JSONObject();
                    objSendSMSDtl.addProperty("posCode", clsGlobalFunctions.gPOSCode);
                    objSendSMSDtl.addProperty("clientCode", clsGlobalFunctions.gClientCode);
                    objSendSMSDtl.add("jArr", jArr);
                }
                catch(Exception e){

                }
                showDialog();
                App.getAPIHelper().funSendSMSWS(objSendSMSDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try
                            {
                                String res=jObj.get("returnResult").getAsString();
                                System.out.println("result "+res);

                                if(res.equals("true"))
                                    Toast.makeText(clsTableReservation.this, "Reserved Table SuccessFully!!!",Toast.LENGTH_SHORT).show();
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
                                    for(int cnt=0;cnt<arrayList.size();cnt++)
                                    {
                                        clsCustomerMaster objCustomerMaster = (clsCustomerMaster) arrayList.get(cnt);
                                        customerCode=objCustomerMaster.getCustomerCode();
                                        edtCustomerName.setText(objCustomerMaster.getCustomerName());
                                        edtPax.requestFocus();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(clsTableReservation.this,"Customer Not Found",Toast.LENGTH_LONG).show();
                                    edtCustomerName.requestFocus();

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

    private void funGetReservationDetailList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();

                App.getAPIHelper().funGetReservationDetailList("TableReservation",clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray mJsonArray) {
                        dismissDialog();
                        if (null != mJsonArray) {
                            try
                            {
                                List<clsReprintDocumentBean> arrListTemp=new ArrayList<clsReprintDocumentBean>();
                                JsonObject mJsonObject = new JsonObject();
                                for (int i = 0; i < mJsonArray.size(); i++) {
                                    mJsonObject = (JsonObject) mJsonArray.get(i);
                                    if (mJsonObject.get("ReservationNo").getAsString().toString().equals(""))
                                    {
                                        //memberInfo = "no data";
                                    }
                                    else
                                    {
                                        clsReprintDocumentBean objReservation = new clsReprintDocumentBean();
                                        objReservation.setStrDocNo(mJsonObject.get("ReservationNo").getAsString().toString());      //ReservationNo
                                        objReservation.setStrMode(mJsonObject.get("CustomerCode").getAsString().toString());        //Customer Code
                                        objReservation.setStrCustomerName(mJsonObject.get("CustomerName").getAsString().toString());  //Customer Name
                                        objReservation.setStrCardNo(mJsonObject.get("MobileNo").getAsString().toString());            //Mobile No
                                        objReservation.setStrCardTypeCode(mJsonObject.get("TableNo").getAsString().toString());       //Table No
                                        objReservation.setStrMemberCode(mJsonObject.get("TableName").getAsString().toString());       //Table Name
                                        objReservation.setStrUser(mJsonObject.get("PaxNo").getAsString().toString());                  //pax
                                        objReservation.setStrDate(mJsonObject.get("ResDate").getAsString().toString());              //Reservation Date
                                        objReservation.setStrTime(mJsonObject.get("ResTime").getAsString().toString());             //Reservation Time
                                        objReservation.setStrDocSlipNo(mJsonObject.get("Comments").getAsString().toString());       //Comments
                                        arrListTemp.add(objReservation);
                                    }
                                }

                                if (arrListTemp.size()>0)
                                {
                                    arrListReservation.clear();
                                    flgCheck="Y";
                                    for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                    {
                                        clsReprintDocumentBean objTableMaster=(clsReprintDocumentBean)arrListTemp.get(cnt);
                                        arrListReservation.add(objTableMaster);
                                    }
                                    funFillReservationListSearchDailog(arrListTemp);

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
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {

                String regStatus="false";
                String custTypeCode="CT001";
                Date dt=new Date();
                String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                dateTime=dateTime.replaceAll(" ","%20");

                String custname= edtCustomerName.getText().toString().replaceAll(" ","%20");
                String MobileNo=edtPhoneNo.getText().toString();
                String Address="Pune"; // because default assigned.. edtAddress.getText().toString();

                App.getAPIHelper().funSaveNewCustomerDtl(custname,MobileNo,custTypeCode,Address,clsGlobalFunctions.gClientCode,clsGlobalFunctions.gUserCode,dateTime,new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {

                        if (null != jObj) {
                            try{
                                String reason=jObj.get("Reason").getAsString().toString();//1
                                String customerStatus=jObj.get("CustomerStatus").getAsString().toString();///0

                                if (reason.equals("Exception"))
                                {
                                    Toast.makeText(mActivity,"problem while save new customer",Toast.LENGTH_LONG).show();

                                }

                                if (customerStatus.equals("false")) {

                                    if(reason.equals("MobileNo"))
                                    {
                                        Toast.makeText(mActivity, "Mobile No is already registered!!!", Toast.LENGTH_LONG).show();
                                    }

                                    else
                                    {
                                        Toast.makeText(mActivity, "Failed!!!", Toast.LENGTH_LONG).show();
                                    }

                                }
                                else
                                {
                                    customerCode=customerStatus;
                                    Toast.makeText(clsTableReservation.this, "Customer Entry Added Successfully "+customerCode, Toast.LENGTH_LONG).show();
                                    funDoneReservation();
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

    private void funGetUnReservedTableList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetUnReservedTableList("UnReservedTableList",clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            try
                            {
                                if (arrListTemp.size()>0)
                                {
                                    arrListTableMaster.clear();
                                    for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                    {
                                        clsTableMaster objTableMaster=(clsTableMaster)arrListTemp.get(cnt);
                                        arrListTableMaster.add(objTableMaster);
                                    }
                                    funFillTableSearchDailog(arrListTemp);

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

    private void funDoneReservation()
    {
        JsonObject objReservationDtl=new JsonObject();
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                try{

                    JsonArray arrKOTClass = new JsonArray();
                    JsonObject objRows=new JsonObject();
                    String []spDate=edtShowDate.getText().toString().split("-");
                    String resDate=spDate[2]+"-"+spDate[1]+"-"+spDate[0];
                    String []spTime=edtShowTime.getText().toString().split(" ");

                    objRows.addProperty("strCustomerCode",customerCode);
                    objRows.addProperty("intPax",edtPax.getText().toString());
                    objRows.addProperty("strSmoking","N");
                    objRows.addProperty("dteResDate",resDate);
                    objRows.addProperty("tmeResTime",spTime[0]);
                    objRows.addProperty("strAMPM",spTime[1]);
                    objRows.addProperty("strSpecialInfo",edtComments.getText().toString());
                    objRows.addProperty("strTableNo",tableNo);
                    objRows.addProperty("strUserCreated",clsGlobalFunctions.gUserCode);
                    objRows.addProperty("strUserEdited",clsGlobalFunctions.gUserCode);
                    objRows.addProperty("dteDateCreated", clsGlobalFunctions.funGetPOSDateTime());
                    objRows.addProperty("dteDateEdited", clsGlobalFunctions.funGetPOSDateTime());
                    objRows.addProperty("strClientCode",clsGlobalFunctions.gClientCode);
                    objRows.addProperty("strReservationCode",reservationNo);
                    objRows.addProperty("strPosCode",clsGlobalFunctions.gPOSCode);
                    arrKOTClass.add(objRows);

                    objReservationDtl.add("ReservationDtl",arrKOTClass);

                }catch(Exception e){

                }
                showDialog();
                App.getAPIHelper().funDoneReservation(objReservationDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try
                            {
                                String reservationCode=jObj.get("reservationCode").getAsString();
                                Toast.makeText(clsTableReservation.this, "Reservation No- " + reservationCode,Toast.LENGTH_SHORT).show();
                                if(clsGlobalFunctions.gEnableTableReservationForCustomer.equals("Y"))
                                {
                                    funSendSMS();
                                }
                                funResetFields();
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
            pgDialog = CommonUtils.getProgressDialog(clsTableReservation.mActivity, 0, false);
        }
        pgDialog.show();
    }
    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}
