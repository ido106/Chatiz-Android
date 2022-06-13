package com.example.androidchat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.databinding.ActivityStartChatBinding;

import java.util.ArrayList;
import java.util.List;

public class StartChatActivity extends AppCompatActivity {

    private List<Contact> contactList;
    private ActivityStartChatBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private ArrayAdapter<Contact> contactArrayAdapter;
    private String connected;
    private NotificationManagerCompat notificationManagerCompat;
    private static int maxNotificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getting connected username
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("Username")) {
            connected = sharedpreferences.getString("Username", "shit");
        }

        //create notification manager
        notificationManagerCompat = NotificationManagerCompat.from(this);

        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivityStartChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create the DB connection
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();

        contactList = new ArrayList<>();
        ListView listContacts = binding.listContacts;
        //ListView listContacts = findViewById(R.id.listContacts);

        // adapt between the list and the list view
        contactArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, // draw according to this layout (this layout contains only one TextView tag)
                contactList);
        // adapt
        listContacts.setAdapter(contactArrayAdapter);

        binding.listContacts.setOnItemClickListener(((adapterView, view, i, l) -> {
            Intent chat = new Intent(this, ChatPageActivity.class);
            chat.putExtra("id", contactList.get(i).getId());
            startActivity(chat);
        }));


        // add onclick listener
        binding.btnAdd.setOnClickListener(view -> {
            // move to the FormActivity in order to add a new contact
            Intent i = new Intent(this, FormActivity.class);
            startActivity(i);
        });
    }


    private void createNotificationChannel() {
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

    @Override
    protected void onResume() {
        super.onResume();
        contactList.clear();
        contactList.addAll(chatDao.getAllUserContacts(connected));
        contactArrayAdapter.notifyDataSetChanged();
    }


    //call this function when receiving new massage
    private void putNotification(String contactName, String msg) {
        Intent contactChat = new Intent(this, ChatPageActivity.class);
        contactChat.putExtra("id", contactName);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this, getString(R.string.NotificationMessageID))
                .setSmallIcon(R.drawable.fulllogo_transparent)
                .setContentTitle(contactName)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        contactChat, 0));

        if (msg.length() > 20) {
            builder = builder
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        }

        notificationManagerCompat.notify(maxNotificationId++, builder.build());
    }
}