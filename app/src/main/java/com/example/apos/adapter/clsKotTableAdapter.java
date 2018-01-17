package com.example.apos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.listeners.clsKotTableListSelectionListener;
import com.example.apos.views.SquareTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class clsKotTableAdapter extends BaseAdapter {
	private ArrayList arrListTable;
	private LayoutInflater layoutInflater;
	private ViewHolder viewHolder = null;
	private clsKotTableListSelectionListener tableSelectionListener;

	public clsKotTableAdapter(Activity activity, ArrayList arrListTable, clsKotTableListSelectionListener mTableSelectionListener) {
		this.arrListTable = arrListTable;
		this.tableSelectionListener = mTableSelectionListener;
		layoutInflater = activity.getLayoutInflater();
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
	public View getView(final int position, View convertView, final ViewGroup parent) {

		if (null == convertView) {
			convertView = layoutInflater.inflate(R.layout.grid_item_table, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		clsTableMaster tableDTO = (clsTableMaster) arrListTable.get(position);
		viewHolder.btnTable.setText(tableDTO.getStrTableName());
		viewHolder.btnTable.setTag(tableDTO);

		boolean isOccupied = tableDTO.getStrTableStatus().equalsIgnoreCase("Occupied");
		boolean isBilled = tableDTO.getStrTableStatus().equalsIgnoreCase("Billed");
		boolean isReversed = tableDTO.getStrTableStatus().equalsIgnoreCase("Reserve");

		/*if (objTable.getStrTableStatus().equalsIgnoreCase("Occupied")) {
			btnTable.setBackgroundResource(R.drawable.redbutton);
		} else if (objTable.getStrTableStatus().equalsIgnoreCase("Billed")) {
			btnTable.setBackgroundResource(R.drawable.bluebutton);
		} else if (objTable.getStrTableStatus().equalsIgnoreCase("Reserved")) {
			btnTable.setBackgroundResource(R.drawable.greenbutton);
		}*/

		viewHolder.btnTable.setSelected(false);
		viewHolder.btnTable.setActivated(false);

		if (isOccupied) {
			viewHolder.btnTable.setActivated(true);
		}

		if (isBilled) {
			viewHolder.btnTable.setSelected(true);
			viewHolder.btnTable.setActivated(true);
		}

		if (isReversed) {
			viewHolder.btnTable.setSelected(true);
		}

		viewHolder.btnTable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clsTableMaster objTable = (clsTableMaster) v.getTag();

				if (objTable.getStrTableStatus().equalsIgnoreCase("Occupied")) {
					Toast.makeText(parent.getContext(), "Busy Table...... ", Toast.LENGTH_SHORT).show();
					tableSelectionListener.getDirectKotItemListSelected(objTable.getStrTableNo(), objTable.getStrTableName(), objTable.getStrWaiterCode(), objTable.getStrWaiterName());

				} else {
					// Table is open for new order.
					tableSelectionListener.getTableListSelected(objTable.getStrTableNo(), objTable.getStrTableName());
				}
			}
		});
		return convertView;
	}

	private class ViewHolder {
		SquareTextView btnTable;

		public ViewHolder(View v) {
			btnTable = ButterKnife.findById(v, R.id.btnTable);

		}
	}
}


