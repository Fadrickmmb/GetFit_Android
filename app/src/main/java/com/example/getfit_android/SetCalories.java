package com.example.getfit_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;

public class SetCalories extends AppCompatActivity {


    Slider calorieSlider;

    TextView userTextView, sliderValue;

    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_calories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userTextView = findViewById(R.id.setCaloriesNameView);
        sliderValue = findViewById(R.id.sliderValue);
        calorieSlider = findViewById(R.id.calorieSlider);
        done = findViewById(R.id.setCaloriesDoneButton);

        sliderValue.setText("Calorie Goal: " + (int) calorieSlider.getValue());
        calorieSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                sliderValue.setText("Calorie Goal: " + (int) value);
            }
        });



    }
}