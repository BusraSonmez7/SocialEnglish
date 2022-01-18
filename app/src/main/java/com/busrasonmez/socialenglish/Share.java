package com.busrasonmez.socialenglish;

public class Share {
    private String username;
    private String profile;
    private String shareimg;
    private String comment;
    private String email;
    private String shareID;

    public Share(String username, String profile, String shareimg, String comment,String email, String shareID) {
        this.username = username;
        this.profile = profile;
        this.shareimg = shareimg;
        this.comment = comment;
        this.email = email;
        this.shareID = shareID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getShareimg() {
        return shareimg;
    }

    public void setShareimg(String shareimg) {
        this.shareimg = shareimg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShareID() {
        return shareID;
    }

    public void setShareID(String shareID) {
        this.shareID = shareID;
    }
}
