package com.example.apos.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.apos.activity.R;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.listeners.clsMakeBillTableListener;

import java.util.ArrayList;

public class clsMakeBillTableRefreshAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList arrListTable;
    private Activity mActivity;
    private static LayoutInflater inflater = null;
    private clsMakeBillTableListener tableSelectionListener;


    public clsMakeBillTableRefreshAdapter(Context c, Activity activity,ArrayList arrListTable, clsMakeBillTableListener mTableSelectionListener) {
       mContext = c;
       mActivity = activity;
        this.arrListTable = arrListTable;
        this.tableSelectionListener = mTableSelectionListener;
    }

    public int getCount() {
        // return images.length;
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

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        gridView = new View(mContext);
        gridView = inflater.inflate(R.layout.tablegridviewmembers, null);

        final Button btnTable = (Button) gridView.findViewById(R.id.btnTable);
        btnTable.setTag(position);
        clsTableMaster objTable = (clsTableMaster) arrListTable.get(position);
        btnTable.setText(objTable.getStrTableName());

        return gridView;
    }


}
