package com.example.trackmygrades.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.TrackMyGradesDatabase;
import com.example.trackmygrades.database.TrackMyGradesRepository;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.Grade;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.databinding.ActivityAssignGradesBinding;

import java.util.ArrayList;
import java.util.List;

public class AssignGradesActivity extends AppCompatActivity {

    ActivityAssignGradesBinding binding;

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.trackmygrades.activities.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.trackmygrades.SAVED_INSTANCE_STATE_USERID_KEY";
    private User user;
    int loggedInUserId = -1;
    private static final int LOGGED_OUT = -1;
    private TrackMyGradesRepository repository;

    private TrackMyGradesDatabase db;

    private Spinner studentSpinner, assessmentSpinner, gradeSpinner;
    private EditText commentEditText;
    private Button submitButton;

    private int selectedStudentId = -1;
    private int selectedAssessmentId = -1;
    private double selectedGrade = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignGradesBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_assign_grades);

        repository = TrackMyGradesRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        db = TrackMyGradesDatabase.getInstance(getApplicationContext());

        studentSpinner = findViewById(R.id.studentSpinner);
        assessmentSpinner = findViewById(R.id.assessmentSpinner);
        gradeSpinner = findViewById(R.id.gradeSpinner);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);

        loadStudentsIntoSpinner();
        loadAssessmentsIntoSpinner();
        loadGradesIntoSpinner();

        submitButton.setOnClickListener(v -> assignGrade());

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssignGradesActivity.this, TeacherDashboardActivity.class);
                startActivity(intent);
            }
        });


    }

    public void loadStudentsIntoSpinner() {
        new Thread(() -> {
            List<User> students = db.userDAO().getAllStudents(); // Fetch students from DB
            runOnUiThread(() -> {
                Spinner studentSpinner = findViewById(R.id.studentSpinner);

                List<String> studentNames = new ArrayList<>();
                for (User student : students) {
                    studentNames.add(student.getUsername());
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                studentSpinner.setAdapter(spinnerAdapter);

                studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedStudentId = students.get(position).getUserId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedStudentId = -1;
                    }
                });
            });
        }).start();
    }


    private void loadAssessmentsIntoSpinner() {
        new Thread(() -> {
            List<Assessment> assessments = db.assessmentDAO().getAllAssessments();
            runOnUiThread(() -> {
                ArrayAdapter<Assessment> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assessments);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                assessmentSpinner.setAdapter(adapter);

                assessmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedAssessmentId = assessments.get(position).getAssessmentId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedAssessmentId = -1;
                    }
                });
            });
        }).start();
    }

    private void loadGradesIntoSpinner() {
        final Double[] numericGrades = {4.0, 3.0, 2.0, 1.0, 0.0};

        String[] letterGrades = {"A", "B", "C", "D", "F"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, letterGrades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(adapter);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedGrade = numericGrades[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGrade = -1;
            }
        });
    }


    private void assignGrade() {
        if (selectedStudentId == -1 || selectedAssessmentId == -1 || selectedGrade == -1) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = commentEditText.getText().toString().trim();

        new Thread(() -> {
            Grade grade = new Grade(selectedAssessmentId, selectedGrade, comment, selectedStudentId);
            db.gradeDAO().insert(grade);

            runOnUiThread(() -> {
                Toast.makeText(this, "Grade assigned successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AssignGradesActivity.this);
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