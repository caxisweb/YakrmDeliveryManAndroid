package com.codeclinic.yakrmdeliveryman.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrmdeliveryman.Activity.LoginActivity;
import com.codeclinic.yakrmdeliveryman.Models.NotificationCountModel;
import com.codeclinic.yakrmdeliveryman.Models.OrderListModel;
import com.codeclinic.yakrmdeliveryman.Models.OrderlistResponseModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.codeclinic.yakrmdeliveryman.adepter.MyOrderAdepter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codeclinic.yakrmdeliveryman.Activity.MainActivity.textnotificationCount;

public class OrderlistFragment extends Fragment {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    API apiService;

    String order_status;
    List<OrderListModel> myorderlist = new ArrayList<>();

    private View mainView;

    RecyclerView rc_orderlist;

    public OrderlistFragment(String order_status){
        this.order_status=order_status;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.my_order_fragment, null);

        sessionManager = new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        apiService = RestClass.getClientDelivery().create(API.class);

        Log.i("tocken", sessionManager.getUserDetails().get(SessionManager.User_Token));

        rc_orderlist = mainView.findViewById(R.id.rc_orderlist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rc_orderlist.setLayoutManager(layoutManager);
        rc_orderlist.setHasFixedSize(true);
        rc_orderlist.setNestedScrollingEnabled(true);

        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {

            JSONObject data=new JSONObject();
            data.put("status",order_status);

            Call<OrderlistResponseModel> getOrderList = apiService.getOrderList(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            getOrderList.enqueue(new Callback<OrderlistResponseModel>() {
                @Override
                public void onResponse(Call<OrderlistResponseModel> call, Response<OrderlistResponseModel> response) {

                    progressDialog.dismiss();

                    if (response.body().getStatus().equals("1")) {

                        myorderlist = response.body().getOrderlist();

                        MyOrderAdepter adepter = new MyOrderAdepter(myorderlist, getActivity());
                        rc_orderlist.setAdapter(adepter);
                        getNotificationCount();


                    } else {
                        if (sessionManager.getLanguage("Language", "en").equals("en")) {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<OrderlistResponseModel> call, Throwable t) {

                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "server error", Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mainView;
    }

    void getNotificationCount(){

        Log.i("user_Token",sessionManager.getUserDetails().get(SessionManager.User_Token));
        Call<NotificationCountModel> getOrderList=apiService.NOTIFICATION_COUNT(sessionManager.getUserDetails().get(SessionManager.User_Token));
        getOrderList.enqueue(new Callback<NotificationCountModel>() {
            @Override
            public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {


                if(response.body().getStatus().equals("1")){
                    textnotificationCount.setText(String.valueOf(response.body().getTotalNoti()));
                }else{

                }

            }

            @Override
            public void onFailure(Call<NotificationCountModel> call, Throwable t) {

            }
        });
    }
}
