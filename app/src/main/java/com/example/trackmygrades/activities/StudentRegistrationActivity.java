package com.example.trackmygrades.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trackmygrades.R;
import com.example.trackmygrades.database.TrackMyGradesDatabase;
import com.example.trackmygrades.database.UserDAO;
import com.example.trackmygrades.database.entities.User;
import com.example.trackmygrades.databinding.ActivityStudentRegistrationBinding;

public class StudentRegistrationActivity extends AppCompatActivity {

    private ActivityStudentRegistrationBinding binding;
    private TrackMyGradesDatabase db;
    private User user;

    private static final String HARD_CODED_ACCESS_CODE = "cst338"; // Replace with your hardcoded access code.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = TrackMyGradesDatabase.getInstance(getApplicationContext());

        binding.buttonRegister.setOnClickListener(v -> {
            String username = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String confirmPassword = binding.editTextConfirmPassword.getText().toString();
            String email = binding.editTextEmail.getText().toString();
            String accessCode = binding.editTextAccessCode.getText().toString();

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

            User newUser = new User(username, password, email, false);
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

                    Log.d(MainActivity.TAG, "User inserted: " + user.getUsername());

                } catch (Exception e) {
                    Log.e(MainActivity.TAG, "Error inserting user", e);
                }
            }
        }).start();
    }
}