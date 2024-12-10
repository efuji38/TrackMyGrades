package com.example.trackmygrades.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trackmygrades.R;
import com.example.trackmygrades.databinding.ActivityStudentDashboardBinding;

public class StudentDashboardActivity extends AppCompatActivity {

    ActivityStudentDashboardBinding binding;

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.trackmygrades.activities.MAIN_ACTIVITY_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

    }

    static Intent studentDashboardIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, StudentDashboardActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }
}