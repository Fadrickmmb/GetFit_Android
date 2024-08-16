package com.example.getfit_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextName;
    Button buttonReg;

    FirebaseAuth mAuth;

    FirebaseDatabase database;

    DatabaseReference reference;

    ProgressBar progressBar;

    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), SetGoal.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.nameRegister);
        editTextEmail = findViewById(R.id.emailRegister);
        editTextPassword = findViewById(R.id.passwordRegister);
        buttonReg = findViewById(R.id.btn_register);
        textView = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.progressBar);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String name, email, password;
                name = String.valueOf(editTextName.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "Please enter a Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Please enter an Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Please enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {

                                    insertToDatabase();


                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            private void insertToDatabase() {
                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("users");

                                String nameUser = editTextName.getText().toString().trim();
                                String emailUser = editTextEmail.getText().toString().trim();
                                String passwordUser = editTextPassword.getText().toString().trim();
                                int calorieGoal = 2000;

                                // Creating user object
                                Model user = new Model(emailUser, nameUser, passwordUser, calorieGoal, new ArrayList<>());

                                reference.child(nameUser).setValue(user);

                                // Creating the Days table
                                DatabaseReference daysReference = database.getReference("days");

                                // Creating First Day for the User
                                Day day1 = new Day("1", emailUser, new ArrayList<>(), "0", "0", "0", "0");

                                // Replacing "." on the email by ",", because Firebase does not allow table keys with "."
                                daysReference.child(emailUser.replace(".", ",")).child("1").setValue(day1);

                                Toast.makeText(Register.this, "Thank You for creating an Account", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, SetGoal.class);
                                intent.putExtra("email", emailUser);
                                intent.putExtra("name", nameUser);
                                intent.putExtra("password", passwordUser);
                                startActivity(intent);
                                finish();
                            }

                        });
            }
        });



    }
}