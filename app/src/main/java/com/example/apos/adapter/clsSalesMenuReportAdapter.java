package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.apos.activity.R;
import com.example.apos.bean.clsSalesReportDtl;
import java.util.ArrayList;
import java.util.List;


public  class clsSalesMenuReportAdapter extends BaseAdapter {
    int count = 0;
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;
    private List<clsSalesReportDtl> arrListItem = new ArrayList();
    private String reportType;

    TableLayout tl;
    TableRow tr;

    public clsSalesMenuReportAdapter(Context context,  List<clsSalesReportDtl> arrListBillWise,String reportType) {
        this.arrListItem = arrListBillWise;
        this.context = context;
        this.reportType = reportType;
    }

    /*private view holder class*/
    public class ViewHolder {
        TextView textItemCode, textItemName, textPOSName, textSaleAmount, textSalePer;
        TableLayout tblLayout;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.salesmenureportitemlist, null);
            viewHolder = new ViewHolder();
            tl = (TableLayout) convertView.findViewById(R.id.table_main);
            if(reportType.equals("BillWise"))
            {
                addBillWiseHeaders();
                addBillWiseData();
            }
            else if(reportType.equals("SettlementWise"))
            {
                addSettlementWiseHeaders();
                addSettlementWiseData();
            }
            else if(reportType.equals("ItemWise"))
            {
                addItemWiseHeaders();
                addItemWiseData();
            }
            else if(reportType.equals("MenuWise"))
            {
                addMenuWiseHeaders();
                addMenuWiseData();
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        return convertView;
    }

    public int getCount() {
        return 1;
    }

    public Object getItem(int position) {
        return arrListItem.get(position);
    }

    public long getItemId(int position) {
        return arrListItem.indexOf(getItem(position));
    }

