package com.codeclinic.yakrmdeliveryman.Utils;

import android.annotation.SuppressLint;
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
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.codeclinic.yakrmdeliveryman.Activity.OrderDetailActivity;
import com.codeclinic.yakrmdeliveryman.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

/**
 * Created by bhatt on 1/12/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    String order_id, user_name, noti_type, user_type, str_title, message;
    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    SessionManager sessionManager;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //

        sessionManager = new SessionManager(this);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            //message = remoteMessage.getData().toString();
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            message = remoteMessage.getNotification().getBody();
        } else {
            str_title = remoteMessage.getData().get("subject");
            message = remoteMessage.getData().get("description");

        }
        Notification(message);
    }

    private void Notification(String messageBody) {
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    notificationManager.createNotificationChannel(mChannel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(bitmap)/*Notification icon image*/
                    .setContentTitle(str_title)
                    .setChannelId(channelId)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

            notificationManager.notify(m, notificationBuilder.build());

        }
    }
}
