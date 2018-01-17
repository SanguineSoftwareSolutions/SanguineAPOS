package com.example.apos.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.config.CommonUtils;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.listeners.clsPaxNoSelectionListener;


public class clsPaxFragment extends Fragment implements clsPaxNoSelectionListener {
    private String keyCase = "upperCase";
    public clsMakeKotActListener objMakeKotActListener;
    private  clsPaxNoSelectionListener objPaxSelectionListener;
    private EditText edtPaxNo;
    private int paxNo;
    public static String tableNo="";

    public static clsPaxFragment getInstance()
    {

        clsPaxFragment mPax = new clsPaxFragment();
        return mPax;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.numberkeyboard, container, false);
       try {
            objPaxSelectionListener = (clsPaxNoSelectionListener) clsPaxFragment.this;
            objMakeKotActListener=(clsMakeKotActListener)getActivity();
        } catch (Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
       // waiterNo="";
        paxNo=1;
        edtPaxNo = (EditText) rootView.findViewById(R.id.edtGetNo);
        edtPaxNo.requestFocus() ;
        CommonUtils.showKeyboard(edtPaxNo,true);
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(edtPaxNo, InputMethodManager.SHOW_IMPLICIT);





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
                    if(edtPaxNo.getText()!=null){
                        if(!edtPaxNo.getText().toString().equals(""))
                        {
                            if(edtPaxNo.getText().toString().equals("0"))
                            {
                                Toast.makeText(getActivity(),"pax no should not be 0 !!",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if(Double.parseDouble(edtPaxNo.getText().toString())<100){
                                    paxNo= Integer.parseInt(edtPaxNo.getText().toString());
                                    objMakeKotActListener.setSelectedPaxResult(paxNo);
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

      //objMakeKotActListener.setSelectedPaxResult(strPaxNo);
        tableNo=strWaiterNo;
       // Toast.makeText(getActivity(),"waiterNo is"+waiterNo,Toast.LENGTH_LONG).show();
    }

}