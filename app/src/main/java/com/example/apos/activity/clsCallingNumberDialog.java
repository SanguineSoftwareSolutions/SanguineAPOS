package com.example.apos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by admin on 02/02/2017.
 */

public class clsCallingNumberDialog extends Activity
{

    TextView txtMobileNumber,txtCustDetails;
    LinearLayout linearCalling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcallingnumber);
        funGetWidget();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


        linearCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DirectBiller = new Intent(clsCallingNumberDialog.this, clsDirectBill.class);
                DirectBiller.putExtra("CounterCode","");
                startActivity(DirectBiller);
            }
        });
    }
    public void funGetWidget() {
        txtMobileNumber = (TextView) findViewById(R.id.txtCallingNumber);
        txtCustDetails=(TextView) findViewById(R.id.txtCallerDetails);
        linearCalling=(LinearLayout) findViewById(R.id.linearCalling);
        txtMobileNumber.setText(clsGlobalFunctions.gPhoneNo);
        txtCustDetails.setText(clsGlobalFunctions.gCustName);
    }




}

 /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.showcallingnumber, container, false);
        funGetWidget();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        linearCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }*/