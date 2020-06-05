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

public class listAdapter extends ArrayAdapter<Questions> {

    private Activity context;
    private List<Questions> questionList;

    public listAdapter(Activity context, List<Questions> questionList) {
        super(context, R.layout.custom_list_view, questionList);
        this.context = context;
        this.questionList = questionList;
    }

    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the Layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_list_view, null, true);

        //Get the reference to the view objects
        TextView questionTextView = row.findViewById(R.id.item);

        //Providing the element
        Questions question = questionList.get(position);
        questionTextView.setText(question.getQuestion());
        System.out.println(question.getQuestion());

        return row;
    }
}
