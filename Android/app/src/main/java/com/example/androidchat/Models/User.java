package com.example.androidchat.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User {

    @PrimaryKey
    private String Username;

    private String Nickname;

    private String Password;

    private String Server;

    private String LastSeen;

    private List<Contact> Contacts;

    // CONSTRUCTOR
    public User(String username, String nickname, String password, String server) {
        Username = username;
        Nickname = nickname;
        Password = password;
        Server = server;
        Contacts = new ArrayList<>();

        // get time
        Date date = new Date(); // new() gives the current date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm");
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

    public List<Contact> getContacts() {
        return Contacts;
    }

    // SET

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void setServer(String server) {
        Server = server;
    }
}
