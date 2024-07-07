package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SetGoal extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    TextView username;
    LinearLayout profile, today, history, meal;
    FloatingActionButton setGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_goal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        profile = findViewById(R.id.profileNavButton);
        today = findViewById(R.id.todayNavButton);
        history = findViewById(R.id.historyNavButton);
        meal = findViewById(R.id.mealNavButton);
        setGoal = findViewById(R.id.setGoalButton);
        username = findViewById(R.id.setGoalUsername);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Launcher.class);
            startActivity(intent);
            finish();
        }
        else {
            username.setText("Hey, " + getIntent().getStringExtra("name"));
        }


        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Today.class);
                startActivity(intent);
                finish();
            }
        });

        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealCalories.class);
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), History.class);
                startActivity(intent);
                finish();
            }
        });

    }



    public void getDBInfo(){



    }
}