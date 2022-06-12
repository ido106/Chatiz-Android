package com.example.androidchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.databinding.ActivityChatPageBinding;

import java.util.List;

public class ChatPageActivity extends AppCompatActivity {

    private ActivityChatPageBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private String connected;
    private Contact currentContact;
    private List<Message> messageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);


        binding = ActivityChatPageBinding.inflate(getLayoutInflater());

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



    }
}