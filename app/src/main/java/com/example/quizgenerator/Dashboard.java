package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    public ImageButton hostQuiz;
    public ImageButton createQuiz;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        mAuth = FirebaseAuth.getInstance();
        hostQuiz = findViewById(R.id.host_quiz);
        createQuiz = findViewById(R.id.create_quiz);

        hostQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ListOfQuizs.class);
                startActivity(intent);
            }
        });

        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Name_Quiz.class);
                startActivity(intent);
            }
        });
    }

    public void logout(View v) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
