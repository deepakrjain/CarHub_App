package com.example.carhub;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView firstNameView, lastNameView, emailView;
    private Button ordersButton, homeButton, profileButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // If not logged in, redirect to login page
            startActivity(new Intent(ProfileActivity.this, login.class));
            finish(); // Close ProfileActivity
            return;
        }
//        mAuth = FirebaseAuth.getInstance();
//        userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());

        firstNameView = findViewById(R.id.firstNameView);
        lastNameView = findViewById(R.id.lastNameView);
        emailView = findViewById(R.id.emailView);
        ordersButton = findViewById(R.id.ordersButton);
        homeButton = findViewById(R.id.homeButton);
        profileButton = findViewById(R.id.profileButton);

        // Load user details from Firebase
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                               .child(currentUser.getUid());
    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String firstName = dataSnapshot.child("firstName").getValue(String.class);
            String lastName = dataSnapshot.child("lastName").getValue(String.class);
            String email = dataSnapshot.child("email").getValue(String.class);

            firstNameView.setText("First Name: " + firstName);
            lastNameView.setText("Last Name: " + lastName);
            emailView.setText("Email: " + email);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(ProfileActivity.this, "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });

        // Handle "View Orders" button click
        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistory.class);
            startActivity(intent);
        });

        // By default, Profile button is highlighted
        profileButton.setBackgroundColor(Color.GRAY);

        // Home button click listener
        homeButton.setOnClickListener(v -> {
            // Set home button as active
            homeButton.setBackgroundColor(Color.GRAY);

            // Smooth transition to HomeActivity
            startActivity(new Intent(ProfileActivity.this, home.class));// Smooth transition
            finish();
        });
    }
}