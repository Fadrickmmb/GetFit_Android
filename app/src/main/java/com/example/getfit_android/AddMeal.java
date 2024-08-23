package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddMeal extends AppCompatActivity {

    EditText mealName, mealDescription;
    Button addMeal, backToToday;
    TextView apiResponse;
    RequestQueue requestQueue;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_meal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestQueue = Volley.newRequestQueue(this);
        backToToday = findViewById(R.id.addMealBackToMain);
        mealName = findViewById(R.id.addMealmealName);
        mealDescription = findViewById(R.id.addMealmealDescription);
        apiResponse = findViewById(R.id.addMealTextView);
        addMeal = findViewById(R.id.addMealAddButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail().replace(".", ",");
            mDatabase = FirebaseDatabase.getInstance().getReference("days").child(userEmail);
        } else {
            Intent intent = new Intent(getApplicationContext(), Launcher.class);
            startActivity(intent);
            finish();
        }

        backToToday.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
            startActivity(intent);
            finish();
        });

        addMeal.setOnClickListener(view -> {
            String nameOfMeal = mealName.getText().toString().trim();
            String descriptionOfMeal = mealDescription.getText().toString().trim();

            if (nameOfMeal.isEmpty() || descriptionOfMeal.isEmpty()) {
                Toast.makeText(AddMeal.this, "Please fill both Inputs", Toast.LENGTH_SHORT).show();
            } else {
                addMealToTextView(nameOfMeal, descriptionOfMeal);
            }
        });
    }

    private void addMealToTextView(String name, String query) {
        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> parseAndDisplayResponse(response, name),
                error -> Toast.makeText(AddMeal.this, "Failed to fetch data", Toast.LENGTH_SHORT).show()) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("X-Api-Key", "6VVStmeL3WusRAYKusjoAw==8lz8IfitH9UU7t5s");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void parseAndDisplayResponse(String response, String mealName) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray items = jsonResponse.getJSONArray("items");
            boolean isValid = false;


            if (items.length() > 0) {
                JSONObject item = items.getJSONObject(0);
                double calories = item.getDouble("calories");
                double protein = item.getDouble("protein_g");
                double fat = item.getDouble("fat_total_g");
                double carbs = item.getDouble("carbohydrates_total_g");

                if (calories > 0){
                    Meal newMeal = new Meal(mealName, item.getString("name"), calories, protein, fat, carbs);
                    addMealToDatabase(newMeal);
                }else{
                    Toast.makeText(this, "The Meal has 0 Calories", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "No nutritional data found.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to Parse Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMealToDatabase(Meal newMeal) {
        mDatabase.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String highestDayId = null;

                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                    String dayId = daySnapshot.getKey();
                    if (highestDayId == null || Integer.parseInt(dayId) > Integer.parseInt(highestDayId)) {
                        highestDayId = dayId;
                    }
                }

                if (highestDayId != null) {
                    DatabaseReference mealsRef = mDatabase.child(highestDayId).child("meals");
                    mealsRef.push().setValue(newMeal);

                    Toast.makeText(AddMeal.this, "Meal added to Day " + highestDayId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddMeal.this, "No day found to add the meal.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddMeal.this, "Failed to retrieve days.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
