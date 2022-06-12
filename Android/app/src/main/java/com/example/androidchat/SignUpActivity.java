package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidchat.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao


    private boolean checkValidation(View view) {
        if (binding == null) {
            return false;
        }

        boolean isValid = true;

        if (binding.SignupUsername.getText().toString().length() < 3) {
            binding.SignupUsername.setError("Username must contain at least 3 characters");
            isValid = false;
        }

        if (binding.SignupPassword1.getText().toString().length() < 6) {
            binding.SignupPassword1.setError("Password must contain at least 6 characters");
            isValid = false;
        }

        if (binding.SignupPassword1.getText().toString()
                .matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            binding.SignupPassword2.setError("Passwords must include both numbers and letters!");
            isValid = false;
        }

        if (!binding.SignupPassword1.getText().toString().equals
                (binding.SignupPassword2.getText().toString())) {
            binding.SignupPassword2.setError("Passwords must match!");
            isValid = false;
        }


        return isValid;
    }

    private boolean registerToServer(View view) {
        // send data to server and register
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create the DB connection
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();

        binding.SignupAction.setOnClickListener(view -> {
            if (!checkValidation(view)) {
                return;
            }
            if (registerToServer(view)) {
                Intent chat = new Intent(this, StartChatActivity.class);
                startActivity(chat);
            }
        });


    }


}