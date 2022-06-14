package com.example.androidchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidchat.Adapters.MessageListAdapter;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.databinding.ActivityChatPageBinding;

import java.util.List;

public class ChatPageActivity extends AppCompatActivity {

    private ActivityChatPageBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private String connected;
    private Contact currentContact;
    private List<Message> messageList;
    private ArrayAdapter<Message> messageArrayAdapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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

//        messageArrayAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, // draw according to this layout (this layout contains only one TextView tag)
//                messageList);
//
//        binding.listMessages.setAdapter(messageArrayAdapter);

        //recycle view for messages
        RecyclerView listMessages = binding.listMessages;
        final MessageListAdapter adapter = new MessageListAdapter(this);
        listMessages.setAdapter(adapter);
        listMessages.setLayoutManager(new LinearLayoutManager(this));


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
            messageArrayAdapter.notifyDataSetChanged();
            binding.messageData.setText("");
        });

        binding.btnGoBackChatPage.setOnClickListener(view -> {
            finish();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        messageArrayAdapter.clear();
        messageList.addAll(chatDao.getUserMessageWithContact(connected, currentContact.getId()));
        messageArrayAdapter.notifyDataSetChanged();
    }
}