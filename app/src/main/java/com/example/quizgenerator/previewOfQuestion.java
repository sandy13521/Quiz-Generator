package com.example.quizgenerator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class previewOfQuestion extends AppCompatActivity {

    public LinearLayout preview;
    public TextView questionTextView;
    public ArrayList<String> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_of_question);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        preview = findViewById(R.id.previewLayout);
        questionTextView = findViewById(R.id.question);

        options = getIntent().getExtras().getStringArrayList("optionsAndQuestion");
        String question = options.get(0);
        questionTextView.append(question);
        questionTextView.setTextSize(20);
        System.out.println(options.size());
        // TODO : Put options to the bottom of the page

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);


        try {
            for (int i = 1; i < options.size(); i++) {
                TextView option = new TextView(getApplicationContext());
                option.setText(options.get(i));
                option.setBackgroundColor(Color.RED);
                option.setTextSize(20);
                option.setId(i);
                option.setLayoutParams(params);
                option.setPadding(8, 8, 8, 8);
                option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                option.setTextSize(20);
                option.setVisibility(View.VISIBLE);
                preview.addView(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectOption(View v) {
        Intent intent = new Intent(this, selectCorrectOption.class);
        intent.putExtra("optionsAndQuestion", options);
        startActivity(intent);
    }
}