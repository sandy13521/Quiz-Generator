package com.example.quizgenerator;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class previewOfQuestion extends AppCompatActivity {

    public LinearLayout preview;
    public TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_of_question);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        preview = findViewById(R.id.previewLayout);
        questionTextView = findViewById(R.id.question);

        ArrayList<String> options = getIntent().getExtras().getStringArrayList("optionsAndQuestion");
        String question = options.get(0);
        questionTextView.setText(question);
        questionTextView.setTextSize(20);
        // TODO : Options not visible and add onClick method to both the buttons.
        try {
            for (int i = 1; i < options.size(); i++) {
                TextView option = new TextView(this);
                option.setText(options.get(i));
                option.setBackgroundColor(Color.argb(0, 244, 251, 252));
                option.setId(i);
                option.setPadding(8, 8, 8, 8);
                preview.addView(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}