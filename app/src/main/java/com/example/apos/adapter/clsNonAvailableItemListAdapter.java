package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.listeners.clsKotItemListSelectionListener;

import java.util.List;

/**
 * Created by User on 11-05-2017.
 */


public class clsNonAvailableItemListAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsNonAvailableItemListAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsKotItemsListBean> listItemsListData;
    //private static LayoutInflater inflater=null;
    private clsKotItemListSelectionListener itemListSelectionListener;
    private String itemType="";

    public clsNonAvailableItemListAdapter (Context context, Activity activity, List<clsKotItemsListBean> itemsListData, clsKotItemListSelectionListener mItemListSelectionListener, String itemType)
    {
        this.context = context;
        this.mActivity = activity;
        this.listItemsListData = itemsListData;
        this.itemListSelectionListener = mItemListSelectionListener;
        this.itemType = itemType;

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
        View rowView=null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(itemType.equals("NonAvailable"))
        {
            rowView = inflater.inflate(R.layout.tablegridviewmembers, null);
            holder.btnItemListName =(Button) rowView.findViewById(R.id.btnTable);


            final clsKotItemsListBean itemListBean = (clsKotItemsListBean) getItem(position);

            if(itemListBean.getStrItemName() != null){
                holder.btnItemListName.setText(itemListBean.getStrItemName());
            }


            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    clsKotItemsListBean itemListBeanTemp = (clsKotItemsListBean) getItem(position);
                    itemListSelectionListener.getItemsListSelectedForOder(itemListBeanTemp.getStrItemCode(), itemListBeanTemp.getStrItemName(),
                            itemListBeanTemp.getStrSubGroupCode(), itemListBeanTemp.getDblSalePrice());
                }
            });
        }
        else
        {
            rowView = inflater.inflate(R.layout.nonavailableitemlistgridrow, null);
            holder.txtItemListName =(TextView) rowView.findViewById(R.id.tv_kot_items_list_name);


            final clsKotItemsListBean itemListBean = (clsKotItemsListBean) getItem(position);

            if(itemListBean.getStrItemName() != null){
                holder.txtItemListName.setText(itemListBean.getStrItemName());
            }


            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    clsKotItemsListBean itemListBeanTemp = (clsKotItemsListBean) getItem(position);
                    itemListSelectionListener.getItemsListSelectedForOder(itemListBeanTemp.getStrItemCode(), itemListBeanTemp.getStrItemName(),
                            itemListBeanTemp.getStrSubGroupCode(), itemListBeanTemp.getDblSalePrice());
                }
            });
        }


        return rowView;
    }

    private class ViewHolder{
        Button btnItemListName;
        TextView txtItemListName;
    }
}