package com.example.androidchat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.databinding.ActivityStartChatBinding;

import java.util.ArrayList;
import java.util.List;

public class StartChatActivity extends AppCompatActivity {

    List<Contact> contactList;
    private ActivityStartChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we have to do this in order to get the Binding (gets null otherwise)
        binding = binding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

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
    }
}