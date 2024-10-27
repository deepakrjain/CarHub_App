package com.example.carhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText emailField, passwordField, firstNameField, lastNameField;
    private Button registerButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        emailField = findViewById(R.id.reg_email);
        passwordField = findViewById(R.id.reg_password);
        firstNameField = findViewById(R.id.reg_firstName);
        lastNameField = findViewById(R.id.reg_lastName);
        registerButton = findViewById(R.id.reg_register);

        // Handle register button click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameField.getText().toString();
        String lastName = lastNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            passwordField.setError("Password must be at least 6 characters long");
            return;
        }
        if (TextUtils.isEmpty(firstName)) {
            firstNameField.setError("First Name is required");
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameField.setError("Last Name is required");
            return;
        }

        // Create the user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Save user details in Firebase
                String userId = mAuth.getCurrentUser().getUid();
                User user = new User(firstName, lastName, email);
                myRef.child(userId).setValue(user);
                // Redirect to login
                startActivity(new Intent(this, login.class));
                finish();
            } else {
                Toast.makeText(register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
