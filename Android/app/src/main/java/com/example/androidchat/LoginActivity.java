package com.example.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidchat.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Button btnLoginToSignUp = binding.btnLoginToSignUp;
        btnLoginToSignUp.setOnClickListener(view -> {
            Intent i = new Intent(this, SignUpActivity.class);
            startActivity(i);
        });

        Button btnLogin = binding.btnLogin;
        btnLogin.setOnClickListener(view -> {
            //validations and login to the chat page if correct
        });
    }
}