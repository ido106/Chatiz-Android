package com.example.androidchat.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.androidchat.AppDB;
import com.example.androidchat.ChatDao;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.api.API;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {

    private ContactsLstData contactsLstData;
    private MessageLstData messageLstData;
    private ChatDao chatDao;
    private API api;


    //todo maybe its better to save them with mutable live data and observe from here, update when
    // user click on different contact or sigh in
    private String username;
    private final MutableLiveData<String> contact;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContact(String contact) {
        this.contact.setValue(contact);
    }


    public UserRepository(Context context) {
        //getting the singleton
        api = API.getInstance();
        this.messageLstData = new MessageLstData();
        this.contactsLstData = new ContactsLstData();
        this.contact = new MutableLiveData<>(null);
        chatDao = Room.databaseBuilder(context, AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build().chatDao();

    }

    class ContactsLstData extends MutableLiveData<List<Contact>> {
        public ContactsLstData( ) {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                contactsLstData.postValue(chatDao.getAllUserContacts(username));
            }).start();
        }
    }

    class MessageLstData extends MutableLiveData<List<Message>> {
        public MessageLstData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            if (username == null || contact.getValue() == null) {
                return;
            }

            new Thread(() -> {
                messageLstData.postValue(chatDao.getUserMessageWithContact(username,
                        contact.getValue()));
            }).start();
        }
    }

    public MessageLstData getMessageLstData() {
        return messageLstData;
    }

    public ContactsLstData getContactsLstData() {
        return contactsLstData;
    }
}
