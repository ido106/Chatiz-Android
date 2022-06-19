package com.example.androidchat.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import com.example.androidchat.AppDB;
import com.example.androidchat.ChatDao;
import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.Models.User;
import com.example.androidchat.AppSettings.MyApplication;
import com.example.androidchat.R;
import com.example.androidchat.StartChatActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

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
                    MyApplication.jwtToken = "Bearer " +response.body();
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
                return;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void TransferMessage(String from, String to, String content) {
        JsonObject message = new JsonObject();
        message.addProperty("from", from);
        message.addProperty("to", to);
        message.addProperty("content", content);

        Call<Void> response = webServiceAPI.TransferMessage(message);
        // in order not to crush on runtime
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                return;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                return;
            }
        });

        // todo how to update on local db
        // add to Room
        final int[] newId = {1};
        chatDao.getUserMessageWithContact(to, from).forEach(m -> {
            if (m.getId() > newId[0]) {
                newId[0] = m.getId();
            }
        });
        Message message_to_add = new Message(to, from, newId[0]+1, content, "", false);
        chatDao.addMessage(message_to_add);
    }

    public void Invitation(String from, String to, String server) {

        JsonObject invitation = new JsonObject();
        invitation.addProperty("from", from);
        invitation.addProperty("to", to);
        invitation.addProperty("server", server);

        Call<Void> response = webServiceAPI.Invitation(invitation);
        // in order not to crush on runtime
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                return;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                return;
            }
        });

        // todo how to update on local db
        chatDao.addContact(new Contact(to, from, from, server));
    }

    public void AddContactLocal(String id, String name, String server) {
        if (MyApplication.jwtToken == null) return;

        JsonObject contact = new JsonObject();
        contact.addProperty("id", id);
        contact.addProperty("name", name);
        contact.addProperty("server", server);

        Call<Void> response = webServiceAPI.addContact(MyApplication.jwtToken, contact);
        // in order not to crush on runtime
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                return;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                return;
            }
        });

        // update ROOM
        chatDao.addContact(new Contact(id, MyApplication.connected_user, name, server));
    }

    public void DeleteContact(String id) {
        if (MyApplication.jwtToken == null) return;

        Call<Void> response = webServiceAPI.DeleteContactById(MyApplication.jwtToken, id);
        // in order not to crush on runtime
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                return;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                return;
            }
        });

        // update ROOM
        Contact contact_to_delete = chatDao.getContact(MyApplication.connected_user, id);
        chatDao.deleteContact(contact_to_delete);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddMessage(String id, String content) {
        if (MyApplication.jwtToken == null) return;

        JsonObject msg = new JsonObject();
        msg.addProperty("content", content);
        Call<Void> response = webServiceAPI.AddMessage(MyApplication.jwtToken, id, msg);
        // in order not to crush on runtime
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                return;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                return;
            }
        });

        // add to Room
        final int[] newId = {1};
        chatDao.getUserMessageWithContact(MyApplication.connected_user, id).forEach(m -> {
            if (m.getId() > newId[0]) {
                newId[0] = m.getId();
            }
        });
        Message message_to_add = new Message(MyApplication.connected_user,id, newId[0]+1, content, "", true);
        chatDao.addMessage(message_to_add);


    }

    //todo update local db?
}
