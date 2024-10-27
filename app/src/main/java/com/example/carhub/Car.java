package com.example.carhub;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;

public class Car implements Serializable {
    private String name;
    private int imagePath;
    private String fuelType;
    private String transmission;
    private String mileage;
    private String color;
    private HashMap<String, Integer> prices; // Prices for fuel/transmission combinations

    public Car(String name, int imagePath, String fuelType, String transmission, String mileage, String color, HashMap<String, Integer> prices) {
        this.name = name;
        this.imagePath = imagePath;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.mileage = mileage;
        this.color = color;
        this.prices = prices;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getImagePath() { // New getter for image path
        return imagePath;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getMileage() {
        return mileage;
    }

    public String getColor() {
        return color;
    }

    // Get price range
    public int getMinPrice() {
        return Collections.min(prices.values());
    }

    public int getMaxPrice() {
        return Collections.max(prices.values());
    }

    public int getPrice(String fuelTransmissionCombo) {
        return prices.get(fuelTransmissionCombo);
    }
}