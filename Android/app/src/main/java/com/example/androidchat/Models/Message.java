package com.example.androidchat.Models;

import androidx.room.*;

@Entity
public class Message {

    @PrimaryKey(autoGenerate=true)
    public String from;

    public String to;

    public int Id;

    public String Content;

    //public DateTime TimeSent { get; set; }

    public boolean Sent;
// GETTERS AND SETTERS
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

    public boolean isSent() {
        return Sent;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setSent(boolean sent) {
        Sent = sent;
    }
}
