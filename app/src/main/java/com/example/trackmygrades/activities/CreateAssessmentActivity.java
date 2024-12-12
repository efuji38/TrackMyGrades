package com.example.trackmygrades.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.TrackMyGradesDatabase;
import com.example.trackmygrades.database.TrackMyGradesRepository;
import com.example.trackmygrades.database.entities.Assessment;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.databinding.ActivityCreateAssessmentBinding;

import java.time.LocalDateTime;
import java.util.Calendar;

public class CreateAssessmentActivity extends AppCompatActivity {
    ActivityCreateAssessmentBinding binding;

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.trackmygrades.activities.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.trackmygrades.SAVED_INSTANCE_STATE_USERID_KEY";
    private TextView dueDateTextView;
    private Button saveButton;

    private int year, month, dayOfMonth;
    int loggedInUserId = -1;
    private static final int LOGGED_OUT = -1;
    private TrackMyGradesRepository repository;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAssessmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = TrackMyGradesRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        dueDateTextView = binding.dueDateTextView; // Ensure this line exists
        dueDateTextView.setOnClickListener(v -> showDatePickerDialog());

        saveButton = binding.saveButton;
        saveButton.setOnClickListener(v -> saveAssessment());
    }

    private void saveAssessment() {
        String assessmentTitle = binding.assessmentTitle.getText().toString().trim();

        if (assessmentTitle.isEmpty() || binding.dueDateTextView.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String dueDateString = binding.dueDateTextView.getText().toString().trim();

        try {
            String[] dateParts = dueDateString.split("-");
            String formattedDate = String.format("%04d-%02d-%02d",
                    Integer.parseInt(dateParts[0]),  // Year
                    Integer.parseInt(dateParts[1]),  // Month
                    Integer.parseInt(dateParts[2])); // Day

            LocalDateTime dueDateTime = LocalDateTime.parse(formattedDate + "T00:00:00");

            Assessment newAssessment = new Assessment(assessmentTitle, dueDateTime, loggedInUserId);

            TrackMyGradesDatabase database = TrackMyGradesDatabase.getDatabase(this);

            new Thread(() -> {
                database.assessmentDAO().insert(newAssessment);

                runOnUiThread(() -> {
                    Toast.makeText(CreateAssessmentActivity.this, "Assessment saved successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                });
            }).start();
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Error Message: ", e);
            Toast.makeText(CreateAssessmentActivity.this, "Failed to save assessment. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateAssessmentActivity.this,
                 (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String dueDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                    binding.dueDateTextView.setText(dueDate);
                    },
                year, month, dayOfMonth
            );
            datePickerDialog.show();
    }

    private void loginUser(Bundle savedInstanceState){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);;

        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(ACTIVITY_SERVICE, LOGGED_OUT);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateAssessmentActivity.this);
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
