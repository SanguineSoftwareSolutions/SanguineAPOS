package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.adapter.clsKotTableAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotTableListSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.views.CustomSearchView;
import com.example.apos.views.CustomView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sanguine on 9/14/2015.
 */
public class clsKotTable extends Fragment implements clsKotTableListSelectionListener {
	private clsKotTableListSelectionListener tableSelectionListener;
	public ArrayList<clsTableMaster> arrListTableMaster;
	public clsMakeKotActListener objMakeKotActListener;
	private ConnectivityManager connectivityManager;
	private Dialog pgDialog;
	private EditText edtTableSearch;

	@BindView(R.id.TableCustomSearchView)
	CustomSearchView customSearchView;

	@BindView(R.id.vgContent)
	ViewGroup vgContent;
	@BindView(R.id.vgProgress)
	ViewGroup vgProgress;
	@BindView(R.id.kottable_gridview)
	GridView kotTableGridview;

	private View view;
	private Activity activity;

	public static clsKotTable getInstance() {
		clsKotTable mKotTable = new clsKotTable();
		return mKotTable;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.kot_screen, container, false);
		ButterKnife.bind(this, view);
		//edtTableSearch=(EditText) findViewById(R.id.etSearch);
		// customSearchView=(CustomSearchView) container.findViewById(R.id.TableCustomSearchView);
		 edtTableSearch  = customSearchView.getEditText();
         edtTableSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString()!=null&&arrListTableMaster!=null){
					ArrayList arrayListtemp = new ArrayList();
					for (int cnt = 0; cnt <arrListTableMaster.size(); cnt++)
					{
						clsTableMaster obj = (clsTableMaster) arrListTableMaster.get(cnt);
						if (obj.getStrTableName().toLowerCase().contains(s.toString().toLowerCase()))
						{
							arrayListtemp.add(arrListTableMaster.get(cnt));
						}
					}
					funFillTableGrid(arrayListtemp);
				}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	private void init() {
		activity = getActivity();
		showProgress();
		connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			tableSelectionListener = (clsKotTableListSelectionListener) clsKotTable.this;
			objMakeKotActListener = (clsMakeKotActListener) activity;
		} catch (Exception ex) {
			Log.i("Tg", " Unable to initialize database." + ex.getMessage());
		}
		if (ConnectivityHelper.isConnected()) {
			funGetTableList();
		} else {
			showErrorState();
			Toast.makeText(activity, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void getTableListSelected(String strTableNo, String strTableName) {
		String status = "",areaCode="";
		for (clsTableMaster objTable : arrListTableMaster) {
			if (objTable.getStrTableNo().equals(strTableNo)) {
				status = objTable.getStrTableStatus();
				areaCode=objTable.getStrAreaCode();
				break;
			}
		}
		objMakeKotActListener.setTableSelectedResult(strTableNo, strTableName, status,areaCode);
		funFillTableGrid(arrListTableMaster);
	}

	@Override
	public void getDirectKotItemListSelected(String strTableNo, String strTableName, String strWaiterNo, String strWaiterName) {
		String status = "", cardType = "", cardNo = "",linkedWaiterNo="",areaCode="";
		double kotAmt = 0, cardBalance = 0;
		int paxNo = 1;
		for (clsTableMaster objTable : arrListTableMaster) {
			if (objTable.getStrTableNo().equals(strTableNo)) {
				status = objTable.getStrTableStatus();
				kotAmt = objTable.getDblKOTAmt();
				cardBalance = objTable.getDblCardBalanace();
				cardType = objTable.getStrCardType();
				paxNo = objTable.getIntPaxNo();
				cardNo = objTable.getStrCardNo();
				linkedWaiterNo=objTable.getStrLinkedWaiterNo();
				areaCode=objTable.getStrAreaCode();
				break;
			}
		}
		objMakeKotActListener.setDirectKotItemListSelectedResult1(strTableNo, strTableName, strWaiterNo, strWaiterName, status, kotAmt, cardBalance, cardType, paxNo, cardNo,linkedWaiterNo,areaCode);
	}


	private void funFillTableGrid(ArrayList arrListTableMaster) {
		if (null != activity) {
			kotTableGridview.setAdapter(new clsKotTableAdapter(activity, arrListTableMaster, tableSelectionListener));
		}
	}

	public void funGetTableList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetTableList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCMSIntegrationYN, clsGlobalFunctions.gTreatMemberAsTable, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
					@Override
					public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
						dismissDialog();
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								dismissProgress();
							}
						}, 5000);

						if (null != arrListTemp) {
							if (arrListTemp.size() > 0) {
								arrListTableMaster = arrListTemp;
								funFillTableGrid(arrListTableMaster);
								dismissProgress();
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
						showErrorState();
					}

				});
			} else {
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
				showErrorState();
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}


	private void showProgress() {
		vgContent.setVisibility(View.GONE);
		vgProgress.setVisibility(View.VISIBLE);
	}

	private void dismissProgress() {
		vgContent.setVisibility(View.VISIBLE);
		vgProgress.setVisibility(View.GONE);
	}


	private void showErrorState() {
		dismissProgress();
		vgContent.setVisibility(View.GONE);
	}

	protected void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(activity, 0, false);
		}
		pgDialog.show();
	}

	protected void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}
}
