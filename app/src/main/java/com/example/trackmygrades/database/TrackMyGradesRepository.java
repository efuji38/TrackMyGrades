package com.example.trackmygrades.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.trackmygrades.activities.MainActivity;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;
import com.example.trackmygrades.database.entities.User;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TrackMyGradesRepository {

    private final UserDAO userDAO;
    private final AssessmentDAO assessmentDAO;
    private final GradeDAO gradeDAO;

    private static TrackMyGradesRepository repository;

    public TrackMyGradesRepository(Application application) {
        TrackMyGradesDatabase db = TrackMyGradesDatabase.getDatabase(application);
        this.assessmentDAO = db.assessmentDAO();
        this.userDAO = db.userDAO();
        this.gradeDAO = db.gradeDAO();
    }


    public static TrackMyGradesRepository getRepository(Application application){
        if(repository != null){
            return repository;
        }

        Future<TrackMyGradesRepository> future = TrackMyGradesDatabase.databaseWriteExecutor.submit(
                new Callable<TrackMyGradesRepository>() {
                    @Override
                    public TrackMyGradesRepository call() throws Exception {
                        return new TrackMyGradesRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting repository, thread error");
        }
        return null;
    }
    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }


    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<Assessment>> getAllAssessments(int userId) {
        return assessmentDAO.getRecordsByUserIdLiveData(userId);
    }

    public LiveData<List<GradeWithAssessment>> getAllGradesWithAssessments(int userId) {
        return gradeDAO.getGradesWithAssessmentTitles(userId);
    }

}
