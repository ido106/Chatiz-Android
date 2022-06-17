package com.example.androidchat.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidchat.Models.Message;
import com.example.androidchat.repositories.UserRepository;

import java.util.List;

public class MessageViewModel extends ViewModel {

    private UserRepository userRepository;

    private LiveData<List<Message>> liveData;

    MessageViewModel(Context context) {
        userRepository = new UserRepository(context);
        liveData = userRepository.getMessageLstData();
    }

    public LiveData<List<Message>> getLiveData() {
        return liveData;
    }
}
