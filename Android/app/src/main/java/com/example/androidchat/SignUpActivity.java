package com.example.androidchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.api.ChatAPI;
import com.google.android.material.switchmaterial.SwitchMaterial;
import androidx.room.Room;
import com.example.androidchat.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private AppDB db;
    private ChatDao chatDao; // we can communicate with the DB with chatDao
    private View parentView;
    private SwitchMaterial themeSwitch;
    private ChatAPI chatAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultSettings();

        setSignUpToLoginButton();
        setSignUpActionButton();
    }

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

    /** wtf is that **/
//    private boolean registerToServer(View view) {
//        // send data to server and register
//        //making the call to the server api
//
//        boolean isRegistrationPassed = ChatAPI.Register(binding.SignupUsername.getText().toString(),
//                binding.SignupNickname.getText().toString(),
//                binding.SignupPassword1.getText().toString());
//
//        //if we were unable to register we put an error message and going back
//        if (!isRegistrationPassed) {
//            binding.SignupAction.setError("Unable to Sign up, please try again");
//            return false;
//        }
//        // if we managed to sign up we save the data in the local db.
//        chatDao.addUser(new User(binding.SignupUsername.getText().toString(),
//                binding.SignupNickname.getText().toString(),
//                binding.SignupPassword1.getText().toString(),
//                MyApplication.context.getString(R.string.ServerURL) // i think we should use that one
//                //"http://localhost:7038"
//        ));
//        return true;
//    }


    private void setSignUpActionButton() {
        binding.SignupAction.setOnClickListener(view -> {
            if (!checkValidation(view)) {
                return;
            }

            chatAPI.Register(
                    binding.SignupUsername.getText().toString(),
                    binding.SignupNickname.getText().toString(),
                    binding.SignupPassword1.getText().toString()
                    );

            Intent chat = new Intent(this, LoginActivity.class);
            startActivity(chat);

        });
    }

    /** what the hell is that shit **/
//    private void setSignUpActionButton() {
//        binding.SignupAction.setOnClickListener(view -> {
//            if (!checkValidation(view)) {
//                return;
//            }
//            //if (registerToServer(view)) {
//
//
//
//            Intent chat = new Intent(this, LoginActivity.class);
//            startActivity(chat);
//            //}
//        });
//    }

    private void setSignUpToLoginButton() {
        binding.SignUpToLogin.setOnClickListener(view -> {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity((login));
        });
    }

    private void setDefaultSettings() {
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatAPI = new ChatAPI(); // RetroFit

        initWidgets();
        loadSharedPreferences();
        updateView(); // init switch listener

        // create the DB connection
        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();
    }

    private void initWidgets() {

        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(MyApplication.CUSTOM_THEME, MyApplication.LIGHT_THEME);
        MyApplication.customTheme = theme;
        updateView();
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, androidx.cardview.R.color.cardview_dark_background);
        final int white = ContextCompat.getColor(this, R.color.white);

        if (MyApplication.customTheme.equals(MyApplication.DARK_THEME)) {

            parentView.setBackgroundColor(black);
        } else {

            parentView.setBackgroundColor(white);

        }

    }

}