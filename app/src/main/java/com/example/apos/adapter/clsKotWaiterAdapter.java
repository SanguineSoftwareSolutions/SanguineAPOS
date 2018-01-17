package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.apos.activity.R;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.listeners.clsKotWaiterListSelectionListener;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;


public class clsKotWaiterAdapter extends BaseAdapter {
	private Context mContext;
	private Activity mActivity;
	private ArrayList arrListWaiter;
	private static LayoutInflater inflater = null;
	private clsKotWaiterListSelectionListener waiterSelectionListener;

	public clsKotWaiterAdapter(Context c, Activity activity, ArrayList arrListWaiter, clsKotWaiterListSelectionListener mWaiterSelectionListener) {
		mContext = c;
		mActivity = activity;
		this.arrListWaiter = arrListWaiter;
		this.waiterSelectionListener = mWaiterSelectionListener;
	}

	public int getCount() {
		// return images.length;
		return arrListWaiter.size();
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
		gridView = inflater.inflate(R.layout.waiter_gridview_members, null);

		SquareTextView btnWaiter = (SquareTextView) gridView.findViewById(R.id.btnWaiter);
		btnWaiter.setTag(position);
		clsWaiterMaster objWaiter = (clsWaiterMaster) arrListWaiter.get(position);
		btnWaiter.setText(objWaiter.getStrWaiterName());


		btnWaiter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				clsWaiterMaster objWaiter = (clsWaiterMaster) arrListWaiter.get(pos);

				waiterSelectionListener.getWaiterListSelected(objWaiter.getStrWaterNo(), objWaiter.getStrWaiterName());

			}
		});

		return gridView;
	}
}