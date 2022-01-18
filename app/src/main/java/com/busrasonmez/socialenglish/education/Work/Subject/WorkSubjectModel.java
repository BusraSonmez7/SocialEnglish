package com.busrasonmez.socialenglish.education.Work.Subject;

public class WorkSubjectModel {
    String subject;
    String image;
    String email;


    public WorkSubjectModel(String subject, String image, String email) {
        this.subject = subject;
        this.image = image;
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
