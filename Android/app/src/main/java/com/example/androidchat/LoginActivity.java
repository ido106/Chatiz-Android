package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidchat.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // have to return the main layout
        setContentView(binding.getRoot());

        // create the DB connection
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                                        .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                                        .build();

        // now we can get the Dao
        chatDao = db.chatDao();

        // get signup button
        Button btnLoginToSignUp = binding.btnLoginToSignUp;
        btnLoginToSignUp.setOnClickListener(view -> {
            // in order to switch page we need an intent object
            Intent i = new Intent(this, SignUpActivity.class);
            // move to the page
            startActivity(i);
        });

        Button btnLogin = binding.btnLogin;
        btnLogin.setOnClickListener(view -> {
            //validations and login to the chat page if correct
        });
    }

}