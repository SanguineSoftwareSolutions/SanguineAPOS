package com.example.apos.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.ExceptionHandler;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsLoginActivity;
import com.example.apos.activity.clsMiniMakeKotScreen;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.PreferenceUtil;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.dto.ConfigurationDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServerConfigurationFragment extends DialogFragment {

	@BindView(R.id.etServerName)
	AutoCompleteTextView etServerName;
	@BindView(R.id.etServerIp)
	EditText etServerId;

	@BindView(R.id.vgProgress)
	RelativeLayout vgProgress;

	@BindView(R.id.vgSettings)
	LinearLayout vgSettings;
	@BindView(R.id.vgMainDialog)
	RelativeLayout vgMainDialog;

	@BindView(R.id.spinnerPrinter)
	Spinner spinnerPrinter;

	HashMap<String, String> testHashMap = new HashMap<String, String>();
	ArrayList<String> arrIPList=new ArrayList<>();
	ArrayList<String> PrinterList=new ArrayList<String>();


	public interface Callback {
		void onSuccess();
	}

	private Callback callback;

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_configuration, container, false);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
		ButterKnife.bind(this, view);

		setupIPAddress();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		return dialog;
	}


	@OnClick(R.id.btnClose)
	public void onClickClose() {
		dismiss();
	}

	@OnClick(R.id.btnConfigure)
	public void checkServerConnection() {
		String ip = etServerId.getText().toString().trim();
		final String newIP="http://" + ip + "/";
		if (!StringUtils.isEmpty(ip)) {
			clsGlobalFunctions.gAPOSWebSrviceURL = "http://" + ip + "/";
            if(etServerName.getText().toString().isEmpty())
            {
                Toast.makeText(getActivity(), "Server Location should not be empty!!", Toast.LENGTH_SHORT).show();
                return;
            }
			if (ConnectivityHelper.isConnected()) {
				showProgress();
				App.refreshBaseURL();
				if(!newIP.equals(clsGlobalFunctions.gAPOSWebSrviceURL))
				{
					clsGlobalFunctions.gAPOSWebSrviceURL=newIP;
				}
				App.getAPIHelper().checkServer(new BaseAPIHelper.OnRequestComplete<Boolean>() {
					@Override
					public void onSuccess(Boolean object) {
						dismissProgress();
						if (null != object) {
							if (object)
							{
								if (null != callback) {
									//PreferenceUtil.clear();
									boolean flgResult=false;
									if(testHashMap.size()>0)
									{
										if(testHashMap.containsKey(etServerName.getText().toString()))
										{
											String oldURL=testHashMap.get(etServerName.getText().toString());
											if(testHashMap.containsValue(clsGlobalFunctions.gAPOSWebSrviceURL))
											{
												if(oldURL.equals(clsGlobalFunctions.gAPOSWebSrviceURL))
												{
													flgResult=true;
												}
												else
												{
													flgResult=false;
													Toast.makeText(getActivity(), "IP already assigned to another server!!", Toast.LENGTH_SHORT).show();
													//return;
												}
											}
											else
											{
												flgResult=true;
											}
										}
										else
										{
											if(testHashMap.containsValue(clsGlobalFunctions.gAPOSWebSrviceURL))
											{
												flgResult=false;
												Toast.makeText(getActivity(), "IP already assigned to another server!!", Toast.LENGTH_SHORT).show();
											}
											else
											{
												flgResult=true;
											}

										}
									}
									else
									{
										flgResult=true;
									}
									if(flgResult)
									{
										PreferenceUtil.setConfig(new ConfigurationDTO(clsGlobalFunctions.gAPOSWebSrviceURL,etServerName.getText().toString(),spinnerPrinter.getSelectedItem().toString()));

										clsGlobalFunctions.gBillPrinterType=spinnerPrinter.getSelectedItem().toString();
										testHashMap.put(etServerName.getText().toString(), clsGlobalFunctions.gAPOSWebSrviceURL);
										PreferenceUtil.setMultipleConfiguration(testHashMap);
										callback.onSuccess();
										dismiss();
									}
								}
							} else {
								SnackBarUtils.showSnackBar(vgMainDialog, "server issue");
								flushTempServerAddress();
							}
						} else {
							SnackBarUtils.showSnackBar(vgMainDialog, "invalid address");
							flushTempServerAddress();
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissProgress();
						SnackBarUtils.showSnackBar(vgMainDialog, "Unable to connect with server!! Please Start server or clear history");
						flushTempServerAddress();
						etServerId.requestFocus();
						etServerId.setSelection(etServerId.getText().toString().length());
					}
				});

			} else {
				SnackBarUtils.showSnackBar(vgMainDialog, "No internet available or not connected to any network");
			}
		} else {
			SnackBarUtils.showSnackBar(vgMainDialog, "Enter server address");
		}

	}

	@OnClick(R.id.btnClear)
	public void onClickClear()
	{
		etServerId.setText("");
		etServerName.setText("");
	}

	private void showProgress() {
		vgSettings.setVisibility(View.GONE);
		vgProgress.setVisibility(View.VISIBLE);
	}

	private void dismissProgress() {
		vgSettings.setVisibility(View.VISIBLE);
		vgProgress.setVisibility(View.GONE);
	}

	private void flushTempServerAddress() {
		clsGlobalFunctions.gAPOSWebSrviceURL = "";
	}


	private void setupIPAddress() {
		if (null != PreferenceUtil.getConfg())
		{
			final ConfigurationDTO objConfig=PreferenceUtil.getConfg();
			//clsGlobalFunctions.gAPOSWebSrviceURL = "http://" + ip + "/";
			String []arrNewIP=objConfig.getBaseURL().split("//");
			etServerId.setText(arrNewIP[1].split("/")[0]);
            etServerName.setText(objConfig.getServerName());
			etServerName.setSelection(etServerName.getText().length());

			if (null != PreferenceUtil.getMultipleConfg())
			{
              testHashMap=PreferenceUtil.getMultipleConfg();
				if(testHashMap.size()>0)
				{
					Set setIP=testHashMap.keySet();
					Iterator itrIP=setIP.iterator();
					while(itrIP.hasNext())
					{
						String serverName= (String) itrIP.next();
						arrIPList.add(serverName);
					}
				}
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item, R.id.item,arrIPList);
			etServerName.setAdapter(adapter);
			etServerName.setThreshold(1);



			etServerName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
				{
					boolean itemFound=false;
					String itemName=etServerName.getText().toString();
					if(testHashMap.size()>0)
					{
						if(testHashMap.containsKey(itemName))
						{
							String []arrNewIP=testHashMap.get(itemName).split("//");
							etServerId.setText(arrNewIP[1].split("/")[0]);
						}
					}
				}
			});


			PrinterList.add(objConfig.getBILLPrinter());
            if(objConfig.getBILLPrinter().equalsIgnoreCase("External Printer"))
			{
				PrinterList.add("Bluetooth Printer");
			}
			else
			{
				PrinterList.add("External Printer");
			}

			ArrayAdapter<String> printerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item, R.id.item,PrinterList);
			spinnerPrinter.setAdapter(printerAdapter);

			clsGlobalFunctions.gBillPrinterType=objConfig.getBILLPrinter();
			//PreferenceUtil.setConfig(PreferenceUtil.getPrinterType());

//            if (null != PreferenceUtil.getPrinterType())
//            {
//
//            }


		}
		else
		{
			PrinterList.add("External Printer");
			PrinterList.add("Bluetooth Printer");
			ArrayAdapter<String> printerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item, R.id.item,PrinterList);
			spinnerPrinter.setAdapter(printerAdapter);
		}


	}
}
