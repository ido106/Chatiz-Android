package com.example.androidchat.api;

import androidx.annotation.NonNull;

import com.example.androidchat.Models.Contact;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    public static final String SERVER_URL = "http://10.0.2.2:7038/api/";

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private String authorization;

    API(String authorization) {
        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.authorization = authorization;

    }


    public List<Contact> getAllContacts() {
        Call<List<Contact>> call = webServiceAPI.getAllContacts(authorization);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contact>> call,
                                   @NonNull Response<List<Contact>> response) {
                List<Contact> contactList = response.body();
                //todo handle with live data
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call, @NonNull Throwable t) {

            }
        });
        return null;
    }


}
