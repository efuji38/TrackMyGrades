package com.example.trackmygrades.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.trackmygrades.database.entities.Grade;

import java.util.List;

@Dao
public interface GradeDAO {
    @Insert
    void insert(Grade grade);
    @Query("DELETE FROM " + TrackMyGradesDatabase.GRADE_TABLE)
    void deleteALL();

    @Transaction
    @Query("SELECT * FROM " + TrackMyGradesDatabase.GRADE_TABLE + " WHERE studentId = :userId")
    LiveData<List<GradeWithAssessment>> getGradesWithAssessmentTitles(int userId);

}
