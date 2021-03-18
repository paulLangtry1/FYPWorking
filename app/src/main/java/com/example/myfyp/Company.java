package com.example.myfyp;

public class Company
{

    String password;
    String companyName;
    String email;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    String sector;

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    String isAdmin;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    String phoneNo;

    public String getPpurl() {
        return ppurl;
    }

    public void setPpurl(String ppurl) {
        this.ppurl = ppurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String ppurl;
    String address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public Company(String email, String password, String companyName,String ppurl,String address,String phoneNo,String isAdmin,String sector) {
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.ppurl = ppurl;
        this.address = address;
        this.phoneNo = phoneNo;
        this.isAdmin = isAdmin;
        this.sector = sector;
    }
    public Company() {

    }






}

