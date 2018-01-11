package eamv.dmu17he.lancrewapp.sql;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import eamv.dmu17he.lancrewapp.model.User;


@Dao
public interface userDAO
{
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Insert
    void insertAll(List<User> users);

    @Insert
    void insertUser(User theUser);

    @Update
    void update(User theUser);

    @Delete
    void delete(User theUser);

    @Query("DELETE FROM User")
    public void deleteTable();

    @Query("SELECT * FROM USER WHERE username LIKE :uname")
    public User findUserFromName(String uname);

}