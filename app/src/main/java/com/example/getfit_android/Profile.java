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

        logoutButton = findViewById(R.id.profile_logout_button);
        saveButton = findViewById(R.id.profile_save_button);
        profile_text = findViewById(R.id.profile_text);
        calorie_text = findViewById(R.id.calorie_text);
        nameEdit = findViewById(R.id.profileNameEdit);
        calorieEdit = findViewById(R.id.profileCalorieEdit);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            fetchUserInfo(userEmail);
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfile();

                Toast.makeText(Profile.this, "Saved changes Successfully", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void editProfile(){

    }

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
}
