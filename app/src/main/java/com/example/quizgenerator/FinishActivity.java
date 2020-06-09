package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class FinishActivity extends AppCompatActivity {

    public TextView quizNameTextView;
    public TextView scoreTextView;
    public Button homeButton;
    public Bundle bundle;
    public LinearLayout scoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        quizNameTextView = findViewById(R.id.quiz_name_text_view);
        scoreLayout = findViewById(R.id.layout_score);
        scoreTextView = findViewById(R.id.score_text_view);
        bundle = getIntent().getExtras();
        homeButton = findViewById(R.id.home);
        quizNameTextView.setText(bundle.getString("QuizName"));
        String score = "" + bundle.getInt("score") + '/' + bundle.getInt("totalQuestion");
        int scoreInt = bundle.getInt("score");
        int totalQuestions = bundle.getInt("totalQuestion");
        int percentage = (scoreInt / totalQuestions) * 100;
        Log.i("percentage", "" + percentage);
        scoreTextView.setText(score);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
