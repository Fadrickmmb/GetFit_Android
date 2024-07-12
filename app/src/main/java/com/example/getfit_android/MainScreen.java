package com.example.getfit_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainScreen extends AppCompatActivity {

    TextView mainScreenName, mainScreenCalories, addMeal;
    ListView mealsList;
    Button endDayButton;

    ProgressBar calorieBar;


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

        mainScreenName = findViewById(R.id.mainScreenName);
        endDayButton = findViewById(R.id.mainScreenEndButton);
        mainScreenCalories = findViewById(R.id.mainScreenCalories);
        addMeal = findViewById(R.id.mainScreenAddMeal);

        mealsList = findViewById(R.id.mainScreenMeals);


        calorieBar = findViewById(R.id.calorieBar);







    }
}