package com.example.carhub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class OrderHistory extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView ordersListView;
    private CustomCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        Button homeButton = findViewById(R.id.homeButton);
        Button profileButton = findViewById(R.id.profileButton);
        homeButton.setOnClickListener(v -> {
            // Smooth transition to ProfileActivity
            Intent homeIntent = new Intent(OrderHistory.this, home.class);
            startActivity(homeIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Smooth transition effect
        });
        profileButton.setOnClickListener(v -> {
            // Smooth transition to ProfileActivity
            Intent profileIntent = new Intent(OrderHistory.this, ProfileActivity.class);
            startActivity(profileIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Smooth transition effect
        });

        dbHelper = new DatabaseHelper(this);
        ordersListView = findViewById(R.id.ordersListView);

        // Fetch current user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Fetch and display the user's orders
        loadOrderHistory(userId);
    }

    private void loadOrderHistory(String userId) {
        Cursor ordersCursor = dbHelper.getOrdersByUser(userId);
        if (ordersCursor.getCount() == 0) {
            Toast.makeText(this, "No orders found!", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new CustomCursorAdapter(this, ordersCursor, 0, userId);
            ordersListView.setAdapter(adapter);
        }
    }
}