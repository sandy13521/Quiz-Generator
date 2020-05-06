package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Guidance extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        btn = findViewById(R.id.getgoing);

    }

    public void getGoing(View v) {
        Intent intent = new Intent(this, Name_Quiz.class);
        startActivity(intent);
    }
}