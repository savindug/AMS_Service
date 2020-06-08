package com.qss.AMS.Entity;

public class Attendance {

    private String uId;
    private String uName;
    private String depart;
    private String place;
    private String verifyMode;
    private String remark;
    private String attTime;
    private String clockIn;
    private String clockOut;
    private int otHrs;
    private String date;
    private String branchname;

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }

    public int getOtHrs() {
        return otHrs;
    }

    public void setOtHrs(int otHrs) {
        this.otHrs = otHrs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setVerifyMode(String verifyMode) {
        this.verifyMode = verifyMode;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAttTime(String attTime) {
        this.attTime = attTime;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getuId() {
        return uId;
    }

    public String getuName() {
        return uName;
    }

    public String getDepart() {
        return depart;
    }

    public String getPlace() {
        return place;
    }

    public String getVerifyMode() {
        return verifyMode;
    }

    public String getRemark() {
        return remark;
    }

    public String getAttTime() {
        return attTime;
    }

    public String getBranchname() { return branchname; }

    @Override
    public String toString() {
        return "Attendance{" +
                "uId='" + uId + '\'' +
                ", uName='" + uName + '\'' +
                ", depart='" + depart + '\'' +
                ", place='" + place + '\'' +
                ", verifyMode='" + verifyMode + '\'' +
                ", remark='" + remark + '\'' +
                ", attTime='" + attTime + '\'' +
                ", clockIn='" + clockIn + '\'' +
                ", clockOut='" + clockOut + '\'' +
                ", otHrs=" + otHrs +
                ", date='" + date + '\'' +
                '}';
    }

    public String attToString() {
        return "{\"userId\":"+  "\"" + uId + "\""
                + ",\"userName\" :"+ "\"" +uName + "\""
                + ",\"clock\":"+ "\"" +attTime + "\""
                + ", \"remarks\":" + "\"" +remark + "\""
                + ", \"branchName\":" + "\"" +branchname + "\"" +"}";

    }

    public String otToString() {
        return "{\"userId\":"+  "\"" + uId + "\""
                + ",\"userName\" :"+ "\"" +uName + "\""
                + ",\"clockIn\":"+ "\"" +clockIn + "\""
                + ",\"clockOut\":"+ "\"" +clockOut+ "\""
                + ", \"dateo\":" + "\"" +date+ "\""
                + ", \"otHours\":" + "\"" +otHrs+ "\""
                + ", \"branchName\":" + "\"" +branchname + "\"" +"}";

    }


}
