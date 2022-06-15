package com.example.androidchat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidchat.Adapters.ContactListAdapter;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.databinding.ActivityStartChatBinding;

import java.util.ArrayList;
import java.util.List;

public class StartChatActivity extends AppCompatActivity {

    private List<Contact> contactList;
    private ActivityStartChatBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private ContactListAdapter contactArrayAdapter;
    private String connected;
    private NotificationManagerCompat notificationManagerCompat;
    private static int maxNotificationId = 1;


    private void setDefaultSettings() {
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
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultSettings();

        contactList = new ArrayList<>();

        contactArrayAdapter = new ContactListAdapter(this);

        //recycle view for messages
        RecyclerView contactListView = binding.listContacts;
        //set layout manager (linear should do the work for our needs every time)
        contactListView.setLayoutManager(new LinearLayoutManager(this));

        //setting the data for the adapter
        contactArrayAdapter.setContactList(contactList);

        //adapt
        contactListView.setAdapter(contactArrayAdapter);


//        RecyclerView listContacts = binding.listContacts;
//        //ListView listContacts = findViewById(R.id.listContacts);
//
//        // adapt between the list and the list view
//        contactArrayAdapter = new ContactListAdapter(this);
//        // adapt
//        listContacts.setAdapter(contactArrayAdapter);
//        listContacts.setLayoutManager(new LinearLayoutManager(this));


        // add onclick listener - adding a contact
        binding.btnAdd.setOnClickListener(view -> {
            // move to the FormActivity in order to add a new contact
            Intent i = new Intent(this, FormActivity.class);
            startActivity(i);
        });


//        // todo check if it is working - cannot run app for now
//        // Delete contact on long click
//        binding.listContacts.setOnItemLongClickListener((adapterView, view, i, l) -> {
//            Contact contact = contactList.remove(i);
//            chatDao.deleteContact(contact);
//            contactArrayAdapter.notifyDataSetChanged();
//            return true;
//        });

        // todo update contact? Room video (number 6) on 45 minute approx


        createNotificationChannel();

//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
//                StartChatActivity.this, instanceIdResult -> {
//                    String newToken = instanceIdResult.getToken();
//                }
//
//        );
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

    /**
     * add listener to "Send message" button to send notification
     **/
    //todo have to figure out how to send the notification only to the message receiver
    private void putNotification(String contactName, String msg) {
        if (msg == null || msg.length() == 0) return;

        Intent contactChat = new Intent(this, ChatPageActivity.class);
        contactChat.putExtra("id", contactName);

        /** OTMA **/
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                this, getString(R.string.NotificationMessageID))
//                .setSmallIcon(R.drawable.fulllogo_transparent)
//                .setContentTitle(contactName)
//                .setContentText(msg)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(PendingIntent.getActivity(this, 0,
//                        contactChat, 0));
//        if (msg.length() > 20) {
//            builder = builder
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
//        }

        /** IDO **/
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
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        contactArrayAdapter.setContactList(chatDao.getAllUserContacts(connected));
        contactArrayAdapter.notifyDataSetChanged();
    }
}