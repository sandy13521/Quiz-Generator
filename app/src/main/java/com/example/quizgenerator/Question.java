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

    //Declaring Variable.
    public EditText questionEditText;
    public LinearLayout edit;
    public int id = 1;
    public String question;
    public ArrayList<String> options;
    public Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        TextView header = findViewById(R.id.name);

        //Removing ActionBar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        bundle = getIntent().getExtras();

        //Question
        questionEditText = findViewById(R.id.question);
        questionEditText.setText(bundle.getString("question"));
        questionEditText.setId(0);

        //Linear Layout
        edit = findViewById(R.id.question_edit);

        //Creating Edit Text Fields for options
        options = bundle.getStringArrayList("options");
        for (String op : options) {
            if (op != null && op.length() != 0) {
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
    }

    //Redirecting to Preview Activity to show the preview of the question.
    public void previewQuestion(View v) {
        ArrayList<String> edited = new ArrayList<>();
        EditText q = findViewById(0);
        String question = q.getText().toString();
        for (int i = 1; i <= id; i++) {
            EditText op = findViewById(i);
            if (op != null) {
                String o = op.getText().toString();
                if (!o.matches("")) {
                    edited.add(o);
                }
            }
        }
        Intent intent = new Intent(this, previewOfQuestion.class);
        intent.putExtra("question", question);
        intent.putExtra("options", edited);
        intent.putExtra("QuizName", getIntent().getExtras().getString("QuizName"));
        if (bundle.containsKey("QuestionIndex")) {
            intent.putExtra("QuestionIndex", bundle.getString("QuestionIndex"));
            intent.putExtra("update", true);
        }
        startActivity(intent);
    }

    //Handling Addition of new Edit text for Options in case it is not picked up by Firebas eml.
    public void addEditText(View v) {
        EditText option = new EditText(this);
        option.setId(id++);
        edit.addView(option);
    }
}