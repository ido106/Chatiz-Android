package com.example.androidchat.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * This class is a singleton class that communicate with the server following the API
 * from assignment 2
 */
public class API {
    public static final String SERVER_URL = "http://10.0.2.2:7038/api/";

    private final WebServiceAPI webServiceAPI;
    private String authorization;
    private static API instanceAPI;

    private API() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    private API(String authorization) {
        this();
        this.authorization = authorization;
    }

    public static API getInstance() {
        if (instanceAPI == null) {
            return new API();
        }
        return instanceAPI;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }


    public boolean signIn(String username, String password) {
        final String[] key = new String[1];
        key[0] = null;

        Call<String> call = webServiceAPI.signIn(username, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                key[0] = response.body();
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
        if (key[0] != null) {
            this.authorization = key[0];
            return true;
        }
        return false;
    }

    public List<Contact> getAllContacts(MutableLiveData<List<Contact>> listMutableLiveData) {
        if (authorization == null) {
            return null;
        }

        Call<List<Contact>> call = webServiceAPI.getAllContacts(authorization);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contact>> call,
                                   @NonNull Response<List<Contact>> response) {
                listMutableLiveData.setValue(response.body());
                //todo handle with live data
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call, @NonNull Throwable t) {

            }
        });
        return null;
    }

    public List<Message> getAllContactMessages(String contactName,
                                               MutableLiveData<List<Message>> listMutableLiveData) {
        if (authorization == null) {
            return null;
        }
        Call<List<Message>> call = webServiceAPI.getAllContactMessages(authorization, contactName);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,
                                   @NonNull Response<List<Message>> response) {
                listMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                //todo handle failure, maybe return null?
            }
        });

        return null;
    }
}
