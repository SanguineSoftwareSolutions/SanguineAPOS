package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsWaiterMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monika on 8/26/2017.
 */


public class clsWaiterAdapter extends BaseAdapter
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onWaiterClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    private Context context;
    private List<clsWaiterMaster> arrListItem = new ArrayList();

    public clsWaiterAdapter(Context context,List<clsWaiterMaster> arrListItem) {
        this.context = context;
        this.arrListItem = arrListItem;
    }

    /*private view holder class*/
    public class ViewHolder
    {
        LinearLayout linearWaiter;
        TextView txtWaiterNo,txtWaiterName;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.waiterselectionadapter, null);
            viewHolder = new ViewHolder();

            viewHolder.txtWaiterNo = (TextView) convertView.findViewById(R.id.txtViewWaiterNo);
            viewHolder.txtWaiterName = (TextView) convertView.findViewById(R.id.txtViewWaiterName);
            viewHolder.linearWaiter = (LinearLayout) convertView.findViewById(R.id.linearWaiter);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final clsWaiterMaster objWaiter= (clsWaiterMaster)arrListItem.get(position);
        final String text=objWaiter.getStrWaterNo()+ "#" +objWaiter.getStrWaiterName();

        viewHolder.txtWaiterNo.setText(objWaiter.getStrWaterNo());
        viewHolder.txtWaiterName.setText(objWaiter.getStrWaiterName());

        viewHolder.linearWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onWaiterClickListner(position, text);
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