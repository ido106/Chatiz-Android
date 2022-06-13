package com.example.androidchat.Models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(primaryKeys = {"Id", "TalkingTo"})
public class Contact {
    @NonNull
    private String Id;

    @NonNull
    private String TalkingTo;

    @NonNull
    private String Nickname;

    @NonNull
    private String Server;

    private String LastSeen;

    private String LastMessage;

    //private List<Message> Messages;

    // CONSTRUCTOR

    public Contact(@NonNull String Id, @NonNull String TalkingTo,@NonNull String Nickname,@NonNull String Server) {
        this.Id = Id;
        this.TalkingTo = TalkingTo;
        this.Nickname = Nickname;
        this.Server = Server;
        //this.Messages = new ArrayList<>();

        // is it ok to do it null ?
        this.LastMessage = null;

        // todo change lastSeen according to contact's date ? currently inserting the current time
        Date date = new Date(); // new() gives the current date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm");
        this.LastSeen = formatter.format(date);
    }

    // GET

    @NonNull
    public String getId() {
        return Id;
    }

    @NonNull
    public String getTalkingTo() {
        return TalkingTo;
    }

    public String getNickname() {
        return Nickname;
    }

    public String getServer() {
        return Server;
    }

    public String getLastSeen() {
        return LastSeen;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    //public List<Message> getMessages() {return Messages;}

    // SET

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void setServer(String server) {
        Server = server;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public void setTalkingTo(@NonNull String talkingTo) {
        TalkingTo = talkingTo;
    }

    public void setLastSeen(String lastSeen) {
        LastSeen = lastSeen;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "Id='" + Id + '\'' +
                ", TalkingTo='" + TalkingTo + '\'' +
                ", Nickname='" + Nickname + '\'' +
                ", Server='" + Server + '\'' +
                ", LastSeen='" + LastSeen + '\'' +
                ", LastMessage='" + LastMessage + '\'' +
                '}';
    }
}
