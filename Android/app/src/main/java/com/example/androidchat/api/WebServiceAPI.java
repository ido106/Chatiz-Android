package com.example.androidchat.api;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("contacts")
    Call<List<Contact>> getAllContacts(@Header("Authorization") String authorization);

    @POST("contacts")
    Call<Void> addContact(@Header("Authorization") String authorization, @Body JsonObject contact);

    @GET("contacts/{id}")
    Call<Contact> getContactById(@Header("Authorization") String authorization, @Path("id") String id);

    @PUT("contacts/{id}")
    Call<Void> putContactById(@Header("Authorization") String authorization, @Path("id") String id, @Body JsonObject contact);

    @DELETE("contacts/{id}")
    Call<Void> DeleteContactById(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("contacts/{id}/messages")
    Call<List<Message>> getAllContactMessages(@Header("Authorization") String authorization, @Path("id") String id);

    @POST("contacts/{id}/messages")
    Call<Void> addMessageToContact(@Header("Authorization") String authorization,
                                   @Path("id") String id,
                                   @Body JsonObject message
    );

    @POST("SignIn")
    Call<String> SignIn(@Body JsonObject contact);

    @POST("Register")
    Call<Void> Register(@Body JsonObject contact);


}
