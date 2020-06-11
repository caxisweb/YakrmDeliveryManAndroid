package com.codeclinic.yakrmdeliveryman.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.codeclinic.yakrmdeliveryman.Models.ForgetPasswordNumberModel;
import com.codeclinic.yakrmdeliveryman.Models.ForgotPasswordOTPModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    CardView main_detail_cardview, number_verify_cardview, main_change_pass_cardview;
    Button btn_send, btn_verify, btn_change_pass, btn_resend;
    ImageView img_back;
    EditText edt_number, edt_1, edt_2, edt_3, edt_4, edt_new_pass, edt_confm_new_pass;
    API apiService;
    ProgressDialog progressDialog;
    JSONObject jsonObject_register = new JSONObject();

    TextView tv_min, tv_sec;

    String str_otp, str_user_token, str_number, str_edt_1, str_edt_2, str_edt_3, str_edt_4, language;

    SessionManager sessionManager;

    CountDownTimer countDownTimer;


    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        main_detail_cardview = findViewById(R.id.main_detail_cardview);
        number_verify_cardview = findViewById(R.id.number_verify_cardview);
        main_change_pass_cardview = findViewById(R.id.main_change_pass_cardview);

        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        sessionManager = new SessionManager(this);


        img_back = findViewById(R.id.img_back);
        language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        apiService = RestClass.getClientDelivery().create(API.class);


        edt_number = findViewById(R.id.edt_number);
        edt_1 = findViewById(R.id.edt_1);
        edt_2 = findViewById(R.id.edt_2);
        edt_3 = findViewById(R.id.edt_3);
        edt_4 = findViewById(R.id.edt_4);
        edt_new_pass = findViewById(R.id.edt_new_pass);
        edt_confm_new_pass = findViewById(R.id.edt_confm_new_pass);

        tv_min = findViewById(R.id.tv_min);
        tv_sec = findViewById(R.id.tv_sec);

        btn_verify = findViewById(R.id.btn_verify);
        btn_send = findViewById(R.id.btn_send);
        btn_resend = findViewById(R.id.btn_resend);
        btn_change_pass = findViewById(R.id.btn_change_pass);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_number.getText().toString()
                        .replaceAll("1", getResources().getString(R.string.one));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_number = edt_number.getText().toString();
                if (isEmpty(str_number)) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } /*else if (!str_number.substring(0, 2).equals("05")) {
                    Toast.makeText(ForgetPasswordActivity.this, "Mobile number shoul start with '05' ", Toast.LENGTH_LONG).show();
                }*/ else if (str_number.length() < 10) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_register.put("mobile_no", str_number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ForgetPasswordNumberModel> forgetPasswordNumberModelCall = apiService.FORGET_PASSWORD_NUMBER_MODEL_CALL(jsonObject_register.toString());
                    forgetPasswordNumberModelCall.enqueue(new Callback<ForgetPasswordNumberModel>() {
                        @Override
                        public void onResponse(Call<ForgetPasswordNumberModel> call, Response<ForgetPasswordNumberModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("3")) {
                                str_otp = String.valueOf(response.body().getOtp());
                                str_user_token = String.valueOf(response.body().getToken());
                                main_detail_cardview.setVisibility(View.GONE);
                                number_verify_cardview.setVisibility(View.VISIBLE);
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                callCountDownTimer();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgetPasswordNumberModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_number = edt_number.getText().toString();
                if (isEmpty(str_number)) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } else if (str_number.length() < 10) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_register.put("mobile_no", str_number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ForgetPasswordNumberModel> forgetPasswordNumberModelCall = apiService.FORGET_PASSWORD_NUMBER_MODEL_CALL(jsonObject_register.toString());
                    forgetPasswordNumberModelCall.enqueue(new Callback<ForgetPasswordNumberModel>() {
                        @Override
                        public void onResponse(Call<ForgetPasswordNumberModel> call, Response<ForgetPasswordNumberModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("3")) {
                                str_otp = String.valueOf(response.body().getOtp());
                                str_user_token = String.valueOf(response.body().getToken());
                                main_detail_cardview.setVisibility(View.GONE);
                                number_verify_cardview.setVisibility(View.VISIBLE);
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                callCountDownTimer();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgetPasswordNumberModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        edt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_1.getText().toString().length() == 1) {
                    edt_2.requestFocus();
                    //editText2.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_2.getText().toString().length() == 1) {
                    edt_3.requestFocus();
                    //editText3.setText("");
                } else {
                    edt_1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_3.getText().toString().length() == 1) {
                    edt_4.requestFocus();
                    //editText4.setText("");
                } else {
                    edt_2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_4.getText().toString().length() == 1) {
                    //editText1.requestFocus();
                } else {
                    edt_3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_edt_1 = edt_1.getText().toString();
                str_edt_2 = edt_2.getText().toString();
                str_edt_3 = edt_3.getText().toString();
                str_edt_4 = edt_4.getText().toString();

                if (isEmpty(str_edt_1)) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Please_Enter_OTP), Toast.LENGTH_SHORT).show();

                } else if (isEmpty(str_edt_2)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_3)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_4)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else {
                    String temp_otp = null;
                    /*if (language.equals("ar")) {
                        temp_otp = str_edt_4 + str_edt_3 + str_edt_2 + str_edt_1;
                    } else {
                        temp_otp = str_edt_1 + str_edt_2 + str_edt_3 + str_edt_4;
                    }*/
                    temp_otp = str_edt_1 + str_edt_2 + str_edt_3 + str_edt_4;

                    if (temp_otp.equals(str_otp)) {
                        number_verify_cardview.setVisibility(View.GONE);
                        main_change_pass_cardview.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "OTP do not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(edt_new_pass.getText().toString())) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Enter_New_password), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(edt_confm_new_pass.getText().toString())) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.Confirm_new_password), Toast.LENGTH_SHORT).show();
                } else if (!edt_confm_new_pass.getText().toString().matches(edt_confm_new_pass.getText().toString())) {
                    Toast.makeText(ForgetPasswordActivity.this, getResources().getString(R.string.PasswordsNotMatch), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("password", edt_new_pass.getText().toString());
                        jsonObject.put("otp", str_otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<ForgotPasswordOTPModel> forgotPasswordOTPModelCall = apiService.FORGOT_PASSWORD_OTP_MODEL_CALL(str_user_token, jsonObject.toString());
                    forgotPasswordOTPModelCall.enqueue(new Callback<ForgotPasswordOTPModel>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordOTPModel> call, Response<ForgotPasswordOTPModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                finish();
                                String language = String.valueOf(getResources().getConfiguration().locale);
                                if (language.equals("ar")) {
                                    if (response.body().getArab_message() != null) {
                                        Toast.makeText(ForgetPasswordActivity.this, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordOTPModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void callCountDownTimer() {
        btn_resend.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                tv_min.setText(String.format("%02d:%02d", TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)), TimeUnit.SECONDS.toSeconds(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))));
            }

            public void onFinish() {

                btn_resend.setEnabled(true);
                tv_min.setText("00:00");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
