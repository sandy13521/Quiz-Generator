package com.example.quizgenerator;

import java.util.ArrayList;

public class Questions {

    public String correctOption;
    public ArrayList<String> options;
    public String question;
    public String index;
    public String selectedOption;

    public Questions() {
        //For Firebase
    }

    public Questions(String index, String correctOption, String question, ArrayList<String> options) {
        this.index = index;
        this.correctOption = correctOption;
        this.options = options;
        this.question = question;
    }


    public Questions(String index, String correctOption, String question, ArrayList<String> options, String selectedOption) {
        this.index = index;
        this.correctOption = correctOption;
        this.options = options;
        this.question = question;
        this.selectedOption = selectedOption;
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

    public String getSelectedOption() {
        return selectedOption;
    }
}
