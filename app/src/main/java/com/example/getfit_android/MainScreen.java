package com.example.getfit_android;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    TextView mainScreenName, mainScreenCalories, addMeal, caloriesNow;
    ListView mealsList;
    Button endDayButton;

    LinearLayout todayNavButton, mealNavButton, historyNavButton, profileNavButton;

    ProgressBar calorieBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, dDatabase;


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
        caloriesNow = findViewById(R.id.caloriesUntilNow);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        dDatabase = FirebaseDatabase.getInstance().getReference("days");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            fetchUserInfo(userEmail);
            String userEmailReplaced = userEmail.replace(".", ",");
            fetchMealsFromCurrentDay(userEmailReplaced);
        }


        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, AddMeal.class);
                startActivity(intent);
                finish();
            }
        });


        endDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    String userEmail = currentUser.getEmail().replace(".", ",");
                    createNewDay(userEmail);
                }
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

                        calorieBar.setMax(calorieGoalInt);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainScreen.this, "Error connecting to the Database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchMealsFromCurrentDay(String userEmailReplaced){
        dDatabase.child(userEmailReplaced).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
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
                    loadMealsFromDay(userEmailReplaced, highestDayId);
                } else {
                    Toast.makeText(MainScreen.this, "No meals found for the current day.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainScreen.this, "Failed to retrieve days.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMealsFromDay(String userEmailReplaced, String dayId) {
        dDatabase.child(userEmailReplaced).child(dayId).child("meals").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Meal> mealList = new ArrayList<>();
                int totalCalories = 0;
                int totalProteins = 0;
                int totalFat = 0;
                int totalCarbs = 0;

                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                    Meal meal = mealSnapshot.getValue(Meal.class);
                    if (meal != null) {
                        mealList.add(meal);
                        totalCalories += meal.getCalories();
                        totalProteins += meal.getProtein_g();
                        totalFat += meal.getFat_total_g();
                        totalCarbs += meal.getCarbohydrates_total_g();
                    }
                }

                Log.d("TotalNutrients", "Calories: " + totalCalories + ", Proteins: " + totalProteins + "g, Fat: " + totalFat + "g, Carbs: " + totalCarbs + "g");


                dDatabase.child(userEmailReplaced).child(dayId).child("dayCalories").setValue(totalCalories);
                dDatabase.child(userEmailReplaced).child(dayId).child("dayProteins").setValue(totalProteins);
                dDatabase.child(userEmailReplaced).child(dayId).child("dayFat").setValue(totalFat);
                dDatabase.child(userEmailReplaced).child(dayId).child("dayCarbs").setValue(totalCarbs)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("DayNutrients", "Day's nutrients updated successfully.");
                            } else {
                                Log.e("DayNutrients", "Failed to update day's nutrients.", task.getException());
                            }
                        });


                calorieBar.setProgress(totalCalories);
                caloriesNow.setText(String.valueOf(totalCalories));

                MainScreenAdapter adapter = new MainScreenAdapter(MainScreen.this, mealList);
                mealsList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainScreen.this, "Failed to retrieve meals.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createNewDay(String userEmail) {

        DatabaseReference userDaysRef = dDatabase.child(userEmail);


        userDaysRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String highestDayId = null;
                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                    String dayId = daySnapshot.getKey();
                    if (highestDayId == null || Integer.parseInt(dayId) > Integer.parseInt(highestDayId)) {
                        highestDayId = dayId;
                    }
                }

                // Determine the new dayId
                int newDayId = (highestDayId != null) ? Integer.parseInt(highestDayId) + 1 : 1;


                Day newDay = new Day(
                        String.valueOf(newDayId),
                        userEmail,
                        new ArrayList<>(),
                        "0",
                        "0",
                        "0",
                        "0"
                );


                userDaysRef.child(String.valueOf(newDayId)).setValue(newDay)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainScreen.this, "New day created successfully!", Toast.LENGTH_SHORT).show();

                                fetchMealsFromCurrentDay(userEmail.replace(".", ","));
                            } else {
                                Toast.makeText(MainScreen.this, "Failed to create new day.", Toast.LENGTH_SHORT).show();
                                Log.e("CreateNewDay", "Error creating new day", task.getException());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainScreen.this, "Failed to retrieve existing days.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}