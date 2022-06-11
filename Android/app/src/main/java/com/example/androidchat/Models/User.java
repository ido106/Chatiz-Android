package com.example.androidchat.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    private Date LastSeen;

    private List<Contact> Contacts;

    // CONSTRUCTOR
    public User(String username, String nickname, String password, String server) {
        Username = username;
        Nickname = nickname;
        Password = password;
        Server = server;
        // todo add lastSeen to now, i do it null for now
        LastSeen = null;
        Contacts = new ArrayList<>();
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

    public Date getLastSeen() {
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
