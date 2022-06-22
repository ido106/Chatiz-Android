package com.example.androidchat.Models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class ProfilePictureHolder {

    @PrimaryKey
    @NonNull
    private String username;

    private String decodedProfileImage;


    public String getUsername() {
        return username;
    }

    public String getDecodedProfileImage() {
        return decodedProfileImage;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setDecodedProfileImage(String decodedProfileImage) {
        this.decodedProfileImage = decodedProfileImage;
    }
}