    public void addBillWiseHeaders()
    {

        /** Create a TableRow dynamically **/
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView tvBillNo = new TextView(context);
        tvBillNo.setText("BillNo");
        tvBillNo.setTextColor(Color.BLACK);
        tvBillNo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvBillNo.setPadding(5, 5, 5, 0);
        tvBillNo.setTextSize(12);
        tr.addView(tvBillNo);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvBillDate = new TextView(context);
        tvBillDate.setText("BillDate");
        tvBillDate.setTextColor(Color.BLACK);
        tvBillDate.setPadding(5, 5, 5, 0);
        tvBillDate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvBillDate.setTextSize(12);
        tr.addView(tvBillDate); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvBillTime = new TextView(context);
        tvBillTime.setText("BillTime");
        tvBillTime.setTextColor(Color.BLACK);
         tvBillTime.setPadding(5, 5, 5, 0);
        tvBillTime.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvBillTime.setTextSize(12);
        tr.addView(tvBillTime); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvTable = new TextView(context);
        tvTable.setText("Table");
        tvTable.setTextColor(Color.BLACK);
        tvTable.setPadding(5, 5, 5, 0);
        tvTable.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvTable.setTextSize(12);
        tr.addView(tvTable); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvPOS = new TextView(context);
        tvPOS.setText("POS");
        tvPOS.setTextColor(Color.BLACK);
        tvPOS.setPadding(5, 5, 5, 0);
        tvPOS.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvPOS.setTextSize(12);
        tr.addView(tvPOS); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvPayMode = new TextView(context);
        tvPayMode.setText("PayMode");
        tvPayMode.setTextColor(Color.BLACK);
        tvPayMode.setPadding(5, 5, 5, 0);
        tvPayMode.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvPayMode.setTextSize(12);
        tr.addView(tvPayMode); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvSubTotal = new TextView(context);
        tvSubTotal.setText("SubTotal");
        tvSubTotal.setGravity(Gravity.RIGHT);
        tvSubTotal.setTextColor(Color.BLACK);
        tvSubTotal.setPadding(5, 5, 5, 0);
        tvSubTotal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSubTotal.setTextSize(12);
        tr.addView(tvSubTotal); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvDis = new TextView(context);
        tvDis.setText("Dis");
        tvDis.setGravity(Gravity.RIGHT);
        tvDis.setTextColor(Color.BLACK);
        tvDis.setPadding(5, 5, 5, 0);
        tvDis.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvDis.setTextSize(12);
        tr.addView(tvDis); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvTax = new TextView(context);
        tvTax.setText("Tax");
        tvTax.setGravity(Gravity.RIGHT);
        tvTax.setTextColor(Color.BLACK);
        tvTax.setPadding(5, 5, 5, 0);
        tvTax.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvTax.setTextSize(12);
        tr.addView(tvTax); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvGrandTotal = new TextView(context);
        tvGrandTotal.setText("G.Total");
        tvGrandTotal.setGravity(Gravity.RIGHT);
        tvGrandTotal.setTextColor(Color.BLACK);
        tvGrandTotal.setPadding(5, 5, 5, 0);
        tvGrandTotal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvGrandTotal.setTextSize(12);
        tr.addView(tvGrandTotal); // Adding textView to tablerow.




        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));




        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


       TextView [] myTextViews = new TextView[10]; // create an empty array;
        for (int j = 0; j < 10; j++) {
            // create a new textview
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText("_____________");
            rowTextView.setTextColor(Color.BLACK);
            rowTextView.setGravity(Gravity.RIGHT);
            //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            rowTextView.setPadding(5, 0, 0, 0);
            rowTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            // add the textview to the linearlayout
            rowTextView.setTextSize(12);
            tr.addView(rowTextView);
            // save a reference to the textview for later
            myTextViews[j] = rowTextView;
        }

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


    }



    /**
     * This function add the data to the table
     **/
    public void addBillWiseData()
    {
        for(int k=0;k<arrListItem.size();k++)
        {
            clsSalesReportDtl objSale = (clsSalesReportDtl) arrListItem.get(k);



            /** Create a TableRow dynamically **/
            tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            TextView tvBillNo = new TextView(context);
            tvBillNo.setText(objSale.getStrbillno());
            tvBillNo.setTextColor(Color.BLACK);
            tvBillNo.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvBillNo.setPadding(5, 5, 5, 0);
            tvBillNo.setTextSize(12);
            tr.addView(tvBillNo);  // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvBillDate = new TextView(context);
            tvBillDate.setText(objSale.getDtebilldate());
            tvBillDate.setTextColor(Color.BLACK);
            tvBillDate.setPadding(5, 5, 5, 0);
            tvBillDate.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvBillDate.setTextSize(12);
            tr.addView(tvBillDate); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvBillTime = new TextView(context);
            tvBillTime.setText(objSale.getBilltime());
            tvBillTime.setTextColor(Color.BLACK);
            tvBillTime.setPadding(5, 5, 5, 0);
            tvBillTime.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvBillTime.setTextSize(12);
            tr.addView(tvBillTime); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvTable = new TextView(context);
            tvTable.setText(objSale.getTableName());
            tvTable.setTextColor(Color.BLACK);
            tvTable.setPadding(5, 5, 5, 0);
            tvTable.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvTable.setTextSize(12);
            tr.addView(tvTable); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvPOS = new TextView(context);
            tvPOS.setText(objSale.getPosName());
            tvPOS.setTextColor(Color.BLACK);
            tvPOS.setPadding(5, 5, 5, 0);
            tvPOS.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvPOS.setTextSize(12);
            tr.addView(tvPOS); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvPayMode = new TextView(context);
            tvPayMode.setText(objSale.getPayMode());
            tvPayMode.setTextColor(Color.BLACK);
            tvPayMode.setPadding(5, 5, 5, 0);
            tvPayMode.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvPayMode.setTextSize(12);
            tr.addView(tvPayMode); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvSubTotal = new TextView(context);
            tvSubTotal.setText(objSale.getSubTotal());
            tvSubTotal.setTextColor(Color.BLACK);
            tvSubTotal.setGravity(Gravity.RIGHT);
            tvSubTotal.setPadding(5, 5, 5, 0);
            tvSubTotal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSubTotal.setTextSize(12);
            tr.addView(tvSubTotal); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvDis = new TextView(context);
            tvDis.setText(objSale.getDiscountAmt());
            tvDis.setTextColor(Color.BLACK);
            tvDis.setGravity(Gravity.RIGHT);
            tvDis.setPadding(5, 5, 5, 0);
            tvDis.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvDis.setTextSize(12);
            tr.addView(tvDis); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvTax = new TextView(context);
            tvTax.setText(objSale.getTaxAmt());
            tvTax.setTextColor(Color.BLACK);
            tvTax.setGravity(Gravity.RIGHT);
            tvTax.setPadding(5, 5, 5, 0);
            tvTax.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvTax.setTextSize(12);
            tr.addView(tvTax); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvGrandTotal = new TextView(context);
            tvGrandTotal.setText(objSale.getGrandTotal());
            tvGrandTotal.setTextColor(Color.BLACK);
            tvGrandTotal.setPadding(5, 5, 5, 0);
            tvGrandTotal.setGravity(Gravity.RIGHT);
            tvGrandTotal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvGrandTotal.setTextSize(12);
            tr.addView(tvGrandTotal); // Adding textView to tablerow.



            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


        }

    }



    public void addSettlementWiseHeaders()
    {

        /** Create a TableRow dynamically **/
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView tvPosName = new TextView(context);
        tvPosName.setText("POS Name");
        tvPosName.setTextColor(Color.BLACK);
        tvPosName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvPosName.setPadding(5, 5, 5, 0);
        tvPosName.setTextSize(12);
        tr.addView(tvPosName);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvSettleMode = new TextView(context);
        tvSettleMode.setText("Settlement Mode");
        tvSettleMode.setTextColor(Color.BLACK);
        tvSettleMode.setPadding(5, 5, 5, 0);
        tvSettleMode.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSettleMode.setTextSize(12);
        tr.addView(tvSettleMode); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvSaleAmt = new TextView(context);
        tvSaleAmt.setText("Sales Amount");
        tvSaleAmt.setGravity(Gravity.RIGHT);
        tvSaleAmt.setTextColor(Color.BLACK);
        tvSaleAmt.setPadding(5, 5, 5, 0);
        tvSaleAmt.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSaleAmt.setTextSize(12);
        tr.addView(tvSaleAmt); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvSalePer = new TextView(context);
        tvSalePer.setText("Sales(%");
        tvSalePer.setGravity(Gravity.RIGHT);
        tvSalePer.setTextColor(Color.BLACK);
        tvSalePer.setPadding(5, 5, 5, 0);
        tvSalePer.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSalePer.setTextSize(12);
        tr.addView(tvSalePer); // Adding textView to tablerow.




        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));




        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        TextView [] myTextViews = new TextView[4]; // create an empty array;
        for (int j = 0; j < 4; j++) {
            // create a new textview
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText("____________________");
            rowTextView.setTextColor(Color.BLACK);
            rowTextView.setGravity(Gravity.RIGHT);
            //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            rowTextView.setPadding(5, 0, 0, 0);
            rowTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            // add the textview to the linearlayout
            rowTextView.setTextSize(12);
            tr.addView(rowTextView);
            // save a reference to the textview for later
            myTextViews[j] = rowTextView;
        }

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
     }




    /**
     * This function add the data to the table
     **/
    public void addSettlementWiseData()
    {
        for(int k=0;k<arrListItem.size();k++)
        {
            clsSalesReportDtl objSale = (clsSalesReportDtl) arrListItem.get(k);



            /** Create a TableRow dynamically **/
            tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            TextView tvPosName = new TextView(context);
            tvPosName.setText(objSale.getPosName());
            tvPosName.setTextColor(Color.BLACK);
            tvPosName.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvPosName.setPadding(5, 5, 5, 0);
            tvPosName.setTextSize(12);
            tr.addView(tvPosName);  // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvSettleMode = new TextView(context);
            tvSettleMode.setText(objSale.getPayMode());
            tvSettleMode.setTextColor(Color.BLACK);
            tvSettleMode.setPadding(5, 5, 5, 0);
            tvSettleMode.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSettleMode.setTextSize(12);
            tr.addView(tvSettleMode); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvSaleAmt = new TextView(context);
            tvSaleAmt.setText(String.valueOf(objSale.getDblSettleAmt()));
            tvSaleAmt.setGravity(Gravity.RIGHT);
            tvSaleAmt.setTextColor(Color.BLACK);
            tvSaleAmt.setPadding(5, 5, 5, 0);
            tvSaleAmt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSaleAmt.setTextSize(12);
            tr.addView(tvSaleAmt); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvSalePer = new TextView(context);
            tvSalePer.setText(objSale.getStrSalePer());
            tvSalePer.setTextColor(Color.BLACK);
            tvSalePer.setGravity(Gravity.RIGHT);
            tvSalePer.setPadding(5, 5, 5, 0);
            tvSalePer.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSalePer.setTextSize(12);
            tr.addView(tvSalePer); // Adding textView to tablerow.





            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


        }

    }






    public void addItemWiseHeaders()
    {

        /** Create a TableRow dynamically **/
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView tvItemName = new TextView(context);
        tvItemName.setText("Item Name");
        tvItemName.setTextColor(Color.BLACK);
        tvItemName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvItemName.setPadding(5, 5, 5, 0);
        tvItemName.setTextSize(12);
        tr.addView(tvItemName);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView vPosName = new TextView(context);
        vPosName.setText("POS Name");
        vPosName.setTextColor(Color.BLACK);
        vPosName.setPadding(5, 5, 5, 0);
        vPosName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        vPosName.setTextSize(12);
        tr.addView(vPosName); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvBillDate= new TextView(context);
        tvBillDate.setText("Bill Date");
        tvBillDate.setTextColor(Color.BLACK);
        tvBillDate.setPadding(5, 5, 5, 0);
        tvBillDate.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvBillDate.setTextSize(12);
        tr.addView(tvBillDate); // Adding textView to tablerow.



        /** Creating another textview **/
        TextView tvQty = new TextView(context);
        tvQty.setText("Quantity");
        tvQty.setGravity(Gravity.RIGHT);
        tvQty.setTextColor(Color.BLACK);
        tvQty.setPadding(5, 5, 5, 0);
        tvQty.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvQty.setTextSize(12);
        tr.addView(tvQty); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvSaleAmt = new TextView(context);
        tvSaleAmt.setText("Sales Amt");
        tvSaleAmt.setGravity(Gravity.RIGHT);
        tvSaleAmt.setTextColor(Color.BLACK);
        tvSaleAmt.setPadding(5, 5, 5, 0);
        tvSaleAmt.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSaleAmt.setTextSize(12);
        tr.addView(tvSaleAmt); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvSubTotal= new TextView(context);
        tvSubTotal.setText("Sub Total");
        tvSubTotal.setGravity(Gravity.RIGHT);
        tvSubTotal.setTextColor(Color.BLACK);
        tvSubTotal.setPadding(5, 5, 5, 0);
        tvSubTotal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSubTotal.setTextSize(12);
        tr.addView(tvSubTotal); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvDis= new TextView(context);
        tvDis.setText("Discount");
        tvDis.setGravity(Gravity.RIGHT);
        tvDis.setTextColor(Color.BLACK);
        tvDis.setPadding(5, 5, 5, 0);
        tvDis.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvDis.setTextSize(12);
        tr.addView(tvDis); // Adding textView to tablerow.




        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));




        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        /** Creating another textview **/
        TextView divider = new TextView(context);
        divider.setText("___________________________________");
        divider.setTextColor(Color.BLACK);
        //    divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider.setPadding(5, 0, 0, 0);
        divider.setTextSize(12);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(context);
        divider2.setText("_______________");
        divider2.setTextColor(Color.BLACK);
        //    divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider2.setPadding(5, 0, 0, 0);
        divider2.setTextSize(12);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.

        TextView divider3 = new TextView(context);
        divider3.setText("_______________");
        divider3.setTextColor(Color.BLACK);
        //    divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider3.setPadding(5, 0, 0, 0);
        divider3.setTextSize(12);
        divider3.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider3); // Adding textView to tablerow.



        TextView [] myTextViews = new TextView[4]; // create an empty array;
        for (int j = 0; j < 4; j++) {
            // create a new textview
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText("____________");
            rowTextView.setTextColor(Color.BLACK);
            rowTextView.setGravity(Gravity.RIGHT);
            //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            rowTextView.setPadding(5, 0, 0, 0);
            rowTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            // add the textview to the linearlayout
            rowTextView.setTextSize(12);
            tr.addView(rowTextView);
            // save a reference to the textview for later
            myTextViews[j] = rowTextView;
        }

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }


    /**
     * This function add the data to the table
     **/
    public void addItemWiseData()
    {
        for(int k=0;k<arrListItem.size();k++)
        {
            clsSalesReportDtl objSale = (clsSalesReportDtl) arrListItem.get(k);



            /** Create a TableRow dynamically **/
            tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            TextView tvItemName = new TextView(context);
            tvItemName.setText(objSale.getStrItemName());
            tvItemName.setTextColor(Color.BLACK);
            tvItemName.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvItemName.setPadding(5, 5, 5, 0);
            tvItemName.setTextSize(12);
            tr.addView(tvItemName);  // Adding textView to tablerow.

            /** Creating another textview **/
            TextView vPosName = new TextView(context);
            vPosName.setText(objSale.getPosName());
            vPosName.setTextColor(Color.BLACK);
            vPosName.setPadding(5, 5, 5, 0);
            vPosName.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            vPosName.setTextSize(12);
            tr.addView(vPosName); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvBillDate= new TextView(context);
            tvBillDate.setText(objSale.getDtebilldate());
            tvBillDate.setTextColor(Color.BLACK);
            tvBillDate.setPadding(5, 5, 5, 0);
            tvBillDate.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvBillDate.setTextSize(12);
            tr.addView(tvBillDate); // Adding textView to tablerow.



            /** Creating another textview **/
            TextView tvQty = new TextView(context);
            tvQty.setText(objSale.getStrQuantity());
            tvQty.setGravity(Gravity.RIGHT);
            tvQty.setTextColor(Color.BLACK);
            tvQty.setPadding(5, 5, 5, 0);
            tvQty.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvQty.setTextSize(12);
            tr.addView(tvQty); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvSaleAmt = new TextView(context);
            tvSaleAmt.setText(objSale.getGrandTotal());
            tvSaleAmt.setGravity(Gravity.RIGHT);
            tvSaleAmt.setTextColor(Color.BLACK);
            tvSaleAmt.setPadding(5, 5, 5, 0);
            tvSaleAmt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSaleAmt.setTextSize(12);
            tr.addView(tvSaleAmt); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvSubTotal= new TextView(context);
            tvSubTotal.setText(objSale.getSubTotal());
            tvSubTotal.setGravity(Gravity.RIGHT);
            tvSubTotal.setTextColor(Color.BLACK);
            tvSubTotal.setPadding(5, 5, 5, 0);
            tvSubTotal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSubTotal.setTextSize(12);
            tr.addView(tvSubTotal); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvDis= new TextView(context);
            tvDis.setText(objSale.getDiscountAmt());
            tvDis.setGravity(Gravity.RIGHT);
            tvDis.setTextColor(Color.BLACK);
            tvDis.setPadding(5, 5, 5, 0);
            tvDis.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvDis.setTextSize(12);
            tr.addView(tvDis); // Adding textView to tablerow.



            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


        }

    }




    public void addMenuWiseHeaders()
    {

        /** Create a TableRow dynamically **/
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView tvMenuName= new TextView(context);
        tvMenuName.setText("Menu Name");
        tvMenuName.setTextColor(Color.BLACK);
        tvMenuName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvMenuName.setPadding(5, 5, 5, 0);
        tvMenuName.setTextSize(12);
        tr.addView(tvMenuName);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvPOSName = new TextView(context);
        tvPOSName.setText("POS Name");
        tvPOSName.setTextColor(Color.BLACK);
        tvPOSName.setPadding(5, 5, 5, 0);
        tvPOSName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvPOSName.setTextSize(12);
        tr.addView(tvPOSName); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvQty = new TextView(context);
        tvQty.setText("Quantity");
        tvQty.setGravity(Gravity.RIGHT);
        tvQty.setTextColor(Color.BLACK);
        tvQty.setPadding(5, 5, 5, 0);
        tvQty.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvQty.setTextSize(12);
        tr.addView(tvQty); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvSaleAmt = new TextView(context);
        tvSaleAmt.setText("Sale Amt");
        tvSaleAmt.setGravity(Gravity.RIGHT);
        tvSaleAmt.setTextColor(Color.BLACK);
        tvSaleAmt.setPadding(5, 5, 5, 0);
        tvSaleAmt.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSaleAmt.setTextSize(12);
        tr.addView(tvSaleAmt); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvSubTotal = new TextView(context);
        tvSubTotal.setText("Sub Total");
        tvSubTotal.setGravity(Gravity.RIGHT);
        tvSubTotal.setTextColor(Color.BLACK);
        tvSubTotal.setPadding(5, 5, 5, 0);
        tvSubTotal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSubTotal.setTextSize(12);
        tr.addView(tvSubTotal); // Adding textView to tablerow.

        /** Creating another textview **/
        TextView tvDiscount = new TextView(context);
        tvDiscount.setText("Discount");
        tvDiscount.setGravity(Gravity.RIGHT);
        tvDiscount.setTextColor(Color.BLACK);
        tvDiscount.setPadding(5, 5, 5, 0);
        tvDiscount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvDiscount.setTextSize(12);
        tr.addView(tvDiscount); // Adding textView to tablerow.


        /** Creating another textview **/
        TextView tvSalePer = new TextView(context);
        tvSalePer.setText("Sale(%)");
        tvSalePer.setGravity(Gravity.RIGHT);
        tvSalePer.setTextColor(Color.BLACK);
        tvSalePer.setPadding(5, 5, 5, 0);
        tvSalePer.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvSalePer.setTextSize(12);
        tr.addView(tvSalePer); // Adding textView to tablerow.




        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));




        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(context);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));



        /** Creating another textview **/
        TextView divider = new TextView(context);
        divider.setText("___________________________");
        divider.setTextColor(Color.BLACK);
        //    divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider.setPadding(5, 0, 0, 0);
        divider.setTextSize(12);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.


        TextView [] myTextViews = new TextView[6]; // create an empty array;
        for (int j = 0; j < 6; j++) {
            // create a new textview
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText("_____________");
            rowTextView.setTextColor(Color.BLACK);
            rowTextView.setGravity(Gravity.RIGHT);
            //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            rowTextView.setPadding(5, 0, 0, 0);
            rowTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            // add the textview to the linearlayout
            rowTextView.setTextSize(12);
            tr.addView(rowTextView);
            // save a reference to the textview for later
            myTextViews[j] = rowTextView;
        }

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


    }



    /**
     * This function add the data to the table
     **/
    public void addMenuWiseData()
    {
        for(int k=0;k<arrListItem.size();k++)
        {
            clsSalesReportDtl objSale = (clsSalesReportDtl) arrListItem.get(k);



            /** Create a TableRow dynamically **/
            tr = new TableRow(context);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            /** Creating a TextView to add to the row **/
            TextView tvMenuName= new TextView(context);
            tvMenuName.setText(objSale.getStrMenuName());
            tvMenuName.setTextColor(Color.BLACK);
            tvMenuName.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvMenuName.setPadding(5, 5, 5, 0);
            tvMenuName.setTextSize(12);
            tr.addView(tvMenuName);  // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvPOSName = new TextView(context);
            tvPOSName.setText(objSale.getPosName());
            tvPOSName.setTextColor(Color.BLACK);
            tvPOSName.setPadding(5, 5, 5, 0);
            tvPOSName.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvPOSName.setTextSize(12);
            tr.addView(tvPOSName); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvQty = new TextView(context);
            tvQty.setText(objSale.getStrQuantity());
            tvQty.setGravity(Gravity.RIGHT);
            tvQty.setTextColor(Color.BLACK);
            tvQty.setPadding(5, 5, 5, 0);
            tvQty.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvQty.setTextSize(12);
            tr.addView(tvQty); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvSaleAmt = new TextView(context);
            tvSaleAmt.setText(objSale.getGrandTotal());
            tvSaleAmt.setGravity(Gravity.RIGHT);
            tvSaleAmt.setTextColor(Color.BLACK);
            tvSaleAmt.setPadding(5, 5, 5, 0);
            tvSaleAmt.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSaleAmt.setTextSize(12);
            tr.addView(tvSaleAmt); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvSubTotal = new TextView(context);
            tvSubTotal.setText(objSale.getSubTotal());
            tvSubTotal.setGravity(Gravity.RIGHT);
            tvSubTotal.setTextColor(Color.BLACK);
            tvSubTotal.setPadding(5, 5, 5, 0);
            tvSubTotal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSubTotal.setTextSize(12);
            tr.addView(tvSubTotal); // Adding textView to tablerow.

            /** Creating another textview **/
            TextView tvDiscount = new TextView(context);
            tvDiscount.setText(objSale.getDiscountAmt());
            tvDiscount.setGravity(Gravity.RIGHT);
            tvDiscount.setTextColor(Color.BLACK);
            tvDiscount.setPadding(5, 5, 5, 0);
            tvDiscount.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvDiscount.setTextSize(12);
            tr.addView(tvDiscount); // Adding textView to tablerow.


            /** Creating another textview **/
            TextView tvSalePer = new TextView(context);
            tvSalePer.setText(objSale.getStrSalePer());
            tvSalePer.setGravity(Gravity.RIGHT);
            tvSalePer.setTextColor(Color.BLACK);
            tvSalePer.setPadding(5, 5, 5, 0);
            tvSalePer.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvSalePer.setTextSize(12);
            tr.addView(tvSalePer); // Adding textView to tablerow.



            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));


        }

    }


}