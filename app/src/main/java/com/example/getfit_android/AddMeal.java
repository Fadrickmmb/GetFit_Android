package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        backToToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
                finish();
            }
        });

        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfMeal = mealName.getText().toString().trim();
                String descriptionOfMeal = mealDescription.getText().toString().trim();

                if (nameOfMeal.isEmpty() || descriptionOfMeal.isEmpty()){
                    Toast.makeText(AddMeal.this, "Please fill both Inputs", Toast.LENGTH_SHORT).show();
                }else{
                    addMealToTextView(nameOfMeal, descriptionOfMeal);
                    addMealToDB(nameOfMeal, descriptionOfMeal);
                }

            }
        });

    }

    private void addMealToTextView(String name, String description){

        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + description;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        parseAndDisplayResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMeal.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }){
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("X-Api-Key", "6VVStmeL3WusRAYKusjoAw==8lz8IfitH9UU7t5s");
                return params;
            }
        };

    }

    private void parseAndDisplayResponse(String response){

    }

    private void addMealToDB(String name, String description){

    }

}