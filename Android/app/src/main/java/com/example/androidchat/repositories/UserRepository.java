package com.example.androidchat.repositories;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.androidchat.AppDB;
import com.example.androidchat.ChatDao;
import com.example.androidchat.Models.Contact;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {

    private ContactsLstData contactsLstData;
    private ChatDao chatDao;
    private String username;

    class ContactsLstData extends MutableLiveData<List<Contact>> {
        public ContactsLstData() {
            super();
            chatDao = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                    .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                    .build().chatDao();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                contactsLstData.setValue(chatDao.getAllUserContacts(username));
            }).start();
        }
    }
}
