package com.example.quizgenerator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class selectCorrectOption extends AppCompatActivity {

    //Declaring UI Elements
    public LinearLayout select;
    public TextView questionTextView;

    //Declaring variables.
    public ArrayList<String> options;
    public String correctOption;
    public String question;

    // Declaring Database Reference
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_correct_option);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Getting DataBase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference("quiz");

        select = findViewById(R.id.selectLayout);
        questionTextView = findViewById(R.id.question);

        //Getting the list of options for the question from intent
        options = getIntent().getExtras().getStringArrayList("optionsAndQuestion");

        question = options.get(0);
        questionTextView.append(question);
        questionTextView.setTextSize(20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);

        //Adding Options to the Layout and adding onClick Listener for the same.
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

    //Adding Question to the Firebase when Add button is clicked.
    public void addQuestion(View v) {
        correctOption = "None";
        for (int i = 1; i < options.size(); i++) {
            TextView op = findViewById(i);
            if (op.isSelected()) {
                System.out.println(op.getText());
                correctOption = op.getText().toString();
                break;
            }
        }
        //Checking Correct Option is Selected or Not and responding accordingly.
        if (correctOption.equals("None")) {
            Toast.makeText(this, "Select a Correct Option", Toast.LENGTH_SHORT).show();
        } else {
            addQuestionToFirebase();
        }

    }

    //Adding Data to Firebase.
    public void addQuestionToFirebase() {
        try {
            final DatabaseReference questions = mDatabase.child("test").child("questions");
            String id = questions.push().getKey();
            Questions questionObject = new Questions(correctOption, question, options);
            questions.child(id).setValue(questionObject);
            Intent intent = new Intent(this, listOfQuestions.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
