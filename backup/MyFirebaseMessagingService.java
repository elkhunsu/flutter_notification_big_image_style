package com.example.loop_app_flutter_v2;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.text.Html;
import android.net.Uri;

import static android.R.drawable.ic_delete;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION_REPLY = "NotificationReply";
    public static int NOTIFICATION_ID = 200;
    public static final int REQUEST_CODE_APPROVE = 101;
    public static final String KEY_INTENT_APPROVE = "keyintentaccept";

    private static final String TAG = "MyFirebaseMessaging";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Log.e(TAG, "Message data size: " + remoteMessage.getData().size());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                String imageURL = remoteMessage.getData().get("image");
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                    if(imageURL.equals("no")){

                    Map<String, String> data = remoteMessage.getData();
                    sendNotification(notification, data);
                }else {
                    Bitmap bitmap = getBitmapFromURL(imageURL);
//                    notificationWithImage(bitmap, title, message);
                }
            } catch (Exception e) {
                Log.e("exc", "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        PendingIntent approvePendingIntent = PendingIntent.getBroadcast(
                this,
                REQUEST_CODE_APPROVE,
                new Intent(this, NotificationReceiver.class)
                        .putExtra(KEY_INTENT_APPROVE, REQUEST_CODE_APPROVE),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        RemoteInput remoteInput = new RemoteInput.Builder((NOTIFICATION_REPLY))
                .setLabel("Approve Comments")
                .build();
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(ic_delete,
                        "Approve", approvePendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(approvePendingIntent)
                .setContentInfo(notification.getTitle())
                .setLargeIcon(icon)
                .setColor(Color.GRAY)
                .setLights(Color.GRAY, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(action);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void notificationWithImage(Bitmap bitmap, String title, String message) {

//        Intent intent = new Intent(this, GotNotificationActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//        bigPictureStyle.setBigContentTitle(title);
//        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//        bigPictureStyle.bigPicture(bitmap);
//        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setContentIntent(pendingIntent)
//                .setSound(defaultSoundUri)
//                .setStyle(bigPictureStyle)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
//                .setContentText(message);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (NOTIFICATION_ID > 1073741824) {
//            NOTIFICATION_ID = 0;
//        }
//        notificationManager.notify(NOTIFICATION_ID++ , mNotifyBuilder.build());
    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

