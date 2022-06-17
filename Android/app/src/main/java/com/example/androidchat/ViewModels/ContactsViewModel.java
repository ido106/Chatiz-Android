package com.example.androidchat.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidchat.Models.Contact;

import java.util.List;

public class ContactsViewModel extends ViewModel {

    private MutableLiveData<List<Contact>> liveData;


    public MutableLiveData<List<Contact>> getLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        return liveData;
    }
}
