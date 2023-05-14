package com.project.seedle.ModelClassess;

public class ModelFavoriteTextStatus {

    private String username, useremail,status,profileurl,currentdatetime;

    public ModelFavoriteTextStatus() {
    }

    public ModelFavoriteTextStatus(String username, String useremail, String status, String profileurl, String currentdatetime) {
        this.username = username;
        this.useremail = useremail;
        this.status = status;
        this.profileurl = profileurl;
        this.currentdatetime = currentdatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
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

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentdatetime) {
        this.currentdatetime = currentdatetime;
    }
}
