package com.example.androidchat.Models;

import java.util.List;

public class User {

    public String Username;

    public String Nickname;


    public String Password;


    public String Server;

    //public DateTime LastSeen

    public List<Contact> Contacts;
//getters and setters
    public void setUsername(String username) {
        Username = username;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setServer(String server) {
        Server = server;
    }

    public void setContacts(List<Contact> contacts) {
        Contacts = contacts;
    }

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

    public List<Contact> getContacts() {
        return Contacts;
    }


}
