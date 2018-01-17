package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.bewo.mach.MACHPrinter;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.adapter.clsDirectBillSelectedItemsCustomBaseAdapter;
import com.example.apos.adapter.clsOrderDialogAdapter;
import com.example.apos.adapter.clsTakeAwayViewPageAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsBillHd;
import com.example.apos.bean.clsBillSettlementDtl;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsSettlementDtl;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.fragments.clsDirectBillerClsMenuItemFragment;
import com.example.apos.fragments.clsDirectBillerItemListFragment;
import com.example.apos.fragments.clsTakeAwayClsMenuItemFragment;
import com.example.apos.fragments.clsTakeAwayItemListFragment;
import com.example.apos.listeners.clsDirectBillerItemListSelectionListener;
import com.example.apos.listeners.clsTakeAwayActListener;
import com.example.apos.util.Utils;
import com.example.apos.util.clsSlidingTabLayout;
import com.example.apos.util.clsUtility;
import com.example.apos.util.goldpos.PrinterJBInterface;
import com.example.apos.util.mach.clsPrintFormatAPI;

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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class clsTakeAwayScreen extends ActionBarActivity implements View.OnClickListener,clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener,clsTakeAwayActListener {

    private TextView tvHeaderTimeStamp = null;
    private static TextView edtTakeAwayTotalAmount = null;
    private static ListView lvTakeAwaySelectedItems = null;
    EditText edtExternalCode=null;
    public static List<clsDirectBillSelectedListItemBean> listTakeAwaySelectedItems;
    public static clsDirectBillSelectedItemsCustomBaseAdapter objDirectBillerItemDtl;
    private Button btnDirectBillBack = null, btnDirectSettle = null;
    public static Activity mActivity;
    public static ViewPager pager;
    clsTakeAwayViewPageAdapter takeAwayViewPageAdapter;
    clsSlidingTabLayout tabs;
    Intent iData;
    CharSequence Titles[] = {"Items", "Menu"};
    int Numboftabs = 2;
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
    private String billNo;
    public String itemcode;
    public int listpos;
    private  String keyCase="upperCase";
    private String homeDelivery;
    private String customerCode;
    private String customerType;
    private  String operationType;
    private  String takeAway;
    private Dialog pgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mActivity = this;
        getSupportActionBar().hide();
        setContentView(R.layout.takeawayscreen);

        iData = getIntent();
        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();
        widgetInit();

        listTakeAwaySelectedItems = new ArrayList<clsDirectBillSelectedListItemBean>();

        takeAwayViewPageAdapter = new clsTakeAwayViewPageAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, pager);
        pager = (ViewPager) findViewById(R.id.pager_take_away);
        pager.setAdapter(takeAwayViewPageAdapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_take_away);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new clsSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);


    }

    private void widgetInit() {
        operationType="Take Away";
        customerCode="";
        customerType="";
        billNo = "";
        subTotalAmt = 0;
        taxTotalAmt = 0;
        discTotalAmt = 0;
        debitCardMemberCode = "";
        debitCardMemberName = "";
        debitCardNo = "";
        debitCardString = "";
        totalMemberBalance = 0;
        tvHeaderTimeStamp = (TextView) findViewById(R.id.tv_take_away_header_timestamp);
        btnDirectSettle = (Button) findViewById(R.id.btntakeawaySettle);
        btnDirectSettle.setOnClickListener(this);
        lvTakeAwaySelectedItems = (ListView) findViewById(R.id.listtakeawayselecteditems);
        edtTakeAwayTotalAmount = (TextView) findViewById(R.id.edt_take_away_total_order_amount);
        tvHeaderTimeStamp.setText(Utils.getCurrentDate());
        edtExternalCode = (EditText) findViewById(R.id.edtExternalCode);

    }

   @Override
    public void setMenuItemSelectionCodeResult(String strMenuItemCode, String strMenuItemName, String strMenuType)
    {
        if(strMenuType.equals("MenuHead"))
        {
            if(clsGlobalFunctions.gMenuItemSortingOn.equals("subMenuHeadWise"))
            {
                clsTakeAwayClsMenuItemFragment obj=new clsTakeAwayClsMenuItemFragment();
                obj.populateSelectedSubMenuItems(strMenuItemCode, strMenuItemName);
                pager.setCurrentItem(1);
            }
            else
            {
                new clsTakeAwayItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName,strMenuType);
                pager.setCurrentItem(0);
            }
        }
        else
        {
            new clsTakeAwayItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName,strMenuType);
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
        for (int k = 0; k < listTakeAwaySelectedItems.size(); k++) {
            if (listTakeAwaySelectedItems.get(k).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                clsDirectBillSelectedListItemBean directBillerItemDtl =
                        (clsDirectBillSelectedListItemBean) listTakeAwaySelectedItems.get(k);
                if (directBillerItemDtl.getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                    directBillerItemDtl.setQty(directBillerItemDtl.getQty() + 1);
                    directBillerItemDtl.setAmount(directBillerItemDtl.getAmount() + dblSalePrice);
                    directBillerItemDtl.setItemSalePrice(dblSalePrice);
                    bCheckItemAlreadyPresent = true;
                    iListViewScrollPosition = k;
                    listTakeAwaySelectedItems.set(k, directBillerItemDtl);

                    break;
                }
            }
        }

        if (!bCheckItemAlreadyPresent) {
            listTakeAwaySelectedItems.add(directBillSelectedListItemBean);
        }

        objDirectBillerItemDtl = new clsDirectBillSelectedItemsCustomBaseAdapter(mActivity, listTakeAwaySelectedItems);
        lvTakeAwaySelectedItems.setItemsCanFocus(true);
        lvTakeAwaySelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        objDirectBillerItemDtl.setCustomButtonListner((clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
        lvTakeAwaySelectedItems.setAdapter(objDirectBillerItemDtl);
        lvTakeAwaySelectedItems.setSelection(objDirectBillerItemDtl.getCount() - 1);
        if (iListViewScrollPosition != -1) {
            lvTakeAwaySelectedItems.setSelection(iListViewScrollPosition);
        }

        subTotalAmt = 0;
        for (int l = 0; l < listTakeAwaySelectedItems.size(); l++) {
            subTotalAmt = subTotalAmt + listTakeAwaySelectedItems.get(l).getAmount();
            edtTakeAwayTotalAmount.setText(String.valueOf(subTotalAmt));
        }

    }


    private void funRefreshItemGrid() {
        int iListViewScrollPosition = -1;
        objDirectBillerItemDtl = new clsDirectBillSelectedItemsCustomBaseAdapter(mActivity, listTakeAwaySelectedItems);
        lvTakeAwaySelectedItems.setItemsCanFocus(true);
        lvTakeAwaySelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        objDirectBillerItemDtl.setCustomButtonListner((clsDirectBillSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
        lvTakeAwaySelectedItems.setAdapter(objDirectBillerItemDtl);
        lvTakeAwaySelectedItems.setSelection(objDirectBillerItemDtl.getCount() - 1);
        if (iListViewScrollPosition != -1) {
            lvTakeAwaySelectedItems.setSelection(iListViewScrollPosition);
        }
        subTotalAmt = 0;
        for (int l = 0; l < listTakeAwaySelectedItems.size(); l++) {
            subTotalAmt = subTotalAmt + listTakeAwaySelectedItems.get(l).getAmount();
            edtTakeAwayTotalAmount.setText(String.valueOf(subTotalAmt));
        }
    }
    private void funClearObjects() {
        hmBillSettleAmtDtl = null;
        listTakeAwaySelectedItems = null;
        clsGlobalFunctions.gCounterCode = "";
        //clsGlobalFunctions.gCounterWiseBilling="No";
    }

    @Override
    public void onBackPressed() {
        funClearObjects();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btntakeawaySettle:

                if (listTakeAwaySelectedItems.size() > 0)
                {
                    if(clsGlobalFunctions.gCMSIntegrationYN.equalsIgnoreCase("Y"))
                    {
                        funSettleForClubFunction();
                    }
                    else
                    {
                        for (int cnt = 0; cnt < listTakeAwaySelectedItems.size(); cnt++)
                        {
                            clsDirectBillSelectedListItemBean objSelectedList = listTakeAwaySelectedItems.get(cnt);
                            objSelectedList.setStrCustomerType(customerType);
                            objSelectedList.setStrCustomerCode(customerCode);
                            objSelectedList.setStrOperationType(operationType);
                            listTakeAwaySelectedItems.set(cnt, objSelectedList);

                        }
                        Intent billSettleIntent = new Intent(clsTakeAwayScreen.this, clsBillSettlement.class);
                        billSettleIntent.putExtra("LIST", (Serializable) listTakeAwaySelectedItems);
                        startActivity(billSettleIntent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(clsTakeAwayScreen.this, "Select Items For Billing!!! ", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }

    private void funSettleForClubFunction()
    {
        List<clsSettlementDtl> listSettlementDtl = clsGlobalFunctions.gHashMapSettlementDtl.get(clsGlobalFunctions.gPOSCode);
        if (listSettlementDtl.size() > 0)
        {
            clsSettlementDtl objSettlementDtl = listSettlementDtl.get(0);
            String settlementType = objSettlementDtl.getStrSettlementType();
            if (settlementType.equals("Member"))
            {
                funSettlementDialogForMember("Enter Member No", "CMS");
            }
            else if (settlementType.equals("Debit Card")) {
                double totalAmt = Double.parseDouble(edtTakeAwayTotalAmount.getText().toString());
                if (totalMemberBalance < totalAmt) {
                    Toast.makeText(clsTakeAwayScreen.this, "Insufficient Balance!!! " + totalMemberBalance, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            else if (settlementType.equals("Cash")) {
                double totalAmt = Double.parseDouble(edtTakeAwayTotalAmount.getText().toString());
            }
        }
        else
        {
            Toast.makeText(clsTakeAwayScreen.this, "settlememnt mode not found!!! ", Toast.LENGTH_LONG).show();
        }
    }


    private void funSettlementDialogForMember(String lableName, String memberType) {
        final Dialog dialog = new Dialog(clsTakeAwayScreen.this);
        dialog.setContentView(R.layout.directbillerdiasettlebox);
        dialog.setTitle(lableName);
        final EditText edtDebitCardString = (EditText) dialog.findViewById(R.id.edtDbtCrdString);
        edtDebitCardString.setVisibility(View.VISIBLE);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(450, 500);

        Button btnback = (Button) dialog.findViewById(R.id.btndiaback);
        Button btnSwipe = (Button) dialog.findViewById(R.id.btnSwipe);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_take_away_header_timestamp);
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
            final Dialog dialog = new Dialog(clsTakeAwayScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.edititemqty);
            //  dialog.setTitle("itemname");
            final int pos = position;
            clsDirectBillSelectedListItemBean objDirectBillerItem = listTakeAwaySelectedItems.get(position);

            ImageButton btnMinus = (ImageButton) dialog.findViewById(R.id.btnRemoveQty);
            ImageButton btnAdd = (ImageButton) dialog.findViewById(R.id.btnAddQty);
            Button btnOk = (Button) dialog.findViewById(R.id.btnUpdate);
            ImageView btnCloseDialog = (ImageView) dialog.findViewById(R.id.btnClose);
            ImageView btnDeleteItem = (ImageView) dialog.findViewById(R.id.btnDeleteItem);
            final EditText edtFFName = (EditText) dialog.findViewById(R.id.edtKotFFItemName);
            final EditText edtFFRate = (EditText) dialog.findViewById(R.id.edtKotFFItemRate);

            Button btnPredefinedModifier = (Button) dialog.findViewById(R.id.btnKotModifier);
              TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
            final TextView txtQty = (TextView) dialog.findViewById(R.id.txtQty);

            txtItemName.setText(objDirectBillerItem.getStrDesc());
            txtQty.setText("" + objDirectBillerItem.getQty());


            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clsDirectBillSelectedListItemBean objDirectBillerItem = listTakeAwaySelectedItems.get(pos);
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
                        listTakeAwaySelectedItems.set(pos, objDirectBillerItem);

                        funRefreshItemGrid();
                    } else if (qty == 1) {

                        String strDeleteItemCode = listTakeAwaySelectedItems.get(pos).getStrItemCode();
                        listTakeAwaySelectedItems.remove(pos);
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

                    clsDirectBillSelectedListItemBean objDirectBillerItem = listTakeAwaySelectedItems.get(pos);
                    long qty1 = 0;
                    long qty = objDirectBillerItem.getQty();
                    //double rate = ((objDirectBillerItem.getAmount()) / (objDirectBillerItem.getQty()));
                    qty = qty + 1;
                    double amt = qty * objDirectBillerItem.getItemSalePrice();
                    objDirectBillerItem.setQty(qty);
                    objDirectBillerItem.setAmount(amt);
                    qty1 = objDirectBillerItem.getQty();
                    listTakeAwaySelectedItems.set(pos, objDirectBillerItem);

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
                        clsDirectBillSelectedListItemBean objDirectBillerItem = listTakeAwaySelectedItems.get(pos);
                        String strDeleteItemCode = listTakeAwaySelectedItems.get(pos).getStrItemCode();
                        listTakeAwaySelectedItems.remove(pos);
                        funDeleteItemFromGrid(strDeleteItemCode);
                    }
                    funRefreshItemGrid();
                    dialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)  {
                    String freeFlowModName="";
                    try{
                        freeFlowModName=new clsUtility().funCheckSpecialCharacters(edtFFName.getText().toString());
                        clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                        if(edtFFName.getText().toString().isEmpty())
                        {
                            Toast.makeText(mActivity, "Enter Free Flow Modifier Name",Toast.LENGTH_SHORT).show();
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
                            listTakeAwaySelectedItems.add(listpos + 1, directBillSelectedListItemBean);
                            funRefreshItemGrid();
                            dialog.dismiss();
                        }

                        funRefreshItemGrid();
                        dialog.dismiss();
                    }catch(Exception e){
                        e.printStackTrace();
                    }


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
            window.setGravity(Gravity.TOP);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.getWindowManager().getDefaultDisplay();
        }
    }

    private void funDeleteItemFromGrid(String strDeleteItemCode)
    {
        if(listTakeAwaySelectedItems.size()>0 )
        {
            for (int cnt = 0; cnt <= listTakeAwaySelectedItems.size(); cnt++)
            {
                clsDirectBillSelectedListItemBean obj=null;
                if(cnt==listTakeAwaySelectedItems.size())
                {
                    obj= (clsDirectBillSelectedListItemBean) listTakeAwaySelectedItems.get(cnt-1);
                    if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                    {
                        listTakeAwaySelectedItems.remove(obj);
                    }
                    else
                    {
                        if(cnt>=2)
                        {
                            obj = (clsDirectBillSelectedListItemBean) listTakeAwaySelectedItems.get(cnt - 2);
                            if (obj.getStrItemCode().contains(strDeleteItemCode + "M")) {
                                listTakeAwaySelectedItems.remove(obj);
                            }
                        }
                    }
                }
                else
                {
                    obj = (clsDirectBillSelectedListItemBean) listTakeAwaySelectedItems.get(cnt);
                    if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                    {
                        listTakeAwaySelectedItems.remove(obj);
                    }
                    else
                    {
                        if(cnt!=0)
                        {
                            obj= (clsDirectBillSelectedListItemBean) listTakeAwaySelectedItems.get(cnt-1);
                            if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                            {
                                listTakeAwaySelectedItems.remove(obj);
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
                                    final Dialog dialog = new Dialog(clsTakeAwayScreen.this);
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
                                            for (int cnt = 0; cnt < listTakeAwaySelectedItems.size(); cnt++) {
                                                if (listTakeAwaySelectedItems.get(cnt).isModifier()) {

                                                    clsDirectBillSelectedListItemBean objTemp = listTakeAwaySelectedItems.get(cnt);
                                                    String itemWithModifier = listTakeAwaySelectedItems.get(cnt).getStrItemCode();
                                                    String itemCode1 = itemWithModifier.substring(0, 7);
                                                    String modifierCode1 = itemWithModifier.substring(7, 11);
                                                    if (itemCode1.equals(objItemModifier.getStrItemCode()) && modifierCode1.equals(objItemModifier.getStrModifierCode())) {
                                                        long modQty = objTemp.getQty();
                                                        modQty = modQty + 1;
                                                        double amt = objItemModifier.getRate() * modQty;
                                                        objTemp.setAmount(amt);
                                                        objTemp.setQty(modQty);
                                                        listTakeAwaySelectedItems.set(cnt, objTemp);
                                                        flgAlreadyPresentMod = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!flgAlreadyPresentMod) {
                                                clsDirectBillSelectedListItemBean directBillSelectedListItemBean;
                                                String itemModifier = objItemModifier.getStrItemCode() + objItemModifier.getStrModifierCode();
                                                directBillSelectedListItemBean = new clsDirectBillSelectedListItemBean(itemModifier, objItemModifier.getStrModifierName(), 1, objItemModifier.getRate(), objItemModifier.getRate(), true,customerCode,customerType,operationType,"N");
                                                listTakeAwaySelectedItems.add(listpos + 1, directBillSelectedListItemBean);
                                            }
                                            funRefreshItemGrid();
                                            onResume();
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
                            catch
                             (Exception e) {
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
                SnackBarUtils.showSnackBar(clsTakeAwayScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsTakeAwayScreen.mActivity, R.string.setup_your_server_settings);
        }
    }

    protected void showDialog() {
        if (null == pgDialog ) {
            pgDialog = CommonUtils.getProgressDialog(clsTakeAwayScreen.mActivity, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }
}
