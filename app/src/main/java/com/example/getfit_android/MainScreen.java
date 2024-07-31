package com.example.getfit_android;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class MainScreen extends AppCompatActivity {

    TextView mainScreenName, mainScreenCalories, addMeal;
    ListView mealsList;
    Button endDayButton;

    LinearLayout todayNavButton, mealNavButton, historyNavButton, profileNavButton;

    ProgressBar calorieBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        todayNavButton = findViewById(R.id.mainScreenTodayNavButton);
        mealNavButton = findViewById(R.id.mainScreenMealNavButton);
        historyNavButton = findViewById(R.id.mainScreenHistoryNavButton);
        profileNavButton = findViewById(R.id.mainScreenProfileNavButton);

        mainScreenName = findViewById(R.id.mainScreenName);
        endDayButton = findViewById(R.id.mainScreenEndButton);
        mainScreenCalories = findViewById(R.id.mainScreenCalories);
        addMeal = findViewById(R.id.mainScreenAddMeal);

        mealsList = findViewById(R.id.mainScreenMeals);

        calorieBar = findViewById(R.id.calorieBar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            fetchUserInfo(userEmail);
        }


        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(MainScreen.this);
                dialog.setContentView(R.layout.popup_add_meal);
                dialog.setCancelable(true);
                dialog.show();

                EditText popUpMealName = dialog.findViewById(R.id.popUpMealName);
                EditText popUpMealDescription = dialog.findViewById(R.id.popUpMeal);
                TextView popUpResult = dialog.findViewById(R.id.popUpResult);
                Button popUpAddButton = dialog.findViewById(R.id.popUpAddButton);

                popUpAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mealName = popUpMealName.getText().toString().trim();
                        String mealDescription = popUpMealDescription.getText().toString().trim();

                        if (mealName.isEmpty() || mealDescription.isEmpty()){
                            Toast.makeText(MainScreen.this, "Please enter both Meal Name and Meal Description", Toast.LENGTH_SHORT).show();
                        }else{
                            fetchCalories(mealDescription, mealName);
                        }
                    }
                });




            }
        });



        // BOTTOM NAV BAR REDIRECTION - 1/4
        todayNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
                finish();
            }
        });

        // BOTTOM NAV BAR REDIRECTION - 2/4
        mealNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealCalories.class);
                startActivity(intent);
                finish();
            }
        });

        // BOTTOM NAV BAR REDIRECTION - 3/4
        historyNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
                finish();
            }
        });

        // BOTTOM NAV BAR REDIRECTION - 4/4
        profileNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void fetchUserInfo(String email){
        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        String userName = userSnapshot.child("name").getValue(String.class);
                        int calorieGoalInt = userSnapshot.child("calorieGoal").getValue(Integer.class);
                        String calorieGoal = String.valueOf(calorieGoalInt);
                        mainScreenName.setText(userName);
                        mainScreenCalories.setText(calorieGoal);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainScreen.this, "Error connecting to the Database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchCalories(String query, String mealName){
        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseAndDisplay(response, mealName);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainScreen.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        }){
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("X-Api-Key", "6VVStmeL3WusRAYKusjoAw==8lz8IfitH9UU7t5s");
                return params;
            }
        };

    }

    private void parseAndDisplay(String response, String mealNameString){

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

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse data", Toast.LENGTH_SHORT).show();
        }

    }

}