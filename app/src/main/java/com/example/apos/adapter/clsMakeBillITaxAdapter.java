package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsMakeBillItemDtls;
import com.example.apos.bean.clsTaxCalculationDtls;
import com.example.apos.listeners.clsMakeBillItemListListener;

import java.util.ArrayList;
import java.util.List;

public class clsMakeBillITaxAdapter extends BaseAdapter {

    private Context context;
    private Activity mActivity;
    List<clsTaxCalculationDtls> listTaxListData;



    public clsMakeBillITaxAdapter(Context context, Activity activity, List<clsTaxCalculationDtls> taxListData) {
        this.context = context;
        this.mActivity = activity;
        this.listTaxListData = taxListData;
    }

    @Override
    public int getCount() {
        return listTaxListData.size();
    }

    @Override
    public Object getItem(int position) {
        return listTaxListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listTaxListData.indexOf(getItem(position));
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.makebillselectedtaxlist, null);
                viewHolder = new ViewHolder();

                viewHolder.txtTaxName= (TextView) convertView.findViewById(R.id.tvMakeBillTaxName);
                viewHolder.txtTaxAmt = (TextView) convertView.findViewById(R.id.tvMakeBillTaxAmt);

                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

        clsTaxCalculationDtls objTaxDtl = (clsTaxCalculationDtls)listTaxListData.get(position);

            viewHolder.txtTaxName.setText(objTaxDtl.getTaxName());
            String text= String.valueOf(objTaxDtl.getTaxAmount());
            viewHolder.txtTaxAmt.setText(text);


            return convertView;
        }

        public class ViewHolder {
            TextView txtTaxName,txtTaxAmt;

        }
    }