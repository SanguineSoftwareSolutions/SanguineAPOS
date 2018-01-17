package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.listeners.clsKotTableListSelectionListener;
import com.example.apos.listeners.clsVoidKotTableListener;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;

public class clsVoidKotTableAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList arrListTable;
    private Activity mActivity;
    private clsVoidKotTableListener tableSelectionListener;


    public clsVoidKotTableAdapter(Context c, Activity activity, ArrayList arrListTable, clsVoidKotTableListener mTableSelectionListener) {
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


        btnTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) v.getTag();
                // Toast.makeText(parent.getContext(), "" + pos, Toast.LENGTH_SHORT).show();
                System.out.println("Position= " + pos);
                clsTableMaster objTable = (clsTableMaster) arrListTable.get(pos);

                    // Table is open for new order.
                    tableSelectionListener.getTableListSelected(objTable.getStrTableNo(),objTable.getStrTableName());


            }
        });

        return gridView;
    }


}
