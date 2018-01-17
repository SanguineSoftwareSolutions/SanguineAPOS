package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.example.apos.activity.R;
import com.example.apos.bean.clsTDHBean;
import com.example.apos.listeners.clsItemSelectionListener;


import java.util.ArrayList;


public class clsButtonAdapter extends BaseAdapter
{
    customItemListener customListner;

    public interface customItemListener {
        public void onItemClickListner(int position,String value);
    }

    public void setCustomItemListner(customItemListener listener) {
        this.customListner = listener;
    }

    private Context mContext;
    private ArrayList arrList;
    private String dataType="";
    private String itemData="";

    public clsButtonAdapter(Context c, Activity activity, ArrayList arrList,String dataType,String itemData) {
        mContext = c;
        this.arrList=arrList;
        this.dataType=dataType;
        this.itemData=itemData;

    }

    public int getCount() {
        return arrList.size();
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
        gridView =new View(mContext);
        gridView = inflater.inflate(R.layout.tablegridviewmembers, null);

        final Button btnTable = (Button) gridView.findViewById(R.id.btnTable);
        btnTable.setTag(position);
        clsTDHBean objBean = (clsTDHBean)arrList.get(position);
        if(dataType.equals("Menu"))
        {
            if(objBean.getIsSelected().equals("Y"))
            {
                btnTable.setBackgroundResource(R.drawable.selectedbuttonxml);
            }

        }

        if(dataType.equals("TDHItems"))
        {
            if(objBean.getIsSelected().equals("Y"))
            {
                btnTable.setBackgroundResource(R.drawable.selectedbuttonxml);
            }

        }


        btnTable.setText(objBean.getStrTDHItemName());


        btnTable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = (Integer) v.getTag();
                clsTDHBean objBean = (clsTDHBean) arrList.get(pos);
                customListner.onItemClickListner(position, objBean.getStrTDHItemCode() + "#"+objBean.getStrTDHItemName()+"#"+dataType+"#"+itemData);
     }
        });

        return gridView;
    }





}