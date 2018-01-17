package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.adapter.clskotMenuItemsGridAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotItemListSelectionListener;
import com.example.apos.listeners.clsKotMenuItemSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.util.clsUtility;
import com.example.apos.views.CustomSearchView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class clsKotMenuItemFragment extends Fragment implements clsKotMenuItemSelectionListener {

	private static String TAG = "BeWo_Restaurant_" + clsKotMenuItemFragment.class.getName();
	private EditText edtMenuSearch;
	private static GridView gvMenuItems;
	private ArrayList arrListMenuItemMaster = null;
	private  static clsKotMenuItemSelectionListener menuItemSelectionListener = null;
	public clsMakeKotActListener objMakeKotActListener;
	private ConnectivityManager connectivityManager;
	private Dialog pgDialog;
	Activity activity;
	ImageView imvRefresh;
	@BindView(R.id.edtkotMenusearch)
	CustomSearchView customSearchView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity=getActivity();
		View rootView = inflater.inflate(R.layout.kotmenuitems, container, false);
		ButterKnife.bind(clsKotScreen.mActivity);
		connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	//	getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		try {
			menuItemSelectionListener = (clsKotMenuItemSelectionListener) clsKotMenuItemFragment.this;
			objMakeKotActListener = (clsMakeKotActListener) getActivity();
		} catch (Exception ex) {
			Log.i(TAG, " Unable to initialize database." + ex.getMessage());
		}
		imvRefresh=(ImageView) rootView.findViewById(R.id.imvRefresh);
		customSearchView=(CustomSearchView) rootView.findViewById(R.id.edtkotMenusearch);
        edtMenuSearch = customSearchView.getEditText();
		customSearchView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
					funGetCounterWiseMenuList();
				}
				else
				{
					funGetMenuHeadList();
				}
			}
		});
        edtMenuSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ArrayList arrayListtemp = new ArrayList();
                if(null!=arrListMenuItemMaster&&s.toString()!=null) {
                    for (int cnt = 0; cnt < arrListMenuItemMaster.size(); cnt++)
                    {
                        clsKotMenuItemsBean objMenu = (clsKotMenuItemsBean) arrListMenuItemMaster.get(cnt);
                        String formName = objMenu.getStrMenuItemName().toString().toLowerCase();
                        s = s.toString().toLowerCase();
                        if (formName.contains(s))
                        {
                            arrayListtemp.add(arrListMenuItemMaster.get(cnt));
                        }
                    }
                }
                funSetMenuList(arrayListtemp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

		gvMenuItems = (GridView) rootView.findViewById(R.id.gvkotmenuitemlist);

		imvRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
				{
					edtMenuSearch.setText("");
					if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
						funGetCounterWiseMenuList();
					} else {
						funGetMenuHeadList();
					}
				} else {
					Toast.makeText(getActivity(), "Internet Connection Not Found", Toast.LENGTH_LONG).show();
				}
			}
		});

		funGetMenuHeadList();

		return rootView;
	}


	@Override
	public void onResume() {
		super.onResume();
	}


	public void funSetMenuList(ArrayList arrList) {
		gvMenuItems.setAdapter(new clskotMenuItemsGridAdapter(clsKotScreen.mActivity, clsKotScreen.mActivity, arrList, menuItemSelectionListener));
	}


	@Override
	public void getMenuItemSelectionResult(String strMenuItemCode, String strMenuItemName, String strMenuType) {
		objMakeKotActListener.setMenuItemSelectionCodeResult(strMenuItemCode, strMenuItemName, strMenuType);
	}

	public void populateSelectedSubMenuItems(String strMenuItemCode, String strMenuItemName) {
		funGetSubMenuHeadList(strMenuItemCode);
	}

	public void funGetMenuHeadList()
	{
		arrListMenuItemMaster = new ArrayList();
		arrListMenuItemMaster=clsGlobalFunctions.gArrListMenuItemMaster;
		if (arrListMenuItemMaster.size() > 0) {
			funSetMenuList(arrListMenuItemMaster);
		}
		else
		{
			funSetMenuList(new ArrayList());
		}
	}


	public void funGetSubMenuHeadList(String menuCode) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				arrListMenuItemMaster = new ArrayList();
				showDialog();
				App.getAPIHelper().funGetSubMenuList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCounterCode, menuCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try {
								JsonArray mJsonArray = (JsonArray) jObj.get("SubMenuList");
								JsonObject mJsonObject = new JsonObject();
								for (int i = 0; i < mJsonArray.size(); i++) {
									mJsonObject = (JsonObject) mJsonArray.get(i);
									if (mJsonObject.get("SubMenuName").getAsString().equals("")) {
										//memberInfo = "no data";
									} else {
										clsKotMenuItemsBean obj = new clsKotMenuItemsBean();
										obj.setStrMenuItemName(mJsonObject.get("SubMenuName").getAsString());
										obj.setStrMenuItemCode(mJsonObject.get("SubMenuCode").getAsString());
										obj.setStrMenuType("SubMenuHead");
										arrListMenuItemMaster.add(obj);
									}
								}
								if (arrListMenuItemMaster.size() > 0) {
									funSetMenuList(arrListMenuItemMaster);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
					}
				});
			} else {
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}

	public void funGetCounterWiseMenuList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				final ArrayList arrListMenuMaster = new ArrayList();
				showDialog();
				App.getAPIHelper().funGetCounterWiseMenuList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCounterCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try {
								JsonArray mJsonArray = (JsonArray) jObj.get("MenuHeadList");
								JsonObject mJsonObject = new JsonObject();
								for (int i = 0; i < mJsonArray.size(); i++) {
									mJsonObject = (JsonObject) mJsonArray.get(i);
									if (mJsonObject.get("MenuName").getAsString().equals("")) {
										//memberInfo = "no data";
									} else {
										clsKotMenuItemsBean obj = new clsKotMenuItemsBean();
										obj.setStrMenuItemName(mJsonObject.get("MenuName").getAsString());
										obj.setStrMenuItemCode(mJsonObject.get("MenuCode").getAsString());
										obj.setStrMenuType("MenuHead");
										arrListMenuItemMaster.add(obj);
									}
								}
								if (arrListMenuMaster.size() > 0) {
									clsDirectBillerClsMenuItemFragment obj=new clsDirectBillerClsMenuItemFragment();
									obj.funSetMenuList(arrListMenuMaster);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
					}
				});


			} else {
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}

	protected void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(clsKotScreen.mActivity, 0, false);
		}
		pgDialog.show();
	}

	protected void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}

}