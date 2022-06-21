package com.example.androidchat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidchat.Adapters.ContactListAdapter;
import com.example.androidchat.Adapters.MessageListAdapter;
import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.api.ChatAPI;
import com.example.androidchat.databinding.ActivityChatLandBinding;
import com.example.androidchat.databinding.ActivityChatPageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class ChatLand extends AppCompatActivity {
    private View parentView;
    private SwitchMaterial themeSwitch;
    private ActivityChatLandBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private String connected;
    private Contact currentContact;
    private List<Message> messageList;
    //working with static var from myApp
    //private MessageListAdapter messageAdapter;
    private ChatAPI chatAPI;
    private ContactListAdapter contactArrayAdapter;
    private NotificationManagerCompat notificationManagerCompat;


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultSettings();
        if (findViewById(R.id.btnGoBackChatPageFuck)!= null) {
            ContactListAdapter.isLand = 0;
            finish();

        }
        setMessageAdapter();
        setArrayAdapter();

        /** ViewModel Delete **/
//        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
//        contactsViewModel.getLiveData().observe(this, contacts -> {
//            contactArrayAdapter.setContactList(contacts);
//            contactArrayAdapter.notifyDataSetChanged();
//        });

        createNotificationChannel();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
//        messageAdapter.setMessageList(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
//        messageAdapter.notifyDataSetChanged();

        MyApplication.messageListAdapter.setMessageList(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
        MyApplication.messageListAdapter.notifyDataSetChanged();

        binding.listMessages.smoothScrollToPosition(messageList.size());
        contactArrayAdapter.setContactList(chatDao.getAllUserContacts(connected));
        chatAPI.getAllConnectedUserContacts(contactArrayAdapter);
        contactArrayAdapter.notifyDataSetChanged();

    }

    private void setDefaultSettings() {
        binding = ActivityChatLandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initWidgets();

        loadSharedPreferences();

        initSwitchListener();

        chatAPI = new ChatAPI(); // RetroFit

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();

        SharedPreferences sharedpreferences = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("Username")) {
            connected = sharedpreferences.getString("Username", "shit");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setMessageAdapter() {
        if (getIntent().getExtras() == null) {
            finish();
        }
        String id = getIntent().getExtras().getString("id");
        currentContact = chatDao.getContact(connected, id);
        messageList = chatDao.getUserMessageWithContact(connected, currentContact.getId());
//        messageAdapter = new MessageListAdapter(this);

        MyApplication.messageListAdapter = new MessageListAdapter(this);
        //recycle view for messages
        RecyclerView listMessages = binding.listMessages;
        //adapt
        //set layout manager (linear should do the work for our needs every time)
        listMessages.setLayoutManager(new LinearLayoutManager(this));
        //setting the data for the adapter
//        messageAdapter.setMessageList(messageList);
//        //adapt
//        listMessages.setAdapter(messageAdapter);

        MyApplication.messageListAdapter.setMessageList(messageList);
        //adapt
        listMessages.setAdapter( MyApplication.messageListAdapter);


        // add listener to "Send message" button to send the message
        binding.btnSendMessage.setOnClickListener(view -> {
            String msg = binding.messageData.getText().toString();
            if (msg.length() == 0) {
                return;
            }

            chatAPI.AddMessage(id, msg);
            chatAPI.TransferMessage(MyApplication.connected_user, id, msg);

//            final int[] newId = {1};
//            chatDao.getAllDatabaseMessages().forEach(m -> {
//                if (m.getId() > newId[0]) {
//                    newId[0] = m.getId();
//                }
//            });
//            Message msgToAdd = new Message(connected, currentContact.getId(),
//                    newId[0] + 1, msg,"", true);

            /** ALREADY ADDED ON ChatAPI AddMessage **/
            //chatDao.addMessage(msgToAdd);




            messageList.clear();
            messageList.addAll(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
            //messageAdapter.notifyDataSetChanged();
            binding.messageData.setText("");
//            messageAdapter.setMessageList(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
//            messageAdapter.notifyDataSetChanged();

            MyApplication.messageListAdapter.setMessageList(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
            MyApplication.messageListAdapter.notifyDataSetChanged();

            //scrolling down to the last message
            listMessages.smoothScrollToPosition(messageList.size());
            currentContact.setLastMessage(msg);
            chatDao.updateContact(currentContact);
        });
    }

    private void setGoBackButton() {
        binding.btnGoBackChatPage.setOnClickListener(view -> {
            finish();
        });
    }

    private void initWidgets() {

        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(MyApplication.CUSTOM_THEME, MyApplication.LIGHT_THEME);
        MyApplication.customTheme = theme;
        updateView();
    }

    private void initSwitchListener() {
        updateView();
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, androidx.cardview.R.color.cardview_dark_background);
        final int white = ContextCompat.getColor(this, R.color.white);

        if (MyApplication.customTheme.equals(MyApplication.DARK_THEME)) {
            parentView.setBackgroundColor(black);
        } else {
            parentView.setBackgroundColor(white);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.messageListAdapter = null;
    }
    private void setArrayAdapter() {
        contactArrayAdapter = new ContactListAdapter(this);

        //recycle view for messages
        RecyclerView contactListView = binding.listContacts;
        //set layout manager (linear should do the work for our needs every time)
        contactListView.setLayoutManager(new LinearLayoutManager(this));

        //setting the data for the adapter
        contactArrayAdapter.setContactList(new ArrayList<>());

        //adapt
        contactListView.setAdapter(contactArrayAdapter);
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

}