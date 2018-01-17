package com.example.apos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apos.adapter.clsCounterAdapter;
import com.example.apos.bean.clsCounterBeen;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prashant on 8/28/2015.
 */
public class clsCounterDtl extends Activity implements clsCounterAdapter.customButtonListener
{
    clsCounterAdapter listDialogAdapter;
    Button btnOk;
    ListView list;
    Map<String,String> hmCounter=new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.counterlist);
        final ArrayList<String> arrList1 = new ArrayList<String>();
        list= (ListView) findViewById(R.id.listitemdialog);
        ArrayList<clsCounterBeen> listCounterDtl = (ArrayList)clsGlobalFunctions.gHashMapCounterDtl.get(clsGlobalFunctions.gPOSCode);
        for(int cnt=0;cnt<listCounterDtl.size();cnt++)
        {
            clsCounterBeen objCounter=listCounterDtl.get(cnt);
            arrList1.add(objCounter.getStrCounterName());
            hmCounter.put(objCounter.getStrCounterName(),objCounter.getStrCounterCode());
        }

        listDialogAdapter = new clsCounterAdapter(this,  arrList1);
        listDialogAdapter.setCustomButtonListner(clsCounterDtl.this);
        list.setAdapter(listDialogAdapter);

        btnOk = (Button) findViewById(R.id.btnlistdialogOk);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
             finish();
            }
        });
    }


    @Override
    public void onButtonClickListner(int position, String value)
    {
        if(value.split("#")[1].equals("item")) {

            String counterName=value.split("#")[0];
            String counterCode=hmCounter.get(counterName);
            clsGlobalFunctions.gCounterCode=counterCode;

            Toast.makeText(clsCounterDtl.this, "item selected" + counterCode, Toast.LENGTH_LONG).show();
            Intent directBillerIntent = new Intent(clsCounterDtl.this, clsDirectBill.class);
           // directBillerIntent.putExtra("CounterCode",counterCode);
            startActivity(directBillerIntent);
            finish();
        }
    }
}