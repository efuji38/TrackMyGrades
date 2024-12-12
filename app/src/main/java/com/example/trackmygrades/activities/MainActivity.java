package com.example.trackmygrades.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.TrackMyGradesRepository;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "GT_LOG";
    private ActivityMainBinding binding;
    private TrackMyGradesRepository repository;

    private boolean isTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = TrackMyGradesRepository.getRepository(getApplication());



        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }






    private void verifyUser(){
        String username = binding.editTextUsername.getText().toString();
        if(username.isEmpty()){
            toastMaker("Username may not be blank");
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.editTextPassword.getText().toString();
                if (password.equals(user.getPassword())) {
                    // Store user ID in SharedPreferences or pass through Intent
                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(getString(R.string.preference_userId_key), user.getUserId());
                    editor.apply();

                    // Redirect to respective dashboard based on user role
                    if (user.isTeacher()) {
                        startActivity(TeacherDashboardActivity.teacherDashboardIntentFactory(getApplicationContext(), user.getUserId()));
                    } else {
                        startActivity(StudentDashboardActivity.studentDashboardIntentFactory(getApplicationContext(), user.getUserId()));
                    }
                } else {
                    toastMaker("Invalid password");
                    binding.editTextPassword.setSelection(0);
                }
            } else {
                toastMaker(String.format("%s is not a valid username.", username));
                binding.editTextUsername.setSelection(0);
            }
        });
//        userObserver.observe(this, user -> {
//            if(user != null){
//                String password = binding.editTextPassword.getText().toString();
//                if(password.equals(user.getPassword())){
//                    if(user.isTeacher()) {
//                        startActivity(TeacherDashboardActivity.teacherDashboardIntentFactory(getApplicationContext(), user.getUserId()));
//                    } else {
//                        startActivity(StudentDashboardActivity.studentDashboardIntentFactory(getApplicationContext(), user.getUserId()));
//                    }
//                } else {
//                    toastMaker("Invalid password");
//                    binding.editTextPassword.setSelection(0);
//                }
//            } else {
//                toastMaker(String.format("%s is not a valid username.", username));
//                binding.editTextUsername.setSelection(0);
//            }
//        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    static Intent loginIntentFactory(Context context){
        return new Intent(context, MainActivity.class);
    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        repository = GradeTrackerRepository.getRepository(getApplication());
//
//        GradeTrackerDatabase db = GradeTrackerDatabase.getDatabase(getApplicationContext());
//        Log.d(TAG, "Database initialized: " + (db != null));
//
//        // Retrieve the role from the Intent
//        isTeacher = getIntent().getBooleanExtra("IS_TEACHER", false);
//
//        // Set up login button click listener
//        binding.buttonLogin.setOnClickListener(v -> {
//            String username = binding.editTextUsername.getText().toString();
//            String password = binding.editTextPassword.getText().toString();
//
//            if (username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent;
//                if (isTeacher) {
//                    return;
////                    intent = new Intent(this, TeacherDashboardActivity.class);
//                } else {
//                    return;
////                    intent = new Intent(this, StudentDashboardActivity.class);
//                }
////                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        binding = null;
//    }
}
