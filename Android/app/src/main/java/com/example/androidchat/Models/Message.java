package com.example.androidchat.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Message {

    @PrimaryKey()
    private String From;

    @PrimaryKey()
    private String To;

    // todo is autoGenerate generating the count globally for all users or by keys?
    @PrimaryKey(autoGenerate = true)
    private int Id;

    private String Content;

    private Date TimeSent;

    private boolean Sent;

    // Constructor

    public Message(String from, String to, String content, boolean sent) {
        this.From = from;
        this.To = to;
        this.Content = content;
        this.Sent = sent;
        // todo how to get time? i do it null for now
        // this.TimeSent = ?
        this.TimeSent = null;
    }

    // GET

    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
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
