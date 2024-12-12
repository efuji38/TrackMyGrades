package com.example.trackmygrades.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.trackmygrades.database.entities.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User... user);
    @Query("DELETE FROM " + TrackMyGradesDatabase.USER_TABLE)
    void deleteALL();

    @Query("SELECT * FROM " + TrackMyGradesDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + TrackMyGradesDatabase.USER_TABLE + " WHERE userId == :userId")
    LiveData<User> getUserByUserId(int userId);
}