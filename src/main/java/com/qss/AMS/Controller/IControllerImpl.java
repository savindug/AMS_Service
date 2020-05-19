package com.qss.AMS.Controller;

import com.qss.AMS.DBHandler.DBHandler;
import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;

import java.util.ArrayList;

public class IControllerImpl implements IController{

    DBHandler dbHandler = new DBHandler();

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

}
