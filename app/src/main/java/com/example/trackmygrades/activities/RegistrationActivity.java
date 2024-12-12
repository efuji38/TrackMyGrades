package com.example.trackmygrades.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.TrackMyGradesDatabase;
import com.example.trackmygrades.database.UserDAO;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private TrackMyGradesDatabase db;
    private User user;

    private static final String HARD_CODED_ACCESS_CODE = "cst338"; // Replace with your hardcoded access code.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = TrackMyGradesDatabase.getInstance(getApplicationContext());

        binding.buttonRegister.setOnClickListener(v -> {
            String username = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String confirmPassword = binding.editTextConfirmPassword.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String accessCode = binding.editTextAccessCode.getText().toString();
            int selectedRoleId = binding.radioGroupRole.getCheckedRadioButtonId();
            boolean isTeacher = selectedRoleId == R.id.radioButtonTeacher;

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(accessCode)) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!accessCode.equals(HARD_CODED_ACCESS_CODE)) {
                Toast.makeText(this, "Invalid access code.", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(username, password, email, isTeacher);
            Log.d("Activity", "Calling saveUserToDatabase method");
            saveUserToDatabase(newUser);

            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        });
    }
    private void saveUserToDatabase(User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(MainActivity.TAG, "Inserting user: " + user.getUsername());

                    UserDAO userDao = db.userDAO();
                    userDao.insert(user);

//                    long userId = user.getUserId();
//
//                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putLong("userId", userId);  // Store the userId
//                    editor.apply();

                } catch (Exception e) {
                    Log.e(MainActivity.TAG, "Error inserting user", e);
                }
            }
        }).start();
    }
}