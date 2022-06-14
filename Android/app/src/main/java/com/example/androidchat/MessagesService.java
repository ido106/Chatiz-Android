package com.example.androidchat;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagesService extends FirebaseMessagingService {
    public MessagesService() {}


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        int a = 1;
    }
}