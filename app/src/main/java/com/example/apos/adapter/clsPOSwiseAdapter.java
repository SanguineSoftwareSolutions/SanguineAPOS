package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.apos.activity.R;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;
import com.example.apos.bean.clsSalesReportDtl;
import java.util.ArrayList;
import java.util.List;


public  class clsPOSwiseAdapter extends BaseAdapter
{
    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
    private Context context;
    private List<clsSalesReportDtl>  arrListItem = new ArrayList();
    private double totalAmount=0;

    public clsPOSwiseAdapter(Context context, List<clsSalesReportDtl> arrListPOSWise, double totalAmount) {
        this.arrListItem = arrListPOSWise;
        this.context = context;
        this.totalAmount=totalAmount;
    }

    /*private view holder class*/
    public class ViewHolder {
        TextView textPOSName,textSaleAmount,textSalePer;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.poswiselist, null);
            viewHolder = new ViewHolder();

            viewHolder.textPOSName = (TextView) convertView.findViewById(R.id.txtPOSName);
            viewHolder.textSaleAmount = (TextView) convertView.findViewById(R.id.txtSaleAmt);
            viewHolder.textSalePer = (TextView) convertView.findViewById(R.id.txtSalePer);


            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        clsSalesReportDtl objSale = (clsSalesReportDtl)arrListItem.get(position);
//        final String text = getItem(position);

        viewHolder.textPOSName.setText(objSale.getPosName());
        double per=((Double.parseDouble(objSale.getDblSettleAmt())/totalAmount)*100);
        if(objSale.getDblSettleAmt().contains("E"))
        {
            String []arrSP=objSale.getDblSettleAmt().split("E");
            viewHolder.textSaleAmount.setText(arrSP[0]);
        }
        else
        {
            viewHolder.textSaleAmount.setText(objSale.getDblSettleAmt());;
        }

        String[] perValue=String.valueOf(Math.rint(per)).split("\\.");
        viewHolder.textSalePer.setText(perValue[0]+"%");

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
