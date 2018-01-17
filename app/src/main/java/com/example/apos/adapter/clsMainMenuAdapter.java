package com.example.apos.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.bean.clsMainMenuBean;
import com.example.apos.config.StringUtils;
import com.example.apos.util.clsModuleImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class clsMainMenuAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder = null;
    ArrayList mainMenuList = null;

    public clsMainMenuAdapter(Activity c, ArrayList mainMenuList) {
        this.mainMenuList = mainMenuList;
        layoutInflater = c.getLayoutInflater();
    }

    public int getCount() {
        return mainMenuList.size();
    }

    public clsMainMenuBean getItem(int position) {
        return (clsMainMenuBean) mainMenuList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.mainmenuimage, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        clsMainMenuBean item = getItem(position);
        clsModuleImage clsModuleImage = new clsModuleImage();
        String photoURL=clsGlobalFunctions.gAPOSWebSrviceURL+"project/images/"+item.getImageName_strImageName()+".png";

        if (!StringUtils.isEmpty(photoURL)) {
            Picasso.with(App.getAppContext()).load(photoURL).placeholder(R.mipmap.apos).into(viewHolder.ivIcon);
        }

//        int imgValue = clsModuleImage.funGetImage(item.getImageName_strImageName());
//        viewHolder.ivIcon.setImageResource(imgValue);
        return convertView;
    }


    private class ViewHolder {
        ImageView ivIcon;

        public ViewHolder(View v) {
            ivIcon = ButterKnife.findById(v, R.id.ivmainmenu);
        }
    }
}
