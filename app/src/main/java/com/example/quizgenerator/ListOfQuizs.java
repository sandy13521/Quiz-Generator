package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

public class ListOfQuizs extends AppCompatActivity {

    public ListView listOfQuiz;
    private DatabaseReference mDatabase;
    public List<String> quizNames;
    public Button addButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quizs);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        //Initializing Variables.
        listOfQuiz = findViewById(R.id.listOfquizs);
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("quiz");
        quizNames = new ArrayList<>();
        addButton = findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfQuizs.this, Guidance.class);
                startActivity(intent);
            }
        });

        listOfQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListOfQuizs.this, listOfQuestions.class);
                intent.putExtra("QuizName", quizNames.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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
