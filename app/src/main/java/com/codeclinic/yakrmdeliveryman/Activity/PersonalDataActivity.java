package com.codeclinic.yakrmdeliveryman.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.codeclinic.yakrmdeliveryman.Models.CountryListItemModel;
import com.codeclinic.yakrmdeliveryman.Models.CountryListModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.Connection_Detector;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivity {
    ImageView img_back;
    private final int CAMERA_IMAGE = 1;
    Button btn_modify_data;
    private final int PICK_IMAGE_GALLERY = 3;
    RoundedImageView img_profile;
    TextView tv_mobile, tv_email, tv_username, tv_name, tv_change_pass;
    RelativeLayout rl_imgprofile;
    Spinner sp_country;

    JSONObject jsonObject = new JSONObject();
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    API apiService;
    String str_country_code, user_id, user_name, user_token, user_email, user_number, user_profile, user_country_id, wallet, userType;

    Uri selectedImage;
    File sourceFile_sign, compressed_Image;
    boolean value;
    Compressor compressedImage;
    String str_email_regex = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    List<CountryListItemModel> arrayList_country = new ArrayList<>();
    ArrayList<String> arrayList_country_name = new ArrayList<>();
    ArrayList<String> arrayList_country_id = new ArrayList<>();

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());
        //compressedImage = new Compressor(this);

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(PersonalDataActivity.this);
        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_profile = findViewById(R.id.img_profile);
        rl_imgprofile = findViewById(R.id.rl_imgprofile);

        sp_country = findViewById(R.id.sp_country);

        btn_modify_data = findViewById(R.id.btn_modify_data);
        apiService = RestClass.getClient().create(API.class);

        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        tv_username = findViewById(R.id.tv_username);
        tv_name = findViewById(R.id.tv_name);
        tv_change_pass = findViewById(R.id.tv_change_pass);

        tv_mobile.setText(sessionManager.getUserDetails().get(SessionManager.USER_MOBILE));
        tv_email.setText(sessionManager.getUserDetails().get(SessionManager.User_Email));
        tv_name.setText(sessionManager.getUserDetails().get(SessionManager.User_Name));
        if (!isEmpty(sessionManager.getUserDetails().get(SessionManager.USER_Profile))) {
            //Picasso.with(this).load(ImageURL.profile_img_url + sessionManager.getUserDetails().get(SessionManager.USER_Profile)).into(img_profile);
        }
        //wallet = sessionManager.getUserDetails().get(SessionManager.Wallet);
        //userType = sessionManager.getUserDetails().get(SessionManager.UserType);//else salesmen

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rl_imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_modify_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u_name = tv_name.getText().toString();
                String u_email = tv_email.getText().toString();
                String u_mobile = tv_mobile.getText().toString();

                if (isEmpty(u_name)) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Please_Enter_Name), Toast.LENGTH_SHORT).show();
                } else if (u_name.length() < 3) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Name_should_be_minimum_of_three_characters), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(u_email)) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Please_Enter_Email), Toast.LENGTH_SHORT).show();
                } else if (!u_email.matches(str_email_regex)) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Please_Enter_Valid_Email), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(u_mobile)) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } else if (!u_mobile.substring(0, 2).equals("05")) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Mobile_number_should_start_with_05), Toast.LENGTH_LONG).show();
                } else if (u_mobile.length() < 10) {
                    Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject.put("name", u_name);
                        jsonObject.put("email", u_email);
                        jsonObject.put("phone", u_mobile);
                        jsonObject.put("country_id", str_country_code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*Call<ProfileUpdateModel> profileUpdateModelCall = apiService.PROFILE_UPDATE_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                    profileUpdateModelCall.enqueue(new Callback<ProfileUpdateModel>() {
                        @Override
                        public void onResponse(Call<ProfileUpdateModel> call, Response<ProfileUpdateModel> response) {
                            progressDialog.dismiss();
                            final String status = response.body().getStatus();
                            if (status.equals("1")) {
                                user_id = sessionManager.getUserDetails().get(SessionManager.User_ID);
                                user_token = sessionManager.getUserDetails().get(SessionManager.User_Token);
                                user_name = response.body().getName();
                                user_email = response.body().getEmail();
                                user_number = response.body().getPhone();
                                Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                if (sourceFile_sign != null) {
                                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), sourceFile_sign);
                                    MultipartBody.Part body = MultipartBody.Part.createFormData("user_profile", sourceFile_sign.getName(), reqFile);
                                    Call<ProfileImageUpload> profileImageUploadCall = apiService.PROFILE_IMAGE_UPLOAD_CALL(user_token, body);
                                    profileImageUploadCall.enqueue(new Callback<ProfileImageUpload>() {
                                        @Override
                                        public void onResponse(Call<ProfileImageUpload> call, Response<ProfileImageUpload> response) {
                                            if (status.equals("1")) {
                                                if (!isEmpty(str_country_code)) {
                                                    sessionManager.createLoginSession(user_token, user_id, user_name, user_email, user_number, str_country_code, response.body().getUserProfile(), wallet, userType);
                                                } else {
                                                    sessionManager.createLoginSession(user_token, user_id, user_name, user_email, user_number, sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), response.body().getUserProfile(), wallet, userType);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ProfileImageUpload> call, Throwable t) {

                                        }
                                    });
                                } else {
                                    if (!isEmpty(str_country_code)) {
                                        sessionManager.createLoginSession(user_token, user_id, user_name, user_email, user_number, str_country_code, "", wallet, userType);
                                    } else {
                                        sessionManager.createLoginSession(user_token, user_id, user_name, user_email, user_number, sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), "", wallet, userType);
                                    }
                                }
                                String language = String.valueOf(getResources().getConfiguration().locale);
                                if (language.equals("ar")) {
                                    if (response.body().getArab_message() != null) {
                                        Toast.makeText(PersonalDataActivity.this, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(PersonalDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileUpdateModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonalDataActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });

        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PersonalDataActivity.this, ChangePasswordActivity.class));
            }
        });

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<CountryListModel> countryListModelCall = apiService.COUNTRY_LIST_MODEL_CALL();
            countryListModelCall.enqueue(new Callback<CountryListModel>() {
                @Override
                public void onResponse(Call<CountryListModel> call, Response<CountryListModel> response) {
                    progressDialog.dismiss();
                    arrayList_country = response.body().getData();
                    for (int i = 0; i < arrayList_country.size(); i++) {
                        arrayList_country_name.add(arrayList_country.get(i).getCountryName());
                        arrayList_country_id.add(arrayList_country.get(i).getId());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(PersonalDataActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList_country_name);
                    sp_country.setAdapter(dataAdapter);
                    sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            str_country_code = arrayList_country_id.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //int pos = arrayList_country_id.indexOf(sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID));

                    //sp_country.setSelection(pos);
                }

                @Override
                public void onFailure(Call<CountryListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(PersonalDataActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalDataActivity.this);
        builder.setTitle(getResources().getString(R.string.Add_Photo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getResources().getString(R.string.Take_Photo))) {
                    if (isPermissionGranted()) {
                        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Yakrm/";
                        File newdir = new File(dir);
                        newdir.mkdirs();
                        String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";
                        File newfile = new File(file);
                        try {
                            newfile.createNewFile();
                        } catch (IOException ignored) {
                        }
                        selectedImage = Uri.fromFile(newfile);
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                        startActivityForResult(cameraIntent, CAMERA_IMAGE);
                    } else {
                        Toast.makeText(PersonalDataActivity.this, "Permission needed to access Camera", Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery))) {
                    if (isPermissionGranted()) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PICK_IMAGE_GALLERY);
                    } else {
                        Toast.makeText(PersonalDataActivity.this, "Permission needed to access Gallery", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals(getResources().getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(PersonalDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                value = true;
            } else {
                ActivityCompat.requestPermissions(PersonalDataActivity.this, new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
                value = false;
            }
        } else {
            value = true;
        }
        return value;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //newBase = LocaleChanger.configureBaseContext(newBase);
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
