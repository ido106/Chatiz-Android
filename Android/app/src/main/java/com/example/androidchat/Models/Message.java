package com.example.androidchat.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Message {

    @PrimaryKey()
    private String from;

    @PrimaryKey()
    private String to;

    // todo is autoGenerate generating the count globally for all users or by keys?
    @PrimaryKey(autoGenerate = true)
    private int Id;

    private String Content;

    private String TimeSent;

    private boolean Sent;

    // Constructor

    public Message(String from, String to, String content, boolean sent) {
        this.from = from;
        this.to = to;
        this.Content = content;
        this.Sent = sent;

        // get time
        Date date = new Date(); // new() gives the current date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm");
        this.TimeSent = formatter.format(date);
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

    public String getTimeSent() {
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
