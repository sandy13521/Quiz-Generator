package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Question extends AppCompatActivity {

    public EditText question;
    public LinearLayout edit;

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
        edit = findViewById(R.id.question_edit);
        ArrayList<String> options = getIntent().getExtras().getStringArrayList("options");
        int id = 1;
        for (String op : options) {
            EditText option = new EditText(this);
            option.setGravity(1);
            option.setId(id);
            option.setText(op);
            option.setPadding(0, 8, 0, 8);
            option.isInEditMode();
            edit.addView(option);

        }
    }

    public void previewQuestion(View v) {
        ArrayList<Editable> edited = new ArrayList<>();
        int question_options_count = edit.getChildCount();
        for (int i = 1; i <= question_options_count; i++) {
            EditText child = (EditText) edit.getChildAt(i);
            edited.add(child.getText());
        }
        Intent intent = new Intent(this, previewOfQuestion.class);
        intent.putExtra("data", edited);
        startActivity(intent);
    }
}
