package com.example.trackmygrades.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.trackmygrades.database.entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert
    void insert(Assessment assessment);
    @Query("DELETE FROM " + TrackMyGradesDatabase.ASSESSMENT_TABLE)
    void deleteALL();

    @Query("SELECT * FROM " + TrackMyGradesDatabase.ASSESSMENT_TABLE + " WHERE teacherId = :loggedInUserId")
    LiveData<List<Assessment>> getRecordsByUserIdLiveData(int loggedInUserId);

    @Query("SELECT * FROM " + TrackMyGradesDatabase.ASSESSMENT_TABLE)
    List<Assessment> getAllAssessments();
    @Query("SELECT * FROM " + TrackMyGradesDatabase.ASSESSMENT_TABLE + " WHERE teacherId = :teacherId")
    LiveData<List<Assessment>> getAssessmentByTeacherId(int teacherId);

}

