package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsMakeBillItemDtls;
import com.example.apos.listeners.clsMakeBillItemListListener;
import com.example.apos.listeners.clsVoidKotItemListListener;

import java.util.List;

public class clsMakeBillItemListAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsVoidKotListAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsMakeBillItemDtls> listItemsListData;
    private clsMakeBillItemListListener makeBillListSelectionListener;


    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    public clsMakeBillItemListAdapter (Context context, Activity activity, List<clsMakeBillItemDtls> itemsListData, clsMakeBillItemListListener makeBilltListSelectionListener) {
        this.context = context;
        this.mActivity = activity;
        this.listItemsListData = itemsListData;
        Log.i(TAG, "Size of the item lists in clsDirectBillItemsListGridViewAdapter : " + listItemsListData.size());
        this.makeBillListSelectionListener = makeBilltListSelectionListener;

    }



    @Override
    public int getCount() {
        return listItemsListData.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemsListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listItemsListData.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        View rowView;
        LayoutInflater inflater = LayoutInflater.from(context);
        rowView = inflater.inflate(R.layout.makebillselecteditemlist, null);
        holder.tvItemName =(TextView) rowView.findViewById(R.id.tvMakeBillListName);
        holder.tvQty = (TextView) rowView.findViewById(R.id.tvMakeBillListQty);
        holder.tvAmount =(TextView) rowView.findViewById(R.id.tvMakeBillListAmt);
        holder.llKotList=(LinearLayout) rowView.findViewById(R.id.ll_list_row_make_bill);

        final clsMakeBillItemDtls listItemBean = (clsMakeBillItemDtls) getItem(position);
        final String code=listItemBean.getStrItemCode();
        final String text=listItemBean.getStrItemName();
        if(listItemBean.getStrItemName() != null){
            holder.tvItemName.setText(listItemBean.getStrItemName());
        }
        if(listItemBean.getDblQuantity() != 0){
            holder.tvQty.setText(Double.toString(listItemBean.getDblQuantity()));
        }
        holder.tvQty.setId(position);

        if(listItemBean.getDblAmount() != -1){
            holder.tvAmount.setText(Double.toString(listItemBean.getDblAmount()));
        }
        return rowView;
    }

    private class ViewHolder{

        TextView tvItemName;
        TextView tvQty;
        TextView tvAmount;
        LinearLayout llKotList;
    }
}