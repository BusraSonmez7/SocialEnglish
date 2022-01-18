package com.busrasonmez.socialenglish.education.Work.Sentence;

public class WorkSentenceModel {

    String subject;
    String sentence_header;
    String email;
    String sentence;

    public WorkSentenceModel(String subject, String sentence_header, String email, String sentence) {
        this.subject = subject;
        this.sentence_header = sentence_header;
        this.email = email;
        this.sentence = sentence;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentence_header() {
        return sentence_header;
    }

    public void setSentence_header(String sentence_header) {
        this.sentence_header = sentence_header;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
