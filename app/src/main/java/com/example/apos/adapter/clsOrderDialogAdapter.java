package com.example.apos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsItemModifierBean;

import java.util.ArrayList;

public class clsOrderDialogAdapter extends ArrayAdapter<String>
{
    private Context context;
    private ArrayList<clsItemModifierBean> arrListItem = new ArrayList<clsItemModifierBean>();

    public clsOrderDialogAdapter(Context context, ArrayList item) {
        super(context, R.layout.diamodifiertext, item);
        this.arrListItem = item;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.diamodifiertext, null);
            viewHolder = new ViewHolder();

            viewHolder.txt1 = (TextView) convertView.findViewById(R.id.dlgtext);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String text = getItem(position);
        viewHolder.txt1.setText(text);
        return convertView;
    }

    public class ViewHolder {
        TextView txt1;
    }
}