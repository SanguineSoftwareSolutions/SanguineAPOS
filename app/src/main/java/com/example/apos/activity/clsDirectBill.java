package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.adapter.clsButtonAdapter;
import com.example.apos.adapter.clsDirectBillSelectedItemsCustomBaseAdapter;
import com.example.apos.adapter.clsDirectBillerViewPagerAdapter;
import com.example.apos.adapter.clsOrderDialogAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillSettlementDtl;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsTDHBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.dto.clsTDHWithModifierDTO;
import com.example.apos.fragments.clsDirectBillerClsMenuItemFragment;
import com.example.apos.fragments.clsDirectBillerItemListFragment;
import com.example.apos.fragments.clsKotItemListFragment;
import com.example.apos.listeners.clsDirectBillerActListener;
import com.example.apos.listeners.clsDirectBillerItemListSelectionListener;
import com.example.apos.util.clsSlidingTabLayout;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class clsDirectBill extends ActionBarActivity implements View.OnClickListener,clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener,clsDirectBillerActListener,clsButtonAdapter.customItemListener {

    private TextView tvHeaderTimeStamp = null;
    private static EditText edtExternalCode=null;
    private static ListView lvDirectBillSelectedItems = null;
    public static List<clsDirectBillSelectedListItemBean> listDirectBillSelectedItems;
    public static clsDirectBillSelectedItemsCustomBaseAdapter objDirectBillerItemDtl;
    private Button btnDirectBillBack = null, btnDirectSettle = null;
    public static Activity mActivity;
    public static ViewPager pager;
    clsDirectBillerViewPagerAdapter adapter;
    clsSlidingTabLayout tabs;
    Intent iData;
    CharSequence Titles[] = {"Items", "Menu","Options"};
    int Numboftabs = 3;
    Thread DareTimeThread = null;
    private static double subTotalAmt;
    private double taxTotalAmt;
    private double discTotalAmt;
    Map<String, clsBillSettlementDtl> hmBillSettleAmtDtl = new HashMap<String, clsBillSettlementDtl>();
    MACHServices machService;
    private String debitCardMemberName;
    private String debitCardMemberCode;
    private String debitCardNo;
    private String debitCardString;
    private double totalMemberBalance;
    public static TextView customerName,edtDirectBillTotalAmount;
    private String billNo;
    public String itemcode;
    public int listpos;
    private  String keyCase="upperCase";
    private String homeDelivery;
    public static String customerCode="", customerType="",operationType="";
    private  String takeAway;
    private ConnectivityManager connectivityManager;
    public Map<String, List<clsTDHBean>> hm_ComboItemDtl = new HashMap<>();

    public  Dialog tdhDialog;
    private Map<String, Map<String, String>> mapSelectedMenuAndItemList;
    private Map<String, Integer> mapSelectedMenu;
    private Map<String, Double> mapModifierItemAndQty;
    private HashMap<String, clsTDHBean> hm_ModifierGroupDetail = null;
    private HashMap<String, clsTDHBean> hm_ModifierItemDetail = null;

    public clsDirectBillerItemListSelectionListener objItemListSelectionListener;
    private Dialog pgDialog;
    JsonObject jsonItmDtlExternalCod;
    List<clsTDHBean> arrListModifierGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().hide();
        setContentView(R.layout.directbillerscreen);

        if(clsGlobalFunctions.gPhoneNo.isEmpty())
        {
            iData = getIntent();
        }

        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();

        widgetInit();


        listDirectBillSelectedItems = new ArrayList<clsDirectBillSelectedListItemBean>();
        objItemListSelectionListener=(clsDirectBillerItemListSelectionListener) clsDirectBillerItemListFragment.getInstance();

        adapter = new clsDirectBillerViewPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, pager);
        pager = (ViewPager) findViewById(R.id.pager_direct_bill);
        pager.setAdapter(adapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_direct_bill);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new clsSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);

        if(!clsGlobalFunctions.gPhoneNo.isEmpty())
        {
            operationType="HomeDelivery";
            funGetCustomerInfo(clsGlobalFunctions.gPhoneNo);
        }

        edtExternalCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == 0)) {
                    String externalCode = edtExternalCode.getText().toString();
                    if (!externalCode.isEmpty()) {
                        if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
                            try {
                                funGetItemDetailsByExternalCode(externalCode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(clsDirectBill.mActivity, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                return false;
            }
        });
    }
    private void widgetInit() {
        operationType="DirectBiller";
        customerCode="";
        customerType="";
        clsGlobalFunctions.gCustName="";
        homeDelivery="N";
        billNo = "";
        subTotalAmt = 0;
        taxTotalAmt = 0;
        discTotalAmt = 0;
        debitCardMemberCode = "";
        debitCardMemberName = "";
        debitCardNo = "";
        debitCardString = "";
        totalMemberBalance = 0;
        tvHeaderTimeStamp = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
        btnDirectBillBack = (Button) findViewById(R.id.btndirectbillHD);
        btnDirectBillBack.setOnClickListener(this);
        customerName=(TextView) findViewById(R.id.customer_name);
        btnDirectSettle = (Button) findViewById(R.id.btndirectbillSettle);
        btnDirectSettle.setOnClickListener(this);
        lvDirectBillSelectedItems = (ListView) findViewById(R.id.listdirectbillselecteditems);
        edtDirectBillTotalAmount = (TextView) findViewById(R.id.edt_direct_bill_total_order_amount);
        edtExternalCode = (EditText) findViewById(R.id.edtExternalCode);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        mapSelectedMenuAndItemList = new HashMap<>();
        mapSelectedMenu = new HashMap<>();
        mapModifierItemAndQty=new HashMap<>();
        hm_ModifierGroupDetail = new HashMap<>();
        hm_ModifierItemDetail= new HashMap<>();
        try{
            funCheckAndGetModifierListForTDH();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //new clsTDHListWS().execute();
        funGetTDHListWS();

    }

    @Override
    public void setSelectedOptionResult(String strCustomerCode, String strCustomerType)
    {
        customerCode=strCustomerCode;
        customerType=strCustomerType;
        Toast.makeText(clsDirectBill.this, "customerCode "+strCustomerCode+"cutomerType"+strCustomerType, Toast.LENGTH_LONG).show();

        if(!clsGlobalFunctions.gPhoneNo.isEmpty())
        {
            pager.setCurrentItem(0);
        }
    }


    @Override
    public void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
    {
         clsDirectBillSelectedListItemBean directBillSelectedListItemBean =
                new clsDirectBillSelectedListItemBean(strItemCode, strItemName, 1, dblSalePrice, dblSalePrice, false,customerCode,customerType,operationType,"N");

        boolean bCheckItemAlreadyPresent = false;
        int iListViewScrollPosition = -1;
        for (int k = 0; k < listDirectBillSelectedItems.size(); k++) {
            if (listDirectBillSelectedItems.get(k).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                clsDirectBillSelectedListItemBean directBillerItemDtl =
                        (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(k);
                if (directBillerItemDtl.getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                    directBillerItemDtl.setQty(directBillerItemDtl.getQty() + 1);
                    directBillerItemDtl.setAmount(directBillerItemDtl.getAmount() + dblSalePrice);
                    directBillerItemDtl.setItemSalePrice(dblSalePrice);
                    bCheckItemAlreadyPresent = true;
                    iListViewScrollPosition = k;
                    listDirectBillSelectedItems.set(k, directBillerItemDtl);

                    break;
                }
            }
        }

        if (!bCheckItemAlreadyPresent) {
            listDirectBillSelectedItems.add(directBillSelectedListItemBean);
        }

        objDirectBillerItemDtl = new clsDirectBillSelectedItemsCustomBaseAdapter(mActivity, listDirectBillSelectedItems);
        lvDirectBillSelectedItems.setItemsCanFocus(true);
        lvDirectBillSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        objDirectBillerItemDtl.setCustomButtonListner((clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
        lvDirectBillSelectedItems.setAdapter(objDirectBillerItemDtl);
        lvDirectBillSelectedItems.setSelection(objDirectBillerItemDtl.getCount() - 1);
        if (iListViewScrollPosition != -1) {
            lvDirectBillSelectedItems.setSelection(iListViewScrollPosition);
        }

        subTotalAmt = 0;
        for (int l = 0; l < listDirectBillSelectedItems.size(); l++)
        {
            subTotalAmt = subTotalAmt + listDirectBillSelectedItems.get(l).getAmount();
            edtDirectBillTotalAmount.setText(String.valueOf(subTotalAmt));
        }


        if (mapModifierItemAndQty.containsKey(strItemCode))
        {
            String modifierGroupCode="";
            int maxLimit=0,minLimit=0;
            for(Map.Entry<String,clsTDHBean> entry:hm_ModifierGroupDetail.entrySet())
            {
                modifierGroupCode = entry.getKey();
                clsTDHBean objTdh= entry.getValue();
                maxLimit=objTdh.getIntItemMaxLimit();
                minLimit=objTdh.getIntItemMaxLimit();
                break;
            }
            mapSelectedMenu.put(modifierGroupCode,maxLimit);
            funOpenTDHDialogForModifier(strItemCode, strItemName,modifierGroupCode,minLimit,maxLimit);
        }
        else
        {
            if(hm_ComboItemDtl.size()>0)
            {
                try
                {
                    List<clsTDHBean> arrListOfModifierGroup = new ArrayList();

                    arrListOfModifierGroup= funGetTDHMenuList(strItemCode);
                    String menuCode="";
                    int minLimit=0,maxLimit=0;
                    List<clsTDHBean> arrList=funGetTDHMenuList(strItemCode);
                    for (int cnt = 0; cnt < arrList.size(); cnt++)
                    {
                        clsTDHBean objBean=arrList.get(0);
                        menuCode=objBean.getStrTDHItemCode();
                        double qty=objBean.getDblQty();
                        maxLimit=(int) Math.round(qty);;
                        break;
                    }
                    mapSelectedMenu.put(menuCode,maxLimit);
                    funOpenTDHDialogForItem(strItemCode, strItemName,arrListOfModifierGroup,menuCode,minLimit,maxLimit);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void setMenuItemSelectionCodeResult(String strMenuItemCode, String strMenuItemName, String strMenuType)
    {
        if(strMenuType.equals("MenuHead"))
        {
            if(clsGlobalFunctions.gMenuItemSortingOn.equals("subMenuHeadWise"))
            {
                clsDirectBillerClsMenuItemFragment obj=new clsDirectBillerClsMenuItemFragment();
                obj.populateSelectedSubMenuItems(strMenuItemCode, strMenuItemName);
                pager.setCurrentItem(1);
            }
            else
            {
                new clsDirectBillerItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName,strMenuType);
                pager.setCurrentItem(0);
            }
        }
        else
        {
            new clsDirectBillerItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName,strMenuType);
            pager.setCurrentItem(0);
        }
    }


    private void funRefreshItemGrid() {
        int iListViewScrollPosition = -1;
        objDirectBillerItemDtl = new clsDirectBillSelectedItemsCustomBaseAdapter(mActivity, listDirectBillSelectedItems);
        lvDirectBillSelectedItems.setItemsCanFocus(true);
        lvDirectBillSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        objDirectBillerItemDtl.setCustomButtonListner((clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
        lvDirectBillSelectedItems.setAdapter(objDirectBillerItemDtl);
        lvDirectBillSelectedItems.setSelection(objDirectBillerItemDtl.getCount() - 1);
        if (iListViewScrollPosition != -1) {
            lvDirectBillSelectedItems.setSelection(iListViewScrollPosition);
        }
        subTotalAmt = 0;
        for (int l = 0; l < listDirectBillSelectedItems.size(); l++) {
            subTotalAmt = subTotalAmt + listDirectBillSelectedItems.get(l).getAmount();
            edtDirectBillTotalAmount.setText(String.valueOf(subTotalAmt));
        }
    }



    private void funResetFields() {
        listDirectBillSelectedItems.clear();
        hmBillSettleAmtDtl.clear();
        subTotalAmt = 0;
        taxTotalAmt = 0;
        discTotalAmt = 0;
        edtDirectBillTotalAmount.setText("0.00");
        //debitCardMemberCode="";
        debitCardMemberName = "";
        debitCardString = "";
        totalMemberBalance = 0;

        int iListViewScrollPosition = -1;
        objDirectBillerItemDtl = new clsDirectBillSelectedItemsCustomBaseAdapter(mActivity, listDirectBillSelectedItems);
        lvDirectBillSelectedItems.setItemsCanFocus(true);
        lvDirectBillSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        objDirectBillerItemDtl.setCustomButtonListner((clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
        lvDirectBillSelectedItems.setAdapter(objDirectBillerItemDtl);
        lvDirectBillSelectedItems.setSelection(objDirectBillerItemDtl.getCount() - 1);
        if (iListViewScrollPosition != -1) {
            lvDirectBillSelectedItems.setSelection(iListViewScrollPosition);
        }



    }



    private void funClearObjects() {
        hmBillSettleAmtDtl = null;
        listDirectBillSelectedItems = null;
        clsGlobalFunctions.gCounterCode = "";
        //clsGlobalFunctions.gCounterWiseBilling="No";
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(clsOrderActivity.this, "Back ", Toast.LENGTH_SHORT).show();
        clsGlobalFunctions.gPhoneNo="";
        clsGlobalFunctions.gCustName="";
        funClearObjects();
        finish();
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btndirectbillHD:
               // funClearObjects();
               // finish();
               if(operationType.equalsIgnoreCase("DirectBiller"))
               {
                   btnDirectBillBack.setTextColor(Color.RED);
                   operationType="HomeDelivery";
               }
                else
               {
                   btnDirectBillBack.setTextColor(Color.WHITE);
                   operationType="DirectBiller";
               }

                break;

            case R.id.btndirectbillSettle:

                if (listDirectBillSelectedItems.size() > 0) {

                    if (operationType.equalsIgnoreCase("HomeDelivery") && customerCode.isEmpty()) {
                        Toast.makeText(clsDirectBill.mActivity, "Please select customer", Toast.LENGTH_LONG).show();
                    } else {

                        for (int cnt = 0; cnt < listDirectBillSelectedItems.size(); cnt++) {
                            clsDirectBillSelectedListItemBean objSelectedList = listDirectBillSelectedItems.get(cnt);
                            objSelectedList.setStrCustomerType(customerType);
                            objSelectedList.setStrCustomerName(clsGlobalFunctions.gCustName);
                            objSelectedList.setStrCustomerCode(customerCode);
                            objSelectedList.setStrOperationType(operationType);
                            listDirectBillSelectedItems.set(cnt, objSelectedList);

                        }

                        Intent billSettleIntent = new Intent(clsDirectBill.this, clsBillSettlement.class);
                        billSettleIntent.putExtra("LIST", (Serializable) listDirectBillSelectedItems);
                        startActivity(billSettleIntent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(clsDirectBill.this, "Select Items For Billing!!! ", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }




    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        machService = new MACHServices(getApplicationContext());
        machService.mach_initialize(getApplicationContext());

        try {

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int connectStatus = machService.mach_connect();
        Toast.makeText(getApplicationContext(), "Connection Status : " + connectStatus, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        machService.mach_release(getApplicationContext());
    }




    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
                } catch (Exception e) {
                }
            }
        });
    }

    //for delete purpose


    @Override
    public void onButtonClickListner(int position, String value) {

        if (value.split("#")[1].equals("selectedrow")) {
            listpos = position;
            itemcode = value.split("#")[2];
            final Dialog dialog = new Dialog(clsDirectBill.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.edititemqty);
            //  dialog.setTitle("itemname");
            final int pos = position;
            clsDirectBillSelectedListItemBean objDirectBillerItem = listDirectBillSelectedItems.get(position);

            ImageButton btnMinus = (ImageButton) dialog.findViewById(R.id.btnRemoveQty);
            ImageButton btnAdd = (ImageButton) dialog.findViewById(R.id.btnAddQty);
            Button btnOk = (Button) dialog.findViewById(R.id.btnUpdate);
            ImageView btnDeleteItem = (ImageView) dialog.findViewById(R.id.btnDeleteItem);
            ImageView btnCloseDialog = (ImageView) dialog.findViewById(R.id.btnClose);
            final EditText edtFFName = (EditText) dialog.findViewById(R.id.edtKotFFItemName);
            final EditText edtFFRate = (EditText) dialog.findViewById(R.id.edtKotFFItemRate);
          //  Button btnApplyFreeFlowMod = (Button) dialog.findViewById(R.id.btnApplyFreeFlow);
            Button btnPredefinedModifier = (Button) dialog.findViewById(R.id.btnKotModifier);
            TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
            final TextView txtQty = (TextView) dialog.findViewById(R.id.txtQty);

            txtItemName.setText(objDirectBillerItem.getStrDesc());
            txtQty.setText("" + objDirectBillerItem.getQty());


            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clsDirectBillSelectedListItemBean objDirectBillerItem = listDirectBillSelectedItems.get(pos);
                    //listDirectBillSelectedItems.remove(pos);
                    long qty1 = 0;
                    long qty = objDirectBillerItem.getQty();
                    double amt = objDirectBillerItem.getAmount();
                    double rate = ((objDirectBillerItem.getAmount()) / (objDirectBillerItem.getQty()));
                    if (qty > 1) {
                        qty = qty - 1;
                        amt = qty * rate;
                        objDirectBillerItem.setQty(qty);
                        objDirectBillerItem.setAmount(amt);
                        qty1 = objDirectBillerItem.getQty();
                        listDirectBillSelectedItems.set(pos, objDirectBillerItem);

                        funRefreshItemGrid();
                    } else if (qty == 1) {
                        String strDeleteItemCode = listDirectBillSelectedItems.get(pos).getStrItemCode();
                        listDirectBillSelectedItems.remove(pos);
                        funDeleteItemFromGrid(strDeleteItemCode);
                        funRefreshItemGrid();
                        dialog.dismiss();


                    }
                    txtQty.setText("" + qty1);

                }
            });


            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clsDirectBillSelectedListItemBean objDirectBillerItem = listDirectBillSelectedItems.get(pos);
                    long qty1 = 0;
                    long qty = objDirectBillerItem.getQty();
                    //double rate = ((objDirectBillerItem.getAmount()) / (objDirectBillerItem.getQty()));
                    qty = qty + 1;
                    double amt = qty * objDirectBillerItem.getItemSalePrice();
                    objDirectBillerItem.setQty(qty);
                    objDirectBillerItem.setAmount(amt);
                    qty1 = objDirectBillerItem.getQty();
                    listDirectBillSelectedItems.set(pos, objDirectBillerItem);

                    // funRefreshItemGrid();
                    txtQty.setText("" + qty1);
                }
            });


            btnDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if (!v.isSelected()) {
                        v.setSelected(true);
                        Toast.makeText(mActivity, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
                        clsDirectBillSelectedListItemBean objDirectBillerItem = listDirectBillSelectedItems.get(pos);
                        String strDeleteItemCode = listDirectBillSelectedItems.get(pos).getStrItemCode();
                        listDirectBillSelectedItems.remove(pos);
                        funDeleteItemFromGrid(strDeleteItemCode);
                        funRefreshItemGrid();
                        dialog.dismiss();
                    }
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String freeFlowModName="";
                    try{
                        freeFlowModName = new clsUtility().funCheckSpecialCharacters(edtFFName.getText().toString());
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                    if(edtFFName.getText().toString().isEmpty())
                    {
                        Toast.makeText(clsDirectBill.this, "Enter Free Flow Modifier Name",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        double freeFlowRate = 0;
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edtFFName, InputMethodManager.SHOW_IMPLICIT);

                        if(!edtFFRate.getText().toString().isEmpty())
                        {
                            freeFlowRate=Double.parseDouble(edtFFRate.getText().toString());
                        }

                        clsDirectBillSelectedListItemBean directBillSelectedListItemBean =
                                new clsDirectBillSelectedListItemBean(itemcode+"M999", "-->"+freeFlowModName, 1, freeFlowRate, freeFlowRate, true,customerCode,customerType,operationType,"N");
                        listDirectBillSelectedItems.add(listpos + 1, directBillSelectedListItemBean);
                        funRefreshItemGrid();
                        dialog.dismiss();
                    }

                    funRefreshItemGrid();
                    dialog.dismiss();
                }
            });

            btnPredefinedModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new ModifierListWS().execute(itemcode);
                    funGetModifierList(itemcode);
                    dialog.dismiss();

                }


            });

            btnCloseDialog.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.getWindowManager().getDefaultDisplay();
        }
    }


    private void funDeleteItemFromGrid(String strDeleteItemCode)
    {
        if(listDirectBillSelectedItems.size()>0 )
        {
            for (int cnt = 0; cnt <= listDirectBillSelectedItems.size(); cnt++)
            {
                clsDirectBillSelectedListItemBean obj=null;
                if(cnt==listDirectBillSelectedItems.size())
                {
                    obj= (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt-1);
                    if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                    {
                        listDirectBillSelectedItems.remove(obj);
                    }
                    else
                    {
                        if(cnt>=2)
                        {
                            obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt - 2);
                            if (obj.getStrItemCode().contains(strDeleteItemCode + "M")) {
                                listDirectBillSelectedItems.remove(obj);
                            }
                        }
                    }
                }
                else
                {
                    obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
                    if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                    {
                        listDirectBillSelectedItems.remove(obj);
                    }
                    else
                    {
                        if(cnt!=0)
                        {
                            obj= (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt-1);
                            if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                            {
                                listDirectBillSelectedItems.remove(obj);
                            }
                        }
                    }
                }
            }
        }

    }


    class CountDownRunner implements Runnable
    {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onItemClickListner(int position, String value)
    {
        //  tdhDialog = new Dialog(clsKotScreen.this);
        int minLimit=0,maxLimit=0;
        String itemData=value.split("#")[3];
        String name=value.split("#")[1];
        String dataType=value.split("#")[2];
        String itemCode=itemData.split("!")[0];
        String itemName=itemData.split("!")[1];


        tdhDialog.dismiss();


        if (mapModifierItemAndQty.containsKey(itemCode))
        {
            if(dataType.equals("TDHItems"))
            {
                String modifierCode=itemData.split("!")[2];
                maxLimit= Integer.parseInt(itemData.split("!")[3]);
                Map<String, String> mapItem = new HashMap<String, String>();

                if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(modifierCode))
                {
                    mapItem=mapSelectedMenuAndItemList.get(modifierCode);
                    if(mapItem.containsKey(value.split("#")[0]))
                    {
                        mapItem.remove(value.split("#")[0]);
                        if(mapItem.size()==0)
                        {
                            mapSelectedMenuAndItemList.remove(modifierCode);
                        }
                        else
                        {
                            mapSelectedMenuAndItemList.remove(modifierCode);
                            mapSelectedMenuAndItemList.put(modifierCode, mapItem);
                        }

                    }
                    else
                    {
                        if(mapItem.size()==maxLimit)
                        {
                            Toast.makeText(clsDirectBill.this,"You Have Selected Max Item Qty!!!!",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            mapItem.put(value.split("#")[0], value.split("#")[1]);
                            mapSelectedMenuAndItemList.put(modifierCode, mapItem);
                        }
                    }

                }
                else
                {
                    mapItem.put(value.split("#")[0], value.split("#")[1]);
                    mapSelectedMenuAndItemList.put(modifierCode, mapItem);
                }



                if(!modifierCode.isEmpty())
                {
                    funOpenTDHDialogForModifier(itemCode, itemName,modifierCode,minLimit,maxLimit);
                }
            }
            else
            {
                String modifierGroupCode=value.split("#")[0].toString();

                if (hm_ModifierGroupDetail.size() > 0 && hm_ModifierGroupDetail.containsKey(modifierGroupCode))
                {
                    clsTDHBean objBean=hm_ModifierGroupDetail.get(modifierGroupCode);
                    maxLimit=objBean.getIntItemMaxLimit();
                }



                if(!modifierGroupCode.isEmpty())
                {
                    if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(modifierGroupCode))
                    {
                        mapSelectedMenu.remove(modifierGroupCode);
                        mapSelectedMenu.put(modifierGroupCode, maxLimit);
                    }
                    else
                    {
                        mapSelectedMenu.put(modifierGroupCode, maxLimit);
                    }
                    funOpenTDHDialogForModifier(itemCode, itemName,modifierGroupCode,minLimit,maxLimit);
                }
            }

        }
        else
        {
            try
            {
                List<clsTDHBean> arrListOfModifierGroup = new ArrayList();

                arrListOfModifierGroup= funGetTDHMenuList(itemCode);
                if(dataType.equals("TDHItems"))
                {
                    String menuCode=itemData.split("!")[2];
                    maxLimit= Integer.parseInt(itemData.split("!")[3]);
                    Map<String, String> mapItem = new HashMap<String, String>();

                    if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode))
                    {
                        mapItem=mapSelectedMenuAndItemList.get(menuCode);
                        if(mapItem.containsKey(value.split("#")[0]))
                        {
                            mapItem.remove(value.split("#")[0]);
                            if(mapItem.size()==0)
                            {
                                mapSelectedMenuAndItemList.remove(menuCode);
                            }
                            else
                            {
                                mapSelectedMenuAndItemList.remove(menuCode);
                                mapSelectedMenuAndItemList.put(menuCode, mapItem);
                            }

                        }
                        else
                        {
                            if(mapItem.size()==maxLimit)
                            {
                                Toast.makeText(clsDirectBill.this,"You Have Selected Max Item Qty!!!!",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                mapItem.put(value.split("#")[0], value.split("#")[1]);
                                mapSelectedMenuAndItemList.put(menuCode, mapItem);
                            }
                        }

                    }
                    else
                    {
                        mapItem.put(value.split("#")[0], value.split("#")[1]);
                        mapSelectedMenuAndItemList.put(menuCode, mapItem);
                    }



                    if(!menuCode.isEmpty())
                    {
                        funOpenTDHDialogForItem(itemCode, itemName,arrListOfModifierGroup,menuCode,minLimit,maxLimit);
                    }
                }
                else
                {

                    String menuCode="";
                    List<clsTDHBean> arrList=funGetTDHMenuList(itemCode);
                    for (int cnt = 0; cnt < arrList.size(); cnt++)
                    {
                        clsTDHBean objBean=arrList.get(cnt);
                        if(value.split("#")[0].toString().equals(objBean.getStrTDHItemCode()))
                        {
                            menuCode=objBean.getStrTDHItemCode();
                            double qty=objBean.getDblQty();
                            maxLimit=(int) Math.round(qty);
                            break;
                        }
                    }

                    if(!menuCode.isEmpty())
                    {
                        if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(menuCode))
                        {
                            mapSelectedMenu.remove(menuCode);
                            mapSelectedMenu.put(menuCode, maxLimit);
                        }
                        else
                        {
                            mapSelectedMenu.put(menuCode, maxLimit);
                        }
                        funOpenTDHDialogForItem(itemCode, itemName,arrListOfModifierGroup,menuCode,minLimit,maxLimit);
                    }

                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }


    }





    private void funOpenTDHDialogForItem(final String strItemCode, String strItemName, List<clsTDHBean> arrListOfModifierGroup, String menuCode, int minLimit, int maxLimit)
    {
        if(hm_ComboItemDtl.size()>0)
        {
            if(hm_ComboItemDtl.containsKey(strItemCode))
            {
                tdhDialog = new Dialog(clsDirectBill.this);
                tdhDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                tdhDialog.setContentView(R.layout.tdhdialogform);
                final TextView txtItemName = (TextView) tdhDialog.findViewById(R.id.txtItemName);
                final TextView txtMinLimit = (TextView) tdhDialog.findViewById(R.id.txtMinLimit);
                final TextView txtMaxLimit = (TextView) tdhDialog.findViewById(R.id.txtMaxLimit);
                final Button btnDone = (Button) tdhDialog.findViewById(R.id.btnDone);
                final Button btnClose = (Button) tdhDialog.findViewById(R.id.btnClose);
                GridView gridviewModifierGroup,gridviewTDHItem;
                gridviewModifierGroup = (GridView) tdhDialog.findViewById(R.id.gridviewModifierGroup);
                gridviewTDHItem = (GridView) tdhDialog.findViewById(R.id.gridviewTDHItem);
                txtMinLimit.setText(String.valueOf(minLimit));
                txtMaxLimit.setText(String.valueOf(maxLimit));


                txtItemName.setText(strItemName);

                try
                {
                    if(arrListOfModifierGroup.size()>0)
                    {
                        ArrayList arrListOfMenu = new ArrayList();
                        for (int cnt = 0; cnt < arrListOfModifierGroup.size(); cnt++)
                        {
                            clsTDHBean objBean = arrListOfModifierGroup.get(cnt);

                            if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(menuCode))
                            {
                                if(objBean.getStrTDHItemCode().equals(menuCode))
                                {
                                    objBean.setIsSelected("Y");
                                }
                                else
                                {
                                    objBean.setIsSelected("N");
                                }

                            }
                            else
                            {
                                objBean.setIsSelected("N");
                            }
                            arrListOfMenu.add(objBean);
                        }

                        clsButtonAdapter objBtnAdapter=new clsButtonAdapter(clsDirectBill.mActivity,clsDirectBill.mActivity, arrListOfMenu,"Menu",strItemCode+"!"+strItemName+"!"+menuCode+"!"+maxLimit);
                        objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
                        gridviewModifierGroup.setAdapter(objBtnAdapter);
                    }


                    List<clsTDHBean> arrListOfTDHItem = new ArrayList();
                    arrListOfTDHItem = (ArrayList) hm_ComboItemDtl.get(strItemCode);


                    ArrayList arrListOfSelectedTDHMenuItems = new ArrayList();
                    Map<String, String> mapItem = new HashMap<String, String>();
                    for (int cnt = 0; cnt < arrListOfTDHItem.size(); cnt++)
                    {
                        clsTDHBean objBean = arrListOfTDHItem.get(cnt);
                        if(objBean.getStrModifierGroupCode().equals(menuCode))
                        {
                            if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode))
                            {
                                mapItem=mapSelectedMenuAndItemList.get(menuCode);
                                if(mapItem.containsKey(objBean.getStrTDHItemCode()))
                                {
                                    objBean.setIsSelected("Y");
                                }
                                else
                                {
                                    objBean.setIsSelected("N");
                                }
                            }
                            else
                            {
                                objBean.setIsSelected("N");
                            }

                            arrListOfSelectedTDHMenuItems.add(objBean);
                        }
                    }
                    clsButtonAdapter objBtnAdapter=new clsButtonAdapter(clsDirectBill.mActivity,clsDirectBill.mActivity, arrListOfSelectedTDHMenuItems,"TDHItems",strItemCode+"!"+strItemName+"!"+menuCode+"!"+maxLimit);
                    objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
                    gridviewTDHItem.setAdapter(objBtnAdapter);


                    btnDone.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if(mapSelectedMenuAndItemList.size()>0)
                            {
                                int pos=0;
                                for(int i=0;i<listDirectBillSelectedItems.size();i++)
                                {
                                    clsDirectBillSelectedListItemBean objBean=listDirectBillSelectedItems.get(i);
                                    if(objBean.getStrItemCode().equals(strItemCode))
                                    {
                                        pos=i;
                                        break;
                                    }
                                }
                                clsGlobalFunctions objGlobal=new clsGlobalFunctions();
                                String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                                int sequenceNo=1;
                                Iterator<Map.Entry<String, Map<String, String>>> itMenu = mapSelectedMenuAndItemList.entrySet().iterator();
                                while (itMenu.hasNext())
                                {
                                    Map.Entry<String, Map<String, String>> menuEntry = itMenu.next();
                                    String menuCode = menuEntry.getKey();
                                    Iterator<Map.Entry<String, String>> itItems = menuEntry.getValue().entrySet().iterator();
                                    while (itItems.hasNext())
                                    {
                                        Map.Entry<String, String> entry = itItems.next();
                                        String itemCode = entry.getKey();
                                        String itemName = entry.getValue();
                                        boolean flgAlreadyPresentMod=false;
                                        sequenceNo++;



                                        if (!flgAlreadyPresentMod)
                                        {
                                            clsDirectBillSelectedListItemBean directBillSelectedListItemBean;
                                            directBillSelectedListItemBean = new clsDirectBillSelectedListItemBean(itemCode, "=>"+itemName, mapSelectedMenu.get(menuCode),0.00,0.00, true,customerCode,customerType,operationType,"N");
                                            listDirectBillSelectedItems.add(pos + 1, directBillSelectedListItemBean);
                                            pos++;
                                        }
                                        funRefreshItemGrid();

                                     }
                                }

                                mapSelectedMenuAndItemList.clear();
                                mapSelectedMenu.clear();
                                onResume();
                                tdhDialog.dismiss();

                            }
                        }
                    });

                    btnClose.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            mapSelectedMenuAndItemList.clear();
                            mapSelectedMenu.clear();
                            tdhDialog.dismiss();

                        }
                    });

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }



                tdhDialog.show();
                Window window = tdhDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.getWindowManager().getDefaultDisplay();
            }
        }

    }


    private void funOpenTDHDialogForModifier(final String strItemCode, String strItemName, String modifierGroupCode, int minLimit, int maxLimit)
    {
        tdhDialog = new Dialog(clsDirectBill.this);
        tdhDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tdhDialog.setContentView(R.layout.tdhdialogform);
        final TextView txtItemName = (TextView) tdhDialog.findViewById(R.id.txtItemName);
        final TextView txtMinLimit = (TextView) tdhDialog.findViewById(R.id.txtMinLimit);
        final TextView txtMaxLimit = (TextView) tdhDialog.findViewById(R.id.txtMaxLimit);
        final Button btnDone = (Button) tdhDialog.findViewById(R.id.btnDone);
        final Button btnClose = (Button) tdhDialog.findViewById(R.id.btnClose);
        GridView gridviewModifierGroup,gridviewTDHItem;
        gridviewModifierGroup = (GridView) tdhDialog.findViewById(R.id.gridviewModifierGroup);
        gridviewTDHItem = (GridView) tdhDialog.findViewById(R.id.gridviewTDHItem);
        txtMinLimit.setText(String.valueOf(minLimit));
        txtMaxLimit.setText(String.valueOf(maxLimit));


        txtItemName.setText(strItemName);

        try
        {
            if(hm_ModifierGroupDetail.size()>0)
            {
                ArrayList arrListOfModifierGroup = new ArrayList();

                for(Map.Entry<String,clsTDHBean> entry:hm_ModifierGroupDetail.entrySet())
                {
                    clsTDHBean objTdh= entry.getValue();
                    if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(modifierGroupCode))
                    {
                        if(objTdh.getStrTDHItemCode().equals(modifierGroupCode))
                        {
                            objTdh.setIsSelected("Y");
                        }
                        else
                        {
                            objTdh.setIsSelected("N");
                        }
                    }
                    else
                    {
                        objTdh.setIsSelected("N");
                    }
                    arrListOfModifierGroup.add(objTdh);
                }

                clsButtonAdapter objBtnAdapter=new clsButtonAdapter(clsDirectBill.mActivity,clsDirectBill.mActivity, arrListOfModifierGroup,"Menu",strItemCode+"!"+strItemName+"!"+modifierGroupCode+"!"+maxLimit);
                objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
                gridviewModifierGroup.setAdapter(objBtnAdapter);
            }



            if(hm_ModifierItemDetail.size()>0)
            {
                ArrayList arrListModifierItem = new ArrayList();

                Map<String, String> mapItem = new HashMap<String, String>();
                for(Map.Entry<String,clsTDHBean> entry:hm_ModifierItemDetail.entrySet())
                {
                    clsTDHBean objTdh= entry.getValue();
                    if(objTdh.getStrModifierGroupCode().equals(modifierGroupCode))
                    {
                        if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(modifierGroupCode))
                        {
                            mapItem=mapSelectedMenuAndItemList.get(modifierGroupCode);
                            if(mapItem.containsKey(objTdh.getStrTDHItemCode()))
                            {
                                objTdh.setIsSelected("Y");
                            }
                            else
                            {
                                objTdh.setIsSelected("N");
                            }
                        }
                        else
                        {
                            objTdh.setIsSelected("N");
                        }

                        arrListModifierItem.add(objTdh);
                    }

                }

                clsButtonAdapter objBtnAdapter=new clsButtonAdapter(clsDirectBill.mActivity,clsDirectBill.mActivity, arrListModifierItem,"TDHItems",strItemCode+"!"+strItemName+"!"+modifierGroupCode+"!"+maxLimit);
                objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
                gridviewTDHItem.setAdapter(objBtnAdapter);
            }



            btnDone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(mapSelectedMenuAndItemList.size()>0)
                    {
                        int pos=0;
                        for(int i=0;i<listDirectBillSelectedItems.size();i++)
                        {
                            clsDirectBillSelectedListItemBean objBean=listDirectBillSelectedItems.get(i);
                            if(objBean.getStrItemCode().equals(strItemCode))
                            {
                                pos=i;
                                break;
                            }
                        }
                        clsGlobalFunctions objGlobal=new clsGlobalFunctions();
                        String dateTime=clsGlobalFunctions.funGetPOSDateTime();
                        int sequenceNo=0;
                        Iterator<Map.Entry<String, Map<String, String>>> itMenu = mapSelectedMenuAndItemList.entrySet().iterator();
                        while (itMenu.hasNext())
                        {
                            Map.Entry<String, Map<String, String>> menuEntry = itMenu.next();
                            String menuCode = menuEntry.getKey();
                            Iterator<Map.Entry<String, String>> itItems = menuEntry.getValue().entrySet().iterator();
                            while (itItems.hasNext())
                            {
                                Map.Entry<String, String> entry = itItems.next();
                                String itemCode = entry.getKey();
                                String itemName = entry.getValue();
                                boolean flgAlreadyPresentMod=false;
                                sequenceNo++;

                                double rate=0;
                                if(hm_ModifierItemDetail.containsKey(itemCode))
                                {
                                    clsTDHBean objBean=hm_ModifierItemDetail.get(itemCode);
                                    rate=objBean.getDblRate();
                                }



                                if (!flgAlreadyPresentMod) {
                                    clsDirectBillSelectedListItemBean directBillSelectedListItemBean;
                                    String itemModifier = strItemCode + itemCode;
                                    directBillSelectedListItemBean = new clsDirectBillSelectedListItemBean(itemModifier, itemName, 1, rate, rate, true,customerCode,customerType,operationType,"N");
                                    listDirectBillSelectedItems.add(pos + 1, directBillSelectedListItemBean);
                                    pos++;
                                }

                                funRefreshItemGrid();



                            }

                        }
                        mapSelectedMenuAndItemList.clear();
                        mapSelectedMenu.clear();
                        onResume();
                        tdhDialog.dismiss();
                    }
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mapSelectedMenuAndItemList.clear();
                    mapSelectedMenu.clear();
                    tdhDialog.dismiss();

                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        tdhDialog.show();
        Window window = tdhDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.getWindowManager().getDefaultDisplay();

    }

    // webservices for gett customer details on call
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
                                        clsGlobalFunctions.gCustName=objCustomerMaster.getCustomerName();//+"\n"+objCustomerMaster.getCustomerBuildingName();
                                        customerName.setText(clsGlobalFunctions.gCustName);

                                        System.out.println("custDetail :\n"+clsGlobalFunctions.gCustName);

                                        Toast.makeText(clsDirectBill.this,clsGlobalFunctions.gCustName,Toast.LENGTH_LONG).show();


                                        ArrayList<String>CustTypeList= new ArrayList<String>();
                                        if(!objCustomerMaster.getCustomerType().isEmpty())
                                        {
                                            CustTypeList.add(objCustomerMaster.getCustomerType());
                                        }
                                    }

                                }
                                else
                                {
                                    Toast.makeText(clsDirectBill.this,"Customer Not Found",Toast.LENGTH_LONG).show();
                                    pager.setCurrentItem(2);
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

    public void funCheckAndGetModifierListForTDH() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                boolean flgSuperUser = true;
                if (clsGlobalFunctions.gSuperUser.equals("No")) {
                    flgSuperUser = false;
                }
                showDialog();
                App.getAPIHelper().funCheckAndGetModifierListForTDH(clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTDHWithModifierDTO>>() {
                    @Override
                    public void onSuccess(ArrayList<clsTDHWithModifierDTO> listTDHWithModifierDTO) {
                        dismissDialog();
                        if (null != listTDHWithModifierDTO) {
                            try {
                                if (listTDHWithModifierDTO.size() > 0) {
                                    clsTDHWithModifierDTO objTDHWithModifierDTO;
                                    for (int i = 0; i < listTDHWithModifierDTO.size(); i++) {
                                        objTDHWithModifierDTO = listTDHWithModifierDTO.get(i);
                                        mapModifierItemAndQty.put(objTDHWithModifierDTO.getStrTDHMainItemCode(), Double.valueOf(objTDHWithModifierDTO.getTDHMainItemMaxQty()));

                                        ArrayList<clsTDHBean> listModifierGroupList = objTDHWithModifierDTO.getListModifierGroupList();
                                        if (listModifierGroupList.size() > 0) {
                                            clsTDHBean obTDHBean = new clsTDHBean();
                                            for (int j = 0; j < listModifierGroupList.size(); j++) {
                                                obTDHBean = listModifierGroupList.get(j);
                                                hm_ModifierGroupDetail.put(obTDHBean.getStrTDHItemCode(), obTDHBean);

                                                ArrayList<clsTDHBean> listModifierItemsArray = obTDHBean.getListModifierItemList();
                                                if (listModifierItemsArray.size() > 0) {
                                                    clsTDHBean objTdhItem = new clsTDHBean();
                                                    for (int k = 0; k < listModifierItemsArray.size(); k++) {
                                                        objTdhItem = listModifierItemsArray.get(k);
                                                        hm_ModifierItemDetail.put(objTdhItem.getStrTDHItemCode(), objTdhItem);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
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
                SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.setup_your_server_settings);
        }
    }

    public void funGetTDHListWS() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {

                boolean flgSuperUser = true;
                if (clsGlobalFunctions.gSuperUser.equals("No")) {
                    flgSuperUser = false;
                }
                showDialog();
                App.getAPIHelper().funGetTDHListWS(clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray mJsonMainArray) {
                        dismissDialog();
                        Map<String, List<clsTDHBean>> hmapComboItemDtl = new HashMap<>();
                        if (null != mJsonMainArray) {
                            try {
                                JsonObject mJsonMainObject = new JsonObject();
                                for (int i = 0; i < mJsonMainArray.size(); i++) {
                                    List<clsTDHBean> arrListTDHItem = new ArrayList<clsTDHBean>();
                                    mJsonMainObject = (JsonObject) mJsonMainArray.get(i);
                                    if (mJsonMainObject.get("TDHCombo " + (i + 1)).getAsString().toString().equals("")) {
                                        //memberInfo = "no data";
                                    } else {
                                        JsonArray mJsonArray = (JsonArray) mJsonMainObject.get("TDHCombo " + (i + 1));
                                        JsonObject mJsonObject = new JsonObject();
                                        for (int cnt = 0; cnt < mJsonArray.size(); cnt++) {
                                            mJsonObject = (JsonObject) mJsonArray.get(cnt);
                                            clsTDHBean objItem = new clsTDHBean();
                                            objItem.setStrTDHItemCode(mJsonObject.get("TDHItemCode").getAsString().toString());
                                            objItem.setStrTDHItemName(mJsonObject.get("TDHItemName").getAsString().toString());
                                            objItem.setDblQty(Double.parseDouble(mJsonObject.get("Quantity").getAsString()));
                                            objItem.setStrDefaultYN(mJsonObject.get("DefaultYN").getAsString().toString());
                                            objItem.setStrModifierGroupCode(mJsonObject.get("SubMenuCode").getAsString().toString());
                                            arrListTDHItem.add(objItem);
                                        }
                                    }
                                    hmapComboItemDtl.put(mJsonMainObject.get("TDHMainItemCode").getAsString().toString(), arrListTDHItem);
                                }
                                if (hm_ComboItemDtl.size() > 0) {
                                    hm_ComboItemDtl.clear();
                                    hm_ComboItemDtl = hmapComboItemDtl;
                                }
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
                SnackBarUtils.showSnackBar(this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(this, R.string.setup_your_server_settings);
        }
    }

    private void funGetItemDetailsByExternalCode(String externalCode)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                String tableNo="";
                App.getAPIHelper().funGetItemDetailsByExternalCode(clsGlobalFunctions.gPOSCode,externalCode,clsGlobalFunctions.gAreaWisePricing,tableNo,clsGlobalFunctions.gDirectBillerAreaCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotItemsListBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsKotItemsListBean> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            try
                            {
                                String pricingDay = new clsUtility().funGetDayForPricing();
                                final ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
                                if (null != arrListTemp) {
                                    clsKotItemsListBean obj = new clsKotItemsListBean();
                                    if (arrListTemp.size() > 0) {
                                        for (int i = 0; i < arrListTemp.size(); i++) {
                                            obj = arrListTemp.get(i);
                                            obj.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
                                            arrListItemMastertemp.add(obj);
                                        }
                                    }
                                    if (arrListItemMastertemp.size() > 0) {
                                        clsKotItemsListBean obItem = arrListItemMastertemp.get(0);
                                        if(obItem.getDblSalePrice()>0){
                                            funRefreshItemDtl(obItem.getStrItemCode(), obItem.getStrItemName(), "", obItem.getDblSalePrice());
                                            edtExternalCode.setText("");
                                            CommonUtils.hideKeyboard(edtExternalCode);
                                        }
                                        else{

                                            final Dialog dialog = new Dialog(mActivity);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setContentView(R.layout.dialogforinput);

                                            final EditText edtInputTypeValue = (EditText) dialog.findViewById(R.id.edtInputTypeValue);
                                            edtInputTypeValue.setInputType(TYPE_CLASS_NUMBER);

                                            final Button btnOK = (Button) dialog.findViewById(R.id.btnOk);
                                            TextView txtInputName = (TextView) dialog.findViewById(R.id.txtInputName);
                                            TextView txtInputType=(TextView)dialog.findViewById(R.id.textInputName);
                                            txtInputName.setText("Enter Rate");
                                            txtInputType.setText("Enter Rate");

                                            btnOK.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (edtInputTypeValue.getText().toString().isEmpty()) {
                                                        Toast.makeText(mActivity, "Enter rate", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    } else {
                                                        clsKotItemsListBean obItem = arrListItemMastertemp.get(0);
                                                        if (obItem.getDblSalePrice() == 0) {
                                                            funRefreshItemDtl(obItem.getStrItemCode(), obItem.getStrItemName(), "", Double.valueOf(edtInputTypeValue.getText().toString()));

                                                            new CommonUtils().hideKeyboard(edtInputTypeValue);
                                                            dialog.dismiss();
                                                        }


                                                    }
                                                }
                                            });
                                            dialog.show();
                                            Window window = dialog.getWindow();
                                            window.setGravity(Gravity.CENTER);
                                            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                            window.getWindowManager().getDefaultDisplay();
                                        }
                                        edtExternalCode.setText("");
                                        CommonUtils.hideKeyboard(edtExternalCode);
                                    } else {
                                        Toast.makeText(clsDirectBill.mActivity, "Item not found!!!!!", Toast.LENGTH_LONG).show();
                                        edtExternalCode.setText("");
                                        CommonUtils.hideKeyboard(edtExternalCode);
                                    }
                                }
                                else {
                                    Toast.makeText(clsDirectBill.mActivity, "Item not found!!!!!", Toast.LENGTH_LONG).show();
                                    edtExternalCode.setText("");
                                    CommonUtils.hideKeyboard(edtExternalCode);
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
                SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.setup_your_server_settings);
        }
    }


    private List<clsTDHBean> funGetTDHMenuList(String... params)
    {
        arrListModifierGroup = new ArrayList();
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetTDHMenuList(clsGlobalFunctions.gClientCode,params[0] , new BaseAPIHelper.OnRequestComplete<ArrayList<clsTDHBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsTDHBean> arrTDH) {
                        dismissDialog();
                        if (null != arrTDH) {
                            try{
                                arrListModifierGroup=arrTDH;
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
                SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.setup_your_server_settings);
        }
        return  arrListModifierGroup;
    }


    private void funGetModifierList(String itemcode) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetModifierList(itemcode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsItemModifierBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsItemModifierBean> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            try {
                                if (arrListTemp.size() > 0) {

                                    final ArrayList arrListItemModifier = (ArrayList) arrListTemp;
                                    final Dialog dialog = new Dialog(clsDirectBill.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.kotmodifierdialog);

                                    //  dialog.setTitle("Freeflow Modifier");

                                    final ArrayList arrList = new ArrayList<String>();
                                    Button btnClose = (Button) dialog.findViewById(R.id.btnFFClose);
                                    final GridView dialoglist= (GridView) dialog.findViewById(R.id.dialoglist);
                                    for (int cnt = 0; cnt < arrListTemp.size(); cnt++) {
                                        clsItemModifierBean objItem = (clsItemModifierBean) arrListTemp.get(cnt);
                                        arrList.add(objItem.getStrModifierName());
                                    }
                                    clsOrderDialogAdapter adapter = new clsOrderDialogAdapter(getBaseContext(), arrList);
                                    dialoglist.setAdapter(adapter);
                                    dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                                            clsItemModifierBean objItemModifier = (clsItemModifierBean) arrListItemModifier.get(position);

                                            boolean flgAlreadyPresentMod = false;
                                            for (int cnt = 0; cnt < listDirectBillSelectedItems.size(); cnt++) {
                                                if (listDirectBillSelectedItems.get(cnt).isModifier()) {

                                                    clsDirectBillSelectedListItemBean objTemp = listDirectBillSelectedItems.get(cnt);
                                                    String itemWithModifier = listDirectBillSelectedItems.get(cnt).getStrItemCode();
                                                    String itemCode1 = itemWithModifier.substring(0, 7);
                                                    String modifierCode1 = itemWithModifier.substring(7, 11);
                                                    if (itemCode1.equals(objItemModifier.getStrItemCode()) && modifierCode1.equals(objItemModifier.getStrModifierCode())) {
                                                        long modQty = objTemp.getQty();
                                                        modQty = modQty + 1;
                                                        double amt = objItemModifier.getRate() * modQty;
                                                        objTemp.setAmount(amt);
                                                        objTemp.setQty(modQty);
                                                        listDirectBillSelectedItems.set(cnt, objTemp);
                                                        flgAlreadyPresentMod = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!flgAlreadyPresentMod) {
                                                clsDirectBillSelectedListItemBean directBillSelectedListItemBean;
                                                String itemModifier = objItemModifier.getStrItemCode() + objItemModifier.getStrModifierCode();
                                                directBillSelectedListItemBean = new clsDirectBillSelectedListItemBean(itemModifier, objItemModifier.getStrModifierName(), 1, objItemModifier.getRate(), objItemModifier.getRate(), true,customerCode,customerType,operationType,"N");
                                                listDirectBillSelectedItems.add(listpos + 1, directBillSelectedListItemBean);
                                            }
                                            funRefreshItemGrid();
                                            onResume();
                                            dialog.dismiss();
                                        }
                                    });

                                    btnClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    Window window = dialog.getWindow();
                                    window.setGravity(Gravity.CENTER);
                                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                    window.getWindowManager().getDefaultDisplay();

                                }

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
                SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsDirectBill.mActivity, R.string.setup_your_server_settings);
        }
    }


   /* protected void showDialog() {
        if (null == pgDialog ) {
            if(mActivity ==null)
                //mActivity=clsDirectBill.mActivity;
            pgDialog = CommonUtils.getProgressDialog(mActivity, 0, false);
        }
        pgDialog.show();
    }*/

    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsDirectBill.mActivity, 0, false);
        }
        pgDialog.show();
    }
    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}

