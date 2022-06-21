package com.example.androidchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.api.ChatAPI;
import com.example.androidchat.databinding.ActivityFormBinding;

public class FormActivity extends AppCompatActivity {

    private ActivityFormBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private String connected;
    private ChatAPI chatAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultSettings();
        setSaveButton();
        setGoBackButton();
    }

    private void setGoBackButton() {
        binding.btnGoBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void setSaveButton() {
        // call the database
        binding.btnSave.setOnClickListener(view -> {
            EditText contactUsername = binding.etUsername;
            EditText contactNickname = binding.etNickname;
            EditText contactServer = binding.etServer;

            // add on DB and ROOM
            chatAPI.AddContactLocal(
                    contactUsername.getText().toString(),
                    contactNickname.getText().toString(),
                    contactServer.getText().toString()
            );
            // invitation
            chatAPI.Invitation(
                    MyApplication.connected_user,
                    contactUsername.getText().toString(),
                    contactServer.getText().toString()
            );


            /** ALREADY ADDED IN CHAT API REGISTER **/
//            Contact contact = new Contact(
//                    contactUsername.getText().toString(),
//                    connected,
//                    contactNickname.getText().toString(),
//                    contactServer.getText().toString());

            //chatDao.addContact(contact);

            // finish and return to the previous activity
            finish();
        });
    }
    private void setDefaultSettings() {
        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        // have to return the main layout
        setContentView(binding.getRoot());

        chatAPI = new ChatAPI(); // RetroFit

        // create the DB connection
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
}