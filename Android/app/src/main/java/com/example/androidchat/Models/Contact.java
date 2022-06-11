package com.example.androidchat.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Contact {

    @PrimaryKey
    private String Id;

    @PrimaryKey
    private String TalkingTo;

    private String Nickname;

    private String Server;

    private Date LastSeen;

    private String LastMessage;

    private List<Message> Messages;

    // CONSTRUCTOR

    public Contact(String id, String talkingTo, String nickname, String server) {
        this.Id = id;
        this.TalkingTo = talkingTo;
        this.Nickname = nickname;
        this.Server = server;
        this.Messages = new ArrayList<>();

        // is it ok to do it null ?
        this.LastMessage = null;

        // todo lastSeen?
        this.LastSeen = null;
    }

    // todo ADD EMPTY CONSTRUCTOR ?

    // GET

    public String getId() {
        return Id;
    }

    public String getTalkingTo() {
        return TalkingTo;
    }

    public String getNickname() {
        return Nickname;
    }

    public String getServer() {
        return Server;
    }

    public Date getLastSeen() {
        return LastSeen;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public List<Message> getMessages() {
        return Messages;
    }

    // SET

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void setServer(String server) {
        Server = server;
    }
}
