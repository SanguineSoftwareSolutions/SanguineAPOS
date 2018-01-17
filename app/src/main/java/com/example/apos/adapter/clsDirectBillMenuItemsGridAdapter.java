package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsDirectBillMenuItemsBean;
import com.example.apos.listeners.clsMenuItemSelectionListener;

import java.util.List;

/**
 * Created by BeWo Tech-4 on 07-07-2015.
 */
public class clsDirectBillMenuItemsGridAdapter extends BaseAdapter {

    private Context context;
    private Activity mActivity;
    List<clsDirectBillMenuItemsBean> listMenuItemsData;
    private clsMenuItemSelectionListener menuItemSelectionListener;

    public clsDirectBillMenuItemsGridAdapter(Context context, Activity activity, List<clsDirectBillMenuItemsBean> menuItemsData, clsMenuItemSelectionListener mMenuItemSelectionListener) {
        this.context = context;
        this.mActivity = activity;
        this.listMenuItemsData = menuItemsData;
        this.menuItemSelectionListener = mMenuItemSelectionListener;

    }

    @Override
    public int getCount() {
        return listMenuItemsData.size();
    }

    @Override
    public Object getItem(int position) {
        return listMenuItemsData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listMenuItemsData.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        View rowView;
        LayoutInflater inflater = LayoutInflater.from(context);
        rowView = inflater.inflate(R.layout.direct_bill_menu_item_grid_row, null);
        holder.tvMenuItemName=(TextView) rowView.findViewById(R.id.tv_grid_row_direct_bill_menu_item);
       // holder.ivMenuItemImage=(ImageView) rowView.findViewById(R.id.iv_grid_row_direct_bill_menu_image);

        clsDirectBillMenuItemsBean menuItemBean = (clsDirectBillMenuItemsBean) getItem(position);

        if(menuItemBean.getStrMenuItemName() != null){
            holder.tvMenuItemName.setText(menuItemBean.getStrMenuItemName());
        }

      rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clsDirectBillMenuItemsBean menuItemBeanTemp = (clsDirectBillMenuItemsBean) getItem(position);
                menuItemSelectionListener.getMenuItemSelectionResult(menuItemBeanTemp.getStrMenuItemCode(), menuItemBeanTemp.getStrMenuItemName(),menuItemBeanTemp.getStrMenuType());
            }
        });

        return rowView;
    }

    private class ViewHolder{
       // ImageView ivMenuItemImage;
        TextView tvMenuItemName;
    }
}
