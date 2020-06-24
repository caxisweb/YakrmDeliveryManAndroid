package com.codeclinic.yakrmdeliveryman.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codeclinic.yakrmdeliveryman.Models.NotificationListItemModel;
import com.codeclinic.yakrmdeliveryman.Models.NotificationListModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.Connection_Detector;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.codeclinic.yakrmdeliveryman.adepter.SimpleAdapter;
import com.codeclinic.yakrmdeliveryman.adepter.SimpleSectionedRecyclerViewAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    String[] arrayList = new String[12];
    ArrayList<String> title_arrayList;
    ArrayList<Integer> title_arrayList_size = new ArrayList<>();
    SimpleAdapter mAdapter;
    int count = 0;

    ArrayList<ArrayList<String>> arrayList_sections = new ArrayList<>();
    List<NotificationListItemModel> arrayList_notification = new ArrayList<>();
    API apiService;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    List<SimpleSectionedRecyclerViewAdapter.Section> sections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        apiService = RestClass.getClientDelivery().create(API.class);
        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.dot_light_intro))
                .sizeResId(R.dimen.divider)
                .build());

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<NotificationListModel> notificationListModelCall = apiService.NOTIFICATION_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            notificationListModelCall.enqueue(new Callback<NotificationListModel>() {
                @Override
                public void onResponse(Call<NotificationListModel> call, Response<NotificationListModel> response) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("1")) {
                        arrayList_notification = response.body().getData();
                        Collections.reverse(arrayList_notification);
                        title_arrayList = new ArrayList<>();
                        for (int i = 0; i < arrayList_notification.size(); i++) {
                            if (title_arrayList.contains(arrayList_notification.get(i).getCreatedAt())) {
                                title_arrayList.add(arrayList_notification.get(i).getCreatedAt());
                                count++;

                            } else {
                                if (title_arrayList.size() != 0) {
                                    arrayList_sections.add(title_arrayList);

                                    if (arrayList_sections.size() == 1) {
                                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, title_arrayList.get(0)));
                                    } else {
                                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count - title_arrayList.size(), title_arrayList.get(0)));
                                    }

                                /*    String date = title_arrayList.get(0);
                                    Calendar nowTime = Calendar.getInstance();
                                    Date strDate = null;
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("ddd MMM yyyy");
                                        String final_date = date.trim();
                                        strDate = sdf.parse(final_date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (nowTime.get(Calendar.DATE) == strDate.getDate()) {
                                        if (arrayList_sections.size() == 1) {
                                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, title_arrayList.get(0)));
                                        } else {
                                            //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count-title_arrayList.size()+2, title_arrayList.get(0)));
                                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count - title_arrayList.size(), title_arrayList.get(0)));
                                        }
                                    } else if (nowTime.get(Calendar.DATE) - strDate.getDate() == 1) {
                                        if (arrayList_sections.size() == 1) {
                                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, title_arrayList.get(0)));
                                        } else {
                                            //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count-title_arrayList.size()+2, title_arrayList.get(0)));
                                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count - title_arrayList.size(), title_arrayList.get(0)));
                                        }
                                    } else {
                                        if (arrayList_sections.size() == 1) {
                                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, title_arrayList.get(0)));
                                        } else {
                                            //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count-title_arrayList.size()+2, title_arrayList.get(0)));
                                            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count - title_arrayList.size(), title_arrayList.get(0)));
                                        }
                                    }*/

                                }
                                title_arrayList = new ArrayList<>();
                                title_arrayList.add(arrayList_notification.get(i).getCreatedAt());
                                count++;
                            }
                        }

                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(count - title_arrayList.size(), title_arrayList.get(0)));
                        mAdapter = new SimpleAdapter(NotificationsActivity.this, arrayList_notification);
                        //Add your adapter to the sectionAdapter
                        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
                        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(NotificationsActivity.this, R.layout.custom_notification_header_view, R.id.tv_header, mAdapter);
                        mSectionedAdapter.setSections(sections.toArray(dummy));
                        //Apply this adapter to the RecyclerView
                        recyclerView.setAdapter(mSectionedAdapter);

                    } else {
                        Toast.makeText(NotificationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<NotificationListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(NotificationsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        //ActivityRecreationHelper.onDestroy(this);
        super.onDestroy();
    }
}
