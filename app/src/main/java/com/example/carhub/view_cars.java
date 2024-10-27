package com.example.carhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class view_cars extends AppCompatActivity {

    private List<Car> carList; // List to hold Car objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cars);
        Button homeButton = findViewById(R.id.homeButton);
        Button profileButton = findViewById(R.id.profileButton);
        homeButton.setOnClickListener(v -> {
            // Smooth transition to ProfileActivity
            Intent homeIntent = new Intent(view_cars.this, home.class);
            startActivity(homeIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Smooth transition effect
        });
        profileButton.setOnClickListener(v -> {
            // Smooth transition to ProfileActivity
            Intent profileIntent = new Intent(view_cars.this, ProfileActivity.class);
            startActivity(profileIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Smooth transition effect
        });

        // Initialize the list of cars
        carList = new ArrayList<>();

// Creta price map
        HashMap<String, Integer> cretaPrices = new HashMap<>();
        cretaPrices.put("Petrol/Manual", 999);
        cretaPrices.put("Petrol/Automatic", 1199);
        cretaPrices.put("Diesel/Manual", 1399);
        cretaPrices.put("Diesel/Automatic", 1599);
        carList.add(new Car("Creta", R.drawable.creta, "Petrol/Diesel", "Manual/Automatic", "13-16 km/l", "White", cretaPrices));

        HashMap<String, Integer> scorpioPrices = new HashMap<>();
        scorpioPrices.put("Petrol/Manual", 999);
        scorpioPrices.put("Petrol/Automatic", 1199);
        scorpioPrices.put("Diesel/Manual", 1399);
        scorpioPrices.put("Diesel/Automatic", 1599);
        carList.add(new Car("Scorpio", R.drawable.scorpio, "Petrol/Diesel", "Manual/Automatic", "13-16 km/l", "White", scorpioPrices));

        HashMap<String, Integer> balenoPrices = new HashMap<>();
        balenoPrices.put("Petrol/Manual", 899);
        balenoPrices.put("Petrol/Automatic", 1199);
        carList.add(new Car("Baleno", R.drawable.baleno, "Petrol", "Manual/Automatic", "14-18 km/l", "Blue", balenoPrices));

        HashMap<String, Integer> innovaPrices = new HashMap<>();
        innovaPrices.put("Diesel/Manual", 1499);
        innovaPrices.put("Diesel/Automatic", 1799);
        carList.add(new Car("Innova", R.drawable.innova, "Diesel", "Manual/Automatic", "12-16 km/l", "White", innovaPrices));

        HashMap<String, Integer> brezzaPrices = new HashMap<>();
        brezzaPrices.put("Diesel/Manual", 999);
        brezzaPrices.put("Diesel/Automatic", 1299);
        carList.add(new Car("Brezza", R.drawable.brezza, "Diesel", "Manual/Automatic", "15-19 km/l", "Grey", brezzaPrices));

        HashMap<String, Integer> i20Prices = new HashMap<>();
        i20Prices.put("Petrol/Manual", 899);
        i20Prices.put("Petrol/Automatic", 1199);
        carList.add(new Car("i20", R.drawable.i20, "Petrol", "Manual/Automatic", "12-16 km/l", "Red", i20Prices));

        HashMap<String, Integer> ecosportPrices = new HashMap<>();
        ecosportPrices.put("Petrol/Manual", 999);
        ecosportPrices.put("Diesel/Manual", 1299);
        carList.add(new Car("Ecosport", R.drawable.ecosport, "Petrol/Diesel", "Manual", "13-16 km/l", "White", ecosportPrices));

        HashMap<String, Integer> dusterPrices = new HashMap<>();
        dusterPrices.put("Diesel/Manual", 999);
        dusterPrices.put("Diesel/Automatic", 1299);
        carList.add(new Car("Duster", R.drawable.duster, "Diesel", "Manual", "13-16 km/l", "Brown", dusterPrices));



        ListView lv = findViewById(R.id.lv);
        ArrayAdapter<Car> carAdapter = new ArrayAdapter<Car>(this, R.layout.car_list_item, R.id.textView, carList) {
            @Override
            public View getView(int position, View currentView, ViewGroup parent) {
                View view = super.getView(position, currentView, parent);
                ImageView imageView = view.findViewById(R.id.imageView);
                TextView textView = view.findViewById(R.id.textView);

                // Set car image and name in the list
                imageView.setImageResource(carList.get(position).getImagePath());
                textView.setText(carList.get(position).getName());

                return view;
            }
        };
        lv.setAdapter(carAdapter);

        // Set an item click listener to launch CarDetailsActivity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view_cars.this, CarDetailsActivity.class);

                // Pass the selected Car object to CarDetailsActivity
                intent.putExtra("selectedCar", carList.get(position)); // Serializable object

                // Start the CarDetailsActivity
                startActivity(intent);
            }
        });
    }
}