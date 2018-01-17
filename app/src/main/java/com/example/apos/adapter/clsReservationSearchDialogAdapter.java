package com.example.apos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.apos.activity.R;

import java.util.ArrayList;

public class clsReservationSearchDialogAdapter extends ArrayAdapter<String>
{
    private Context context;
    private ArrayList<String> arrListItem = new ArrayList<String>();

    public clsReservationSearchDialogAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.reservationsearchadapterdialogform, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.reservationsearchadapterdialogform, null);
            viewHolder = new ViewHolder();

            viewHolder.txtReservationNo= (TextView) convertView.findViewById(R.id.strSearchReservationNo);
            viewHolder.txtCustomerName= (TextView) convertView.findViewById(R.id.strSearchresCustomerName);
            viewHolder.txtTableName= (TextView) convertView.findViewById(R.id.strSearchResTableName);
            viewHolder.txtReservationDate= (TextView) convertView.findViewById(R.id.strSearchResDate);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //  final String text=getItem(position).split("#")[0];
        viewHolder.txtReservationNo.setText(getItem(position).split("#")[0]);
        viewHolder.txtCustomerName.setText(getItem(position).split("#")[1]);
        viewHolder.txtTableName.setText(getItem(position).split("#")[2]);
        viewHolder.txtReservationDate.setText(getItem(position).split("#")[3]);


        //   final String text = getItem(position);
        return convertView;
    }

    public class ViewHolder {
        TextView txtReservationNo,txtCustomerName,txtTableName,txtReservationDate;
    }
}