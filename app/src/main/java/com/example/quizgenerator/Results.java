package com.example.quizgenerator;

public class Results {
    public String score;
    public String quizName;
    public String date;

    public Results() {
        //For Firebase
    }

    public Results(String score, String date, String quizName) {
        this.quizName = quizName;
        this.date = date;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getDate() {
        return date;
    }
}
