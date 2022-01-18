package com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons;

public class Person {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String about;
    private String image;

    public Person(){

    }

    public Person(String name, String surname, String username, String email, String password, String about, String image) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.about = about;
        this.image = image;
    }

    public Person(String username, String about, String image) {
        this.username = username;
        this.about = about;
        this.image = image;
    }



    public Person(String email, String image, int pas) {
        if(pas == 1){
            this.email = email;
            this.image = image;
        }
        else{
            this.username = email;
            this.image = image;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
