package com.example.quizgenerator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Question extends AppCompatActivity {

    public EditText question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        TextView header = findViewById(R.id.name);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        question = findViewById(R.id.question);
        String valQuestion = getIntent().getExtras().getString("question");
        question.setText(valQuestion);
        LinearLayout edit = findViewById(R.id.question_edit);
        ArrayList<String> options = getIntent().getExtras().getStringArrayList("options");
        for (String op : options) {
            EditText option = new EditText(this);
            option.setGravity(1);
            option.setText(op);
            option.setPadding(0, 8, 0, 8);
            option.isInEditMode();
            edit.addView(option);

        }
    }
}
