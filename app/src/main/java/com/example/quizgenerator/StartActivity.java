package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    //Declaring Variables.
    public Button yesButton;
    public Button noButton;
    public Button startButton;
    public EditText minutesEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Initializing Variables.
        yesButton = findViewById(R.id.yes_button);
        noButton = findViewById(R.id.no_button);
        startButton = findViewById(R.id.start_button);
        minutesEditText = findViewById(R.id.minutes_edit_text);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.VISIBLE);
                minutesEditText.setVisibility(View.VISIBLE);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, HostActivity.class);
                intent.putExtra("QuizName", getIntent().getExtras().getString("QuizName"));
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, HostActivity.class);
                intent.putExtra("QuizName", getIntent().getExtras().getString("QuizName"));
                if (minutesEditText.getText().toString() != "") {
                    intent.putExtra("Timer", minutesEditText.getText());
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(StartActivity.this);
                    alert.setTitle("Missing Something!");
                    alert.setMessage("Please Enter the Duration of the Quiz in minutes");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }
            }
        });

    }
}
