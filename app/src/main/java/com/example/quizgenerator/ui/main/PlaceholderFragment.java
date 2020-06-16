package com.example.quizgenerator.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.quizgenerator.QuizAdapter;
import com.example.quizgenerator.R;
import com.example.quizgenerator.listOfQuestions;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    //Declaring UI Variables.
    public ListView listOfQuiz;
    public FloatingActionButton addButton;
    public TextView noneCreated;
    public ProgressBar circle;

    //Declaring Variable.
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public static List<String> quizNames;
    public FirebaseUser user;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_created_quiz, container, false);
        addButton = myView.findViewById(R.id.add);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Initializing Variables.
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        noneCreated = view.findViewById(R.id.noQuiz);
        circle = view.findViewById(R.id.progress);
        circle.setVisibility(View.VISIBLE);
        listOfQuiz = view.findViewById(R.id.listOfquizs);
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("quiz");
        quizNames = new ArrayList<>();
        listOfQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(getActivity(), listOfQuestions.class);
                intent1.putExtra("QuizName", quizNames.get(position));
                startActivity(intent1);
            }
        });

        listOfQuiz.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(getActivity());
                builder
                        .setTitle("Delete Quiz ?")
                        .setMessage("Do you wish to Delete the Quiz ? \n Confirm !?")
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("quiz").child(quizNames.get(position));
                                reference.setValue(null);
                            }
                        }).create().show();
                return true;
            }
        });

        //Retrieving data from Firebase Database
        mDatabase.addValueEventListener(new ValueEventListener() {
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
                QuizAdapter adapter = new QuizAdapter(getActivity(), quizNames);
                listOfQuiz.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
            }
        });

    }
}