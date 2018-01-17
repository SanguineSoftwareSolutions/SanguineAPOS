package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsSettlementOption;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.bean.clsTaxCalculationDtls;
import com.example.apos.listeners.clsKotTableListSelectionListener;

import java.util.ArrayList;
import java.util.List;


public class clsSetSettleModeAdapter extends  ArrayAdapter<String> {

    private Context context;
    ArrayList<clsSettlementOption> listSettlestData;
    private static LayoutInflater inflater=null;


    public clsSetSettleModeAdapter(Context context, ArrayList dataItem) {
        super(context, R.layout.selectedsettlelist, dataItem);
        this.listSettlestData = dataItem;
        this.context = context;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.selectedsettlelist, null);
            viewHolder = new ViewHolder();

            viewHolder.txtSettleType= (TextView) convertView.findViewById(R.id.tvSettleType);
            viewHolder.txtSettleAmt = (TextView) convertView.findViewById(R.id.tvSettleAmt);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtSettleType.setText(getItem(position).split("#")[0]);
        viewHolder.txtSettleAmt.setText(getItem(position).split("#")[1]);

        return convertView;
    }

    public class ViewHolder {

        TextView txtSettleType,txtSettleAmt;
    }
}