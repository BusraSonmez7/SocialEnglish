package com.busrasonmez.socialenglish.education.Work.Word;

public class WorkWordModel {

    String subject;
    String word;
    String email;
    String english_meaning;
    String turkish_meaning;
    String level;
    String group;
    String image;

    public WorkWordModel(String subject, String word, String email, String english_meaning, String turkish_meaning, String level, String group, String image) {
        this.subject = subject;
        this.word = word;
        this.email = email;
        this.english_meaning = english_meaning;
        this.turkish_meaning = turkish_meaning;
        this.level = level;
        this.group = group;
        this.image = image;
    }

    public WorkWordModel(String subject, String word, String email, String image) {
        this.subject = subject;
        this.word = word;
        this.email = email;
        this.image = image;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnglish_meaning() {
        return english_meaning;
    }

    public void setEnglish_meaning(String english_meaning) {
        this.english_meaning = english_meaning;
    }

    public String getTurkish_meaning() {
        return turkish_meaning;
    }

    public void setTurkish_meaning(String turkish_meaning) {
        this.turkish_meaning = turkish_meaning;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
