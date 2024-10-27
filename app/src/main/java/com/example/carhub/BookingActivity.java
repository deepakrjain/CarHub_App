package com.example.carhub;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {

    private EditText fromDate, toDate;
    private Button confirmBookingButton;
    private TextView userNameView, lastNameView, billAmountView, selectedCarView;
    private RadioGroup fuelTypeGroup, transmissionGroup;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Fetch user details from Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);

                    // Set user details to text views
                    userNameView = findViewById(R.id.userNameView);
                    lastNameView = findViewById(R.id.lastNameView);
                    userNameView.setText("First Name : " + firstName);
                    lastNameView.setText("Last Name : " + lastName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Toast.makeText(BookingActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Get the passed car details
        Car selectedCar = (Car) getIntent().getSerializableExtra("selectedCar");

        // Initialize the RadioGroups for fuel type and transmission
        fuelTypeGroup = findViewById(R.id.fuelTypeGroup);
        transmissionGroup = findViewById(R.id.transmissionGroup);

        // Populate fuel type radio buttons
        String[] fuelOptions = selectedCar.getFuelType().split("/");
        for (String fuel : fuelOptions) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(fuel);
            fuelTypeGroup.addView(radioButton);
        }

        // Populate transmission radio buttons
        String[] transmissionOptions = selectedCar.getTransmission().split("/");
        for (String transmission : transmissionOptions) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(transmission);
            transmissionGroup.addView(radioButton);
        }

        // Date inputs
        fromDate = findViewById(R.id.fromDateInput);
        toDate = findViewById(R.id.toDateInput);
        billAmountView = findViewById(R.id.billAmountView);
        selectedCarView = findViewById(R.id.selectedCarView);
        selectedCarView.setText("Selected Car : " + selectedCar.getName());

        // Handle date selection for "From Date"
        fromDate.setOnClickListener(v -> showDatePickerDialog(fromDate));

        // Handle date selection for "To Date"
        toDate.setOnClickListener(v -> showDatePickerDialog(toDate));

        // Add a TextWatcher to toDate to calculate bill when the user enters a date
        toDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAndDisplayBill(selectedCar);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Confirm booking button
        confirmBookingButton = findViewById(R.id.confirmBookingButton);
        confirmBookingButton.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            String fromDateInputVal = fromDate.getText().toString();
            String toDateInputVal = toDate.getText().toString();
            String selectedCarName = selectedCar.getName();
            int totalBill = calculateAndDisplayBill(selectedCar); // Use the same method for calculation

            // Insert order with car image resource ID
            boolean isInserted = dbHelper.insertOrder(
                    mAuth.getCurrentUser().getUid(),
                    selectedCarName,
                    fromDateInputVal,
                    toDateInputVal,
                    totalBill,
                    selectedCar.getImagePath() // Use the image path now
            );
            if (isInserted) {
                Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, home.class)); // Use your actual home activity class name
            } else {
                Toast.makeText(this, "Booking failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show DatePickerDialog
    private void showDatePickerDialog(final EditText dateInput) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            dateInput.setText(selectedDate);
        }, year, month, day);
        if (dateInput == toDate) {
            // Get the selected "From Date"
            String fromDateInput = fromDate.getText().toString();
            if (!fromDateInput.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date fromDateParsed = dateFormat.parse(fromDateInput);
                    datePickerDialog.getDatePicker().setMinDate(fromDateParsed.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Set the maximum date for "From Date" to today
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }
        datePickerDialog.show();
    }

    // Method to calculate and display the total bill, now returning the bill as int
    private int calculateAndDisplayBill(Car selectedCar) {
        String selectedFuelType = ((RadioButton) findViewById(fuelTypeGroup.getCheckedRadioButtonId())).getText().toString();
        String selectedTransmission = ((RadioButton) findViewById(transmissionGroup.getCheckedRadioButtonId())).getText().toString();
        String from = fromDate.getText().toString();
        String to = toDate.getText().toString();

        if (!from.isEmpty() && !to.isEmpty()) {
            // Get the price for the selected combination of fuel and transmission
            String comboKey = selectedFuelType + "/" + selectedTransmission;
            int pricePerDay = selectedCar.getPrice(comboKey);

            // Calculate the number of rental days
            int numberOfDays = calculateDays(from, to);

            if (numberOfDays > 0) {
                // Calculate the total bill
                int totalBill = pricePerDay * numberOfDays;

                // Show the total bill amount
                billAmountView.setText("Total Bill: ₹" + totalBill);

                return totalBill;  // Return the calculated total bill
            } else {
                billAmountView.setText("Invalid date range");
                return 0; // Return 0 if the date range is invalid
            }
        }
        return 0; // Return 0 if from/to dates are empty
    }

    // Method to calculate the number of days between two dates
    private int calculateDays(String fromDate, String toDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date from = dateFormat.parse(fromDate);
            Date to = dateFormat.parse(toDate);
            long differenceInMilliseconds = to.getTime() - from.getTime();
            return (int) (differenceInMilliseconds / (1000 * 60 * 60 * 24)) + 1; // Add 1 to include both start and end dates
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}