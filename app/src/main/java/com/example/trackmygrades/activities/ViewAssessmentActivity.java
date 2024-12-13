package com.example.trackmygrades.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.TrackMyGradesDatabase;
import com.example.trackmygrades.database.TrackMyGradesRepository;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.databinding.ActivityViewAssessmentBinding;
import com.example.trackmygrades.viewHolders.ViewAssessmentAdapter;
import com.example.trackmygrades.viewHolders.ViewAssessmentViewModel;

import java.util.List;

public class ViewAssessmentActivity extends AppCompatActivity {

    ActivityViewAssessmentBinding binding;
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.trackmygrades.activities.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.trackmygrades.SAVED_INSTANCE_STATE_USERID_KEY";
    private User user;
    int loggedInUserId = -1;
    private static final int LOGGED_OUT = -1;
    private TrackMyGradesRepository repository;
    private TrackMyGradesDatabase db;
    private ViewAssessmentAdapter adapter;
    private ViewAssessmentViewModel trackMyGradeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAssessmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the database
        db = TrackMyGradesDatabase.getInstance(getApplicationContext());
        repository = TrackMyGradesRepository.getRepository(getApplication());
        loginUser(savedInstanceState);


        // Set up RecyclerView
        trackMyGradeViewModel = new ViewModelProvider(this).get(ViewAssessmentViewModel.class);

        RecyclerView recyclerView = binding.assessmentsRecyclerView;
        final ViewAssessmentAdapter adapter = new ViewAssessmentAdapter(new ViewAssessmentAdapter.AssessmentDiffCallback());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        trackMyGradeViewModel.getAllAssessments(loggedInUserId).observe(this, assessment -> {
            adapter.submitList(assessment);
        });

        binding.backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAssessmentActivity.this, TeacherDashboardActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loginUser(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);;

        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if(this.user != null){
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedPreference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        if(user == null){
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                showLogoutDialog();
                return false;
            }
        });
        return true;
    }



    private void showLogoutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ViewAssessmentActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();

    }

    private void logout() {

        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();
        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);

        startActivity(MainActivity.loginIntentFactory(getApplicationContext()));

    }
    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();

    }
}