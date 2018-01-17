package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.apos.activity.R;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Monika on 10/11/2017.
 */


public class clsViewTableStatusAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList arrListTable;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder = null;
    private String type="";

    public clsViewTableStatusAdapter(Context c, Activity activity, ArrayList arrListTable,String type) {
        mContext = c;
        this.arrListTable=arrListTable;
        layoutInflater = activity.getLayoutInflater();
        this.type=type;
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
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        if (null == convertView)
        {
            convertView = layoutInflater.inflate(R.layout.grid_item_table, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        clsTableMaster objTable = (clsTableMaster) arrListTable.get(position);
        //viewHolder.btnTable.setText(objTable.getStrTableName());
        viewHolder.btnTable.setTextSize(9);
        viewHolder.btnTable.setText(Html.fromHtml( objTable.getStrTableName()+
                "<br>"+objTable.getIntPaxNo()+"<br/>"
                +"<br>"+objTable.getStrTime()+"<br/>"));

        if(type.equals("All"))
        {
            boolean isOccupied = objTable.getStrTableStatus().equalsIgnoreCase("Occupied");
            boolean isBilled = objTable.getStrTableStatus().equalsIgnoreCase("Billed");
            boolean isReversed = objTable.getStrTableStatus().equalsIgnoreCase("Reserve");
            viewHolder.btnTable.setSelected(false);
            viewHolder.btnTable.setActivated(false);

            if (isOccupied) {

                viewHolder.btnTable.setActivated(true);
            }

            else if (isBilled) {
                viewHolder.btnTable.setSelected(true);
                viewHolder.btnTable.setActivated(true);
            }

            else if (isReversed) {
                viewHolder.btnTable.setSelected(true);
            }
            else
            {
                viewHolder.btnTable.setText(Html.fromHtml( objTable.getStrTableName()+
                        "<br>"+objTable.getIntPaxNo()+"<br/>"));
            }

        }
        else if(type.equals("Billed"))
        {
            viewHolder.btnTable.setSelected(true);
            viewHolder.btnTable.setActivated(true);
        }
        else if(type.equals("Occupied"))
        {
            viewHolder.btnTable.setActivated(true);
        }



        return convertView;
    }


    private class ViewHolder {
        SquareTextView btnTable;

        public ViewHolder(View v) {
            btnTable = ButterKnife.findById(v, R.id.btnTable);

        }
    }


}