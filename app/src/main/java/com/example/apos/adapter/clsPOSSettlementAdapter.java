package com.example.apos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.apos.activity.R;
import com.example.apos.bean.clsSettlementDtl;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Monika on 11/2/2017.
 */

public class clsPOSSettlementAdapter extends BaseAdapter {

    customButtonListener customListner;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder = null;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;

    }


    private Activity activity;
    private ArrayList arrListTable;

    public clsPOSSettlementAdapter(Activity c, ArrayList arrListTable) {
        activity = c;
        this.arrListTable = arrListTable;
        layoutInflater = c.getLayoutInflater();
    }

    public int getCount() {
        return arrListTable.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (null == convertView)
        {
            convertView = layoutInflater.inflate(R.layout.grid_item_settlemode, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final clsSettlementDtl objPos = (clsSettlementDtl) arrListTable.get(position);
        viewHolder.btnSettleName.setTag(position);
        viewHolder.btnSettleName.setText(objPos.getStrSettlementName());


        viewHolder.btnSettleName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, objPos.getStrSettlementName());

                }
            }
        });

        return convertView;
    }


    private class ViewHolder {
        SquareTextView btnSettleName;

        public ViewHolder(View v) {
            btnSettleName = ButterKnife.findById(v, R.id.btnSettle);
        }
    }
}