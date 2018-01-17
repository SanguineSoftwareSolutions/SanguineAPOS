package com.example.apos.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apos.bean.clsMainMenuBean;
import com.example.apos.util.clsModuleImage;

import java.util.ArrayList;

public class clsKotMenu extends BaseAdapter {

    private Context mContext;
    ArrayList mainMenuList = null;

    public clsKotMenu (Context c, ArrayList mainMenuList) {
        mContext = c;
        this.mainMenuList = mainMenuList;
    }

    public int getCount() {
        return mainMenuList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            clsMainMenuBean obj = (clsMainMenuBean)mainMenuList.get(position);
            obj.getModuleName_strModuleName();
            obj.getImageName_strImageName();
            clsModuleImage clsModuleImage =new clsModuleImage();

            int imgValue= clsModuleImage.funGetImage(obj.getImageName_strImageName());
            textView = new TextView(mContext);

            textView.setBackgroundResource(imgValue);

        } else {
            textView = (TextView)convertView;
        }

        return textView;
    }
}
