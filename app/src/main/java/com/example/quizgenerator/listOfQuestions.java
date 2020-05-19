package com.example.quizgenerator;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_questions);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Getting Firebase Instance
        mDatabase = FirebaseDatabase.getInstance().getReference("quiz").child("test").child("questions");

        listOfQuestion = new ArrayList<>();
        questionsListView = findViewById(R.id.questions);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                Toast.makeText(getApplicationContext(), "Question is added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Firebase is Broken", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

