package com.example.androidchat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ChatService extends FirebaseMessagingService {
    private int maxNotificationId = 1;

    public ChatService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        setNotification(remoteMessage);
        setNotificationChannel();
    }

    private void setNotification(RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getTitle(); // get the message

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.NotificationMessageID))
                    .setSmallIcon(R.drawable.fulllogo_transparent_nobuffer)
                    .setContentTitle(getString(R.string.Chatiz)) // the title for the notification
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
// todo  add contact intent                 .setContentIntent(PendingIntent.getActivity(this, 0,
//                            contactChat, 0))
                    .setAutoCancel(true);
            if (message.length() > 50) {
                builder.setContentText(message.substring(0, 50)) // set maximum of 50 characters to the content
                        .setStyle(new NotificationCompat.BigTextStyle() // if the content is longer than 50 characters
                                .bigText(message));
            } else {
                builder.setContentText(message);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(maxNotificationId++, builder.build());
        }
    }

    /*private void setNotification(String contactName, String msg) {
        if (msg == null || msg.length() == 0) return;

        Intent contactChat = new Intent(this, ChatPageActivity.class);
        contactChat.putExtra("id", contactName);


        *//** IDO **//*
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.NotificationMessageID))
                .setSmallIcon(R.drawable.fulllogo_transparent_nobuffer)
                .setContentTitle(getString(R.string.Chatiz)) // the title for the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        contactChat, 0))
                .setAutoCancel(true);
        if (msg.length() > 50) {
            builder.setContentText(msg.substring(0, 50)) // set maximum of 50 characters to the content
                    .setStyle(new NotificationCompat.BigTextStyle() // if the content is longer than 50 characters
                            .bigText(msg));
        } else {
            builder.setContentText(msg);
        }

        notificationManagerCompat.notify(maxNotificationId++, builder.build());
    }*/

    private void setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence msgNotification = getString(R.string.NotificationMessageName);
            String description = getString(R.string.descriptionNotificationMessage);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.NotificationMessageID), msgNotification, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}