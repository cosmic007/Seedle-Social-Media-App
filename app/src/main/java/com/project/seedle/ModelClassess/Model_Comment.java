package com.project.seedle.ModelClassess;

public class Model_Comment {
    private String comment,commentperson,currentdatetime,profilepicurl,username;

    public Model_Comment() {

    }

    public Model_Comment(String comment, String commentperson, String currentdatetime, String profilepicurl, String username) {
        this.comment = comment;
        this.commentperson = commentperson;
        this.currentdatetime = currentdatetime;
        this.profilepicurl = profilepicurl;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentperson() {
        return commentperson;
    }

    public void setCommentperson(String commentperson) {
        this.commentperson = commentperson;
    }

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentdatetime) {
        this.currentdatetime = currentdatetime;
    }

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
