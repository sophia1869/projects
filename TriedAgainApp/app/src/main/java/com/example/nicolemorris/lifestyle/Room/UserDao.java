package com.example.nicolemorris.lifestyle.Room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    void inserUserTable(UserTable ut);

    @Delete
    void deleteUserTable(UserTable ut);

    @Update
    void updateUserTable(UserTable ut);

    @Query("select * from UserTable" )
    List<UserTable> getAll();

    @Query("delete from UserTable")
    void deleteAll();
}
