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
import com.example.apos.bean.clsCostCenterDtl;

import java.util.ArrayList;
import java.util.List;




public class clsCostCenterAdapter extends BaseAdapter
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onCostCenterClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    private Context context;
    private List<clsCostCenterDtl> arrListItem = new ArrayList();

    public clsCostCenterAdapter(Context context,List<clsCostCenterDtl> arrListItem) {
        this.context = context;
        this.arrListItem = arrListItem;
    }

    /*private view holder class*/
    public class ViewHolder
    {
        LinearLayout linearCostCenter;
        TextView txtCostCenterCode,txtCostCenterName;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.costcenteradapter, null);
            viewHolder = new ViewHolder();

            viewHolder.txtCostCenterCode = (TextView) convertView.findViewById(R.id.txtViewCostCenterCode);
            viewHolder.txtCostCenterName = (TextView) convertView.findViewById(R.id.txtViewCostCenterName);
            viewHolder.linearCostCenter = (LinearLayout) convertView.findViewById(R.id.linearCostCenter);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final clsCostCenterDtl objCostCenter = (clsCostCenterDtl)arrListItem.get(position);
        final String text=objCostCenter.getStrCostCenterCode()+ "#" +objCostCenter.getStrCostCenterName();

        viewHolder.txtCostCenterCode.setText(objCostCenter.getStrCostCenterCode());
        viewHolder.txtCostCenterName.setText(objCostCenter.getStrCostCenterName());

        viewHolder.linearCostCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onCostCenterClickListner(position, text);
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