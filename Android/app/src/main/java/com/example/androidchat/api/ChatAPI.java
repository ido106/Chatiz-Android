package com.example.androidchat.api;

import retrofit2.Retrofit;

public class ChatAPI {
    private String BaseURL = "http://10.0.2.2:7038/api/";
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public ChatAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:7038/api/")
    }
}
