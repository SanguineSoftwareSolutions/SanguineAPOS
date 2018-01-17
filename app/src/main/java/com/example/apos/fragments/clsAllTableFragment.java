package com.example.apos.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsViewTableStatusScreen;
import com.example.apos.adapter.clsViewTableStatusAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import java.util.ArrayList;

public class clsAllTableFragment extends Fragment
{
    private GridView allTableGridview;
    public ArrayList<clsTableMaster> arrListAllTableMaster;
    private ConnectivityManager connectivityManager;
    private String posCode="",areaCode="";
    private int paxCount=0;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.viewalltabletab, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        posCode= getArguments().getString("POSCode");
        areaCode= getArguments().getString("AreaCode");
        allTableGridview = (GridView) rootView.findViewById(R.id.viewAllAable_Gridview);
        funGetAllTable();
        return rootView;
    }


    public void funGetAllTable()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                App.getAPIHelper().funGetTableStatus(posCode,"All",areaCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsTableMaster> arrListTemp)
                    {
                        if (null != arrListTemp)
                        {
                            if (arrListTemp.size()>0)
                            {
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    clsTableMaster ojTable=arrListTemp.get(cnt);
                                    if(ojTable.getStrTableStatus().equals("Occupied"))
                                    {
                                        paxCount=paxCount+ojTable.getIntPaxNo();
                                    }
                                }
                                arrListAllTableMaster=arrListTemp;
                                funFillTableGrid(arrListTemp);
                            }
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(getActivity(), R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(getActivity(), R.string.setup_your_server_settings);
        }
    }

    private void funFillTableGrid(ArrayList arrListTableMaster)
    {
        clsViewTableStatusScreen.txtPaxCount.setText("Total Pax "+paxCount );
        allTableGridview.setAdapter(new clsViewTableStatusAdapter(getActivity(), this.getActivity(), arrListTableMaster,"All"));
    }


}
