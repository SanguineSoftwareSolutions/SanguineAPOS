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
import com.example.apos.listeners.clsVoidKotTableListener;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;

public class clsMakeBillTableAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList arrListTable;
    private Activity mActivity;
    private clsMakeBillTableListener tableSelectionListener;

    public clsMakeBillTableAdapter(Context c, Activity activity, ArrayList arrListTable, clsMakeBillTableListener mTableSelectionListener) {
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

        final SquareTextView btnTable = (SquareTextView) gridView.findViewById(R.id.btnTable);
        btnTable.setTag(position);
        clsTableMaster objTable = (clsTableMaster) arrListTable.get(position);
        btnTable.setText(objTable.getStrTableName());
        btnTable.setSelected(false);
        btnTable.setActivated(true);


        btnTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int pos = (Integer) v.getTag();
            clsTableMaster objTable = (clsTableMaster) arrListTable.get(pos);
            tableSelectionListener.funGetSelectedTable(objTable.getStrTableNo(),objTable.getStrTableName());
            }
        });
        return gridView;
    }
}
