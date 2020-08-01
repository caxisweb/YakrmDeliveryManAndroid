package com.codeclinic.yakrmdeliveryman.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.codeclinic.yakrmdeliveryman.Models.AboutApplicationModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.Connection_Detector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutApplicationActivity extends AppCompatActivity {

    ImageView img_back;
    TextView tv_about;
    API apiService;
    ProgressDialog progressDialog;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        img_back = findViewById(R.id.img_back);
        tv_about = findViewById(R.id.tv_about);
        language = String.valueOf(getResources().getConfiguration().locale);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(this);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<AboutApplicationModel> aboutApplicationModelCall = apiService.ABOUT_APPLICATION_MODEL_CALL();
            aboutApplicationModelCall.enqueue(new Callback<AboutApplicationModel>() {
                @Override
                public void onResponse(Call<AboutApplicationModel> call, Response<AboutApplicationModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (language.equals("ar")) {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationArab(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationEnglish(), Html.FROM_HTML_MODE_COMPACT));
                            }
                        } else {
                            if (language.equals("ar")) {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationArab()));
                            } else {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationEnglish()));
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutApplicationModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AboutApplicationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
