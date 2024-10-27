package com.example.carhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {
    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button homeButton = findViewById(R.id.homeButton);
        Button profileButton = findViewById(R.id.profileButton);
        Button button = findViewById(R.id.browseCarsButton);
        Button writeToUsButton = findViewById(R.id.writeToUsButton);
        Button callNowButton = findViewById(R.id.callNowButton);
        Button viewOnMapsButton = findViewById(R.id.viewOnMapsButton);

        // Set homeButton as selected by default
        homeButton.setBackgroundColor(Color.GRAY);

        // Browse cars button click listener
        button.setOnClickListener(v -> startActivity(new Intent(home.this, view_cars.class)));

        // Write to Us button click listener
        writeToUsButton.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gmail.com"));
            startActivity(emailIntent);
        });

        // Call Now button click listener
        callNowButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+919999999999"));
            startActivity(callIntent);
        });

        // View on Maps button click listener
        viewOnMapsButton.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Taj+Mahal,Agra,India");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

       // Profile button click listener
        profileButton.setOnClickListener(v -> {
            // Set profile button as active and darken the color
            profileButton.setBackgroundColor(Color.DKGRAY);
            homeButton.setBackgroundColor(Color.GRAY);

            // Smooth transition to ProfileActivity
            Intent profileIntent = new Intent(home.this, ProfileActivity.class);
            startActivity(profileIntent);// Smooth transition effect
        });
    }
}