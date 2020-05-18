package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
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
    public int id = 1;

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

        int question_id = 0;

        question.setId(question_id);
        edit = findViewById(R.id.question_edit);

        ArrayList<String> options = getIntent().getExtras().getStringArrayList("options");
        for (String op : options) {
            EditText option = new EditText(this);
            option.setGravity(1);
            option.setId(id);
            option.setText(op);
            option.setPadding(0, 8, 0, 8);
            option.isInEditMode();
            edit.addView(option);
            id += 1;
        }
    }

    public void previewQuestion(View v) {
        ArrayList<String> edited = new ArrayList<>();
        int question_options_count = edit.getChildCount();
        EditText q = findViewById(0);
        edited.add(q.getText().toString());
        for (int i = 1; i <= question_options_count; i++) {
            EditText op = findViewById(i);
            if (op != null) {
                String o = "" + op.getText();
                if (o != "") {
                    edited.add(o);
                }
            }
        }
        Intent intent = new Intent(this, previewOfQuestion.class);
        intent.putExtra("optionsAndQuestion", edited);
        startActivity(intent);
    }
}
