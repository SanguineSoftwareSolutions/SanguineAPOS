package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class clsPOSSelectionAdapter extends BaseAdapter {

	customButtonListener customListner;
	private LayoutInflater layoutInflater;
	private ViewHolder viewHolder = null;

	public interface customButtonListener {
		public void onButtonClickListner(int position, String value);
	}

	public void setCustomButtonListner(customButtonListener listener) {
		this.customListner = listener;

	}


	private Activity activity;
	private ArrayList arrListTable;

	public clsPOSSelectionAdapter(Activity c, ArrayList arrListTable) {
		activity = c;
		this.arrListTable = arrListTable;
		layoutInflater = c.getLayoutInflater();
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
//	public View getView(final int position, View convertView, final ViewGroup parent) {
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View gridView;
//		gridView = inflater.inflate(R.layout.grid_item_pos, null);
//
//
//		SquareTextView btnPOSName = (SquareTextView) gridView.findViewById(R.id.btnTable);
//
//		btnPOSName.setTag(position);
//		final clsPosSelectionMaster objPos = (clsPosSelectionMaster) arrListTable.get(position);
//		btnPOSName.setText(objPos.getStrPosName());
//
//
//		btnPOSName.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (customListner != null) {
//					customListner.onButtonClickListner(position, objPos.getStrPosName() + "#selectedrow");
//
//				}
//			}
//		});
//
//		return btnPOSName;
//
//
//	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (null == convertView)
		{
			convertView = layoutInflater.inflate(R.layout.grid_item_pos, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final clsPosSelectionMaster objPos = (clsPosSelectionMaster) arrListTable.get(position);
		viewHolder.btnPOSName.setTag(position);
		viewHolder.btnPOSName.setText(objPos.getStrPosName());


		viewHolder.btnPOSName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (customListner != null) {
					customListner.onButtonClickListner(position, objPos.getStrPosName() + "#selectedrow");

				}
			}
		});

		return convertView;
	}


	private class ViewHolder {
		SquareTextView btnPOSName;

		public ViewHolder(View v) {
			btnPOSName = ButterKnife.findById(v, R.id.btnTable);
		}
	}
}