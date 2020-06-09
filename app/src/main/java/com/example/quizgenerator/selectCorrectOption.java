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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;
    public int id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_correct_option);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        // Getting DataBase Reference
        mAuth = FirebaseAuth.getInstance();

        select = findViewById(R.id.selectLayout);
        questionTextView = findViewById(R.id.question);

        //Getting the list of options for the question from intent
        options = getIntent().getExtras().getStringArrayList("options");

        question = getIntent().getExtras().getString("question");
        questionTextView.append(question);
        questionTextView.setTextSize(20);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);

        //Adding Options to the Layout and adding onClick Listener for the same.
        try {
            for (int i = 0; i < options.size(); i++) {
                final TextView option = new TextView(getApplicationContext());
                option.setText(options.get(i));
                option.setBackgroundColor(Color.RED);
                option.setId(id++);
                option.setLayoutParams(params);
                option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                option.setTextSize(20);
                option.setVisibility(View.VISIBLE);
                option.setPadding(8, 16, 8, 16);
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
        for (int i = 1; i < id; i++) {
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
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("update") && bundle.getBoolean("update")) {
            try {
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference().child("users").child(user.getUid()).child("quiz").child(bundle.getString("QuizName"));
                Questions questionObject = new Questions(bundle.getString("QuestionIndex"), correctOption, question, options);
                reference.child(bundle.getString("QuestionIndex")).setValue(questionObject);
                Intent intent = new Intent(this, listOfQuestions.class);
                intent.putExtra("QuizName", bundle.getString("QuizName"));
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String quizName = bundle.getString("QuizName");
                FirebaseUser user = mAuth.getCurrentUser();
                assert user != null;
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference reference = database.getReference().child("users").child(user.getUid()).child("quiz").child(quizName);
                String id = reference.push().getKey();
                Questions questionObject = new Questions(id, correctOption, question, options);
                reference.child(id).setValue(questionObject);
                Intent intent = new Intent(this, listOfQuestions.class);
                intent.putExtra("QuizName", quizName);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}

