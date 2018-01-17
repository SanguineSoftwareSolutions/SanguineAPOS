package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.activity.clsKDSScreenFotKOT;
import com.example.apos.activity.clsKPSScreen;
import com.example.apos.bean.clsBillDtl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monika on 8/28/2017.
 */


public  class clsKPSAdapter extends BaseAdapter
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onKOTClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    private Context context;
    private List<clsBillDtl> arrListItem = new ArrayList();
    Map<String,  List<clsBillDtl>> hmKOTDtl = new HashMap<String,  List<clsBillDtl>>();
    Map<String,  String> hmSelectedKOTDtl = new HashMap<String,  String>();


    public clsKPSAdapter(Context context, Map<String,  List<clsBillDtl>> hmKOTDtl,List<clsBillDtl> arrListItem, Map<String,  String> hmSelectedKOTDtl) {
        this.hmKOTDtl = hmKOTDtl;
        this.context = context;
        this.arrListItem = arrListItem;
        this.hmSelectedKOTDtl= hmSelectedKOTDtl;
    }

    /*private view holder class*/
    public class ViewHolder
    {
        ListView kdsListView;
        TextView txtTableNo,txtKotNo,txtKOTTime;
        LinearLayout linearKDS;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.kdsscreenforadapter, null);
            viewHolder = new ViewHolder();

            viewHolder.kdsListView = (ListView) convertView.findViewById(R.id.kdslist);
            viewHolder.txtTableNo = (TextView) convertView.findViewById(R.id.kdsTableNo);
            viewHolder.txtKotNo = (TextView) convertView.findViewById(R.id.kdsKOTNo);
            viewHolder.txtKOTTime = (TextView) convertView.findViewById(R.id.kdsKOTTime);
            viewHolder.linearKDS = (LinearLayout) convertView.findViewById(R.id.linearKDS);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final clsBillDtl objKOT = (clsBillDtl)arrListItem.get(position);
        final String text=objKOT.getStrKOTNo();


        if(hmSelectedKOTDtl.size()>0)
        {
            if(hmSelectedKOTDtl.containsKey(text))
            {

                viewHolder.txtKotNo.setText(objKOT.getStrKOTNo());
                viewHolder.txtKotNo.setTextColor(Color.BLUE);
                viewHolder.txtTableNo.setText(objKOT.getStrTableName());
                viewHolder.txtTableNo.setTextColor(Color.BLUE);
            }
            else
            {
                viewHolder.txtKotNo.setText(objKOT.getStrKOTNo());
                viewHolder.txtTableNo.setText(objKOT.getStrTableName());
            }
        }
        else
        {
            viewHolder.txtKotNo.setText(objKOT.getStrKOTNo());
            viewHolder.txtTableNo.setText(objKOT.getStrTableName());
        }



        viewHolder.txtKOTTime.setText(objKOT.getStrKOTTime());
        viewHolder.txtKOTTime.setTextColor(Color.RED);


        if(hmKOTDtl.size()>0)
        {
            if(hmKOTDtl.containsKey(objKOT.getStrKOTNo()))
            {
                List<clsBillDtl> arrKOTListItem=hmKOTDtl.get(objKOT.getStrKOTNo());
                clsKPSListItemAdapter adapter= new clsKPSListItemAdapter(clsKPSScreen.mActivity,arrKOTListItem);
                adapter.setCustomButtonListner((clsKPSListItemAdapter.customButtonListener) clsKPSScreen.mActivity);
                viewHolder.kdsListView.setAdapter(adapter);
            }

        }


        viewHolder.linearKDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onKOTClickListner(position, text);
                    viewHolder.txtKotNo.setBackgroundColor(Color.BLUE);
                    viewHolder.txtTableNo.setBackgroundColor(Color.BLUE);
                }

            }
        });





        return convertView;
    }

    public int getCount()
    {
        return arrListItem.size();
    }

    public Object getItem(int position)
    {
        return arrListItem.get(position);
    }

    public long getItemId(int position)
    {
        return arrListItem.indexOf(getItem(position));
    }
}