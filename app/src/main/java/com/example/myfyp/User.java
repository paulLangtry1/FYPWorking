package com.example.myfyp;

public class User
{

    private String email;
    private String password;
    private String username;
    private String ppurl;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;



    public String getHasjobaccepted() {
        return hasjobaccepted;
    }

    public void setHasjobaccepted(String hasjobaccepted) {
        this.hasjobaccepted = hasjobaccepted;
    }

    private String hasjobaccepted;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getSafepassurl() {
        return safepassurl;
    }

    public void setSafepassurl(String safepassurl) {
        this.safepassurl = safepassurl;
    }

    private String safepassurl;

    public String getPpurl() {
        return ppurl;
    }

    public void setPpurl(String ppurl) {
        this.ppurl = ppurl;
    }

    public String getRefurl() {
        return refurl;
    }

    public void setRefurl(String refurl) {
        this.refurl = refurl;
    }

    private String refurl;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    private String phoneNo;

    public String getUsername()
    {
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


    public User()
    {

    }


    public User(String email, String password, String username,String phoneNo, String ppurl, String refurl,String safepassurl,String status,String hasjobaccepted,String userID)
    {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNo = phoneNo;
        this.ppurl = ppurl;
        this.refurl = refurl;
        this.safepassurl = safepassurl;
        this.status = status;
        this.hasjobaccepted = hasjobaccepted;
        this.userID = userID;

    }
}
