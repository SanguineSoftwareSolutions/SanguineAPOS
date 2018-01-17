package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.activity.clsDirectBill;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;

import java.util.List;


/**
 * Created by BeWo Tech-4 on 06-07-2015.
 */
public  class clsDirectBillSelectedItemsCustomBaseAdapter extends BaseAdapter
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    private Context context;
    List<clsDirectBillSelectedListItemBean> selectedRowItems;
    private ViewHolder holder = null;

    public clsDirectBillSelectedItemsCustomBaseAdapter(Context context, List<clsDirectBillSelectedListItemBean> items) {
        this.context = context;
        this.selectedRowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        LinearLayout llDirectBillList;
        TextView tvDirectBillSelectedItemListDesc;
        TextView etDirectBillSelectedItemListQty;
        TextView tvDirectBillSelectedItemListAmount;
        //Button tvDirectBillSelectedItemListDelete;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.direct_bill_items_selected_list_row, null);
            holder = new ViewHolder();
            holder.llDirectBillList = (LinearLayout) convertView.findViewById(R.id.ll_list_row_direct_bill);
            holder.tvDirectBillSelectedItemListDesc = (TextView) convertView.findViewById(R.id.tv_list_row_direct_bill_desc);
            holder.etDirectBillSelectedItemListQty = (TextView) convertView.findViewById(R.id.txt_list_row_direct_bill_qty);
            holder.tvDirectBillSelectedItemListAmount = (TextView) convertView.findViewById(R.id.tv_list_row_direct_bill_amount);
            //holder.tvDirectBillSelectedItemListDelete = (Button) convertView.findViewById(R.id.tv_list_row_direct_bill_delete);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final clsDirectBillSelectedListItemBean listItemBean = (clsDirectBillSelectedListItemBean) getItem(position);
        final String text=listItemBean.getStrDesc();
        final String code=listItemBean.getStrItemCode();

        if(listItemBean.getStrDesc() != null){
            holder.tvDirectBillSelectedItemListDesc.setText(listItemBean.getStrDesc());
        }
        if(listItemBean.getQty() != 0){
            holder.etDirectBillSelectedItemListQty.setText(Long.toString(listItemBean.getQty()));
        }
        holder.etDirectBillSelectedItemListQty.setId(position);

        if(listItemBean.getAmount() != -1){
            holder.tvDirectBillSelectedItemListAmount.setText(Double.toString(listItemBean.getAmount()));
        }

        holder.llDirectBillList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onButtonClickListner(position, text + "#selectedrow"+ "#"+code);
                }

            }
        });

        return convertView;
    }

    public int getCount() {
        return selectedRowItems.size();
    }

    public Object getItem(int position) {
        return selectedRowItems.get(position);
    }

    public long getItemId(int position) {
        return selectedRowItems.indexOf(getItem(position));
    }
}
