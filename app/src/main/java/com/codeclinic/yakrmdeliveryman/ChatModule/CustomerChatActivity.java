package com.codeclinic.yakrmdeliveryman.ChatModule;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.codeclinic.yakrmdeliveryman.R;
import com.codeclinic.yakrmdeliveryman.Utils.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomerChatActivity extends AppCompatActivity {

    ImageView img_back;
    TextView tv_orderid;
    ScrollView scrollView;
    LinearLayout layout_chat;
    EditText edt_message;
    ImageView img_send_message;

    String USER_TYPE="Driver";
    ArrayList<ChatModel> arrayList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    SessionManager sessionManager;
    String orderID, customerID, driverID, customerName, driverName, token, type, sendersDeviceToken;//type == 1 for Customer and 2 for Driver

    OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                sendersDeviceToken= instanceIdResult.getToken();
            }
        });
        //sendersDeviceToken = FirebaseInstanceId.getInstance().getToken();

        mClient = new OkHttpClient();

        orderID = getIntent().getStringExtra("orderID");
        customerID = getIntent().getStringExtra("customerID");
        driverID = getIntent().getStringExtra("driverID");
        driverName = getIntent().getStringExtra("driverName");
        customerName = getIntent().getStringExtra("customerName");
        token = getIntent().getStringExtra("token");
        type = getIntent().getStringExtra("type");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("OrdersChat").child(orderID);
        sessionManager = new SessionManager(this);


        initialization();
    }

    private void initialization() {
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_send_message = findViewById(R.id.img_send_message);
        tv_orderid = findViewById(R.id.tv_order_id);

        tv_orderid.setText(getString(R.string.order_id)+" : "+orderID);


        //Scrollview
        scrollView = findViewById(R.id.scrollView);

        //LinearLayout
        layout_chat = findViewById(R.id.layout_chat);

        //Editext
        edt_message = findViewById(R.id.edt_message);

        img_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = edt_message.getText().toString();
                if (!messageText.equals("")) {
                    ChatModel chatModel = new ChatModel(orderID, USER_TYPE, edt_message.getText().toString(), customerID, customerName, driverID, driverName, getCurrentDate(), getCurrentTime());
                    databaseReference.child(DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()).setValue(chatModel);
                    sendNotification(messageText);
                    edt_message.setText("");
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                    }
                }
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();

                if (orderID.equals(map.get("orderId").toString())) {
                    if (USER_TYPE.equals(map.get("userType").toString())) {
                        addMessageBox(message, "1");
                    } else {
                        addMessageBox(message, "2");
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addMessageBox(String message, String types) {
        TextView textView = new TextView(this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (types.equals("1")) {
            lp2.gravity = Gravity.RIGHT;
            lp2.leftMargin = 50;
            textView.setBackgroundResource(R.drawable.dark_red_round_border_bg);
            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            lp2.gravity = Gravity.LEFT;
            lp2.rightMargin = 50;
            textView.setBackgroundResource(R.drawable.blue_round_border_bg);
            textView.setTextColor(getResources().getColor(R.color.white));
        }
        lp2.topMargin = 20;
        lp2.bottomMargin = 20;
        textView.setPadding(20, 20, 20, 20);
        textView.setLayoutParams(lp2);
        layout_chat.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    @SuppressLint("StaticFieldLeak")
    public void sendNotification(final String message) {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {

                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    JSONObject data = new JSONObject();

                    orderID = getIntent().getStringExtra("orderID");
                    customerID = getIntent().getStringExtra("customerID");
                    driverID = getIntent().getStringExtra("driverID");
                    driverName = getIntent().getStringExtra("driverName");
                    customerName = getIntent().getStringExtra("customerName");
                    token = getIntent().getStringExtra("token");
                    type = getIntent().getStringExtra("type");

                    data.put("message", message);

                    if (type.equals("1")) {
                        data.put("recieverName", driverName);
                        data.put("senderName", sessionManager.getUserDetails().get(SessionManager.User_Name));
                    } else {
                        data.put("recieverName", customerName);
                        data.put("senderName", driverName);
                    }
                    data.put("customerName", customerName);
                    data.put("driverName", driverName);
                    data.put("orderID", orderID);
                    data.put("customerID", customerID);
                    data.put("driverID", driverID);
                    data.put("type", type);
                    data.put("token", sendersDeviceToken);
                    data.put("noti_for", "chat");

                    notification.put("chatnotification", "chatnotification");
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("to", token);

                    String result = postToFCM(root.toString());

                    return result;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    // Toast.makeText(Chat_ScreenActivity.this, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //  Toast.makeText(Chat_ScreenActivity.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {

        Response response = null;
        try {
            String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";// "https://fcm.googleapis.com/fcm/send";
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.create(JSON, bodyString);
            Request request = new Request.Builder()
                    .url(FCM_MESSAGE_URL)
                    .post(body)
                    .addHeader("Authorization", "key=AIzaSyBYSolO428exrywVHtiafM_nG-Skgbaa6k")
                    .build();
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body().string();
    }

    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String cTime = sdf.format(d);

        return cTime;
    }

    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("d");
        String date = format.format(new Date());

        if (date.endsWith("1") && !date.endsWith("11"))
            format = new SimpleDateFormat("EE MMM d'st', yyyy");
        else if (date.endsWith("2") && !date.endsWith("12"))
            format = new SimpleDateFormat("EE MMM d'nd', yyyy");
        else if (date.endsWith("3") && !date.endsWith("13"))
            format = new SimpleDateFormat("EE MMM d'rd', yyyy");
        else
            format = new SimpleDateFormat("EE MMM d'th', yyyy");

        String cDate = format.format(new Date());

        return cDate;

    }
}
