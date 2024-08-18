    package com.example.getfit_android;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.LinearLayout;
    import android.widget.ListView;
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

    import java.util.ArrayList;
    import java.util.List;

    public class History extends AppCompatActivity {

        LinearLayout toProfile, toHistory, toMainScreen, toMealCalories;

        TextView yourLastDays;

        ListView historyListView;

        private FirebaseAuth mAuth;
        private DatabaseReference dDatabase;
        private String userEmail;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_history);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            toProfile = findViewById(R.id.historyProfileNavButton);
            toHistory = findViewById(R.id.historyHistoryNavButton);
            toMainScreen = findViewById(R.id.historyTodayNavButton);
            toMealCalories = findViewById(R.id.historyMealNavButton);


            yourLastDays = findViewById(R.id.yourLastDays);
            historyListView = findViewById(R.id.historyListView);

            mAuth = FirebaseAuth.getInstance();
            dDatabase = FirebaseDatabase.getInstance().getReference("days");

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                userEmail = currentUser.getEmail().replace(".", ",");
                fetchLastDays(userEmail);
            }


            toProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Profile.class);
                    startActivity(intent);
                    finish();
                }
            });

            toHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), History.class);
                    startActivity(intent);
                    finish();
                }
            });

            toMainScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                    startActivity(intent);
                    finish();
                }
            });

            toMealCalories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MealCalories.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        private void fetchLastDays(String userEmail) {
            dDatabase.child(userEmail).orderByKey().limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Day> dayList = new ArrayList<>();
                    for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                        Day day = new Day();

                        // Convert values manually
                        day.setDayId(daySnapshot.getKey());
                        day.setUserEmail(userEmail);
                        day.setDayCalories(getStringValue(daySnapshot.child("dayCalories")));
                        day.setDayProtein(getStringValue(daySnapshot.child("dayProtein")));
                        day.setDayFat(getStringValue(daySnapshot.child("dayFat")));
                        day.setDayCarbs(getStringValue(daySnapshot.child("dayCarbs")));

                        // Handle meals separately if necessary
                        // For simplicity, assuming meals are not handled here

                        dayList.add(day);
                    }
                    HistoryAdapter adapter = new HistoryAdapter(History.this, dayList);
                    historyListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(History.this, "Failed to Retrieve Days", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private String getStringValue(DataSnapshot snapshot) {
            Object value = snapshot.getValue();
            if (value instanceof Long) {
                return ((Long) value).toString();
            }
            return (String) value;
        }



    }