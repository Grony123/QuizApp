package com.bygn.login;

public class Question_Options {
    String Question;
    String optionA;
    String optionB;
    String optionC;
    String optionD;
    String CorrectOption;
    String ImageUrl;
    int Id;

    public Question_Options(){
    }

    public Question_Options(String question, String optionA, String optionB, String optionC, String optionD, String correctOption, String imageUrl, int id) {
        Question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        CorrectOption = correctOption;
        ImageUrl = imageUrl;
        Id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return CorrectOption;
    }

    public void setCorrectOption(String correctOption) {
        CorrectOption = correctOption;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
