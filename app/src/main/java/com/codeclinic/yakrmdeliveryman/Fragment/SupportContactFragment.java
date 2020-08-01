package com.codeclinic.yakrmdeliveryman.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.codeclinic.yakrmdeliveryman.Models.FeedBackModel;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Retrofit.API;
import com.codeclinic.yakrmdeliveryman.Retrofit.RestClass;
import com.codeclinic.yakrmdeliveryman.Utils.Connection_Detector;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportContactFragment extends Fragment {

    EditText edt_msg_title, edt_description, edt_email;
    Button btn_send;
    API apiService;
    ProgressDialog progressDialog;
    JSONObject jsonObject = new JSONObject();
    SessionManager sessionManager;

    HashMap<String, String> user_detail = new HashMap<>();

    public SupportContactFragment() {
        // Required empty public constructor
    }

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_contact, container, false);

        edt_msg_title = view.findViewById(R.id.edt_msg_title);
        edt_description = view.findViewById(R.id.edt_description);
        edt_email = view.findViewById(R.id.edt_email);
        btn_send = view.findViewById(R.id.btn_send);

        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
        user_detail = sessionManager.getUserDetails();
        edt_email.setText(user_detail.get(SessionManager.User_Email));

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection_Detector.isInternetAvailable(getActivity())) {
                    if (isEmpty(edt_email.getText().toString())) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.Please_Enter_Email), Toast.LENGTH_SHORT).show();
                    } else if (isEmpty(edt_msg_title.getText().toString())) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.Enter_message_title), Toast.LENGTH_SHORT).show();
                    } else if (isEmpty(edt_description.getText().toString())) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.Enter_messgae_description), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        try {
                            jsonObject.put("email", edt_email.getText().toString());
                            jsonObject.put("title", edt_msg_title.getText().toString());
                            jsonObject.put("description", edt_description.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Call<FeedBackModel> feedBackModelCall = apiService.FEED_BACK_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        feedBackModelCall.enqueue(new Callback<FeedBackModel>() {
                            @Override
                            public void onResponse(Call<FeedBackModel> call, Response<FeedBackModel> response) {
                                progressDialog.dismiss();
                                if (response.body().getStatus().equals("1")) {
                                    edt_msg_title.setText("");
                                    edt_description.setText("");
                                }
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<FeedBackModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
