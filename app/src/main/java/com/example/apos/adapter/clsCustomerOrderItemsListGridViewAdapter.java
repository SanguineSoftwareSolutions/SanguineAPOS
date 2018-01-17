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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.listeners.clsKotItemListSelectionListener;

import java.util.List;

/**
 * Created by User on 28-04-2017.
 */

public class clsCustomerOrderItemsListGridViewAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsKotItemsListGridViewAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsKotItemsListBean> listItemsListData;
    //private static LayoutInflater inflater=null;
    private clsKotItemListSelectionListener itemListSelectionListener;

    public clsCustomerOrderItemsListGridViewAdapter (Context context, Activity activity, List<clsKotItemsListBean> itemsListData, clsKotItemListSelectionListener mItemListSelectionListener) {
        this.context = context;
        this.mActivity = activity;
        this.listItemsListData = itemsListData;
        this.itemListSelectionListener = mItemListSelectionListener;

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
        rowView = inflater.inflate(R.layout.customerorderitemlistforadpater, null);
        holder.ivItemListImage =(ImageView) rowView.findViewById(R.id.iv_grid_row_item_image);
        holder.tvItemListName =(TextView) rowView.findViewById(R.id.tvItemName);
        holder.tvItemListAmount = (TextView) rowView.findViewById(R.id.tvPrice);
//        holder.ivAddToCart.setLayoutParams(new LinearLayout.LayoutParams(85, 85));
//        holder.ivAddToCart.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        holder.ivAddToCart.setPadding(6, 6, 6, 6);


        final clsKotItemsListBean itemListBean = (clsKotItemsListBean) getItem(position);

        if(itemListBean.getStrItemName() != null)
        {
            holder.tvItemListName.setText(itemListBean.getStrItemName());
        }

        if(itemListBean.getDblSalePrice() != -1)
        {
            holder.tvItemListAmount.setText(" Rs. "+itemListBean.getDblSalePrice() );
        }

      //  holder.ivItemListImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
      //  holder.ivItemListImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
      //  holder.ivItemListImage.setPadding(6, 6, 6, 6);

        String imageString=itemListBean.getStrItemImage();
        if(imageString.equals("Image Not Found"))
        {
           // holder.ivItemListImage.setImageResource(R.mipmap.noimageicon);
        }
        else
        {

            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.ivItemListImage.setImageBitmap(decodedByte);
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




        return rowView;
    }

    private class ViewHolder{
        ImageView ivItemListImage;
        TextView tvItemListName;
        TextView tvItemListAmount;
    }
}