package com.qss.AMS.Controller;

import com.qss.AMS.DBHandler.DBHandler;
import com.qss.AMS.DBHandler.onlineDownloadHandler;
import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;

import java.util.ArrayList;

public class IControllerImpl implements IController{

    DBHandler dbHandler = new DBHandler();
    onlineDownloadHandler downloadHandler = new  onlineDownloadHandler();

    @Override
    public String hello() {
        return "Hello";
    }

    @Override
    public ArrayList<Users> getEmployees() {
        return dbHandler.getEmployees();
    }

    @Override
    public ArrayList<Attendance> getAllAttendance() {
        return dbHandler.getAllAttendance();
    }

    @Override
    public ArrayList<Users> getAllLeaves() {
        return dbHandler.getAllLeaves();
    }

    @Override
    public ArrayList<Attendance> getAttendanceByDuration(String from, String to) {
        return dbHandler.getAttendanceByDuration(from, to);
    }

    @Override
    public ArrayList<Users> getLeavesByDuration(String from, String to) {
        return dbHandler.getLeavesByDuration(from, to);
    }

    @Override
    public ArrayList<Attendance> getOTByDuration(String from, String to) {
        return dbHandler.getOTByDuration(from, to);
    }



    /*Admin Side--------------------------------------*/


    public ArrayList<Users> getEmployeesAdmin(String branchname) {
        return downloadHandler.downloadEmployees(branchname);
    }

    @Override
    public ArrayList<Attendance> getAttendanceByDurationAdmin(String from, String to, String branchname) {
        return downloadHandler.downloadAttendance(from, to, branchname);
    }

    @Override
    public ArrayList<Users> getLeavesByDurationAdmin(String from, String to, String branchname) {
        return downloadHandler.downloadLeaves(from, to, branchname);
    }

    @Override
    public ArrayList<Attendance> getOTByDurationAdmin(String from, String to, String branchname) {
        return downloadHandler.downloadOt(from, to, branchname);
    }
}
