package com.example.apos.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.example.apos.activity.clsKotScreen;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.listeners.clsKotItemListSelectionListener;

import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;

/**
 * Created by Prashant on 9/12/2015.
 */
public class clsKotItemsListGridViewAdapter extends BaseAdapter {

    private static String TAG = "BeWo_Restaurant_" + clsKotItemsListGridViewAdapter.class.getName();
    private Context context;
    private Activity mActivity;
    List<clsKotItemsListBean> listItemsListData;
    //private static LayoutInflater inflater=null;
    private clsKotItemListSelectionListener itemListSelectionListener;

    public clsKotItemsListGridViewAdapter (Context context, Activity activity, List<clsKotItemsListBean> itemsListData, clsKotItemListSelectionListener mItemListSelectionListener) {
        this.context = context;
        this.mActivity = activity;
        this.listItemsListData = itemsListData;
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
        rowView = inflater.inflate(R.layout.kot_items_list_grid_row, null);
        holder.tvItemListName =(TextView) rowView.findViewById(R.id.tv_kot_items_list_name);
        holder.tvItemListAmount = (TextView) rowView.findViewById(R.id.tv_kot_items_list_amount);


        final clsKotItemsListBean itemListBean = (clsKotItemsListBean) getItem(position);

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

        rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if(itemListBean.getDblSalePrice()>0)
                {
                    clsKotItemsListBean itemListBeanTemp = (clsKotItemsListBean) getItem(position);
                    itemListSelectionListener.getItemsListSelectedForOder(itemListBeanTemp.getStrItemCode(), itemListBeanTemp.getStrItemName(),
                            itemListBeanTemp.getStrSubGroupCode(), itemListBeanTemp.getDblSalePrice());
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
                                // dialog.dismiss();
                                Toast.makeText(mActivity,"Enter rate",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                clsKotItemsListBean itemListBeanTemp = (clsKotItemsListBean) getItem(position);
                                itemListSelectionListener.getItemsListSelectedForOder(itemListBeanTemp.getStrItemCode(), itemListBeanTemp.getStrItemName(),
                                        itemListBeanTemp.getStrSubGroupCode(), Double.valueOf(edtInputTypeValue.getText().toString()));
                                new CommonUtils().hideKeyboard(edtInputTypeValue);
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