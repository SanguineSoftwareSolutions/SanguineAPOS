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
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.bean.clsSalesReportDtl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public  class clsPoswiseSalesMenuAdapter extends BaseAdapter {
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
    private ArrayList<String> arrPosName;
    private String reportType="";
    private TreeMap<String,clsSalesReportDtl> mapChartData=new TreeMap<String,clsSalesReportDtl>();

    TableLayout tl;
    TableRow tr;

    public clsPoswiseSalesMenuAdapter(Context context, List<clsSalesReportDtl> arrListPOSWise, ArrayList<String> arrPosNameList,String reportType) {
        this.arrListItem = arrListPOSWise;
        this.context = context;
        this.arrPosName = arrPosNameList;
        this.reportType=reportType;
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
            convertView = mInflater.inflate(R.layout.poswisesalesmenulist, null);
            viewHolder = new ViewHolder();
            tl = (TableLayout) convertView.findViewById(R.id.table_main);
            addHeaders();
            addData();
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

    public void addHeaders() {

        /** Create a TableRow dynamically **/
        tr = new TableRow(context);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        TextView companyTV = new TextView(context);
        if(reportType.equals("ItemWise"))
        {
            companyTV.setText("ItemCode");
        }
        else if(reportType.equals("MenuWise"))
        {
            companyTV.setText("MenuCode");
        }
        else if(reportType.equals("GroupWise"))
        {
            companyTV.setText("GroupCode");
        }
        else if(reportType.equals("SubGroupWise"))
        {
            companyTV.setText("SubGroupCode");
        }
        companyTV.setTextColor(Color.BLACK);
        companyTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //companyTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        companyTV.setPadding(5, 5, 5, 0);
        companyTV.setTextSize(12);
        tr.addView(companyTV);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView valueTV = new TextView(context);
        if(reportType.equals("ItemWise"))
        {
            valueTV.setText("ItemName");
        }
        else if(reportType.equals("MenuWise"))
        {
            valueTV.setText("MenuName");
        }
        else if(reportType.equals("GroupWise"))
        {
            valueTV.setText("GroupName");
        }
        else if(reportType.equals("SubGroupWise"))
        {
            valueTV.setText("SubGroupName");
        }
        valueTV.setTextColor(Color.BLACK);
        //   valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        valueTV.setPadding(5, 5, 5, 0);
        valueTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        valueTV.setTextSize(12);
        tr.addView(valueTV); // Adding textView to tablerow.

        TextView[] myTextViews = new TextView[arrPosName.size()]; // create an empty array;
        for (int j = 0; j < arrPosName.size(); j++) {
            String posName = arrPosName.get(j);
            // create a new textview
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText(posName);
            rowTextView.setTextColor(Color.BLACK);
            rowTextView.setGravity(Gravity.RIGHT);
            //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            rowTextView.setPadding(2, 2, 2, 0);
            rowTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            // add the textview to the linearlayout
            tr.addView(rowTextView);
            rowTextView.setTextSize(12);
            // save a reference to the textview for later
            myTextViews[j] = rowTextView;
        }

        // Add the TableRow to the TableLayout
        tl.addView(tr, new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the divider because we have two columns
        tr = new TableRow(context);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/
        TextView divider = new TextView(context);
        divider.setText("-----------------");
        divider.setTextColor(Color.BLACK);
        //    divider.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider.setPadding(5, 0, 0, 0);
        divider.setTextSize(12);
        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider); // Adding textView to tablerow.

        TextView divider2 = new TextView(context);
        divider2.setText("-------------------------");
        divider2.setTextColor(Color.BLACK);
        //    divider2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        divider2.setPadding(5, 0, 0, 0);
        divider2.setTextSize(12);
        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(divider2); // Adding textView to tablerow.

        myTextViews = new TextView[arrPosName.size()]; // create an empty array;
        for (int j = 0; j < arrPosName.size(); j++) {
            String posName = arrPosName.get(j);
            // create a new textview
            final TextView rowTextView = new TextView(context);
            // set some properties of rowTextView or something
            rowTextView.setText("-----------------");
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
        tl.addView(tr, new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    /**
     * This function add the data to the table
     **/
    public void addData()
    {
        for(int k=0;k<arrListItem.size();k++)
        {
            clsSalesReportDtl objSale = (clsSalesReportDtl) arrListItem.get(k);
            if(mapChartData.size()>0)
            {
                if(mapChartData.containsKey(objSale.getStrItemCode()))
                {
                    clsSalesReportDtl objSaleDtl = mapChartData.get(objSale.getStrItemCode());
                    List<clsPosSelectionMaster> arrPOSList = new ArrayList();
                    for (int cnt = 0; cnt < objSaleDtl.getArrListPosDtl().size(); cnt++)
                    {
                        clsPosSelectionMaster objPOS = objSaleDtl.getArrListPosDtl().get(cnt);
                        for (int cnt1 = 0; cnt1 < objSale.getArrListPosDtl().size(); cnt1++)
                        {
                            clsPosSelectionMaster objPOS1 = objSale.getArrListPosDtl().get(cnt1);
                            if(objPOS1.getStrPosName().equals(objPOS.getStrPosName()))
                            {
                                double amt=objPOS1.getDblAmount()+objPOS.getDblAmount();
                                objPOS.setDblAmount(amt);
                            }
                        }
                    }
                    mapChartData.put(objSale.getStrItemCode(),objSaleDtl);

                }
                else
                {
                    mapChartData.put(objSale.getStrItemCode(),objSale);
                }

            }
            else
            {
                mapChartData.put(objSale.getStrItemCode(),objSale);

            }

        }

        if(mapChartData.size()>0)
        {
            for (Map.Entry<String, clsSalesReportDtl> entry : mapChartData.entrySet())
            {
                clsSalesReportDtl objSale = entry.getValue();
                tr = new TableRow(context);
                tr.setLayoutParams(new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));


                /** Creating a TextView to add to the row **/
                TextView itemCodeTV = new TextView(context);
                itemCodeTV.setText(objSale.getStrItemCode());
                itemCodeTV.setTextColor(Color.BLACK);
                itemCodeTV.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                //   companyTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                itemCodeTV.setPadding(5, 5, 5, 0);
                itemCodeTV.setTextSize(12);
                tr.addView(itemCodeTV);  // Adding textView to tablerow.

                /** Creating another textview **/
                TextView itemNameTV = new TextView(context);
                itemNameTV.setText(objSale.getStrItemName());
                itemNameTV.setTextColor(Color.BLACK);
                //   valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                itemNameTV.setPadding(5, 5, 5, 0);
                itemNameTV.setTextSize(12);
                itemNameTV.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                tr.addView(itemNameTV); // Adding textView to tablerow.
                double totalAmt = 0;
                TextView[] myTextViews = new TextView[arrPosName.size()]; // create an empty array;
                for (int i = 0; i < objSale.getArrListPosDtl().size(); i++)
                {
                    clsPosSelectionMaster objPOS = objSale.getArrListPosDtl().get(i);
                    // create a new textview
                    final TextView rowTextView = new TextView(context);
                    // set some properties of rowTextView or something
                    rowTextView.setText(String.valueOf(objPOS.getDblAmount()));
                    rowTextView.setTextColor(Color.BLACK);
                    rowTextView.setGravity(Gravity.RIGHT);
                    //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                    rowTextView.setPadding(2, 2, 2, 0);
                    rowTextView.setTextSize(12);
                    rowTextView.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    // add the textview to the linearlayout
                    tr.addView(rowTextView);
                    // save a reference to the textview for later
                    myTextViews[i] = rowTextView;
                    totalAmt += objPOS.getDblAmount();


                }

                /** Creating another textview **/
                TextView totalTV = new TextView(context);
                totalTV.setText(String.valueOf(totalAmt));
                totalTV.setTextColor(Color.BLACK);
                totalTV.setGravity(Gravity.RIGHT);
                //   valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                totalTV.setPadding(2, 2, 2, 0);
                totalTV.setTextSize(12);
                totalTV.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                tr.addView(totalTV); // Adding textView to tablerow.
                // Add the TableRow to the TableLayout
                tl.addView(tr, new LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
        }


        tr = new TableRow(context);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        tl.addView(tr, new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));


        tr = new TableRow(context);
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));


        /** Creating a TextView to add to the row **/
        TextView totalTV = new TextView(context);
        totalTV.setText("Total:");
        totalTV.setTextColor(Color.BLACK);
        totalTV.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        //   companyTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        totalTV.setPadding(5, 5, 5, 0);
        totalTV.setTextSize(14);
        tr.addView(totalTV);  // Adding textView to tablerow.

        /** Creating another textview **/
        TextView totalTV1 = new TextView(context);
        totalTV1.setText("");
        totalTV1.setTextColor(Color.BLACK);
        //   valueTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        totalTV1.setPadding(5, 5, 5, 0);
        totalTV1.setTextSize(14);
        totalTV1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tr.addView(totalTV1); // Adding textView to tablerow.

        TreeMap<Integer,Double> mapItems=new TreeMap<>();
        int total = 0;


        for (int i = 2; i < tl.getChildCount(); i++)
        {
            double colTotalSales = 0.0;
            View parentRow = tl.getChildAt(i);
            if (parentRow instanceof TableRow)
            {
                for (int j = 2; j < ((TableRow) parentRow).getChildCount(); j++)
                {
                    TextView txtView = (TextView) ((TableRow) parentRow).getChildAt(j);
                    if (txtView instanceof TextView)
                    {
                        String text = txtView.getText().toString();
                        if(mapItems.size()>0)
                        {
                            if(mapItems.containsKey(j))
                            {
                                double totalValue=mapItems.get(j);
                                totalValue+=Double.parseDouble(text);
                                mapItems.put(j,totalValue);
                            }
                            else
                            {
                                mapItems.put(j,Double.parseDouble(text));
                            }
                        }
                        else
                        {
                            mapItems.put(j,Double.parseDouble(text));
                        }
                    }

                 }
            }

        }


        System.out.println("mapItems:"+mapItems);
        TextView[] myTextViews = new TextView[arrPosName.size()]; // create an empty array;

        if(mapItems.size()>0)
        {

            for (Map.Entry<Integer, Double> entry : mapItems.entrySet())
            {
                Integer key = entry.getKey();
                final TextView rowTextView = new TextView(context);
                // set some properties of rowTextView or something
                rowTextView.setText(String.valueOf(entry.getValue()));
                rowTextView.setTextColor(Color.BLACK);
                rowTextView.setGravity(Gravity.RIGHT);
                //  rowTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
                rowTextView.setPadding(2, 2, 2, 0);
                rowTextView.setTextSize(14);
                rowTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                // add the textview to the linearlayout
                tr.addView(rowTextView);
                // save a reference to the textview for later
                myTextViews[key-2] = rowTextView;
                // ...
            }
        }

        // Add the TableRow to the TableLayout
        tl.addView(tr, new LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));


    }



}