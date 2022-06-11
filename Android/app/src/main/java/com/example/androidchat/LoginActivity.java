package com.example.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchat.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // have to return the main layout
        setContentView(binding.getRoot());

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