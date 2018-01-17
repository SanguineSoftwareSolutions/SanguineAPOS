package com.example.apos.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.config.CommonUtils;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.listeners.clsNCKOTActListener;
import com.example.apos.listeners.clsPaxNoSelectionListener;


public class clsNCKOTPaxFragment extends Fragment implements clsPaxNoSelectionListener {
    private String keyCase = "upperCase";
    public clsNCKOTActListener objNCKOTActListener;
    private  clsPaxNoSelectionListener objPaxSelectionListener;
    private EditText edtPaxNo;
    private int paxNo;
    public static String tableNo="";

    public static clsNCKOTPaxFragment getInstance()
    {

        clsNCKOTPaxFragment mPax = new clsNCKOTPaxFragment();
        return mPax;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.numberkeyboard, container, false);
        try {
            objPaxSelectionListener = (clsPaxNoSelectionListener) clsNCKOTPaxFragment.this;
            objNCKOTActListener=(clsNCKOTActListener)getActivity();
        } catch (Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        // waiterNo="";
        paxNo=1;
        edtPaxNo = (EditText) rootView.findViewById(R.id.edtGetNo);
        edtPaxNo.requestFocus() ;
        CommonUtils.showKeyboard(edtPaxNo,true);


        edtPaxNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||(actionId ==0) )  {
                    if(tableNo.isEmpty())
                    {
                        Toast.makeText(getActivity(),"selection of pax unavailable ",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                         CommonUtils.hideKeyboard(edtPaxNo);
                        if(edtPaxNo.getText()!=null)
                        {
                            if(edtPaxNo.getText().toString().equals("0"))
                            {
                                Toast.makeText(getActivity(),"pax no should not be 0 !!",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if(!edtPaxNo.getText().toString().equals(""))
                                {
                                    if(Double.parseDouble(edtPaxNo.getText().toString())<100){
                                        paxNo= Integer.parseInt(edtPaxNo.getText().toString());
                                        objNCKOTActListener.setSelectedPaxResult(paxNo);
                                        edtPaxNo.setText("");
                                    }else{
                                        Toast.makeText(getActivity(),"pax unavailable",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void getPaxSelectedNo(String strWaiterNo)
    {
        tableNo=strWaiterNo;
    }


}