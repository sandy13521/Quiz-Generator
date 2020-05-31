package com.example.quizgenerator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    public FloatingActionButton addButton;
    private FirebaseAuth mAuth;
    public TextView noneCreated;
    public ProgressBar circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quizs);
        //Removing ActionBar
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        //Initializing Variables.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        noneCreated = findViewById(R.id.noQuiz);
        circle = findViewById(R.id.progress);
        circle.setVisibility(View.VISIBLE);
        listOfQuiz = findViewById(R.id.listOfquizs);
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("quiz");
        quizNames = new ArrayList<>();
        addButton = findViewById(R.id.add);

        //Add Quiz Button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfQuizs.this, Guidance.class);
                startActivity(intent);
                finish();
            }
        });

        //Handling the On Click Listener on the list of quizzes
        listOfQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListOfQuizs.this, listOfQuestions.class);
                intent.putExtra("QuizName", quizNames.get(position));
                startActivity(intent);
            }
        });

        listOfQuiz.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(ListOfQuizs.this);
                builder
                        .setTitle("Host Quiz!")
                        .setMessage("Shall we continue?")
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(ListOfQuizs.this, StartActivity.class);
                                intent.putExtra("QuizName", quizNames.get(position));
                                startActivity(intent);
                                finish();
                            }
                        }).create().show();
                return true;
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
                if (quizNames.size() != 0) {
                    noneCreated.setVisibility(View.GONE);
                }
                circle.setVisibility(View.GONE);
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
