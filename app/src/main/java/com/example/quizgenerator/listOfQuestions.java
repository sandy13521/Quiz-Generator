package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_questions);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Getting Firebase Instance
        mDatabase = FirebaseDatabase.getInstance().getReference("quiz").child("test").child("questions");

        //Initializing variables.
        listOfQuestion = new ArrayList<>();
        questionsListView = findViewById(R.id.questions);
        finishButton = findViewById(R.id.finish_quiz_button);
        addQuestionButton = findViewById(R.id.add_question_button);

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

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listOfQuestions.this, ListOfQuizs.class);
                startActivity(intent);
            }
        });

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listOfQuestions.this, Crop.class);
                startActivity(intent);
            }
        });
    }
}

