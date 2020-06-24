package com.codeclinic.yakrmdeliveryman.Retrofit;


import com.codeclinic.yakrmdeliveryman.Models.ForgetPasswordNumberModel;
import com.codeclinic.yakrmdeliveryman.Models.ForgotPasswordOTPModel;
import com.codeclinic.yakrmdeliveryman.Models.LoginModel;
import com.codeclinic.yakrmdeliveryman.Models.NotificationCountModel;
import com.codeclinic.yakrmdeliveryman.Models.NotificationListModel;
import com.codeclinic.yakrmdeliveryman.Models.OrderDetailResponseModel;
import com.codeclinic.yakrmdeliveryman.Models.OrderStatusChange;
import com.codeclinic.yakrmdeliveryman.Models.OrderlistResponseModel;
import com.codeclinic.yakrmdeliveryman.Models.RegistrationModel;
import com.codeclinic.yakrmdeliveryman.Models.UpdateLocationModel;
import com.codeclinic.yakrmdeliveryman.Models.UpdatePaymentModel;
import com.codeclinic.yakrmdeliveryman.Models.VerifyOTPModel;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {

    @Multipart
    @POST("delivery_boy/register")
    Call<RegistrationModel> REGISTER(@Part("name") RequestBody name,
                                     @Part("email") RequestBody email,
                                     @Part("mobile_no") RequestBody MobileNo,
                                     @Part("password") RequestBody degree,
                                     @Part("device_type") RequestBody pri_name,
                                     @Part("notification_token") RequestBody token);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/otpverification")
    Call<VerifyOTPModel> VERIFY_OTP_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/signin")
    Call<LoginModel> LOGIN_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/forget_password/checked_mobile")
    Call<ForgetPasswordNumberModel> FORGET_PASSWORD_NUMBER_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/forget_password/verify_otp_reset_password")
    Call<ForgotPasswordOTPModel> FORGOT_PASSWORD_OTP_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/orders/detail")
    Call<OrderDetailResponseModel> OrderDetail(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/orders/get_order_list_statuswise")
    Call<OrderlistResponseModel> getOrderList(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/orders/accept_new_order")
    Call<OrderStatusChange> orderStatusChange(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/orders/pickup_order")
    Call<OrderStatusChange> orderDispatch(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/orders/complete_order")
    Call<OrderStatusChange> orderDilivered(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/orders/update_order_price_bydeliveryboy")
    Call<UpdatePaymentModel> updatePayment(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("delivery_boy/profile/update_lat_long")
    Single<UpdateLocationModel> updateLocation(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @GET("delivery_boy/notification/list")
    Call<NotificationListModel> NOTIFICATION_LIST_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @GET("delivery_boy/notification/list")
    Call<NotificationCountModel> NOTIFICATION_COUNT(@Header("Authorization") String header);

    /*@Headers("Content-Type: application/json")
    @POST("registration_step_1")
    Call<RegistrationModel> REGISTRATION_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @GET("users/orders/my_orders")
    Call<OrderlistResponseModel> GetOrderList(@Header("Authorization") String header);*/

}
