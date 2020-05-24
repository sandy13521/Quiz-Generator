package com.example.quizgenerator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class previewOfQuestion extends AppCompatActivity {

    public LinearLayout preview;
    public TextView questionTextView;
    public ArrayList<String> options;
    public FirebaseAuth mAuth;
    public DatabaseReference reference;
    public Question question;
    public String correctOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_of_question);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        preview = findViewById(R.id.previewLayout);
        questionTextView = findViewById(R.id.question);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("QuestionIndex")) {
            final String quizName = bundle.getString("QuizName");
            String index = bundle.getString("QuestionIndex");
            System.out.println(index);
            System.out.println(quizName);
            if (quizName != null && index != null) {
                reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("quiz").child(quizName).child(index);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        options.clear();
                        for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                            Questions question = questionSnapshot.getValue(Questions.class);
                            options = question.getOptions();
                            correctOption = question.getCorrectOption();
                            questionTextView.setText(question.getQuestion());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Intent Fucked Up", Toast.LENGTH_SHORT).show();
            }
        } else {
            options = getIntent().getExtras().getStringArrayList("optionsAndQuestion");
            String question = options.get(0);
            questionTextView.append(question);
            questionTextView.setTextSize(20);
        }


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);


        try {
            for (int i = 1; i < options.size(); i++) {
                TextView option = new TextView(getApplicationContext());
                if (options.get(i).equals(correctOption)) {
                    option.setBackgroundColor(Color.GREEN);
                } else {
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

    //Directing to next page to select the correct option for the question.
    public void selectOption(View v) {
        Intent intent = new Intent(this, selectCorrectOption.class);
        intent.putExtra("optionsAndQuestion", options);
        startActivity(intent);
    }
}