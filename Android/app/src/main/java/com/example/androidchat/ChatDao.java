package com.example.androidchat;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.Models.ProfilePictureHolder;
import com.example.androidchat.Models.User;

import java.util.List;

@Dao
public interface ChatDao {

    // *** USER ***
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE Username = :username")
    User getUser(String username);

    @Insert
    void addUser(User user);

    @Update
    void updateUser(User username);

    @Delete
    void deleteUser(User username);

    // *** CONTACT ***
    @Query("SELECT * FROM contact")
    List<Contact> getAllDatabaseContacts(); // from db without filtering

    @Query("SELECT * FROM contact WHERE TalkingTo = :username") // from db with filter
    List<Contact> getAllUserContacts(String username);

    @Query("SELECT * FROM contact WHERE Id = :contact AND TalkingTo = :username") // get specific contact
    Contact getContact(String username, String contact);

    @Insert
    void addContact(Contact contact);

    @Update
    void updateContact(Contact contact);
    // todo do i have to support changing the contact's server and nickname like the api ?
    // void updateContact(String username, String contact, String server, String Nickname);

    @Delete
    void deleteContact(Contact contact);

    // *** MESSAGE ***
    @Query("SELECT * FROM message")
    List<Message> getAllDatabaseMessages(); // from db without filtering

    @Query("SELECT * FROM message WHERE `To` = :to AND `From`= :from ORDER BY Id") // from db with filter
    List<Message> getUserMessageWithContact(String from, String to);

    @Query("SELECT * FROM message WHERE `From` = :from AND `To` = :to AND Id = :id") // get specific message
    Message getMessage(String from, String to, int id);

    @Insert
    void addMessage(Message message);

    @Update
    void updateMessage(Message message);

    @Delete
    void deleteMessage(Message message);

    @Query("DELETE FROM user")
    void resetUserTable();

    @Query("DELETE FROM contact")
    void resetContactTable();

    @Query("DELETE FROM message")
    void resetMessageTable();


    @Insert
    void addProfilePictureHolder(ProfilePictureHolder holder);

    @Query("SELECT * FROM profilepictureholder WHERE username =:username")
    ProfilePictureHolder getPictureByUsername(String username);
}
