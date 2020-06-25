package com.example.quizgenerator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class previewOfQuestion extends AppCompatActivity {

    //Declaring UI Variables.
    public LinearLayout preview;
    public TextView questionTextView;
    public Button okButton;
    public Button edit_Button;

    //Declaring Variables.
    public ArrayList<String> options;
    public FirebaseAuth mAuth;
    public String question;
    public String correctOption;
    public String selectedOption;
    public Bundle bundle;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_of_question);

        //Initializing Variables
        okButton = findViewById(R.id.ok_button);
        edit_Button = findViewById(R.id.edit_button);
        preview = findViewById(R.id.previewLayout);
        questionTextView = findViewById(R.id.question);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        options = new ArrayList<String>();
        bundle = getIntent().getExtras();
        final String quizName = bundle.getString("QuizName");

        if (bundle.containsKey("QuestionIndex") && bundle.containsKey("view") && bundle.getBoolean("view")) {
            final String index = bundle.getString("QuestionIndex");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(previewOfQuestion.this, listOfQuestions.class);
                    intent.putExtra("QuizName", quizName);
                    startActivity(intent);
                    finish();
                }
            });

            edit_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(previewOfQuestion.this, Question.class);
                    intent.putExtra("QuizName", quizName);
                    intent.putExtra("QuestionIndex", index);
                    intent.putExtra("question", question);
                    intent.putExtra("options", options);
                    startActivity(intent);
                    finish();
                }
            });


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("quiz").child(quizName);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                        Questions q = questionSnapshot.getValue(Questions.class);
                        if (q.getIndex().equals(index)) {
                            options = q.getOptions();
                            question = q.getQuestion();
                            correctOption = q.getCorrectOption();
                        }
                    }

                    questionTextView.append(question);
                    questionTextView.setTextSize(20);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.setMargins(10, 10, 10, 10);

                    try {
                        for (int i = 0; i < options.size(); i++) {
                            TextView option = new TextView(getApplicationContext());
                            if (options.get(i) != null && options.get(i).equals(correctOption)) {
                                option.setBackgroundColor(Color.GREEN);
                            } else if (options.get(i) != null) {
                                option.setBackgroundColor(Color.RED);
                            }
                            option.setText(options.get(i));
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            options = getIntent().getExtras().getStringArrayList("options");
            question = getIntent().getExtras().getString("question");
            questionTextView.append(question);
            questionTextView.setTextSize(20);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(8, 8, 8, 8);

            try {
                for (int i = 0; i < options.size(); i++) {
                    TextView option = new TextView(getApplicationContext());
                    option.setBackgroundColor(Color.RED);
                    option.setText(options.get(i));
                    option.setTextSize(20);
                    option.setId(i);
                    option.setLayoutParams(params);
                    option.setPadding(8, 16, 8, 16);
                    option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    option.setTextSize(20);
                    option.setVisibility(View.VISIBLE);
                    preview.addView(option);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(previewOfQuestion.this, selectCorrectOption.class);
                    intent.putExtra("QuizName", quizName);
                    intent.putExtra("question", question);
                    intent.putExtra("options", options);
                    if (bundle.containsKey("QuestionIndex") && bundle.containsKey("update") && bundle.getBoolean("update")) {
                        intent.putExtra("update", true);
                        intent.putExtra("QuestionIndex", bundle.getString("QuestionIndex"));
                    }
                    startActivity(intent);
                    finish();
                }
            });

            edit_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(previewOfQuestion.this, Question.class);
                    intent.putExtra("QuizName", getIntent().getExtras().getString("QuizName"));
                    intent.putExtra("question", question);
                    intent.putExtra("options", options);
                    if (bundle.containsKey("QuestionIndex") && bundle.containsKey("update") && bundle.getBoolean("update")) {
                        intent.putExtra("update", true);
                        intent.putExtra("QuestionIndex", bundle.getString("QuestionIndex"));
                    }
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
    }
}
