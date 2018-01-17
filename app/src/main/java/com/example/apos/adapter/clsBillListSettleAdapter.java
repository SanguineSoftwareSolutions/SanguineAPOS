package com.example.apos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsItemDtl;

import java.util.ArrayList;

public class clsBillListSettleAdapter extends ArrayAdapter<String>
{
    customButtonListener customListner;

    public interface customButtonListener
    {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener)
    {
        this.customListner = listener;
    }

    private Context context;
    private ArrayList arrListItem = new ArrayList();

    public clsBillListSettleAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.billsettlelist, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.billsettlelist, null);
            viewHolder = new ViewHolder();

            viewHolder.txtItemName= (TextView) convertView.findViewById(R.id.txtBillItemName);
            viewHolder.txtItemQty = (TextView) convertView.findViewById(R.id.txtBillQty);
            viewHolder.txtItemAmt = (TextView) convertView.findViewById(R.id.txtBillAmt);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        clsItemDtl objBillSettle = (clsItemDtl)arrListItem.get(position);

        viewHolder.txtItemName.setText(objBillSettle.getStrItemName());
        String[] arrQtyData =objBillSettle.getStrQty().split("\\.");
        int Qty = Integer.parseInt(arrQtyData[0]);
        viewHolder.txtItemQty.setText(String.valueOf(Qty));
        viewHolder.txtItemAmt.setText(objBillSettle.getStrAmount());


        return convertView;
    }

    public class ViewHolder {
        TextView txtItemName,txtItemQty,txtItemAmt;

    }
}