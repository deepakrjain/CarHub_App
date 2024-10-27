package com.example.carhub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "carhub.db";
    private static final String TABLE_NAME = "orders";
    private static final String COL_ID = "_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_CAR_NAME = "car_name";
    private static final String COL_FROM_DATE = "from_date";
    private static final String COL_TO_DATE = "to_date";
    private static final String COL_TOTAL_BILL = "total_bill";
    private static final String COL_CAR_IMAGE = "car_image"; // New column for car image

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2); // Incremented version number
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " TEXT, " +
                COL_CAR_NAME + " TEXT, " +
                COL_FROM_DATE + " TEXT, " +
                COL_TO_DATE + " TEXT, " +
                COL_TOTAL_BILL + " INTEGER, " +
                COL_CAR_IMAGE + " INTEGER)"); // Added car_image column
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // If upgrading from a version less than 2
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_CAR_IMAGE + " TEXT"); // Add new column
        }
    }

    // Insert a new order
    public boolean insertOrder(String userId, String carName, String fromDate, String toDate, int totalBill, int carImagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, userId);
        contentValues.put(COL_CAR_NAME, carName);
        contentValues.put(COL_FROM_DATE, fromDate);
        contentValues.put(COL_TO_DATE, toDate);
        contentValues.put(COL_TOTAL_BILL, totalBill);
        contentValues.put(COL_CAR_IMAGE, carImagePath);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Return true if inserted successfully, else false
    }

    // Retrieve orders for a specific user
    public Cursor getOrdersByUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase(); // Use getReadableDatabase for fetching data
        return db.query(TABLE_NAME, null, COL_USER_ID + " = ?", new String[]{userId}, null, null, null);
    }

    // Delete an order by ID
    public int deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}