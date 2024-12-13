package com.example.trackmygrades.viewHolders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.trackmygrades.database.GradeWithAssessment;
import com.example.trackmygrades.database.TrackMyGradesRepository;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;

import java.util.List;

public class ViewGradesViewModel extends AndroidViewModel {
    private final TrackMyGradesRepository repository;

    public ViewGradesViewModel(@NonNull Application application) {
        super(application);
        repository = TrackMyGradesRepository.getRepository(application);
    }

//    public LiveData<List<Assessment>> getAllGrades(int userId) {
//        return repository.getAllAssessments(userId);
//    }

    public LiveData<List<GradeWithAssessment>> getAllGradesWithAssessments(int userId) {
        return repository.getAllGradesWithAssessments(userId);  // This fetches Grade and associated Assessment data
    }



}