package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.listeners.clsKotMenuItemSelectionListener;

import java.util.List;


public class clskotMenuItemsGridAdapter extends BaseAdapter {

    private Context context;
    private Activity mActivity;
    List<clsKotMenuItemsBean> listMenuItemsData;
   // private static LayoutInflater inflater=null;
    private clsKotMenuItemSelectionListener menuItemSelectionListener;

    public clskotMenuItemsGridAdapter(Context context, Activity activity, List<clsKotMenuItemsBean> menuItemsData, clsKotMenuItemSelectionListener mMenuItemSelectionListener) {
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
        rowView = inflater.inflate(R.layout.kot_menu_item_grid_row, null);
        holder.tvMenuItemName=(TextView) rowView.findViewById(R.id.tv_grid_row_kot_menu_item);
        holder.ivMenuItemImage=(ImageView) rowView.findViewById(R.id.iv_grid_row_kot_menu_image);

        clsKotMenuItemsBean menuItemBean = (clsKotMenuItemsBean) getItem(position);

        if(menuItemBean.getStrMenuItemName() != null)
        {
            holder.tvMenuItemName.setText(menuItemBean.getStrMenuItemName());
        }
       /*if(menuItemBean.getbMenuItemImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(menuItemBean.getbMenuItemImage(), 0, menuItemBean.getbMenuItemImage().length);
            holder.ivMenuItemImage.setImageBitmap(bitmap);
        }*/

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                clsKotMenuItemsBean menuItemBeanTemp = (clsKotMenuItemsBean) getItem(position);
                menuItemSelectionListener.getMenuItemSelectionResult(menuItemBeanTemp.getStrMenuItemCode(), menuItemBeanTemp.getStrMenuItemName(),menuItemBeanTemp.getStrMenuType());
            }
        });

        return rowView;
    }

    private class ViewHolder{
        ImageView ivMenuItemImage;
        TextView tvMenuItemName;
    }
}
