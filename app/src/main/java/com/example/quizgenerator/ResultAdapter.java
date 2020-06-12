package com.example.quizgenerator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class ResultAdapter extends ArrayAdapter<Results> {

    public List<Results> result;
    public int resource;
    public Context context;

    public ResultAdapter(Activity context, int resource, List<Results> result) {
        super(context, resource, result);
        this.context = context;
        this.result = result;
        this.resource = resource;
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        TextView scoreTextView = convertView.findViewById(R.id.score_in_quiz);
        TextView dateTextView = convertView.findViewById(R.id.quiz_date);
        TextView quizNameTextView = convertView.findViewById(R.id.name_of_quiz);

        scoreTextView.setText(result.get(position).getScore());
        dateTextView.setText(result.get(position).getDate());
        quizNameTextView.setText(result.get(position).getQuizName());

        return convertView;
    }
}

