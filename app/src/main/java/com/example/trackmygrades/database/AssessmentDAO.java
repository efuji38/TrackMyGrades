package com.example.trackmygrades.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.trackmygrades.database.entities.Assessment;
@Dao
public interface AssessmentDAO {

    @Insert
    void insert(Assessment... assessment);
    @Query("DELETE FROM " + TrackMyGradesDatabase.ASSESSMENT_TABLE)
    void deleteALL();
}

