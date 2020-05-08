package com.codeclinic.yakrmdeliveryman.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codeclinic.yakrmdeliveryman.Models.OrderDetailResponseModel;
import com.codeclinic.yakrmdeliveryman.Models.OrderStatusChange;
import com.codeclinic.yakrmdeliveryman.Models.UpdatePaymentModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class OrderDetailActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SessionManager sessionManager;
    API apiService;

    String order_id;
    String str_home_lat,str_home_long,str_shop_lat,str_shop_long;
    double total_amount;

    CardView card_image;
    TextView tv_order_id,tv_order_status,tv_product_count,tv_home_address,tv_store_address,tv_notes;
    TextView tv_product_cost,tv_servicetax,tv_delivery_charge,tv_total_cost;
    TextView tv_delivery_boy,tv_delivery_contact;
    LinearLayout lv_productlist,lv_payment_detail,lv_payment_add,lv_payment_status,lv_footer;
    LinearLayout lv_notes,lv_home_address,lv_store_address;
    ImageView img_product;
    ImageView img_back;
    EditText edt_amount;
    Button btn_accept,btn_payment,btn_chat,btn_complete,btn_dispatch;
    CardView btn_home_address,btn_shop_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        Bundle b=getIntent().getExtras();
        order_id=b.getString("order_id");

        img_back=findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        apiService = RestClass.getClientDelivery().create(API.class);
        final LayoutInflater inflater = LayoutInflater.from(this);

        Log.i("token",sessionManager.getUserDetails().get(SessionManager.User_Token));

        tv_order_id=findViewById(R.id.tv_order_id);
        tv_order_status=findViewById(R.id.tv_order_status);
        tv_product_count=findViewById(R.id.tv_product_count);
        tv_home_address=findViewById(R.id.tv_home_address);
        tv_store_address=findViewById(R.id.tv_shop_address);
        tv_notes=findViewById(R.id.tv_notes);

        tv_product_cost=findViewById(R.id.tv_product_cost);
        tv_servicetax=findViewById(R.id.tv_service_tax);
        tv_delivery_charge=findViewById(R.id.tv_delivery_charge);
        tv_total_cost=findViewById(R.id.tv_total_cost);

        tv_delivery_boy=findViewById(R.id.tv_delivery_boy);
        tv_delivery_contact=findViewById(R.id.tv_delivery_contact);

        edt_amount=findViewById(R.id.edt_amount);

        lv_productlist=findViewById(R.id.lv_productlist);
        lv_payment_detail=findViewById(R.id.lv_payment_detail);
        lv_payment_add=findViewById(R.id.lv_payment_add);
        lv_payment_status=findViewById(R.id.lv_payment_status);
        lv_footer=findViewById(R.id.lv_footer);

        lv_notes=findViewById(R.id.lv_notes);
        lv_home_address=findViewById(R.id.lv_home_address);
        lv_store_address=findViewById(R.id.lv_shop_address);

        card_image = findViewById(R.id.card_image);
        img_product=findViewById(R.id.img_product);

        btn_accept=findViewById(R.id.btn_accept);
        btn_payment=findViewById(R.id.btn_addpayment);
        btn_chat=findViewById(R.id.btn_chat);
        btn_complete=findViewById(R.id.btn_complete);
        btn_dispatch=findViewById(R.id.btn_dispatch);

        btn_home_address=findViewById(R.id.btn_home_address);
        btn_shop_address=findViewById(R.id.btn_shop_address);

        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);

            Call<OrderDetailResponseModel> orderDetail=apiService.OrderDetail(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            orderDetail.enqueue(new Callback<OrderDetailResponseModel>() {
                @Override
                public void onResponse(Call<OrderDetailResponseModel> call, Response<OrderDetailResponseModel> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){

                        tv_order_id.setText(getString(R.string.order_id)+" : "+response.body().getId());
                        tv_product_count.setText(getString(R.string.product)+" : "+response.body().getOrderDetail().size());

                        if(response.body().getShopAddress()==null){
                            lv_store_address.setVisibility(View.GONE);
                        }else{
                            lv_store_address.setVisibility(View.VISIBLE);
                            tv_store_address.setText(response.body().getShopAddress());
                            str_shop_lat=response.body().getShopLatitude();
                            str_shop_long=response.body().getShopLongitude();
                        }

                        str_home_lat=response.body().getUserLatitude();
                        str_home_long=response.body().getUserLongitude();

                        tv_home_address.setText(response.body().getUserAddress());


                        if(response.body().getPrice().equals("0")){
                            lv_payment_add.setVisibility(View.VISIBLE);
                            btn_payment.setVisibility(View.VISIBLE);
                        }else {
                            lv_payment_detail.setVisibility(View.VISIBLE);
                            tv_product_cost.setText(response.body().getPrice()+ getString(R.string.Sr));
                            tv_servicetax.setText(response.body().getService_charge()+ getString(R.string.Sr));
                            tv_delivery_charge.setText(response.body().getOrder_charge()+getString(R.string.Sr));
                            total_amount=Double.parseDouble(response.body().getPrice())+Double.parseDouble(response.body().getService_charge())+Double.parseDouble(response.body().getOrder_charge());
                            tv_total_cost.setText(total_amount +getString(R.string.Sr));
                        }

                        if(response.body().getOrder_status().equals("1")){
                            tv_order_status.setText(getString(R.string.pending));
                            lv_payment_detail.setVisibility(View.GONE);
                            btn_accept.setVisibility(View.VISIBLE);
                            btn_payment.setVisibility(View.GONE);
                            btn_chat.setVisibility(View.GONE);
                            edt_amount.setEnabled(false);

                        }else if(response.body().getOrder_status().equals("5")){

                            tv_order_status.setText(getString(R.string.delivered));
                            btn_payment.setVisibility(View.GONE);
                            btn_chat.setVisibility(View.GONE);
                            lv_footer.setVisibility(View.GONE);

                        }else if(response.body().getOrder_status().equals("6")){

                            tv_order_status.setText(getString(R.string.button_cancel));
                            btn_payment.setVisibility(View.GONE);
                            btn_chat.setVisibility(View.GONE);
                            lv_footer.setVisibility(View.GONE);

                        } else {

                            if(response.body().getOrder_status().equals("2") && response.body().getIs_payment_complete().equals("1")){
                                btn_dispatch.setVisibility(View.VISIBLE);
                            }else if(response.body().getOrder_status().equals("4") && response.body().getIs_payment_complete().equals("1")){
                                tv_order_status.setText(getString(R.string.dispatch));
                                btn_complete.setVisibility(View.VISIBLE);
                            }
                            else {
                                tv_order_status.setText(getString(R.string.accept));
                            }

                            btn_accept.setVisibility(View.GONE);
                            btn_chat.setVisibility(View.VISIBLE);
                            edt_amount.setEnabled(true);

                            if(response.body().getIs_payment_complete().equals("1")){
                                btn_payment.setVisibility(View.GONE);
                                lv_payment_status.setVisibility(View.VISIBLE);
                            }else{

                                btn_complete.setVisibility(View.GONE);
                                btn_payment.setVisibility(View.VISIBLE);
                                lv_payment_status.setVisibility(View.GONE);
                            }
                        }

                        if(response.body().getNotes()==null){
                            //tv_notes.setText(response.body().getNotes());
                            lv_notes.setVisibility(View.GONE);
                        }else{
                            tv_notes.setText(response.body().getNotes());
                        }

                        for(int i=0;i<response.body().getOrderDetail().size();i++){

                            View custLayout = inflater.inflate(R.layout.custome_myproductlist_view, null, false);

                            TextView tv_product = (TextView) custLayout.findViewById(R.id.tv_productname);
                            TextView tv_qty = (TextView) custLayout.findViewById(R.id.tv_qty);

                            tv_product.setText(response.body().getOrderDetail().get(i).getProductTitle());
                            tv_qty.setText(response.body().getOrderDetail().get(i).getQuantity());

                            lv_productlist.addView(custLayout);
                        }

                        tv_delivery_boy.setText(getString(R.string.name) + " : " + response.body().getName());
                        tv_delivery_contact.setText(getString(R.string.contact) + " : " + response.body().getPhone());
                        //Log.i("image",ImageURL.produtList +response.body().getOrder_image());

                        if(isEmpty(response.body().getOrder_image())){
                            card_image.setVisibility(View.GONE);
                        }else{
                            card_image.setVisibility(View.VISIBLE);
                            Picasso.with(OrderDetailActivity.this).load(RestClass.ImageBaseURL+response.body().getOrder_image()).into(img_product);
                        }

                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderDetailResponseModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOrderAccept();
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount=edt_amount.getText().toString().trim();

                if(amount.equals("")){
                    edt_amount.setError("Please enter Amount");
                }else{
                    callupdatePayment(amount);
                }

            }
        });

        btn_home_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_home_lat + "," + str_home_long);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        btn_shop_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_shop_lat + "," + str_shop_long);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        btn_dispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOrderDispatch();
            }
        });

        btn_dispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOrderDispatch();
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callOrderDilivered();
            }
        });
    }

    void callOrderAccept(){

        try {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);

            Call<OrderStatusChange> orderStatusChange=apiService.orderStatusChange(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            orderStatusChange.enqueue(new Callback<OrderStatusChange>() {
                @Override
                public void onResponse(Call<OrderStatusChange> call, Response<OrderStatusChange> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){
                        finish();
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<OrderStatusChange> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void callupdatePayment(String amount){

        try {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);
            data.put("price",amount);

            Call<UpdatePaymentModel> updatePayment=apiService.updatePayment(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            updatePayment.enqueue(new Callback<UpdatePaymentModel>() {
                @Override
                public void onResponse(Call<UpdatePaymentModel> call, Response<UpdatePaymentModel> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){
                        finish();
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UpdatePaymentModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void callOrderDispatch(){

        try {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);

            Call<OrderStatusChange> orderStatusChange=apiService.orderDispatch(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            orderStatusChange.enqueue(new Callback<OrderStatusChange>() {
                @Override
                public void onResponse(Call<OrderStatusChange> call, Response<OrderStatusChange> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){
                        finish();
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<OrderStatusChange> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void callOrderDilivered(){

        try {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);

            Call<OrderStatusChange> orderStatusChange=apiService.orderDilivered(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            orderStatusChange.enqueue(new Callback<OrderStatusChange>() {
                @Override
                public void onResponse(Call<OrderStatusChange> call, Response<OrderStatusChange> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){
                        finish();
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<OrderStatusChange> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
