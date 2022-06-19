package com.example.androidchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.databinding.ActivityMainBinding;
import com.example.androidchat.databinding.ActivitySettingsPageBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsPage extends AppCompatActivity
{
    private View parentView;
    private SwitchMaterial themeSwitch;
    private TextView themeTV, titleTV;
    private ActivitySettingsPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setDefaultSettings();
        setImageButton();
    }

    private void setDefaultSettings() {
        // we have to do this in order to get the Binding (gets null otherwise)
        binding = ActivitySettingsPageBinding.inflate(getLayoutInflater());
        // have to return the main layout
        setContentView(binding.getRoot());

        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
    }

    private void setImageButton() {
        binding.imageButton.setOnClickListener(view -> {
            // in order to switch page we need an intent object
            Intent i = new Intent(this, LoginActivity.class);
            // move to the page
            startActivity(i);
        });
    }

    private void initWidgets()
    {
        themeTV = findViewById(R.id.themeTV);
        titleTV = findViewById(R.id.titleTV);
        themeSwitch = findViewById(R.id.themeSwitch);
        parentView = findViewById(R.id.parentView);
    }

    private void loadSharedPreferences()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(MyApplication.PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(MyApplication.CUSTOM_THEME, MyApplication.LIGHT_THEME);
        MyApplication.customTheme = theme;
        updateView();
    }

    private void initSwitchListener()
    {
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
            {
                if(checked)
                    MyApplication.customTheme = MyApplication.DARK_THEME;
                else
                    MyApplication.customTheme = MyApplication.LIGHT_THEME;

                SharedPreferences.Editor editor = getSharedPreferences(MyApplication.PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(MyApplication.CUSTOM_THEME, MyApplication.customTheme);
                editor.apply();
                updateView();
            }
        });
    }

    private void updateView()
    {
        final int black = ContextCompat.getColor(this, com.google.android.material.R.color.cardview_dark_background);
        final int white = ContextCompat.getColor(this, R.color.white);

        if(MyApplication.customTheme.equals(MyApplication.DARK_THEME))
        {
            titleTV.setTextColor(white);
            themeTV.setTextColor(white);
            themeTV.setText("Dark");
            parentView.setBackgroundColor(black);
            themeSwitch.setChecked(true);
        }
        else
        {
            titleTV.setTextColor(black);
            themeTV.setTextColor(black);
            themeTV.setText("Light");
            parentView.setBackgroundColor(white);
            themeSwitch.setChecked(false);
        }

    }

}

