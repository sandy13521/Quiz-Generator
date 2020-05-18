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

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }
}
