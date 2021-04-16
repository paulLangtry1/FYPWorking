package com.example.myfyp;

public class Contract
{
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    String position;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    String sector;

    public Contract(String position, String address,String county, String startdate, String enddate, String starttime, String endtime,String userID,String contractID,String companyName,String companyID,String sector) {
        this.position = position;
        this.address = address;
        this.county = county;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.userID = userID;
        this.contractID=contractID;
        this.companyName = companyName;
        this.companyID = companyID;
        this.sector = sector;

    }

    String address;




    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    String county;
    String startdate;
    String enddate;
    String starttime;
    String endtime;
    String contractID;
    String userID;

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    String companyID;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    String companyName;

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }





    public Contract()
    {

    }
}
