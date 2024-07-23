package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    Button logoutButton, saveButton;
    TextView profile_text, calorie_text;

    LinearLayout profileNavButtonProfile, profileNavButtonMeal, profileNavButtonHistory, profileNavButtonToday;

    TextInputEditText nameEdit, calorieEdit;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Profile ID's
        logoutButton = findViewById(R.id.profile_logout_button);
        saveButton = findViewById(R.id.profile_save_button);
        profile_text = findViewById(R.id.profile_text);
        calorie_text = findViewById(R.id.calorie_text);
        nameEdit = findViewById(R.id.profileNameEdit);
        calorieEdit = findViewById(R.id.profileCalorieEdit);

        // Bottom Nav Bar ID's
        profileNavButtonProfile = findViewById(R.id.profileNavButtonProfile);
        profileNavButtonHistory = findViewById(R.id.profileNavButtonHistory);
        profileNavButtonMeal = findViewById(R.id.profileNavButtonMeal);
        profileNavButtonToday = findViewById(R.id.profileNavButtonToday);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            fetchUserInfo(userEmail);
        }


        // LOGOUT BUTTON (Firebase Auth)
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        // SAVE CHANGES TO PROFILE (editProfile Function)
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();
                    editProfile(userEmail);
                }
            }
        });

        // BOTTOM NAV BAR NAVIGATION 1/4
        profileNavButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });

        // BOTTOM NAV BAR NAVIGATION 2/4
        profileNavButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
                finish();
            }
        });

        // BOTTOM NAV BAR NAVIGATION 3/4
        profileNavButtonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
                finish();
            }
        });

        // BOTTOM NAV BAR NAVIGATION 4/4
        profileNavButtonMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealCalories.class);
                startActivity(intent);
                finish();
            }
        });


    }

    // Fetching User Name and Calorie Goal / Displaying on the Input Fields
    private void fetchUserInfo(String email) {
        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userName = userSnapshot.child("name").getValue(String.class);
                        int calorieGoalInt = userSnapshot.child("calorieGoal").getValue(Integer.class);
                        String calorieGoal = String.valueOf(calorieGoalInt);
                        profile_text.setText(userName);
                        calorie_text.setText("Current Calorie Goal: " + calorieGoal);
                        nameEdit.setHint(userName);
                        calorieEdit.setHint(calorieGoal);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Profile.this, "Error connecting to the Database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void editProfile(String email){

        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userName = userSnapshot.child("name").getValue(String.class);
                        int calorieGoalInt = userSnapshot.child("calorieGoal").getValue(Integer.class);
                        String calorieGoal = String.valueOf(calorieGoalInt);

                        String newName = nameEdit.getText().toString().trim();
                        String newCalorie = calorieEdit.getText().toString().trim();

                        if (!newName.isEmpty() && newName != userName){
                            userSnapshot.getRef().child("name").setValue(newName);
                        }

                        if (!newCalorie.isEmpty()){
                            try {
                                int newCalorieGoal = Integer.parseInt(newCalorie);
                                if (newCalorieGoal != calorieGoalInt){
                                    userSnapshot.getRef().child("calorieGoal").setValue(newCalorieGoal);
                                }
                            }catch (NumberFormatException e){
                                Toast.makeText(Profile.this, "We've Encountered an Error", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                }
                Toast.makeText(Profile.this, "Saved changes Successfully", Toast.LENGTH_SHORT).show();
                refreshActivity();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private void refreshActivity(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        startActivity(intent);
        finish();
    }


}
