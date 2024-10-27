package com.example.carhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class CarDetailsActivity extends AppCompatActivity {

    private ImageView carImageView;
    private TextView carNameTextView, fuelTypeTextView, transmissionTextView, mileageTextView, colorTextView, priceRangeTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        // Get the selected car object
        Car selectedCar = (Car) getIntent().getSerializableExtra("selectedCar");

        // Initialize the views
        carImageView = findViewById(R.id.carImageView);
        carNameTextView = findViewById(R.id.carNameView);
        fuelTypeTextView = findViewById(R.id.fuelTypeView);
        transmissionTextView = findViewById(R.id.transmissionView);
        mileageTextView = findViewById(R.id.mileageView);
        colorTextView = findViewById(R.id.colorView);
        priceRangeTextView = findViewById(R.id.priceRangeView);

        int imagePath = selectedCar.getImagePath();

        carImageView.setImageResource(imagePath);
        carNameTextView.setText(selectedCar.getName());
        fuelTypeTextView.setText("Fuel Type: " + selectedCar.getFuelType());
        transmissionTextView.setText("Transmission: " + selectedCar.getTransmission());
        mileageTextView.setText("Mileage: " + selectedCar.getMileage());
        colorTextView.setText("Color: " + selectedCar.getColor());

        // Get and display the price range
        int minPrice = selectedCar.getMinPrice();
        int maxPrice = selectedCar.getMaxPrice();
        priceRangeTextView.setText("Price Range (per day): ₹" + minPrice + " - ₹" + maxPrice);

        Button bookButton = findViewById(R.id.button);
        bookButton.setOnClickListener(v -> {
            Intent intent = new Intent(CarDetailsActivity.this, BookingActivity.class);
            intent.putExtra("selectedCar", selectedCar); // Pass the selected car details
            startActivity(intent);
        });
    }
}