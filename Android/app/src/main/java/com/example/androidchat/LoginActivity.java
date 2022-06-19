package com.example.androidchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.api.ChatAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import androidx.room.Room;
import com.example.androidchat.databinding.ActivityMainBinding;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginActivity extends AppCompatActivity {
    private View parentView;
    private SwitchMaterial themeSwitch;
    private TextView themeTV, titleTV, userN, Pass;
    private ChatAPI chatAPI;
    private ActivityMainBinding binding;
    private ChatDao chatDao; // we can communicate with the DB with chatDao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultSettings();

        setFirebase();
        setSignUpButton();
        setSettingsButton();
        setLoginButton();

    }

    private void onLoginSuccess() {
        if(MyApplication.jwtToken != null) { //success
            binding.LoginError.setVisibility(View.INVISIBLE);

            SharedPreferences pref = MyApplication.context.getSharedPreferences("MyPref",
                    0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Username", binding.LoginUsername.getText().toString());
            editor.apply();

            Intent chat = new Intent(MyApplication.context, StartChatActivity.class);
            startActivity(chat);
        } else {
            binding.LoginError.setVisibility(View.VISIBLE);
        }
    }


    private void setLoginButton() {
        Button btnLogin = binding.btnLogin;
        btnLogin.setOnClickListener(view -> {
            String username = binding.LoginUsername.getText().toString();
            String password = binding.LoginPassword.getText().toString();
            chatAPI.SignIn(username, password, () -> {onLoginSuccess();});
        });
    }

    /** firebase **/
    private void setFirebase() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            MyApplication.firebaseToken = newToken;
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

    private void setDefaultSettings() {
        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // have to return the main layout
        setContentView(binding.getRoot());

        chatAPI = new ChatAPI(); // RetroFit


        initWidgets();
        loadSharedPreferences();
        initSwitchListener();


        // create the DB connection
        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();
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
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(MyApplication.CUSTOM_THEME, MyApplication.LIGHT_THEME);
        MyApplication.customTheme = theme;
        updateView();
    }

    private void initSwitchListener() {
        themeSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked)
                MyApplication.customTheme = MyApplication.DARK_THEME;
            else
                MyApplication.customTheme = MyApplication.LIGHT_THEME;

            SharedPreferences.Editor editor = getSharedPreferences(MyApplication.PREFERENCES, MODE_PRIVATE).edit();
            editor.putString(MyApplication.CUSTOM_THEME, MyApplication.customTheme);
            editor.apply();
            updateView();
        });
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, androidx.cardview.R.color.cardview_dark_background);
        final int white = ContextCompat.getColor(this, R.color.white);

        if (MyApplication.customTheme.equals(MyApplication.DARK_THEME)) {
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