package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsBillDtl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monika on 8/28/2017.
 */


public  class clsKPSListItemAdapter extends BaseAdapter
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onKOTItemClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    private Context context;
    private List<clsBillDtl> arrListItem = new ArrayList();
    Map<String,  String> hmSelectedKOTItemDtl = new HashMap<String,  String>();
    private final int Orange=0xFFFF3300;

    public clsKPSListItemAdapter(Context context, List<clsBillDtl> arrListItem) {
        this.context = context;
        this.arrListItem = arrListItem;
    }

    /*private view holder class*/
    public class ViewHolder
    {
        TextView txtItemName,txlQty;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.kdslistviewitem, null);
            viewHolder = new ViewHolder();
            viewHolder.txtItemName = (TextView) convertView.findViewById(R.id.kdslistitem);
            viewHolder.txlQty = (TextView) convertView.findViewById(R.id.kdslistitemqty);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        clsBillDtl objKOT = (clsBillDtl)arrListItem.get(position);;
        String []arrQtyData=String.valueOf(objKOT.getDblQuantity()).split("\\.");
        int Qty=Integer.parseInt(arrQtyData[0]);
        viewHolder.txlQty.setText(String.valueOf(Qty)+".0");
        viewHolder.txtItemName.setText(" "+objKOT.getStrItemName());
        String itemTextColor=objKOT.getStrKOTTimeDifferenceResult();

        if(itemTextColor.equals("RED"))
        {
            viewHolder.txtItemName.setTextColor(Color.RED);
            viewHolder.txlQty.setTextColor(Color.RED);
        }
        else if(itemTextColor.equals("ORANGE"))
        {
            viewHolder.txtItemName.setTextColor(Color.CYAN);
            viewHolder.txlQty.setTextColor(Color.CYAN);
        }
        else
        {
            viewHolder.txtItemName.setTextColor(Color.WHITE);
            viewHolder.txlQty.setTextColor(Color.WHITE);
        }


        final String text=objKOT.getStrItemCode()+"#"+objKOT.getStrItemName()+"#"+objKOT.getStrKOTNo()+"#"+objKOT.getStrTableName()+"#"+objKOT.getStrItemType()+"#"+objKOT.getStrWaiterNo()+"#"+objKOT.getStrKOTDate();

        viewHolder.txtItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customListner != null)
                {
                    customListner.onKOTItemClickListner(position, text);
                    viewHolder.txtItemName.setBackgroundColor(Color.CYAN);
                    viewHolder.txlQty.setBackgroundColor(Color.CYAN);
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