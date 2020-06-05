package com.example.quizgenerator;

import java.util.ArrayList;

public class HostedQuestion {

    public String correctOption;
    public ArrayList<String> options;
    public String question;
    public String index;
    public int score;
    public String selectOption;

    public HostedQuestion() {
        //For Firebase
    }

    public HostedQuestion(String index, String question, String correctOption, ArrayList<String> options, String selectedOption, int score) {
        this.index = index;
        this.correctOption = correctOption;
        this.options = options;
        this.question = question;
        this.selectOption = selectedOption;
        this.score = score;
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

    public int getScore() {
        return score;
    }

    public String getSelectOption() {
        return selectOption;
    }
}
