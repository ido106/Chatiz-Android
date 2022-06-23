package com.example.androidchat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.Models.ProfilePictureHolder;
import com.example.androidchat.api.ChatAPI;
import com.google.android.material.switchmaterial.SwitchMaterial;

import androidx.room.Room;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidchat.Adapters.ContactListAdapter;
import com.example.androidchat.databinding.ActivityStartChatBinding;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class StartChatActivity extends AppCompatActivity {
    private View parentView;
    private SwitchMaterial themeSwitch;
    //private ContactsViewModel contactsViewModel;
    //private List<Contact> contactList;
    private ActivityStartChatBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private ContactListAdapter contactArrayAdapter;
    private String connected;
    private NotificationManagerCompat notificationManagerCompat;
    private ChatAPI chatAPI;
    private Bitmap bitmapPicture;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultSettings();
        setArrayAdapter();
        setAddButton();

        createNotificationChannel();
    }

    @SuppressLint("SetTextI18n")
    private void setTopBar() {
        assert binding.userNameStartChat != null;
        binding.userNameStartChat.setText("       Current User: " + MyApplication.connected_user);
        // set bitmap on imageView
        assert binding.userImageViewStartChat != null;
        binding.userImageViewStartChat.setImageBitmap(bitmapPicture);
    }

    private void initBitmapPicture() {
        ProfilePictureHolder holder = chatDao.getPictureByUsername(MyApplication.connected_user);

        byte[] bytes=Base64.decode(holder.getDecodedProfileImage(),Base64.DEFAULT);
        bitmapPicture =  BitmapFactory.decodeByteArray(bytes,0,bytes.length);

    }

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

        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
        // create the DB connection
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();
        chatAPI = new ChatAPI();
    }

    private void setAddButton() {
        if (findViewById(R.id.btnAdd) != null) {
            // add onclick listener - adding a contact
            binding.btnAdd.setOnClickListener(view -> {
                // move to the FormActivity in order to add a new contact
                Intent i = new Intent(this, FormActivity.class);
                startActivity(i);
            });
        } else {
            ContactListAdapter.isLand = 1;
        }

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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();

        contactArrayAdapter.setContactList(chatDao.getAllUserContacts(connected));
        chatAPI.getAllConnectedUserContacts(contactArrayAdapter);
        contactArrayAdapter.notifyDataSetChanged();
        initBitmapPicture();
        setTopBar();

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
}