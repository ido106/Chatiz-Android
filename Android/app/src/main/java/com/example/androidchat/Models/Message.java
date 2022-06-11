package com.example.androidchat.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Message {

    @PrimaryKey()
    private String from;

    @PrimaryKey()
    private String to;

    @PrimaryKey(autoGenerate = true)
    private int Id;

    private String Content;

    private Date TimeSent;

    private boolean Sent;

    // Constructor

    public Message(String from, String to, String content, boolean sent) {
        this.from = from;
        this.to = to;
        this.Content = content;
        this.Sent = sent;
        // todo how to get time? i do it null for now
        // this.TimeSent = ?
        this.TimeSent = null;
    }

    // GET

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getId() {
        return Id;
    }

    public String getContent() {
        return Content;
    }

    public Date getTimeSent() {
        return TimeSent;
    }

    public boolean isSent() {
        return Sent;
    }

    //SET

    public void setContent(String content) {
        Content = content;
    }
}
