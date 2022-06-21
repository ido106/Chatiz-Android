package com.example.androidchat.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.androidchat.Adapters.MessageListAdapter;
import com.example.androidchat.AppDB;
import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.ChatDao;
import com.example.androidchat.ChatPageActivity;
import com.example.androidchat.Models.Message;
import com.example.androidchat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class ChatService extends FirebaseMessagingService {
    private int maxNotificationId = 1;
    private boolean initialized = false;
    private ChatDao chatDao;

    public ChatService() {
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (!initialized) {
            setNotificationChannel();
            initialized = true;
        }
        setNotification(remoteMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Message createMessageFromRemote(RemoteMessage remoteMessage) {
        final int[] newId = {1};
        String id = remoteMessage.getFrom();
        String content = remoteMessage.getData().get("content");
        chatDao.getUserMessageWithContact(MyApplication.connected_user, id).forEach(m -> {
            if (m.getId() > newId[0]) {
                newId[0] = m.getId();
            }
        });
        assert content != null;
        assert id != null;
        return new Message(MyApplication.connected_user, id, newId[0] + 1,
                content, "", true);
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addMessageToRoom(RemoteMessage remoteMessage) {
        if (MyApplication.messageListAdapter == null) return;

        Message message = createMessageFromRemote(remoteMessage);
        List<Message> list =  MyApplication.messageListAdapter.getMessageList();
        list.add(message);
        MyApplication.messageListAdapter.setMessageList(list);
        MyApplication.messageListAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getData().get("content"); // get the message
            Intent intent = new Intent(this, ChatPageActivity.class);
            intent.putExtra("id", remoteMessage.getData().get("from"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.NotificationMessageID))
                    .setSmallIcon(R.drawable.fulllogo_transparent_nobuffer)
                    .setContentTitle(getString(R.string.Chatiz)) // the title for the notification
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
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

            addMessageToRoom(remoteMessage);
        }
    }

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