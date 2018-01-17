package com.example.apos.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.apos.activity.R;

import java.util.ArrayList;

public class clsGuestDialogAdapter extends ArrayAdapter<String>
{
    private Context context;
    private ArrayList<String> arrListItem = new ArrayList<String>();

    public clsGuestDialogAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.registerdebitdialogadapterform, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.registerdebitdialogadapterform, null);
            viewHolder = new ViewHolder();

            viewHolder.txtGuestCode= (TextView) convertView.findViewById(R.id.tv_tv_register_debit_dialog_adapter_cust_code);
            viewHolder.txtGuestName= (TextView) convertView.findViewById(R.id.tv_register_debit_dialog_adapter_cust_name);
            viewHolder.txtRoomNo= (TextView) convertView.findViewById(R.id.tv_register_debit_dialog_adapter_mob_no);
            viewHolder.txtRoomName= (TextView) convertView.findViewById(R.id.tv_register_debit_dialog_adapter_type);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //  final String text=getItem(position).split("#")[0];
        viewHolder.txtGuestCode.setText(getItem(position).split("#")[0]);
        viewHolder.txtGuestName.setText(getItem(position).split("#")[1]);
        viewHolder.txtRoomNo.setText(getItem(position).split("#")[2]);
        viewHolder.txtRoomName.setText(getItem(position).split("#")[3]);

        //   final String text = getItem(position);
        return convertView;
    }

    public class ViewHolder {
        TextView txtGuestCode,txtGuestName,txtRoomNo,txtRoomName;
    }
}