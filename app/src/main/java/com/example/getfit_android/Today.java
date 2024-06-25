package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Today Screen, Showing the total of the Calories for the day

public class Today extends AppCompatActivity {

    Button back, checkCalories, addMeal, finishDay;
    TextInputEditText mealName, mealInput;
    TextView mealsOfTheDay, totalNutrients;
    ListView mealList;

    ArrayList<Meal> meals = new ArrayList<>();
    MealAdapter mealAdapter;

    RequestQueue requestQueue;

    // Variables to hold total nutrient values
    double totalCalories = 0.0;
    double totalProtein = 0.0;
    double totalFat = 0.0;
    double totalCarbohydrates = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_today);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back = findViewById(R.id.BackToMenu);
        checkCalories = findViewById(R.id.checkCalories);
        addMeal = findViewById(R.id.addMeal);
        finishDay = findViewById(R.id.finishDay);

        mealName = findViewById(R.id.MealName);
        mealInput = findViewById(R.id.newMeal);

        mealsOfTheDay = findViewById(R.id.mealsOfTheDay);
        totalNutrients = findViewById(R.id.totalNutrients);
        mealList = findViewById(R.id.mealList);

        requestQueue = Volley.newRequestQueue(this);

        mealAdapter = new MealAdapter(this, meals);
        mealList.setAdapter(mealAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        finishDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FinishDay.class);
                startActivity(intent);
                finish();
            }
        });

        checkCalories.setOnClickListener(v -> {
            String mealDescription = mealInput.getText().toString().trim();
            if (!mealDescription.isEmpty()) {
                fetchCalories(mealDescription);
            } else {
                Toast.makeText(Today.this, "Please enter a meal description", Toast.LENGTH_SHORT).show();
            }
        });

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mealNameStr = mealName.getText().toString().trim();
                String mealDescription = mealInput.getText().toString().trim();
                if (mealDescription.isEmpty() || mealNameStr.isEmpty()) {
                    Toast.makeText(Today.this, "Please fill all the inputs", Toast.LENGTH_SHORT).show();
                } else {
                    fetchCalories2(mealDescription, mealNameStr);
                }
            }
        });
    }

    private void fetchCalories(String query) {
        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseAndDisplayResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Today.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                mealsOfTheDay.setText("Failed to fetch data");
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("X-Api-Key", "6VVStmeL3WusRAYKusjoAw==8lz8IfitH9UU7t5s");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void parseAndDisplayResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray items = jsonResponse.getJSONArray("items");

            StringBuilder formattedResponse = new StringBuilder();

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                formattedResponse.append("Item ").append(i + 1).append(":\n");
                formattedResponse.append("Name: ").append(item.getString("name")).append("\n");
                formattedResponse.append("Calories: ").append(item.getDouble("calories")).append("\n");
                formattedResponse.append("Protein: ").append(item.getDouble("protein_g")).append(" g\n");
                formattedResponse.append("Fat: ").append(item.getDouble("fat_total_g")).append(" g\n");
                formattedResponse.append("Carbohydrates: ").append(item.getDouble("carbohydrates_total_g")).append(" g\n\n");
            }

            mealsOfTheDay.setText(formattedResponse.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Today.this, "Failed to parse data", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCalories2(String query, String mealNameStr) {
        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseAndAddMeal(response, mealNameStr);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Today.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("X-Api-Key", "6VVStmeL3WusRAYKusjoAw==8lz8IfitH9UU7t5s");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void parseAndAddMeal(String response, String mealNameStr) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray items = jsonResponse.getJSONArray("items");

            double mealCalories = 0.0;
            double mealProtein = 0.0;
            double mealFat = 0.0;
            double mealCarbohydrates = 0.0;

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                mealCalories += item.getDouble("calories");
                mealProtein += item.getDouble("protein_g");
                mealFat += item.getDouble("fat_total_g");
                mealCarbohydrates += item.getDouble("carbohydrates_total_g");
            }

            // Update total values
            totalCalories += mealCalories;
            totalProtein += mealProtein;
            totalFat += mealFat;
            totalCarbohydrates += mealCarbohydrates;

            // Create a new Meal object
            Meal meal = new Meal(mealNameStr, "Calories: " + mealCalories +
                    "\nProtein: " + mealProtein + " g" +
                    "\nFat: " + mealFat + " g" +
                    "\nCarbohydrates: " + mealCarbohydrates + " g");
            meals.add(meal);
            mealAdapter.notifyDataSetChanged();

            // Update the total nutrients display
            totalNutrients.setText("Total Nutrients:\nCalories: " + totalCalories +
                    "\nProtein: " + totalProtein + " g" +
                    "\nFat: " + totalFat + " g" +
                    "\nCarbohydrates: " + totalCarbohydrates + " g");

            mealsOfTheDay.setText("");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(Today.this, "Failed to parse data", Toast.LENGTH_SHORT).show();
        }
    }
}
