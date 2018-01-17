package com.example.apos.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.apos.App;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.fragments.clsBillwiseReportFragment;
import com.example.apos.fragments.clsItemwiseReportFragment;
import com.example.apos.fragments.clsMenuwiseReportFragment;
import com.example.apos.fragments.clsSettelmentwiseReportFragment;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class clsSalesReportScreen extends Activity implements View.OnClickListener
{
    @BindView(R.id.edtFromDate)
    EditText editFromDate;
    @BindView(R.id.edtToDate)
    EditText editToDate;

    Fragment fragment;
    Button buttonBillwise, buttonSettelmentwise,buttonItemwise,buttonMenuwise;
    ImageView imgViewClose;
    ArrayList arrPosList;
    private TreeMap hmPos;
    private Spinner spinnerPos;
    private String posCode;
    Calendar myCalendarTo,myCalendarFrom;
    SimpleDateFormat sdf;
    DatePickerDialog.OnDateSetListener toDatePicker,fromDatePicker;
    private ConnectivityManager connectivityManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.salesflashreportscreen);
        ButterKnife.bind(this);
        widgetInit();
        String date1=editFromDate.getText().toString();
        String date2=editToDate.getText().toString();

        funGetPOSList();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle1 = new Bundle();
        bundle1.putString("FromDate", date1);
        bundle1.putString("ToDate", date2);
        bundle1.putString("PosCode", posCode);
        clsBillwiseReportFragment myFragment1 = new clsBillwiseReportFragment();
        myFragment1.setArguments(bundle1);
        fragmentTransaction.add(R.id.myfragment, myFragment1);
        fragmentTransaction.commit();

        toDatePicker= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarTo.set(Calendar.YEAR, year);
                myCalendarTo.set(Calendar.MONTH, monthOfYear);
                myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDate();
            }

        };

        fromDatePicker= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarFrom.set(Calendar.YEAR, year);
                myCalendarFrom.set(Calendar.MONTH, monthOfYear);
                myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }

        };



        editFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideDefaultKeyboard();
                new DatePickerDialog(clsSalesReportScreen.this, fromDatePicker, myCalendarFrom
                        .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                        myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideDefaultKeyboard();
                new DatePickerDialog(clsSalesReportScreen.this, toDatePicker, myCalendarTo
                        .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    private void widgetInit()
    {
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        posCode="All";
        buttonBillwise = (Button)findViewById(R.id.btnBillwise);
        buttonSettelmentwise = (Button)findViewById(R.id.btnSettelmentwise);
        buttonItemwise = (Button)findViewById(R.id.btnItemwise);
        buttonMenuwise = (Button)findViewById(R.id.btnMenuwise);
        buttonBillwise.setOnClickListener(this);
        buttonSettelmentwise.setOnClickListener(this);
        buttonItemwise.setOnClickListener(this);
        buttonMenuwise.setOnClickListener(this);
        editFromDate.setFocusable(false);
        editFromDate.setFocusableInTouchMode(true);
        editFromDate.setKeyListener(null);
        editToDate.setFocusable(false);
        editToDate.setFocusableInTouchMode(true);
        editToDate.setKeyListener(null);

        String myFormat = "dd/MM/yyyy";
        System.out.println(clsGlobalFunctions.gPOSDate);
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        String []posDate=clsGlobalFunctions.gPOSDate.split("-");
        String date1 = posDate[2] + "/" + (posDate[1]) + "/" + (posDate[0]);
        editFromDate.setText(date1);
        editToDate.setText(date1);
        myCalendarTo = Calendar.getInstance();
        myCalendarFrom = Calendar.getInstance();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(clsSalesReportScreen.this, clsMainMenu.class);
        intent.putExtra("PosName", clsGlobalFunctions.gPOSName);
        intent.putExtra("PosCode", clsGlobalFunctions.gPOSCode);
        startActivity(intent);
        finish();
    }


    private void updateToDate()
    {
        editToDate.setText(sdf.format(myCalendarTo.getTime()));
    }
    private void updateFromDate()
    {
        editFromDate.setText(sdf.format(myCalendarFrom.getTime()));

    }

    @Override
    public void onClick(View v)
    {
        if(hmPos.size() > 0 )
        {
            Fragment newFragment = null;
            posCode = hmPos.get(spinnerPos.getSelectedItem().toString()).toString();


            switch (v.getId()) {
                case R.id.btnBillwise:
                    Bundle bundle = new Bundle();
                    bundle.putString("FromDate", editFromDate.getText().toString());
                    bundle.putString("ToDate", editToDate.getText().toString());
                    bundle.putString("PosCode", posCode);

                    newFragment = new clsBillwiseReportFragment();
                    newFragment.setArguments(bundle);
                    break;

                case R.id.btnSettelmentwise:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("FromDate", editFromDate.getText().toString());
                    bundle1.putString("ToDate", editToDate.getText().toString());
                    bundle1.putString("PosCode", posCode);
                    newFragment = new clsSettelmentwiseReportFragment();
                    newFragment.setArguments(bundle1);
                    break;

                case R.id.btnItemwise:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("FromDate", editFromDate.getText().toString());
                    bundle2.putString("ToDate", editToDate.getText().toString());
                    bundle2.putString("PosCode", posCode);
                    newFragment = new clsItemwiseReportFragment();
                    newFragment.setArguments(bundle2);
                    break;

                case R.id.btnMenuwise:
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("FromDate", editFromDate.getText().toString());
                    bundle3.putString("ToDate", editToDate.getText().toString());
                    bundle3.putString("PosCode", posCode);
                    newFragment = new clsMenuwiseReportFragment();
                    newFragment.setArguments(bundle3);
                    break;


                default:
                    break;
            }

            // Create new transaction
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.myfragment, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
        else
        {
            Toast.makeText(clsSalesReportScreen.this,"Please select POS",Toast.LENGTH_LONG).show();
        }
    }

    private void hideDefaultKeyboard()
    {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * HTTP request for pos-list.
     */

    private void funGetPOSList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                App.getAPIHelper().funGetPOSList(clsGlobalFunctions.gUserCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsPosSelectionMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsPosSelectionMaster> arrList) {
                        if (null != arrList) {
                            try
                            {
                                ArrayList<String> poslist= new ArrayList<String>();
                                String posName="";
                                arrPosList=arrList;
                                hmPos=new TreeMap();
                                hmPos.put("All","All");
                                for(int cnt=0;cnt<arrList.size();cnt++)
                                {
                                    clsPosSelectionMaster objPosMaster=(clsPosSelectionMaster)arrList.get(cnt);
                                    hmPos.put(objPosMaster.getStrPosName(), objPosMaster.getStrPosCode());

                                }

                                Set setPOS=hmPos.keySet();
                                Iterator itrPos=setPOS.iterator();
                                while(itrPos.hasNext())
                                {
                                    posName= (String) itrPos.next();
                                    poslist.add(posName);
                                }

                                posCode=hmPos.get(posName).toString();


                                spinnerPos= (Spinner) findViewById(R.id.spinnerPos);

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (clsSalesReportScreen.this,  R.layout.spinneritemtextview,poslist);

                                dataAdapter.setDropDownViewResource
                                        (R.layout.spinnerdropdownitem);
                                spinnerPos.setAdapter(dataAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(clsSalesReportScreen.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsSalesReportScreen.this, R.string.setup_your_server_settings);
        }
    }


    /**
     *For opening todate calendar
     */
    @OnClick(R.id.btnSaleClose)
    public void onClickCloseBtn()
    {
        finish();
    }



}
