package com.example.androidchat.Models;

import java.util.List;

public class Contact {
    public String Id;

    public String TalkingTo;

    public String Nickname;

    public String Server;

    // public DateTime LastSeen ;

    public String LastMessage;


    public List<Message> Messages;
    //GETTERS AND SETTERS
    public void setId(String id) {
        Id = id;
    }

    public void setTalkingTo(String talkingTo) {
        TalkingTo = talkingTo;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public void setServer(String server) {
        Server = server;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public void setMessages(List<Message> messages) {
        Messages = messages;
    }

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

    public String getLastMessage() {
        return LastMessage;
    }

    public List<Message> getMessages() {
        return Messages;
    }

}
