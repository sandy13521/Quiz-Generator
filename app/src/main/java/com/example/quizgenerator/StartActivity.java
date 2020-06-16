package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    //Declaring UI Variables.
    public Switch aSwitch;
    public Button startButton;
    public EditText minutesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Initializing Variables.
        aSwitch = findViewById(R.id.switch_timer);
        startButton = findViewById(R.id.start_button);
        minutesEditText = findViewById(R.id.minutes_edit_text);

        //Handling the Toggle Bar and minutes Edit Text Visibility.
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    minutesEditText.setVisibility(View.VISIBLE);
                } else {
                    minutesEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Start Quiz (Start Button)
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, HostActivity.class);
                intent.putExtra("QuizName", getIntent().getExtras().getString("QuizName"));
                if (aSwitch.isChecked()) {
                    if (!minutesEditText.getText().toString().equals("") && !minutesEditText.getText().toString().equals("0")) {
                        intent.putExtra("Timer", minutesEditText.getText());
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(StartActivity.this);
                        alert.setTitle("Missing Something!");
                        alert.setMessage("Please Enter the Duration of the Quiz in minutes");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                    }
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
