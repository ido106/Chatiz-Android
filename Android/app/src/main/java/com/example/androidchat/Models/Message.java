package com.example.androidchat.Models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(primaryKeys = {"from", "to", "Id"})
public class Message {

    @NonNull
    @ColumnInfo(name = "from")
    private final String from;

    @NonNull
    @ColumnInfo(name = "to")
    private final String to;

    // todo is autoGenerate generating the count globally for all users or by keys?
    private final int Id;

    @NonNull
    private String Content;

    private final String TimeSent;

    private final boolean Sent;

    // Constructor

    public Message(@NonNull String from, @NonNull String to, int Id,@NonNull String Content,
                   String TimeSent, boolean Sent) {
        this.from = from;
        this.to = to;
        this.Id = Id;
        this.Content = Content;
        this.Sent = Sent;

        // get time
        Date date = new Date(); // new() gives the current date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM hh:mm");
        this.TimeSent = formatter.format(date);
    }



    // GET

    @NonNull
    public String getFrom() {
        return from;
    }

    @NonNull
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

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", Id=" + Id +
                ", Content='" + Content + '\'' +
                ", TimeSent='" + TimeSent + '\'' +
                ", Sent=" + Sent +
                '}';
    }
}
