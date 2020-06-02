package com.codeclinic.yakrmdeliveryman.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codeclinic.yakrmdeliveryman.Models.LoginModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    CardView main_login_cardview;
    Button btn_log_in;
    ImageView img_back;
    ProgressDialog progressDialog;

    EditText edt_email, edt_password;
    TextView tv_signup, tv_forget_pass;

    API apiService;
    JSONObject jsonObject = new JSONObject();

    String str_email, str_password;
    SessionManager sessionManager;
    String notification_token="1";


    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);

        main_login_cardview = findViewById(R.id.main_login_cardview);

        sessionManager = new SessionManager(this);

        btn_log_in = findViewById(R.id.btn_log_in);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        tv_signup = findViewById(R.id.tv_signup);
        tv_forget_pass = findViewById(R.id.tv_forget_pass);

        tv_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        apiService = RestClass.getClientDelivery().create(API.class);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                notification_token = instanceIdResult.getToken();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_email = edt_email.getText().toString();
                str_password = edt_password.getText().toString();

                if (isEmpty(str_email)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } else if (!str_email.substring(0, 2).equals("05")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Mobile_number_should_start_with_05), Toast.LENGTH_LONG).show();
                } else if (str_email.length() < 10) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_password)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Please_Enter_Password), Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {

                        jsonObject.put("mobile_no", str_email);
                        jsonObject.put("password", str_password);
                        jsonObject.put("device_type", "1");
                        jsonObject.put("notification_token", notification_token);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<LoginModel> loginModelCall = apiService.LOGIN_MODEL_CALL(jsonObject.toString());
                    loginModelCall.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            progressDialog.dismiss();

                            if (response.body().getStatus().equals("1")) {

                                sessionManager.createLoginSession(response.body().getToken(), response.body().getId(), response.body().getName(), response.body().getEmail(), response.body().getMobileNo());//else salesmen
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                finish();

                            } else if (response.body().getStatus().equals("2")) {

                                Intent i_otp = new Intent(LoginActivity.this, NewAccountActivity.class);
                                i_otp.putExtra("token", response.body().getToken());
                                startActivity(i_otp);
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, NewAccountActivity.class));
                Intent i_otp = new Intent(LoginActivity.this, NewAccountActivity.class);
                i_otp.putExtra("token", "0");
                startActivity(i_otp);
            }
        });
    }
}
