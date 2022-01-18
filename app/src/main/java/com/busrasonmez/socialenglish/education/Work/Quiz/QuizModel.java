package com.busrasonmez.socialenglish.education.Work.Quiz;

public class QuizModel {
    String english_sentence;
    String turkish_meaning;
    String wrong_words;

    public QuizModel(String english_sentence, String turkish_meaning, String wrong_words) {
        this.english_sentence = english_sentence;
        this.turkish_meaning = turkish_meaning;
        this.wrong_words = wrong_words;
    }

    public String getEnglish_sentence() {
        return english_sentence;
    }

    public void setEnglish_sentence(String english_sentence) {
        this.english_sentence = english_sentence;
    }

    public String getTurkish_meaning() {
        return turkish_meaning;
    }

    public void setTurkish_meaning(String turkish_meaning) {
        this.turkish_meaning = turkish_meaning;
    }

    public String getWrong_words() {
        return wrong_words;
    }

    public void setWrong_words(String wrong_words) {
        this.wrong_words = wrong_words;
    }
}
