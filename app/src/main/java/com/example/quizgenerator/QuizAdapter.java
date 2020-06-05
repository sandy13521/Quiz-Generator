package com.example.quizgenerator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class QuizAdapter extends ArrayAdapter<String> {
    private Activity context;
    List<String> quizList;

    public QuizAdapter(Activity context, List<String> quizList) {
        super(context, R.layout.custom_list_view, quizList);
        this.context = context;
        this.quizList = quizList;
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the Layout
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_list_view, null, true);

        //Get the reference to the view objects
        TextView questionTextView = row.findViewById(R.id.item);

        //Providing the element
        String quizName = quizList.get(position);
        questionTextView.setText(quizName);

        return row;
    }
}

