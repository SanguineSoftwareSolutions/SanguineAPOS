package com.example.apos.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsDirectBill;
import com.example.apos.bean.clsDirectBillItemsListBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.listeners.clsDirectBillerItemListSelectionListener;

import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;


/**
 * Created by BeWo Tech-4 on 09-07-2015.
 */
public class clsDirectBillItemsListGridViewAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsDirectBillItemsListGridViewAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsDirectBillItemsListBean> listItemsListData;
    private clsDirectBillerItemListSelectionListener itemListSelectionListener;

    public clsDirectBillItemsListGridViewAdapter(Context context, Activity activity, List<clsDirectBillItemsListBean> itemsListData, clsDirectBillerItemListSelectionListener mItemListSelectionListener) {
        this.context = context;
        this.mActivity = activity;
        this.listItemsListData = itemsListData;
        Log.i(TAG, "Size of the item lists in clsDirectBillItemsListGridViewAdapter : " + listItemsListData.size());
        this.itemListSelectionListener = mItemListSelectionListener;

    }



    @Override
    public int getCount() {
        return listItemsListData.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemsListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listItemsListData.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        View rowView;
        LayoutInflater inflater = LayoutInflater.from(context);
        rowView = inflater.inflate(R.layout.direct_bill_items_list_grid_row, null);
        holder.tvItemListName =(TextView) rowView.findViewById(R.id.tv_direct_bill_items_list_name);
        holder.tvItemListAmount = (TextView) rowView.findViewById(R.id.tv_direct_bill_items_list_amount);


        final clsDirectBillItemsListBean itemListBean = (clsDirectBillItemsListBean) getItem(position);

        if(itemListBean.getStrItemName() != null){
            holder.tvItemListName.setText(itemListBean.getStrItemName());
        }

        if(itemListBean.getDblSalePrice() != -1){
            holder.tvItemListAmount.setText(itemListBean.getDblSalePrice() + " Rs");
        }
/*
        if(itemListBean.getBtItemImage() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(itemListBean.getBtItemImage(), 0, itemListBean.getBtItemImage().length);
            holder.ivItemListImage.setImageBitmap(bitmap);
        }*/

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if(itemListBean.getDblSalePrice()>0)
                {
                    clsDirectBillItemsListBean itemListBeanTemp = (clsDirectBillItemsListBean) getItem(position);
                    itemListSelectionListener.getItemsListSelectedForOder(itemListBeanTemp.getStrItemCode(), itemListBeanTemp.getStrItemName(),
                            itemListBeanTemp.getStrSubGroupCode(),itemListBeanTemp.getDblSalePrice());
                }
                else
                {
                    final Dialog dialog = new Dialog(context);
                    //  hideDefaultKeyboard();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialogforinput);

                    final EditText edtInputTypeValue = (EditText) dialog.findViewById(R.id.edtInputTypeValue);
                    edtInputTypeValue.setInputType(TYPE_CLASS_NUMBER);
                    CommonUtils.showKeyboard(edtInputTypeValue,true);
                    final Button btnOK = (Button) dialog.findViewById(R.id.btnOk);
                    TextView txtInputName = (TextView) dialog.findViewById(R.id.txtInputName);
                    TextView txtInputType=(TextView)dialog.findViewById(R.id.textInputName);

                    txtInputName.setText("Enter Rate");
                    txtInputType.setText("Enter Rate");

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {
                            if(edtInputTypeValue.getText().toString().isEmpty())
                            {
                                Toast.makeText(mActivity,"Enter rate",Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                clsDirectBillItemsListBean itemListBeanTemp = (clsDirectBillItemsListBean) getItem(position);
                                itemListSelectionListener.getItemsListSelectedForOder(itemListBeanTemp.getStrItemCode(), itemListBeanTemp.getStrItemName(),
                                        itemListBeanTemp.getStrSubGroupCode(),Double.valueOf(edtInputTypeValue.getText().toString()));
                                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.getWindowManager().getDefaultDisplay();
                }
            }
        });

        return rowView;
    }

    private class ViewHolder{
        ImageView ivItemListImage;
        TextView tvItemListName;
        TextView tvItemListAmount;
    }
}