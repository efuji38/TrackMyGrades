package com.example.trackmygrades.viewHolders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.trackmygrades.database.TrackMyGradesRepository;
import com.example.trackmygrades.database.entities.Assessment;

import java.util.List;

public class ViewAssessmentViewModel extends AndroidViewModel {
    private final TrackMyGradesRepository repository;

    public ViewAssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = TrackMyGradesRepository.getRepository(application);
    }

    public LiveData<List<Assessment>> getAllAssessments(int userId) {
        return repository.getAllAssessments(userId);
    }

}
