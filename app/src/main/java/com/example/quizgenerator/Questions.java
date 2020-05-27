package com.example.quizgenerator;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Questions {

    public String correctOption;
    public ArrayList<String> options;
    public String question;
    public String index;

    public Questions() {

    }

    public Questions(String index, String correctOption, String question, ArrayList<String> options) {
        this.index = index;
        this.correctOption = correctOption;
        this.options = options;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public String getIndex() {
        return index;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("index", index);
        result.put("question", question);
        result.put("options", options);
        result.put("correctOption", correctOption);

        return result;
    }
}
