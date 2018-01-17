package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import com.example.apos.bean.clsReprintDocumentBean;
import com.example.apos.bean.clsVoidKotListDtl;

import java.util.ArrayList;
import java.util.List;

public class clsReprintDocumentListAdapter extends ArrayAdapter<String>
{
    customButtonListener customListner;

    public interface customButtonListener
    {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener)
    {
        this.customListner = listener;
    }

    private Context context;
    private ArrayList arrListItem = new ArrayList();

    public clsReprintDocumentListAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.reprintdocumentlist, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.reprintdocumentlist, null);
            viewHolder = new ViewHolder();

            viewHolder.llDocList = (LinearLayout) convertView.findViewById(R.id.ll_list_row_reprint_doc);
            viewHolder.tvDocNo =(TextView) convertView.findViewById(R.id.tv_reprint_doc_no);
            viewHolder.tvRefundAmt = (TextView) convertView.findViewById(R.id.tv_reprint_Amount);
            viewHolder.tvRefundSlipNo =(TextView) convertView.findViewById(R.id.tv_reprint_doc_slip_no);
            viewHolder.tvCustomerName=(TextView) convertView.findViewById(R.id.tv_reprint_Customer_Name);



            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        clsReprintDocumentBean objReprintDoc = (clsReprintDocumentBean)arrListItem.get(position);
        final String text = objReprintDoc.getStrDocNo();

        viewHolder.tvDocNo.setText(objReprintDoc.getStrDocNo());
        viewHolder.tvRefundAmt.setText(objReprintDoc.getStrAmount());
        viewHolder.tvRefundSlipNo.setText(objReprintDoc.getStrDocSlipNo());
        viewHolder.tvCustomerName.setText(objReprintDoc.getStrCustomerName());

        viewHolder.llDocList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.llDocList.setBackgroundColor(Color.DKGRAY);
                if (customListner != null)
                {
                    customListner.onButtonClickListner(position,text+"#selectedrow");

                }
                viewHolder.llDocList.setBackgroundColor(Color.WHITE);

            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView tvDocNo;
        TextView tvRefundAmt;
        TextView tvRefundSlipNo;
        TextView tvCustomerName;
        LinearLayout llDocList;

    }
}


