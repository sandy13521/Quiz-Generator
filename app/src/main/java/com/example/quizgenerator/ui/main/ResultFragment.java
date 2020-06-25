package com.example.quizgenerator.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

import com.example.quizgenerator.R;
import com.example.quizgenerator.ResultAdapter;
import com.example.quizgenerator.Results;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultFragment extends Fragment {

    public ListView results;
    public ProgressBar circle;
    public TextView noneCreated;

    //Declaring Variable.
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public static List<Results> resultData;
    public FirebaseUser user;
    public ArrayList<String> hostId;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_result_quiz, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        noneCreated = view.findViewById(R.id.none_hosted);
        results = view.findViewById(R.id.result_quiz);
        circle = view.findViewById(R.id.progress);
        circle.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("hosted");
        resultData = new ArrayList<>();
        hostId = new ArrayList<>();

        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent1 = new Intent(getActivity(), .class);
//                intent1.putExtra("QuizName", quizNames.get(position));
//                startActivity(intent1);
            }
        });

        results.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(getActivity());
                builder
                        .setTitle("Delete Quiz ?")
                        .setMessage("Do you wish to Delete the Results of this Quiz ?\nConfirm !?")
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Log.i("ID", resultData.get(position).getQuizName());
                                Log.i("ID", hostId.get(position));
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("hosted").child(resultData.get(position).getQuizName()).child(hostId.get(position));
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
                resultData.clear();
                hostId.clear();
                for (DataSnapshot quizSnapshot : dataSnapshot.getChildren()) {
                    String q = quizSnapshot.getKey();
                    for (DataSnapshot r : quizSnapshot.getChildren()) {
                        hostId.add(r.getKey());
                        String d = "";
                        String s = "";
                        for (DataSnapshot result : r.getChildren()) {
                            if (result.getKey().equals("date")) {
                                d = (String) result.getValue();
                            }
                            if (result.getKey().equals("score")) {
                                s = (String) result.getValue();
                            }
                        }
                        resultData.add(new Results(s, d, q));
                    }
                }
                if (resultData.size() != 0) {
                    noneCreated.setVisibility(View.GONE);
                }
                circle.setVisibility(View.GONE);
                ResultAdapter adapter = new ResultAdapter(getActivity(), R.layout.custom_list_view_result, resultData);
                results.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Firebase is Broken!!! Shit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
