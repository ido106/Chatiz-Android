package com.example.androidchat.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.repositories.UserRepository;

import java.util.List;

public class ContactsViewModel extends ViewModel {

    private final LiveData<List<Contact>> liveData;


    ContactsViewModel(Context context) {
        UserRepository userRepository = new UserRepository(context);
        liveData = userRepository.getContactsLstData();
    }

    public LiveData<List<Contact>> getLiveData() {
        return liveData;
    }


}
