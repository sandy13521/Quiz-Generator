package com.example.quizgenerator;

import java.util.ArrayList;

public class Questions {

    public String correctOption;
    public ArrayList<String> options;
    public String question;

    public Questions() {

    }

    public Questions(String correctOption, String question, ArrayList<String> options) {
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
}
