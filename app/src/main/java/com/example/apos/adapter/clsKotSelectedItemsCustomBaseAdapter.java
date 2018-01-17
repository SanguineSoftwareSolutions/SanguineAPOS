package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsKotSelectedListItemBean;

import java.util.HashMap;
import java.util.List;

public  class clsKotSelectedItemsCustomBaseAdapter extends BaseAdapter
{
	customButtonListener customListner;

	public interface customButtonListener {
		public void onButtonClickListner(int position, String value);
	}

	public void setCustomButtonListner(customButtonListener listener) {
		this.customListner = listener;
	}
	private Context context;
	List<clsKOTItemDtlBean> selectedRowItems;
	List<clsKOTItemDtlBean> previousKOTRowItems;
	private ViewHolder holder = null;
	private int previousKOTSize=0;


	public clsKotSelectedItemsCustomBaseAdapter(Context context, List<clsKOTItemDtlBean> items,int previousKOTSize)
	{
		this.context = context;
		this.selectedRowItems = items;
		this.previousKOTSize=previousKOTSize;

	}

	/*private view holder class*/
	private class ViewHolder {
		LinearLayout llKotList;
		TextView tvKotSelectedItemListDesc;
		TextView etKotSelectedItemListQty;
		TextView tvKotSelectedItemListAmount;
		//Button tvDirectBillSelectedItemListDelete;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater mInflater = (LayoutInflater)
				context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.kot_items_selected_list_row, null);
			holder = new ViewHolder();
			holder.llKotList = (LinearLayout) convertView.findViewById(R.id.ll_list_row_kot);
			holder.tvKotSelectedItemListDesc = (TextView) convertView.findViewById(R.id.tv_list_row_kot_desc);
			holder.etKotSelectedItemListQty = (TextView) convertView.findViewById(R.id.txt_list_row_kot_qty);
			holder.tvKotSelectedItemListAmount = (TextView) convertView.findViewById(R.id.tv_list_row_kot_amount);
			//holder.tvDirectBillSelectedItemListDelete = (Button) convertView.findViewById(R.id.tv_list_row_direct_bill_delete);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		final clsKOTItemDtlBean listItemBean = selectedRowItems.get(position);
		final String code=listItemBean.getStrItemCode();
		final String text=listItemBean.getStrItemName();
		if(previousKOTSize>0)
		{
			if(text.contains("#"))
			{
				String kotTime=text.split("#")[2].split(":")[0]+":"+text.split("#")[2].split(":")[1];
				holder.tvKotSelectedItemListDesc.setText(Html.fromHtml( "<b>"+text.split("#")[1]+"   "+kotTime  + "</b>" +
						"<br>"+text.split("#")[0]+"<br/>"));

				//  holder.tvKotSelectedItemListDesc.setText(text.split("#")[1]+"                     "+text.split("#")[2] +"          "+ text.split("#")[0]);
			}
			else
			{
				holder.tvKotSelectedItemListDesc.setText(listItemBean.getStrItemName());
			}
			if(listItemBean.getDblItemQuantity() != 0){
				String []arrQtyData=String.valueOf(listItemBean.getDblItemQuantity()).split("\\.");
				int Qty=Integer.parseInt(arrQtyData[0]);
				holder.etKotSelectedItemListQty.setText(String.valueOf(Qty));
			}
			holder.etKotSelectedItemListQty.setId(position);

			if(listItemBean.getDblAmount() != -1){
				holder.tvKotSelectedItemListAmount.setText(Double.toString(listItemBean.getDblAmount()));
			}
		}
		else
		{
			if(listItemBean.getStrItemName() != null){
				holder.tvKotSelectedItemListDesc.setText(listItemBean.getStrItemName());
			}
			if(listItemBean.getDblItemQuantity() != 0){
				String []arrQtyData=String.valueOf(listItemBean.getDblItemQuantity()).split("\\.");
				int Qty=Integer.parseInt(arrQtyData[0]);
				holder.etKotSelectedItemListQty.setText(String.valueOf(Qty));
			}
			holder.etKotSelectedItemListQty.setId(position);

			if(listItemBean.getDblAmount() != -1){
				holder.tvKotSelectedItemListAmount.setText(Double.toString(listItemBean.getDblAmount()));
			}
		}


		holder.llKotList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (customListner != null)
				{
					customListner.onButtonClickListner(position, text + "#selectedrow"+ "#"+code);
				}

			}
		});

		if(previousKOTSize>0)
		{

			if(position>=0&&position<previousKOTSize)
			{
				//convertView.setBackgroundColor(Color.GRAY);
				holder.tvKotSelectedItemListDesc.setTextColor(Color.BLACK);
				holder.etKotSelectedItemListQty.setTextColor(Color.BLACK);
				holder.tvKotSelectedItemListAmount.setTextColor(Color.BLACK);
			}
			else
			{
				// convertView.setBackgroundColor(Color.LTGRAY);
				holder.tvKotSelectedItemListDesc.setTextColor(Color.BLUE);
				holder.etKotSelectedItemListQty.setTextColor(Color.BLUE);
				holder.tvKotSelectedItemListAmount.setTextColor(Color.BLUE);
			}
		}
		else
		{
			// convertView.setBackgroundColor(Color.LTGRAY);
			holder.tvKotSelectedItemListDesc.setTextColor(Color.BLUE);
			holder.etKotSelectedItemListQty.setTextColor(Color.BLUE);
			holder.tvKotSelectedItemListAmount.setTextColor(Color.BLUE);
		}





		return convertView;
	}

	public int getCount() {
		return selectedRowItems.size();
	}

	public Object getItem(int position) {
		return selectedRowItems.get(position);
	}

	public long getItemId(int position) {
		return selectedRowItems.indexOf(getItem(position));
	}
}
