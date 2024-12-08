package com.example.trackmygrades.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.trackmygrades.database.entities.Grade;
@Dao
public interface GradeDAO {
    @Insert
    void insert(Grade... grade);
    @Query("DELETE FROM " + TrackMyGradesDatabase.GRADE_TABLE)
    void deleteALL();
}
