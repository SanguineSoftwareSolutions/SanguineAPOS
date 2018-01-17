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
import com.example.apos.bean.clsVoidKotListDtl;
import com.example.apos.listeners.clsVoidKotItemListListener;
import com.example.apos.listeners.clsVoidKotListSelectionListener;

import java.util.List;



public class clsVoidKotItemListAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsVoidKotListAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsKOTItemDtlBean> listItemsListData;
    private clsVoidKotItemListListener kotListSelectionListener;


    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener)
    {
        this.customListner = listener;
    }

    public clsVoidKotItemListAdapter (Context context, Activity activity, List<clsKOTItemDtlBean> itemsListData, clsVoidKotItemListListener mKotListSelectionListener) {
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
        rowView = inflater.inflate(R.layout.voidkotselecteditemlist, null);
        holder.tvItemName =(TextView) rowView.findViewById(R.id.tv_list_row_void_kot_desc);
        holder.tvQty = (TextView) rowView.findViewById(R.id.txt_list_row_void_kot_qty);
        holder.tvAmount =(TextView) rowView.findViewById(R.id.tv_list_row_void_kot_amount);
        holder.tvDelete = (Button) rowView.findViewById(R.id.btn_list_row_void_kot_delete);
        holder.btnReduceQty=(Button) rowView.findViewById(R.id.btnReduceQty);
        holder.llKotList=(LinearLayout) rowView.findViewById(R.id.ll_list_row_void_kot);


        final clsKOTItemDtlBean listItemBean = (clsKOTItemDtlBean) getItem(position);
        final String code=listItemBean.getStrItemCode();
        final String text=listItemBean.getStrItemName();
        if(listItemBean.getStrItemName() != null){
            holder.tvItemName.setText(listItemBean.getStrItemName());
        }
        if(listItemBean.getDblItemQuantity() != 0){
            holder.tvQty.setText(Double.toString(listItemBean.getDblItemQuantity()));
        }
        holder.tvQty.setId(position);

        if(listItemBean.getDblAmount() != -1){
            holder.tvAmount.setText(Double.toString(listItemBean.getDblAmount()));
        }

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onButtonClickListner(position, text + "#deleterow"+ "#"+code);
                }

            }
        });


        holder.btnReduceQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onButtonClickListner(position,  text + "#qtyrow"+ "#"+code);
                }

            }
        });


        return rowView;
    }

    private class ViewHolder{

        TextView tvItemName;
        TextView tvQty;
        TextView tvAmount;
        Button tvDelete;
        Button btnReduceQty;
        LinearLayout llKotList;




    }
}