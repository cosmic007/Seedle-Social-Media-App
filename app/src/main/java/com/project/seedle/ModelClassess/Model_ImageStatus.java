package com.project.seedle.ModelClassess;

public class Model_ImageStatus {
    private String username,currentdatetime,status,profileurl,statusimageurl,useremail;

    public Model_ImageStatus(String useremail) {
        this.useremail = useremail;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    private int nooflove,noofcomments;

    public Model_ImageStatus() {
    }

    public Model_ImageStatus(String username, String currentdatetime, String status, String profileurl, String statusimageurl, int nooflove, int noofcomments) {
        this.username = username;
        this.currentdatetime = currentdatetime;
        this.status = status;
        this.profileurl = profileurl;
        this.statusimageurl = statusimageurl;
        this.nooflove = nooflove;
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

    public String getStatusimageurl() {
        return statusimageurl;
    }

    public void setStatusimageurl(String statusimageurl) {
        this.statusimageurl = statusimageurl;
    }

    public int getNooflove() {
        return nooflove;
    }

    public void setNooflove(int nooflove) {
        this.nooflove = nooflove;
    }

    public int getNoofcomments() {
        return noofcomments;
    }

    public void setNoofcomments(int noofcomments) {
        this.noofcomments = noofcomments;
    }
}
