package com.example.carhub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class CustomCursorAdapter extends CursorAdapter {
    private String userId;
    private DatabaseHelper dbHelper;

    public CustomCursorAdapter(Context context, Cursor cursor, int flags, String userId) {
        super(context, cursor, flags);
        this.userId = userId;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.order_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView carNameTextView = view.findViewById(R.id.carNameView);
        TextView fromDateTextView = view.findViewById(R.id.fromDateView);
        TextView toDateTextView = view.findViewById(R.id.toDateView);
        TextView billAmountTextView = view.findViewById(R.id.billAmountView);
        Button cancelOrderButton = view.findViewById(R.id.cancelOrderButton);
        ImageView carImageView = view.findViewById(R.id.imageView2);

        // Retrieve data using the actual column names
        @SuppressLint("Range") String carName = cursor.getString(cursor.getColumnIndex("car_name"));
        @SuppressLint("Range") String fromDate = cursor.getString(cursor.getColumnIndex("from_date"));
        @SuppressLint("Range") String toDate = cursor.getString(cursor.getColumnIndex("to_date"));
        @SuppressLint("Range") int totalBill = cursor.getInt(cursor.getColumnIndex("total_bill"));
        @SuppressLint("Range") int carImageResourceId = cursor.getInt(cursor.getColumnIndex("car_image")); // Changed to int

        // Set data to views
        carNameTextView.setText(carName);
        fromDateTextView.setText(fromDate);
        toDateTextView.setText(toDate);
        billAmountTextView.setText("₹" + totalBill);

        // Set the car image using the resource ID
        carImageView.setImageResource(carImageResourceId);

        // Set the cancel button's onClickListener
        cancelOrderButton.setOnClickListener(v -> {
            @SuppressLint("Range") int orderId = cursor.getInt(cursor.getColumnIndex("_id"));
            showCancelOrderDialog(context, orderId);
        });
    }

    // Method to show confirmation dialog
    private void showCancelOrderDialog(Context context, final int orderId) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this order?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    boolean isDeleted = deleteOrder(orderId);
                    if (isDeleted) {
                        Toast.makeText(context, "Order Canceled", Toast.LENGTH_SHORT).show();
                        refreshOrdersList();
                    } else {
                        Toast.makeText(context, "Failed to Cancel Order", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    // Delete order method
    private boolean deleteOrder(int orderId) {
        int deletedRows = dbHelper.deleteOrder(orderId);
        return deletedRows > 0;
    }

    private void refreshOrdersList() {
        Cursor newCursor = dbHelper.getOrdersByUser(userId); // Fetch updated orders
        swapCursor(newCursor); // Update the adapter's cursor
    }
}