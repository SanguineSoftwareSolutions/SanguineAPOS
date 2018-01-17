package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsCustomerOrderScreen;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.adapter.clsCustomerOrderItemsListGridViewAdapter;
import com.example.apos.adapter.clsOrderCartAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillHd;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsMenuItemMasterBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotItemListSelectionListener;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static com.example.apos.fragments.clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.listMenuItemMasterBean;

/**
 * Created by User on 27-04-2017.
 */

public class clsCustomerOrderItemFragment  extends Fragment implements clsKotItemListSelectionListener
{
    private GridView gvItemsList;
    private ConnectivityManager connectivityManager;
    private clsKotItemListSelectionListener itemListSelectionListener = null;
    private ArrayList arrListMenuItemMaster;
    private  ArrayList arrListItemMaster=new ArrayList();
    public static ImageView btnCart;
    private String menuCode="",menuName="",menuType="";
    public static String customerCode,doneOrder="No";
    private Dialog pgDialog;
    public static Activity mActivity;
    public double taxTotalAmt=0;
    private static ArrayList arrListTaxDtls;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState); // TODO Auto-generated method stub
        View myFragmentView = inflater.inflate(R.layout.customerorderitemlist, container, false);
        mActivity=getActivity();
        widget(myFragmentView);
        try
        {
            itemListSelectionListener = (clsKotItemListSelectionListener) clsCustomerOrderItemFragment.this;
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            //new MenuItemPriceListWS().execute(menuCode);
            funGetItemPriceDtlForCustomerOrder(menuCode);
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }



        btnCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager fm = getFragmentManager();
                DialogFragment dialogFragment = new clsCustomerOrderViewCartFragment();
                dialogFragment.show(fm, "WELCOME");


                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //add a fragment
                Bundle bundle1 = new Bundle();
                bundle1.putString("FromDate", " ");
                bundle1.putString("ToDate", " ");
                bundle1.putString("ReportType", " ");
                Fragment newFragment = new clsCustomerOrderMenuFragment();
                newFragment.setArguments(bundle1);

                // Create new transaction
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.linearCustomerOrder, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        return myFragmentView;
    }

    private void widget(View myFragmentView)
    {
        menuCode= getArguments().getString("MenuCode");
        menuName= getArguments().getString("MenuName");
        menuType= getArguments().getString("MenuType");
        clsCustomerOrderScreen.txtHeading.setText("ITEMS");
        gvItemsList = (GridView) myFragmentView.findViewById(R.id.gvkotitemslist);
        btnCart=(ImageView) myFragmentView.findViewById(R.id.btnCart);
        btnCart.setVisibility(View.INVISIBLE);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        arrListTaxDtls=new ArrayList();
    }

    @Override
    public void getItemsListSelectedForOder(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
    {
       clsKotItemsListBean objMenuItemMasterBean=null;
        if (arrListItemMaster.size() > 0)
        {
            for (int i = 0; i < arrListItemMaster.size(); i++)
            {
                objMenuItemMasterBean = new clsKotItemsListBean();
                objMenuItemMasterBean = (clsKotItemsListBean) arrListItemMaster.get(i);
                if (objMenuItemMasterBean.getStrItemCode().equals(strItemCode))
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("selectedItemDetails", (Serializable) objMenuItemMasterBean);
                    FragmentManager fm = getFragmentManager();
                    DialogFragment dialogFragment = new clsDialogAddItemInBasket();
                    dialogFragment.show(fm, "WELCOME");
                    dialogFragment.setArguments(bundle);
                    break;
                }

            }

        }
    }

    private void funSetList(ArrayList arrList)
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;


        int width = 0, height = 0;
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        height = metrics.heightPixels;
        width = metrics.widthPixels;

       /* if(width >480 && width <780)
        {

            gvItemsList.setNumColumns(3);
            if(density <200)
            {
                clsCustomerOrderItemsListGridViewAdapter customerOrderItemsListGridViewAdapter
                        = new clsCustomerOrderItemsListGridViewAdapter(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.mActivity, arrList, itemListSelectionListener);
                gvItemsList.setAdapter(customerOrderItemsListGridViewAdapter);
            }
            else
            {
                clsCustomerOrderItemsListGridViewAdapter customerOrderItemsListGridViewAdapter
                        = new clsCustomerOrderItemsListGridViewAdapter(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.mActivity, arrList, itemListSelectionListener);
                gvItemsList.setAdapter(customerOrderItemsListGridViewAdapter);
            }
        }
        else if(width<=480)
        {

            gvItemsList.setNumColumns(2);
            clsCustomerOrderItemsListGridViewAdapter customerOrderItemsListGridViewAdapter
                    = new clsCustomerOrderItemsListGridViewAdapter(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.mActivity, arrList, itemListSelectionListener);
            gvItemsList.setAdapter(customerOrderItemsListGridViewAdapter);

        }

        else if(width >= 780)
        {

            gvItemsList.setNumColumns(3);
            clsCustomerOrderItemsListGridViewAdapter customerOrderItemsListGridViewAdapter
                    = new clsCustomerOrderItemsListGridViewAdapter(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.mActivity, arrList, itemListSelectionListener);
            gvItemsList.setAdapter(customerOrderItemsListGridViewAdapter);
        }
        */
        clsCustomerOrderItemsListGridViewAdapter customerOrderItemsListGridViewAdapter
                = new clsCustomerOrderItemsListGridViewAdapter(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.mActivity, arrList, itemListSelectionListener);
        gvItemsList.setAdapter(customerOrderItemsListGridViewAdapter);
        clsCustomerOrderScreen.btnRefresh.setVisibility(View.VISIBLE);

    }

    public static void funEnableButton()
    {
        if (clsCustomerOrderScreen.gblHmCartData.size() > 0)
        {
            String cartAmount = String.valueOf(clsCustomerOrderScreen.gblHmCartData.size());
            btnCart.setVisibility(View.VISIBLE);
            btnCart.setImageBitmap(textAsBitmap(cartAmount, 40, Color.WHITE));

        }
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    public static class clsDialogAddItemInBasket extends DialogFragment
    {

        //new HashMap<String,clsMenuItemMasterBean>();
        clsKotItemsListBean objMenuItemMasterBean;
        ImageButton btnIncrease,btnDecrease,btnClose;
        Button btnAddtoCart,btnDeleteCart;
        TextView txtEdtItemPrice,txtEdtTotalPrice,txtEdtQuantity;
        TextView txtSetSelectedItemName,txtSetSelectedItemDetail;
        View rootView;

        ArrayList<clsKotItemsListBean> arrayList=new ArrayList();
        int quantity=0;
        double total=0,itemPrice=0;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView =inflater.inflate(R.layout.dialog_edititemincart, container, false);
            funGetWidget();
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            funGetSelectedItemDetail();
            //funCheckItemPresent();
            funSetItemDetails();

           // System.out.println("Global map:"+clsGlobal.gblHmCartData);
            btnIncrease.setOnClickListener(new View.OnClickListener()
            { @Override
            public void onClick(View view) {

                quantity++;
                double itemPrice = Double.valueOf(txtEdtItemPrice.getText().toString());
                txtEdtQuantity.setText(String.valueOf(quantity));
                total = quantity * itemPrice;
                txtEdtTotalPrice.setText(String.valueOf(total));  //Double.parseDouble(quantity)


            }
            });
            btnAddtoCart.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    objMenuItemMasterBean.setDblSalePrice(total);
                    objMenuItemMasterBean.setStrItemQty(String.valueOf(quantity));
                    if(quantity>0) {
                        clsCustomerOrderScreen.gblHmCartData.put(objMenuItemMasterBean.getStrItemCode(), objMenuItemMasterBean);
                        System.out.println("global Card Data :" + clsCustomerOrderScreen.gblHmCartData);
                        funEnableButton();
                    }
                    getDialog().dismiss();

                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    quantity--;
                    if(quantity<1)
                    {
                        getDialog().dismiss();
                    }
                    double itemPrice = Double.valueOf(txtEdtItemPrice.getText().toString());
                    txtEdtQuantity.setText(String.valueOf(quantity));
                    total=quantity*itemPrice;
                    txtEdtTotalPrice.setText(String.valueOf(total));

                    //Toast.makeText(getActivity(),"Minus",Toast.LENGTH_SHORT).show();

                }
            });

            btnClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });



            return rootView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog=super.onCreateDialog(savedInstanceState);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }


        public void funGetWidget()
        {
            btnClose=(ImageButton)rootView.findViewById(R.id.btnClose);
            btnIncrease=(ImageButton)rootView.findViewById(R.id.btnQuantityIncrease);
            btnDecrease=(ImageButton)rootView.findViewById(R.id.btnQuantityDecrease);
            btnAddtoCart=(Button)rootView.findViewById(R.id.btnAddToCart);
            txtEdtItemPrice=(TextView)rootView.findViewById(R.id.txtShowItemPrice);
            txtEdtQuantity=(TextView)rootView.findViewById(R.id.txtShowQuantity);
            txtEdtTotalPrice=(TextView)rootView.findViewById(R.id.txtTotalPrice);
            txtSetSelectedItemName=(TextView)rootView.findViewById(R.id.txtSelectedMenu);
            txtSetSelectedItemDetail=(TextView)rootView.findViewById(R.id.txtSelectedMenuDetail);
        }


        public void funGetSelectedItemDetail()
        {
            //objMenuItemMasterBean=new clsMenuItemMasterBean();
            objMenuItemMasterBean= (clsKotItemsListBean) getArguments().getSerializable("selectedItemDetails");
            String itemCode1=objMenuItemMasterBean.getStrItemCode();
            if(clsCustomerOrderScreen.gblHmCartData.containsKey(itemCode1))
            {
                objMenuItemMasterBean=clsCustomerOrderScreen.gblHmCartData.get(itemCode1);
            }
        }
        public void funCheckItemPresent()
        {
            String itemCode1=objMenuItemMasterBean.getStrItemCode();
            if(clsCustomerOrderScreen.gblHmCartData.containsKey(itemCode1))
            {
                objMenuItemMasterBean=clsCustomerOrderScreen.gblHmCartData.get(itemCode1);
            }
        }
        public void funSetItemDetails()
        {
            if(!(objMenuItemMasterBean==null))
            {
                quantity= Integer.parseInt(objMenuItemMasterBean.getStrItemQty());
                total = quantity * (objMenuItemMasterBean.getDblSalePrice());
                txtEdtTotalPrice.setText(String.valueOf(total));
                txtSetSelectedItemName.setText(objMenuItemMasterBean.getStrItemName());
                txtEdtItemPrice.setText(String.valueOf(objMenuItemMasterBean.getDblSalePrice()));
                txtEdtQuantity.setText(objMenuItemMasterBean.getStrItemQty());
            }

        }

    }



    public static class clsCustomerOrderViewCartFragment extends DialogFragment implements  clsOrderCartAdapter.customLayoutListener
    {

        private ListView listViewItemList;
        public clsOrderCartAdapter cartAdapter;
        public static List<clsKotItemsListBean> listMenuItemMasterBean;
        HashMap<String, Map<String, clsMenuItemMasterBean>> hmMapMenuItem;
        String SelectedItemCode = null;
        Button btnClear, btnProceed;
        public static TextView txtTotalBillAmt;
        View rootView;

        ArrayList<clsKotItemsListBean> arrayList=new ArrayList();
        int quantity=0;
        double total=0,itemPrice=0;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView =inflater.inflate(R.layout.customerviewordercart, container, false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            funGetWidget();
            funGetItemDetails();

            btnProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clsCustomerOrderScreen.gblHmCartData.size() > 0)
                    {
                        btnProceed.setEnabled(false);
                        //new FireKOTWS().execute();
                         FragmentManager fm = getFragmentManager();
                        DialogFragment dialogFragment = new clsDialogValidateMobile();
                        dialogFragment.show(fm, "WELCOME");
                      //  clsMainFormActivity.btnCart.setVisibility(View.INVISIBLE);

                        new clsCustomerOrderItemFragment().funFireKOTWS();

                        getDialog().dismiss();

                    } else {
                        Toast.makeText(getActivity(), "PLease select item first", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clsCustomerOrderScreen.gblHmCartData.size() > 0)
                    {
                        btnClear.setEnabled(false);
                        clsCustomerOrderScreen.gblHmCartData.clear();
                        getDialog().dismiss();

                    }
                }
            });


            return rootView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog=super.onCreateDialog(savedInstanceState);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }


        public void funGetWidget() {
            listViewItemList = (ListView) rootView.findViewById(R.id.lvItemList);
            //  btnApplyChange=(Button)view.findViewById(R.id.btnApplyChanges);
            btnProceed = (Button) rootView.findViewById(R.id.btnProceedToValidMobile);
            btnClear = (Button) rootView.findViewById(R.id.btnClearSelectedItem);
            txtTotalBillAmt =(TextView) rootView.findViewById(R.id.txtTotalBill);

        }

        public void funGetItemDetails()
        {
            listMenuItemMasterBean = new ArrayList<clsKotItemsListBean>();
            //clsGlobal.gblHmCartData.get("")
            double price=0;
            for (Map.Entry<String, clsKotItemsListBean> entry : clsCustomerOrderScreen.gblHmCartData.entrySet()) {
                // SelectedItemCode = entry.getKey();
                listMenuItemMasterBean.add(entry.getValue());
                price=entry.getValue().getDblSalePrice()*Double.parseDouble(entry.getValue().getStrItemQty());
                total+=price;
            }
            System.out.println("listMenuItemMasterBean " + listMenuItemMasterBean);
            txtTotalBillAmt.setText(String.valueOf(total));
       /*if (listMenuItemMasterBean.size() > 0)*/


            cartAdapter = new clsOrderCartAdapter(getActivity(), listMenuItemMasterBean);
            cartAdapter.notifyDataSetChanged();

            cartAdapter.setCustomLayoutListener(clsCustomerOrderViewCartFragment.this);
            listViewItemList.setAdapter(cartAdapter);

        }

        @Override
        public void onLayoutClickListener(int position, String value)
        {
            clsKotItemsListBean obj;
            clsCustomerOrderScreen.gblHmCartData.remove(value);
            String cartAmount = String.valueOf(clsCustomerOrderScreen.gblHmCartData.size());
            btnCart.setImageBitmap(textAsBitmap(cartAmount, 40, Color.WHITE));
            funGetItemDetails();
            Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
        }




        public static class clsDialogValidateMobile extends DialogFragment {
            View rootView;
            EditText textMobileNo;
            public static TextView txtCustName;
            public static String mobileNo;
            public static Button btnValidMobile;
           // public static String customerCode = null;

            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                rootView = inflater.inflate(R.layout.mobilevalidationdialogbox, container, false);
                funGetWidget();
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                showInputMethod();
                btnValidMobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(btnValidMobile.getText().equals("Proceed To Bill"))
                        {
                            CommonUtils.hideKeyboard(textMobileNo);
                            if(clsGlobalFunctions.gEnableBillSeries.equals("Y")){
                                new clsCustomerOrderItemFragment().funGetBillSeriesList();
                            }else{
                                new clsCustomerOrderItemFragment().funGenerateBillNo(clsGlobalFunctions.gPOSCode);
                            }


                        }
                        else {
                            CommonUtils.hideKeyboard(textMobileNo);
                            funIsValidateMobileNumber();
                            // getDialog().dismiss();
                        }
                    }
                });


                return rootView;
            }


            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                Dialog dialog=super.onCreateDialog(savedInstanceState);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }

            @Override
            public void onStart() {
                super.onStart();
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }

            public void showInputMethod() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

            public void funGetWidget() {
                textMobileNo = (EditText) rootView.findViewById(R.id.editTextMobileNumber);
                btnValidMobile = (Button) rootView.findViewById(R.id.btnValidateNo);
                txtCustName=(TextView) rootView.findViewById(R.id.txtShowCustName);
                CommonUtils.showKeyboard(textMobileNo,true);
            }

            public void funIsValidateMobileNumber() {
                String inputNo = String.valueOf(textMobileNo.getText());
                if (checkNumber(inputNo) && inputNo.length() == 10) {
                    if(Long.parseLong(inputNo)==0 || inputNo.startsWith("00000")){
                        TextView textInValiedMsg = (TextView) rootView.findViewById(R.id.txtInValidateMsg);
                        textInValiedMsg.setVisibility(View.VISIBLE);
                        textMobileNo.setText("");
                    }else{
                        mobileNo = inputNo;
                        TextView textInValiedMsg = (TextView) rootView.findViewById(R.id.txtInValidateMsg);
                        textInValiedMsg.setVisibility(View.INVISIBLE);
                        funGetCustomerInfo(mobileNo);
                    }

                } else {
                    TextView textInValiedMsg = (TextView) rootView.findViewById(R.id.txtInValidateMsg);
                    textInValiedMsg.setVisibility(View.VISIBLE);
                    textMobileNo.setText("");
                }
            }

            private boolean checkNumber(String text) {
                try {
                    Long.parseLong(text);
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
            //Async class for check mobile present or not
           private void funGetCustomerInfo(String... params)
            {
                final clsCustomerOrderItemFragment obCustOrd=new clsCustomerOrderItemFragment();
                if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
                    if (ConnectivityHelper.isConnected()) {
                        obCustOrd.showDialog();
                        App.getAPIHelper().funGetCustomerInfo(params[0],clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsCustomerMaster>>() {
                            @Override
                            public void onSuccess(ArrayList<clsCustomerMaster> arrayList) {
                                String customerName="";
                                obCustOrd.dismissDialog();
                                if (null != arrayList) {
                                    try{
                                        if (arrayList.size()>0)
                                        {
                                            clsCustomerMaster obj;
                                            for(int cnt=0;cnt<arrayList.size();cnt++)
                                            {
                                                obj=arrayList.get(cnt);
                                                customerCode = obj.getCustomerCode();
                                                customerName = obj.getCustomerName();
                                                txtCustName.setVisibility(View.VISIBLE);
                                                txtCustName.setText("Hello "+customerName);
                                                btnValidMobile.setText("Proceed To Bill");
                                                System.out.println("customerCode :"+customerCode);
                                                Toast.makeText(getActivity(), customerCode, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), "Mobile not found \n please Register here..", Toast.LENGTH_SHORT).show();
                                            FragmentManager fm = getFragmentManager();
                                            DialogFragment dialogFragment = new clsDialogCustomerDetails();
                                            dialogFragment.show(fm, "WELCOME");
                                        }
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String errorMessage, int errorCode) {
                                obCustOrd.dismissDialog();
                            }
                        });


                    } else {
                        SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
                    }
                } else {
                    SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
                }
            }
        }




       // Dialog for save customer detail........
        public static class clsDialogCustomerDetails extends DialogFragment
       {
            View rootView;
            String mobileNo, customerName = "", customerAddress = "";
            EditText teditMobileNo, teditAddress, teditCustomerName;
            Button btnSave, btnReset;
            ImageButton btnExit;

            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                rootView = inflater.inflate(R.layout.registercustomer, container, false);
                funGetWidget();
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                teditCustomerName.requestFocus();
                mobileNo = clsDialogValidateMobile.mobileNo;
                teditMobileNo.setText(mobileNo);
                System.out.println("mobileNo.. " + mobileNo);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // funSaveCustoreDetails();
                        customerName = String.valueOf(teditCustomerName.getText());
                        customerAddress = String.valueOf(teditAddress.getText());
                        if (customerName.equals ("") && customerAddress.equals("")) {
                            Toast.makeText(getActivity(), "Please enter data", Toast.LENGTH_SHORT).show();
                        } else {
                            //new clsSaveCustomer().execute(mobileNo);
                            funSaveNewCustomerDtl(mobileNo);

                        }
                    }
                });

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        funResetCustomer();
                    }
                });
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDialog().dismiss();
                    }
                });
                return rootView;
            }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                Dialog dialog = super.onCreateDialog(savedInstanceState);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }

            @Override
            public void onStart() {
                super.onStart();

                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
            }

            public void funResetCustomer() {
                teditMobileNo.setText("");
                teditCustomerName.setText("");
                teditAddress.setText("");
                teditMobileNo.requestFocus();
                Toast.makeText(getActivity(), "Please fill your info", Toast.LENGTH_SHORT).show();
            }

            public void funGetWidget() {
                teditMobileNo = (EditText) rootView.findViewById(R.id.edtMobileNo);
                teditAddress = (EditText) rootView.findViewById(R.id.edtCustomerAddress);
                teditCustomerName = (EditText) rootView.findViewById(R.id.edtCustomerName);
                btnSave = (Button) rootView.findViewById(R.id.btnSaveCustomer);
                btnReset = (Button) rootView.findViewById(R.id.btnResetCustomer);
                btnExit = (ImageButton) rootView.findViewById(R.id.imgViewCancelButton);
            }
            //Async class for send customer info in url
           private void funSaveNewCustomerDtl(String... params)
           {
               if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
                   if (ConnectivityHelper.isConnected()) {
                       String regStatus="false";
                       String mobileNo = params[0];
                       Date objDate = new Date();
                       int day = objDate.getDate();
                       int month = objDate.getMonth() + 1;
                       int year = objDate.getYear() + 1900;
                       objDate.getHours();
                       //long time=objDate.getTime();
                       String custTypeCode="CT001";
                       String currentDate = year + "-" + month + "-" + day + "_" + objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();
                       final clsCustomerOrderItemFragment obCustOrd=new clsCustomerOrderItemFragment();
                       obCustOrd.showDialog();
                       App.getAPIHelper().funSaveNewCustomerDtl(customerName.replaceAll(" ","%20") , mobileNo.replaceAll(" ","%20"),custTypeCode,customerAddress.replaceAll(" ","%20"),clsGlobalFunctions.gClientCode,clsGlobalFunctions.gUserCode,currentDate,new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                           @Override
                           public void onSuccess(JsonObject jObj) {
                               obCustOrd.dismissDialog();
                               if (null != jObj) {
                                   try{
                                       String reason=jObj.get("Reason").getAsString().toString();//1
                                       String customerStatus=jObj.get("CustomerStatus").getAsString().toString();///0

                                       if (reason.equals("Exception"))
                                       {
                                           Toast.makeText(getActivity(),"problem while save new customer",Toast.LENGTH_LONG).show();
                                       }
                                       if (customerStatus.equals("false")) {

                                           if(reason.equals("MobileNo"))
                                           {
                                               Toast.makeText(getActivity(), "Mobile No is already registered!!!", Toast.LENGTH_LONG).show();
                                           }
                                           else
                                           {
                                               Toast.makeText(getActivity(), "Failed!!!", Toast.LENGTH_LONG).show();
                                           }
                                       }
                                       else
                                       {
                                           customerCode=customerStatus;
                                           clsDialogValidateMobile.txtCustName.setVisibility(View.VISIBLE);
                                           clsDialogValidateMobile.txtCustName.setText("Hello "+customerName);
                                           clsDialogValidateMobile.btnValidMobile.setText("Proceed To Bill");
                                           Toast.makeText(getActivity(), "Registration done.. \n" + customerCode, Toast.LENGTH_SHORT).show();

                                           //funResetCustomer();
                                           teditMobileNo.setText("");
                                           teditCustomerName.setText("");
                                           teditAddress.setText("");
                                           getDialog().dismiss();
                                       }
                                   }
                                   catch (Exception e) {
                                       e.printStackTrace();
                                   }
                               }
                           }
                           @Override
                           public void onFailure(String errorMessage, int errorCode) {
                               obCustOrd.dismissDialog();
                           }
                       });


                   } else {
                       SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
                   }
               } else {
                   SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
               }
           }


       }


        public static JsonArray funSaveHomeDelivaryBillDtl(String billNo)
        {
            JsonArray mJsonArrBillDtl = new JsonArray();
            try
            {
                String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                Date objDate = new Date();
                int day = objDate.getDate();
                int month = objDate.getMonth() + 1;
                int year = objDate.getYear() + 1900;
                objDate.getHours();
                String currentDate = year + "-" + month + "-" + day+"::"+objDate.getHours()+":"+objDate.getMinutes()+":"+objDate.getSeconds();
                listMenuItemMasterBean = new ArrayList<clsKotItemsListBean>();
                //clsGlobal.gblHmCartData.get("")
                for (Map.Entry<String, clsKotItemsListBean> entry : clsCustomerOrderScreen.gblHmCartData.entrySet()) {
                    // SelectedItemCode = entry.getKey();
                    listMenuItemMasterBean.add(entry.getValue());
                }
                JsonObject jObjBillDtl = new JsonObject();


                for (int l = 0; l < listMenuItemMasterBean.size(); l++)
                {
                    clsKotItemsListBean objItemBean = (clsKotItemsListBean) listMenuItemMasterBean.get(l);

                    //I001321M99
                    String itemCode=objItemBean.getStrItemCode();
                    if(itemCode.contains("M") && itemCode.length()>=10)
                    {
                    }
                    else
                    {
                        JsonObject mJsonObjectBillDtl = new JsonObject();
                        itemCode=itemCode.substring(0,7);
                        double rate=objItemBean.getDblSalePrice()/Double.valueOf(objItemBean.getStrItemQty());
                        mJsonObjectBillDtl.addProperty("ItemCode", itemCode);
                        mJsonObjectBillDtl.addProperty("ItemName", objItemBean.getStrItemName());
                        mJsonObjectBillDtl.addProperty("BillNo",billNo);
                        mJsonObjectBillDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                        mJsonObjectBillDtl.addProperty("AdvBookingNo", "");
                        mJsonObjectBillDtl.addProperty("Quantity", objItemBean.getStrItemQty());
                        mJsonObjectBillDtl.addProperty("Rate", rate);
                        mJsonObjectBillDtl.addProperty("Amount", objItemBean.getDblSalePrice());
                        mJsonObjectBillDtl.addProperty("TaxAmount", 0.00);
                        mJsonObjectBillDtl.addProperty("KOTNo", "");
                        mJsonObjectBillDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                        mJsonObjectBillDtl.addProperty("CustomerCode",customerCode);
                        mJsonObjectBillDtl.addProperty("OrderProcessedTime", "00:00:00");
                        mJsonObjectBillDtl.addProperty("DataPostFlag", "N");
                        mJsonObjectBillDtl.addProperty("MMSDataPostFlag", "N");
                        mJsonObjectBillDtl.addProperty("ManualKOTNo", "");
                        mJsonObjectBillDtl.addProperty("tdhYN", "N");
                        mJsonObjectBillDtl.addProperty("PromoCode", "");
                        mJsonObjectBillDtl.addProperty("CounterCode", "");
                        mJsonObjectBillDtl.addProperty("WaiterNo", "");
                        mJsonObjectBillDtl.addProperty("DiscountAmt", 0.00);
                        mJsonObjectBillDtl.addProperty("OrderPickedUpdTime", "00:00:00");
                        mJsonObjectBillDtl.addProperty("AreaCode",clsGlobalFunctions.gDirectBillerAreaCode);
                        mJsonObjectBillDtl.addProperty("OperationType","DineIn");
                        mJsonObjectBillDtl.addProperty("POSCode",clsGlobalFunctions.gPOSCode);
                        mJsonObjectBillDtl.addProperty("UserCode", clsGlobalFunctions.gUserCode);
                        // String posDate=dateTime.replaceAll(" ","%20");
                        mJsonObjectBillDtl.addProperty("BillDt", dateTime);
                        clsUtility objUtility = new clsUtility();
                        mJsonObjectBillDtl.addProperty("deviceName", objUtility.funGetHostName());
                        mJsonArrBillDtl.add( mJsonObjectBillDtl);
                    }
                }

               // jObjBillDtl.add("BillDtlInfo", mJsonArrBillDtl);

                //new SaveBill().execute(jObjBillDtl);
                //new clsCustomerOrderItemFragment().funSaveBill(jObjBillDtl);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mJsonArrBillDtl;
        }


        public static  JsonArray funSaveHomeDelivaryDtl(String billNo)
        {
            JsonArray mJsonArrHomeDeliveryDtl = new JsonArray();
            try {
                String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                Date objDate = new Date();
                int day = objDate.getDate();
                int month = objDate.getMonth() + 1;
                int year = objDate.getYear() + 1900;
                objDate.getHours();
                //long time=objDate.getTime();
                String currentDate = year + "-" + month + "-" + day;
                String currentTime = objDate.getHours() + ":" + objDate.getMinutes() + ":" + objDate.getSeconds();
                JsonObject jObjHomeDeliveryDtl = new JsonObject();


                JsonObject mJsonObjectBillDtl = new JsonObject();
                mJsonObjectBillDtl.addProperty("BillNo", billNo);
                mJsonObjectBillDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                mJsonObjectBillDtl.addProperty("Date", currentDate);
                mJsonObjectBillDtl.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
                mJsonObjectBillDtl.addProperty("CustomerCode",customerCode);;
                mJsonObjectBillDtl.addProperty("Time", currentTime);
                mJsonObjectBillDtl.addProperty("CustAddress1", "Vimannagar");
                mJsonObjectBillDtl.addProperty("CustAddress2", "");
                mJsonObjectBillDtl.addProperty("CustAddress3", "");
                mJsonObjectBillDtl.addProperty("CustAddress4", "");
                mJsonObjectBillDtl.addProperty("CustCity", "Pune");
                mJsonObjectBillDtl.addProperty("DataPostFlag", "N");
                mJsonObjectBillDtl.addProperty("DPCode","");

                String posDate=dateTime.replaceAll(" ","%20");
                mJsonObjectBillDtl.addProperty("BillDt", posDate);
                clsUtility objUtility = new clsUtility();
                mJsonObjectBillDtl.addProperty("deviceName", objUtility.funGetHostName());

                mJsonArrHomeDeliveryDtl.add(mJsonObjectBillDtl);

               /* jObjHomeDeliveryDtl.add("HomeDelivery", mJsonArrHomeDeliveryDtl);
                jObjHomeDeliveryDtl.add("HomeDeliveryDtl", mJsonArrHomeDeliveryDtl);
*/
                //new SaveBill().execute(jObjHomeDeliveryDtl);
               // new clsCustomerOrderItemFragment().funSaveBill(jObjHomeDeliveryDtl);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return mJsonArrHomeDeliveryDtl;
        }



        public static JsonArray funInsertBillHd(String billNo) {
            JsonArray mJsonArray = new JsonArray();
            try {
                String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                List<clsBillHd> listBillHdObject = new ArrayList<clsBillHd>();
                clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                double amount=Double.valueOf(txtTotalBillAmt.getText().toString());
                JsonObject jObj = new JsonObject();

                JsonObject mJsonObject = new JsonObject();
                mJsonObject.addProperty("BillNo", billNo);
                mJsonObject.addProperty("AdvBookingNo", "");
                mJsonObject.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                mJsonObject.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
                mJsonObject.addProperty("SettelmentMode", "");
                mJsonObject.addProperty("DiscountAmt", 0);
                mJsonObject.addProperty("DiscountPer", 0.0);
                mJsonObject.addProperty("TaxAmount", 0);
                mJsonObject.addProperty("SubTotal", amount);
                mJsonObject.addProperty("GrandTotal", amount);
                mJsonObject.addProperty("TakeAway", "No");

                mJsonObject.addProperty("OperationType","Home Delivery");
                mJsonObject.addProperty("UserCreated", clsGlobalFunctions.gUserCode);
                mJsonObject.addProperty("UserEdited", clsGlobalFunctions.gUserCode);
                mJsonObject.addProperty("DateCreated", objGlobal.funGetCurrentDateTime());
                mJsonObject.addProperty("DateEdited", objGlobal.funGetCurrentDateTime());
                mJsonObject.addProperty("ClientCode", clsGlobalFunctions.gClientCode);

                mJsonObject.addProperty("TableNo", clsCustomerOrderScreen.tableNo);
                mJsonObject.addProperty("WaiterNo", clsCustomerOrderScreen.waiterNo);
                mJsonObject.addProperty("CustomerCode", customerCode);
                mJsonObject.addProperty("ManualBillNo", "");
                mJsonObject.addProperty("ShiftCode", 1);
                mJsonObject.addProperty("PaxNo", 0);

                mJsonObject.addProperty("DataPostFlag","N");
                mJsonObject.addProperty("ReasonCode", "");
                mJsonObject.addProperty("Remarks", "");
                mJsonObject.addProperty("TipAmount", 0.0);
                mJsonObject.addProperty("SettleDate", objGlobal.funGetCurrentDateTime());
                mJsonObject.addProperty("CounterCode", "");
                mJsonObject.addProperty("DeliveryCharges", 0.0);
                mJsonObject.addProperty("CouponCode", "");
                mJsonObject.addProperty("AreaCode", "A001");
                mJsonObject.addProperty("DiscountRemark", "");
                mJsonObject.addProperty("TakeAwayRemark", "");
                mJsonObject.addProperty("CardNo", "");
                mJsonObject.addProperty("OrderProcessedTime", "00:00:00");
               // String posDate=dateTime.replaceAll(" ","%20");
                mJsonObject.addProperty("BillDt", dateTime);
                clsUtility objUtility = new clsUtility();
                mJsonObject.addProperty("deviceName", objUtility.funGetHostName());

                mJsonArray.add(mJsonObject);

               // jObj.add("BillHdInfo", mJsonArray);

               // new SaveBill().execute(jObj);
              //  new clsCustomerOrderItemFragment().funSaveBill(jObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mJsonArray;
        }

    }

     private void funSaveBill(String billNo) {
         JsonObject jObjBillData=new JsonObject();
         jObjBillData.add("BillHDData", clsCustomerOrderViewCartFragment.funInsertBillHd(billNo));
         jObjBillData.add("BillDtlData", clsCustomerOrderViewCartFragment.funSaveHomeDelivaryBillDtl(billNo));
         jObjBillData.add("HomeDeliveryDtl", clsCustomerOrderViewCartFragment.funSaveHomeDelivaryDtl(billNo));
         jObjBillData.add("HomeDelivery", clsCustomerOrderViewCartFragment.funSaveHomeDelivaryDtl(billNo));

         clsCustomerOrderScreen.gblHmCartData.clear();
        // jObjHomeDeliveryDtl.add("HomeDelivery", mJsonArrHomeDeliveryDtl);HomeDeliveryDtl
         //jObjHomeDeliveryDtl.add("HomeDeliveryDtl", mJsonArrHomeDeliveryDtl);HomeDelivery

         if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funSaveAllBillData(jObjBillData,clsGlobalFunctions.gEnableBillSeries,clsGlobalFunctions.gPOSCode, new BaseAPIHelper.OnRequestComplete<HashMap>() {
                    @Override
                    public void onSuccess(HashMap mapResponse) {
                        dismissDialog();
                        if (null != mapResponse)
                        {
                            String key="";
                            try
                            {
                                String billNo="";
                                List listResponse=(List)mapResponse.get("response");
                                String response="";
                               // intNoOfBills=listResponse.size();
                                for(int i=0;i<listResponse.size();i++) {
                                    LinkedTreeMap<String, String> hmBill = (LinkedTreeMap<String, String>) listResponse.get(i);

                                    for (Map.Entry<String, String> entry : hmBill.entrySet()) {

                                        if (entry.getKey().equals("NoBillSeries")) {
                                            Toast.makeText(mActivity, "Please Create Bill Series", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (entry.getKey().equals("GenaratedBillSeriesNo")) {
                                            billNo = entry.getValue();
                                            Toast.makeText(mActivity, "BillNo=" + billNo, Toast.LENGTH_LONG).show();
                                        }
                                        response = entry.getValue();

                                        if (response.equals("BillHdInfo"))
                                        {
                                           /* if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("External Printer"))// External Printer Type
                                            {
                                                funGenerateBillTextFile(billNo);
                                            }
                                            else
                                            {
                                                funGenerateBillData(billNo);
                                            }
                                            billNo = "";
                                            funClearObjects();
                                            funResetFields();
                                            */
                                        }
                                    }
                                }
                                clsCustomerOrderScreen.gblHmCartData.clear();
                                Toast.makeText(clsCustomerOrderScreen.mActivity, "Saved Bill ", Toast.LENGTH_SHORT).show();
                                clsCustomerOrderScreen.mActivity.finish();
                            }
                            catch (Exception e)
                            {
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



    public void funGenerateBillNo(String... params) {
        String POSCode = params[0];
        if (ConnectivityHelper.isConnected()) {
            App.getAPIHelper().funGenerateBillNo(POSCode,new BaseAPIHelper.OnRequestComplete<HashMap>() {
                @Override
                public void onSuccess(HashMap map) {
                    try
                    {
                        String billNo = "";

                        if (null != map)
                        {
                            HashMap<String,String> hmBill= map;
                            String cardNo ="";
                            for (Map.Entry<String, String> entry : hmBill.entrySet())
                            {
                                billNo=entry.getValue();
                            }
                            funSaveBill(billNo);
                           // Toast.makeText(clsCustomerOrderScreen.mActivity, "Bill Generated "+billNo, Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(mActivity, "Error!!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage, int errorCode) {
                }
            });
        } else {
            SnackBarUtils.showSnackBar(mActivity, "No internet available or not connected to any network");
        }
    }

    public void funGetBillSeriesList(){
        String billNo="BS0000001";
        funSaveBill(billNo);
    }

    private void funFireKOTWS() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                listMenuItemMasterBean = new ArrayList<clsKotItemsListBean>();
                String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                for (Map.Entry<String, clsKotItemsListBean> entry : clsCustomerOrderScreen.gblHmCartData.entrySet())
                {
                    // SelectedItemCode = entry.getKey();
                    listMenuItemMasterBean.add(entry.getValue());
                }
                JsonObject objKOTDtl = new JsonObject();
                JsonArray arrKOTClass = new JsonArray();

                try{
                    String posDate = dateTime.replaceAll(" ", "%20");
                    for (int l = 0; l < listMenuItemMasterBean.size(); l++)
                    {
                        clsKotItemsListBean objItemBean = (clsKotItemsListBean) listMenuItemMasterBean.get(l);
                        double rate=objItemBean.getDblSalePrice()/Double.valueOf(objItemBean.getStrItemQty());
                        JsonObject objRows=new JsonObject();
                        objRows.addProperty("strSerialNo",1);
                        objRows.addProperty("strTableNo",clsCustomerOrderScreen.tableNo);
                        objRows.addProperty("strCardNo","");
                        objRows.addProperty("dblRedeemAmt",0);
                        objRows.addProperty("strHomeDelivery","");
                        objRows.addProperty("strCustomerCode","");
                        objRows.addProperty("strPOSCode",clsGlobalFunctions.gPOSCode);
                        objRows.addProperty("strPOSName",clsGlobalFunctions.gPOSName);
                        objRows.addProperty("strItemCode",objItemBean.getStrItemCode());
                        objRows.addProperty("strItemName",objItemBean.getStrItemName());
                        objRows.addProperty("dblItemQuantity",objItemBean.getStrItemQty());
                        objRows.addProperty("dblAmount",objItemBean.getDblSalePrice());
                        objRows.addProperty("strWaiterNo",clsCustomerOrderScreen.waiterNo);
                        objRows.addProperty("strKOTNo","K01");
                        objRows.addProperty("intPaxNo",0);

                        objRows.addProperty("strPrintYN","Y");
                        objRows.addProperty("strManualKOTNo","");
                        objRows.addProperty("strUserCreated",clsGlobalFunctions.gUserCode);
                        objRows.addProperty("strUserEdited",clsGlobalFunctions.gUserCode);
                        objRows.addProperty("dteDateCreated",dateTime);
                        objRows.addProperty("dteDateEdited",dateTime);
                        objRows.addProperty("strOrderBefore","NO");
                        objRows.addProperty("strTakeAwayYesNo"," N");
                        objRows.addProperty("tdhComboItemYN","N");
                        objRows.addProperty("strDelBoyCode","N");
                        objRows.addProperty("strNCKotYN","N");
                        objRows.addProperty("strCustomerName","");
                        objRows.addProperty("strActiveYN","");

                        objRows.addProperty("dblBalance","");
                        objRows.addProperty("dblCreditLimit","");
                        objRows.addProperty("strCounterCode","");
                        objRows.addProperty("strPromoCode","");
                        objRows.addProperty("dblRate",rate);
                        objRows.addProperty("strRemark","");
                        objRows.addProperty("strReasonCode","");
                        objRows.addProperty("strClientCode",clsGlobalFunctions.gClientCode);
                        objRows.addProperty("strCardType","");
                        clsUtility objUtility = new clsUtility();
                        objRows.addProperty("POSDate", posDate);
                        objRows.addProperty("deviceName", objUtility.funGetHostName());
                        objRows.addProperty("macAddress", objUtility.funGetCurrentMACAddress(mActivity));

                        arrKOTClass.add(objRows);

                    }
                    objKOTDtl.add("KOTDtl",arrKOTClass);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                showDialog();
                App.getAPIHelper().funSaveAndPrintKOT(objKOTDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {
                                String kotNO = jObj.get("kotNO").getAsString();
                                String printingResult = jObj.get("printingResult").getAsString();
                                System.out.print(kotNO+" "+printingResult);
                                Toast.makeText(clsCustomerOrderScreen.mActivity, "Order Saved Successfully" ,Toast.LENGTH_SHORT).show();
                              //  clsCustomerOrderScreen.gblHmCartData.clear();
                                clsCustomerOrderScreen.txtMenuName.setText("");
                            } catch (Exception e) {
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


    public void funGetItemPriceDtlForCustomerOrder(String... params) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                String menuType="";
                boolean flgAllItems=true;
                if(!params[0].equals("All"))
                {
                    flgAllItems=false;
                    menuType="MenuHead";
                }
                clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                showDialog();
               App.getAPIHelper().funGetItemPriceDtlForCustomerOrder(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gDirectBillerAreaCode, params[0], clsGlobalFunctions.gAreaWisePricing, objGlobal.funGetCurrentDate(), objGlobal.funGetCurrentDate(), flgAllItems, menuType, new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotItemsListBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsKotItemsListBean> arrListTemp)
                    {
                        dismissDialog();
                        String pricingDay = new clsUtility().funGetDayForPricing();
                        ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
                        if (null != arrListTemp) {
                            if (arrListTemp.size()>0)
                            {
                                clsKotItemsListBean obj;
                                for(int i=0;i<arrListTemp.size();i++){
                                    obj=arrListTemp.get(i);
                                    obj.setStrSubGroupCode("");
                                    obj.setDblSalePrice(funGetitemPriceOnDay(pricingDay, obj));
                                    obj.setStrItemQty("1");
                                    arrListItemMaster.add(obj);
                                }
                                funSetList(arrListItemMaster);
                            }
                            else {
                                funSetList(new ArrayList());
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


    private double funGetitemPriceOnDay(String day, clsKotItemsListBean obBean) {

        double itemPrice = 0;
        switch (day) {
            case "PriceSunday":
                itemPrice = obBean.getDblSalePriceSun();
                break;

            case "PriceMonday":
                itemPrice = obBean.getDblSalePriceMon();
                break;

            case "PriceTuesday":
                itemPrice = obBean.getDblSalePriceTues();
                break;

            case "PriceWenesday":
                itemPrice = obBean.getDblSalePriceWend();
                break;

            case "PriceThursday":
                itemPrice = obBean.getDblSalePriceThu();
                break;

            case "PriceFriday":
                itemPrice = obBean.getDblSalePriceFri();
                break;

            case "PriceSaturday":
                itemPrice = obBean.getDblSalePriceSat();
                break;

            case "strPriceSunday":
                itemPrice = obBean.getDblSalePriceSun();
                break;

            default:
                itemPrice = obBean.getDblSalePrice();
        }
        return itemPrice;
    }



    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(mActivity, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}
