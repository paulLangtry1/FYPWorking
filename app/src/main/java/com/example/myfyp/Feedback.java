package com.example.myfyp;

public class Feedback
{
    String experience;
    String pay;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    String sector;

    public Feedback() {

    }

    public String getFeedbackid() {
        return feedbackid;
    }

    public void setFeedbackid(String feedbackid) {
        this.feedbackid = feedbackid;
    }

    String feedbackid;

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    String useruid;
    String companyName;
    String position;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    String companyID;

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Feedback(String experience, String pay,String description, String useruid,String companyName,String position,String feedbackid,String companyID,String sector)
    {
        this.experience = experience;
        this.pay = pay;
        this.description = description;
        this.useruid = useruid;
        this.companyName = companyName;
        this.position = position;
        this.feedbackid = feedbackid;
        this.companyID = companyID;
        this.sector = sector;

    }

    String description;
}
