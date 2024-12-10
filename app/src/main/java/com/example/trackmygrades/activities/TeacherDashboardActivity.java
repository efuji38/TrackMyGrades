package com.example.trackmygrades.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import com.example.trackmygrades.R;
import com.example.trackmygrades.databinding.ActivityTeacherDashboardBinding;

public class TeacherDashboardActivity extends AppCompatActivity {

    ActivityTeacherDashboardBinding binding;

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.trackmygrades.activities.MAIN_ACTIVITY_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

    }

    static Intent teacherDashboardIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, TeacherDashboardActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }
}