package com.example.quizgenerator;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HostActivity extends AppCompatActivity {

    //Declaring UI variables.
    public Button previousButton;
    public Button nextButton;
    public Button finishButton;
    public LinearLayout questionLayout;
    public TextView questionNumber;
    public Chronometer timer;

    //Declaring Variables.
    public String quizName;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public FirebaseDatabase mDatabse;
    public List<Questions> questions;
    public Bundle bundle;
    public int curQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Initializing Variables.
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        finishButton = findViewById(R.id.finish_button);
        timer = findViewById(R.id.timer);
        questionLayout = findViewById(R.id.question_layout);
        questionNumber = findViewById(R.id.question_number);
        mAuth = FirebaseAuth.getInstance();
        mDatabse = FirebaseDatabase.getInstance();
        bundle = getIntent().getExtras();
        quizName = bundle.getString("QuizName");
        user = mAuth.getCurrentUser();
        curQuestion = 0;
        questions = new ArrayList<>();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(HostActivity.this);
                builder
                        .setTitle("Finish Quiz ! ")
                        .setMessage("Do You Wish to Finish this Quiz !?")
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
//                                Intent intent = new Intent(HostActivity.this, ScoreActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        }).create().show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < questionLayout.getChildCount(); i++) {
                    questionLayout.removeViewAt(i);
                }
                if (curQuestion != questions.size()) {
                    curQuestion += 1;

                    AddTextViews();
                    if (curQuestion == questions.size()) {
                        nextButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 1; i < questionLayout.getChildCount(); i++) {
                    questionLayout.removeViewAt(i);
                }

                if (curQuestion != 0) {
                    curQuestion -= 1;
                    AddTextViews();
                    if (curQuestion == 0) {
                        previousButton.setVisibility(View.INVISIBLE);
                    }
                    if (curQuestion != questions.size()) {
                        nextButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //Retrieving All the questions from Database.
        DatabaseReference reference = mDatabse.getReference().child("users").child(user.getUid()).child("quiz").child(quizName);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapShot : dataSnapshot.getChildren()) {
                    Questions gotQuestion = questionSnapShot.getValue(Questions.class);
                    questions.add(gotQuestion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        AddTextViews();
    }

    public void AddTextViews() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);

        try {
            List<String> options = questions.get(curQuestion).getOptions();
            for (int i = 0; i < options.size(); i++) {
                final TextView option = new TextView(getApplicationContext());
                option.setBackgroundColor(Color.BLUE);
                option.setText(options.get(i));
                option.setTextSize(20);
                option.setId(i);
                option.setTextColor(Color.WHITE);
                option.setLayoutParams(params);
                option.setPadding(8, 8, 8, 8);
                option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                option.setTextSize(20);
                option.setVisibility(View.VISIBLE);
                option.setOnClickListener(new View.OnClickListener() {
                    boolean status = false;

                    @Override
                    public void onClick(View v) {
                        if (!status) {
                            option.setBackgroundColor(Color.GREEN);
                            option.setSelected(true);
                            status = true;
                        } else {
                            option.setBackgroundColor(Color.BLUE);
                            option.setSelected(false);
                            status = false;
                        }
                    }
                });
                questionLayout.addView(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
