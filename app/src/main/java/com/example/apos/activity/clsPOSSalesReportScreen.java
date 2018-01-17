package com.example.apos.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apos.fragments.clsBillwiseReportFragment;
import com.example.apos.fragments.clsItemwiseReportFragment;
import com.example.apos.fragments.clsMenuwiseReportFragment;
import com.example.apos.fragments.clsPOSWiseSaleReportFragment;
import com.example.apos.fragments.clsPieChartReportFragment;
import com.example.apos.fragments.clsPoswiseGroupwiseReportFragment;
import com.example.apos.fragments.clsPoswiseItemwiseReportFragment;
import com.example.apos.fragments.clsPoswiseMenuheadwiseReportFragment;
import com.example.apos.fragments.clsPoswiseSubGroupwiseReportFragment;
import com.example.apos.fragments.clsSettelmentwiseReportFragment;
import com.example.apos.util.clsUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by User on 31-03-2017.
 */

public class clsPOSSalesReportScreen extends Activity
{
    EditText editFromDate,editToDate;
    Spinner spinnerReportType,spinnerType;
    public String pos;
    Calendar myCalendarTo,myCalendarFrom;
    SimpleDateFormat sdf;
    DatePickerDialog.OnDateSetListener toDatePicker,fromDatePicker;
    Button btnExecute;
    private ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poswisesalesreportscreen);
        widgetInit();
        String date1=editFromDate.getText().toString();
        String date2=editToDate.getText().toString();


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        Bundle bundle1 = new Bundle();
        bundle1.putString("FromDate", date1);
        bundle1.putString("ToDate", date2);
        if(spinnerReportType.getSelectedItem().equals("POSWise") && spinnerType.getSelectedItem().equals("Grid"))
        {
            clsPOSWiseSaleReportFragment myFragment1 = new clsPOSWiseSaleReportFragment();
            myFragment1.setArguments(bundle1);
            fragmentTransaction.add(R.id.myfragment, myFragment1);
            fragmentTransaction.commit();
        }
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
                new DatePickerDialog(clsPOSSalesReportScreen.this, fromDatePicker, myCalendarFrom
                        .get(Calendar.YEAR), myCalendarFrom.get(Calendar.MONTH),
                        myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show();
                // editFromDate.setText(sdf.format(myCalendarFrom.getTime()));


            }
        });

        editToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(clsPOSSalesReportScreen.this, toDatePicker, myCalendarTo
                        .get(Calendar.YEAR), myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
                //editToDate.setText(sdf.format(myCalendarTo.getTime()));


            }
        });



        btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                funChangeView();
            }
        });

    }

    private void widgetInit()
    {
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        editFromDate=(EditText)findViewById(R.id.edtFromDate);
        editToDate=(EditText)findViewById(R.id.edtToDate);
        btnExecute=(Button)findViewById((R.id.btnExecute)) ;
        editFromDate.setFocusable(false);
        editFromDate.setFocusableInTouchMode(true);
        editFromDate.setKeyListener(null);
        editToDate.setFocusable(false);
        editToDate.setFocusableInTouchMode(true);
        editToDate.setKeyListener(null);
        spinnerReportType= (Spinner) findViewById(R.id.spinnerOfReportType);
        spinnerType= (Spinner) findViewById(R.id.spinnerOfViewType);
        String myFormat = "dd/MM/yyyy";
        //String myFormat = "MM/dd/yy";
        System.out.println(clsGlobalFunctions.gPOSDate);
        sdf = new SimpleDateFormat(myFormat, Locale.US);
        String []posDate=clsGlobalFunctions.gPOSDate.split("-");
        String date1 = posDate[2] + "/" + (posDate[1]) + "/" + (posDate[0]);
        editFromDate.setText(date1);
        editToDate.setText(date1);

        myCalendarTo = Calendar.getInstance();
        myCalendarFrom = Calendar.getInstance();
//        editFromDate.setText(sdf.format(myCalendarFrom.getTime()));
//        editToDate.setText(sdf.format(myCalendarTo.getTime()));
        ArrayList<String> reportType= new ArrayList<String>();
        reportType.add("POSWise");
        reportType.add("ItemWise");
        reportType.add("MenuHeadWise");
        reportType.add("GroupWise");
        reportType.add("SubGroupWise");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.spinneritemtextview,reportType);
        dataAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        spinnerReportType.setAdapter(dataAdapter);

        ArrayList<String> type= new ArrayList<String>();
        type.add("Grid");
        type.add("Chart");
        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinneritemtextview,type);
        dataAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
        spinnerType.setAdapter(dataAdapter);
    }

    private void updateToDate()
    {
        editToDate.setText(sdf.format(myCalendarTo.getTime()));
    }
    private void updateFromDate()
    {
        editFromDate.setText(sdf.format(myCalendarFrom.getTime()));

    }

    private void funChangeView()
    {
        Fragment newFragment = null;


        switch (spinnerReportType.getSelectedItem().toString())
        {

            case "POSWise":
                switch (spinnerType.getSelectedItem().toString())
                {
                    case "Grid":
                        Bundle bundle = new Bundle();
                        bundle.putString("FromDate", editFromDate.getText().toString());
                        bundle.putString("ToDate", editToDate.getText().toString());
                        newFragment = new clsPOSWiseSaleReportFragment();
                        newFragment.setArguments(bundle);
                        break;

                    case "Chart":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("FromDate", editFromDate.getText().toString());
                        bundle1.putString("ToDate", editToDate.getText().toString());
                        bundle1.putString("ReportType", spinnerReportType.getSelectedItem().toString());
                        newFragment = new clsPieChartReportFragment();
                        newFragment.setArguments(bundle1);
                        break;
                    default:
                        break;
                }
                break;

            case "ItemWise":
                switch (spinnerType.getSelectedItem().toString())
                {

                    case "Grid":
                        Bundle bundle = new Bundle();
                        bundle.putString("FromDate", editFromDate.getText().toString());
                        bundle.putString("ToDate", editToDate.getText().toString());
                        newFragment = new clsPoswiseItemwiseReportFragment();
                        newFragment.setArguments(bundle);
                        break;

                    case "Chart":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("FromDate", editFromDate.getText().toString());
                        bundle1.putString("ToDate", editToDate.getText().toString());
                        bundle1.putString("ReportType", spinnerReportType.getSelectedItem().toString());
                        newFragment = new clsPieChartReportFragment();
                        newFragment.setArguments(bundle1);
                        break;
                    default:
                        break;
                }
                break;

            case "MenuHeadWise":
                switch (spinnerType.getSelectedItem().toString())
                {

                    case "Grid":
                        Bundle bundle = new Bundle();
                        bundle.putString("FromDate", editFromDate.getText().toString());
                        bundle.putString("ToDate", editToDate.getText().toString());
                        newFragment = new clsPoswiseMenuheadwiseReportFragment();
                        newFragment.setArguments(bundle);
                        break;

                    case "Chart":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("FromDate", editFromDate.getText().toString());
                        bundle1.putString("ToDate", editToDate.getText().toString());
                        bundle1.putString("ReportType", spinnerReportType.getSelectedItem().toString());
                        newFragment = new clsPieChartReportFragment();
                        newFragment.setArguments(bundle1);
                        break;
                    default:
                        break;
                }
                break;

            case "GroupWise":
                switch (spinnerType.getSelectedItem().toString())
                {

                    case "Grid":
                        Bundle bundle = new Bundle();
                        bundle.putString("FromDate", editFromDate.getText().toString());
                        bundle.putString("ToDate", editToDate.getText().toString());
                        newFragment = new clsPoswiseGroupwiseReportFragment();
                        newFragment.setArguments(bundle);
                        break;

                    case "Chart":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("FromDate", editFromDate.getText().toString());
                        bundle1.putString("ToDate", editToDate.getText().toString());
                        bundle1.putString("ReportType", spinnerReportType.getSelectedItem().toString());
                        newFragment = new clsPieChartReportFragment();
                        newFragment.setArguments(bundle1);
                        break;
                    default:
                        break;
                }
                break;

            case "SubGroupWise":
                switch (spinnerType.getSelectedItem().toString())
                {

                    case "Grid":
                        Bundle bundle = new Bundle();
                        bundle.putString("FromDate", editFromDate.getText().toString());
                        bundle.putString("ToDate", editToDate.getText().toString());
                        newFragment = new clsPoswiseSubGroupwiseReportFragment();
                        newFragment.setArguments(bundle);
                        break;

                    case "Chart":
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("FromDate", editFromDate.getText().toString());
                        bundle1.putString("ToDate", editToDate.getText().toString());
                        bundle1.putString("ReportType", spinnerReportType.getSelectedItem().toString());
                        newFragment = new clsPieChartReportFragment();
                        newFragment.setArguments(bundle1);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }




        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.myfragment, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(clsPOSSalesReportScreen.this, clsMainMenu.class);
        intent.putExtra("PosName", clsGlobalFunctions.gPOSName);
        intent.putExtra("PosCode", clsGlobalFunctions.gPOSCode);
        startActivity(intent);
        finish();
    }
}
