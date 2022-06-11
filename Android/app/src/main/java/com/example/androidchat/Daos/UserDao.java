package com.example.androidchat.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidchat.Models.Contact;
import com.example.androidchat.Models.Message;
import com.example.androidchat.Models.User;

import java.util.List;

/**
 * HERE!!!!!!!!!!!!!!!!!!!!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!
 * !!!!!!!!!!!!!!!!!!!!!!!!!!
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> index();

    @Query("SELECT * FROM user WHERE id = :id")
    User get(int id);

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);


    //make this query work!!!!!!!
    @Query("SELECT * FROM user")
    List<Contact> index(String Username);

    //make this query work!!!!!!!
    @Query("SELECT * FROM contact WHERE id = :id")
    Contact get(String username, int id);

    //maybe need to change the params to let the dao know to which row he need to insert,
    // ask someone or try and check
    @Insert
    void insert(Contact... users);

    @Update
    void update(Contact... users);

    @Delete
    void delete(Contact... users);





    @Query("SELECT * FROM user")
    List<Message> index(String Username, String contact);

    //make this query work!!!!!!!
    @Query("SELECT * FROM contact WHERE id = :id")
    Message get(String username,String contact, int id);


    //maybe need to change the params to let the dao know to which row he need to insert,
    // ask someone or try and check
    @Insert
    void insert(Message... users);

    @Update
    void update(Message... users);

    @Delete
    void delete(Message... users);

}
