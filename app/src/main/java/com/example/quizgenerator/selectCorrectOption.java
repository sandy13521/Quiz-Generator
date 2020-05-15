package com.example.quizgenerator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class selectCorrectOption extends AppCompatActivity {

    public LinearLayout select;
    public TextView questionTextView;
    public ArrayList<String> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_correct_option);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        select = findViewById(R.id.selectLayout);
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
                final TextView option = new TextView(getApplicationContext());
                option.setText(options.get(i));
                option.setBackgroundColor(Color.RED);
                option.setId(i);
                option.setLayoutParams(params);
                option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                option.setTextSize(20);
                option.setVisibility(View.VISIBLE);
                option.setPadding(8, 8, 8, 8);
                option.setOnClickListener(new View.OnClickListener() {
                    boolean status = false;

                    @Override
                    public void onClick(View v) {
                        if (!status) {
                            option.setBackgroundColor(Color.GREEN);
                            option.setSelected(true);
                            status = true;
                        } else {
                            option.setBackgroundColor(Color.RED);
                            option.setSelected(false);
                            status = false;
                        }
                    }
                });
                select.addView(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addQuestion(View v) {
        String correctOption = "None";
        for (int i = 1; i < options.size(); i++) {
            TextView option = findViewById(i);
            if (option.isSelected()) {
                correctOption = option.getText().toString();
                break;
            }
        }
        if (correctOption.equals("None")) {
            System.out.println(correctOption);
        } else {
            Toast.makeText(this, "Select a Correct Option", Toast.LENGTH_SHORT).show();
        }
    }
}







