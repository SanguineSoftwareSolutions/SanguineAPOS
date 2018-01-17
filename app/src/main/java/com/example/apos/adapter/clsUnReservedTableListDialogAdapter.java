package com.example.apos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.apos.activity.R;
import java.util.ArrayList;

public class clsUnReservedTableListDialogAdapter extends ArrayAdapter<String>
{
    private Context context;
    private ArrayList<String> arrListItem = new ArrayList<String>();

    public clsUnReservedTableListDialogAdapter(Context context, ArrayList item)
    {
        super(context, R.layout.unreservedtablesearchadapterform, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.unreservedtablesearchadapterform, null);
            viewHolder = new ViewHolder();

            viewHolder.txtTableNo= (TextView) convertView.findViewById(R.id.strSearchTableNo);
            viewHolder.txtTableName= (TextView) convertView.findViewById(R.id.strSearchTableName);
            viewHolder.txtTableStatus= (TextView) convertView.findViewById(R.id.strSearchTableStatus);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //  final String text=getItem(position).split("#")[0];
        viewHolder.txtTableNo.setText(getItem(position).split("#")[0]);
        viewHolder.txtTableName.setText(getItem(position).split("#")[1]);
        viewHolder.txtTableStatus.setText(getItem(position).split("#")[2]);


        //   final String text = getItem(position);
        return convertView;
    }

    public class ViewHolder {
        TextView txtTableNo,txtTableName,txtTableStatus;
    }
}