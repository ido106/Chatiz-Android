package com.example.androidchat.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.room.Room;

import com.example.androidchat.AppDB;
import com.example.androidchat.ChatDao;
import com.example.androidchat.Models.User;
import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.R;
import com.example.androidchat.StartChatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPI {

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private ChatDao chatDao; // we can communicate with the DB with chatDao

    public ChatAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);

        setDefaultSettings();
    }

    private void setDefaultSettings() {
        // create the DB connection
        // "ChatDB" will be the name of the DB
        // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
        AppDB db = Room.databaseBuilder(MyApplication.context, AppDB.class, "ChatDB") // "ChatDB" will be the name of the DB
                .allowMainThreadQueries()  // allow the DB to run on the main thread, it is not supposed to be like this but its okay for now
                .build();

        // now we can get the Dao
        chatDao = db.chatDao();
    }

    public void SignIn(String username, String password, Runnable onSuccess) {
        JsonObject connect = new JsonObject();
        connect.addProperty("username", username);
        connect.addProperty("password", password);
        connect.addProperty("token", MyApplication.firebaseToken);

        Call<String> responseToken = webServiceAPI.SignIn(connect);
        responseToken.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) { // signin success
                    MyApplication.jwtToken = response.body();
                    onSuccess.run();
                    //validations and login to the chat page if correct
                } else { // signin failed
                    MyApplication.jwtToken = null;
                    onSuccess.run();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // nothing here
            }
        });
    }

    public void Register(String username,String nickName, String password) {
        JsonObject register_response = new JsonObject();
        register_response.addProperty("username", username);
        register_response.addProperty("nickName", nickName);
        register_response.addProperty("password", password);

        Call<Void> response = webServiceAPI.Register(register_response);
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String server = MyApplication.context.getString(R.string.ServerURL);
                User user = new User(username,nickName,password, server);
                chatDao.addUser(user);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // nothing here
            }
        });
    }
}
