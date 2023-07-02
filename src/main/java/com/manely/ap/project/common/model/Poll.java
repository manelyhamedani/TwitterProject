package com.manely.ap.project.common.model;

public class Poll {
    private int id;
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int choice1Count;
    private int choice2Count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public int getChoice1Count() {
        return choice1Count;
    }

    public void setChoice1Count(int choice1Count) {
        this.choice1Count = choice1Count;
    }

    public int getChoice2Count() {
        return choice2Count;
    }

    public void setChoice2Count(int choice2Count) {
        this.choice2Count = choice2Count;
    }

    public int getChoice3Count() {
        return choice3Count;
    }

    public void setChoice3Count(int choice3Count) {
        this.choice3Count = choice3Count;
    }

    public int getChoice4Count() {
        return choice4Count;
    }

    public void setChoice4Count(int choice4Count) {
        this.choice4Count = choice4Count;
    }

    private int choice3Count;
    private int choice4Count;


}
