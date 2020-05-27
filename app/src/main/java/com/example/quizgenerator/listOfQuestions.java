package com.example.quizgenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class listOfQuestions extends AppCompatActivity {

    //Declaring Variables.
    public ListView questionsListView;
    private DatabaseReference mDatabase;
    public List<Questions> listOfQuestion;
    public Button finishButton;
    public Button addQuestionButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public String quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_questions);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Initializing variables.
        listOfQuestion = new ArrayList<>();
        questionsListView = findViewById(R.id.questions);
        finishButton = findViewById(R.id.finish_quiz_button);
        addQuestionButton = findViewById(R.id.add_question_button);

        //Getting Firebase Instance
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        assert user != null;
        if (bundle != null && bundle.containsKey("QuizName")) {
            quizName = bundle.getString("QuizName");
        } else {
            quizName = Name_Quiz.quizName.toString();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("quiz").child(quizName);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listOfQuestions.this, ListOfQuizs.class);
                startActivity(intent);
                finish();
            }
        });

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listOfQuestions.this, Crop.class);
                intent.putExtra("QuizName", quizName);
                startActivity(intent);
                finish();
            }
        });

        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(listOfQuestions.this, previewOfQuestion.class);
                intent.putExtra("QuestionIndex", listOfQuestion.get(position).getIndex());
                intent.putExtra("QuizName", quizName);
                intent.putExtra("view", true);
                startActivity(intent);
                finish();
            }
        });
        questionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(listOfQuestions.this);
                builder
                        .setTitle("Delete Question")
                        .setMessage("Are you sure you want to Delete this Question ?")
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("quiz").child(quizName).child(listOfQuestion.get(position).getIndex());
                                reference.removeValue();
                            }
                        }).create().show();
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //Retrieving and Directing the data in into ListView.
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfQuestion.clear();
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    Questions question = questionSnapshot.getValue(Questions.class);
                    listOfQuestion.add(question);
                }
                listAdapter adapter = new listAdapter(listOfQuestions.this, listOfQuestion);
                questionsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPresses() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        listOfQuestions.super.onBackPressed();
                    }
                }).create().show();
    }
}