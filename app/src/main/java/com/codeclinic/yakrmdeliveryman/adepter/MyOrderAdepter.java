package com.codeclinic.yakrmdeliveryman.adepter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codeclinic.yakrmdeliveryman.Activity.OrderDetailActivity;
import com.codeclinic.yakrmdeliveryman.Models.OrderListModel;
import com.codeclinic.yakrmdeliveryman.R;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class MyOrderAdepter extends RecyclerView.Adapter<MyOrderAdepter.Holder> {

    List<OrderListModel> myorderlist;
    Activity context;

    public MyOrderAdepter(List<OrderListModel> myorderlist, Activity context) {
        this.myorderlist = myorderlist;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custome_orderlist_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

        holder.tv_order_id.setText(context.getString(R.string.order_id)+" : "+myorderlist.get(i).getId());

        if(myorderlist.get(i).getOrder_status().equals("1")) {
            holder.tv_order_status.setText(context.getString(R.string.pending));
        }else{
            holder.tv_order_status.setText(context.getString(R.string.accept));
        }

        holder.tv_product_count.setText(context.getString(R.string.product)+" : "+myorderlist.get(i).getTotal_products());
        holder.tv_home_address.setText(myorderlist.get(i).getUserAddress());

        if(isEmpty(myorderlist.get(i).getShopAddress())) {
            holder.lv_sotre_address.setVisibility(View.GONE);
        }else {
            holder.lv_sotre_address.setVisibility(View.VISIBLE);
            holder.tv_store_address.setText(myorderlist.get(i).getShopAddress());
        }

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent i_detail=new Intent(context, OrderDetailActivity.class);
                    i_detail.putExtra("order_id",myorderlist.get(i).getId());
                    context.startActivity(i_detail);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return myorderlist.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView tv_order_id,tv_order_status,tv_product_count,tv_home_address,tv_store_address;
        Button btn_detail;
        LinearLayout lv_sotre_address;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tv_order_id=itemView.findViewById(R.id.tv_order_id);
            tv_order_status=itemView.findViewById(R.id.tv_order_status);
            tv_product_count=itemView.findViewById(R.id.tv_product_count);
            tv_home_address=itemView.findViewById(R.id.tv_home_address);
            tv_store_address=itemView.findViewById(R.id.tv_store_address);
            lv_sotre_address=itemView.findViewById(R.id.lv_store_address);
            btn_detail=itemView.findViewById(R.id.btn_detail);
        }
    }
}
