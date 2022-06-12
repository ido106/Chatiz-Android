package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.databinding.ActivityStartChatBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class StartChatActivity extends AppCompatActivity {

    List<Contact> contactList;
    private ActivityStartChatBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we have to do this in order to get the Binding (gets null otherwise)
        binding = binding.inflate(getLayoutInflater());
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
        ArrayAdapter<Contact> contactArrayAdapter =
                new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_list_item_1, // draw according to this layout (this layout contains onlu one TextView tag)
                        contactList);

        // adapt
        listContacts.setAdapter(contactArrayAdapter);

        // catch the button that adds contacts
        FloatingActionButton btnAdd = binding.btnAdd;
        // add onclick listener
        btnAdd.setOnClickListener(view ->{
            // move to the FormActivity in order to add a new contact
            Intent i = new Intent(this, FormActivity.class);
            startActivity(i);
        });
    }
}