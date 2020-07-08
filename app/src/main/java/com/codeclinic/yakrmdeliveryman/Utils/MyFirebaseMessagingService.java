package com.codeclinic.yakrmdeliveryman.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.codeclinic.yakrmdeliveryman.Activity.OrderDetailActivity;
import com.codeclinic.yakrmdeliveryman.ChatModule.CustomerChatActivity;
import com.codeclinic.yakrmdeliveryman.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import static android.text.TextUtils.isEmpty;

/**
 * Created by bhatt on 1/12/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    String str_title, message, noti_for, noti_type;
    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    SessionManager sessionManager;
    String notiFor = "normal";
    String orderID, customerID, driverID, customerName, senderName, driverName, token, type, notification_type = "";//type == 1 for Customer and 2 for Driver

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sessionManager = new SessionManager(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        //Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        /*message = remoteMessage.getNotification().getBody();
        str_title = remoteMessage.getNotification().getTitle();*/

        if (remoteMessage.getData().get("noti_for").equals("chat")) {

            notiFor = remoteMessage.getData().get("noti_for");
            if (!isEmpty(remoteMessage.getData().get("senderName"))) {
                str_title = remoteMessage.getData().get("senderName");
            }
            message = remoteMessage.getData().get("message");
            orderID = remoteMessage.getData().get("orderID");
            customerID = remoteMessage.getData().get("customerID");
            driverID = remoteMessage.getData().get("driverID");
            driverName = remoteMessage.getData().get("driverName");
            customerName = remoteMessage.getData().get("customerName");
            senderName = remoteMessage.getData().get("senderName");
            token = remoteMessage.getData().get("token");
            type = remoteMessage.getData().get("type");

            notification_type = remoteMessage.getData().get("noti_type");

        } else {

            str_title = remoteMessage.getData().get("subject");
            message = remoteMessage.getData().get("description");
            orderID = remoteMessage.getData().get("order_id");

        }

        Notification(message, notiFor, notification_type);

    }

    private void Notification(String messageBody, String notificationFor, String notificationType) {
        if (sessionManager.isLoggedIn()) {
            if (notificationFor.equals("chat")) {
                launchActivity(CustomerChatActivity.class, messageBody, notificationFor);
            } else {
                launchActivity(OrderDetailActivity.class, messageBody, notificationFor);
            }
        }
    }

    public void launchActivity(Class<?> intentClass, String messageBody, String notificationType) {
        Intent intent = new Intent(this, intentClass);
        if (notificationType.equals("chat")) {
            intent.putExtra("orderID", orderID);
            intent.putExtra("driverID", driverID);
            intent.putExtra("driverName", driverName);
            intent.putExtra("customerName", customerName);
            intent.putExtra("token", token);
            intent.putExtra("type", type);
        } else {
            intent.putExtra("order_id", orderID);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setLargeIcon(bitmap)/*Notification icon image*/
                .setContentTitle(str_title)
                .setChannelId(channelId)
                .setSmallIcon(R.drawable.app_logo)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        notificationManager.notify(m, notificationBuilder.build());
    }


}
