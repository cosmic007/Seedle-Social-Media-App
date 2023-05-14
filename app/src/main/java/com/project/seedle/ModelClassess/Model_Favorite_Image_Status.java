package com.project.seedle.ModelClassess;

public class Model_Favorite_Image_Status {

    String username,statusurl,status,profileurl,currentdatetime;

    public Model_Favorite_Image_Status() {
    }

    public Model_Favorite_Image_Status(String username, String statusurl, String status, String profileurl, String currentdatetime) {
        this.username = username;
        this.statusurl = statusurl;
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

    public String getStatusurl() {
        return statusurl;
    }

    public void setStatusurl(String statusurl) {
        this.statusurl = statusurl;
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
