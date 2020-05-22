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

public class ListOfQuizs extends AppCompatActivity {

    public ListView listOfQuiz;
    private DatabaseReference mDatabase;
    public List<String> quizNames;
    public Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quizs);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Initializing Variables.
        listOfQuiz = findViewById(R.id.listOfquizs);
        mDatabase = FirebaseDatabase.getInstance().getReference("quiz");
        quizNames = new ArrayList<>();
        addButton = findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfQuizs.this, Guidance.class);
                startActivity(intent);
            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizNames.clear();
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    quizNames.add(quizSnapshot.getKey());
                }
                QuizAdapter adapter = new QuizAdapter(ListOfQuizs.this, quizNames);
                listOfQuiz.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
