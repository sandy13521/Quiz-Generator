package com.example.quizgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class Name_Quiz extends AppCompatActivity {

    public static CharSequence quizName;
    private FirebaseAuth mAuth;
    public boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_quiz);

        mAuth = FirebaseAuth.getInstance();
        TextView name = findViewById(R.id.quiz_name);
        quizName = name.getText();
    }

    //Going to next page to capture the image.
    public void openCamera(View v) {
        if (quizName.length() != 0) {
            insertQuiz();
            if (status) {
                Intent intent = new Intent(this, Crop.class);
                intent.putExtra("QuizName", quizName.toString());
                startActivity(intent);
                finish();
            }
        } else {
            Toast toast = Toast.makeText(this, "Enter the Quiz Name", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void insertQuiz() {
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference().child("users").child(user.getUid()).child("quiz");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status = checkAndAddData(reference, dataSnapshot, quizName.toString(), 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("WTF", databaseError.toString());
                Toast.makeText(Name_Quiz.this, "Firebase Database Error Occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Verifying is the tree/path already exists.
    private <T> boolean checkAndAddData(DatabaseReference reference, DataSnapshot dataSnapshot, String key, T value) {
        if (dataSnapshot.child(key).exists()) {
            Toast.makeText(Name_Quiz.this, "Quiz with the Same Name Already Exists !", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}