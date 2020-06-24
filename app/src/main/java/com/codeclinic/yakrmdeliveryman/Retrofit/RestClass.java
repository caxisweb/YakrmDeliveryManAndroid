package com.codeclinic.yakrmdeliveryman.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by bhatt on 6/20/2017.
 */

public class RestClass {

    public static String ImageBaseURL = "https://test.yakrm.com/assets/uploads/order_images/";
    //public static String ImageBaseURL = "https://yakrm.com/assets/uploads/order_images/";
    //public static final String BASE_URL = "https://www.yakrm.com/api/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
    public static final String BASE_URL = "http://test.yakrm.com/api/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
    private static Retrofit retrofit = null;

    //private static final String SALESMAN_BASE_URL = "http://yakrm.com/api_salesmen/";
    private static final String SALESMAN_BASE_URL = "http://test.yakrm.com/api_salesmen/";
    private static Retrofit retrofit_salesman = null;

    public static final String Delivery_BASE_URL = "http://test.yakrm.com/apis/v1/";
    //public static final String Delivery_BASE_URL = "http://yakrm.com/apis/v1/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
    private static Retrofit retrofit_delivery = null;

    public static Retrofit getSalesmanClient() {
        if (retrofit_salesman == null) {
            retrofit_salesman = new Retrofit.Builder()
                    .baseUrl(SALESMAN_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit_salesman;
    }

    public static Retrofit getClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientDelivery() {

        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        if (retrofit_delivery == null) {

            retrofit_delivery = new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(Delivery_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit_delivery;
    }

}
