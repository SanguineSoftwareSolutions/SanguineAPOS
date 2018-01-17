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
import com.example.apos.listeners.clsReprintKotItemListSelectionListener;
import com.example.apos.listeners.clsVoidKotItemListListener;

import java.util.List;

public class clsReprintKotItemListAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsVoidKotListAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsKOTItemDtlBean> listItemsListData;
    private clsReprintKotItemListSelectionListener kotListSelectionListener;

    public clsReprintKotItemListAdapter (Context context, Activity activity, List<clsKOTItemDtlBean> itemsListData, clsReprintKotItemListSelectionListener mKotListSelectionListener) {
        this.context = context;
        this.mActivity = activity;
        this.listItemsListData = itemsListData;
        Log.i(TAG, "Size of the item lists in clsDirectBillItemsListGridViewAdapter : " + listItemsListData.size());
        this.kotListSelectionListener = mKotListSelectionListener;

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
        rowView = inflater.inflate(R.layout.reprintkotselecteditemlist, null);
        holder.tvItemName =(TextView) rowView.findViewById(R.id.tv_list_row_reprint_kot_desc);
        holder.tvQty = (TextView) rowView.findViewById(R.id.txt_list_row_reprint_kot_qty);
        holder.tvAmount =(TextView) rowView.findViewById(R.id.tv_list_row_reprint_kot_amount);
        holder.llKotList=(LinearLayout) rowView.findViewById(R.id.ll_list_row_void_kot);


        final clsKOTItemDtlBean listItemBean = (clsKOTItemDtlBean) getItem(position);
        final String code=listItemBean.getStrItemCode();
        final String text=listItemBean.getStrItemName();
        if(listItemBean.getStrItemName() != null){
            holder.tvItemName.setText(listItemBean.getStrItemName());
        }
        if(listItemBean.getDblItemQuantity() != 0){
            holder.tvQty.setText(Double.toString(listItemBean.getDblItemQuantity()));
        }else{
            holder.tvQty.setText("");//for blank lines
        }
        holder.tvQty.setId(position);

        if(listItemBean.getDblAmount() != -1){
            holder.tvAmount.setText(Double.toString(listItemBean.getDblAmount()));
        }else{
            holder.tvAmount.setText("");//for blank lines
        }

        //for blank lines
        if(holder.tvItemName.getText().toString().equals("")){
            holder.tvAmount.setText("");
            holder.tvQty.setText("");
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