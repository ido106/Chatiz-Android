package com.example.androidchat.api;

import androidx.room.Room;

import com.example.androidchat.AppDB;
import com.example.androidchat.ChatDao;
import com.example.androidchat.Models.User;
import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.R;
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
        String s = MyApplication.context.getString(R.string.BaseURL);
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseURL))
                .addConverterFactory(GsonConverterFactory.create())
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

    public void SignIn(String username, String password) {
        // not yet implemented
        //String firebaseToken = MyApplication.firebaseToken;

        JsonObject connect = new JsonObject();
        connect.addProperty("username", username);
        connect.addProperty("password", password);

        Call<String> responseToken = webServiceAPI.SignIn(connect);
        responseToken.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                MyApplication.jwtToken = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyApplication.jwtToken = "SignIn failed in ChatAPI";
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
                String failed = "Failed on Register";
            }
        });
    }
}
