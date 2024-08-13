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

public class AddMeal extends AppCompatActivity {

    EditText mealName, mealDescription;

    Button addMeal, backToToday;

    TextView apiResponse;


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

    }

    private void addMealToDB(String name, String description){

    }

}