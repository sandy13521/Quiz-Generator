package com.example.quizgenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HostActivity extends AppCompatActivity {

    //Declaring UI variables.
    public Button previousButton;
    public Button nextButton;
    public Button finishButton;
    public LinearLayout questionLayout;
    public TextView questionNumber;
    public TextView timer;
    public TextView questionTextView;
    public ProgressBar progressBar;

    //Declaring Variables.
    public String quizName;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public FirebaseDatabase mDatabse;
    public List<Questions> questions;
    public List<HostedQuestion> hostedQuestions;
    public int[] selectedOptions;
    public Bundle bundle;
    public int curQuestion;
    private long mStartTimeInMillis;
    public int score;

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
        progressBar = findViewById(R.id.progress);
        questionLayout = findViewById(R.id.question_layout);
        questionNumber = findViewById(R.id.question_number);
        questionTextView = findViewById(R.id.question);
        mAuth = FirebaseAuth.getInstance();
        mDatabse = FirebaseDatabase.getInstance();
        bundle = getIntent().getExtras();
        quizName = bundle.getString("QuizName");
        user = mAuth.getCurrentUser();
        curQuestion = 0;
        questions = new ArrayList<>();

        //Checking whether user choose to have a timer or not.
        if (bundle.containsKey("Timer")) {
            String input = bundle.get("Timer").toString();
            mStartTimeInMillis = Long.parseLong(input) * 60000;
            StartTimer();
        } else {
            timer.setVisibility(View.INVISIBLE);
        }

        //Handling Finish button
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
                                Intent intent = new Intent(HostActivity.this, FinishActivity.class);
                                progressBar.setVisibility(View.VISIBLE);
                                computeScoreAndUpdateDb();
                                progressBar.setVisibility(View.GONE);
                                intent.putExtra("score", score);
                                intent.putExtra("totalQuestion", questions.size());
                                intent.putExtra("QuizName", quizName);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();
            }
        });

        //Handling Next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curQuestion != questions.size() - 1) {
                    questionLayout.removeAllViews();
                    curQuestion += 1;
                    AddTextViews(selectedOptions[curQuestion]);
                    if (curQuestion == questions.size() - 1) {
                        nextButton.setVisibility(View.INVISIBLE);
                    }
                    if (previousButton.getVisibility() == View.INVISIBLE) {
                        previousButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //Handling Previous Button
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curQuestion != 0) {
                    questionLayout.removeAllViews();
                    curQuestion -= 1;
                    AddTextViews(selectedOptions[curQuestion]);
                    if (curQuestion == 0) {
                        previousButton.setVisibility(View.INVISIBLE);
                    }
                    if (curQuestion != questions.size() - 1) {
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
                AddTextViews(-1);
                selectedOptions = new int[questions.size()];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Filling layout with question and options.
    public void AddTextViews(int selected) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);

        try {
            String questionNumberString = "" + (curQuestion + 1) + "/" + questions.size();
            questionNumber.setText(questionNumberString);
            questionTextView.setText("Q. ");
            questionTextView.append(questions.get(curQuestion).getQuestion());
            List<String> options = questions.get(curQuestion).getOptions();
            int id = 1;
            for (int i = 0; i < options.size(); i++) {
                final TextView option = new TextView(getApplicationContext());
                if (selected == id) {
                    option.setBackgroundColor(Color.GREEN);
                } else {
                    option.setBackgroundColor(Color.BLUE);
                }
                option.setText(options.get(i));
                option.setTextSize(20);
                option.setId(id);
                id += 1;
                option.setTextColor(Color.WHITE);
                option.setLayoutParams(params);
                option.setPadding(8, 8, 8, 8);
                option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                option.setTextSize(20);
                option.setVisibility(View.VISIBLE);
                option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedOptions[curQuestion] == 0) {
                            selectedOptions[curQuestion] = option.getId();
                            option.setBackgroundColor(Color.GREEN);
                            option.setSelected(true);
                        } else {
                            findViewById(selectedOptions[curQuestion]).setBackgroundColor(Color.BLUE);
                            findViewById(selectedOptions[curQuestion]).setSelected(false);
                            if (option.getId() != selectedOptions[curQuestion]) {
                                selectedOptions[curQuestion] = option.getId();
                                option.setBackgroundColor(Color.GREEN);
                                option.setSelected(true);
                            } else {
                                selectedOptions[curQuestion] = 0;
                            }

                        }
                    }
                });
                questionLayout.addView(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Starting the Timer if the user opts fot it.
    public void StartTimer() {
        new CountDownTimer(mStartTimeInMillis, 1000) {

            public void onTick(long l) {
                mStartTimeInMillis = l;
                updateTime(mStartTimeInMillis);
            }

            public void onFinish() {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(HostActivity.this);
                builder
                        .setTitle("Finish Quiz !")
                        .setMessage("The Time Alloted Finished. Thank You.")
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(HostActivity.this, FinishActivity.class);
                                progressBar.setVisibility(View.VISIBLE);
                                computeScoreAndUpdateDb();
                                progressBar.setVisibility(View.GONE);
                                intent.putExtra("score", score);
                                intent.putExtra("totalQuestion", questions.size());
                                intent.putExtra("QuizName", quizName);
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();
            }
        }.start();
    }

    //Updating Timer in the layout.
    public void updateTime(long mTimeLeftInMillis) {
        int minutes = (int) mTimeLeftInMillis / 60000;
        int seconds = (int) mTimeLeftInMillis % 60000 / 1000;
        String timeLeft;

        timeLeft = "" + minutes;
        timeLeft += ":";
        if (seconds < 10) {
            timeLeft += "0";
        }
        timeLeft += seconds;
        timer.setText(timeLeft);
    }

    //Computing the Score and Adding the hosted quiz to the Firebase Database.
    public void computeScoreAndUpdateDb() {
        score = 0;
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference().child("users").child(user.getUid()).child("hosted").child(quizName);
        String idQuiz = reference.push().getKey();
        final DatabaseReference idReference = database.getReference().child("users").child(user.getUid()).child("hosted").child(quizName).child(idQuiz).child("id");
        idReference.setValue(idQuiz);
        final DatabaseReference dateReference = database.getReference().child("users").child(user.getUid()).child("hosted").child(quizName).child(idQuiz).child("date");
        final DatabaseReference scoreReference = database.getReference().child("users").child(user.getUid()).child("hosted").child(quizName).child(idQuiz).child("score");
        final DatabaseReference hostReference = database.getReference().child("users").child(user.getUid()).child("hosted").child(quizName).child(idQuiz).child("quiz");

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        dateReference.setValue(date);

        for (int i = 0; i < questions.size(); i++) {
            Questions q = questions.get(i);
            String id = hostReference.push().getKey();
            HostedQuestion hostedQuestion;
            if (selectedOptions[i] == 0) {
                hostedQuestion = new HostedQuestion(id, q.getQuestion(), q.getCorrectOption(), q.getOptions(), "None", 1);
            } else {
                if (q.getCorrectOption().equals(q.getOptions().get(selectedOptions[i] - 1))) {
                    hostedQuestion = new HostedQuestion(id, q.getQuestion(), q.getCorrectOption(), q.getOptions(), q.getOptions().get(selectedOptions[i] - 1), 1);
                    score += 1;
                } else {
                    hostedQuestion = new HostedQuestion(id, q.getQuestion(), q.getCorrectOption(), q.getOptions(), q.getOptions().get(selectedOptions[i] - 1), 0);

                }
            }

            hostReference.child(id).setValue(hostedQuestion);
        }
        String scoreString = "" + score + "/" + questions.size();
        scoreReference.setValue(scoreString);
    }

    @Override
    public void onBackPressed() {
    }
}
