package com.example.androidchat.Models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class User {
    @NonNull
    @PrimaryKey
    private String Username;

    private String Nickname;

    private String Password;

    private String Server;

    private String LastSeen;

    //private List<Contact> Contacts;

    // CONSTRUCTOR
    public User(@NonNull String Username, String Nickname, String Password, String Server) {
        this.Username = Username;
        this.Nickname = Nickname;
        this.Password = Password;
        this.Server = Server;
        //Contacts = new ArrayList<>();

        // get time
        Date date = new Date(); // new() gives the current date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm");
        this.LastSeen = formatter.format(date);
    }

    // GET

    public String getUsername() {
        return Username;
    }

    public String getNickname() {
        return Nickname;
    }

    public String getPassword() {
        return Password;
    }

    public String getServer() {
        return Server;
    }

    public String getLastSeen() {
        return LastSeen;
    }

    //public List<Contact> getContacts() {return Contacts;}

    // SET

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void setServer(String server) {
        Server = server;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setLastSeen(String lastSeen) {
        LastSeen = lastSeen;
    }
}
