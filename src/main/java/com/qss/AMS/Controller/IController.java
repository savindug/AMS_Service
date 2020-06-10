package com.qss.AMS.Controller;

import com.qss.AMS.Entity.Attendance;
import com.qss.AMS.Entity.Users;

import java.util.ArrayList;

public interface IController {

    String hello();

    ArrayList<Users> getEmployees();

    ArrayList<Attendance> getAllAttendance();

    ArrayList<Users> getAllLeaves();

    ArrayList<Attendance> getAttendanceByDuration(String from, String to);

    ArrayList<Users> getLeavesByDuration(String from, String to);

    ArrayList<Attendance> getOTByDuration(String from, String to);


    /*Admin Side---------------------------------------------------------------*/

    ArrayList<Users> getEmployeesAdmin(String branchname);

    ArrayList<Attendance> getAttendanceByDurationAdmin(String from, String to, String branchname);

    ArrayList<Users> getLeavesByDurationAdmin(String from, String to , String branchname);

    ArrayList<Attendance> getOTByDurationAdmin(String from, String to , String branchname);
}
