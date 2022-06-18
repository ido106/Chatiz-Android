package com.example.androidchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.androidchat.api.API;
import com.google.android.material.switchmaterial.SwitchMaterial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.androidchat.databinding.ActivityMainBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class LoginActivity extends AppCompatActivity {
    private View parentView;
    private SwitchMaterial themeSwitch;
    private TextView themeTV, titleTV, userN, Pass;
    private API api;

    private UserSettings settings;
    private ActivityMainBinding binding;
    private ChatDao chatDao; // we can communicate with the DB with chatDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultSettings();

        setSignUpButton();
        setSettingsButton();
        setLoginButton();

    }

    private void setLoginButton() {
        Button btnLogin = binding.btnLogin;
        btnLogin.setOnClickListener(view -> {
            boolean loginResponse = login();
            if (!loginResponse) {
                binding.LoginError.setVisibility(View.VISIBLE);
                return;
            }
            binding.LoginError.setVisibility(View.INVISIBLE);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",
                    0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Username", binding.LoginUsername.getText().toString());
            editor.apply();

            Intent chat = new Intent(this, StartChatActivity.class);
            startActivity(chat);

            //validations and login to the chat page if correct
        });
    }

    private void setSettingsButton() {
        Button btnSettings = binding.button3;
        btnSettings.setOnClickListener(view -> {
            // in order to switch page we need an intent object
            Intent i = new Intent(this, SettingsPage.class);
            // move to the page
            startActivity(i);
        });
    }
    private void setSignUpButton() {
        // get signup button
        Button btnLoginToSignUp = binding.btnLoginToSignUp;
        btnLoginToSignUp.setOnClickListener(view -> {
            // in order to switch page we need an intent object
            Intent i = new Intent(this, SignUpActivity.class);
            // move to the page
            startActivity(i);
        });
    }

    private String getFireBaseToken() {
        //todo implement this function
        return "";
    }

    private void setDefaultSettings() {
        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // have to return the main layout
        setContentView(binding.getRoot());
        settings = (UserSettings) getApplication();

        api = API.getInstance();

        initWidgets();
        loadSharedPreferences();
        initSwitchListener();


        // create the DB connection
        // "ChatDB" will be the name of the DB
        // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();
    }

    private boolean login() {
        String username = binding.LoginUsername.getText().toString();
        String password = binding.LoginPassword.getText().toString();
        String fireBaseToken = getFireBaseToken();
        return api.signIn(username, password, fireBaseToken);
    }

    private void initWidgets() {
        themeTV = findViewById(R.id.themeTV);
        titleTV = findViewById(R.id.titleTV);
        userN = findViewById(R.id.LoginUsername);
        Pass = findViewById(R.id.LoginPassword);
        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(UserSettings.CUSTOM_THEME, UserSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void initSwitchListener() {
        themeSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked)
                settings.setCustomTheme(UserSettings.DARK_THEME);
            else
                settings.setCustomTheme(UserSettings.LIGHT_THEME);

            SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
            editor.putString(UserSettings.CUSTOM_THEME, settings.getCustomTheme());
            editor.apply();
            updateView();
        });
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, androidx.cardview.R.color.cardview_dark_background);
        final int white = ContextCompat.getColor(this, R.color.white);

        if (settings.getCustomTheme().equals(UserSettings.DARK_THEME)) {
            titleTV.setTextColor(white);
            themeTV.setTextColor(white);
            userN.setTextColor(white);
            Pass.setTextColor(white);
            themeTV.setText("Dark");
            parentView.setBackgroundColor(black);
            themeSwitch.setChecked(true);
        } else {
            userN.setTextColor(black);
            Pass.setTextColor(black);
            titleTV.setTextColor(black);
            themeTV.setTextColor(black);
            themeTV.setText("Light");
            parentView.setBackgroundColor(white);
            themeSwitch.setChecked(false);
        }

    }
}