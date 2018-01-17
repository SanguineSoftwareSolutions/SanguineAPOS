package com.example.apos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apos.activity.R;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.fragments.clsCustomerOrderItemFragment;

import java.util.List;


public class clsOrderCartAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    customLayoutListener layoutListner;
    double totBill=0;
    int count=0;
    List<clsKotItemsListBean> itemList;

    public interface customLayoutListener
    {
        public void onLayoutClickListener(int position, String value);
    }
    public void setCustomLayoutListener(customLayoutListener listener)
    {
        this.layoutListner=listener;
    }
    public clsOrderCartAdapter(Activity context, List<clsKotItemsListBean> itemList) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity=context;
        this.itemList=itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(count>=itemList.size())
        {
            count=0;
            totBill=0;
           // clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.txtTotalBillAmt.setText(String.valueOf(totBill));
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view= View.inflate(activity, R.layout.customerordercartitemlist,null);
        TextView txtItemName= (TextView)view.findViewById(R.id.textViewItemName1);
        final EditText txtItemQnt= (EditText)view.findViewById(R.id.textqunt);
        final TextView txtItemAmt= (TextView)view.findViewById(R.id.textamt);
        LinearLayout llItemListLayout=(LinearLayout)view.findViewById(R.id.cartview);
        ImageView imgDel=(ImageView)view.findViewById(R.id.imgdel);
        ImageView imgMinus=(ImageView)view.findViewById(R.id.imgMinus);
        ImageView imgPlus=(ImageView)view.findViewById(R.id.imgPlus);
        txtItemName.setText(itemList.get(position).getStrItemName());
        txtItemQnt.setText(itemList.get(position).getStrItemQty());
        txtItemAmt.setText(String.valueOf(itemList.get(position).getDblSalePrice()));
        final String text=itemList.get(position).getStrItemCode();
        txtItemQnt.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        txtItemQnt.setEnabled(false);


        totBill+=itemList.get(position).getDblSalePrice();
        //clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.txtTotalBillAmt.setText(String.valueOf(totBill));

        /* double tot=0;
        for(int i=0;i<itemList.size();i++)
        {
            tot=Double.parseDouble(itemList.get(i).getStrTotalPrice());
            tot+=tot;
        }*/
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutListner != null)
                {
                    itemList.get(position).setStrItemQty("1");
                    double tot=Double.parseDouble((String)clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.txtTotalBillAmt.getText());
                    tot=tot-(itemList.get(position).getDblSalePrice()*Double.parseDouble(itemList.get(position).getStrItemQty()));
                    layoutListner.onLayoutClickListener(position,itemList.get(position).getStrItemCode());
                    clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.txtTotalBillAmt.setText(String.valueOf(tot));
                }
            }
        });

        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutListner != null)
                {
                    double itemPrice= itemList.get(position).getDblSalePrice();
                    double qty= Double.parseDouble(itemList.get(position).getStrItemQty());
                    double itemRate=itemPrice/qty;
                    if(qty>0)
                    {
                        qty=qty-1;
                        if(qty==0)
                        {
                            itemList.get(position).setStrItemQty("1");
                            layoutListner.onLayoutClickListener(position,itemList.get(position).getStrItemCode());
                        }
                        Long L = Math.round(qty);
                        int quantity = Integer.valueOf(L.intValue());
                        itemList.get(position).setStrItemQty(String.valueOf(quantity));
                        itemList.get(position).setDblSalePrice(itemRate*quantity);
                        txtItemQnt.setText(String.valueOf(quantity));
                        txtItemAmt.setText(String.valueOf(itemList.get(position).getDblSalePrice()));


                        double tot=0;
                        for(int i=0;i<itemList.size();i++)
                        {
                            double tot1=itemList.get(i).getDblSalePrice();
                            System.out.println("tot: "+i+" "+tot);
                            tot=tot+tot1;
                            clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.txtTotalBillAmt.setText(String.valueOf(tot));
                        }
                    }
                    else if(qty==0)
                    {
                        itemList.get(position).setStrItemQty("1");
                        layoutListner.onLayoutClickListener(position,itemList.get(position).getStrItemCode());
                    }
                }
            }
        });


        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layoutListner != null)
                {
                    double itemPrice= itemList.get(position).getDblSalePrice();
                    double qty= Double.parseDouble(itemList.get(position).getStrItemQty());
                    double itemRate=itemPrice/qty;
                    if(qty>0)
                    {
                        qty=qty+1;
                        Long L = Math.round(qty);
                        int quantity = Integer.valueOf(L.intValue());
                        itemList.get(position).setStrItemQty(String.valueOf(quantity));
                        itemList.get(position).setDblSalePrice(itemRate*quantity);
                        txtItemQnt.setText(String.valueOf(quantity));
                        txtItemAmt.setText(String.valueOf(itemList.get(position).getDblSalePrice()));


                        double tot=0;
                        for(int i=0;i<itemList.size();i++)
                        {
                            double tot1=itemList.get(i).getDblSalePrice();
                            System.out.println("tot: "+i+" "+tot);
                            tot=tot+tot1;
                            clsCustomerOrderItemFragment.clsCustomerOrderViewCartFragment.txtTotalBillAmt.setText(String.valueOf(tot));
                        }
                    }
                    else if(qty==0)
                    {
                        itemList.get(position).setStrItemQty("1");
                        layoutListner.onLayoutClickListener(position,itemList.get(position).getStrItemCode());
                    }
                }
            }
        });




        count=count+1;
        return view;
    }

}
