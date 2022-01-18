package com.busrasonmez.socialenglish.social_media.Messages;

public class MessageModel {

    String sender_username;
    String buyer_username;
    String message;
    String image;
    String date;
    String profile;
    String username;

    public MessageModel() {
    }

    public MessageModel(String x, String y, String message, String image, String date, boolean what) {
        if(what){
            this.sender_username = x;
            this.buyer_username = y;
            this.message = message;
            this.image = image;
            this.date = date;
        }
        else{
            this.message = message;
            this.date = date;
            this.profile = image;
            this.username = x;
            this.buyer_username = y;
        }

    }

    public MessageModel(String buyer_username, String message, String date) {
        this.buyer_username = buyer_username;
        this.message = message;
        this.date = date;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public String getBuyer_username() {
        return buyer_username;
    }

    public void setBuyer_username(String buyer_username) {
        this.buyer_username = buyer_username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
