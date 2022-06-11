package com.example.androidchat.AppDbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.androidchat.Daos.UserDao;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.Models.User;

@Database(entities = {User.class, Contact.class, Message.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao UserDao();
}
