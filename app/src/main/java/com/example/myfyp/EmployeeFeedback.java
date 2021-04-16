package com.example.myfyp;

public class EmployeeFeedback
{
    public String getOverallrating() {
        return overallrating;
    }

    public void setOverallrating(String overallrating) {
        this.overallrating = overallrating;
    }

    public String getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(String punctuality) {
        this.punctuality = punctuality;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getEmpfeedbackid() {
        return empfeedbackid;
    }

    public void setEmpfeedbackid(String empfeedbackid) {
        this.empfeedbackid = empfeedbackid;
    }

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    String overallrating;
    String punctuality;
    String communication;
    String empfeedbackid;
    String employeeid;

    public EmployeeFeedback(String overallrating, String punctuality,String communication, String empfeedbackid,String employeeid)
    {
        this.overallrating = overallrating;
        this.punctuality = punctuality;
        this.communication = communication;
        this.empfeedbackid = empfeedbackid;
        this.employeeid = employeeid;

    }
    public EmployeeFeedback()
    {


    }
}
