package com.project.seedle.ModelClassess;

public class Model_TextStatus {

    private String username,currentdatetime,status,profileurl;
    private int nooflove,noofhaha,noofsad,noofcomments;

    public Model_TextStatus() {
    }

    public Model_TextStatus(String username, String currentdatetime, String status, String profileurl, int nooflove, int noofhaha, int noofsad, int noofcomments) {
        this.username = username;
        this.currentdatetime = currentdatetime;
        this.status = status;
        this.profileurl = profileurl;
        this.nooflove = nooflove;
        this.noofhaha = noofhaha;
        this.noofsad = noofsad;
        this.noofcomments = noofcomments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentdatetime) {
        this.currentdatetime = currentdatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public int getNooflove() {
        return nooflove;
    }

    public void setNooflove(int nooflove) {
        this.nooflove = nooflove;
    }

    public int getNoofhaha() {
        return noofhaha;
    }

    public void setNoofhaha(int noofhaha) {
        this.noofhaha = noofhaha;
    }

    public int getNoofsad() {
        return noofsad;
    }

    public void setNoofsad(int noofsad) {
        this.noofsad = noofsad;
    }

    public int getNoofcomments() {
        return noofcomments;
    }

    public void setNoofcomments(int noofcomments) {
        this.noofcomments = noofcomments;
    }
}
