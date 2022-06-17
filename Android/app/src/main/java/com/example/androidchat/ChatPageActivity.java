package com.example.androidchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidchat.Adapters.MessageListAdapter;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.databinding.ActivityChatPageBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class ChatPageActivity extends AppCompatActivity {
    private View parentView;
    private SwitchMaterial themeSwitch;
    private UserSettings settings;
    private ActivityChatPageBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private String connected;
    private Contact currentContact;
    private List<Message> messageList;
    private MessageListAdapter messageAdapter;


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settings = (UserSettings) getApplication();

        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
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

        if (getIntent().getExtras() == null) {
            finish();
        }
        String id = getIntent().getExtras().getString("id");
        currentContact = chatDao.getContact(connected, id);
        messageList = chatDao.getUserMessageWithContact(connected, currentContact.getId());


        messageAdapter = new MessageListAdapter(this);

        //recycle view for messages
        RecyclerView listMessages = binding.listMessages;
        //adapt
        //set layout manager (linear should do the work for our needs every time)
        listMessages.setLayoutManager(new LinearLayoutManager(this));

        //setting the data for the adapter
        messageAdapter.setMessageList(messageList);

        //adapt
        listMessages.setAdapter(messageAdapter);


        // add listener to "Send message" button to send the message
        binding.btnSendMessage.setOnClickListener(view -> {
            String msg = binding.messageData.getText().toString();
            if (msg.length() == 0) {
                return;
            }
            final int[] newId = {1};
            chatDao.getAllDatabaseMessages().forEach(m -> {
                if (m.getId() > newId[0]) {
                    newId[0] = m.getId();
                }
            });
            Message msgToAdd = new Message(connected, currentContact.getId(),
                    newId[0] + 1, msg, "", true);

            chatDao.addMessage(msgToAdd);
            messageList.clear();
            messageList.addAll(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
            //messageAdapter.notifyDataSetChanged();
            binding.messageData.setText("");
            messageAdapter.setMessageList(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
            messageAdapter.notifyDataSetChanged();

            //scrolling down to the last message
            listMessages.smoothScrollToPosition(messageList.size());
            currentContact.setLastMessage(msg);
            chatDao.updateContact(currentContact);
        });


        binding.btnGoBackChatPage.setOnClickListener(view -> {
            finish();
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        messageAdapter.setMessageList(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
        messageAdapter.notifyDataSetChanged();
        binding.listMessages.smoothScrollToPosition(messageList.size());

    }

    private void initWidgets() {

        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void initSwitchListener() {
        updateView();
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, androidx.cardview.R.color.cardview_dark_background);
        final int white = ContextCompat.getColor(this, R.color.white);

        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {

            parentView.setBackgroundColor(black);
        } else {

            parentView.setBackgroundColor(white);

        }

    }
}