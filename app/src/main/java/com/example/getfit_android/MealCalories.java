package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MealCalories extends AppCompatActivity {

    Button findCaloriesButton, backToMenuButton;
    TextView responseText, parsedResponse;
    TextInputEditText inputMeal;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal_calories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backToMenuButton = findViewById(R.id.BackToMenu);
        findCaloriesButton = findViewById(R.id.findCaloriesButton);
        responseText = findViewById(R.id.responseText);
        parsedResponse = findViewById(R.id.parsedResponse);
        inputMeal = findViewById(R.id.inputMeal);

        requestQueue = Volley.newRequestQueue(this);


        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findCaloriesButton.setOnClickListener(v -> {
            String mealDescription = inputMeal.getText().toString().trim();
            if (!mealDescription.isEmpty()) {
                fetchCalories(mealDescription);
            } else {
                Toast.makeText(MealCalories.this, "Please enter a meal description", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MealCalories.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                responseText.setText("Failed to fetch data");
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

            parsedResponse.setText(formattedResponse.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MealCalories.this, "Failed to parse data", Toast.LENGTH_SHORT).show();
        }
    }
}
