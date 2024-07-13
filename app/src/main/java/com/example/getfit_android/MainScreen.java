package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        }



        // BOTTOM NAV BAR REDIRECTION - 1/4
        todayNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Today.class);
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
                Intent intent = new Intent(getApplicationContext(), ContactsContract.Profile.class);
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






}