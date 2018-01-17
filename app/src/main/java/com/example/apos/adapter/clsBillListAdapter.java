package com.example.apos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsSalesReportDtl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Prashant on 11/16/2015.
 */
public class clsBillListAdapter extends ArrayAdapter<String>
{
    customButtonListener customListner;

    public interface customButtonListener
    {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener)
    {
        this.customListner = listener;
    }

    private Context context;
    private ArrayList arrListItem = new ArrayList();

    public clsBillListAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.billlistitemforsettle, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.billlistitemforsettle, null);
            viewHolder = new ViewHolder();
            viewHolder.llDirectBillList = (LinearLayout) convertView.findViewById(R.id.billListSelectedRow);
            viewHolder.txtBillNo= (TextView) convertView.findViewById(R.id.txtBillNo);
            viewHolder.txtBillDate = (TextView) convertView.findViewById(R.id.txtBillDate);
            viewHolder.txtTableName = (TextView) convertView.findViewById(R.id.txtTableName);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txtBillAmt);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        clsBillListDtl objSale = (clsBillListDtl)arrListItem.get(position);
        final String text = objSale.getStrBillNo();
        String billDate="";
        try{
            DateFormat df= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date=df.parse(objSale.getDteBillDate());
            billDate=new SimpleDateFormat("dd-MM-yyyy hh:mm").format(date);

        }catch (Exception e){
            e.printStackTrace();
        }
        viewHolder.txtBillNo.setText(objSale.getStrBillNo());
        viewHolder.txtBillDate.setText(billDate);
        viewHolder.txtTableName.setText(objSale.getStrTableName());
        viewHolder.txtAmount.setText(objSale.getStrGrandTotal());

        viewHolder.llDirectBillList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               viewHolder.llDirectBillList.setBackgroundColor(Color.LTGRAY);
                if (customListner != null)
                {
                    customListner.onButtonClickListner(position,text+"#selectedrow");

                }

            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView txtBillNo,txtBillDate,txtTableName,txtAmount;
        LinearLayout llDirectBillList;
    }
}