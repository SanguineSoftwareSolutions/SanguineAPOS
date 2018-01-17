package com.example.apos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsReprintDocumentBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class clsReservationListAdapter extends ArrayAdapter<String>
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

    public clsReservationListAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.reservationadapterlist, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.reservationadapterlist, null);
            viewHolder = new ViewHolder();

            viewHolder.llDocList = (LinearLayout) convertView.findViewById(R.id.ll_list_row_reprint_doc);
            viewHolder.tvContactNo=(TextView) convertView.findViewById(R.id.txt_Mobile_No);
            viewHolder.tvCustomerName = (TextView) convertView.findViewById(R.id.txt_Cust_Name);
            viewHolder.tvTableName =(TextView) convertView.findViewById(R.id.txt_table);
            viewHolder.tvPax=(TextView) convertView.findViewById(R.id.txt_Pax);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.txt_Date);
            viewHolder.tvTime =(TextView) convertView.findViewById(R.id.txt_Time);
            viewHolder.tvComments=(TextView) convertView.findViewById(R.id.txt_Comments);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        clsReprintDocumentBean objReservDoc = (clsReprintDocumentBean)arrListItem.get(position);
        final String text = objReservDoc.getStrDocNo();

        viewHolder.tvContactNo.setText(objReservDoc.getStrCardNo());
        viewHolder.tvCustomerName.setText(objReservDoc.getStrCustomerName());
        viewHolder.tvTableName.setText(objReservDoc.getStrMemberCode());
        viewHolder.tvPax.setText(objReservDoc.getStrUser());
        String []spDate=objReservDoc.getStrDate().split("-");
        viewHolder.tvDate.setText(spDate[2]+"-"+spDate[1]+"-"+spDate[0]);

        String time[]=objReservDoc.getStrTime().split(":");
        viewHolder.tvTime.setText(time[0]+":"+time[1]);
        viewHolder.tvComments.setText(objReservDoc.getStrDocSlipNo());

        viewHolder.llDocList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.llDocList.setBackgroundColor(Color.LTGRAY);
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
        TextView tvContactNo;
        TextView tvCustomerName;
        TextView tvTableName;
        TextView tvPax;
        TextView tvDate;
        TextView tvTime;
        TextView tvComments;
        LinearLayout llDocList;

    }
}


