package com.example.apos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.apos.activity.R;


import java.util.ArrayList;

public class clsCounterAdapter extends ArrayAdapter<String>
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener)
    {
        this.customListner = listener;
    }
    private Context context;
    private ArrayList<String> arrListDialogItem = new ArrayList<String>();





    public clsCounterAdapter(Context context, ArrayList Item)
    {
        super(context, R.layout.countertextview, Item);
        this.arrListDialogItem = Item;
        this.context = context;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.countertextview, null);
            viewHolder = new ViewHolder();

            viewHolder.btnItem = (TextView) convertView.findViewById(R.id.btndlg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String text = getItem(position);
        viewHolder.btnItem.setText(text);



        viewHolder.btnItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null)
                {

                    customListner.onButtonClickListner(position, text + "#item");
                }
            }
        });


        return convertView;
    }

    public class ViewHolder {

        TextView btnItem;

    }





}
